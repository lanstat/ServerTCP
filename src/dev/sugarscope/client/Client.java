package dev.sugarscope.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import dev.sugarscope.generic.Utils;
import dev.sugarscope.transport.Packet;

public class Client {
	private Socket mclsSocket;
	private static Client mclsClient;
	private Reader mclsReader;
	
	private Client(){
	}
	
	public static Client getInstance(){
		if(mclsClient==null)
			mclsClient = new Client();
		return mclsClient;
	}
	
	public void connect(String serverHost, int port) throws UnknownHostException, IOException{
		mclsSocket = new Socket(serverHost, port);
		mclsReader = new Reader(mclsSocket.getInputStream());
		new Thread(mclsReader).start();
	}
	
	public boolean sendPackage(Packet lclsPacket){
		boolean lblnResponse = true;
		try {
			final byte[] larrByte = serialize(lclsPacket);
			mclsSocket.getOutputStream().write(larrByte);
		} catch (IOException e) {
			lblnResponse = false;
		}
		
		return lblnResponse;
	}
	
	private byte[] serialize(Packet lclsPacket) throws IOException{
		return Utils.serialize(lclsPacket);
	}
	
	public void close() throws IOException{
		mclsReader.setRunning(false);
		mclsSocket.close();
	}
	
	public Reader getReader(){
		return mclsReader;
	}
}
