package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.FILE_FROM_FOLDER;
import static com.zixi.globals.Macros.UDPMODE;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterFailOverInputCreationDriver extends BroadcasterLoggableApiWorker implements TestDriver{

	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String type, String id, String matrix, String max_outputs, String mcast_out, String time_shift,
    String old, String fast_connect, String kompression, String enc_type, String enc_key, String latency, String max_bitrate, String keep_rtp, String group_components) throws Exception {

		String[] group_components_array = group_components.split(",");
		StringBuilder groupStreamNames = new StringBuilder();
		int length = group_components_array.length;
		
		for(int i = 0 ;i < length; i++)
		{
			groupStreamNames.append("&component=").append(group_components_array[i]);			
		}
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		
		return new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" +  uiport + "/zixi/add_stream.json?type=" +  type + "&id=" +  id + "&matrix=" + matrix + "&max_outputs=" + max_outputs +
		"&mcast_out=" + mcast_out + "&time_shift=" + time_shift + "&enc-type=" + enc_type + "&enc-key=" + enc_key + "&old=" + old + 
		"&fast-connect=" +  fast_connect + "&kompression=" + kompression + "&search_window=" + latency + "&max_bitrate=" + max_bitrate + 
		"&keep_rtp=" + keep_rtp + groupStreamNames.toString(), "", UDPMODE, responseCookieContainer, login_ip, this, uiport));
	}
}
