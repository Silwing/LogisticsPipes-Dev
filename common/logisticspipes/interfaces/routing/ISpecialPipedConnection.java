package logisticspipes.interfaces.routing;

import java.util.List;

import logisticspipes.routing.pathfinder.IPipeInformationProvider;

import buildcraft.transport.TileGenericPipe;

public interface ISpecialPipedConnection {
	public boolean init();
	public boolean isType(IPipeInformationProvider startPipe);
	public List<IPipeInformationProvider> getConnections(IPipeInformationProvider startPipe);
}
