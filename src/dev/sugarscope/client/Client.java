package dev.sugarscope.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import dev.sugarscope.transport.Packet;

public class Client implements Observer{
	private Socket mclsSocket;
	private static Client mclsClient;
	private Reader mclsReader;
	private ArrayList<Packet> marrStackPakects;
	private boolean mblnSendingInProcess;
	
	private Client(){
		mblnSendingInProcess = false;
		marrStackPakects = new ArrayList<Packet>();
	}
	
	public static Client getInstance(){
		if(mclsClient==null)
			mclsClient = new Client();
		return mclsClient;
	}
	
	/**
	 * Conecta con el servidor especificado
	 * @param serverHost Direccion del servidor
	 * @param port Puerto de conexion
	 * @throws UnknownHostException Lanzado cuando no se logra encontrar el servidor.
	 * @throws IOException Lanzado cuando existe un error la comunicacion.
	 */
	public void connect(String serverHost, int port) throws UnknownHostException, IOException{
		mclsSocket = new Socket(serverHost, port);
		mclsReader = new Reader(mclsSocket.getInputStream());
		new Thread(mclsReader).start();
	}
	
	/**
	 * Envia un paquete al servidor, si el cliente se encuentre en proceso de envio coloca el paquete en cola.
	 * @param lclsPacket {@link Packet} Paquete con los datos a enviar. 
	 */
	public void sendPackage(Packet lclsPacket){
		try {
			if(!mblnSendingInProcess){
				sendPacketNow(lclsPacket);
				mblnSendingInProcess = true;
			}else{
				marrStackPakects.add(lclsPacket);
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
	
	/**
	 * Cierra la conexion y detiene los hilos de escucha.
	 * @throws IOException 
	 */
	public void close() throws IOException{
		mclsReader.setRunning(false);
		mclsSocket.close();
	}
	
	public Reader getReader(){
		return mclsReader;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(marrStackPakects.isEmpty()){
			mblnSendingInProcess = false;
			return;
		}
		try {
			sendPacketNow(marrStackPakects.get(0));
			marrStackPakects.remove(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
