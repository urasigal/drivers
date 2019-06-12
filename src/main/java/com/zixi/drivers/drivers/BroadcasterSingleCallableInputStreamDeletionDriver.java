package com.zixi.drivers.drivers;

import static com.zixi.tools.Macros.*;

import java.util.List;

import com.zixi.drivers.interfaces.ZCallableTask;
import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterLoggableApiWorker;


public class BroadcasterSingleCallableInputStreamDeletionDriver  extends BroadcasterLoggableApiWorker  implements ZCallableTask<String>, TestDriver{
	
	protected String[] parameters;
	
	public BroadcasterSingleCallableInputStreamDeletionDriver(String[] parameters) { this.parameters = parameters; }
	
	public String call() throws Exception 
	{
	
		testParameters = new TestParameters("login_ip:"+ parameters[0], "userName:"+ parameters[1] , "userPassword:"+ parameters[2], 
						"streamId:"+ parameters[3], "uiport:"+ parameters[4]);
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" +
						parameters[0] + ":" + parameters[4] + "/login.htm",  parameters[1], parameters[2],  parameters[0],  parameters[4]);
		
		String results = apiworker.sendGet("http://" + parameters[0] + ":" + parameters[4] +  "/zixi/remove_stream.json?" + "id" + "=" + parameters[3], 
				"", UDPMODE, responseCookieContainer, parameters[0], this, parameters[4]);
		
		return  results; 
	}
}
