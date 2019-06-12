package com.zixi.drivers.drivers;

import static com.zixi.globals.Macros.VALIDATOR;

import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;

public class HlsAppleAnalyzerDriver extends BroadcasterLoggableApiWorker implements TestDriver {
	
	public DriverReslut testAnalyzer(String analyzer_url, String hls_url)  throws Exception 
	{
		driverReslut = new DriverReslut(apiworker.sendGetAnalyzer("http://"+ analyzer_url +":"+ "5000?" + "hlsurl=" + hls_url, "", VALIDATOR, analyzer_url, "5000"));
		if( driverReslut.getResult().equals("null") )
			driverReslut.setResult("No errors");
		return driverReslut;
	}
	
	public static void main(String[] args) throws Exception {
		HlsAppleAnalyzerDriver HlsAppleAnalyzerDriver = new HlsAppleAnalyzerDriver();
		DriverReslut DriverReslut = HlsAppleAnalyzerDriver.testAnalyzer("10.7.0.201", "http://10.7.0.201:5000/?name=http://10.7.0.62:7777/at.m3u8");
		String res = DriverReslut.getResult();
	}
}