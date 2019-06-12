package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.PUSHINMODE;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterAddStreamDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	
	public DriverReslut broadcasterAddHttpPullInputStream(String login_ip, String userName, String userPass, String uiport, String type, String id, String matrix, String max_outputs, 
	String mcast_out, String time_shift, String enc_type, String enc_key, String old, String fast_connect,
	String kompression, String rec_history, String rec_duration,String rec_path, String rec_template, String ts_http_url,
	String smoothing_latency) throws Exception 
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		return new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/add_stream.json?" +"type=" + type + "&id=" + id + "&matrix=" + matrix + "&max_outputs=" + max_outputs + 
		"&mcast_out=" + mcast_out + "&time_shift=" + time_shift + "&enc-type=" + enc_type + "&enc-key=" + enc_key + "&old=" + old + "&fast-connect=" + fast_connect + "&kompression=" + kompression +
		"&rec_history=" + rec_history + "&rec_duration=" + rec_duration + "&rec_path=" + rec_path + "&rec_template=" + rec_template + "&ts_http_url=" + ts_http_url + "&smoothing_latency=" + smoothing_latency, 
		id, PUSHINMODE, responseCookieContainer, login_ip, this, uiport)); 
	}
	
	public DriverReslut broadcasterAddHttpPushInputStream(String login_ip, String userName, String userPass, String uiport, String type, String name, 
	String matrix, String stream, String dec_type, String dec_key, String host, String port, String http, String buffer_size) throws Exception 
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		return new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/add_output.json?" +"type=" + type + "&name=" + name + "&matrix=" + matrix + 
		"&stream=" + stream + "&dec-type=" + dec_type + "&dec-key=" + dec_key + "&host=" + host + "&port=" + port + "&http=" + http + "&buffer_size=" + buffer_size, 
		"", PUSHINMODE, responseCookieContainer, login_ip, this, uiport)); 
	}
}
