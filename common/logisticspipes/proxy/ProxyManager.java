package logisticspipes.proxy;

import java.util.ArrayList;
import java.util.List;

import logisticspipes.LogisticsPipes;
import logisticspipes.interfaces.IHUDConfig;
import logisticspipes.pipes.basic.CoreRoutedPipe;
import logisticspipes.pipes.basic.LogisticsTileGenericPipe;
import logisticspipes.proxy.bettersign.BetterSignProxy;
import logisticspipes.proxy.bs.BetterStorageProxy;
import logisticspipes.proxy.cc.CCProxy;
import logisticspipes.proxy.cc.CCTurtleProxy;
import logisticspipes.proxy.enderio.EnderIOProxy;
import logisticspipes.proxy.factorization.FactorizationProxy;
import logisticspipes.proxy.forestry.ForestryProxy;
import logisticspipes.proxy.ic2.IC2Proxy;
import logisticspipes.proxy.interfaces.IBetterSignProxy;
import logisticspipes.proxy.interfaces.IBetterStorageProxy;
import logisticspipes.proxy.interfaces.ICCProxy;
import logisticspipes.proxy.interfaces.IEnderIOProxy;
import logisticspipes.proxy.interfaces.IFactorizationProxy;
import logisticspipes.proxy.interfaces.IForestryProxy;
import logisticspipes.proxy.interfaces.IIC2Proxy;
import logisticspipes.proxy.interfaces.IModularPowersuitsProxy;
import logisticspipes.proxy.interfaces.INEIProxy;
import logisticspipes.proxy.interfaces.IThaumCraftProxy;
import logisticspipes.proxy.interfaces.IThermalExpansionProxy;
import logisticspipes.proxy.mps.ModularPowersuitsProxy;
import logisticspipes.proxy.nei.NEIProxy;
import logisticspipes.proxy.te.ThermalExpansionProxy;
import logisticspipes.proxy.thaumcraft.ThaumCraftProxy;
import logisticspipes.utils.ModStatusHelper;
import logisticspipes.utils.item.ItemIdentifier;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelSign;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.transport.TravelingItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ProxyManager {

	public static void load() {
		if(ModStatusHelper.isModLoaded("Forestry")) {
			SimpleServiceLocator.setForestryProxy(new ForestryProxy());
			LogisticsPipes.log.info("Loaded ForestryProxy");
		} else {
			//DummyProxy
			SimpleServiceLocator.setForestryProxy(new IForestryProxy() {
				@Override public boolean isBee(ItemStack item) {return false;}
				@Override public boolean isBee(ItemIdentifier item) {return false;}
				@Override public boolean isAnalysedBee(ItemStack item) {return false;}
				@Override public boolean isAnalysedBee(ItemIdentifier item) {return false;}
				@Override public boolean isTileAnalyser(TileEntity tile) {return false;}
				@Override public boolean forestryEnabled() {return false;}
				@Override public boolean isKnownAlleleId(String uid, World world) {return false;}
				@Override public String getAlleleName(String uid) {return "";}
				@Override public String getFirstAlleleId(ItemStack bee) {return "";}
				@Override public String getSecondAlleleId(ItemStack bee) {return "";}
				@Override public boolean isDrone(ItemStack bee) {return false;}
				@Override public boolean isFlyer(ItemStack bee) {return false;}
				@Override public boolean isPrincess(ItemStack bee) {return false;}
				@Override public boolean isQueen(ItemStack bee) {return false;}
				@Override public boolean isPurebred(ItemStack bee) {return false;}
				@Override public boolean isNocturnal(ItemStack bee) {return false;}
				@Override public boolean isPureNocturnal(ItemStack bee) {return false;}
				@Override public boolean isPureFlyer(ItemStack bee) {return false;}
				@Override public boolean isCave(ItemStack bee) {return false;}
				@Override public boolean isPureCave(ItemStack bee) {return false;}
				@Override public String getForestryTranslation(String input) {return input.substring(input.lastIndexOf(".") + 1).toLowerCase().replace("_", " ");}
				@Override @SideOnly(Side.CLIENT) public Icon getIconIndexForAlleleId(String id, int phase) {return null;}
				@Override @SideOnly(Side.CLIENT) public int getColorForAlleleId(String id, int phase) {return 0;}
				@Override @SideOnly(Side.CLIENT) public int getRenderPassesForAlleleId(String id) {return 0;}
				@Override public void addCraftingRecipes() {}
				@Override public String getNextAlleleId(String uid, World world) {return "";}
				@Override public String getPrevAlleleId(String uid, World world) {return "";}
				@Override @SideOnly(Side.CLIENT) public Icon getIconFromTextureManager(String name) {return null;}
			});
			LogisticsPipes.log.info("Loaded Forestry DummyProxy");
		}
		if(ModStatusHelper.isModLoaded("IC2")) {
			SimpleServiceLocator.setElectricItemProxy(new IC2Proxy());
			LogisticsPipes.log.info("Loaded IC2Proxy");
		} else {
			//DummyProxy
			SimpleServiceLocator.setElectricItemProxy(new IIC2Proxy() {
				@Override public boolean isElectricItem(ItemStack stack) {return false;}
				@Override public boolean isSimilarElectricItem(ItemStack stack, ItemStack template) {return false;}
				@Override public boolean isFullyCharged(ItemStack stack) {return false;}
				@Override public boolean isFullyDischarged(ItemStack stack) {return false;}
				@Override public boolean isPartiallyCharged(ItemStack stack) {return false;}
				@Override public void addCraftingRecipes() {}
				@Override public boolean hasIC2() {return false;}
				@Override public void registerToEneryNet(TileEntity tile) {}
				@Override public void unregisterToEneryNet(TileEntity tile) {}
			});
			LogisticsPipes.log.info("Loaded IC2 DummyProxy");
		}
		if(ModStatusHelper.isModLoaded("ComputerCraft@1.5")) {
			if(ModStatusHelper.isModLoaded("CCTurtle@1.5")) {
				SimpleServiceLocator.setCCProxy(new CCTurtleProxy());
				LogisticsPipes.log.info("Loaded CCTurtleProxy");
			} else {
				SimpleServiceLocator.setCCProxy(new CCProxy());
				LogisticsPipes.log.info("Loaded CCProxy");
			}
		} else {
			//DummyProxy
			SimpleServiceLocator.setCCProxy(new ICCProxy() {
				@Override public boolean isTurtle(TileEntity tile) {return false;}
				@Override public boolean isComputer(TileEntity tile) {return false;}
				@Override public boolean isCC() {return false;}
				@Override public ForgeDirection getOrientation(Object computer, TileEntity tile) {return ForgeDirection.UNKNOWN;}
				@Override public boolean isLuaThread(Thread thread) {return false;}
				@Override public void queueEvent(String event, Object[] arguments, LogisticsTileGenericPipe logisticsTileGenericPipe) {}
				@Override public void setTurtleConnect(boolean flag, LogisticsTileGenericPipe logisticsTileGenericPipe) {}
				@Override public boolean getTurtleConnect(LogisticsTileGenericPipe logisticsTileGenericPipe) {return false;}
				@Override public int getLastCCID(LogisticsTileGenericPipe logisticsTileGenericPipe) {return 0;}
			});
			LogisticsPipes.log.info("Loaded CC DummyProxy");
		}
		
		if(ModStatusHelper.isModLoaded("Thaumcraft")) {
			SimpleServiceLocator.setThaumCraftProxy(new ThaumCraftProxy());
			LogisticsPipes.log.info("Loaded Thaumcraft Proxy");
		} else {
			SimpleServiceLocator.setThaumCraftProxy(new IThaumCraftProxy() {
				@Override public boolean isScannedObject(ItemStack stack, String playerName) {return false;}
				@Override public List<String> getListOfTagsForStack(ItemStack stack) {return null;}
				@Override public void renderAspectsDown(ItemStack item, int x, int y, GuiScreen gui) {}
				@Override public void renderAspectsInGrid(List<String> eTags, int x, int y, int legnth, int width, GuiScreen gui) {}
				@Override public void addCraftingRecipes() {}
			});
			LogisticsPipes.log.info("Loaded Thaumcraft DummyProxy");
		}
		
		if(ModStatusHelper.isModLoaded("ThermalExpansion")) {
			SimpleServiceLocator.setThermalExpansionProxy(new ThermalExpansionProxy());
			LogisticsPipes.log.info("Loaded ThermalExpansion Proxy");
		} else {
			SimpleServiceLocator.setThermalExpansionProxy(new IThermalExpansionProxy() {
				@Override public boolean isTesseract(TileEntity tile) {return false;}
				@Override public boolean isTE() {return false;}
				@Override public List<TileEntity> getConnectedTesseracts(TileEntity tile) {return new ArrayList<TileEntity>(0);}
				@Override public boolean isItemConduit(TileEntity tile) {return false;}
				@Override public void handleLPInternalConduitChunkUnload(LogisticsTileGenericPipe pipe) {}
				@Override public void handleLPInternalConduitRemove(LogisticsTileGenericPipe pipe) {}
				@Override public void handleLPInternalConduitNeighborChange(LogisticsTileGenericPipe logisticsTileGenericPipe) {}
				@Override public void handleLPInternalConduitUpdate(LogisticsTileGenericPipe pipe) {}
				@Override public boolean insertIntoConduit(TravelingItem arrivingItem, TileEntity tile, CoreRoutedPipe pipe) {return false;}
				@Override public boolean isSideFree(TileEntity tile, int side) {return false;}
			});
			LogisticsPipes.log.info("Loaded ThermalExpansion DummyProxy");
		}
		
		if(ModStatusHelper.isModLoaded("betterstorage")) {
			SimpleServiceLocator.setBetterStorageProxy(new BetterStorageProxy());
			LogisticsPipes.log.info("Loaded BetterStorage Proxy");
		} else {
			SimpleServiceLocator.setBetterStorageProxy(new IBetterStorageProxy() {
				@Override public boolean isBetterStorageCrate(TileEntity tile) {return false;}
			});
			LogisticsPipes.log.info("Loaded BetterStorage DummyProxy");
		}
		
		if(ModStatusHelper.isModLoaded("NotEnoughItems")) {
			SimpleServiceLocator.setNEIProxy(new NEIProxy());
			LogisticsPipes.log.info("Loaded NotEnoughItems Proxy");
		} else {
			SimpleServiceLocator.setNEIProxy(new INEIProxy() {
				@Override public int getWidthForList(List<String> data, FontRenderer fontRenderer) {return 0;}
				@Override public List<String> getInfoForPosition(World world, EntityPlayer player, MovingObjectPosition objectMouseOver) {return new ArrayList<String>(0);}
				@Override public ItemStack getItemForPosition(World world, EntityPlayer player, MovingObjectPosition objectMouseOver) {return null;}
			});
			LogisticsPipes.log.info("Loaded NotEnoughItems DummyProxy");
		}

		if(ModStatusHelper.isModLoaded("powersuits")) {
			SimpleServiceLocator.setMPSProxy(new ModularPowersuitsProxy());
			LogisticsPipes.log.info("Loaded Modular Powersuits Proxy");
		} else {
			SimpleServiceLocator.setMPSProxy(new IModularPowersuitsProxy() {
				@Override public boolean isMPSHelm(ItemStack stack) {return false;}
				@Override public void initModules() {}
				@Override public boolean hasActiveHUDModule(ItemStack stack) {return false;}
				@Override public IHUDConfig getConfigFor(ItemStack itemStack) {
					return new IHUDConfig() {
						@Override public boolean isHUDSatellite() {return false;}
						@Override public boolean isHUDProvider() {return false;}
						@Override public boolean isHUDPowerJunction() {return false;}
						@Override public boolean isHUDInvSysCon() {return false;}
						@Override public boolean isHUDCrafting() {return false;}
						@Override public boolean isHUDChassie() {return false;}
						@Override public void setHUDChassie(boolean state) {}
						@Override public void setHUDCrafting(boolean state) {}
						@Override public void setHUDInvSysCon(boolean state) {}
						@Override public void setHUDPowerJunction(boolean state) {}
						@Override public void setHUDProvider(boolean state) {}
						@Override public void setHUDSatellite(boolean state) {}
					};
				}
				@Override public boolean isMPSHand(ItemStack stack) {return false;}
				@Override public boolean hasHelmHUDInstalled(ItemStack stack) {return false;}
			});
			LogisticsPipes.log.info("Loaded Modular Powersuits DummyProxy");
		}
		
		if(ModStatusHelper.isModLoaded("factorization")) {
			SimpleServiceLocator.setFactorizationProxy(new FactorizationProxy());
			LogisticsPipes.log.info("Loaded Factorization Proxy");
		} else {
			SimpleServiceLocator.setFactorizationProxy(new IFactorizationProxy() {
				@Override public boolean isBarral(TileEntity tile) {return false;}
			});
			LogisticsPipes.log.info("Loaded Factorization DummyProxy");
		}
		
		if(ModStatusHelper.isModLoaded("BetterSignsMod") && MainProxy.isClient()) {
			SimpleServiceLocator.setBetterSignProxy(new BetterSignProxy());
			LogisticsPipes.log.info("Loaded BetterSign Proxy");
		} else {
			SimpleServiceLocator.setBetterSignProxy(new IBetterSignProxy() {
				@Override public void hideSignSticks(ModelSign model) {
					model.signStick.showModel = false;
				}
			});
			LogisticsPipes.log.info("Loaded BetterSign DummyProxy");
		}

		if(ModStatusHelper.isModLoaded("EnderIO")) {
			SimpleServiceLocator.setEnderIOProxy(new EnderIOProxy());
			LogisticsPipes.log.info("Loaded EnderIO Proxy");
		} else {
			SimpleServiceLocator.setEnderIOProxy(new IEnderIOProxy() {
				@Override public boolean isSendAndReceive(TileEntity tile) {return false;}
				@Override public boolean isHyperCube(TileEntity tile) {return false;}
				@Override public List<TileEntity> getConnectedHyperCubes(TileEntity tile) {return new ArrayList<TileEntity>(0);}
				@Override public boolean isEnderIO() {return false;}
			});
			LogisticsPipes.log.info("Loaded EnderIO DummyProxy");
		}
		
	}
}
