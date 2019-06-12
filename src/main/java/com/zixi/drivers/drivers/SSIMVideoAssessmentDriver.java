package com.zixi.drivers.drivers;

import com.zixi.drivers.tools.SSIMTest;
import com.zixi.entities.TestParameters;
import com.zixi.tools.BroadcasterLoggableApiWorker;

import static java.nio.file.StandardOpenOption.CREATE;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.image.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

import javax.tools.FileObject;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.opencv.core.Size;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import com.emaraic.digitrecognition.*;


public class SSIMVideoAssessmentDriver  extends
BroadcasterLoggableApiWorker implements TestDriver  {

	private static final String SRC_FILE_PATH 		= "src/main/resources/to_write.ts";
	private static final String TESTED_FILE_PATH = "src/main/resources/to_write_dest.ts";
	//private Queue<ImageIdToPathBox> circularFifoQueue;
	private final HashMap<Integer, String> leakMap = new HashMap<>();
	private  ArrayList<Double> ssimResultList;
	private static final int QUEUE_SIZE = 1200;
	private Object monit;
	private boolean interuptFlag = true;
	double average = 0;
	
	static {
		try {
		String property = System.getProperty("java.library.path");
		StringTokenizer parser = new StringTokenizer(property, ";");
		while (parser.hasMoreTokens()) {
			System.out.println(parser.nextToken());
		    }
			File currentDirFile = new File(".");
			String helper = currentDirFile.getAbsolutePath();
			System.out.println("helper dir " +helper );
			String currentDir = helper.substring(0, helper.length() - currentDirFile.getCanonicalPath().length());//this line may need a try-catch block
			System.out.println("Dir " + currentDir );
			System.out.println("local " +currentDirFile.getCanonicalPath() );
			System.out.println("More dir " + System.getProperty("user.dir"));
			nu.pattern.OpenCV.loadShared(); 
			String nm = Core.NATIVE_LIBRARY_NAME;
			System.out.println("Native lib opencv so name is  -->> " +  nm );
			System.loadLibrary(nm);
			System.out.println(".so is loaded");
		}catch(Exception e)
		{
			System.out.println("Error is ****** " + e.getMessage());
		}
	}
	
	public SSIMVideoAssessmentDriver()
	{
		ssimResultList = new ArrayList<>();
		monit = new Object();
	}
	
	public double ssim_evaluation(int sourceStreamUdpPort, int testedStreamUdpPort, int fileRecordInterval, 
	int cropHight, int cropWidth) throws Exception
	{
		 //  Gets and stores source stream into a file.
		UdpServerThread udpServerThreadSource = new UdpServerThread(sourceStreamUdpPort, SRC_FILE_PATH); 
		udpServerThreadSource.start();
		
		 //  Gets and stores tested stream into a file.
		UdpServerThread udpServerThreadTested = new UdpServerThread(testedStreamUdpPort, TESTED_FILE_PATH); 
		udpServerThreadTested.start();
		
		Thread.sleep(fileRecordInterval);
		udpServerThreadSource.interrupt();
		udpServerThreadTested.interrupt(); 
		udpServerThreadSource.join();
		udpServerThreadTested.join();
		
		 // Gets stream from the local file, decodes stream to frames, 
		// builds data structure that matches derived number from marked frames to frame's location.
		 Thread source = new Thread(() -> {
			try { rotatedSourceFrames(QUEUE_SIZE, monit,  cropHight,  cropWidth );  
			} catch (Exception e) {
				return;
			}
		} );
		
		 Thread  ssimTesting = new Thread(() -> {
			try {
				estimateSSIM(source, monit,  cropHight,  cropWidth);
			} catch (Exception e) {
				return;
			}
		} );
		
		source.start();
		source.join();
		
		ssimTesting.start();
		//Thread.sleep(fileRecordInterval * 1000); 
		//ssimTesting.interrupt();
		ssimTesting.join();
		double summation = ssimResultList.parallelStream().reduce(0d, Double::sum);
		return summation / ssimResultList.size();
	}
	
	public void estimateSSIM(Thread dependantThreadToStop, Object mon,  int cropHight, int cropWidth) throws Exception
	{
			System.out.println("Allocate Mat obj");
		 	Mat matOrig = new Mat();
		 	System.out.println("Trying to capture video file");
	        VideoCapture capture = new VideoCapture(TESTED_FILE_PATH);
	        System.out.println(capture.getNativeObjAddr());
	        if( capture.isOpened()){
	            while (true){  
	                capture.read(matOrig);  
	                double h = capture.get(Videoio.CAP_PROP_FRAME_HEIGHT);
	                double w = capture.get(Videoio.CAP_PROP_FRAME_WIDTH);
	                
	                if( matOrig.empty()  || (! interuptFlag )) {
	                	System.out.println("SSIM stoopped ------------------------------------------------------");
	                	dependantThreadToStop.interrupt();
	                	dependantThreadToStop.join();
        				return;
	                }
	                Rect subRect = new Rect(90, 40, 150, 50);
	                Mat subFrame = new Mat(matOrig, subRect);
	                
	                Rect normRect = new Rect(0, 0, cropWidth, cropHight);
	                Mat normFrame = new Mat(matOrig, normRect);
	                matOrig.release();

	                synchronized(mon){
		                // code to write  image to disk
		                Imgcodecs.imwrite("src\\main\\resources\\out_frame.jpg", subFrame);
		                int  index = Application.getIntFromImage ( "src\\main\\resources\\out_frame.jpg");
		                if(index != -1)
		                {
		                	Imgcodecs.imwrite("src\\main\\resources\\out_frame_test.jpg", normFrame);
			                String origPath =  leakMap.get(index); 
			                if(origPath != null)
			                {
				                	System.out.println("Match found ");
				                	Double res = SSIMTest.getMSSIM(origPath, "src\\main\\resources\\out_frame_test.jpg" ).get();
				                	if(res != null)
				                	{
				                		ssimResultList.add(res);
					                	System.out.println("SSIM ---> " + res);
					                	System.out.println("Guest frame of source to test " + origPath);
					                	 Imgcodecs.imwrite("src\\main\\resources\\out_frame_test__"  + index + "__" + ".jpg", normFrame);
				                	}
				                	normFrame.release();
			                	}
			                }
	                }
	                if(Thread.interrupted())
        			{
	                	System.out.println("SSIM stoopped ------------------------------------------------------");
	                	dependantThreadToStop.interrupt();
	                	dependantThreadToStop.join();
        				return;
        			}
	            }
	        }
	}
	
	// rotationLimit - the queue size. The queue stores pairs of guessed number. 
	public void rotatedSourceFrames(int rotationLimit, Object mon,  int cropHight, int cropWidth) throws Exception {
		    int sourceSuffixToParh = 0; // this number will be added to the image path's string.
		    try {
		        Mat matOrig = new Mat();
		        VideoCapture capture = new VideoCapture(SRC_FILE_PATH);
		        System.out.println(capture.getNativeObjAddr());
		        if( capture.isOpened()){
		        while (true){   
		           synchronized(mon){
		                capture.read(matOrig);  
			                if( matOrig.empty() ) {
			                	//interuptFlag  = false;
			                	 System.out.println("rotation is stopped");
				            	 return ;
			                }
			                Rect subRect = new Rect(90, 40, 150, 50);
			                Mat subFrame = new Mat(matOrig, subRect);
			                
			                Rect normRect = new Rect(0, 0, cropWidth, cropHight);
			                Mat normFrame = new Mat(matOrig, normRect);
			                matOrig.release();
			                //  Write image with id number to disk.
			                Imgcodecs.imwrite("src\\main\\resources\\input_src .jpg", subFrame);
			                // Guess integer number from Image with numbers.
			                int frameId = Application.getIntFromImage( "src\\main\\resources\\input_src .jpg");
			                if(frameId != -1)
			                {
				                //  Write original image to disk.
				                Imgcodecs.imwrite("src\\main\\resources\\input_test" + sourceSuffixToParh + "_" + frameId + "_"+ ".jpg", normFrame);
				                leakMap.put(frameId, "src\\main\\resources\\input_test" + sourceSuffixToParh + "_" + frameId + "_"+ ".jpg");
				                sourceSuffixToParh++; 
				                normFrame.release(); 
			                }
		                }
		                // Rotate the written content - override existing 
		               if(sourceSuffixToParh >= rotationLimit) return;
		             
		               // Exit from thread.
		               if(Thread.interrupted())
		               {
		            	   System.out.println("rotation is stopped");
		            	   return ;
		               }
		            }
		        }else
		        	System.out.println("Not opened");
		    }catch(Exception ex) {
		    	System.out.println("Error is " + ex.getMessage());
		    }
		return ; 
	}
	
	//  Helper class - defines kind of container to store image id and its location.
	static public class ImageIdToPathBox {
		public final int id;
		public final String path;
		public ImageIdToPathBox(int id, String path) {
    	   this.id = id;
    	   this.path = path;
       }
		
		// Overriding equals() to compare two ImageIdToPathBox objects 
	    @Override
	    public boolean equals(Object o) { 
	        // If the object is compared with itself then return true   
	        if (o == this) { 
	            return true; 
	        } 
	  
	        /* Check if o is an instance of Complex or not 
	          "null instanceof [type]" also returns false */
	        if (!(o instanceof ImageIdToPathBox)) { 
	            return false; 
	        } 
	         
	        // typecast o to Complex so that we can compare data members  
	        ImageIdToPathBox c = (ImageIdToPathBox) o; 
	          
	        // Compare the data members and return accordingly  
	        return Integer.compare(id, c.id) == 0;
	    } 
    }
	
	public static class UdpServerThread extends Thread {
	    //protected DatagramSocket socket = null;
	    DatagramChannel 		udpChannel  = null;
	    protected boolean 	moreQuotes = true;
	    private int 					port;
	    private String 				pathToFile;  // Path to local file to write to.
	  
	    public UdpServerThread(int port, String pathToFile) throws IOException {
		    this("UdpServerThread");
		    this.port = port; 
		    this.pathToFile = pathToFile;
	    }
	 
	    public UdpServerThread(String name) throws IOException {
	        super(name);
	       // socket = new DatagramSocket(port);
	       // udpChannel =  DatagramChannel.open();
	        //udpChannel.socket().bind(new InetSocketAddress(port));
	    }
	    public void run() {
	    	ByteBuffer byteNioDirectBuffer =  ByteBuffer.allocateDirect(1500);
	    	Path pathToFile = Paths.get(this.pathToFile); 
	    	
	    	try ( 
	    			// Open file channel with mandatory closing.
	    			FileChannel fileCnanelToWrite = new FileOutputStream(pathToFile.toString(), true ).getChannel();
	    			// Define UDP socket. will be closed automatically.
	    			DatagramSocket socket = new DatagramSocket(port) )
	    	{ 
	    		
		    	System.out.println("File " + pathToFile + " opened ");
		    	byte[] buf = new byte[1500]; 
		    	DatagramPacket    packet = new DatagramPacket(buf, buf.length); 
		    	while (true) {
		                socket.receive(packet);
	        			byteNioDirectBuffer.put(packet.getData(), 0, packet.getLength()); 
	        			byteNioDirectBuffer.flip();
	        			fileCnanelToWrite.write(byteNioDirectBuffer);
	        		   //System.out.println(	"bytes written " + fileCnanelToWrite.write(byteNioDirectBuffer));
	        			byteNioDirectBuffer.clear();
	        			
		                if(Thread.interrupted())
	        			{
		                	//udpChannel.close();
		                	System.out.println("Should be stoopped ------------------------------------------------------");
	        				return;
	        			}
		        }
	        }catch (Exception e) {
	        		return;
    		}
	    }
	}
}