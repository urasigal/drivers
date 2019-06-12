package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.TRANSSTAT;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterTransoderGetStatsDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	
	
	public DriverReslut getTranscoderStatistics(String userName, String userPass, String login_ip, String uiport, String stream_name, String testid) 
	throws Exception 
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, 
		login_ip, uiport);
		
		return new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/input_stream_stats.json?id=" + stream_name, "",
		TRANSSTAT, responseCookieContainer, login_ip, this, uiport)); 
	}
}
