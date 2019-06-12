package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import org.json.JSONObject;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.ApiWorkir;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ZenDeleteChannelDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	 
	public DriverReslut deleteChannel(String zenUser, String zenPass, String zenLogin_ip, String zenUiPort, String channel_type, String channel_name) throws Exception
	{
		JSONObject json = new JSONObject();
		json.put("username", zenUser).put("password", zenPass);
		
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login" , zenUser , zenPass, zenUiPort, zenLogin_ip, json.toString().getBytes());
		
		String channelId = null;
		channelId = new ApiWorkir().zenSendGet("https://" + zenLogin_ip + "/api/channels/" + channel_type, ZEN_CHANNEL_ID, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, channel_name);
	
		return new DriverReslut(apiworker.zenSendDelete("https://" + zenLogin_ip + "/api/channels/" + channel_type + "/" + channelId , ZEN_DELETE_CHANNEL, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, channelId));
	}
	public DriverReslut deleteSource(String zenUser, String zenPass, String zenLogin_ip, String zenUiPort, String source_name) throws Exception
	{
		JSONObject json = new JSONObject();
		json.put("username", zenUser).put("password", zenPass);
		
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login" , zenUser , zenPass, zenUiPort, zenLogin_ip, json.toString().getBytes());

		String sourceId = null;
		sourceId = new ApiWorkir().zenSendGet("https://" + zenLogin_ip + "/api/sources", ZEN_CHANNEL_ID, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, source_name);
		
		return new DriverReslut(apiworker.zenSendDelete("https://" + zenLogin_ip + "/api/sources/" + sourceId , ZEN_DELETE_CHANNEL, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, source_name));
	}
	
	public DriverReslut deleteFeeder(String zenUser, String zenPass, String zenLogin_ip, String zenUiPort, String feeder_name) throws Exception
	{
		JSONObject json = new JSONObject();
		json.put("username", zenUser).put("password", zenPass);
		
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login" , zenUser , zenPass, zenUiPort, zenLogin_ip, json.toString().getBytes());

		String feederId = null;
		feederId = new ApiWorkir().zenSendGet("https://" + zenLogin_ip + "/api/feeders", ZEN_CHANNEL_ID, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, feeder_name);
		
		return new DriverReslut(apiworker.zenSendDelete("https://" + zenLogin_ip + "/api/feeders/" + feederId , ZEN_DELETE_CHANNEL, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, feeder_name));
	}
	public DriverReslut deleteTransProfile(String zenUser, String zenPass, String zenLogin_ip, String zenUiPort,
	String profile_name) {
		JSONObject json = new JSONObject();
		json.put("username", zenUser).put("password", zenPass);
		
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login" , zenUser , zenPass, zenUiPort, zenLogin_ip, json.toString().getBytes());

		String PRFID = null;
		PRFID = new ApiWorkir().zenSendGet("https://" + zenLogin_ip + "/api/transcoding_profiles", ZEN_PRFID_ID, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, profile_name);
		
		return new DriverReslut(apiworker.zenSendDelete("https://" + zenLogin_ip + "/api/transcoding_profiles/" + PRFID , ZEN_DELETE_CHANNEL, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, profile_name));
	}
	
	public DriverReslut deleteTarget(String zenUser, String zenPass, String zenLogin_ip, String zenUiPort, String type,
	String target_name) {
		JSONObject json = new JSONObject();
		json.put("username", zenUser).put("password", zenPass);
		
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login" , zenUser , zenPass, zenUiPort, zenLogin_ip, json.toString().getBytes());

		String TRGID = null;
		TRGID = new ApiWorkir().zenSendGet("https://" + zenLogin_ip + "/api/targets/" + type, ZEN_CHANNEL_ID, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, target_name);
		
		return new DriverReslut(apiworker.zenSendDelete("https://" + zenLogin_ip + "/api/targets/" + type + "/"+ TRGID , ZEN_DELETE_CHANNEL, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, target_name));
	}
}