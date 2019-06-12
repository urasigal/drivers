package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.PUSHINMODE;

import java.net.URLEncoder;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.ApiWorkir;
import com.zixi.tools.BroadcasterLoggableApiWorker;


public class FeederOutputDeletionDriver extends BroadcasterLoggableApiWorker implements TestDriver {

	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String id, String mip, String port, String ip,
	String prog, String chan, String type, String host, String push_port) throws Exception {
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
 
		return new DriverReslut( apiworker.sendGet("http://" + login_ip + ":" + uiport+ "/del_sink?" + "mip" + "=" + mip + "&port" + "=" + port + "&ip"
		+ "=" + ip + "&prog"  + "=" + prog + "&chan" + "=" + chan + "&type" + "=" + type + "&id" + "=" + "zixi%3A%2F%2F" + 
		host + "%3A" + push_port + "%2F" + id, id, PUSHINMODE, responseCookieContainer, login_ip, this, uiport));
	}
	
	public DriverReslut testIMPL(String userName, String userPass, String login_ip, String uiport, String id, String mip, 
		String port, String ip, String prog, String chan, String type, String nic1, String nic2, String dest_host1,
		String dest_host2) throws Exception {
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
 
		String request = "http://" + login_ip + ":" + uiport + "/del_sink?mip=" + mip + "&port=" + port + "&ip=" + ip + "&prog=" + prog + "&chan=" + chan + 
		"&type=" + type + "&id=zixi%3A%2F%2Fbonded(" + id + ")%23" + dest_host1 + "%3A2088%40" + nic1 + "%2C" + dest_host2 + "%3A2088%40" + nic2;
		
		driverReslut = new DriverReslut(apiworker.sendGet(request, id, PUSHINMODE, responseCookieContainer, login_ip, this, uiport));
		
		return driverReslut;
		}
	
	public DriverReslut testUdpIMPL(String userName, String userPass, String login_ip, String uiport, String id, String mip, String port, String ip,
	String prog, String chan, String type, String host, String op) throws Exception {

		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
		
		driverReslut =  new DriverReslut( apiworker.sendGet("http://" + login_ip + ":" + uiport + "/del_sink?" + "mip" + "=" + mip + "&port" + "=" + port + "&ip"
		+ "=" + ip + "&prog"  + "=" + prog + "&chan" + "=" + chan + "&type" + "=" + type + "&id" + "="
		+ "udp%3A%2F%2F" + host + "%3A" + op, id, PUSHINMODE, responseCookieContainer, login_ip, this, uiport));
		
		return driverReslut;
		// TODO Auto-generated method stub
	}
	
	// Delete bonded stream with three links.
	public String testIMPL(String userName, String userPass, String login_ip, String uiport, String id, String mip, 
		String port, String ip, String prog, String chan, String type, String nic1, String nic2, String nic3, String dest_host1,
		String dest_host2, String dest_host3) throws Exception {
		
		testParameters = new TestParameters("userName:"+userName, "userPass:" + userPass, "login_ip:" + login_ip, "uiport:" + uiport, "id:" + id, "mip:" + mip, 
		"port:" + port, "ip:" + ip, "prog:" + prog, "chan:" + chan, "type:" + type, "nic1:" + nic1, "nic2:" + nic2, "dest_host1:" + dest_host1, 
		"dest_host2:" + dest_host2);
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass,
		login_ip, uiport);
 
		String request = "http://" + login_ip + ":" + uiport + "/del_sink?mip=" + mip + "&port=" + port + "&ip=" + ip + "&prog=" + prog + "&chan=" + chan + 
		"&type=" + type + "&id=zixi%3A%2F%2Fbonded(" + id + ")%23" + dest_host1 + "%3A2088%40" + nic1 + "%2C" + dest_host2 + "%3A2088%40" + nic2 + "%2C" +
		dest_host3 + "%3A2088%40" + nic3;
		
		return apiworker.sendGet(request, id, PUSHINMODE,
		responseCookieContainer, login_ip, this, uiport);
		// TODO Auto-generated method stub
		}
	
	    // Delete bonded stream with three links multiple.
		public String testIMPL(String userName, String userPass, String login_ip, String uiport, String mip, String port, 
		String ip, String prog, String chan, String type, String id) throws Exception {
			
		testParameters = new TestParameters("userName:"+userName, "userPass:" + userPass, "login_ip:" + login_ip, "uiport:" + uiport, "id:" + id);
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);
 
		String request = "http://" + login_ip + ":" + uiport + "/del_sink?mip=" + mip + "&port=" + port + "&ip=" + ip + "&prog=" + prog + "&chan=" + chan + 
		"&type=" + type + "&id=" +  URLEncoder.encode(id, "UTF-8");
		
		return apiworker.sendGet(request, id, PUSHINMODE, responseCookieContainer, login_ip, this, uiport);
	}
}
