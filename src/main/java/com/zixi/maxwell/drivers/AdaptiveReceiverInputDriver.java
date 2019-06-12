package com.zixi.maxwell.drivers;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.zixi.drivers.drivers.ReceiverInputStatisticDriver;
import com.zixi.drivers.drivers.TestDriver;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tcpserver.MaxwellTcpAPIClient;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class AdaptiveReceiverInputDriver  extends BroadcasterLoggableApiWorker implements TestDriver {
	
	private ReceiverInputStatisticDriver 	receiverInputStatisticDriver ;
	private Thread 						 	receiverStatisticsCollectorThread;
	private String 							guserName, guserPassword, glogin_ip, guiport, gid;
	private DriverReslut 					testResultDriver;
	
	private static int rowCounter = 60;
	
	public DriverReslut testIMPL(String login_ip, String userName, String userPassword, String uiport, String stream_id,
			String maxwellIp, String maxwell_match_control, String maxwellPort, String maxwellImpairmentsCommand) throws Exception {
		
		// Instantiate stream statistics.
		receiverInputStatisticDriver  = new ReceiverInputStatisticDriver();
		
		// Logging to a Receiver server.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet( "http://" + login_ip + ":" + uiport + "/login.htm", userName, userPassword, login_ip, uiport);
           
		Thread.sleep(60_000);
		
		// Instantiate a thread object and provide it with a run method.
		receiverStatisticsCollectorThread = new Thread( () -> {
			try 
			{
				 testResultDriver = receiverInputStatisticDriver.continiousCheck(userName, userPassword, login_ip, uiport, responseCookieContainer ,
				 stream_id);
			} catch (Exception e) { e.printStackTrace(); }
		});
		
		// Starting to measure statistic of a receiver input stream.
		receiverStatisticsCollectorThread.start();
		
		Thread.sleep( 60 * rowCounter );
		
		 ///////////////////////////////////////////////////////////////////////////////////////////
		//			Configure Maxwell and start impairments. 									 //	
	   ///////////////////////////////////////////////////////////////////////////////////////////
	  
		//////////////////////////////Set flow match pattern //////////////////////////////////////////////////
		MaxwellTcpAPIClient.maxwellconnect(maxwellIp, Integer.parseInt(maxwellPort), maxwell_match_control);
		MaxwellTcpAPIClient.maxwellconnect(maxwellIp, Integer.parseInt(maxwellPort), "getmatch 0 0");
		
		MaxwellTcpAPIClient.maxwellconnect(maxwellIp, Integer.parseInt(maxwellPort), maxwellImpairmentsCommand);
		MaxwellTcpAPIClient.maxwellconnect(maxwellIp, Integer.parseInt(maxwellPort), "getimpair 0 0");
		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
		// waiting.
		Thread.sleep(180_000);
		
		// Stopping monitor thread. 
		receiverInputStatisticDriver.setStopFlag(false);
		
		// wait till receiverStatisticsCollectorThread thread will complete.
		receiverStatisticsCollectorThread.join();
		
		///////////////////////////// Release impairment Maxwell //////////////////////////////////////////
		MaxwellTcpAPIClient.maxwellconnect(maxwellIp, Integer.parseInt(maxwellPort), "setimpair 0 0");
		MaxwellTcpAPIClient.maxwellconnect(maxwellIp, Integer.parseInt(maxwellPort), "getimpair 0 0");
		//////////////////////////////////////////////////////////////////////////////////////////////////
		
		// Get statistic result measured on a receiver side as arrayList. 
		ArrayList<String> statisticsResults = (ArrayList<String>)(testResultDriver.getGeneralResult());
		
		boolean isStable = true;	
		
		System.out.println("-===================%%%%%%%%%%%%%%%%%%%%%%%%%%%%% length is " + statisticsResults.size());
		
		LinkedList<Integer> dek = new  LinkedList<Integer>();
		// int rowCounter = 60;
		
		// get splitted row. [0] -> bitrate, [1] -> dropped, [2] -> recovered
		LinkedList<Integer> list = IntStream.range(rowCounter, statisticsResults.size())
        .map( i -> Integer.parseInt( statisticsResults.get(i).split("#")[1]) ). boxed().collect(Collectors.toCollection(LinkedList::new));
		
		isStable = list.stream().anyMatch( droppedPacketValue -> {   
			boolean predicate = false;
		////for( ; rowCounter < statisticsResults.size(); rowCounter ++)
		//{
			// get splitted row. [0] -> bitrate, [1] -> dropped, [2] -> recovered
			//String splittedResults[] = statisticsResults.get(rowCounter).split("#");
			rowCounter ++;
			if(dek.size() >= 50) dek.poll();
			
			dek.add(droppedPacketValue);
			
			if(dek.size() >= 50)
			{
				predicate = false;
				for(int j = 0; j < 49; j ++)
				{
					predicate = true;
					if(dek.get(j).equals(dek.get(j +1)) != true)
					{
						predicate = false;
						break;
					}
				}
			}
			return predicate; 
		}
	);
	
		if(! isStable)
			return new DriverReslut("BAD");
		
		System.out.println(rowCounter - 60);
		
		for(; rowCounter < statisticsResults.size(); rowCounter ++)
		{
			// get splitted row. [0] -> bitrate, [1] -> dropped, [2] -> recovered
			String splittedResults[] = statisticsResults.get(rowCounter).split("#");
			System.out.println(splittedResults[1]);
		}
		
		return  new DriverReslut("Test passed accordingly predefined parameters");
	}	

	public DriverReslut testIMPLUpCase(String login_ip, String userName, String userPassword, String uiport, String stream_id,
	String maxwellIp, String maxwell_match_control, String maxwellPort, String maxwellImpairmentsCommand) throws Exception {
		rowCounter = 120;
		// Instantiate stream statistics.
		receiverInputStatisticDriver  = new ReceiverInputStatisticDriver();
		
		// Logging to a Receiver server.
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet( "http://" + login_ip + ":" + uiport + "/login.htm", userName, userPassword, login_ip, uiport);
           
		Thread.sleep(60_000);
		
		// Instantiate a thread object and provide it with a run method.
		// This thread will collect receiver input stream's statistic data and buffer it to collection container.
		receiverStatisticsCollectorThread = new Thread( () -> {
			try 
			{
				 testResultDriver = receiverInputStatisticDriver.continiousCheck(userName, userPassword, login_ip, uiport, responseCookieContainer ,
				 stream_id);
			} catch (Exception e) { e.printStackTrace(); }
		});
		System.out.println("Start Statistic measurement");
		// Starting to measure statistic of a receiver input stream.
		receiverStatisticsCollectorThread.start();
		
		 ///////////////////////////////////////////////////////////////////////////////////////////
		//			Configure Maxwell network simulator and start impairments. 					 //	
	   ///////////////////////////////////////////////////////////////////////////////////////////
	  
		System.out.println("Start impairment");
		//////////////////////////////Set flow match pattern //////////////////////////////////////////////////
		MaxwellTcpAPIClient.maxwellconnect(maxwellIp, Integer.parseInt(maxwellPort), maxwell_match_control);
		MaxwellTcpAPIClient.maxwellconnect(maxwellIp, Integer.parseInt(maxwellPort), "getmatch 0 0");
		
		MaxwellTcpAPIClient.maxwellconnect(maxwellIp, Integer.parseInt(maxwellPort), maxwellImpairmentsCommand);
		MaxwellTcpAPIClient.maxwellconnect(maxwellIp, Integer.parseInt(maxwellPort), "getimpair 0 0");
		//////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// Waiting while impairments are applied.
		Thread.sleep(120_000);
		
		System.out.println("Stop impairment");
		///////////////////////////// Release impairment Maxwell //////////////////////////////////////////
		MaxwellTcpAPIClient.maxwellconnect(maxwellIp, Integer.parseInt(maxwellPort), "setimpair 0 0");
		MaxwellTcpAPIClient.maxwellconnect(maxwellIp, Integer.parseInt(maxwellPort), "getimpair 0 0");
		//////////////////////////////////////////////////////////////////////////////////////////////////
		
		Thread.sleep(60_000);
		
		// Stopping monitor thread. 
		receiverInputStatisticDriver.setStopFlag(false);
		
		// Wait till receiverStatisticsCollectorThread thread will complete.
		receiverStatisticsCollectorThread.join();
		System.out.println("Stop statistic measurement");
		
		// Get statistic result measured on a receiver side as arrayList. 
		ArrayList<String> statisticsResults = (ArrayList<String>)(testResultDriver.getGeneralResult());
		
		boolean isStable = true;	
		
		System.out.println("-===================%%%%%%%%%%%%%%%%%%%%%%%%%%%%% length is " + statisticsResults.size());
		
		LinkedList<Integer> dek = new  LinkedList<Integer>();
		
		// get splitted row. [0] -> bitrate, [1] -> dropped, [2] -> recovered
		LinkedList<Integer> list = IntStream.range(rowCounter, statisticsResults.size())
        .map( i -> Integer.parseInt( statisticsResults.get(i).split("#")[1]) ). boxed().collect(Collectors.toCollection(LinkedList::new));
		
		isStable = list.stream().anyMatch( droppedPacketValue -> {   
			boolean predicate = false;
		
			if(dek.size() >= 50) dek.poll();
			
			dek.add(droppedPacketValue);
			
			if(dek.size() >= 50)
			{
				predicate = false;
				for(int j = 0; j < 49; j ++)
				{
					predicate = true;
					if(dek.get(j).equals(dek.get(j +1)) != true)
					{
						predicate = false;
						break;
					}
				}
			}
			if(predicate)
				dek.stream().forEach( (element) -> System.out.println( "Stop element " + element) );
			return predicate; 
		}
	);
	
		if(! isStable)
			return new DriverReslut("BAD");
		
		return  new DriverReslut("Test passed accordingly predefined parameters");
	}
}