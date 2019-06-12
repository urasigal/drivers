package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ReceiverDeleteActiveOutputSourceDriver  extends BroadcasterLoggableApiWorker implements TestDriver{
	
	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String outputStreamIdArg, String remoteBX_UI_port) throws Exception {	
		String streamName 		= null;
		String outputStreamId 	= null;
	
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet( "http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
	    
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
		
		// To do ....
	    String streamToDeleteParams =  apiworker.sendGet("http://"+ login_ip + ":" + uiport + "/out_info.json?out_id=" + outputStreamId , "", RECEIVER_ACTIVE_OUT_MODE, responseCookieContainer, login_ip, this, uiport);
		
	    String addressStreamToDelete = StringUtils.substringBefore(streamToDeleteParams, ":");
	    String nameStreamToDelete = StringUtils.substringAfter(streamToDeleteParams, "/");
	    
	    BroadcasterSingleInputStreamDeletionDriver broadcasterSingleInputStreamDeletionDriver = new BroadcasterSingleInputStreamDeletionDriver();
		return new DriverReslut( broadcasterSingleInputStreamDeletionDriver.removeInput(addressStreamToDelete, userName, userPass, nameStreamToDelete, remoteBX_UI_port));
	}
}

