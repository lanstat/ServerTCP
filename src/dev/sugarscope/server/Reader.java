package dev.sugarscope.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;

import dev.sugarscope.generic.Utils;
import dev.sugarscope.transport.Packet;

public class Reader extends Observable implements Runnable{
	private byte[] marrBuffer;
	private BufferedInputStream mclsInput;
	public final int BUFFER_SIZE = 1024;
	private boolean mblnIsAlive = true;
	private Handler mclsHandler;
	private final int mintPeerId;
	
	public Reader(InputStream lclsInput, Handler lclsHandler, int lintPeerId) throws IOException{
		marrBuffer = new byte[0];
		mclsInput = new BufferedInputStream(lclsInput);
		mclsHandler = lclsHandler;
		mintPeerId = lintPeerId;
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
				ServerTCP.getPeer(mintPeerId).close();
			}
		}
		
	}
	
	
	
}