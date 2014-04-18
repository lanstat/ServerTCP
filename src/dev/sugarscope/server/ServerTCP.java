package dev.sugarscope.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {
	private ServerSocket mclsServer;
	private final int PORT;
	private boolean mblnRunning;
	private Class<?> mclsHandlerClass;
	
	public ServerTCP(int port) throws IOException{
		PORT = port;
		mclsServer = new ServerSocket(PORT);
	}
	
	public void registerHandler(Class<?> lclsHandlerClass){
		mclsHandlerClass = lclsHandlerClass;
	}
	
	private void acceptClients() throws IOException{
		Peer lclsPeer;
		Socket lclsSocket;
		try {
			while(mblnRunning){
				Logger.i("Esperando clientes....");
				lclsSocket = mclsServer.accept();
				try {
					lclsPeer = new Peer(lclsSocket);
					lclsPeer.initialize((Handler)mclsHandlerClass.newInstance());
					PeersActive.getInstance().append(lclsPeer);
					Logger.i("Cliente aceptado: "+lclsSocket.getInetAddress());
					Logger.i("count: "+PeersActive.getInstance().count());
				} catch (InstantiationException | IllegalAccessException e) {
					Logger.e("dev.sugarscope.server.ServerTCP.acceptClients()", e);
				}
			}
			Logger.i("Servidor terminado");
		} catch (Exception e) {
			Logger.e("dev.sugarscope.server.ServerTCP.acceptClients()", e);
		}
	}
	
	public void stop(){
		mblnRunning = false;
	}
	
	public void start() throws IOException{
		mblnRunning = true;
		acceptClients();
	}
}
