package com.zixi.drivers.drivers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.testng.Reporter;

import com.zixi.drivers.drivers.AutomatedTelnetClient;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class FFMPEGImageStatisticTestDriver extends BroadcasterLoggableApiWorker implements TestDriver {

	private static final String 	hostName          = "10.7.0.68";
	private static final int    	portNumber       =	4445;
	private static final int    	attempts             = 	10;
	private static final int    	hlsAttempts   	    = 	60;
	private static final String 	fromUser      	    = 	"get";
	private static final int    	negativeAttempts   = 	 5;
	private Socket 					clientSocket;
	private PrintWriter 			out;
	private BufferedReader 			in;
	
	// Default constructor.
	public FFMPEGImageStatisticTestDriver() {}
	// Parameterized constructor.
	public FFMPEGImageStatisticTestDriver(StringBuffer testFlowDescriptor) { super(testFlowDescriptor); }
	
	
	
	public DriverReslut testSetUp() 
	{
		long sum = 0;
		int loopCnt = 0;
		try {
			clientSocket = new Socket(hostName, portNumber);
			clientSocket.setSoTimeout(60000);
			 
            out = new PrintWriter(clientSocket.getOutputStream(), true);
		 
            in  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String fromServer = in.readLine();
			System.out.println("Expected is connected " + fromServer);
			if (fromServer.equals("connected"))
			{
				for (;loopCnt < attempts; loopCnt ++ )
				{
					out.println(fromUser);
					fromServer = in.readLine();
					System.out.println("Expected is output " + fromServer);
					if(fromServer.equals("output"))
					{
						fromServer = in.readLine();
						System.out.println("Expected is number " + fromServer);
						if(fromServer.equals("1"))
						{
							sum += Long.parseLong(fromServer, 10);   
						}
						fromServer = in.readLine();
						System.out.println("Results " + fromServer);
					}
				}
			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
		}
		finally{
			try {
				Reporter.log("FFMPEG success measurement relation: " + sum+ " / " + loopCnt);
			if (clientSocket != null)
				clientSocket.close();
			if (out != null)
				out.close();
			if (in!=null)
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if ((sum / attempts) >= 0.8)
		{
			driverReslut = new DriverReslut("good");
			driverReslut.touchResutlDescription("The number of sucessful attempts was " + sum + "");
			return driverReslut ; 
		}
		else 
		{
			driverReslut = new DriverReslut("bad");
			driverReslut.touchResutlDescription("The number of sucessful attempts was " + sum + "");
			return driverReslut ; 
		}
	}
	
	public DriverReslut testStatistic() 
	{
		long sum 	= 	0;
		int loopCnt	=	0;
		
		try {
			clientSocket = new Socket(hostName, portNumber);
			clientSocket.setSoTimeout(60000);
			 
            out = new PrintWriter(clientSocket.getOutputStream(), true);
		 
            in  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String fromServer = in.readLine();
			System.out.println("Expected is connected " + fromServer);
			if (fromServer.equals("connected"))
			{
				for (loopCnt = 0; loopCnt < attempts; loopCnt ++ )
				{
					out.println(fromUser);
					fromServer = in.readLine();
					System.out.println("Expected is output " + fromServer);
					if(fromServer.equals("output"))
					{
						fromServer = in.readLine();
						System.out.println("Expected is number " + fromServer);
						if(fromServer.equals("1"))
						{
							sum += Long.parseLong(fromServer, 10);   
						}
						fromServer = in.readLine();
						System.out.println("Results " + fromServer);
					}
				}
			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
		}
		finally{
			try {
				Reporter.log("FFMPEG success measurement relation: " + sum+ " / " + loopCnt);
			if (clientSocket != null)
				clientSocket.close();
			if (out != null)
				out.close();
			if (in!=null)
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if ((sum / attempts) >= 0.8)
		{
			driverReslut = new DriverReslut("good");
			driverReslut.touchResutlDescription("The number of sucessful attempts was " + sum + "");
			return driverReslut ; 
		}
		else 
		{
			driverReslut = new DriverReslut("The number of sucessful attempts was " + sum + "");
			driverReslut.touchResutlDescription("The number of sucessful attempts was " + sum + "");
			return driverReslut ; 
		}
	}
	
	public DriverReslut testStatistic(String mode) 
	{
		long 	sum 	= 0;
		int 	loopCnt	=	0;
		try {
			// This class implements client sockets (also called just "sockets").
			clientSocket = new Socket(hostName, portNumber);
			clientSocket.setSoTimeout(120000);
			 
            out = new PrintWriter(clientSocket.getOutputStream(), true);
		 
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String fromServer = in.readLine();
			//System.out.println("Expected is connected " + fromServer);
			if (fromServer.equals("connected"))
			{
				System.out.println("Starting FFMPEG probing ...........");
				for (loopCnt = 0; loopCnt < hlsAttempts; loopCnt ++ )
				{
					System.out.println("Sent to server " + mode);
					out.println(mode);
					fromServer = in.readLine();
					//System.out.println("Expected is output " + fromServer);
					if(fromServer.equals("output"))
					{
						fromServer = in.readLine();
						//System.out.println("Expected is number, actual is: " + fromServer);
						if(fromServer.equals("1"))
						{
							sum += Long.parseLong(fromServer, 10);   
						}
						fromServer = in.readLine();
						//System.out.println("Expected is accepted, actual is: " + fromServer);
					}
				}
			}
			
		} catch (UnknownHostException e) {
			System.err.println("Don't know about the host " + hostName);
			driverReslut = new DriverReslut("Proxy server exeption");
			return driverReslut;
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to "
					+ hostName);
			driverReslut = new DriverReslut("Proxy server exeption");
			return driverReslut;
		}
		finally{
			try {
				Reporter.log("FFMPEG success measurement relation: " + sum+ " / " + loopCnt);
			if (clientSocket != null)
				clientSocket.close();
			if (out != null)
				out.close();
			if (in!=null)
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if ((sum / attempts) >= 0.8)
		{
			driverReslut = new DriverReslut("good");
			driverReslut.touchResutlDescription("The number of sucessful attempts was " + sum + "");
			return driverReslut ; 
		}
		else 
		{
			driverReslut = new DriverReslut("bad");
			driverReslut.touchResutlDescription("The number of sucessful attempts was " + sum + "");
			return driverReslut ; 
		}
	}
	
	
	public DriverReslut testStatistic(int mode) 
	{
		long 	time = 0;
		int 	drop = -1;
		try {
			// This class implements client sockets (also called just "sockets"). A socket is an endpoint for communication between two machines.
			clientSocket = new Socket(hostName, portNumber);
			clientSocket.setSoTimeout(120000);
			 
            out = new PrintWriter(clientSocket.getOutputStream(), true);
		 
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String fromServer = in.readLine();
			//System.out.println("Expected is connected " + fromServer);
			if (fromServer.equals("connected"))
			{
				//for (loopCnt = 0; loopCnt < hlsAttempts; loopCnt ++ )
				//{
					out.println("maxwell");
					fromServer = in.readLine();
					//System.out.println("Expected is output " + fromServer);
					if(fromServer.equals("output"))
					{
						fromServer = in.readLine();
						time = Long.parseLong(fromServer, 10);   
						drop = Integer.parseInt(in.readLine());
						
						//System.out.println("Expected is accepted, actual is: " + fromServer);
					}
				//}//
			}
			
		} catch (UnknownHostException e) { System.err.println("Don't know about host " + hostName); } 
		catch (IOException e) { System.err.println("Couldn't get I/O for the connection to " + hostName); }
		finally{
			try { if (clientSocket != null) clientSocket.close();
			if (out != null) out.close();
			if (in!=null) in.close();
			} catch (IOException e) { e.printStackTrace(); }
		}
			driverReslut = new DriverReslut("Total G1050 test running time is " + time + " Number of dropped by Maxwell packets durring this test is " + drop);
			return driverReslut ; 
	}
	

	
	
	
	public DriverReslut testStatistic(boolean isNegative) 
	{
		// TODO Auto-generated method stub
		long sum = 0;
		int loopCnt	=	0;
		
		try {
			clientSocket = new Socket(hostName, portNumber);
			
			clientSocket.setSoTimeout(120000);
			 
            out = new PrintWriter(clientSocket.getOutputStream(), true);
		 
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			String fromServer = in.readLine();
			System.out.println("Expected is connected " + fromServer);
			if (fromServer.equals("connected"))
			{
				for (loopCnt = 0; loopCnt < negativeAttempts; loopCnt ++ )
				{
					out.println(fromUser);
					fromServer = in.readLine();
					System.out.println("Expected is output " + fromServer);
					if(fromServer.equals("output"))
					{
						fromServer = in.readLine();
						System.out.println("Expected is number, actual is: " + fromServer);
						if(fromServer.equals("1"))
						{
							sum += Long.parseLong(fromServer, 10);   
						}
						fromServer = in.readLine();
						System.out.println("Expected is accepted, actual is: " + fromServer);
					}
				}
			}
			
		} catch (UnknownHostException e) { System.err.println("Don't know about host " + hostName); }
		  catch (IOException e) { System.err.println("Couldn't get I/O for the connection to " + hostName); }
		finally{
			try {
				Reporter.log("FFMPEG success measurement relation: " + sum+ " / " + loopCnt);
			if (clientSocket != null)
				clientSocket.close();
			if (out != null)
				out.close();
			if (in!=null)
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (sum == 0 )
		{
			driverReslut = new DriverReslut("The number of sucessful attempts was " + sum + "");
			driverReslut.touchResutlDescription("The number of sucessful attempts was " + sum + "");
			return driverReslut ; 
		}
		else 
		{
			driverReslut = new DriverReslut("bad");
			driverReslut.touchResutlDescription("The number of sucessful attempts was " + sum + "");
			return driverReslut ; 
		}
	}

	public DriverReslut backUpBondedPartialLimitation()
	{
		/////////////////////////////////////THREAD DEFNINITION AND THREAD STARTING///////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 * In order to provide functioning of this test the following preset must be on the cisco's router.
		 * 
		 */
		Thread ciscoTelnetThread = new Thread( () -> {
			try {
	    		AutomatedTelnetClient telnet = new AutomatedTelnetClient("10.7.0.140", "cisco", "cisco");
	    		
	    		telnet.sendCommand("configure terminal\n");
	    		telnet.sendCommand("interface gigabitEthernet 1/0\n");
	    		telnet.sendCommand("interface gigabitEthernet 1/0\n");
	    		telnet.sendCommand("no traffic-shape group 145 128000 7936 7936 1000\n");
	    		telnet.sendCommand("no traffic-shape group 145 128000 7936 7936 1000\n");
	    		
	    		while(! Thread.interrupted()){
		    		System.out.println("Got Connection...");
		    		
		    		Thread.sleep(180_000); // Wait a two minute.
		    		
		    		telnet.sendCommand("traffic-shape group 145 128000 7936 7936 1000\n");
		    		telnet.sendCommand("traffic-shape group 145 128000 7936 7936 1000\n");
		    		
		    		Thread.sleep(180_000); // Wait a two minute.
		    		
		    		telnet.sendCommand("no traffic-shape group 145 128000 7936 7936 1000\n");
		    		telnet.sendCommand("no traffic-shape group 145 128000 7936 7936 1000\n");
			} // End while
	    		telnet.disconnect();
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
		}
		); ciscoTelnetThread.start(); // run the thread
		////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////		
		
		long sum = 0;
		int loopCnt = 0;
		try {
			clientSocket = new Socket(hostName, portNumber);
			clientSocket.setSoTimeout(120000);
			 
	        out = new PrintWriter(clientSocket.getOutputStream(), true);
		 
	        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	
			String fromServer = in.readLine();
			System.out.println("Expected is connected " + fromServer);
			if (fromServer.equals("connected"))
			{
				for (loopCnt = 0; loopCnt < 60_000; loopCnt ++ )
				{
					out.println(fromUser);
					fromServer = in.readLine();
					System.out.println("Expected is output " + fromServer);
					if(fromServer.equals("output"))
					{
						fromServer = in.readLine();
						System.out.println("Expected is number " + fromServer);
						if(fromServer.equals("1"))
						{
							sum += Long.parseLong(fromServer, 10);   
						}
						fromServer = in.readLine();
						System.out.println("Expected is accepted " + fromServer);
					}
					Thread.sleep(10_000); 
				}
			}
			ciscoTelnetThread.interrupt();
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally{
			try {
				Reporter.log("FFMPEG success measurement relation: " + sum+ " / " + loopCnt);
			if (clientSocket != null)
				clientSocket.close();
			if (out != null)
				out.close();
			if (in!=null)
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if ((sum / attempts) >= 0.9)
		{
			driverReslut = new DriverReslut("good");
			driverReslut.touchResutlDescription("The number of sucessful attempts was " + sum + "");
			return driverReslut ; 
		}
		else 
		{
			driverReslut = new DriverReslut("bad");
			driverReslut.touchResutlDescription("The number of sucessful attempts was " + sum + "");
			return driverReslut ; 
		}
	}
	
	public DriverReslut backUpBondedFullLimitation()
	{
		Thread mainThread = Thread.currentThread();
		testFlowDescriptor.append("\nStart the test driver FFMPEGImageStatisticTestDriver.backUpBondedFullLimitation" );
		/////////////////////////////////////THREAD DEFNINITION AND THREAD STARTING...////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 * In order to provide correct functioning of this test the following preset must be done on the cisco router.
		 * 
		 */
		Thread ciscoTelnetThread = new Thread( () -> {
			try {
				
				BroadcaserSingleOutputStreamDeletionDriver.getPid("root",  "zixiroot1234",  "10.7.0.65",  "22",  "ifdown ens35");
	    		while(! Thread.interrupted()){
		    		Thread.sleep(1000); // Wait a three minutes.
			} // End while
	    		BroadcaserSingleOutputStreamDeletionDriver.getPid("root",  "zixiroot1234",  "10.7.0.65",  "22",  "ifup ens35");	
			} catch (Exception e) { mainThread.interrupt(); e.printStackTrace(); }
		}); 
		////////////////////////////////////////////////////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////////////////////////////////////		
		
		long sum = 0;
		int loopCnt  = 0;
		try {
			Thread.sleep(10000);
			ciscoTelnetThread.start(); // run the thread
			clientSocket = new Socket(hostName, portNumber);
			clientSocket.setSoTimeout(120000);
			 
	        out = new PrintWriter(clientSocket.getOutputStream(), true);
	        in  = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	
			String fromServer = in.readLine();
			System.out.println("Expected is connected " + fromServer);
			
			if (fromServer.equals("connected"))
			{
				// Ask FFMPEG server to bring its results.
				for (loopCnt = 0; loopCnt < 20; loopCnt ++ )
				{
					if (mainThread.isInterrupted())
					{
						return driverReslut = new DriverReslut("bad");
					}
					
					out.println(fromUser);
					fromServer = in.readLine();
					System.out.println("Expected is output " + fromServer);
					if(fromServer.equals("output"))
					{
						fromServer = in.readLine();
						System.out.println("Expected is number " + fromServer);
						if(fromServer.equals("1"))
						{
							sum += Long.parseLong(fromServer, 10);   
						}
						fromServer = in.readLine();
						System.out.println("Expected is accepted " + fromServer);
					}
					// Wait because of 
					//Thread.sleep(10_000); 
				}
			}
			ciscoTelnetThread.interrupt();
			ciscoTelnetThread.join();	
			
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + hostName);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		finally{
			try {
				Reporter.log("FFMPEG success measurement relation: " + sum+ " / " + loopCnt);
			if (clientSocket != null)
				clientSocket.close();
			if (out != null)
				out.close();
			if (in!=null)
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if ((sum / attempts) >= 0.9)
		{
			driverReslut = new DriverReslut("good");
			driverReslut.touchResutlDescription("The number of sucessful attempts was " + sum + "");
			return driverReslut ; 
		}
		else 
		{
			driverReslut = new DriverReslut("bad");
			driverReslut.touchResutlDescription("The number of sucessful attempts was " + sum + "");
			return driverReslut ; 
		}
	}
}
