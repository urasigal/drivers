package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.ZEN_ADD_RECEIVVER;
import static com.zixi.globals.Macros.ZEN_GET_ACCESS_TAG;
import static com.zixi.globals.Macros.ZEN_GET_KEYS;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ZenAddReceiverDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	 
	public DriverReslut addReceiver(String zenUserName, String zenUserPass, String zenLogin_ip, String zenUiport, 
	String receiverName, String receiverUser, String receiverPass, String shhKeyName, String accessTagName) throws Exception
	{
		JSONObject json = new JSONObject();
		json.put("username", zenUserName).put("password", zenUserPass);
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login" , zenUserName , zenUserPass, zenUiport, zenLogin_ip, json.toString().getBytes());
		String sshKeyId = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/access_keys", ZEN_GET_KEYS, cokieValuesForLoggin, zenLogin_ip, zenUiport, shhKeyName);
		String accessTagId = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/resource_tags/resource", ZEN_GET_ACCESS_TAG, cokieValuesForLoggin, zenLogin_ip, zenUiport, accessTagName);
		
		json = new JSONObject();
		String addFeederRequestPayload = json.put("name", receiverName).put("resource_tag_ids", new JSONArray().put(Integer.parseInt(accessTagId) ) ).put("api_user", receiverUser).
		put("api_password", receiverPass).put("remote_access_key_id", Integer.parseInt( sshKeyId)).toString();
		
		return new DriverReslut(apiworker.zenPost( "https://" + zenLogin_ip + "/api/receivers", zenUserName, zenUserPass, zenUiport, zenLogin_ip,
		addFeederRequestPayload.getBytes(), ZEN_ADD_RECEIVVER, cokieValuesForLoggin));
	}	
}