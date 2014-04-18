package dev.sugarscope.server;

import java.io.BufferedInputStream;
import java.io.IOException;

import dev.sugarscope.generic.Utils;
import dev.sugarscope.transport.Packet;

public class Reader implements Runnable{
	private byte[] marrBuffer;
	private BufferedInputStream mclsInput;
	public final int BUFFER_SIZE = 1024;
	private boolean mblnIsAlive = true;
	private Handler mclsHandler;
	private Peer mclsPeer;
	
	public Reader(Handler lclsHandler, Peer lclsPeer) throws IOException{
		marrBuffer = new byte[0];
		mclsInput = new BufferedInputStream(lclsPeer.getConnection().getInputStream());
		mclsHandler = lclsHandler;
		mclsPeer = lclsPeer;
	}
	
	public void setRunning(boolean lblnRun){
		mblnIsAlive = lblnRun;
		try {
			mclsInput.close();
		} catch (IOException e) {
		}
	}
	
	public boolean isAlive(){
		return mblnIsAlive;
	}
	
	public void close(){
		try {
			mclsInput.close();
			mblnIsAlive = false;
		} catch (Exception e) {
			Logger.e("dev.sugarscope.server.Reader.close()", e);
		}
	}
	
	@Override
	public void run() {
		byte[] larrBuffer;
		try {
			while (mblnIsAlive) {
				try {
					larrBuffer = new byte[BUFFER_SIZE];
					mclsInput.read(larrBuffer);
					if(mclsInput.available()==0){
						marrBuffer = Utils.concat(marrBuffer, larrBuffer);
						final Packet lclsPacket = (Packet)Utils.deserialize(marrBuffer);
						mclsHandler.handleMessage(lclsPacket);
						marrBuffer = new byte[0];
					}else{
						marrBuffer = Utils.concat(marrBuffer, larrBuffer);
					}
				} catch (IOException | ClassNotFoundException e) {
					mblnIsAlive = false;
					Logger.e("dev.sugarscope.server.Reader.run()", e);
				}
			}
			PeersActive.getInstance().remove(mclsPeer);
		} catch (Exception e) {
			Logger.e("dev.sugarscope.server.Reader.run()", e);
		} finally {
			larrBuffer = null;
		}
	}
	
}