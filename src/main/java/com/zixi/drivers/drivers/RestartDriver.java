package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.LINE_MODE;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class RestartDriver extends BroadcasterLoggableApiWorker implements TestDriver{

	public DriverReslut restart(String userName, String userPass, String login_ip, String uiport) throws Exception 
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		Thread.sleep(30_000); 
		return new DriverReslut( apiworker.sendGet("http://" + login_ip + ":" + uiport + "/quit", 
		"", LINE_MODE, responseCookieContainer, login_ip, this, uiport));
	}
}