package com.zixi.drivers.drivers;
import static com.zixi.globals.Macros.*;
import org.json.JSONObject;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ZenAssignUserToRoleDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	 
	public DriverReslut assignUserToRole(String zen_userName, String zen_userPass, String login_ip, String uiport, String role_name, String user_name)throws Exception
	{    
		JSONObject json = new JSONObject();
		json.put("username", zen_userName).put("password", zen_userPass);
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + login_ip + "/login",
		zen_userName, zen_userPass, uiport, login_ip, json.toString().getBytes());
		
		String roleId = apiworker.zenSendGet("https://" + login_ip + "/api/roles", ZEN_GET_ACCESS_TAG, cokieValuesForLoggin, login_ip, uiport, role_name);

		String userId = apiworker.zenSendGet("https://" + login_ip + "/api/users", ZEN_GET_ACCESS_TAG, cokieValuesForLoggin, login_ip, uiport, user_name);
		json = new JSONObject();
		
		return new DriverReslut(apiworker.zenPost( "https://" + login_ip + "/api/roles/" + roleId + "/users/" + userId, zen_userName, zen_userPass, uiport, login_ip, 
		json.toString().getBytes(), ZEN_ADD_USER, cokieValuesForLoggin));
	}	
}
