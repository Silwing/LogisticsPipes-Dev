package logisticspipes.network.packets.module;

import logisticspipes.modules.ModuleProvider;
import logisticspipes.network.PacketHandler;
import logisticspipes.network.abstractpackets.IntegerCoordinatesPacket;
import logisticspipes.network.abstractpackets.ModernPacket;
import logisticspipes.network.packets.modules.ProviderModuleInclude;
import logisticspipes.pipes.basic.CoreRoutedPipe;
import logisticspipes.pipes.basic.LogisticsTileGenericPipe;
import logisticspipes.proxy.MainProxy;
import logisticspipes.utils.gui.DummyModuleContainer;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.Player;

public class ProviderModuleIncludePacket extends IntegerCoordinatesPacket {

	public ProviderModuleIncludePacket(int id) {
		super(id);
	}

	@Override
	public ModernPacket template() {
		return new ProviderModuleIncludePacket(getId());
	}

	@Override
	public void processPacket(EntityPlayer player) {
		final int slot = getInteger();
		if(slot < 0) {
			if(player.openContainer instanceof DummyModuleContainer) {
				DummyModuleContainer dummy = (DummyModuleContainer) player.openContainer;
				if(dummy.getModule() instanceof ModuleProvider) {
					final ModuleProvider module = (ModuleProvider) dummy.getModule();
					module.setFilterExcluded(!module.isExcludeFilter());
					MainProxy.sendPacketToPlayer(PacketHandler.getPacket(ProviderModuleInclude.class).setInteger(module.isExcludeFilter() ? 1 : 0).setPosX(getPosX()).setPosY(getPosY()).setPosZ(getPosZ()), (Player) player);
				}
			}
			return;
		}
		final LogisticsTileGenericPipe pipe = this.getPipe(player.worldObj);
		if(pipe == null) {
			return;
		}
		if( !(pipe.pipe instanceof CoreRoutedPipe)) {
			return;
		}
		final CoreRoutedPipe piperouted = (CoreRoutedPipe) pipe.pipe;
		if(piperouted.getLogisticsModule() == null) {
			return;
		}
		if(slot <= 0) {
			if(piperouted.getLogisticsModule() instanceof ModuleProvider) {
				final ModuleProvider module = (ModuleProvider) piperouted.getLogisticsModule();
				module.setFilterExcluded( !module.isExcludeFilter());
				MainProxy.sendPacketToPlayer(PacketHandler.getPacket(ProviderModuleInclude.class).setInteger(module.isExcludeFilter() ? 1 : 0).setPosX(getPosX()).setPosY(getPosY()).setPosZ(getPosZ()), (Player) player);
				return;
			}
		} else {
			if(piperouted.getLogisticsModule().getSubModule(slot - 1) instanceof ModuleProvider) {
				final ModuleProvider module = (ModuleProvider) piperouted.getLogisticsModule().getSubModule(slot - 1);
				module.setFilterExcluded( !module.isExcludeFilter());
				MainProxy.sendPacketToPlayer(PacketHandler.getPacket(ProviderModuleInclude.class).setInteger((module.isExcludeFilter() ? 1 : 0)).setPosX(getPosX()).setPosY(getPosY()).setPosZ(getPosZ()), (Player) player);
				return;
			}
		}
	}
}

