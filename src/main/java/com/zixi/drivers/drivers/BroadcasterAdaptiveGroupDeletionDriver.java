package com.zixi.drivers.drivers;

import com.zixi.tools.*;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;

import static com.zixi.globals.Macros.*;

public class BroadcasterAdaptiveGroupDeletionDriver extends BroadcasterLoggableApiWorker implements TestDriver {

	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String name) throws Exception {
	
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet( "http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		
		driverReslut = new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/remove_adaptive_channel.json?name=" + name, "", UDPMODE,
		responseCookieContainer, login_ip, this, uiport));
		return driverReslut;
	}
}
