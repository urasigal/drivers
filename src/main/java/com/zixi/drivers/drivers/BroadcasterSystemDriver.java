package com.zixi.drivers.drivers;


import static com.zixi.tools.Macros.*;

import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterSystemDriver extends BroadcasterLoggableApiWorker implements TestDriver
{
	public String getCpuFromBroadcaster(String userName, String userPass, String login_ip, String uiport) throws Exception 
	{
		testParameters = new TestParameters( "userName:" + userName,  "userPass:"+ userPass, "login_ip:"+login_ip,  "uiport:"+uiport);
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass,
				login_ip, uiport);

		return apiworker.sendGet("http://"+ login_ip +":"+ uiport + "/sys_load.json", "", SYS_CPU,
				responseCookieContainer, login_ip, this, uiport);
		// TODO Auto-generated method stub
	}
}