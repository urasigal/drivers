package com.zixi.drivers.drivers;

import  static com.zixi.globals.Macros.*;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import com.zixi.awsdrivers.Opereation;
import com.zixi.drivers.tools.DriverReslut;
import com.zixi.tools.BroadcasterLoggableApiWorker;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.Random;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class AwsConnectorDriver extends BroadcasterLoggableApiWorker
{
	public DriverReslut uploadHlsToS3(String userName, String userPass, String login_ip, String uiport,
			String output_name, String matrix, String stream, String type, String url, String cleanup, String region,
			String  encap, String no_tls, String upload_type ) throws Exception {
		
		String accessKey = FeederPostKeyDriver.getStringFromUrl("acc_key");
		String secretKey =  FeederPostKeyDriver.getStringFromUrl("sec_key");
		
		responseCookieContainer = broadcasterInitialSecuredLogin.sendGet( "http://" + login_ip + ":" + uiport + "/login.htm", userName, userPass, login_ip, uiport);

		driverReslut = new DriverReslut(apiworker.sendGet("http://" + login_ip + ":" + uiport + "/zixi/add_output.json?name=" + output_name + "&matrix=" + matrix + 
		"&stream=" + stream + "&type=" +  type + "&url=" +  url + "&cleanup=" + cleanup + "&region=" + region + "&encap=" + encap + "&no_tls="  + no_tls +
		"&upload_type=" + upload_type + "&access=" + accessKey + "&secret=" + secretKey, "", PUSHINMODE, responseCookieContainer, login_ip, this, uiport));
		
		Thread.sleep(60_000);
		return driverReslut;
	}
	
	public DriverReslut performOperationOnAwsS3(Opereation operation, AwsConnectorDriver.OperationContainer operationParams) throws Exception
	{
		String accessKey			= FeederPostKeyDriver.getStringFromUrl("acc_key");
		String secretKey			=  FeederPostKeyDriver.getStringFromUrl("sec_key");
		Properties props			= System.getProperties();
		props.setProperty("aws.accessKeyId", accessKey);
		props.setProperty("aws.secretKey", secretKey);
        AmazonS3 s3				= new AmazonS3Client();
        Region usEast1			= Region.getRegion(Regions.US_EAST_2);
        s3.setRegion(usEast1);

        
        ObjectListing objectList = null;
        List<S3ObjectSummary> objectSummeryList = null;
        switch (operation) {
		        case DELETE_FOLDER_FROM_S3:	objectList = s3.listObjects( operationParams.params.get("bucketName"), operationParams.params.get("prefix"));
															            objectSummeryList =  objectList.getObjectSummaries();
															            String[] keysList = new String[ objectSummeryList.size() ];
															            int count = 0;
															            for( S3ObjectSummary summery : objectSummeryList ) {
															                keysList[count++] = summery.getKey();
															            }
															            DeleteObjectsRequest deleteObjectsRequest = 
															            new DeleteObjectsRequest( operationParams.params.get("bucketName") ).withKeys( keysList );
															            s3.deleteObjects(deleteObjectsRequest);
															            return new DriverReslut("The object assumed to be deleted from AWS s3 bucket"); 
		        
		        case LIST_S3_FOLDER: 					objectList =   s3.listObjects( operationParams.params.get("bucketName"), operationParams.params.get("prefix"));
																        objectSummeryList =  objectList.getObjectSummaries();
															            int numberOfUploadedFilesBefore =  objectSummeryList.size();
															            int waiting  = Integer.parseInt( operationParams.params.get("test_duration"));
															            Thread.sleep( waiting * 1000 );
															            objectList =   s3.listObjects( operationParams.params.get("bucketName"), operationParams.params.get("prefix"));
																        objectSummeryList =  objectList.getObjectSummaries();
															            int numberOfUploadedFilesAfter =  objectSummeryList.size();
															            int numOfUploadedFiles = numberOfUploadedFilesAfter - numberOfUploadedFilesBefore;
															            if(  waiting / ( Integer.parseInt( operationParams.params.get("file_duration")) ) <= numOfUploadedFiles )
															            	return new DriverReslut("The correct number of uploaded files was found");
															            else  return new DriverReslut("Number of uploaded files is " + numOfUploadedFiles + " but should be "
															            		+  (waiting / ( Integer.parseInt( operationParams.params.get("file_duration")) + " at least" )));
		        
		        default: 											 return new DriverReslut("Operation is not found");
	        }
       // return new DriverReslut("Operation is not found");
	}
	
	
	public static void main(String[] args) throws Exception  {
		AwsConnectorDriver connectorDriver  = new AwsConnectorDriver();
		HashMap<String, String> params = new HashMap();
		params.put("bucketName", "zixi");
		params.put("prefix", "test/hls_upload");
		connectorDriver.performOperationOnAwsS3(Opereation.valueOf("DELETE_FROM_S3"), 
				new AwsConnectorDriver.OperationContainer (params));
	}
	public static class OperationContainer{
		public HashMap<String, String> params = new HashMap();
		
		public OperationContainer( HashMap<String, String> params )
		{
			this.params = params;
		}
	} 
}
