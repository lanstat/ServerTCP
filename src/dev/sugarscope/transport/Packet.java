package dev.sugarscope.transport;

import java.io.Serializable;

public class Packet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6647230373426524074L;
	private int mintTag;
	private Object marrData;
	
	public Packet(int lintTag){
		mintTag = lintTag;
	}
	
	public Packet(int lintTag, Object larrData){
		mintTag = lintTag;
		marrData = larrData;
	}
	
	public void setData(Object... larrData){
		marrData = larrData;
	}
	
	public Object getData(){
		return marrData;
	}
	
	public int getTag(){
		return mintTag;
	}

	@Override
	public String toString() {
		return "Tag: "+mintTag;
	}
}
