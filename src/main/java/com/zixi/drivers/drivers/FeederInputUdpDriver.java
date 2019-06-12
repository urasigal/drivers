package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class FeederInputUdpDriver extends BroadcasterLoggableApiWorker implements TestDriver{

		public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String mip, String port, String ip, String name,
		String ssm, String rtp_type) throws Exception {
	   
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet( "http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
	
		driverReslut =  new DriverReslut(apiworker.sendGet("http://" + login_ip  + ":" + uiport + "/add_input?mip="  + mip + "&port=" + port + "&ip=" + ip + "&name=" + 
		name + "&ssm=" + ssm + "&rtp_type=" + rtp_type, "", UDPMODE, responseCookieContainer, login_ip, this, uiport));
		
		return driverReslut;
	}
}
