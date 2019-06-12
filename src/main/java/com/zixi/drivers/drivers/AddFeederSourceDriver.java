package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;
import org.json.JSONArray;
import org.json.JSONObject;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.ApiWorkir;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class AddFeederSourceDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	 
	public DriverReslut addFeederSource(String zenUser, String zenPass, String zenLogin_ip, String zenUiPort, String sourceName,
	String broadcaster_cluster_name, String feeder_name, String broadcaster_name, String input_id, String max_bitrate, String latency, 
	String monitor_pids_change, String resource_tag_ids, String password, String encryption, String encryption_key, String allow_outputs,
	String outputs_password, String monitor_only, String cleanup_outputs, String testid)throws Exception
	{  
		JSONObject json = new JSONObject();
		json.put("username", zenUser).put("password", zenPass);
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login" , zenUser , zenPass, zenUiPort, zenLogin_ip, json.toString().getBytes());
		
		String accessTagId = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/resource_tags/resource", ZEN_GET_ACCESS_TAG, cokieValuesForLoggin, zenLogin_ip, zenUiPort, resource_tag_ids);
		String broadcaster_cluster_id = null;
		for(int i = 0; i < 4; i++ )
		{
			Thread.sleep(20_000);
			broadcaster_cluster_id = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/broadcaster_clusters", ZEN_CLUSTER_ID, cokieValuesForLoggin, zenLogin_ip, zenUiPort, broadcaster_cluster_name);
			if ( !(broadcaster_cluster_id.equals("cluster name not found")))
			{
				break;
			}
			if (i == 3)
			{
				return new DriverReslut("cluster name not found");
			}
		}
		
		String feeder_id = null;
		if(! feeder_name.equals(""))
		{
			feeder_id = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/feeders", ZEN_GET_FEEDER_ID, cokieValuesForLoggin, zenLogin_ip, zenUiPort, feeder_name);
		}
		
		json = new JSONObject();
		json.
			put("name", sourceName).
			put("resource_tag_ids", new JSONArray().put(Integer.parseInt(accessTagId))).
			put("broadcaster_cluster_id", broadcaster_cluster_id);
			
			// Case of feeder source.
			if(feeder_id != null)
			{
				json.put("feeder_id", feeder_id);
			}
			else
			{
				json.put("feeder_id", JSONObject.NULL);
			}
			// Case of broadcaster source
			String bx_id = null;
			if (broadcaster_name.equals(""))
			{
				json.put("broadcaster_id", JSONObject.NULL);
			}else
			{
				//json.put("broadcaster_id", broadcaster_id);
				String[] clstr_bx = broadcaster_name.split("@");
				String clstr_name = clstr_bx[0];
				String bx_name = clstr_bx[1];
				
				String clstr_id = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/broadcaster_clusters", ZEN_CLUSTER_ID, cokieValuesForLoggin, zenLogin_ip, zenUiPort, clstr_name);
				
				bx_id = new ApiWorkir().zenSendGet("https://" + zenLogin_ip + "/api/broadcaster_clusters/" + clstr_id + 
				"/broadcasters", ZEN_GET_BX_ID,
				cokieValuesForLoggin, zenLogin_ip, zenUiPort, bx_name);
				json.put("broadcaster_id", bx_id);
			}
			json.put("input_id", input_id).
			put("max_bitrate", max_bitrate).
			put("latency", Integer.parseInt(latency)).
			put("monitor_pids_change", Integer.parseInt(monitor_pids_change)).
			put("password", password).
			put("encryption", encryption).
			put("encryption_key", encryption_key).
			put("allow_outputs", Boolean.parseBoolean(allow_outputs)).
			put("outputs_password", outputs_password).
			put("monitor_only", Boolean.parseBoolean(monitor_only)).
			put("cleanup_outputs", Boolean.parseBoolean(cleanup_outputs));

		return new DriverReslut(apiworker.zenPost( "https://" + zenLogin_ip + "/api/sources", zenUser, zenPass, zenUiPort, zenLogin_ip, 
		json.toString().getBytes(), ZEN_ADD_FEEDER_SOURCE, cokieValuesForLoggin));
 	}
	
	
	public DriverReslut addHitlessSource(String zenUser, String zenPass, String zenLogin_ip, String zenUiPort,
	String source_name, String broadcaster_cluster_name, String feeder_id, String broadcaster_id, String target_broadcaster_id, 
	String input_id, String max_bitrate, String hitless_failover_source_ids, String latency, String monitor_pids_change, String resource_tag_ids,
	String password, String encryption, String encryption_key, String allow_outputs, String outputs_password, String monitor_only) throws InterruptedException
	{
		JSONObject json = new JSONObject();
		json.put("username", zenUser).put("password", zenPass);
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login" , zenUser , zenPass, zenUiPort, zenLogin_ip, json.toString().getBytes());
		
		json = new JSONObject();
		
		String accessTagId = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/resource_tags/resource", ZEN_GET_ACCESS_TAG, cokieValuesForLoggin, zenLogin_ip, zenUiPort, resource_tag_ids);
		
		String broadcaster_cluster_id = null;
		for(int i = 0; i < 4; i++ )
		{
			Thread.sleep(20_000);
			broadcaster_cluster_id = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/broadcaster_clusters", ZEN_CLUSTER_ID, cokieValuesForLoggin, zenLogin_ip, zenUiPort, broadcaster_cluster_name);
			if ( !(broadcaster_cluster_id.equals("cluster name not found")))
			{
				break;
			}
			if (i == 3)
			{
				return new DriverReslut("cluster name not found");
			}
		}
		json.
		put("name", source_name).
		put("resource_tag_ids", new JSONArray().put(Integer.parseInt(accessTagId))).
		put("broadcaster_cluster_id", broadcaster_cluster_id);
		if(feeder_id.equals(""))
			json.put("feeder_id", JSONObject.NULL);
		if(broadcaster_id.equals(""))
			json.put("broadcaster_id", JSONObject.NULL);
		json.put("target_broadcaster_id", Integer.parseInt(target_broadcaster_id));
		if(input_id.equals(""))
			json.put("input_id", JSONObject.NULL);
		json.put("max_bitrate", Integer.parseInt(max_bitrate));
		
		String componentSources[] = hitless_failover_source_ids.split("@");
		JSONArray sourcesIds = new JSONArray();
		String INID = null;
		// Get source IDs and put them into JSONArray object.
		for(int i = 0; i < componentSources.length; i++)
		{
			INID = new ApiWorkir().zenSendGet("https://" + zenLogin_ip + "/api/sources", ZEN_CHANNEL_ID, 
			cokieValuesForLoggin, zenLogin_ip, zenUiPort, componentSources[i]);
			sourcesIds.put(Integer.parseInt(INID));
		}
		
		json.put("hitless_failover_source_ids", sourcesIds);
		json.put("latency", Integer.parseInt(latency));
		json.put("monitor_pids_change", Integer.parseInt(monitor_pids_change));
		json.put("password", password);
		json.put("encryption", encryption);
		json.put("encryption_key", encryption_key);
		json.put("allow_outputs", Boolean.parseBoolean(allow_outputs));
		json.put("outputs_password", outputs_password);
		json.put("monitor_only", Boolean.parseBoolean(monitor_only));

		return new DriverReslut(apiworker.zenPost( "https://" + zenLogin_ip + "/api/sources", zenUser, zenPass, zenUiPort, zenLogin_ip, 
				json.toString().getBytes(), ZEN_ADD_FEEDER_SOURCE, cokieValuesForLoggin));
	}
}
