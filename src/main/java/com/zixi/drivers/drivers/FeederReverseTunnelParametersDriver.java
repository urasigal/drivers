package com.zixi.drivers.drivers;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;
import static com.zixi.globals.Macros.*; 

public class FeederReverseTunnelParametersDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	
	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String on, String target_port, String target_ip, String remote_port) throws Exception  
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);		
		driverReslut = new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/add_ssh_tunnel.json?on=" + on + "&target_port=" + target_port + "&target_ip=" + target_ip + 
		"&remote_port=" + remote_port, "", UDPMODE, responseCookieContainer, login_ip, this, uiport));
		return driverReslut;
	}
}
