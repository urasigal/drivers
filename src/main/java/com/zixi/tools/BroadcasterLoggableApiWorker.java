package com.zixi.tools;

import java.io.IOException;
import java.util.logging.Logger;

import com.jcraft.jsch.JSchException;
import com.zixi.drivers.drivers.TestDriver;
import com.zixi.entities.TestParameters;
import com.zixi.ssh.SshJcraftClient;
import com.zixi.tools.*;
import com.zixi.drivers.tools.*;

public class BroadcasterLoggableApiWorker implements TestDriver{
	protected 		ApiWorkir 					 	apiworker 						= 	new ApiWorkir();
	protected 		String 						 	responseCookieContainer[] 		= 	new String[2];
	protected 		TestParameters 				 	testParameters;
	protected 		BroadcasterInitialSecuredLogin 	broadcasterInitialSecuredLogin 	= 	new BroadcasterInitialSecuredLogin();
	private static 	SshJcraftClient 			 	sshJcraftClient;
	protected 		StringBuffer 					testFlowDescriptor;
	protected 		DriverReslut 					driverReslut 					= 	new DriverReslut();
	public static 	String 							zapiCycleID 					=   null;
	// Default constructor.
	public BroadcasterLoggableApiWorker() {}
	
	public BroadcasterLoggableApiWorker(StringBuffer testFlowDescriptor) {
		
		this.testFlowDescriptor = testFlowDescriptor;
	}

	public static String getPid(String sshUser, String sshPassword, String sshLoginIp, String sshPort, String command)
	{
		String SUT_PID = "";
		sshJcraftClient = new SshJcraftClient();
		try 
		{
			SUT_PID = sshJcraftClient.callCommand( sshUser,  sshPassword,  sshLoginIp,  sshPort,  command);
			Thread.sleep(2000);
			return SUT_PID; 
		} catch (Exception e) {
			return "";
		} 
	}
	
	public void setLogger(Logger logger)
	{
		//this.LOGGER = logger;
	}
}
