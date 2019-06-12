package com.zixi.drivers.drivers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import org.apache.commons.lang3.ArrayUtils;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;
import static com.zixi.globals.Macros.*;

public class FeederPostKeyDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	
	protected FileInputStream fileInputStream		=	null;
	protected File sppk						 		= 	null;
	 
	public DriverReslut uploadHttpsCertificate(String userName, String userPass, String login_ip, String uiport, String keyLocation, String boundary) throws Exception
	{
		byte[] keyByteArray = null;
		try {
			// Post request content.
			sppk = new File(keyLocation); // Open a file which contains a RCA key.
		
			// Holding a key from file.
			Long length = sppk.length();
			keyByteArray = new byte[length.intValue()];
			fileInputStream 		= new FileInputStream(sppk);
		    fileInputStream.read(keyByteArray);
		    fileInputStream.close();
		}catch(NullPointerException | IOException npe)
		{
			return new DriverReslut(npe.getMessage());
		}
	    // Login to broadcaster.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		// Upload a key and get results.
		driverReslut = new DriverReslut(apiworker.uploadCertificate("http://" + login_ip + ":" + uiport + 
		"/upload_https_certificate.htm", "", responseCookieContainer, login_ip, this, uiport, keyByteArray, boundary));
		return driverReslut;
	}
	
	public DriverReslut uploadSshKeyToBroadcasterServer(String userName, String userPass, String login_ip, String uiport, String keyLocation) throws Exception
	{
		byte[] keyByteArray = null;
		try {
			// Post request content.
			sppk = new File(keyLocation); // Open a file which contains a RCA key.
		
			// Holding a key from file.
			Long length = sppk.length();
			keyByteArray = new byte[length.intValue()];
			fileInputStream 		= new FileInputStream(sppk);
		    fileInputStream.read(keyByteArray);
		    fileInputStream.close();
		}catch(NullPointerException | IOException npe)
		{
			return new DriverReslut(npe.getMessage());
		}
	    // Login to broadcaster.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		// Upload a key and get results.
		driverReslut = new DriverReslut(apiworker.inserKeyToSpecificBroadcaster1("http://" + login_ip + ":" + uiport + 
		"/zixi/upload_ssh_key.htm", "", responseCookieContainer, login_ip, this, uiport, keyByteArray));
		return driverReslut;
	}
	
	public DriverReslut uploadPrivateKeyFeeder(String userName, String userPass, String login_ip, String uiport) throws Exception
	{
		{
			int tryTest;
			int tryAdd = 0;
			do {
				try {
					tryTest = 3;
				 	URL testFileURL 				 = new URL("http://10.7.0.42:4445/zenpk");
				 	ArrayList<Byte> byteArrayList 	 = new ArrayList<Byte>();
			        InputStream in 					 = testFileURL.openStream();
			        byte c;
			        
			        while ((c = (byte)in.read()) != -1) {
			        	byteArrayList.add(c);
			            }
			        in.close();
			        
			        Byte[] ByteArray 		= byteArrayList.toArray(new Byte[byteArrayList.size()]);
			        byte[] bytesArray 		= ArrayUtils.toPrimitive(ByteArray);
					
					responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", 
					userName , userPass, login_ip, uiport);
					driverReslut 			= new DriverReslut(apiworker.uploadPkToFeeder("http://" + login_ip + ":" + uiport + "/upload_ssh_key.htm", "", 
					responseCookieContainer, login_ip, this, uiport, bytesArray));
					
				}catch(Exception e)
				{
					tryAdd ++;
					tryTest = tryAdd;
					return new DriverReslut(e.getMessage() + "Cuurent class name:  " + this.getClass().getName());
				}
				tryTest +=3;
			}while(tryTest < 3); // Try three times to upload a private key.
			return driverReslut;
		}
	}
	
	// Get String from URL resource.
	public static String getStringFromUrl(String refPath) throws Exception
	{
		 int  attempts = 0;
			do {
				try {
				 	StringBuffer outputString		 =  new  StringBuffer();
			        URL testFileURL 				     = new URL("http://10.7.0.42:4445/" + refPath);
			        InputStream in 					 = testFileURL.openStream();
			        byte c;
			        while ((c = (byte)in.read()) != -1) { outputString.append((char)c); }
			        in.close();  
			       
			        return  outputString.toString();
				} catch(Exception e) { attempts ++; }
				
			}while(attempts < 3); // Try three times to upload a private key.
			return new String("Value is not taken");
	}
	
	public DriverReslut uploadPrivateKeyBroadcaster(String userName, String userPass, String login_ip, String uiport) throws Exception
	{
		{
			int tryTest;
			int tryAdd = 0;
			do {
				try {
					tryTest = 3;
				 	URL testFileURL 				 = new URL("http://10.7.0.42:4445/zenbxpk");
				 	ArrayList<Byte> byteArrayList 	 = new ArrayList<Byte>();
			        InputStream in 					 = testFileURL.openStream();
			        byte c;
			        
			        while ((c = (byte)in.read()) != -1) {
			        	byteArrayList.add(c);
			            }
			        in.close();
			        
			        Byte[] ByteArray 		= byteArrayList.toArray(new Byte[byteArrayList.size()]);
			        byte[] bytesArray 		= ArrayUtils.toPrimitive(ByteArray);
					
					responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", 
					userName , userPass, login_ip, uiport);
					driverReslut = new DriverReslut(apiworker.inserKeyToSpecificBroadcasterZenCase("http://" + login_ip + ":" + uiport + 
					"/zixi/upload_ssh_key.htm", "", responseCookieContainer, login_ip, this, uiport, bytesArray));
					
				}catch(Exception e)
				{
					tryAdd ++;
					tryTest = tryAdd;
					return new DriverReslut(e.getMessage() + "Cuurent class name:  " + this.getClass().getName());
				}
				tryTest +=3;
			}while(tryTest < 3); // Try three times to upload a private key.
			return driverReslut;
		}
	}
	
	public DriverReslut testIMPL2(String userName, String userPass, String login_ip, String uiport)  
	{
		int tryTest;
		int tryAdd = 0;
		do {
			try {
				tryTest = 3;
			 	URL testFileURL 				 = new URL("http://10.7.0.42:4445/pk");
			 	ArrayList<Byte> byteArrayList 	 = new ArrayList<Byte>();
		        InputStream in 					 = testFileURL.openStream();
		        byte c;
		        
		        while ((c = (byte)in.read()) != -1) {
		        	byteArrayList.add(c);
		            }
		        in.close();
		        
		        Byte[] ByteArray 		= byteArrayList.toArray(new Byte[byteArrayList.size()]);
		        byte[] bytesArray 		= ArrayUtils.toPrimitive(ByteArray);
				
				responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", 
				userName , userPass, login_ip, uiport);
				driverReslut 			= new DriverReslut(apiworker.inserKeyToSpecificBroadcaster("http://" + login_ip + ":" + uiport + "/upload_https_pk.htm", "", 
				responseCookieContainer, login_ip, this, uiport, bytesArray));
				
			}catch(Exception e)
			{
				tryAdd ++;
				tryTest = tryAdd;
				return new DriverReslut(e.getMessage() + "Cuurent class name:  " + this.getClass().getName());
			}
			tryTest +=3;
		}while(tryTest < 3); // Try three times to upload a private key.
		return driverReslut;
	}
	
	public DriverReslut uploadZenCrtToSys(String userName, String userPass, String login_ip, String uiport) throws Exception
	{
		{
			int tryTest;
			int tryAdd = 0;
			do {
				try {
					tryTest = 3;
				 	URL testFileURL 				 = new URL("http://10.7.0.42:4445/zencrt");
				 	ArrayList<Byte> byteArrayList 	 = new ArrayList<Byte>();
			        InputStream in 					 = testFileURL.openStream();
			        byte c;
			        
			        while ((c = (byte)in.read()) != -1) {
			        	byteArrayList.add(c);
			            }
			        in.close();
			        
			        Byte[] ByteArray 		= byteArrayList.toArray(new Byte[byteArrayList.size()]);
			        byte[] bytesArray 		= ArrayUtils.toPrimitive(ByteArray);
					
			        
				}catch(Exception e)
				{
					tryAdd ++;
					tryTest = tryAdd;
					return new DriverReslut(e.getMessage() + "Cuurent class name:  " + this.getClass().getName());
				}
				tryTest +=3;
			}while(tryTest < 3); // Try three times to upload a private key.
			return driverReslut;
		}
	}
}
