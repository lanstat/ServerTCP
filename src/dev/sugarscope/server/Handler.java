package dev.sugarscope.server;


import dev.sugarscope.transport.Packet;

/**
 * Clase que recepciona las peticiones hechas por los clientes
 * @author lanstat
 *
 */
public abstract class Handler {
	
	protected Peer mclsPeer;
	
	public void setPeer(Peer lclsPeer){
		mclsPeer = lclsPeer;
	}
	
	public void response(Packet lclsPacket){
		mclsPeer.sendPackage(lclsPacket);
	}
	
	public abstract void handleMessage(Packet lclsRequest);
}
