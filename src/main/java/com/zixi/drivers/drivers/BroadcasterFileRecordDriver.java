package com.zixi.drivers.drivers;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import com.zixi.tools.BroadcasterLoggableApiWorker;
import com.zixi.tools.FileManagerTools;

public class BroadcasterFileRecordDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	 
	public String inputRecord(String userName, String userPass, String login_ip, String uiport, String id, String on, String cpuFolder) 
	throws Exception 
	{
		BroadcasterSystemDriver broadcasterSystemDriver = new BroadcasterSystemDriver();
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + login_ip + ":" + uiport + "/login.htm",
				userName, userPass, login_ip, uiport);
		// Start record a stream.
		apiworker.sendGet("http://" + login_ip + ":" + uiport + "/set_live_recording.json?id=" + id + "&on=" + on, 
				"", 77, responseCookieContainer, login_ip, this, uiport);
		// Create a file to write a stats.
		File dirs = new File("src/main/resources/" + cpuFolder);
	    dirs.mkdirs();
		File file = FileManagerTools.createFile("src/main/resources/" + cpuFolder + "/" + id);
		for(int i = 0; i < 1200; i++)
		{
			String cpuLoad = null;
			try {
				cpuLoad = broadcasterSystemDriver.getCpuFromBroadcaster(userName, userPass, login_ip, uiport);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.out.println("CPU Load is " + cpuLoad); 
			
			if (file != null)
			{
				try(PrintWriter output = new PrintWriter(new FileWriter("src/main/resources/" + cpuFolder + "/" + id, true))) 
				{
				    output.println(cpuLoad);
				} 
				catch (Exception e) {}
			}
			Thread.sleep(1000);
		}
		//return ret;
		return "good";
	}
}
