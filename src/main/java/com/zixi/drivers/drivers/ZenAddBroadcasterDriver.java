package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;
import org.json.JSONObject;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ZenAddBroadcasterDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	 
	public DriverReslut addBroadcasterToCluster(String zenUserName, String zenUserPass, String zenLogin_ip, String zenUiport, 
	String bxName, String streaming_ip, String private_ip, String standby, String api_user, String api_password, String remote_access_key_name, String broadcaster_cluster_name) throws Exception
	{
		JSONObject json = new JSONObject(); 
		json.put("username", zenUserName).put("password", zenUserPass);
		// Logging to ZEN.  
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login" , zenUserName , zenUserPass, zenUiport, zenLogin_ip, json.toString().getBytes());
		String remote_access_key_id = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/access_keys", ZEN_GET_KEYS, cokieValuesForLoggin, zenLogin_ip, zenUiport, remote_access_key_name);
		String broadcaster_cluster_id = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/broadcaster_clusters", ZEN_CLUSTER_ID, cokieValuesForLoggin, zenLogin_ip, zenUiport, broadcaster_cluster_name);
		json = new JSONObject();
		json.put("name", bxName).
		put("streaming_ip", streaming_ip).
		put("private_ip", private_ip).
		put("standby", Integer.parseInt(standby)).
		put("api_user",api_user).
		put("api_password", api_password).
		put("remote_access_key_id", remote_access_key_id).
		put("broadcaster_cluster_id", Integer.parseInt(broadcaster_cluster_id));

		return new DriverReslut(apiworker.zenPost( "https://" + zenLogin_ip + "/api/broadcaster_clusters/" + Integer.parseInt(broadcaster_cluster_id) + "/broadcasters", 
		zenUserName, zenUserPass, zenUiport, zenLogin_ip, json.toString().getBytes(), ZEN_ADD_BROADCASTER, cokieValuesForLoggin));
	}	
}