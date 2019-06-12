package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterStreamMulticastDriver extends BroadcasterLoggableApiWorker implements TestDriver {
	
	public String testIMPL(String bx1_login_ip, String bx1_userName, String bx1_bxuserPassword, String bx1_uiport, String id) throws Exception{
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + bx1_login_ip + ":" + bx1_uiport + "/login.htm", bx1_userName, bx1_bxuserPassword, 
		bx1_login_ip, bx1_uiport);

		return apiworker.sendGet("http://" + bx1_login_ip + ":" + bx1_uiport + "/multicast_pool_info.json", id, MULTICAST_IP_MODE, responseCookieContainer, bx1_login_ip, this, bx1_uiport);
	}
}
