package com.zixi.tools;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.testng.Reporter;

public class RemoveInputHelper 
{
    private JSONObject json = null;
    private final String USER_AGENT = "Mozilla/5.0";
    // HTTP GET request
    public String sendGet(String url, String id, String[] responseCookieContainer) 
    {
    	String message = null;
    	try{
    		
    		URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional, default is GET
			con.setRequestMethod("GET");

			// add request header
			con.setRequestProperty("Host", "" +":4444");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Referer", "http://"+ "" +":4444/login.html");
			//con.setRequestProperty("Authorization", Authorization);
			con.setRequestProperty(StringUtils.substringBetween( responseCookieContainer[0], "=", "%"), StringUtils.substringAfter(responseCookieContainer[0], "%3D"));
			con.setRequestProperty("Cookie", responseCookieContainer[1] + "; " + responseCookieContainer[0] );
	
	        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        String  inputLine = "";
	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();
	        //System.out.println("+*+" + response + "+*+");
	        inputLine = response.toString();
			json = new JSONObject(inputLine);
			return json.get("msg").toString();  
    	}
        catch (Exception e)
        {
        	String errorr = e.getMessage();
        	System.out.println(errorr);
            
        	System.out.println("bug RemoveInputHelper, url " + url );
        }  
        return "Error has occured - check the setup";
    }
}