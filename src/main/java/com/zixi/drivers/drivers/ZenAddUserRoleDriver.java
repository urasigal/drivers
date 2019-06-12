package com.zixi.drivers.drivers;
import static com.zixi.globals.Macros.*;
import org.json.JSONObject;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ZenAddUserRoleDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	 
	public DriverReslut addZenUserRole(String zen_userName, String zen_userPass, String login_ip, String uiport,
	String name, String clusters_edit, String clusters_view, String clusters_notify, String source_edit,
	String source_view, String source_notify, String adaptive_edit, String adaptive_view,
	String adaptive_notify, String delivery_edit, String delivery_view, String delivery_notify,
	String resource_tag_name)throws Exception
	{    
		JSONObject json = new JSONObject();
		json.put("username", zen_userName).put("password", zen_userPass);
		String[] cokieValuesForLoggin = apiworker.zenLogginPost("https://" + login_ip + "/login",
		zen_userName, zen_userPass, uiport, login_ip, json.toString().getBytes());
		
		json = new JSONObject();
		json.putOpt("name", name);
		json.putOpt("clusters_edit", Integer.parseInt( clusters_edit));
		json.putOpt("clusters_view", Integer.parseInt( clusters_view));
		json.putOpt("clusters_notify", Integer.parseInt( clusters_notify));
		json.putOpt("source_edit", Integer.parseInt( source_edit));
		json.putOpt("source_view", Integer.parseInt( source_view));
		json.putOpt("source_notify", Integer.parseInt( source_notify));
		json.putOpt("adaptive_edit", Integer.parseInt( adaptive_edit));
		json.putOpt("adaptive_view", Integer.parseInt( adaptive_view));
		json.putOpt("adaptive_notify", Integer.parseInt( adaptive_notify));
		json.putOpt("delivery_edit", Integer.parseInt( delivery_edit));
		json.putOpt("delivery_view", Integer.parseInt( delivery_view));
		json.putOpt("delivery_notify", Integer.parseInt( delivery_notify));
		json.putOpt("resource_tag_name", resource_tag_name);
		
		
		return new DriverReslut(apiworker.zenPost( "https://" + login_ip + "/api/roles", zen_userName, zen_userPass, uiport, login_ip, 
		json.toString().getBytes(), ZEN_ADD_USER, cokieValuesForLoggin));
	}	
}
