package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.*;

import java.io.File;
import java.io.FileInputStream;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterSetSshParametersDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	
	FileInputStream fileInputStream=null;
	File sppk = null; 
	
	// Provide a SSH server host name. 
	public DriverReslut setHostandSshPortBroadcaster(String userName, String userPass, String login_ip, String uiport, String host, String ssh_port) throws Exception  
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		
		driverReslut =  new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/set_ssh.json?host=" + host + "&ssh_port=" +
		ssh_port +  "&connection_id=" + host, "", FEEDER_SSH_SERVER_STATUS, responseCookieContainer, login_ip, this, uiport));
		return driverReslut;
	}
	
	// Add client's private key - broadcaster server.
	public DriverReslut uploadSshKeyToBroadcaster(String userName, String userPass, String login_ip, String uiport, String keyLocation) throws Exception  
	{
		FeederPostKeyDriver feederPostKeyDriver = new FeederPostKeyDriver();
		return feederPostKeyDriver.uploadSshKeyToBroadcasterServer(userName, userPass, login_ip, uiport, keyLocation);
	}
	
	// Add client's private key - broadcaster server.
		public DriverReslut uploadSshKeyToBroadcasterZen(String userName, String userPass, String login_ip, String uiport) throws Exception  
		{
			FeederPostKeyDriver feederPostKeyDriver = new FeederPostKeyDriver();
			return feederPostKeyDriver.uploadPrivateKeyBroadcaster(userName, userPass, login_ip, uiport);
		}
	
	// Add SSH user name.
	public DriverReslut setSshUserNameBroadcaster(String userName, String userPass, String login_ip, String uiport, String sshUserName, String connectionId) throws Exception  
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		
		driverReslut =  new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/set_ssh.json?ssh_user=" + sshUserName +
		"&connection_id=" + connectionId, "", FEEDER_SSH_SERVER_STATUS, responseCookieContainer, login_ip, this, uiport));
		
		return driverReslut;
	}
	
	
	// Add SSH port.
	public DriverReslut setSshPortBroadcaster(String userName, String userPass, String login_ip, String uiport, String sshPort, String connectionId) throws Exception  
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		
		driverReslut =  new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/add_ssh_tunnel.json?on=1&target_port=0&connection_id=" +
		connectionId + "&target_ip=127.0.0.1&remote_port=" + sshPort, "", TUNNEL_ADDED_MODE, responseCookieContainer, login_ip, this, uiport));
		
		return driverReslut;
	}
}
