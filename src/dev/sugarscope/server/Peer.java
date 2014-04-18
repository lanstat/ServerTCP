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
	private Stack<Packet> marrStackPakects;
	private boolean mblnSendingInProcess;
	private String mstrKey;
	
	public Peer(Socket lclsSocket){
		mclsSocket = lclsSocket;
		mblnSendingInProcess = false;
		marrStackPakects = new Stack<>();
	}
	
	public Socket getConnection(){
		return mclsSocket;
	}
	
	public void initialize(Handler lclsHandler) throws IOException{
		lclsHandler.setPeer(this);
		mclsReader = new Reader(lclsHandler, this);
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
	
	public void setKey(String lstrKey){
		mstrKey = lstrKey;
	}
	
	public String getKey(){
		return mstrKey;
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
			mclsReader.close();
			if(mclsSocket!=null && mclsSocket.isConnected())
				mclsSocket.close();
			Logger.i("Conexion terminada");
		} catch (Exception e) {
			Logger.e("dev.sugarscope.server.Peer.close()", e);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		try {
			if(marrStackPakects.isEmpty()){
				mblnSendingInProcess = false;
				return;
			}
			sendPacketNow(marrStackPakects.pop());
		} catch (Exception e) {
			Logger.e("dev.sugarscope.server.Peer.update()", e);
		}
	}
}
