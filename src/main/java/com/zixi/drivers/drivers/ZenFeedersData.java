package com.zixi.drivers.drivers;

import org.json.JSONArray;
import org.json.JSONObject;
import com.zixi.tools.BroadcasterLoggableApiWorker;
import static com.zixi.globals.Macros.*;

public class ZenFeedersData extends BroadcasterLoggableApiWorker implements TestDriver{
	 
	public JSONObject getFeederData(String userName, String userPass, String host, String uiport, String feederName) throws Exception
	{
		JSONObject json = new JSONObject();
		json.put("username", userName).put("password", userPass);
		// Login to the ZEN.
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + host + "/login" , userName , userPass, uiport, host, json.toString().getBytes());
		String feederData = apiworker.zenSendGet("https://" + host + "/api/feeders", ZEN_FEEDER_DATA, cokieValuesForLoggin, host, uiport, feederName);
		json = new JSONObject(feederData);
		return json;
	}
	
	// Returns SEN feeder's ID by feeder's name.
	public static String getFeederID(String userName, String userPass, String host, String uiport, String feederName) throws Exception
	{
		ZenFeedersData zenFeedersData = new ZenFeedersData();
		return zenFeedersData.getFeederData( userName, userPass, host, uiport, feederName).getInt("id") + "";
	}
	
	// Returns reverse tunnel port.
	public static String getZenFeederRemoteTunnelPort(String userName, String userPass, String host, String uiport, String feederName) throws Exception
	{
		ZenFeedersData zenFeedersData = new ZenFeedersData();
		return zenFeedersData.getFeederData( userName, userPass, host, uiport, feederName).getJSONObject("tunnel").getInt("port") + "";
	}
	
	// Get ZEN feeder's input streams names .
	public static String getZenFeederInputStreams(String userName, String userPass, String host, String uiport, String feederName) throws Exception
	{
		Thread.sleep(30_000); 
		ZenFeedersData zenFeedersData = new ZenFeedersData();
		JSONArray inputs =  zenFeedersData.getFeederData( userName, userPass, host, uiport, feederName).getJSONObject("status").getJSONArray("inputs");
		JSONArray inputsNames = new JSONArray();
		for(int i = 0; i < inputs.length(); i++)
		{
			inputsNames.put(inputs.getJSONObject(i).getString("name"));
		}
		return inputsNames.toString();
	}
}
