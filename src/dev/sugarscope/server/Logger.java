package dev.sugarscope.server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.Date;

public class Logger {
	public static void w(String lstrMessage){
		System.out.println("[Warning] "+lstrMessage);
	}
	
	public static void w(String lstrProc, String lstrMessage){
		System.out.println("[Warning] Procedure: "+lstrProc+" Message: "+lstrMessage);
	}
	
	public static void w(String lstrProc, Exception lclsException){
		lclsException.printStackTrace();
	}
	
	public static void e(String lstrMessage){
		System.out.println("[Error] "+lstrMessage);
	}
	
	public static void e(String lstrProc, String lstrMessage){
		System.out.println("[Error] "+lstrMessage);
	}
	
	public static void e(String lstrProc, Exception lclsException){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		lclsException.printStackTrace(pw);
		//System.out.println("[Error] Procedure: "+lstrProc+" Message: "+sw.toString());
		writeFile("[Error] Procedure: "+lstrProc+" Message: "+sw.toString());
	}
	
	public static void i(String lstrMessage){
		System.out.println("[Info] "+lstrMessage);
	}
	
	public static void i(String lstrProc, String lstrMessage){
		System.out.println("[Info] "+lstrMessage);
	}
	
	public static void i(String lstrProc, Exception lclsException){
		lclsException.printStackTrace();
	}
	
	public static String getTimeStamp(){
		Date date = new Date();
		Timestamp stamp = new Timestamp(date.getTime());
		return stamp.toString();
	}
	
	private static void writeFile(String lstrData){
		try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("smsserver.log", true)));
		    out.println(lstrData);
		    out.close();
		} catch (IOException e) {
		}
	}
}
