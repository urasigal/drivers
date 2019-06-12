package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;


import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterLoggableApiWorker;
import com.zixi.tools.JsonParser;

public class BxFileTransferAndVodSettingsDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	
	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String ft_download, 
    String ft_upload, String ft_auto_index, String ft_http, String ft_prog, String ft_encrypt,
    String ft_bitrate_cache, String ft_aggr, String ft_mtu, String ft_init_speed, String ft_cache, String ft_proxy_http_port, String ft_proxy_https_port,
    String max_download_bitrate, String max_upload_bitrate, String ft_zixi_index, String ft_scan_on_startup, String ft_hls_index, String ft_mpd_index) throws Exception
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		
		driverReslut =  new DriverReslut(JsonParser.getVodSettings(apiworker.sendGet("http://" + login_ip + ":" + uiport +
		"/apply_settings.json?" + "ft_download=" + ft_download + "&ft_upload=" + ft_upload + "&ft_auto_index=" + ft_auto_index +
		"&ft_http=" + ft_http + "&ft_prog=" + ft_prog + "&ft_encrypt=" + ft_encrypt + "&ft_bitrate_cache=" + ft_bitrate_cache + 
		"&ft_aggr=" + ft_aggr + "&ft_mtu=" + ft_mtu + "&ft_init_speed=" + ft_init_speed + "&ft_cache=" + ft_cache + 
		"&ft_proxy_http_port=" + ft_proxy_http_port + "&ft_proxy_https_port=" + ft_proxy_https_port + "&max_download_bitrate=" + max_download_bitrate + 
		"&max_upload_bitrate=" + max_upload_bitrate + "&ft_zixi_index=" + ft_zixi_index + "&ft_scan_on_startup=" + ft_scan_on_startup + 
		"&ft_hls_index=" + ft_hls_index + "&ft_mpd_index=" + ft_mpd_index, "", 77, responseCookieContainer, login_ip, this, uiport)));
		
		return driverReslut;
	}
}