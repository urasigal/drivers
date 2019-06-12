package com.zixi.drivers.drivers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class FfmpegStreamParametersDriver  extends BroadcasterLoggableApiWorker implements TestDriver {

	// Constructor.
	public FfmpegStreamParametersDriver(String serverHost, String serverPort)
	{
		SERVER_HOST = serverHost;
		SERVER_PORT = serverPort;
	}
	// Initialization through constructor.
	private final String SERVER_HOST;
	// Initialization through constructor.
	private final String SERVER_PORT;
	
	public DriverReslut testIMPL()
	{
		Socket 				clientSocket;
		PrintWriter 		out;
		BufferedReader 		inputFrpmServer;
		JSONObject 			stream  =  null;
		try {
			clientSocket 	 = new Socket(SERVER_HOST, Integer.parseInt( SERVER_PORT ));
			clientSocket.setSoTimeout(60000);
	        out 		 	 = new PrintWriter(clientSocket.getOutputStream(), true);
	        inputFrpmServer  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	        
	        out.println("ok");
	        
	        // This variable will store server's answer.
	        StringBuffer fromServer = new StringBuffer();
	        String tempLine ;
	        while( ( tempLine = inputFrpmServer.readLine() ) != null)
	        {
	        	fromServer.append(tempLine);
	        }
	        JSONObject jsonResponse = new JSONObject(fromServer.toString());
	        
	        JSONArray streamsArray = jsonResponse.getJSONArray("streams");
	        
	        // Assumption: only one video stream is expected for this test.
	        
	        int arrayLenght  = streamsArray.length();
	        
	        for(int i = 0; i < arrayLenght; i++)
	        {
	        	stream = streamsArray.getJSONObject(i);	
	        	if(stream.getString("codec_type").equals("video"))
	        		break;
	        }
	        
	        //System.out.println(fromServer);
		}catch(Exception e) {}
		 
		
		return new DriverReslut("codec_name " + stream.getString("codec_name") + " profile " + stream.getString("profile") + 
		" coded_width " + stream.getInt("coded_width") + " coded_height " + stream.getInt("coded_height")  + 
		" field_order " + stream.getString("field_order") );
	}
	
	
	public static void main(String[] args) {
		FfmpegStreamParametersDriver FfmpegStreamParametersDriver =  new FfmpegStreamParametersDriver("10.7.0.68", "9999");
		System.out.println(FfmpegStreamParametersDriver.testIMPL().getResult());
	}
}
