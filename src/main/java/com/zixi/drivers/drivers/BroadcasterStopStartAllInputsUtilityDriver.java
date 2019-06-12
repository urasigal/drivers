package com.zixi.drivers.drivers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class BroadcasterStopStartAllInputsUtilityDriver extends BroadcasterLoggableApiWorker
{
	protected BroadcasterInputStreamDriver broadcasterInputStreamDriver = new BroadcasterInputStreamDriver();
	protected static final int NUMBER_OF_THREADS = 5 ;
	
	public DriverReslut testIMPL(String login_ip, String userName, String userPassword, String uiport) throws Exception
	{
		// Retrieve input stream names from a broadcaster server.
		String[] streamsNames  = broadcasterInputStreamDriver.retrieveAllInputStreamsFromBroadcaser(login_ip, userName, userPassword, uiport);
		
		ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS); 
		
		ArrayList<Callable<String>> callablesTasks = new ArrayList<Callable<String>>();
		
		int taskNumbers = streamsNames.length;
		
		String[] params = {login_ip, userName, userPassword, "", uiport};
		
		for(int i = 0; i < taskNumbers; i ++)
		{
			params[3] = streamsNames[i];
			String tmpParams[] = params.clone();
			BroadcasterSingleCallableStopStartInputStreamDriver broadcasterSingleCallableStopStartInputStreamDriver = new BroadcasterSingleCallableStopStartInputStreamDriver(tmpParams);
			
			callablesTasks.add(broadcasterSingleCallableStopStartInputStreamDriver);
		}
		
		List<Future<String>> futures = executorService.invokeAll(callablesTasks);
		
		for(Future future : futures)
		{
			System.out.println((String)future.get());
		}
		
		driverReslut = new DriverReslut("good");
		
		return driverReslut;
	}
	
}