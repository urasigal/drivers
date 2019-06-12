package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterRtmpPushInputStreamDriver extends BroadcasterLoggableApiWorker implements TestDriver {


	public DriverReslut testIMPL(String userName, String userPass, String login_ip,
	String uiport, String type, String id, String matrix,String max_outputs, String mcast_out, String time_shift,
	String old, String fast_connect, String kompression, String enc_type, String enc_key, String rec_history,
	String rec_duration, String rtmp_url, String rtmp_name, String rtmp_user) throws Exception {
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet( "http://" + login_ip + ":" + uiport + "/login.htm", userName,userPass, login_ip, uiport);

		driverReslut =  new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/add_stream.json?"
		+ "type" + "=" + type + "&id=" + id + "&matrix=" + matrix + "&max_outputs=" + max_outputs
		+ "&mcast_out=" + mcast_out + "&time_shift=" + time_shift + "&old=" + old + "&fast-connect=" + fast_connect +
		"&kompression=" + kompression + "&enc-type=" + enc_type
		+ "&enc-key=" + enc_key + "&rec_history=" + rec_history + "&rec_duration=" + rec_duration
		+ "&rtmp_url=" + rtmp_url + "&rtmp_name=" + rtmp_name + "&rtmp_user=" + rtmp_user, "",
		PUSHINMODE, responseCookieContainer, login_ip, this, uiport));
		
		return driverReslut;
	}

}
