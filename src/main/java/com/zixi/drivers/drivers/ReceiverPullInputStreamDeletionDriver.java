package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ReceiverPullInputStreamDeletionDriver extends BroadcasterLoggableApiWorker implements TestDriver{

	//protected ApiWorkir streamCreator = new ApiWorkir();
	
	public DriverReslut testIMPL(String userName, String userPass, String login_ip,
	String uiport, String id) throws Exception {
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet( "http://" + login_ip + ":" + uiport + "/login.htm", userName,
		userPass, login_ip, uiport);
		
	    String streamId =  apiworker.sendGet("http://"+ login_ip + ":" + uiport + "/in_streams.json?complete=1", id, RECEIVERIDMODE, responseCookieContainer, login_ip, this, uiport);
		 
		return new DriverReslut( apiworker.sendGet("http://" + login_ip + ":" + uiport + "/remove_in.json?id=" + streamId, id, RECEIVERDELETIONMODE,
		responseCookieContainer, login_ip, this, uiport));
	}
}
 