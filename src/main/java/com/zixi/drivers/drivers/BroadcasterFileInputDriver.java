package com.zixi.drivers.drivers;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterLoggableApiWorker;
import static com.zixi.globals.Macros.*;

public class BroadcasterFileInputDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	
	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String type,
	String id, String matrix, String max_outputs, String mcast_out, String time_shift, String old, String fast_connect,
	String kompression, String enc_type, String enc_key, String path) throws Exception {
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);

		return new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport
		+ "/zixi/add_stream.json?type=" +  type +  "&id=" + id + "&matrix=" + matrix + "&max_outputs=" + max_outputs + 
		"&mcast_out=" + mcast_out + "&time_shift=" + time_shift + "&old=" + old + "&fast-connect=" + fast_connect + "&kompression=" + kompression + 
		"&enc-type=" + enc_type + "&enc-key=" + enc_key + "&path=" + path, "", UDPMODE, responseCookieContainer, login_ip, this, uiport));
	}
	
	public DriverReslut testIMPLFromRecordingFolder(String userName, String userPass, String login_ip, String uiport, String type,
	String id, String matrix, String max_outputs, String mcast_out, String time_shift, String old, String fast_connect,
	String kompression, String enc_type, String enc_key, String path) throws Exception {

		testParameters = new TestParameters("userName" + userName, "userPass" + userPass, "login_ip" + login_ip, "uiport" + uiport, "type" + type, "id" + id,
		"matrix" + matrix, "max_outputs" + max_outputs, "mcast_out" + mcast_out, "time_shift" + time_shift, "old" + old,
		"fast_connect" + fast_connect, "kompression" + kompression, "enc_type" + enc_type, "enc_key" + enc_key, "path" + path);

		// Logging to the broadcaster server.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);

		String fileNameInFolder = apiworker.sendGet("http://" + login_ip + ":" + uiport + "/ls?path=" + path + "&user=" + "" + "&pass=" + "" , "", FILE_FROM_FOLDER, responseCookieContainer, 
		login_ip, this, uiport);
		
		return new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport
		+ "/zixi/add_stream.json?type=" +  type +  "&id=" + id + "&matrix=" + matrix + "&max_outputs=" + max_outputs + 
		"&mcast_out=" + mcast_out + "&time_shift=" + time_shift + "&old=" + old + "&fast-connect=" + fast_connect + "&kompression=" + kompression + 
		"&enc-type=" + enc_type + "&enc-key=" + enc_key + "&path=" + path + "/" + fileNameInFolder, "", UDPMODE, responseCookieContainer, login_ip, this, uiport));
	}
	
	public String testIMPL(String userName, String userPass, String login_ip,
			String uiport, String id, String on) throws Exception 
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		return apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/edit_stream.json?id=" + id + "&on=" + on, "", UDPMODE, responseCookieContainer, login_ip, this, uiport);
	}
}
