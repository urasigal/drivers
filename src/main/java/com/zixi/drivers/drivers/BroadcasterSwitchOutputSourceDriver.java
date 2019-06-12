package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterSwitchOutputSourceDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	public DriverReslut swithOutputSource(String userName, String userPass, String login_ip, String uiport,
		String out_stream_id, String alternative_stream) throws Exception {
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		String response = apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/redirect_client.json?id=" + out_stream_id + "&stream=" + alternative_stream + 
		"&update-remote=1", "", RECEIVERMODE, responseCookieContainer, login_ip, this, uiport);
		
		if(response.contains("switched to input"))
			return new DriverReslut("switched");
		else return new DriverReslut("not switched");
	}
}
