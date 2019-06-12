package com.zixi.drivers.drivers;

import com.zixi.tools.*;
import com.zixi.drivers.tools.DriverReslut;
import  static com.zixi.globals.Macros.*;

public class BroadcasterCreateAddaptiveGroupDriver extends BroadcasterLoggableApiWorker implements TestDriver {


	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String name, String record, String zixi, String hls,
	String hds, String mpd, String mmt, String compress_zixi, String multicast, String streams, String bitrates, String max_time,
	String remux, String iframes, String file, String scte35, String ts_split, String order_ascending, String change_folder_chunks) throws Exception {

		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet( "http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);

		driverReslut = new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/set_adaptive_channel_with_streams.json?" + "name="
		+ name + "&record=" + record + "&zixi=" + zixi + "&hls=" + hls + "&hds=" + hds + "&mpd=" + mpd + "&mmt="
		+ mmt + "&compress-zixi=" + compress_zixi + "&multicast=" + multicast + streamsNames(streams) + streamsBitrates(bitrates)
		+ "&max_time=" + max_time + "&remux=" + remux + "&iframes=" + iframes + "&file=" + file + "&scte35=" + scte35 +
		"&ts_split=" + ts_split + "&order_ascending=" + order_ascending + "&change_folder_chunks=" + change_folder_chunks, "", 77, responseCookieContainer, login_ip, this, uiport));
		
		Thread.sleep(60_000);
		
		return driverReslut;
	}

	private static String streamsNames(String streams) { StringBuilder stringBuilder = new StringBuilder();
		String[] streamNames = streams.split(","); 
		for (String name : streamNames) { stringBuilder.append("&stream=" + name);}
		return stringBuilder.toString();
	}

	private static String streamsBitrates(String bitrates) {
		StringBuilder stringBuilder = new StringBuilder();
		String[] streamBits = bitrates.split(",");
		for (String bitrate : streamBits) {
			stringBuilder.append("&bitrate=" + bitrate);
		}
		return stringBuilder.toString();
	}

	public DriverReslut broadcasterAdaptiveGroupRecordingOn(String userName, String userPass, String login_ip,
			String uiport, String group_name, String record, String testid) throws Exception { 
		http://10.7.0.77:4444/zixi/record_hls.json?name=adaptive&record=1
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet( "http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);

		String result = apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/record_hls.json?name="
		+ group_name + "&record=" + record, "", ADAPTIVE_REC, responseCookieContainer, login_ip, this, uiport);
		
		if (result.equals("1"))
			return new DriverReslut("Recording has been started");
		else return new DriverReslut("Recording is not started");
	}
}
