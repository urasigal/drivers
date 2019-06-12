package com.zixi.drivers.drivers;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;
import static com.zixi.globals.Macros.*;

public class FeederAddHTTPPullInputDriver extends BroadcasterLoggableApiWorker implements TestDriver
{
	public DriverReslut addHttpIn( String userName, String userPass, String login_ip, String uiport, String http_url, String latency,
	String stream_name, String testid) throws Exception 
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		
		// http://10.7.0.75:4200/add_ts_tcp_input?url=http://10.7.0.63:7777/test.ts&latency=2000&name=http_output
		return new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport +  "/add_ts_tcp_input?url=" + http_url + "&latency=" + latency + "&name=" + stream_name, "",
		MSG, responseCookieContainer, login_ip, this, uiport));
	}
}