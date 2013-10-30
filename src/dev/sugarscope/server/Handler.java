package dev.sugarscope.server;


import dev.sugarscope.transport.Packet;

public abstract class Handler {
	
	protected Peer mPeer;
	
	public void setPeer(Peer lclsPeer){
		mPeer = lclsPeer;
	}
	
	public void response(Packet lclsPacket){
		mPeer.sendPackage(lclsPacket);
	}
	
	public abstract void handleMessage(Packet lclsRequest);
}
