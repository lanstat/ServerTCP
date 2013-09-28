package dev.sugarscope.server;


import dev.sugarscope.transport.Packet;

public interface IHandler {
	public void handleMessage(Packet lclsRequest);
}
