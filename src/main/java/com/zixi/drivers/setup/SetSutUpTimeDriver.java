package com.zixi.drivers.setup;

import static com.zixi.globals.Macros.UPTIME;
import static com.zixi.globals.Macros.PUSHOUTMODE;
import static com.zixi.tools.Macros.GET_ALL_STREAMS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.zixi.drivers.drivers.BroadcasterInputStreamDriver;
import com.zixi.drivers.drivers.TestDriver;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class SetSutUpTimeDriver  extends BroadcasterLoggableApiWorker implements TestDriver
{
	public DriverReslut fillSetUpFileWithSutIps(String bx_1, String bx_2, String fx_3, String rx_4, String setUpFileLocation)  throws Exception 
	{	
		//Reader reader = new FileReader(setUpFileLocation);
		
		try(PrintWriter printWriter = new PrintWriter(setUpFileLocation)){
			printWriter.println(bx_1);
			printWriter.println(bx_2);
			printWriter.println(fx_3);
			printWriter.println(rx_4);
			printWriter.flush();
			
			// First waiting.
			Thread.sleep(1000); } catch(Exception e) { System.out.println(e.getMessage());} 
		
		driverReslut = new DriverReslut();
		driverReslut.setResult("pass");
		return driverReslut;
	}

	
	public DriverReslut checkUptime(String setUpFileLocation) { 
		//String responseCookieContainerBX1[] = new String[2];
		//String responseCookieContainerBX2[] = new String[2];
		String responseCookieContainerFX3[] = new String[2];
		String responseCookieContainerRX4[] = new String[2];
		
		String SUT1[];
		String SUT2[];
		String SUT3[];
		String SUT4[];
		
		boolean bx1Flag = true;
		boolean bx2Flag = true;
		
		try(BufferedReader in = new BufferedReader(new FileReader(setUpFileLocation)))
		{
			SUT1 = in.readLine().split(" ");
			SUT2 = in.readLine().split(" ");
			SUT3 = in.readLine().split(" ");
			SUT4 = in.readLine().split(" ");
			
			//responseCookieContainerBX1 = broadcasterInitialSecuredLogin.sendGet("http://" + SUT1[0] + ":" + SUT1[3] + "/login.htm", SUT1[1] , SUT1[2], SUT1[0], SUT1[3]);
			
			//responseCookieContainerBX2 = broadcasterInitialSecuredLogin.sendGet("http://" + SUT2[0] + ":" + SUT2[3] + "/login.htm", SUT2[1] , SUT2[2], SUT2[0], SUT2[3]);
			
			responseCookieContainerFX3 = broadcasterInitialSecuredLogin.sendGet("http://" + SUT3[0] + ":" + SUT3[3] + "/login.htm", SUT3[1] , SUT3[2], SUT3[0], SUT3[3]);
			
			responseCookieContainerRX4 = broadcasterInitialSecuredLogin.sendGet("http://" + SUT4[0] + ":" + SUT4[3] + "/login.htm", SUT4[1] , SUT4[2], SUT4[0], SUT4[3]);
			
			String upTimeBX1 = getUptimeFromBxSuts(SUT1[0], SUT1[3], SUT1[1], SUT1[2]);
			
			String upTimeBX2 = getUptimeFromBxSuts(SUT2[0], SUT2[3], SUT2[1], SUT2[2]);
			
			//String upTimeBX1 = apiworker.sendGet("http://" + SUT1[0] + ":" + SUT1[3] + "/status.json?full=1", "", UPTIME, responseCookieContainerBX1, SUT1[0], this, SUT1[3]);
			
			//String upTimeBX2 = apiworker.sendGet("http://" + SUT2[0] + ":" + SUT2[3] + "/status.json?full=1", "", UPTIME, responseCookieContainerBX2, SUT2[0], this, SUT2[3]);
			
			String hh, h, min;
			
			hh 	= Character.toString(upTimeBX1.charAt(0));
			h  	= Character.toString(upTimeBX1.charAt(1));
			min = Character.toString(upTimeBX1.charAt(3));
			
			if (Integer.parseInt(hh) == 0)
			{
				if (Integer.parseInt(h) == 0)
				{
					if (Integer.parseInt(min) == 0)
					{
						bx1Flag = false;
					}
				}
			}
			
			String resultBx1;
			String resultBx2;
			
			if(bx1Flag)
			{
				resultBx1 = "pass";
			}
			else
			{
				resultBx1 = "faild";
			}
		
			hh 	= Character.toString(upTimeBX2.charAt(0));
			h  	= Character.toString(upTimeBX2.charAt(1));
			min = Character.toString(upTimeBX2.charAt(3));
			
			if (Integer.parseInt(hh) == 0)
			{
				if (Integer.parseInt(h) == 0)
				{
					if (Integer.parseInt(min) == 0)
					{
						bx2Flag = false;
					}
				}
			}
			
			if(bx2Flag)
			{
				resultBx2 = "pass";
			}
			else
			{
				resultBx2 = "faild";
			}
			
			String finalResult = "";
			
			if(resultBx1.equals("pass"))
			{
				finalResult  =  finalResult + "first bx good";
			}
			else{
				finalResult  =  finalResult + "first bx was crash";
			}
			
			if(resultBx2.equals("pass"))
			{
				finalResult  =  finalResult + " secod bx good";
			}
			else{
				finalResult  =  finalResult + " secod bx was crash";
			}
			
			return new DriverReslut(finalResult);
			
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	// Get uptime from broadcaster.
	private String getUptimeFromBxSuts(String ipAddress, String port, String userName, String password) { 
		String upTime 				     = null;
		String responseCookieContainer[] = null;
		try{
			// Logging 
			responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + ipAddress + ":" + port + "/login.htm", userName , password, ipAddress, port);
			upTime = apiworker.sendGet("http://" + ipAddress + ":" + port + "/status.json?full=1", "", UPTIME, responseCookieContainer, userName, this, port);
	    }catch(Exception e){ System.out.println(e.getMessage());}
		return upTime;
	}
	
	public String uptimeSet(String setUpFileLocation, String upTimeFileLocation)
	{
		String SUT1[];
		String SUT2[];
		String SUT3[];
		String SUT4[];
		
		try(BufferedReader in = new BufferedReader(new FileReader(setUpFileLocation)))
		{
			SUT1 = in.readLine().split(" ");
			SUT2 = in.readLine().split(" ");
			SUT3 = in.readLine().split(" ");
			SUT4 = in.readLine().split(" ");
			
			String upTimeBX1 = getUptimeFromBxSuts(SUT1[0], SUT1[3], SUT1[1], SUT1[2]);
			String upTimeBX2 = getUptimeFromBxSuts(SUT2[0], SUT2[3], SUT2[1], SUT2[2]);
			
			try(PrintWriter printWriter = new PrintWriter(upTimeFileLocation)){
				printWriter.println(upTimeBX1);
				printWriter.println(upTimeBX2);
				printWriter.flush();
			}catch(Exception e) { System.out.println(e.getMessage()); }
		}catch(Exception e) { System.out.println(e.getMessage()); }
		return "pass";
	}
	
	public String continuousUpTimeCheck()
	{
		String SUT1[];
		String SUT2[];
		
		String crashResults = "pass";
		boolean crashFlag = false;
		// Previous uptime. looks like {BX1, BX2} .
		
		String upTime[] = getUpTimeFromSystemFile();
		if (upTime != null)
		{
			try(BufferedReader in = new BufferedReader(new FileReader("src/main/resources/uptime")))
			{
				// Get previous uptime.
				SUT1 = in.readLine().split(" ");
				SUT2 = in.readLine().split(" ");
				
				// Get current uptime.
				String upTimeBX1 = getUptimeFromBxSuts(SUT1[0], SUT1[3], SUT1[1], SUT1[2]);
				String upTimeBX2 = getUptimeFromBxSuts(SUT2[0], SUT2[3], SUT2[1], SUT2[2]);
				
				String curr_hh, curr_h, curr_mm, curr_m, curr_ss, curr_s, prev_hh, prev_h, prev_mm, prev_m, prev_ss, prev_s;
				
				curr_hh 	= 	Character.toString(upTimeBX1.charAt(0));
				curr_h  	= 	Character.toString(upTimeBX1.charAt(1));
				curr_mm 	= 	Character.toString(upTimeBX1.charAt(3));
				curr_m      =   Character.toString(upTimeBX1.charAt(4));
				curr_ss     =   Character.toString(upTimeBX1.charAt(6));
				curr_s      =   Character.toString(upTimeBX1.charAt(7));
				
				prev_hh  	= 	Character.toString(upTime[0].charAt(0));
				prev_h   	= 	Character.toString(upTime[0].charAt(1));
				prev_mm 	= 	Character.toString(upTime[0].charAt(3));
				prev_m		=   Character.toString(upTime[0].charAt(4));
				prev_ss     =   Character.toString(upTime[0].charAt(6));
				prev_s      =   Character.toString(upTime[0].charAt(7));
				
				
				if(Integer.parseInt(prev_s) > Integer.parseInt(curr_s))
				{
					crashFlag = true;
				}else if(Integer.parseInt(prev_s) < Integer.parseInt(curr_s))
				{
					crashFlag = false;
				}
				
				if(Integer.parseInt(prev_ss) > Integer.parseInt(curr_ss))
				{
					crashFlag = true;
				}else if(Integer.parseInt(prev_ss) < Integer.parseInt(curr_ss))
				{
					crashFlag = false;
				}
				
				if(Integer.parseInt(prev_m) > Integer.parseInt(curr_m))
				{
					crashFlag = true;
				}else if(Integer.parseInt(prev_m) < Integer.parseInt(curr_m))
				{
					crashFlag = false;
				}
				
				if(Integer.parseInt(prev_mm) > Integer.parseInt(curr_mm))
				{
					crashFlag = true;
				}else if(Integer.parseInt(prev_mm) < Integer.parseInt(curr_mm))
				{
					crashFlag = false;
				}
				
				if(Integer.parseInt(prev_h) > Integer.parseInt(curr_h))
				{
					crashFlag = true;
				}else if(Integer.parseInt(prev_h) < Integer.parseInt(curr_h))
				{
					crashFlag = false;
				}
				
				if(Integer.parseInt(prev_hh) > Integer.parseInt(curr_hh))
				{
					crashFlag = true;
				}else if(Integer.parseInt(prev_hh) < Integer.parseInt(curr_hh))
				{
					crashFlag = false;
				}
				
				if(crashFlag) {crashResults = "first crashes";}
			
				curr_hh 	= 	Character.toString(upTimeBX2.charAt(0));
				curr_h  	= 	Character.toString(upTimeBX2.charAt(1));
				curr_mm 	= 	Character.toString(upTimeBX2.charAt(3));
				curr_m      =   Character.toString(upTimeBX2.charAt(4));
				curr_ss     =   Character.toString(upTimeBX2.charAt(6));
				curr_s      =   Character.toString(upTimeBX2.charAt(7));
				
				prev_hh  	= 	Character.toString(upTime[1].charAt(0));
				prev_h   	= 	Character.toString(upTime[1].charAt(1));
				prev_mm 	= 	Character.toString(upTime[1].charAt(3));
				prev_m		=   Character.toString(upTime[1].charAt(4));
				prev_ss     =   Character.toString(upTime[1].charAt(6));
				prev_s      =   Character.toString(upTime[1].charAt(7));
				
				crashFlag = false;

				if(Integer.parseInt(prev_s) > Integer.parseInt(curr_s))
				{
					crashFlag = true;
				}else if(Integer.parseInt(prev_s) < Integer.parseInt(curr_s))
				{
					crashFlag = false;
				}
				
				if(Integer.parseInt(prev_ss) > Integer.parseInt(curr_ss))
				{
					crashFlag = true;
				}else if(Integer.parseInt(prev_ss) < Integer.parseInt(curr_ss))
				{
					crashFlag = false;
				}
				
				if(Integer.parseInt(prev_m) > Integer.parseInt(curr_m))
				{
					crashFlag = true;
				}else if(Integer.parseInt(prev_m) < Integer.parseInt(curr_m))
				{
					crashFlag = false;
				}
				
				if(Integer.parseInt(prev_mm) > Integer.parseInt(curr_mm))
				{
					crashFlag = true;
				}else if(Integer.parseInt(prev_mm) < Integer.parseInt(curr_mm))
				{
					crashFlag = false;
				}
				
				if(Integer.parseInt(prev_h) > Integer.parseInt(curr_h))
				{
					crashFlag = true;
				}else if(Integer.parseInt(prev_h) < Integer.parseInt(curr_h))
				{
					crashFlag = false;
				}
				
				if(Integer.parseInt(prev_hh) > Integer.parseInt(curr_hh))
				{
					crashFlag = true;
				}else if(Integer.parseInt(prev_hh) < Integer.parseInt(curr_hh))
				{
					crashFlag = false;
				}
				
				if(crashFlag) {crashResults += " secod crashes";}
				
			}catch(Exception e){System.out.println(e.getMessage());} 
		}
		return crashResults;
	}
	
	// Return looks like {BX1, BX2}
	private String[] getUpTimeFromSystemFile()
	{
		String sut1 = "";
		String sut2 = "";
		try(BufferedReader in = new BufferedReader(new FileReader("src/main/resources/current_uptime")))
		{
			sut1 = in.readLine(); // It is an uptime of a first bx.
			sut2 = in.readLine(); // It is an uptime of a first bx.
		}catch(Exception e){System.out.println(e.getMessage()); return null;} 
		String [] upTimeArray = {sut1, sut2} ;
		return upTimeArray;
	}
		
	public static List<String> getEmailAddressesFromSystemFolder(String emailSystemFolder) { 
		
		List<String> email_addresses = new ArrayList<>();
		
		try(BufferedReader in = new BufferedReader(new FileReader(emailSystemFolder)))
		{
			// Walk through email addresses
			while(  true  )
			{
				String line = in.readLine();
				if(line == null)
				{
					break;
				}
				email_addresses.add(line);
			}
			
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
			return null;
		}
		return email_addresses;
	}
}
