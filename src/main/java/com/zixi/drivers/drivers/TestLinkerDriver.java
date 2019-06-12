package com.zixi.drivers.drivers;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import br.eti.kinoshita.testlinkjavaapi.util.TestLinkAPIException;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;
import com.zixi.tools.TestlinkIntegration;


public class TestLinkerDriver extends BroadcasterLoggableApiWorker implements TestDriver
{
	public DriverReslut testIMPL(String version) throws TestLinkAPIException, IOException {
		
		Writer fileWriter = new FileWriter("src/main/resources/build");

		TestlinkIntegration tl = new TestlinkIntegration();
	
		/////////////////////// Define a new build //////////////////////////
		
		fileWriter.write(tl.defineBuild(version) + "");
		fileWriter.close();
		
		driverReslut =  new DriverReslut("success");
		
		return driverReslut;
	}
}
