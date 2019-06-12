package com.zixi.load.drivers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import com.zixi.drivers.drivers.TestDriver;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.threads.ZthreadPool;
import com.zixi.tools.BroadcasterLoggableApiWorker;
public class FeederBroadcasterBondingMultipleThreeLinkDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	
	public DriverReslut testIMPL(String... args) throws InterruptedException, ExecutionException
	{	
		// Pass parameters to arrayList in order to provide them to ZthreadPool.
		ArrayList<String> parameters = new ArrayList<String>(Arrays.asList(args));
		ZthreadPool zthreadPool =  new ZthreadPool(10, parameters);
		
		driverReslut =  new DriverReslut(zthreadPool.zexecuteBondedFeeder2Bx());
		
		return driverReslut;
	}
}