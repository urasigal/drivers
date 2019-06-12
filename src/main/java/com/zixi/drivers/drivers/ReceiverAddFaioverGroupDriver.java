package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.RECEIVERIDMODE;
import static com.zixi.globals.Macros.RECEIVERMODE;

import java.util.ArrayList;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ReceiverAddFaioverGroupDriver extends BroadcasterLoggableApiWorker implements TestDriver {

	public DriverReslut addFailoverGroup(String login_ip, String userName, String userPass, String uiport,
	String group_name, String group_streams, String latency, String max_bitrate) throws Exception {
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		StringBuffer components = new StringBuffer();
		String streamsSplitted[] = group_streams.split("@");
		ArrayList<String> streamNames = new ArrayList<>();
		ArrayList<String> streamPrior = new ArrayList<>();
		
		for(int i = 0; i < streamsSplitted.length; i ++)
		{
			if(i % 2 == 0)
			{
				streamNames.add(streamsSplitted[i]);
			}else streamPrior.add(streamsSplitted[i]);
		}
		
		for(int i = 0; i < streamNames.size(); i++)
		{
			String streamId = apiworker.sendGet("http://" + login_ip + ":" + uiport + "/in_streams.json?complete=1", streamNames.get(i), RECEIVERIDMODE,
			responseCookieContainer, login_ip, this, uiport);
			streamNames.set(i, streamId); 
					
		}
		
		for(int i = 0; i < streamNames.size(); i++)
		{
			components.append("&component=" + streamNames.get(i) + "&priority=" + streamPrior.get(i));
		}
		String result = apiworker.sendGet("http://" + login_ip + ":" + uiport + "/add_failover_in.json?name=" + group_name +
		"&latency=" + latency + "&max_bitrate=" + max_bitrate + components, "", RECEIVERMODE, 
		responseCookieContainer, login_ip, this, uiport);

		return new DriverReslut(result);
	}
	
}