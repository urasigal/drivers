package com.zixi.drivers.drivers;

import java.io.IOException;
import com.jcraft.jsch.JSchException;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.ssh.SshJcraftClient;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class FeederReverseTunnelDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	
	private SshJcraftClient sshJcraftClient;
	 
	public DriverReslut testIMPL(String userName, String sshPass, String login_ip, String ssh_port, String ssh_user, String reverse_port) throws IOException, InterruptedException, JSchException  
	{
		sshJcraftClient = new SshJcraftClient();
		Thread.sleep(10000);
		String test = sshJcraftClient.callCommand(userName, sshPass, login_ip, ssh_port, "lsof -nPi | grep " +  reverse_port);
		
		driverReslut =  new DriverReslut(test.split(" ")[0]);	
		return driverReslut;
	}

}
