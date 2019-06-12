package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;
import org.json.JSONObject;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.ApiWorkir;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ZenDeleteClusterDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	 
	public DriverReslut deleteCluster(String zenUserName, String zenUserPass, String zenLogin_ip, String zenUiport, String clusterName) throws Exception
	{
		JSONObject json = new JSONObject();
		json.put("username", zenUserName).put("password", zenUserPass);
		
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login" , zenUserName , zenUserPass, zenUiport, zenLogin_ip, json.toString().getBytes());
		
		String broadcaster_cluster_id = new ApiWorkir().zenSendGet("https://" + zenLogin_ip + "/api/broadcaster_clusters", ZEN_CLUSTER_ID, 
		cokieValuesForLoggin, zenLogin_ip, zenUiport, clusterName);
		
		return new DriverReslut(apiworker.zenSendDelete("https://" + zenLogin_ip + "/api/broadcaster_clusters/" + broadcaster_cluster_id, ZEN_DELETE_CLUSTER, 
		cokieValuesForLoggin, zenLogin_ip, zenUiport, broadcaster_cluster_id));
	}
	
	public DriverReslut deleteUser(String zenUserName, String zenUserPass, String zenLogin_ip, String zenUiport, String userName) throws Exception
	{
		JSONObject json = new JSONObject();
		json.put("username", zenUserName).put("password", zenUserPass);
		
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login" , zenUserName , zenUserPass, zenUiport, zenLogin_ip, json.toString().getBytes());
		
		String user_id = new ApiWorkir().zenSendGet("https://" + zenLogin_ip + "/api/users", ZEN_CLUSTER_ID, 
		cokieValuesForLoggin, zenLogin_ip, zenUiport, userName);
		
		return new DriverReslut(apiworker.zenSendDelete("https://" + zenLogin_ip + "/api/users/" + user_id, ZEN_DELETE_CLUSTER, 
		cokieValuesForLoggin, zenLogin_ip, zenUiport, user_id));
	}	
	
	public DriverReslut deleteRole(String zenUserName, String zenUserPass, String zenLogin_ip, String zenUiport, String roleName) throws Exception
	{
		JSONObject json = new JSONObject();
		json.put("username", zenUserName).put("password", zenUserPass);
		
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + zenLogin_ip + "/login" , zenUserName , zenUserPass, zenUiport, zenLogin_ip, json.toString().getBytes());
		
		String role_id = new ApiWorkir().zenSendGet("https://" + zenLogin_ip + "/api/roles", ZEN_CLUSTER_ID, 
		cokieValuesForLoggin, zenLogin_ip, zenUiport, roleName);
		
		return new DriverReslut(apiworker.zenSendDelete("https://" + zenLogin_ip + "/api/roles/" + role_id, ZEN_DELETE_CLUSTER, 
		cokieValuesForLoggin, zenLogin_ip, zenUiport, role_id));
	}	
}
