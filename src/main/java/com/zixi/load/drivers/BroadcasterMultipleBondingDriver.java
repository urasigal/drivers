package com.zixi.load.drivers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import com.zixi.drivers.drivers.TestDriver;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.threads.ZthreadPool;
import com.zixi.tools.BroadcasterLoggableApiWorker;


public class BroadcasterMultipleBondingDriver extends BroadcasterLoggableApiWorker implements TestDriver{
	
	// Default constructor.
	public BroadcasterMultipleBondingDriver() {
		// TODO Auto-generated constructor stub
	}
	
	// 
	public BroadcasterMultipleBondingDriver(StringBuffer testFlowDescriptor) {
		super(testFlowDescriptor);
	}
	
	public DriverReslut testIMPL(String... args) throws InterruptedException, ExecutionException
	{	
		// Pass parameters to arrayList in order to provide them to ZthreadPool.
		ArrayList<String> parameters = new ArrayList<String>(Arrays.asList(args));
		ZthreadPool zthreadPool =  new ZthreadPool(10, parameters);
		
		driverReslut = new DriverReslut(zthreadPool.zexecuteBondedBroadcaster2Bx(testFlowDescriptor));
		
		return driverReslut;
	}
}