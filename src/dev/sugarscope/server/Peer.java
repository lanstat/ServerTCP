package dev.sugarscope.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

import dev.sugarscope.server.Sender;
import dev.sugarscope.transport.Packet;

public class Peer implements Observer{
	private Reader mclsReader;
	private Socket mclsSocket;
	private int mintGroup;
	private Stack<Packet> marrStackPakects;
	private boolean mblnSendingInProcess;
	
	public Peer(Socket lclsSocket){
		mclsSocket = lclsSocket;
		mblnSendingInProcess = false;
		marrStackPakects = new Stack<>();
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
	
	public void initialize(Handler lclsHandler) throws IOException{
		lclsHandler.setPeer(this);
		mclsReader = new Reader(mclsSocket.getInputStream(), lclsHandler, hashCode());
		new Thread(mclsReader).start();
	}
	
	public void sendPackage(Packet lclsPacket){
		try {
			if(!mblnSendingInProcess){
				sendPacketNow(lclsPacket);
				mblnSendingInProcess = true;
			}else{
				marrStackPakects.push(lclsPacket);
			}
		} catch (IOException e) {
			e.printStackTrace();
			mblnSendingInProcess = false;
		}
	}
	
	/**
	 * 
	 * @param lclsPacket
	 * @throws IOException
	 */
	private void sendPacketNow(Packet lclsPacket) throws IOException{
		final Sender lclsSender = new Sender(lclsPacket, mclsSocket.getOutputStream());
		lclsSender.addObserver(this);
		new Thread(lclsSender).start();
	}
	
	public boolean sendBroadcast(Packet lclsPacket, boolean lblnHimself){
		boolean lblnResponse = true;
		
		return lblnResponse;
	}
	
	public void close(){
		try {
			mclsReader.setRunning(false);
			ServerTCP.getPeers().remove(this);
			mclsSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			System.out.println("Conexion terminada");
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof Sender){
			if(marrStackPakects.isEmpty()){
				mblnSendingInProcess = false;
				return;
			}
			try {
				sendPacketNow(marrStackPakects.pop());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(o instanceof Reader){
			
		}
	}
}
