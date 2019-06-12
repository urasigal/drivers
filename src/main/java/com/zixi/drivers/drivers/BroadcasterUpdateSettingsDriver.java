package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.CNANGE_SETTINGS_MODE;
import static com.zixi.globals.Macros.GOOD;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterUpdateSettingsDriver extends BroadcasterLoggableApiWorker implements TestDriver {
	
	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport,
	String server_id, String gui_web_port, String uname, String aname, String ft_max_quota, String max_cpu, 
	String max_mem, String max_in_bandwidth, String max_out_bandwidth, String admin_https, String use_operator, String use_user, 
	String use_observer, String private_port, String public_port ) throws Exception{
		
		driverReslut = new DriverReslut();
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		
		if( apiworker.sendGet("http://" + login_ip + ":" + uiport + "/apply_settings.json?" + "server_id=" + server_id + "&gui_web_port=" + gui_web_port +
			"&uname=" + uname + "&aname=" + aname + "&ft_max_quota=" + ft_max_quota + "&max_cpu=" + max_cpu + "&max_mem=" + max_mem + "&max_in_bandwidth=" + max_in_bandwidth +
			"&max_out_bandwidth=" + max_out_bandwidth + "&admin_https=" + admin_https + "&use_operator=" + use_operator + "&use_user=" + use_user + "&use_observer=" + use_observer +
			"&private_port=" + private_port + "&public_port=" + public_port, "", CNANGE_SETTINGS_MODE, responseCookieContainer, login_ip, this, uiport).equals(GOOD))
		{
			apiworker.sendGet("http://" + login_ip + ":" + uiport + "/quit", "", CNANGE_SETTINGS_MODE, responseCookieContainer, login_ip, this, uiport);
			Thread.sleep(30000);
			driverReslut.setResult("GOOD");
			return driverReslut;
		}
		driverReslut.setResult("No settings were applyed");
		return driverReslut;
	}
	
	public DriverReslut enableHTTP(String login_ip, String userName, String userPass, String uiport, String flv_on, 
			String hls_on, String mpd_on, String pls_on, String http_out_ip, String http_out_port,
			String hls_chunk_time, String hls_chunks, String http_auth_cahce_timeout, String http_on, String https_on, String https_out_port, 
			String hls_dvr_duration_s, String hls_no_mem_chunks, String hls_no_dvr, String hls_vod_abs_path_on, String http_ts_auto_in,
			String http_ts_auto_out, String http_ts_buffer_size, String http_ts_smoothing_latency, String tcp_congestion_algo) throws Exception{
			
		driverReslut = new DriverReslut();
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		
		if( apiworker.sendGet("http://" + login_ip + ":" + uiport + "/apply_settings.json?" + "flv_on=" + flv_on + "&hls_on=" + hls_on + "&mpd_on=" + mpd_on + "&pls_on=" + pls_on +
				 "&http_out_ip=" + http_out_ip + "&http_out_port=" + http_out_port + "&hls_chunk_time=" + hls_chunk_time + "&hls_chunks=" + hls_chunks + "&http_auth_cahce_timeout=" + http_auth_cahce_timeout + 
				 "&http_on=" + http_on +  "&https_on=" + https_on + "&https_out_port=" + https_out_port + "&hls_dvr_duration_s=" + hls_dvr_duration_s + "&hls_no_mem_chunks=" + hls_no_mem_chunks +
				 "&hls_no_dvr=" + hls_no_dvr +  "&hls_vod_abs_path_on=" + hls_vod_abs_path_on + "&http_ts_auto_in=" + http_ts_auto_in +  
				 "&http_ts_auto_out=" + http_ts_auto_out + "&http_ts_buffer_size=" + http_ts_buffer_size + "&http_ts_smoothing_latency=" + http_ts_smoothing_latency + "&tcp_congestion_algo=" + tcp_congestion_algo,
				 "", CNANGE_SETTINGS_MODE, responseCookieContainer, login_ip, this, uiport).equals(GOOD))
		{
			apiworker.sendGet("http://" + login_ip + ":" + uiport + "/quit", "", CNANGE_SETTINGS_MODE, responseCookieContainer, login_ip, this, uiport);
			Thread.sleep(30000);
			driverReslut.setResult("GOOD");
			return driverReslut;
		}
		driverReslut.setResult("No settings were applyed");
		return driverReslut;
	}
}
