package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.LINE_MODE;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterDeleteFromFileDriver extends BroadcasterLoggableApiWorker implements TestDriver{

	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String path, String user, String pass) throws Exception 
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		
		return new DriverReslut( apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zift_delete?path=" + path + "&user=" + user + "&pass=" + pass, 
		"", LINE_MODE, responseCookieContainer, login_ip, this, uiport));
	}
}

