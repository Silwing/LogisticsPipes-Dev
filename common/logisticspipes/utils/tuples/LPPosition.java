package logisticspipes.utils.tuples;

import logisticspipes.pipes.basic.CoreRoutedPipe;
import logisticspipes.routing.pathfinder.IPipeInformationProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class LPPosition extends Triplet<Double, Double, Double> {

	public LPPosition(double xPos, double yPos, double zPos) {
		super(xPos, yPos, zPos);
	}

	public LPPosition(int xPos, int yPos, int zPos) {
		super((double)xPos, (double)yPos, (double)zPos);
	}

	public LPPosition(TileEntity tile) {
		super((double)tile.xCoord, (double)tile.yCoord, (double)tile.zCoord);
	}
	
	public LPPosition(CoreRoutedPipe pipe) {
		super((double)pipe.getX(), (double)pipe.getY(), (double)pipe.getZ());
	}
	
	public LPPosition(IPipeInformationProvider pipe) {
		super((double)pipe.getX(), (double)pipe.getY(), (double)pipe.getZ());
	}

	public int getX() {
		return (int)(double)this.getValue1();
	}
	
	public int getY() {
		return (int)(double)this.getValue2();
	}
	
	public int getZ() {
		return (int)(double)this.getValue3();
	}

	public double getXD() {
		return this.getValue1();
	}
	
	public double getYD() {
		return this.getValue2();
	}
	
	public double getZD() {
		return this.getValue3();
	}
	
	public TileEntity getTileEntity(World world) {
		return world.getBlockTileEntity(getX(), getY(), getZ());
	}
	
	public void moveForward(ForgeDirection dir, double steps) {
		switch(dir) {
			case UP:
				this.value2 += steps;
				break;
			case DOWN:
				this.value2 -= steps;
				break;
			case NORTH:
				this.value3 -= steps;
				break;
			case SOUTH:
				this.value3 += steps;
				break;
			case EAST:
				this.value1 += steps;
				break;
			case WEST:
				this.value1 -= steps;
				break;
			default:
		}
	}

	public void moveForward(ForgeDirection dir) {
		moveForward(dir, 1);
	}
	
	public void moveBackward(ForgeDirection dir, double steps) {
		moveForward(dir, -1 * steps);
	}

	public void moveBackward(ForgeDirection dir) {
		moveBackward(dir, 1);
	}
}
