package com.zixi.zapi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;



import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;
public class ZapiConnection {

	// Zypher base URL string.
	public static final String zephyrBaseUrl = "https://prod-api.zephyr4jiracloud.com/connect";
	public static void main(String[] args) throws URISyntaxException, IllegalStateException, IOException {
		// zephyr accessKey , we can get from Addons >> zapi section
		String accessKey = "ZjhjNDRkZGItY2YwZi0zMzA0LWI0NGQtODdlMTY3NmQ4NThhIHVyaSBVU0VSX0RFRkFVTFRfTkFNRQ";
		// zephyr secretKey , we can get from Addons >> zapi section
		String secretKey = "nKw_iEzONL_AgPhMRxsrDysWj98wp0ulvApTYdcFG6U";
		// Jira UserName
		String userName = "uri@zixi.com";
		//String queryParams = zephyrBaseUrl + 
		ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName).build();
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		
		// API to which the JWT token has to be generated
		String createCycleUri = zephyrBaseUrl + "/public/rest/api/1.0/cycle?expand=&clonedCycleId=";
		
		URI uri = new URI(createCycleUri);
		int expirationInSec = 360;
		String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);
		
		// Print the URL and JWT token to be used for making the REST call
		System.out.println("FINAL API : " +uri.toString());
		System.out.println("JWT Token : " +jwt);	
	}
	
	public static String getJwtPost(String zypherUserName, String zapiAccesskey, String zapiSecretkey, String queryString) throws URISyntaxException
	{
		// zephyr accessKey , we can get from Addons >> zapi section
		//String accessKey = "ZjhjNDRkZGItY2YwZi0zMzA0LWI0NGQtODdlMTY3NmQ4NThhIHVyaSBVU0VSX0RFRkFVTFRfTkFNRQ";
		// zephyr secretKey , we can get from Addons >> zapi section
		//String secretKey = "nKw_iEzONL_AgPhMRxsrDysWj98wp0ulvApTYdcFG6U";
		ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, zapiAccesskey, zapiSecretkey, zypherUserName).build();
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		// API to which the JWT token has to be generated
		String createCycleUri = zephyrBaseUrl + queryString;

		URI uri = new URI(createCycleUri);
		int expirationInSec = 360;
		String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);
		return jwt;
	}
	
	public static String getJwtGet(String zypherUserName, String zapiAccesskey, String zapiSecretkey, String queryString) throws URISyntaxException
	{
		// zephyr accessKey , we can get from Addons >> zapi section
		//String accessKey = "ZjhjNDRkZGItY2YwZi0zMzA0LWI0NGQtODdlMTY3NmQ4NThhIHVyaSBVU0VSX0RFRkFVTFRfTkFNRQ";
		// zephyr secretKey , we can get from Addons >> zapi section
		//String secretKey = "nKw_iEzONL_AgPhMRxsrDysWj98wp0ulvApTYdcFG6U";
		ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, zapiAccesskey, zapiSecretkey, zypherUserName).build();
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		// API to which the JWT token has to be generated
		String createCycleUri = zephyrBaseUrl + queryString;

		URI uri = new URI(createCycleUri);
		int expirationInSec = 360;
		String jwt = jwtGenerator.generateJWT("GET", uri, expirationInSec);
		return jwt;
	}
	
	public static String getJwtPut(String zypherUserName, String zapiAccesskey, String zapiSecretkey, String queryString) throws URISyntaxException
	{
		// zephyr accessKey , we can get from Addons >> zapi section
		//String accessKey = "ZjhjNDRkZGItY2YwZi0zMzA0LWI0NGQtODdlMTY3NmQ4NThhIHVyaSBVU0VSX0RFRkFVTFRfTkFNRQ";
		// zephyr secretKey , we can get from Addons >> zapi section
		//String secretKey = "nKw_iEzONL_AgPhMRxsrDysWj98wp0ulvApTYdcFG6U";
		ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, zapiAccesskey, zapiSecretkey, zypherUserName).build();
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		// API to which the JWT token has to be generated
		String createCycleUri = zephyrBaseUrl + queryString;

		URI uri = new URI(createCycleUri);
		int expirationInSec = 360;
		String jwt = jwtGenerator.generateJWT("PUT", uri, expirationInSec);
		return jwt;
	}
	
	public static String getJwtDelete(String zypherUserName, String zapiAccesskey, String zapiSecretkey, String queryString) throws URISyntaxException
	{
		// zephyr accessKey , we can get from Addons >> zapi section
		//String accessKey = "ZjhjNDRkZGItY2YwZi0zMzA0LWI0NGQtODdlMTY3NmQ4NThhIHVyaSBVU0VSX0RFRkFVTFRfTkFNRQ";
		// zephyr secretKey , we can get from Addons >> zapi section
		//String secretKey = "nKw_iEzONL_AgPhMRxsrDysWj98wp0ulvApTYdcFG6U";
		ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, zapiAccesskey, zapiSecretkey, zypherUserName).build();
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		// API to which the JWT token has to be generated
		String createCycleUri = zephyrBaseUrl + queryString;

		URI uri = new URI(createCycleUri);
		int expirationInSec = 360;
		String jwt = jwtGenerator.generateJWT("DELETE", uri, expirationInSec);
		return jwt;
	}
}
