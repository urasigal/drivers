package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class FeederUdpOutToFfmpegDriver extends BroadcasterLoggableApiWorker implements TestDriver{

	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String name, String mip, String port, String ip,
	String prog, String chan, String oh, String op, String onic, String ottl, String osmooth) throws Exception {
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet( "http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		
		driverReslut =  new  DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/set_udp_out?name=" + name + "&mip=" + mip + "&port=" + port + "&ip=" + ip + "&prog=" + prog + "&chan=" + chan +
		"&oh=" + oh + "&op=" + op + "&onic=" + onic + "&ottl=" + ottl + "&osmooth=" + osmooth ,"", UDPMODE, responseCookieContainer, login_ip, this, uiport));
		
		return driverReslut;
	}
}
