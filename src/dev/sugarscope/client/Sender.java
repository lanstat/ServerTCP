package dev.sugarscope.client;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Observable;

import dev.sugarscope.generic.Utils;
import dev.sugarscope.transport.Packet;

public class Sender extends Observable implements Runnable{
	
	private final byte[] mclsData;
	private OutputStream mclsOutput;
	private int mintPointer;
	private final int MAX_SIZE = 4095;
	private boolean mblnRunning;
	
	public Sender(Packet lclsPacket, OutputStream lclsOutput) throws IOException{
		mclsData = Utils.serialize(lclsPacket);
		mclsOutput = lclsOutput;
		mblnRunning = true;
	}
	
	public void setRunning(boolean lblnRunning){
		mblnRunning = lblnRunning;
	}

	@Override
	public void run() {
		while(mblnRunning){
			try {
				if(mclsData.length < mintPointer + MAX_SIZE){
					final byte[] larrCutting = Arrays.copyOfRange(mclsData, mintPointer, mclsData.length);
					mclsOutput.write(larrCutting);
					mclsOutput.flush();
					mblnRunning = false;
					System.out.println("Size: "+mclsData.length);
					setChanged();
					notifyObservers();
				}else{
					final byte[] larrCutting = Arrays.copyOfRange(mclsData, mintPointer, MAX_SIZE+mintPointer);
					mintPointer += MAX_SIZE;
					mclsOutput.write(larrCutting);
				}
			} catch (IOException e) {
				e.printStackTrace();
				mblnRunning = false;
			}
		}
	}

}
