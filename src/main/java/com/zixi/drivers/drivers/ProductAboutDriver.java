package com.zixi.drivers.drivers;

import com.zixi.tools.BroadcasterLoggableApiWorker;
import com.zixi.tools.JsonParser;


public class ProductAboutDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	
	public String version;
	
	public  String getBroadcasterVersion(String login_ip, String uiport, String userName, String userPassword) throws Exception
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPassword, login_ip, uiport);
		version = JsonParser.getBroadcasterVersion(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/json_about" , "",  77, responseCookieContainer, login_ip, this, uiport));
		return version;
	}
}
