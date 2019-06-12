package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.PUSHINMODE;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.ApiWorkir;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterRtmpInCreationDriver extends BroadcasterLoggableApiWorker implements TestDriver {


	final private String rtime_shift = "time_shift";
	final private String rmax_outputs = "max_outputs";
	final private String rid = "id";
	final private String ron = "on";
	final private String rtype = "type";
	final private String rmcast_out = "mcast_out";
	final private String rmcast_ip = "mcast_ip";
	final private String rmcast_port = "mcast_port";
	final private String rcomplete = "complete";

	final private String rrtmp_bitrate = "rtmp_bitrate";
	final private String rrtmp_nulls = "rtmp_nulls";
	final private String rrtmp_name = "rtmp_name";
	final private String rrtmp_latency = "rtmp_latency";
	final private String rrtmp_url = "rtmp_url";
	final private String rrtmp_user = "rtmp_user";

	public DriverReslut testIMPL(String userName, String userPass, String login_ip,
	String rtmp_nulls, String id, String rtmp_url, String rtmp_name, String time_shift, String mcast_ip, String mcast_force,
	String mcast_port, String type, String rtmp_user, String rtmp_bitrate, String rtmp_passwd, String uiport, String mcast_ttl, String rtmp_latency,
	String mcast_out, String complete, String max_outputs, String on) throws Exception {
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);

		driverReslut = new DriverReslut( apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/add_stream.json?" + rtype + "=" + type + "&" + rid + "=" + id + "&" + rmax_outputs + "=" + 
		max_outputs + "&" + rmcast_out + "=" + mcast_out + "&" + "mcast_force" + "=" + mcast_force + "&" + "mcast_ip" + "=" + mcast_ip + "&" + "mcast_port" + "=" + mcast_port
		+ "&" + "mcast_ttl" + "=" + mcast_ttl + "&" + rtime_shift + "=" + time_shift + "&" + rcomplete + "=" + complete + "&" + ron + "=" + on + "&"
		+ rrtmp_bitrate + "=" + rtmp_bitrate + "&" + rrtmp_nulls + "=" + rtmp_nulls + "&" + rrtmp_name
		+ "=" + rtmp_name + "&" + rrtmp_latency + "=" + rtmp_latency + "&" + rrtmp_url + "=" + rtmp_url
		+ "&" + "rtmp_user" + "=" + rtmp_user + "&" + "rtmp_passwd" + "=" + rtmp_passwd, id, PUSHINMODE, responseCookieContainer, login_ip, this, uiport));
		
		return driverReslut;
	}

	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String enc_type, String enc_key, String disconnect_low_br,
	String rtmp_nulls, String id, String rtmp_url, String rtmp_name, String time_shift, String mcast_ip, String mcast_force, String mcast_port, 
	String type, String rtmp_user, String rtmp_bitrate, String rtmp_passwd, String uiport, String mcast_ttl, String rtmp_latency, String mcast_out,
	String complete, String max_outputs, String on) throws Exception 
	{
//		//ApiWorkir streamCreator = new ApiWorkir(); 
//		testParameters = new TestParameters("userName:" + userName, "userPass:"
//		+ userPass, "login_ip:" + login_ip, "enc_type" + enc_type, "enc-key" + enc_key, "rtmp_nulls:" + rtmp_nulls, "disconnect_low_br" + disconnect_low_br, 
//		"id:" + id, "rtmp_url:" + rtmp_url, "rtmp_name:" + rtmp_name, "time_shift:" + time_shift, "mcast_ip:" + mcast_ip,
//		"mcast_force:" + mcast_force, "mcast_port:" + mcast_port, "type:" + type, "rtmp_user:" + rtmp_user, ":rtmp_bitrate" + rtmp_bitrate, "rtmp_passwd:" + rtmp_passwd,
//		"uiport:" + uiport, "mcast_ttl:" + mcast_ttl, "rtmp_latency:" + rtmp_latency, "mcast_out:" + mcast_out, "complete:" + complete, "max_outputs:" + max_outputs, "on:" + on);

		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);

		driverReslut = new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/add_stream.json?" + rtype + "=" + type + "&" + rid + "="
		+ id + "&enc-type="+ enc_type + "&enc-key="+ enc_key +"&disconnect_low_br=" + disconnect_low_br + "&rmax_outputs=" + max_outputs + "&"
		+ rmcast_out + "=" + mcast_out + "&" + "mcast_force" + "=" + mcast_force + "&" + rmcast_ip + "="
		+ mcast_ip + "&" + rmcast_port + "=" + mcast_port + "&" + "mcast_ttl" + "=" + mcast_ttl + "&"
		+ rtime_shift + "=" + time_shift + "&" + rcomplete + "=" + complete + "&" + ron + "=" + on + "&"
		+ rrtmp_bitrate + "=" + rtmp_bitrate + "&" + rrtmp_nulls + "=" + rtmp_nulls + "&" + rrtmp_name
		+ "=" + rtmp_name + "&" + rrtmp_latency + "=" + rtmp_latency + "&" + rrtmp_url + "=" + rtmp_url
		+ "&" + rrtmp_user + "=" + rtmp_user + "&" + "rtmp_passwd" + "=" + rtmp_passwd, id, PUSHINMODE,responseCookieContainer, login_ip, this, uiport));
		
		return driverReslut;
	}
}
