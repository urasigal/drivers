package com.zixi.drivers.drivers;
import static com.zixi.globals.Macros.*;
import org.json.JSONObject;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ZenAddUserGroupDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	 
	public DriverReslut addZenUserGroup(String zen_userName, String zen_userPass, String login_ip, String uiport, String group_name)throws Exception
	{   
		JSONObject json = new JSONObject();
		json.put("username", zen_userName).put("password", zen_userPass);
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + login_ip + "/login",
		zen_userName, zen_userPass, uiport, login_ip, json.toString().getBytes());
		
		json = new JSONObject();
		json.putOpt("name", group_name);
		
		return new DriverReslut(apiworker.zenPost( "https://" + login_ip + "/api/user_groups", zen_userName, zen_userPass, uiport, login_ip, 
		json.toString().getBytes(), ZEN_ADD_USER, cokieValuesForLoggin));
	}	
}
