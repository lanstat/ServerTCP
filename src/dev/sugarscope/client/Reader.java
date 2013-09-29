package dev.sugarscope.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;

import dev.sugarscope.generic.Utils;
import dev.sugarscope.transport.Packet;

public class Reader extends Observable implements Runnable {
	private byte[] marrBuffer;
	private BufferedInputStream mclsInput;
	public final int BUFFER_SIZE = 1024;
	private boolean mblnIsAlive = true;
	
	public Reader(InputStream lclsInput) throws IOException{
		marrBuffer = new byte[0];
		mclsInput = new BufferedInputStream(lclsInput);
	}
	
	public void setRunning(boolean lblnRunning){
		mblnIsAlive = lblnRunning;
		try {
			mclsInput.close();
		} catch (IOException e) {
			e.printStackTrace();
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
					setChanged();
					notifyObservers(lclsPacket);
					marrBuffer = new byte[0];
				}else{
					marrBuffer = Utils.concat(marrBuffer, larrBuffer);
				}
			} catch (Exception e) {
				mblnIsAlive = false;
			} 
		}
	}

}
