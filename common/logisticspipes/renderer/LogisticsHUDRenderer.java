package logisticspipes.renderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

import logisticspipes.LogisticsPipes;
import logisticspipes.interfaces.IHeadUpDisplayRendererProvider;
import logisticspipes.main.CoreRoutedPipe;
import logisticspipes.main.SimpleServiceLocator;
import logisticspipes.proxy.MainProxy;
import logisticspipes.routing.IRouter;
import logisticspipes.utils.MathVector;
import logisticspipes.utils.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ScaledResolution;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;

public class LogisticsHUDRenderer {

	private LinkedList<IHeadUpDisplayRendererProvider> list = new LinkedList<IHeadUpDisplayRendererProvider>();
	private double lastXPos = 0;
	private double lastYPos = 0;
	private double lastZPos = 0;
	private boolean warned = false;
	private int warned_progress = 0;
	private long lastTick = 0;
	
	private static LogisticsHUDRenderer renderer = null;
	
	private void clearList() {
		for(IHeadUpDisplayRendererProvider renderer:list) {
			renderer.stopWaitching();
		}
		list.clear();
	}
	
	private void refreshList(double x,double y,double z) {
		clearList();
		ArrayList<Pair<Double,IHeadUpDisplayRendererProvider>> newList = new ArrayList<Pair<Double,IHeadUpDisplayRendererProvider>>();
		for(IRouter router:SimpleServiceLocator.routerManager.getRouters().values()) {
			CoreRoutedPipe pipe = router.getPipe();
			if(!(pipe instanceof IHeadUpDisplayRendererProvider)) continue;
			if(MainProxy.getDimensionForWorld(pipe.worldObj) == MainProxy.getDimensionForWorld(FMLClientHandler.instance().getClient().theWorld)) {
				double dis = Math.hypot(pipe.xCoord - x + 0.5,Math.hypot(pipe.yCoord - y + 0.5, pipe.zCoord - z + 0.5));
				if(dis < 15 && dis > 0.75) {
					newList.add(new Pair<Double,IHeadUpDisplayRendererProvider>(dis,(IHeadUpDisplayRendererProvider)pipe));
					((IHeadUpDisplayRendererProvider)pipe).startWaitching();
				}
			}
		}
		if(newList.size() < 1) return;
		Object[] sorter = newList.toArray();
		Arrays.sort(sorter, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				if(((Pair<Double,IHeadUpDisplayRendererProvider>)o1).getValue1() < ((Pair<Double,IHeadUpDisplayRendererProvider>)o2).getValue1()) {
					return -1;
				} else if(((Pair<Double,IHeadUpDisplayRendererProvider>)o1).getValue1() > ((Pair<Double,IHeadUpDisplayRendererProvider>)o2).getValue1()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		for(Object part:sorter) {
			list.addLast(((Pair<Double,IHeadUpDisplayRendererProvider>)part).getValue2());
		}
	}
	
	private boolean playerWearsHUD() {
		return FMLClientHandler.instance().getClient().thePlayer != null && FMLClientHandler.instance().getClient().thePlayer.inventory != null && FMLClientHandler.instance().getClient().thePlayer.inventory.armorInventory != null && FMLClientHandler.instance().getClient().thePlayer.inventory.armorInventory[3] != null && FMLClientHandler.instance().getClient().thePlayer.inventory.armorInventory[3].itemID == LogisticsPipes.LogisticsHUDArmor.shiftedIndex;
	}
	
	public void renderPlayerDisplay(long renderTicks) {
		if(!displayRenderer()) return;
		Minecraft mc = FMLClientHandler.instance().getClient();
		//Screen Rendering
		if(!warned && !LogisticsPipes.DEBUG) {
			if(lastTick == 0) {
				lastTick = System.currentTimeMillis();
			}
			warned_progress += ((System.currentTimeMillis() - lastTick) * 20 / 1000);
			if(warned_progress > 2000) {
				warned = true;
				warned_progress = 0;
				lastTick = 0;
			}
			if(warned_progress < 1000) {
				String warning = "Warning: This is a WIP. Highly testing. Use on your own risk.";
				ScaledResolution size = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
	            mc.fontRenderer.drawString(warning , ((size.getScaledWidth() - mc.fontRenderer.getStringWidth(warning)) / 2), (size.getScaledHeight() / 2) - 4, 0xFFFF0000);
			}
			if(warned_progress > 1000) {
				double d = (2000 - ((double)warned_progress)) / 1000;
				String warning = "Warning: This is a WIP. Highly testing. Use on your own risk.";
				ScaledResolution size = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
	            mc.fontRenderer.drawString(warning , (int) (((size.getScaledWidth() - mc.fontRenderer.getStringWidth(warning)) / 2) * d), (int) (((size.getScaledHeight() / 2) - 4) * d), 0xFFFF0000);
			}
			if(!warned) {
				GL11.glPopMatrix();
				return;
			}
		}
		String warning = "Warning: This is a WIP. Highly testing. Use on your own risk.";
		ScaledResolution size = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		mc.fontRenderer.drawString(warning , 3, 3, 0xFFFF0000);
	}
	
	public void renderWorldRelative(long renderTicks) {
		if(!displayRenderer()) return;
        GL11.glEnable(GL11.GL_BLEND);
		Minecraft mc = FMLClientHandler.instance().getClient();
		EntityPlayer player = mc.thePlayer;
		if(list.size() == 0 || Math.hypot(lastXPos - player.posX,Math.hypot(lastYPos - player.posY, lastZPos - player.posZ)) > 0.5 || (renderTicks % 10 == 0 && (lastXPos != player.posX || lastYPos != player.posY + player.getEyeHeight() || lastZPos != player.posZ)) || renderTicks % 600 == 0) {
			refreshList(player.posX,player.posY,player.posZ);
			lastXPos = player.posX;
			lastYPos = player.posY + player.getEyeHeight();
			lastZPos = player.posZ;
		}
		boolean cursorHandled = false;
		for(IHeadUpDisplayRendererProvider renderer:list) {
			if(renderer.getRenderer() == null) continue;
			if(renderer.getRenderer().display()) {
				GL11.glPushMatrix();
				if(!cursorHandled) {
					double x = renderer.getX() + 0.5 - player.posX;
					double y = renderer.getY() + 0.5 - player.posY;
					double z = renderer.getZ() + 0.5 - player.posZ;
					if(Math.hypot(x,Math.hypot(y, z)) < 0.75) {
						refreshList(player.posX,player.posY,player.posZ);
				        GL11.glPopMatrix();
						break;
					}
					cursorHandled = handleCursor(renderer);
				}
		        GL11.glPopMatrix();
				GL11.glPushMatrix();
				displayOneView(renderer);
		        GL11.glPopMatrix();
			}
		}
	}

	
	private void displayOneView(IHeadUpDisplayRendererProvider renderer) {
		Minecraft mc = FMLClientHandler.instance().getClient();
		EntityPlayer player = mc.thePlayer;
		double x = renderer.getX() + 0.5 - player.posX;
		double y = renderer.getY() + 0.5 - player.posY;
		double z = renderer.getZ() + 0.5 - player.posZ;
		GL11.glTranslatef((float)x, (float)y, (float)z);
		GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(getAngle(z,x) + 90, 0.0F, 0.0F, 1.0F);
		GL11.glRotatef((-1)*getAngle(Math.hypot(x,z),y) + 180, 1.0F, 0.0F, 0.0F);

		GL11.glTranslatef(0.0F, 0.0F, -0.4F);
		
		GL11.glScalef(0.01F, 0.01F, 1F);
		
		float var3 = mc.theWorld.getWorldTime();
		renderer.getRenderer().renderHeadUpDisplay(Math.hypot(x,Math.hypot(y, z)),(var3 % 24000) > 12700, mc);
	}
	
	private float getAngle(double x, double y) {
		return (float) (Math.atan2(x,y) * 360 / (2 * Math.PI));
	}
	
	public double up(double input) {
		input %= 360.0D;
		while(input < 0 && !Double.isNaN(input) && !Double.isInfinite(input)) {
			input += 360;
		}
		return input;
	}
	
	private boolean handleCursor(IHeadUpDisplayRendererProvider renderer) {
		Minecraft mc = FMLClientHandler.instance().getClient();
		EntityPlayer player = mc.thePlayer;
		double x = renderer.getX() + 0.5 - player.posX;
		double y = renderer.getY() + 0.5 - player.posY;
		double z = renderer.getZ() + 0.5 - player.posZ;
		
		//if(!player.isSneaking()) return true;
		
		MathVector playerView = MathVector.getFromAngles((270 - player.rotationYaw) / 360 * -2 * Math.PI, (player.rotationPitch) / 360 * -2 * Math.PI);
		MathVector playerPos = new MathVector();
		playerPos.X = player.posX;
		playerPos.Y = player.posY;
		playerPos.Z = player.posZ;

		MathVector panelPos = new MathVector();
		panelPos.X = renderer.getX() + 0.5;
		panelPos.Y = renderer.getY() + 0.5;
		panelPos.Z = renderer.getZ() + 0.5;
		
		MathVector panelView = new MathVector();
		panelView.X = playerPos.X - panelPos.X;
		panelView.Y = playerPos.Y - panelPos.Y;
		panelView.Z = playerPos.Z - panelPos.Z;

		panelPos.add(panelView, 0.44D);

		double d = panelPos.X * panelView.X + panelPos.Y * panelView.Y + panelPos.Z * panelView.Z;
		double c = panelView.X * playerPos.X + panelView.Y * playerPos.Y + panelView.Z * playerPos.Z;
		double b = panelView.X * playerView.X + panelView.Y * playerView.Y + panelView.Z * playerView.Z;
		double a = (d - c) / b;
		
		MathVector viewPos = new MathVector();
		viewPos.X = playerPos.X + a * playerView.X - panelPos.X;
		viewPos.Y = playerPos.Y + a * playerView.Y - panelPos.Y;
		viewPos.Z = playerPos.Z + a * playerView.Z - panelPos.Z;
		
		MathVector panelScalVector1 = new MathVector();
		
		if(panelView.Y == 0) {
			panelScalVector1.X = 0;
			panelScalVector1.Y = 1;
			panelScalVector1.Z = 0;
		} else {
			panelScalVector1 = panelView.getOrtogonal(-panelView.X, null, -panelView.Z).makeVectorLength(1.0D);
		}
		
		MathVector panelScalVector2 = new MathVector();
		
		if(panelView.Z == 0) {
			panelScalVector2.X = 0;
			panelScalVector2.Y = 0;
			panelScalVector2.Z = 1;
		} else {
			panelScalVector2 = panelView.getOrtogonal(1.0D, 0.0D, null).makeVectorLength(1.0D);
		}
		
		if(panelScalVector1.Y == 0) {
			return false;
		}
		
		double cursorY = -viewPos.Y / panelScalVector1.Y;
		
		MathVector restViewPos = viewPos.clone();
		restViewPos.X += cursorY*panelScalVector1.X;
		restViewPos.Y = 0;
		restViewPos.Z += cursorY*panelScalVector1.Z;
		
		double cursorX;
		
		if(panelScalVector2.X == 0) {
			cursorX = restViewPos.Z / panelScalVector2.Z;
		} else {
			cursorX = restViewPos.X / panelScalVector2.X;
		}
		
		cursorX *= 50 / 0.47D;
		cursorY *= 50 / 0.47D;
		if(panelView.Z < 0) {
			cursorX *= -1;
		}
		if(panelView.Y < 0) {
			cursorY *= -1;
		}

		if(renderer.getRenderer().cursorOnWindow((int) cursorX, (int)cursorY)) {
			renderer.getRenderer().handleCursor((int) cursorX, (int)cursorY);
			return true;
		}
		return false;
	}
	
	public boolean displayRenderer() {
		if(!displayHUD()) {
			if(list.size() != 0) {
				clearList();
			}
			warned = false;
		}
		return displayHUD();
	}
	
	private boolean displayHUD() {
		return playerWearsHUD() && FMLClientHandler.instance().getClient().currentScreen == null && FMLClientHandler.instance().getClient().gameSettings.thirdPersonView == 0 && !FMLClientHandler.instance().getClient().gameSettings.hideGUI;
	}
	
	public static LogisticsHUDRenderer instance() {
		if(renderer == null) {
			renderer = new LogisticsHUDRenderer();
		}
		return renderer;
	}
}