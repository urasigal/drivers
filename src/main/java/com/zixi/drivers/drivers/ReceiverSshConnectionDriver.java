package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.FEEDER_SSH_SERVER_STATUS;
import static com.zixi.globals.Macros.TUNNEL_ADDED_MODE;

import java.io.File;
import java.io.FileInputStream;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class ReceiverSshConnectionDriver extends BroadcasterLoggableApiWorker implements TestDriver{

	FileInputStream fileInputStream=null;
	File sppk = null; 
	
	// Provide a SSH server host name. 
	public DriverReslut setSshUserAndSshHostReceiver(String userName, String userPass, String login_ip, String uiport, String host, String ssh_port, String ssh_user) throws Exception  
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass, login_ip, uiport);
		
		driverReslut =  new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/set_ssh.json?host=" + host + "&ssh_port=" + ssh_port+ 
		"&ssh_user=" +  ssh_user, "", FEEDER_SSH_SERVER_STATUS, responseCookieContainer, login_ip, this, uiport));
		return driverReslut;
	}
	
	// Add client's private key - broadcaster server.
	public DriverReslut uploadSshKeyToReceiver(String userName, String userPass, String login_ip, String uiport, String keyLocation) throws Exception  
	{
		FeederPostKeyDriver feederPostKeyDriver = new FeederPostKeyDriver();
		return new DriverReslut(feederPostKeyDriver.uploadHttpsCertificate(userName, userPass, login_ip, uiport, keyLocation, "WebKitFormBoundary4uIxlrzNgZt3P0q9"));
	}
	
	
	public DriverReslut uploadSshKeyToReceiverZen(String userName, String userPass, String login_ip, String uiport) throws Exception  
	{
		FeederPostKeyDriver feederPostKeyDriver = new FeederPostKeyDriver();
		return new DriverReslut(feederPostKeyDriver.uploadPrivateKeyFeeder(userName, userPass, login_ip, uiport)); 
	}
	
	// Add SSH port.
	public DriverReslut setSshPortReceiver(String userName, String userPass, String login_ip, String uiport, String sshPort) throws Exception  
	{
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPass,
		login_ip, uiport);
		driverReslut =  new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/add_ssh_tunnel.json?on=1&target_port=0&target_ip=127.0.0.1&remote_port=" + sshPort, "",
		TUNNEL_ADDED_MODE, responseCookieContainer, login_ip, this, uiport));
		return driverReslut;
	}
}
