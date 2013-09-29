package dev.sugarscope.server;

import java.util.ArrayList;
import java.util.HashMap;






import dev.sugarscope.generic.Utils;
import dev.sugarscope.transport.Packet;

public class Group {
	private HashMap<Integer, GroupPeer> mclsPeers;
	private int mintID;
	private static Group instance;
	
	private Group(){
		mclsPeers = new HashMap<>();
	}
	
	public static Group getInstance(){
		if(instance == null){
			instance = new Group();
		}
		return instance;
	}
	
	public int createGroup(Peer lclsPeer, int lintCountMax, int lintNumPieces){
		mintID++;
		GroupPeer lclsGroup = new GroupPeer();
		lclsGroup.marrIntegrants.add(lclsPeer);
		lclsGroup.mintCountMax = lintCountMax;
		lclsGroup.mintNumPieces = lintNumPieces;
		mclsPeers.put(mintID, lclsGroup);
		
		return mintID;
	}
	
	public void appendImage(Peer lclsPeer, byte[] larrImage){
		byte[] larrByte = mclsPeers.get(lclsPeer.getGroupID()).marrImage;
		if(larrByte==null)
			larrByte= new byte[0];
		larrByte = Utils.concat(larrByte, larrImage);
		System.out.println("Concat: "+larrByte.length);
		mclsPeers.get(lclsPeer.getGroupID()).marrImage = larrByte;
	}
	
	public void addPeerToGroup(int lintId, Peer lclPeer){
		mclsPeers.get(lintId).marrIntegrants.add(lclPeer);
	}
	
	public void saveImage(Peer lclsPeer){
		//Utils.saveImage(mclsPeers.get(lclsPeer.getGroupID()).marrImage, "foto");
	}
	
	public int[] obtainUsersId(int lintId){
		GroupPeer larrPeers = mclsPeers.get(lintId);
		int[] larrIds = new int[larrPeers.marrIntegrants.size()];
		int iter = 0;
		for(Peer lclsPeer: larrPeers.marrIntegrants){
			larrIds[iter] = lclsPeer.getGroupID();
			iter++;
		}
		
		return larrIds;
	}
	
	public void sendBroadcast(Peer lclsSender, Packet lclsPacket, boolean lblnHimself){
		GroupPeer larrPeers = mclsPeers.get(lclsSender.getGroupID());
		for(Peer lclsPeer: larrPeers.marrIntegrants){
			if(lclsPeer.equals(lclsSender) && lblnHimself){
				lclsPeer.sendMessage(lclsPacket);
			}else{
				lclsPeer.sendMessage(lclsPacket);
			}
		}
	}
	
	public class GroupPeer{
		ArrayList<Peer> marrIntegrants;
		byte[] marrImage;
		int mintCountMax, mintNumPieces;
		
		public GroupPeer(){
			marrIntegrants = new ArrayList<>();
		}
	}
}
