package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;
import org.json.JSONArray;
import org.json.JSONObject;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ReceiverAddSourceToOutputDriver  extends BroadcasterLoggableApiWorker implements TestDriver{
	
	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String inputStreamIdArg, String outputStreamIdArg, String fallback) throws Exception {	
		String streamName 		= null;
		String outputStreamId 	= null;
	
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet( "http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		
	    String inputStreamId =  apiworker.sendGet("http://"+ login_ip + ":" + uiport + "/in_streams.json?complete=1", inputStreamIdArg, RECEIVERIDMODE, responseCookieContainer, login_ip, this, uiport);
		 
	    
	    String response = apiworker.sendGet("http://" + login_ip + ":" + uiport + "/out_streams.json", "", 77, responseCookieContainer, login_ip, this, uiport);
		JSONObject responseJson = new JSONObject(response.toString());
		JSONArray outputStreamsArray = responseJson.getJSONArray("streams");
	
		for (int i = 0; i < outputStreamsArray.length(); i++) {
			// System.out.println("before");
			JSONObject outputStream = outputStreamsArray.getJSONObject(i);
	
			streamName = outputStream.getString("name");
			if (streamName.equals(outputStreamIdArg)) {
				outputStreamId = outputStream.getString("out_id");
				break;
			}
		}
	    
		return new DriverReslut( apiworker.sendGet("http://" + login_ip + ":" + uiport + "/out_add_in.json?out_id=" + outputStreamId + "&source=" + inputStreamId + 
		"&fallback=" +  fallback, "", RECEIVERMODE,
		responseCookieContainer, login_ip, this, uiport));
	}
}
