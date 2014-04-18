package dev.sugarscope.server;

import java.util.ArrayList;
import java.util.HashMap;

public class PeersActive {
	private ArrayList<Peer> mclsPeersIncoming;
	private HashMap<String, Peer> mclsPeersHashed;
	private static PeersActive mclsInstance;
	
	private PeersActive(){
		mclsPeersIncoming = new ArrayList<>();
		mclsPeersHashed = new HashMap<>();
	}
	
	public static PeersActive getInstance(){
		if(mclsInstance == null){
			mclsInstance = new PeersActive();
		}
		return mclsInstance;
	}
	
	public void append(Peer lclsPeer){
		mclsPeersIncoming.add(lclsPeer);
	}
	
	/**
	 * Agrega un objeto a la lista de objetos con llave
	 * @param lstrKey LLave unica
	 * @param lclsPeer Conexion activa
	 */
	public void append(String lstrKey, Peer lclsPeer){
		lclsPeer.setKey(lstrKey);
		mclsPeersHashed.put(lstrKey, lclsPeer);
		mclsPeersIncoming.remove(lclsPeer);
	}
	
	public Peer get(int lintIndex){
		return mclsPeersIncoming.get(lintIndex);
	}
	
	public Peer get(String lstrIndex){
		return mclsPeersHashed.get(lstrIndex);
	}
	
	public void remove(Peer lclsPeer){
		lclsPeer.close();
		mclsPeersIncoming.remove(lclsPeer);
		if(lclsPeer.getKey() != null){
			mclsPeersHashed.remove(lclsPeer.getKey());
		}
	}
	
	public void remove(String lstrKey){
		mclsPeersHashed.get(lstrKey).close();
		mclsPeersHashed.remove(lstrKey);
	}
	
	public int count(){
		return mclsPeersIncoming.size()+mclsPeersHashed.size();
	}
}
