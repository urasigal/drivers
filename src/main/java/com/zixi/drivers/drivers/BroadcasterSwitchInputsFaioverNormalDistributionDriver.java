package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.LINE_MODE;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterSwitchInputsFaioverNormalDistributionDriver  extends BroadcasterLoggableApiWorker implements TestDriver {
	
	public static void main(String[] args) throws Exception {
		BroadcasterSwitchInputsFaioverNormalDistributionDriver BroadcasterSwitchInputsFaioverNormalDistibutionDriver = new BroadcasterSwitchInputsFaioverNormalDistributionDriver();
		//BroadcasterSwitchInputsFaioverNormalDistibutionDriver.testNormalDistributioInputSwitchingExclude("10.7.0.74", "admin", "1234", "4444", "10.7.0.75", "admin", "1234", "4444", "300000", "1000", "tg");
	}
	
	public DriverReslut testNormalDistributioInputSwitching(String remoteBX_login_ip, 
	String remoteBXuserName, String remoteBXuserPassword, 
	String remoteBXuiport, String login_ip, String userName, String userPassword, String uiport, String testElapsedTime, String streamSwitchInterval, String streamReferenceName)  throws Exception 
	{
		StreamsDriver streamsDriver 	= new StreamsDriver();
		ArrayList<String> outputIds  	=  streamsDriver.broadcasterGetOutputStreamsIds(remoteBX_login_ip, remoteBXuiport, remoteBXuserName, remoteBXuserPassword);
		DriverReslut testResults		        = new DriverReslut("failed") ;
		
		Thread continuityCounterChecker = new Thread( () -> { 
			BroadcasterAnalyzerDriver broadcasterAnalyzerDriver = new BroadcasterAnalyzerDriver();
			while(true) {
				testResults.setResult(broadcasterAnalyzerDriver.boadcasterCompareStatisticOnAllInputsStreams( userName,  userPassword, login_ip,  uiport, streamReferenceName).getResult());
				if(testResults.getResult().equals("failed"))
				{
					return;
				}
				
				try {
					Thread.sleep(10_000);
				} catch (InterruptedException e) {
					return;
				}
				
				if (Thread.currentThread().isInterrupted())
				{
					System.out.println("Interrupted");
					return;
				}
			}
		});
		
		Thread normalSwitch = new Thread(() -> {
			try {
				responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + remoteBX_login_ip + ":" + remoteBXuiport + "/login.htm", remoteBXuserName,
				remoteBXuserPassword, remoteBX_login_ip, remoteBXuiport);
				Random rand = new Random();
				int amountOfOutputStreams = outputIds.size();
				int nextIndex; 
				
				for(int i = 0; i < Integer.parseInt(testElapsedTime); i+= Integer.parseInt(streamSwitchInterval))
				{
					nextIndex = rand.nextInt(amountOfOutputStreams);
					String outputId = outputIds.get(nextIndex);
					apiworker.sendGet("http://"+ remoteBX_login_ip +":"+ remoteBXuiport + "/zixi/edit_output.json?id=" + outputId + "&"
					+ "on="+ "0", "", LINE_MODE, responseCookieContainer, remoteBX_login_ip, this, remoteBXuiport);
					
					Thread.sleep(Integer.parseInt(streamSwitchInterval));
					
					apiworker.sendGet("http://"+ remoteBX_login_ip +":"+ remoteBXuiport + "/zixi/edit_output.json?id=" + outputId + "&"
					+ "on="+ "1", "", LINE_MODE, responseCookieContainer, remoteBX_login_ip, this, remoteBXuiport);
					
					Thread.sleep(Integer.parseInt(streamSwitchInterval));
				}
				System.out.println("Sending interuppt!!!");
				continuityCounterChecker.interrupt();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		normalSwitch.start();
		continuityCounterChecker.start();
		normalSwitch.join();
		continuityCounterChecker.join();
		return testResults;
	}
	
	public DriverReslut testNormalDistributioInputSwitchingReceiver(String remoteBX_login_ip, String remoteBXuserName, String remoteBXuserPassword,
	String remoteBXuiport, String testElapsedTime, String streamSwitchInterval)  throws Exception 
	{
		StreamsDriver streamsDriver 	= new StreamsDriver();
		ArrayList<String> outputIds  	=  streamsDriver.broadcasterGetOutputStreamsIds(remoteBX_login_ip, remoteBXuiport, remoteBXuserName, remoteBXuserPassword);
		DriverReslut res 				= new DriverReslut("pass") ;
		
		Thread normalSwitch = new Thread(() -> {
			try {
				responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + remoteBX_login_ip + ":" + remoteBXuiport + "/login.htm", remoteBXuserName,
				remoteBXuserPassword, remoteBX_login_ip, remoteBXuiport);
				Random rand = new Random();
				int amountOfOutputStreams = outputIds.size();
				int nextIndex; 
				
				for(int i = 0; i < Integer.parseInt(testElapsedTime); i+= Integer.parseInt(streamSwitchInterval))
				{
					nextIndex = rand.nextInt(amountOfOutputStreams);
					String outputId = outputIds.get(nextIndex);
					apiworker.sendGet("http://"+ remoteBX_login_ip +":"+ remoteBXuiport + "/zixi/edit_output.json?id=" + outputId + "&"
					+ "on="+ "0", "", LINE_MODE, responseCookieContainer, remoteBX_login_ip, this, remoteBXuiport);
					
					Thread.sleep(Integer.parseInt(streamSwitchInterval));
					
					apiworker.sendGet("http://"+ remoteBX_login_ip +":"+ remoteBXuiport + "/zixi/edit_output.json?id=" + outputId + "&"
					+ "on="+ "1", "", LINE_MODE, responseCookieContainer, remoteBX_login_ip, this, remoteBXuiport);
				}
				System.out.println("Sending interuppt!!!");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		normalSwitch.start();
		normalSwitch.join();
		return res;
	}
	
	
	public DriverReslut testNormalDistributioInputSwitchingExclude(String remoteBX_login_ip, 
	String remoteBXuserName, String remoteBXuserPassword, String remoteBXuiport, String login_ip, String userName,
	String userPassword, String uiport, String testElapsedTime, 
	String streamSwitchInterval, String streamReferenceName, String streamExcludeName)
	throws Exception 
	{
		StreamsDriver streamsDriver 	= new StreamsDriver();
		ArrayList<String> outputIds  	=  streamsDriver.broadcasterGetOutputStreamsIds(remoteBX_login_ip, remoteBXuiport, remoteBXuserName, remoteBXuserPassword);
		DriverReslut testResults 				= new DriverReslut("failed");
		
		for (String temp : outputIds ) {
		    if (temp.matches(streamExcludeName)){
		    	outputIds.remove(temp);
		    }
		}
		
		Thread continuityCounterChecker = new Thread(() -> { 
			BroadcasterAnalyzerDriver broadcasterAnalyzerDriver = new BroadcasterAnalyzerDriver();
			while(true) {
				testResults.setResult(broadcasterAnalyzerDriver.boadcasterCompareStatisticOnAllInputsStreams( userName,  userPassword, login_ip,  uiport, streamReferenceName).getResult());
				if(testResults.getResult().equals("failed"))
				{
					return;
				}
				
				try {
					Thread.sleep(10_000);
				} catch (InterruptedException e) {
					return;
				}
				
				if (Thread.currentThread().isInterrupted())
				{
					System.out.println("Interrupted");
					return;
				}
			}
		});
		
		Thread normalSwitch = new Thread(() -> {
			try {
				responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + remoteBX_login_ip + ":" + remoteBXuiport + "/login.htm", remoteBXuserName,
				remoteBXuserPassword, remoteBX_login_ip, remoteBXuiport);
				Random rand = new Random();
				int amountOfOutputStreams = outputIds.size();
				int nextIndex; 
				
				for(int i = 0; i < Integer.parseInt(testElapsedTime); i+= Integer.parseInt(streamSwitchInterval))
				{
					nextIndex = rand.nextInt(amountOfOutputStreams);
					String outputId = outputIds.get(nextIndex);
					apiworker.sendGet("http://"+ remoteBX_login_ip +":"+ remoteBXuiport + "/zixi/edit_output.json?id=" + outputId + "&"
					+ "on="+ "0", "", LINE_MODE, responseCookieContainer, remoteBX_login_ip, this, remoteBXuiport);
					
					Thread.sleep(Integer.parseInt(streamSwitchInterval));
					
					apiworker.sendGet("http://"+ remoteBX_login_ip +":"+ remoteBXuiport + "/zixi/edit_output.json?id=" + outputId + "&"
					+ "on="+ "1", "", LINE_MODE, responseCookieContainer, remoteBX_login_ip, this, remoteBXuiport);
				}
				System.out.println("Sending interuppt!!!");
				continuityCounterChecker.interrupt();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		normalSwitch.start();
		continuityCounterChecker.start();
		normalSwitch.join();
		continuityCounterChecker.join();
		return testResults;
	}
	
	public DriverReslut feederNormalDistributioInputSwitching(String remoteBX_login_ip, String remoteBXuserName, String remoteBXuserPassword,
	String remoteBXuiport, String login_ip, String userName, String userPassword, String uiport, String testElapsedTime, String streamSwitchInterval,
	String streamReferenceName, String streams)  throws Exception 
	{
		StreamsDriver streamsDriver 	= new StreamsDriver();
		ArrayList<String> outputIds  	=  streamsDriver.broadcasterGetOutputStreamsIds(remoteBX_login_ip, remoteBXuiport, remoteBXuserName, remoteBXuserPassword);
		DriverReslut testResults 				= new DriverReslut("failed") ;
		
		Thread continuityCounterChecker = new Thread(() -> { 
			BroadcasterAnalyzerDriver broadcasterAnalyzerDriver = new BroadcasterAnalyzerDriver();
			while(true) {
				try {
					testResults.setResult(broadcasterAnalyzerDriver.feederCompareStatisticCcErrors(login_ip, userName, userPassword, 
					uiport, streams, streamReferenceName).getResult());
					if(testResults.getResult().equals("failed"))
					{
						return;
					}
					
						Thread.sleep(10_000);
					} catch (Exception e) {
						return;
					}
					
					if (Thread.interrupted())
					{
						System.out.println("Interrupted");
						return;
					}
			}
		});
		
		Thread normalSwitch = new Thread(() -> {
			try {
				responseCookieContainer = broadcasterInitialSecuredLogin.sendGet("http://" + remoteBX_login_ip + ":" + remoteBXuiport + "/login.htm", remoteBXuserName,
				remoteBXuserPassword, remoteBX_login_ip, remoteBXuiport);
				Random rand = new Random();
				int amountOfOutputStreams = outputIds.size();
				int nextIndex; 
				
				for(int i = 0; i < Integer.parseInt(testElapsedTime); i+= Integer.parseInt(streamSwitchInterval))
				{
					nextIndex = rand.nextInt(amountOfOutputStreams);
					String outputId = outputIds.get(nextIndex);
					apiworker.sendGet("http://"+ remoteBX_login_ip +":"+ remoteBXuiport + "/zixi/edit_output.json?id=" + outputId + "&"
					+ "on="+ "0", "", LINE_MODE, responseCookieContainer, remoteBX_login_ip, this, remoteBXuiport);
					
					Thread.sleep(Integer.parseInt(streamSwitchInterval));
					
					apiworker.sendGet("http://"+ remoteBX_login_ip +":"+ remoteBXuiport + "/zixi/edit_output.json?id=" + outputId + "&"
					+ "on="+ "1", "", LINE_MODE, responseCookieContainer, remoteBX_login_ip, this, remoteBXuiport);
				}
				System.out.println("Sending interrupt!!!");
				continuityCounterChecker.interrupt();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		normalSwitch.start();
		continuityCounterChecker.start();
		normalSwitch.join();
		continuityCounterChecker.join();
		return testResults;
	}
}
