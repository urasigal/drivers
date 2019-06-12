package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.PUSHOUTMODE;
import static com.zixi.tools.Macros.GET_ALL_STREAMS;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterHlsPullInputDriver extends BroadcasterLoggableApiWorker implements TestDriver {
	
	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String type, 
	String id, String matrix, String max_outputs, String mcast_out, String time_shift, String enc_type, String enc_key, 
	String old, String fast_connect, String kompression, String rec_history, String rec_duration,  String rec_path, 
	String rec_template, String hls_url, String hls_adaptive) 
	throws Exception {
		// login to broadcaster server.
		responseCookieContainer = broadcasterInitialSecuredLogin.
		sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		
		// Use broadcaster's API - send HHTP GET request - create an input stream. Then the result will be returned in appropriate result object.
		driverReslut = new DriverReslut(apiworker.sendGet("http://"+ login_ip + ":" + uiport + "/zixi/add_stream.json?"+
		"type=" + type + "&id=" + id + "&matrix=" + matrix + "&max_outputs=" + max_outputs
		+ "&mcast_out=" + mcast_out + "&time_shift="+ time_shift + "&enc-type=" + enc_type + "&enc-key=" + enc_key +
		"&old="+ old + "&fast-connect=" + fast_connect + "&kompression=" + kompression + "&rec_history=" + rec_history +
		"&rec_duration=" + rec_duration + "&rec_path="+ "&rec_template=" + rec_template + 
		"&hls_url=" + hls_url + "&hls_adaptive=" + hls_adaptive, "", PUSHOUTMODE, responseCookieContainer, login_ip, this, uiport));
		
		Thread.sleep(40_000);
		return driverReslut;
	}
}
