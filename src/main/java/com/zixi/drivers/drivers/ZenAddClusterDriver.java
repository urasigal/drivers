package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;
import java.io.File;
import java.io.FileInputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ZenAddClusterDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	 
	public DriverReslut addCluster(String zenUserName, String zenUserPass, String zenLogin_ip, String zenUiport, String clusterName,
	String resource_tag_name, String can_input, String can_output, String can_process, String http_streaming_port, String auth_mode, 
	String allow_unmanaged_inputs, String allow_unmanaged_outputs, String whitelist, String dtls, String allow_ignore_dtls_cert,
	String load_balance, String bx_input_bw_limit, String bx_output_bw_limit, String dns_prefix, String is_auto_scaling, String activation_key,
	String bx_version, String aws_account_name, String root_device_size, String region, String key_pair, String security_group, String eips, String min_size, 
	String max_size, String api_user, String api_password, String vpc_name, String vpc_subnet_name) throws Exception
	{
		JSONObject json = new JSONObject();
		json.put("username", zenUserName).put("password", zenUserPass);
		// Logging to ZEN.  
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login" , zenUserName , zenUserPass, zenUiport, zenLogin_ip, json.toString().getBytes());
		String accessTagId = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/resource_tags/resource", ZEN_GET_ACCESS_TAG, cokieValuesForLoggin, zenLogin_ip, zenUiport, resource_tag_name);
		String bx_version_id = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/versions", ZEN_GET_BX_VERSION_ID, cokieValuesForLoggin, zenLogin_ip, zenUiport, bx_version);
		String aws_account_id = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/aws_accounts", AWS_ACCOUNT_ID, cokieValuesForLoggin, zenLogin_ip, zenUiport, aws_account_name); 
		
		json = new JSONObject();
		json.put("name", clusterName).
		put("resource_tag_ids", new JSONArray().put(Integer.parseInt(accessTagId))).
		put("can_input", Integer.parseInt(can_input)).
		put("can_output", Integer.parseInt(can_output)).
		put("can_process",Integer.parseInt(can_process)).
		put("http_streaming_port", http_streaming_port).
		put("auth_mode", auth_mode).
		put("allow_unmanaged_inputs", Integer.parseInt(allow_unmanaged_inputs)).
		put("allow_unmanaged_outputs", Integer.parseInt(allow_unmanaged_outputs)).
		put("whitelist", Integer.parseInt(whitelist)).
		put("dtls", Integer.parseInt(dtls)).
		put("allow_ignore_dtls_cert", Integer.parseInt(allow_ignore_dtls_cert)). 
		put("load_balance", Integer.parseInt(load_balance)). 
		put("bx_input_bw_limit", Integer.parseInt(bx_input_bw_limit)).
		put("bx_output_bw_limit", Integer.parseInt(bx_output_bw_limit)).
		put("root_device_size", Integer.parseInt(root_device_size)).
		put("manage_aws_sgs",  Boolean.parseBoolean("false")).
		put("dns_prefix", dns_prefix);
		
		if(api_user !=null && (! api_user.equals("")))
			json.put("api_user", api_user);
		if(api_password !=null && (! api_password.equals("")))
			json.put("api_password", api_password);
		if(is_auto_scaling !=null && (! is_auto_scaling.equals("")))
			json.put("is_auto_scaling", Boolean.parseBoolean(is_auto_scaling));
		if(activation_key != null && (! activation_key.equals("")))
			json.put("activation_key", activation_key);
		if(bx_version != null && (! bx_version.equals("")))
			json.put("version_id", bx_version_id);
		if(aws_account_name != null && (! aws_account_name.equals("")))
			json.put("aws_account_id", aws_account_id);
		if(region != null && (! region.equals("")))
		{
			// vpcData = { "keys": ["x", "y", ...],
			// 			   "vpcs": [{m}, {n}, ...]
			//  		 }
			JSONObject vpcData = new JSONObject( apiworker.zenSendGet("https://" + zenLogin_ip + "/api/aws/" + aws_account_id + "/regions/" + region + "/options",
					  			 AWS_VPC, cokieValuesForLoggin, zenLogin_ip, zenUiport, null ));
			JSONArray keysPairNamesArray = vpcData.getJSONArray("keys"); // Getting key pairs names as Json array.
			JSONObject vpcs = vpcData.getJSONObject("vpcs"); // Getting json array of VPCs objects.
			/* 
			  Just for test purposes the first key pair will be taken as well as the first VPC.
			 */
			
			String firstkeyPair 	= keysPairNamesArray.getString(0);
            String [] vpcsNames     = JSONObject.getNames(vpcs);
			JSONObject firstVPC 	= vpcs.getJSONObject(vpcsNames[0]);
			JSONObject allSubnets  	= firstVPC.getJSONObject("subnets");
			String firstSubnetName  = JSONObject.getNames(allSubnets)[0];
			String firstSgId		= firstVPC.getJSONArray("security_groups").getJSONObject(0).getString("id");
			
			json.put("region", region).
			put("key_pair", firstkeyPair).
			put("vpc", vpcsNames[0]).
			put("subnet", firstSubnetName).
			put("security_group", firstSgId).
			put("eips", eips);
			json.put("instance_type", "t2.medium");
		}
		if(region != null && (! region.equals("")))
			json.put("region", region);
		if(min_size != null && (! min_size.equals("")))
			json.put("min_size", min_size);
		if(max_size != null && (! max_size.equals("")))
			json.put("max_size", max_size);
		
		// Add cluster.
		DriverReslut addingResults = new DriverReslut(apiworker.zenPost( "https://" + zenLogin_ip + "/api/broadcaster_clusters", 
		zenUserName, zenUserPass, zenUiport, zenLogin_ip, json.toString().getBytes(), ZEN_ADD_CLUSTER, cokieValuesForLoggin));
		
		String result = addingResults.getResult();
		if(result.equals("true"))
		{
			// Get added cluster's ID.
			String broadcaster_cluster_id = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/broadcaster_clusters", ZEN_CLUSTER_ID,
			cokieValuesForLoggin, zenLogin_ip, zenUiport, clusterName);
	
			for(int i = 0; i < 180_000; i += 10)
			{
				Thread.sleep(10000);
				result = apiworker.zenSendGet("https://" + zenLogin_ip + "/api/broadcaster_clusters/" + broadcaster_cluster_id + "/broadcasters", 
				ZEN_CLUSTER_STATUS, cokieValuesForLoggin, zenLogin_ip, zenUiport, "");
				if(result.equals("good"))
					return new DriverReslut("true");
			}
		}
		return new DriverReslut("false");
	}	
}
