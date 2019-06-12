package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.StreamEntity;
import com.zixi.entities.TestParameters;
import com.zixi.tools.ApiWorkir;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterPushInStreamCreationDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	
	
	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String latency, String time_shift, String force_p2p ,String mcast_ip, String mcast_force, String
	mcast_port, String type, String uiport, String analyze, String mcast_ttl, String id, String mcast_out, String complete, String max_outputs, String on, String password) throws Exception 
	{
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		
		return new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/add_stream.json?" + "type" + "=" + type + "&" + "password" + "=" + password
		+ "&" + "latency" + "=" + latency + "&" + "force_p2p" + "=" + force_p2p + "&" + "id" + "=" + id 
		+ "&" + "max_outputs" + "=" + max_outputs + "&" + "mcast_out" + "=" + mcast_out + "&" + "on" + "="
		+ on + "&" + "analyze" + "=" + analyze + "&" + "complete" + "=" + complete + "&" + "mcast_force"
		+ "=" + mcast_force + "&" + "mcast_ip" + "=" + mcast_ip + "&" + "mcast_port" + "=" + mcast_port
		+ "&" + "mcast_ttl" + "=" + mcast_port + "&" + "time_shift" + "=" + time_shift, id, PUSHINMODE, responseCookieContainer, login_ip, this, uiport)); 
	}
	
	public DriverReslut testInIMPLManual(String userName, String userPass, String login_ip, String latency, String time_shift, String force_p2p ,String mcast_ip, String mcast_force, String
			mcast_port, String type, String uiport, String analyze, String mcast_ttl, String id, String mcast_out, String complete, String max_outputs, String on, String password, String dec_type, String dec_key) throws Exception 
			{
				responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
				
				return new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/add_stream.json?" + "type" + "=" + type + "&" + "password" + "=" + password
				+ "&" + "latency" + "=" + latency + "&" + "force_p2p" + "=" + force_p2p + "&" + "id" + "=" + id 
				+ "&" + "max_outputs" + "=" + max_outputs + "&" + "mcast_out" + "=" + mcast_out + "&" + "on" + "="
				+ on + "&" + "analyze" + "=" + analyze + "&" + "complete" + "=" + complete + "&" + "mcast_force"
				+ "=" + mcast_force + "&" + "mcast_ip" + "=" + mcast_ip + "&" + "mcast_port" + "=" + mcast_port
				+ "&" + "mcast_ttl" + "=" + mcast_port + "&" + "time_shift" + "=" + time_shift + "&dec_type=" + dec_type + "&dec_key=" + dec_key  + "&" + params5, id, PUSHINMODE, responseCookieContainer, login_ip, this, uiport)); 
			}
	
	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String latency, String time_shift, String force_p2p, String mcast_ip,
			String mcast_force, String mcast_port, String type, String uiport, String analyze, String mcast_ttl, String id, String mcast_out,
			String complete, String max_outputs, String on, String password, String dec_type, String dec_key) throws Exception 
	{
		testParameters = new TestParameters(userName,userPass,login_ip,latency,
		time_shift,force_p2p,mcast_ip,mcast_force,mcast_port,type,uiport,analyze,mcast_ttl,id,mcast_out,complete,
		max_outputs,on, password, dec_type, dec_key);
		
		// Print out to the HTML test report the test's parameters
		testParameters.printParametersToHTML();
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		
		return new DriverReslut( apiworker.sendGet("HTTP://" + login_ip + ":" + uiport + "/zixi/add_stream.json?" + "type" + "=" + type + "&" + "password" + "=" + password
		+ "&" + "latency" + "=" + latency + "&" + "force_p2p" + "=" + force_p2p + "&" + "id" + "=" + id  + "&" + "max_outputs" + "=" + max_outputs + "&"
		+ "mcast_out" + "=" + mcast_out + "&" + "on" + "=" + on + "&" + "analyze" + "=" + analyze + "&" + "complete" + "=" + complete + "&" + "mcast_force" + "=" + mcast_force + "&" + "mcast_ip" + "="
		+ mcast_ip + "&" + "mcast_port" + "=" + mcast_port + "&" + "mcast_ttl" + "=" + mcast_port + "&" + "time_shift" + "=" + time_shift + "&" + "dec_type=" + dec_type + 
		"&dec_key=" + dec_key  + "&ie_fooler=0.45086039789021015", id, PUSHINMODE, responseCookieContainer, login_ip, this, uiport) ); 
	}
	
	public DriverReslut testIMPLManual(String userName, String userPass, String login_ip, String latency, String time_shift, String force_p2p, String mcast_ip,
			String mcast_force, String mcast_port, String type, String uiport, String analyze, String mcast_ttl, String id, String mcast_out,
			String complete, String max_outputs, String on, String password, String dec_type, String dec_key) throws Exception 
	{
		testParameters = new TestParameters(userName,userPass,login_ip,latency,
		time_shift,force_p2p,mcast_ip,mcast_force,mcast_port,type,uiport,analyze,mcast_ttl,id,mcast_out,complete,
		max_outputs,on, password, dec_type, dec_key);
		
		// Print out to the HTML test report the test's parameters
		testParameters.printParametersToHTML();
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		
		return new DriverReslut( apiworker.sendGet("HTTP://" + login_ip + ":" + uiport + "/zixi/add_stream.json?" + "type" + "=" + type + "&" + "password" + "=" + password
		+ "&" + "latency" + "=" + latency + "&" + "force_p2p" + "=" + force_p2p + "&" + "id" + "=" + id  + "&" + "max_outputs" + "=" + max_outputs + "&"
		+ "mcast_out" + "=" + mcast_out + "&" + "on" + "=" + on + "&" + "analyze" + "=" + analyze + "&" + "complete" + "=" + complete + "&" + "mcast_force" + "=" + mcast_force + "&" + "mcast_ip" + "="
		+ mcast_ip + "&" + "mcast_port" + "=" + mcast_port + "&" + "mcast_ttl" + "=" + mcast_port + "&" + "time_shift" + "=" + time_shift + "&" + "dec-type=" + dec_type + 
		"&dec-key=" + dec_key  + "&ie_fooler=0.45086039789021015", id, PUSHINMODE, responseCookieContainer, login_ip, this, uiport) ); 
	}
	
	public DriverReslut testIMPL(String userName,String userPass,String login_ip,String latency,String time_shift,String force_p2p,String mcast_ip,String mcast_force,String mcast_port,String type,
			String uiport,String analyze,String mcast_ttl,String id,String mcast_out,String complete,String max_outputs,String on, String password, String priority_ids) throws Exception
	{
//		testParameters = new TestParameters(userName,userPass,login_ip,latency,
//		time_shift,force_p2p,mcast_ip,mcast_force,mcast_port,type,uiport,analyze,mcast_ttl,id,mcast_out,complete,
//		max_outputs,on, password, priority_ids);
		
		// Print out to the HTML test report the test's parameters
		//testParameters.printParametersToHTML();
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		
		return new DriverReslut( apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/add_stream.json?" + "type" + "=" + type + "&" + "password" + "=" + password
		+ "&" + "latency" + "=" + latency + "&" + "force_p2p" + "=" + force_p2p + "&" + "id" + "=" + id  + "&" + "max_outputs" + "=" + max_outputs + "&" + "mcast_out" + 
		"=" + mcast_out + "&" + "on" + "=" + on + "&" + "analyze" + "=" + analyze + "&" + "complete" + "=" + complete + "&" + "mcast_force"
		+ "=" + mcast_force + "&" + "mcast_ip" + "=" + mcast_ip + "&" + "mcast_port" + "=" + mcast_port + "&" + "mcast_ttl" + "=" + mcast_port + "&" + "time_shift" + "=" 
		+ time_shift + "&" + params5 + "&priority_ids=" + priority_ids, id, PUSHINMODE, responseCookieContainer, login_ip, this, uiport)); 
	}
	
	final protected static String params5 = "ie_fooler=0.45086039789021015";
}
