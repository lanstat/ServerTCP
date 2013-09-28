package dev.sugarscope.server;

import java.io.IOException;
import java.net.Socket;

import dev.sugarscope.transport.Packet;

public class Peer{
	private Reader mclsReader;
	private Socket mclsSocket;
	private int mintGroup;
	
	public Peer(Socket lclsSocket){
		mclsSocket = lclsSocket;
	}
	
	public Socket getConnection(){
		return mclsSocket;
	}
	
	public void setGroupID(int lintID){
		mintGroup = lintID;
	}
	
	public int getGroupID(){
		return mintGroup;
	}
	
	public void initialize(IHandler lclsHandler) throws IOException{
		mclsReader = new Reader(mclsSocket.getInputStream(), lclsHandler);
		new Thread(mclsReader).start();
	}
	
	public boolean sendMessage(Packet lclsPacket){
		boolean lblnResponse = true;
		try {
			mclsSocket.getOutputStream().write(Utils.serialize(lclsPacket));
		} catch (IOException e) {
			System.out.println(e.getMessage());
			lblnResponse = false;
		}
		
		return lblnResponse;
	}
	
	public boolean sendBroadcast(Packet lclsPacket, boolean lblnHimself){
		boolean lblnResponse = true;
		
		return lblnResponse;
	}
	
	public void close(){
		System.out.println("Conexion terminada");
		mclsReader.setRunning(false);
		try {
			mclsSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ServerTCP.getPeers().remove(this);
	}
}
