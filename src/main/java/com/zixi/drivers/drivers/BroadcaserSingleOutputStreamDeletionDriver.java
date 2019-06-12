package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import com.zixi.tools.*;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;



//  Output stream deletion on a broadcaster server.
public class BroadcaserSingleOutputStreamDeletionDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	
	public  	  ArrayList<String> 	list 				= 	new ArrayList<String>();
	private 	  String 				internalStreamID	= 	null;
	private 	  String 				internalStreamName 	= 	null;
	final private String 		    	rid                 = 	"id";
	
	public DriverReslut testIMPL(String login_ip,String userName,String userPassword,String name, String uiport) throws Exception
	{		
		// Super class member (BroadcasterLoggableApiWorker) -- get session parameters - broadcaster credentials.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm",
		userName , userPassword, login_ip, uiport);
		
		// get all outputs Json from broadcaster
		String 		response 			= 	apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/outputs.json", "", LINE_MODE, responseCookieContainer, login_ip, this, uiport );
		JSONObject 	responseJson 		= 	new JSONObject(response.toString());
		JSONArray 	outputStreamsArray 	= 	responseJson.getJSONArray("outputs");
		String streamName 				= 	null;
		
		// Walk through all outputs and find an id by an output name.
		for (int i = 0; i < outputStreamsArray.length(); i++) 
		{
			//System.out.println("before");
			JSONObject outputStream = outputStreamsArray.getJSONObject(i);
		    streamName = outputStream.getString("name");
		    if(streamName.equals(name))
		    {
		    	internalStreamID 	= outputStream.getString("id");
		    	internalStreamName  = outputStream.getString("stream_id");
		    }
		 }
		
		driverReslut =  new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport +  "/zixi/remove_output.json?" + rid + "=" + internalStreamID +"&stream=" +
		internalStreamName , streamName, UDPMODE, responseCookieContainer, login_ip, this, uiport));
		
		return driverReslut;
	}
	
	public String getId1() { return internalStreamName; }
}
