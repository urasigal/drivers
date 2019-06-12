package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ZenAddAdaptiveChannelDriver  extends BroadcasterLoggableApiWorker implements TestDriver{
	 
	public DriverReslut addAdaptiveChannel(String zenUser, String zenPass, String zenLogin_ip, String zenUiPort, String type,
	String name, String resource_tag_ids, String broadcaster_cluster_id, String adaptive, String delivery, String is_transcoding, 
	String is_source_included, String inputs, String log_scte, String profile_names) throws Exception
	{  
		//Thread.sleep(180_000);
		JSONObject json = new JSONObject(); // JSON container for log in.  
		json.put("username", zenUser).put("password", zenPass);
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login" , zenUser , zenPass, zenUiPort, zenLogin_ip, json.toString().getBytes());
		
		// Get access tag ID by its name (supported just one tag for now).
		String accessTagId = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/resource_tags/resource", ZEN_GET_ACCESS_TAG, cokieValuesForLoggin, zenLogin_ip, zenUiPort, resource_tag_ids);
		// Get process cluster ID by its name.
		String broadcaster_cluster_id_ = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/broadcaster_clusters",
		ZEN_CLUSTER_ID, cokieValuesForLoggin, zenLogin_ip, zenUiPort, broadcaster_cluster_id);
		
		json = new JSONObject(); // // POST request JSON container for log in.
		json.
			put("type", type).
			put("name", name).
			put("resource_tag_ids", new JSONArray().put(Integer.parseInt(accessTagId))).
			put("broadcaster_cluster_id", broadcaster_cluster_id_).
			put("adaptive", Boolean.parseBoolean(adaptive)).
			put("delivery", Boolean.parseBoolean(delivery)).
			put("is_transcoding", Boolean.parseBoolean(is_transcoding)).
			put("is_source_included", Boolean.parseBoolean(is_source_included));
		
			//////////////////////////////////////////////////////////////////////
			// In a case of adaptive channel.
			if(type.equals("adaptive"))
			{
				ArrayList<String[]> splittedSourcesData = convertSourceDatatoList(zenLogin_ip, zenUiPort, inputs, cokieValuesForLoggin);
				JSONArray bitrates = new JSONArray();
				for(int i = 0; i < splittedSourcesData.size(); i++)
				{
					bitrates.put( new JSONObject().
												   put("profile_id", JSONObject.NULL).
												   put("source_id", Integer.parseInt(splittedSourcesData.get(i)[1])).
												   put("name", splittedSourcesData.get(i)[0]).
												   put("kbps", Integer.parseInt(splittedSourcesData.get(i)[2])));
				}
				json.put("bitrates", bitrates);
			}
			
			//////////////////////////////////////////////////////////////////////
			// In a case of transcoded channel.
			if(type.equals("transcoded")) // Case of transcoded channel.
			{
				ArrayList<String[]> splittedSourcesData = convertSourceDatatoList(zenLogin_ip, zenUiPort, inputs, cokieValuesForLoggin);
				JSONArray bitrates = new JSONArray();
				//String[] transcodingProfilesNames = profile_names.split("@");
				
				JSONArray transcodedProfilesJsons = new JSONArray(apiworker.zenSendGet("https://" + zenLogin_ip + "/api/transcoding_profiles", 
				ZEN_GET_TRANS_PROFS, cokieValuesForLoggin, zenLogin_ip, zenUiPort, profile_names));
				
				for(int i = 0; i < splittedSourcesData.size(); i++)
				{
					
//					[
//					  [
//					    "1280x720",
//					    12
//					  ],
//					  [
//					    "1920x1080",
//					    11
//					  ]
//					]
					for(int j = 0; j < transcodedProfilesJsons.length(); j++)
					{
						bitrates.put( new JSONObject().
						    put("profile_id", transcodedProfilesJsons.getJSONArray(j).getInt(1)).
						    put("source_id", Integer.parseInt(splittedSourcesData.get(i)[1])).
						    put("name", splittedSourcesData.get(i)[0]).
						    put("kbps", Integer.parseInt(splittedSourcesData.get(i)[2])));
					}
				}
				json.put("bitrates", bitrates);
			}
			if(type.equals("delivery"))
			{
				ArrayList<String[]> splittedSourcesData = convertSourceDatatoList(zenLogin_ip, zenUiPort, inputs, cokieValuesForLoggin);
				 json.put("source_id", Integer.parseInt(splittedSourcesData.get(0)[1]));
				 if (splittedSourcesData.size() > 1) // Alternative source 
					 json.put("alt_source_id", Integer.parseInt(splittedSourcesData.get(1)[1]));
			}
			
		return new DriverReslut(apiworker.zenPost( "https://" + zenLogin_ip + "/api/channels/" + type , zenUser, zenPass, zenUiPort, zenLogin_ip, 
		json.toString().getBytes(), ZEN_ADD_FEEDER_SOURCE, cokieValuesForLoggin));
	}
	
	private ArrayList<String[]> convertSourceDatatoList( String zenLogin_ip, String zenUiPort, String inputs, String[] cokieValuesForLoggin)
	{
		String[] inputsNames = inputs.split("@");
		ArrayList<String[]> splittedSourcesData = new ArrayList<>();
		
		for(int i = 0 ; i < inputsNames.length; i++)
		{
			JSONObject sourceObj = new JSONObject(apiworker.zenSendGet("https://" + zenLogin_ip + "/api/sources", 
								ZEN_GET_SOURCE_OBJ, cokieValuesForLoggin, zenLogin_ip, zenUiPort, inputsNames[i]));
			String sourceNameId = sourceObj.getInt("id") + "";
			String bitrate = null;
			try {
				bitrate = ((int)sourceObj.getJSONObject("status").getDouble("bitrate")) + "";
			}catch(Exception e) {
				bitrate = "3000"; // Temporary default.
			}
			String [] sourceData = {inputsNames[i], sourceNameId, bitrate};
			splittedSourcesData.add(sourceData);
		}
		return splittedSourcesData;
	}
}

