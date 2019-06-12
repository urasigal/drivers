package com.zixi.drivers.drivers;

import java.util.ArrayList;

import com.zixi.entities.TestParameters;
import com.zixi.tools.*;

import static com.zixi.tools.Macros.*;



public class BroadcasterInputStreamDriver extends BroadcasterLoggableApiWorker implements TestDriver
{
	public String[] retrieveAllInputStreamsFromBroadcaser(String login_ip, String userName, String userPassword, String uiport) throws Exception 
	{
		com.zixi.drivers.tools.ApiWorkir apiworker = new com.zixi.drivers.tools.ApiWorkir();
		
		testParameters = new TestParameters("login_ip:"+login_ip, "userName:"+userName , "userPassword:"+userPassword, "uiport:"+uiport);
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm", userName , userPassword, login_ip, uiport);
		
		String[] streamIds = apiworker.sendGet("http://" + login_ip + ":"+ uiport + "/zixi/streams.json", "", GET_ALL_STREAMS, responseCookieContainer, login_ip, this, uiport).split("#");
		
//		for(String str : streamIds)
//		{
//			System.out.println(str);
//		}
		return streamIds;
	}
	
	
	// Testing stuff spoof main method.
	
	public static class Tester
	{
		private BroadcasterInputStreamDriver broadcasterInputStreamDriver = new BroadcasterInputStreamDriver ();
		
		public void testOuterClass(String login_ip, String userName, String userPassword, String uiport) throws Exception
		{
			String[] test  = broadcasterInputStreamDriver.retrieveAllInputStreamsFromBroadcaser(login_ip, userName, userPassword, uiport);
			System.out.println(test);
		}
	}
}
