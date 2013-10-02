package dev.sugarscope.server;


import dev.sugarscope.transport.Packet;

public abstract class Handler {
	
	private Peer mclsPeer;
	
	public void setPeer(Peer lclsPeer){
		mclsPeer = lclsPeer;
	}
	
	public void sendPackage(Packet lclsPacket){
		mclsPeer.sendPackage(lclsPacket);
	}
	
	public abstract void handleMessage(Packet lclsRequest);
}
