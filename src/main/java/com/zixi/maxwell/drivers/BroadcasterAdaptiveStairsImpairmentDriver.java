package com.zixi.maxwell.drivers;

import static com.zixi.globals.Macros.ADD_TRANSCODER_PROFILE;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.testng.Assert;

import com.zixi.drivers.drivers.ReceiverInputStatisticDriver;
import com.zixi.drivers.drivers.TestDriver;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.entities.TestParameters;
import com.zixi.tcpserver.MaxwellTcpAPIClient;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterAdaptiveStairsImpairmentDriver extends BroadcasterLoggableApiWorker implements TestDriver {
	
	private ReceiverInputStatisticDriver 	receiverInputStatisticDriver ;
	private Thread 						 	receiverStatisticsCollectorThread;
	private String 							guserName, guserPassword, glogin_ip, guiport, gid;
	private DriverReslut 					testResultDriver;
	
	
	/**
	 * @param maxwell_address     						IP address of Maxwell 
	 * @param standart_impairment_server_api_port		TCP port which Maxwell listening to. 
	 * @param flow_match_control_setmatch				Flow criteria.
	 * @param impairment_control_setimpair				Interference parameters.
	 * @param userName									Receiver user name.
	 * @param userPassword								Receiver user pass.
	 * @param login_ip									Receiver IP address.
	 * @param uiport									Receiver UI port.
	 * @param id										Receiver input stream name.
	 * @return
	 * @throws Exception
	 */
	public DriverReslut testIMPL(String maxwell_address, String standart_impairment_server_api_port, String flow_match_control_setmatch, 
	String impairment_control_setimpair,  String userName, String userPassword, String login_ip, String uiport, String id) throws Exception {
		
		// Instantiate stream statistics. 
		receiverInputStatisticDriver  = new ReceiverInputStatisticDriver();
		
		// Instantiate a thread object, provide it with an instance of inner class. Inner class implements Runnable interface. Here inner class 
		receiverStatisticsCollectorThread = new Thread( () -> {
			try 
			{
				 testResultDriver = receiverInputStatisticDriver.continiousCheck(guserName, guserPassword, glogin_ip, guiport, responseCookieContainer, gid);
			} catch (Exception e) { e.printStackTrace(); }
		});
		
		// Starting to measure statistic of a receiver input stream.
		receiverStatisticsCollectorThread.start();
		
		// Wait a 6 minutes. While this delay no network impairments are applied.
		try { Thread.currentThread().sleep(360_000);}  catch  (InterruptedException e) { e.printStackTrace(); }
		
		////////////////////////////// Set flow match pattern ////////////////////////////////////////////////////////////////////////////
		MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), flow_match_control_setmatch);
		MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), "getmatch 0 0");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		///////////////////////////// Set impairment Maxwell ///////////////////////////////////////////////////////////////////////////
		MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), impairment_control_setimpair);
		MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), "getimpair 0 0");
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// Waiting a 6 minutes in impairment state.
		try { Thread.currentThread().sleep(360_000); }  catch  (InterruptedException e) { e.printStackTrace(); }
		
		
		///////////////////////////// Release impairment Maxwell ///////////////////////////////////////////////////////////////////////////
		MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), "setimpair 0 0");
		MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), "getimpair 0 0");
		
		// Waiting a 5 minutes in released state.
		try { Thread.currentThread().sleep(300000);}  catch  (InterruptedException e) { e.printStackTrace(); }
		
		// Stopping monitor thread. 
		receiverInputStatisticDriver.setStopFlag(false);
		
		// wait till monitor thread will complete.
		receiverStatisticsCollectorThread.join();
		
		ArrayList<String> statisticsResults = (ArrayList<String>)(testResultDriver.getGeneralResult());
		
		int 	prev 				 = 0;
		int 	index				 = 0;
		float 	seccond_array[]		 = {0,0,0};
		float 	half_sec_time 		 = 0;
		boolean isFirst				 = true;	
		
		System.out.println("-===================%%%%%%%%%%%%%%%%%%%%%%%%%%%%% length is " + statisticsResults.size());
		
		PrintWriter pw = new PrintWriter(new FileWriter("src/main/resources/adp"));
		
		for(int i = 0; i < statisticsResults.size(); i ++)
		{
			String splittedResults[] = statisticsResults.get(i).split("#");
//			// [0] - bitrate, [1] - dropped, [2] - recovered.
			System.out.println("Bitrate " + splittedResults[0] + " Dropped packets " + splittedResults[1] + " Recovered packets " + splittedResults[2]);
			pw.write("Bitrate " + splittedResults[0] + " Dropped packets " + splittedResults[1] + " Recovered packets " + splittedResults[2] + "\n");
			//System.out.println("Dropped packets " + splittedResults[1]);
		//	System.out.println("Recovered packets " + splittedResults[2]);
		}
		pw.close();	
		System.out.println("============================");
		
		System.out.println("Statistic size " + statisticsResults.size());
		
		driverReslut = new DriverReslut("GOOD");
		
		return driverReslut;
	}
	
	
	public DriverReslut testIMPL(String maxwell_address, String standart_impairment_server_api_port,
	String flow_match_control_setmatch,  String impairment_control_setimpair) 
	{
				
		////////////////////////////// Set flow match pattern ////////////////////////////////////////////////////////////////////////////
		MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), flow_match_control_setmatch);
		MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), "getmatch 0 0");
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		///////////////////////////// Set impairment Maxwell ///////////////////////////////////////////////////////////////////////////
		MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), impairment_control_setimpair);
		MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), "getimpair 0 0");
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		driverReslut = new DriverReslut("GOOD");	
		return driverReslut;
	}
	
	public DriverReslut testIMPL(String maxwell_address, String standart_impairment_server_api_port,
	String flow_match_control_setmatch, String impairment_control_setimpair1, String impairment_control_setimpair2,
	String g_1050TestCaseNumber, String setlogging) 
			{		
				////////////////////////////// Set flow match pattern ////////////////////////////////////////////////////////////////////////////
				MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), flow_match_control_setmatch);
				MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), "getmatch 0 0");
				/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), setlogging);
				
				///////////////////////////// Set impairment Maxwell ///////////////////////////////////////////////////////////////////////////
				MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), impairment_control_setimpair1);
				MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), "getimpair 0 0");
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				///////////////////////////// Set impairment Maxwell ///////////////////////////////////////////////////////////////////////////
				MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), impairment_control_setimpair2);
				MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), "getimpair 0 0");
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				MaxwellTcpAPIClient.maxwellconnect(maxwell_address, Integer.parseInt(standart_impairment_server_api_port), g_1050TestCaseNumber);
				
				driverReslut = new DriverReslut("GOOD");	
				return driverReslut;
			}
}