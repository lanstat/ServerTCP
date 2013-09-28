package dev.sugarscope.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import dev.sugarscope.transport.Packet;

public class Reader implements Runnable{
	private byte[] marrBuffer;
	private BufferedInputStream mclsInput;
	public final int BUFFER_SIZE = 1024;
	private boolean mblnIsAlive = true;
	private IHandler mclsHandler;
	
	public Reader(InputStream lclsInput, IHandler lclsHandler) throws IOException{
		marrBuffer = new byte[0];
		mclsInput = new BufferedInputStream(lclsInput);
		mclsHandler = lclsHandler;
	}
	
	public void setRunning(boolean lblnRun){
		mblnIsAlive = lblnRun;
		try {
			mclsInput.close();
		} catch (IOException e) {
		}
	}
	
	@Override
	public void run() {
		while (mblnIsAlive) {
			try {
				byte[] larrBuffer = new byte[BUFFER_SIZE];
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
				System.out.println(e);
			}
		}
		
	}
	
	
	
}