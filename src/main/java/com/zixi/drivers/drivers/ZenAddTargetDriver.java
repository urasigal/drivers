package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;
import org.json.JSONArray;
import org.json.JSONObject;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.ApiWorkir;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ZenAddTargetDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	 
	public DriverReslut addPullBroadcasterTarget(String zenUser, String zenPass, String zenLogin_ip, 
	String zenUiPort, String targetName, String resource_tag_ids, String targetType, String channel_name,
	String password, String receiver_id_, String broadcaster_name_id, String output_id, String receiver_name, 
	String zixi_encryption_key, String output_name)throws Exception
	{   
		Thread.sleep(180_000);
		JSONObject json = new JSONObject();
		json.put("username", zenUser).put("password", zenPass);
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login",
		zenUser, zenPass, zenUiPort, zenLogin_ip, json.toString().getBytes());
		
		String accessTagId = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/resource_tags/resource", ZEN_GET_ACCESS_TAG, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, resource_tag_ids);
		
		String channelId = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/channels/delivery", ZEN_GET_ACCESS_TAG, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, channel_name);
		
		if(channelId == null)
		{
			channelId = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/channels/adaptive", ZEN_GET_ACCESS_TAG, 
			cokieValuesForLoggin, zenLogin_ip, zenUiPort, channel_name);
		}
		
		json = new JSONObject();
		json.put("resource_tag_ids", new JSONArray().put(Integer.parseInt(accessTagId))).
		put("adaptive_channel_id", Integer.parseInt(channelId)).
		put("delivery_channel_id", Integer.parseInt(channelId));
		
		// Case of receiver target.
		String rx_id = null;
		if (receiver_id_.equals(""))
		{
			json.put("receiver_id", JSONObject.NULL);
		}else { // If target destination is ZEN receiver.
			String res_id= new ApiWorkir().zenSendGet("https://" + zenLogin_ip + "/api/receivers", ZEN_GET_RX_ID,
			cokieValuesForLoggin, zenLogin_ip, zenUiPort, receiver_id_);
			json.put("receiver_id", new JSONArray().put(Integer.parseInt(res_id)));
		}
		
		if (output_id.equals(""))
		{
			json.put("output_id", JSONObject.NULL);
		}
		
		String rx_name = null;
		if (receiver_name.equals(""))
		{
			json.put("receiver_name", JSONObject.NULL);
		}
		
		json.put("zixi_encryption_key", zixi_encryption_key);
		
		// Case of broadcaster target
		String bx_id = null;
		if (broadcaster_name_id.equals(""))
		{
			json.put("broadcaster_id", JSONObject.NULL);
		}else
		{
			String[] clstr_bx = broadcaster_name_id.split("@");
			String clstr_name = clstr_bx[0];
			String bx_name = clstr_bx[1];
			
			if(bx_name.equals("aws"))
			{
				// AWS case - pick one from cluster.
				String clstr_id = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/broadcaster_clusters", ZEN_CLUSTER_ID, cokieValuesForLoggin, zenLogin_ip, zenUiPort, clstr_name);
				bx_id = new ApiWorkir().zenSendGet("https://" + zenLogin_ip + "/api/broadcaster_clusters/" + clstr_id + 
				"/broadcasters", ZEN_GET_BX_RUNDOM_ID, cokieValuesForLoggin, zenLogin_ip, zenUiPort, "");
				json.put("broadcaster_id", bx_id);
			}else
			{
				String clstr_id = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/broadcaster_clusters", ZEN_CLUSTER_ID, cokieValuesForLoggin, zenLogin_ip, zenUiPort, clstr_name);
				for(int i = 0; i < 4; i++)
				{
					Thread.sleep(60_000);
					bx_id = new ApiWorkir().zenSendGet("https://" + zenLogin_ip + "/api/broadcaster_clusters/" + clstr_id + 
					"/broadcasters", ZEN_GET_BX_ID, cokieValuesForLoggin, zenLogin_ip, zenUiPort, bx_name);
					if(! (bx_id.equals("broadcaster name not found")))
					{
						break;
					}
					if(i == 3)
						return new DriverReslut("broadcaster name not found");
				}
				json.put("broadcaster_id", bx_id);
			}
		}
		json.put("type", targetType).
		put("output_name", output_name).
		put("name", targetName);
		return new DriverReslut(apiworker.zenPost( "https://" + zenLogin_ip + "/api/targets/" + targetType, zenUser, zenPass, zenUiPort, zenLogin_ip, 
		json.toString().getBytes(), ZEN_ADD_FEEDER_SOURCE, cokieValuesForLoggin));
	}
	
	public DriverReslut addUdpRtpTarget(String zenUser, String zenPass, String zenLogin_ip, String zenUiPort, String targetName, String resource_tag_ids, String targetType, String channel_name,
	String host, String port, String bind_cidr, String rtp, String smpte_2022_fec, String smpte_2022_fec_cols, String smpte_2022_fec_rows, String smoothing, String remux_kbps)
	{
		JSONObject json = new JSONObject();
		json.put("username", zenUser).put("password", zenPass);
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login", zenUser, zenPass, zenUiPort, zenLogin_ip, json.toString().getBytes());
		
		String accessTagId = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/resource_tags/resource", ZEN_GET_ACCESS_TAG, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, resource_tag_ids);
		
		String channelId = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/channels/delivery", ZEN_GET_ACCESS_TAG, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, channel_name);
		
		if(channelId == null)
		{
			channelId = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/channels/adaptive", ZEN_GET_ACCESS_TAG, 
			cokieValuesForLoggin, zenLogin_ip, zenUiPort, channel_name);
		}
		
		json = new JSONObject();
		json.put("resource_tag_ids", new JSONArray().put(Integer.parseInt(accessTagId))).
		put("name", targetName).
		put("adaptive_channel_id", Integer.parseInt(channelId)).
		put("delivery_channel_id", Integer.parseInt(channelId)).put("type", targetType).
		put("host", host).put("port", port).put("bind_cidr", bind_cidr).put("rtp", Integer.parseInt(rtp)).
		put("smpte_2022_fec", smpte_2022_fec).put("smpte_2022_fec_cols", Integer.parseInt(smpte_2022_fec_cols)).
		put("smpte_2022_fec_rows", Integer.parseInt(smpte_2022_fec_rows)).put("smoothing", Integer.parseInt(smoothing));
		
		if(!remux_kbps.equals(""))
		{
			json.put("remux_kbps", Integer.parseInt(remux_kbps));
		}
		
		return new DriverReslut(apiworker.zenPost( "https://" + zenLogin_ip + "/api/targets/" + targetType, zenUser, zenPass, zenUiPort, zenLogin_ip, 
		json.toString().getBytes(), ZEN_ADD_FEEDER_SOURCE, cokieValuesForLoggin));
	}
	
	public DriverReslut addPushTarget(String zenUser, String zenPass, String zenLogin_ip, String zenUiPort, String name, String resource_tag_ids,
	String type, String channel_name, String is_bonding, String target, String alt_target, String stream_id,
	String password, String latency, String zixi_encryption_key)
	{
		JSONObject json = new JSONObject();
		json.put("username", zenUser).put("password", zenPass);
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login", zenUser, zenPass, zenUiPort, zenLogin_ip, json.toString().getBytes());
		
		String accessTagId = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/resource_tags/resource", ZEN_GET_ACCESS_TAG, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, resource_tag_ids);
		
		String channelId = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/channels/delivery", ZEN_GET_ACCESS_TAG, 
		cokieValuesForLoggin, zenLogin_ip, zenUiPort, channel_name);
		
		if(channelId == null)
		{
			channelId = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/channels/adaptive", ZEN_GET_ACCESS_TAG, 
			cokieValuesForLoggin, zenLogin_ip, zenUiPort, channel_name);
		}
		
		json = new JSONObject();
		json.put("resource_tag_ids", new JSONArray().put(Integer.parseInt(accessTagId))).
		put("name", name).
		put("type", type).
		put("adaptive_channel_id", Integer.parseInt(channelId)).
		put("delivery_channel_id", Integer.parseInt(channelId)).
		put("is_bonding", Boolean.parseBoolean(is_bonding)).
		put("target", target).
		put("alt_target", alt_target).
		put("stream_id", "stream_id").
		put("password", password).
		put("latency", Integer.parseInt(latency)).
		put("zixi_encryption_key", zixi_encryption_key);
		
		return new DriverReslut(apiworker.zenPost( "https://" + zenLogin_ip + "/api/targets/" + type, zenUser, zenPass, zenUiPort, zenLogin_ip, 
		json.toString().getBytes(), ZEN_ADD_FEEDER_SOURCE, cokieValuesForLoggin));
	}
}
