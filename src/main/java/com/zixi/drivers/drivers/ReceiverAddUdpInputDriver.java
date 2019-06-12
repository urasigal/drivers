package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.RECEIVERMODE;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ReceiverAddUdpInputDriver extends BroadcasterLoggableApiWorker implements TestDriver {

	public DriverReslut addUdpInput(String login_ip, String userName, String userPass, String uiport,
	String stream_name, String host, String port, String nic) throws Exception {
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);

		String result = apiworker.sendGet("http://" + login_ip + ":" + uiport + "/add_udp_in.json?name=" + stream_name + "&host=" + host +
		"&port=" + port + "&nic=" + nic, "", RECEIVERMODE, responseCookieContainer, login_ip, this, uiport);

		return new DriverReslut(result);
	}
	
}
