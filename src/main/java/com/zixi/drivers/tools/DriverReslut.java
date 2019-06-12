package com.zixi.drivers.tools;

public class DriverReslut {
	
	private String 			result 					= 		null;
	private Object 			generalResult 			=   	null;
	private StringBuffer 	resutlDescription		=		new StringBuffer();
	
	public DriverReslut(String result)
	{
		this.result = result;
	}
	
	// Instantiate object and set any result.
	public DriverReslut(Object generalResult)
	{
		this.generalResult = generalResult;
	}

	public DriverReslut(DriverReslut other)
	{
		this.result 			= other.result;
		this.generalResult  	= other.generalResult;
		this.resutlDescription.append(other.resutlDescription);
	}
	
	public DriverReslut()
	{
	}
	
	public String getResult()
	{
		return result;
	}
	
	public Object getGeneralResult()
	{
		return generalResult;
	}
	
	public void appendResult(String resultToAppend)
	{
		result = result + resultToAppend;
	}
	
	public DriverReslut setResult(String result)
	{
		this.result = result;
		return this;
	}
	
	public DriverReslut getResultObj()
	{
		return this;
	}
	
	public String touchResutlDescription(String toAdd)
	{
		return resutlDescription.append(toAdd).toString();
	}
}
