package com.zixi.drivers.drivers;

import java.io.File;
import java.io.FileInputStream;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;
import static com.zixi.globals.Macros.*;

public class ZenAddFeederDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	 
	public DriverReslut addFeeder(String userName, String userPass, String host, String uiport, String feederName, String feederUser, String feederPass, String shhKeyName, String accessTagName) throws Exception
	{
		JSONObject json = new JSONObject();
		json.put("username", userName).put("password", userPass);
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + host + "/login" , userName , userPass, uiport, host, json.toString().getBytes());
		String sshKeyId = apiworker.zenSendGet("https://" + host + "/api/access_keys", ZEN_GET_KEYS, cokieValuesForLoggin, host, uiport, shhKeyName);
		String accessTagId = apiworker.zenSendGet("https://" + host + "/api/resource_tags/resource", ZEN_GET_ACCESS_TAG, cokieValuesForLoggin, host, uiport, accessTagName);
		json = new JSONObject();
		String addFeederRequestPayload = json.put("name", feederName).put("resource_tag_ids", new JSONArray().put(Integer.parseInt(accessTagId) ) ).put("api_user", feederUser).
										 put("api_password", feederPass).put("remote_access_key_id", Integer.parseInt( sshKeyId)).toString();
		return new DriverReslut(apiworker.zenPost( "https://" + host + "/api/feeders", userName, userPass, uiport, host, addFeederRequestPayload.getBytes(), ZEN_ADD_FEEDER, cokieValuesForLoggin));
	}	
}
