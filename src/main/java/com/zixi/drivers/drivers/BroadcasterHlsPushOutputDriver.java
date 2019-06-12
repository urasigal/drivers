package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.PUSHOUTMODE;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterHlsPushOutputDriver extends BroadcasterLoggableApiWorker implements TestDriver {
	
	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String name, String matrix, String stream,
			String type, String url, String cleanup, String encap, String no_tls) throws Exception
	{
		//login to broadcaster server.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		
		
		// Use broadcaster's API - send HHTP GET request - create an input stream. Then the result will be returned in appropriate result object.
		driverReslut = new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/add_output.json?" + "name=" + name
		+ "&matrix=" + matrix + "&stream=" + stream  + "&type=" + type + "&url=" + url + "&cleanup=" + cleanup + "&encap=" + encap + 
		"&no_tls=" + no_tls, "", PUSHOUTMODE, responseCookieContainer, login_ip, this, uiport));
		
		return driverReslut;
	}
}