package dev.sugarscope.generic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Utils {
	
	public static byte[] serialize(Object lobjData) throws IOException {
	    ByteArrayOutputStream lclsOut = new ByteArrayOutputStream();
	    ObjectOutputStream lclsOs = new ObjectOutputStream(lclsOut);
	    lclsOs.writeObject(lobjData);
	    return lclsOut.toByteArray();
	}
	
	public static Object deserialize(byte[] larrData) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream lclsIn = new ByteArrayInputStream(larrData);
	    ObjectInputStream lclsIs = new ObjectInputStream(lclsIn);
	    return lclsIs.readObject();
	}
	
	/*public static void saveImage(byte[] larrData, String lstrFilename){
		try {
			InputStream in = new ByteArrayInputStream(larrData);
			BufferedImage lclsBuffer = ImageIO.read(in);
			ImageIO.write(lclsBuffer, "png", new File(lstrFilename+".png"));
			lclsBuffer.flush();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] readImage(String lstrFilename){
		try {
			BufferedImage originalImage = ImageIO.read(new File(lstrFilename+".png"));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "png", baos);
			baos.flush();
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	public static byte[] concat(byte[] A, byte[] B) {
		int aLen = A.length;
		int bLen = B.length;
		byte[] C= new byte[aLen+bLen];
		System.arraycopy(A, 0, C, 0, aLen);
		System.arraycopy(B, 0, C, aLen, bLen);
		return C;
	}
}
