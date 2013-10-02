package dev.sugarscope.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerTCP {
	private ServerSocket mclsServer;
	private final int PORT;
	private static ArrayList<Peer> marrPeersActive;
	private boolean mblnRunning;
	private Class<?> mclsHandlerClass;
	
	public ServerTCP(int port) throws IOException{
		PORT = port;
		mclsServer = new ServerSocket(PORT);
		marrPeersActive = new ArrayList<>();
	}
	
	public void registerHandler(Class<?> lclsHandlerClass){
		mclsHandlerClass = lclsHandlerClass;
	}
	
	public static ArrayList<Peer> getPeers(){
		return marrPeersActive;
	}
	
	public static Peer getPeer(int lintId){
		for(Peer lclsPeer: marrPeersActive){
			if(lclsPeer.hashCode() == lintId)
				return lclsPeer;
		}
		return null;
	}
	
	private void acceptClients() throws IOException{
		while(mblnRunning){
			System.out.println("Esperando clientes....");
			Socket lclsSocket = mclsServer.accept();
			Peer lclsPeer = new Peer(lclsSocket);
			try {
				lclsPeer.initialize((Handler)mclsHandlerClass.newInstance());
			} catch (InstantiationException e) {
				System.out.println(e.getMessage());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			marrPeersActive.add(lclsPeer);
			System.out.println("Cliente aceptado: "+lclsSocket.getInetAddress());
		}
	}
	
	public void start() throws IOException{
		mblnRunning = true;
		acceptClients();
	}
}
