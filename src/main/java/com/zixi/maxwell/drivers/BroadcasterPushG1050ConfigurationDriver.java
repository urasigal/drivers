package com.zixi.maxwell.drivers;

import com.zixi.drivers.drivers.TestDriver;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterPushG1050ConfigurationDriver extends BroadcasterLoggableApiWorker implements TestDriver{
		
	public static void confG1050PluginOnMaxwell()
	{		 
		String maxwellProcessId = getPid("root", "maxwell", "10.7.0.70", "22", "pidof stdiserver");
		getPid("root", "maxwell",  "10.7.0.70",  "22",  "kill -9 " + maxwellProcessId);
		getPid("root", "maxwell",  "10.7.0.70",  "22",  "rm -f /home/maxwell/log");
		getPid("maxwell", "maxwell", "10.7.0.70", "22", "stdiserver -L /usr/local/lib/iwl/plugins/itu_t_g_1050_3.so -g /home/maxwell/log > /dev/null &");
		
		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void releaseG1050PluginOnMaxwell()
	{		 
		String maxwellProcessId = getPid("root", "maxwell", "10.7.0.70", "22", "pidof stdiserver");
		getPid("root", "maxwell", "10.7.0.70", "22", "kill -9 " + maxwellProcessId);
		getPid("maxwell", "maxwell", "10.7.0.70", "22", "stdiserver > /dev/null &");
		try {
			Thread.sleep(180_000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void transferLogFile(){		 
		getPid("maxwell", "maxwell", "10.7.0.70", "22", "sshpass -p  \"zixiroot1234\" scp /home/maxwell/log root@10.7.0.68:/root/log");
		try { Thread.sleep(180_000); } catch (InterruptedException e) { e.printStackTrace(); } }	
}
