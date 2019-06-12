package com.zixi.threads;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Request;

import com.zixi.nio.WriteerToFile;

public class JettyHandlerZixiThread extends Thread{
	private HttpServletRequest request;
	private Request baseRequest;
	public InputStream stream = null;
	public boolean appendFlag;
	public Object writeLock = null;
	
	public JettyHandlerZixiThread(Object writeLock) {
		this.writeLock = writeLock;
	}

	public void setHttpServletRequest(HttpServletRequest request)
	{
		this.request = request;
	}
	
	public String path;
	
	public void setRequest(Request baseRequest)
	{
		this.baseRequest = baseRequest;
	}
	
	public void run() 
	{
		int read = 0;
 		int nRead; 
        byte[] data = new byte[10_024_000];
        synchronized(writeLock) {
        ByteBuffer byteNioDirectBuffer =  ByteBuffer.allocateDirect(10_024_000);
        Path pathToFile = Paths.get(path);
	        try (FileChannel fileCnanelToWrite = new FileOutputStream(pathToFile.toString()).getChannel()){ 
	        	if(appendFlag)
	        	{
	        		while ((nRead = stream.read(data, 0, data.length)) != -1 )
	        		{
	        			read += nRead;
	        			byteNioDirectBuffer.put(data, 0, nRead);
	        			byteNioDirectBuffer.flip();
	        			fileCnanelToWrite.write(byteNioDirectBuffer);
	        			byteNioDirectBuffer.clear();
				   }
	        	}else
	        	{
	        		PrintWriter pw = new PrintWriter(pathToFile.toString());
	        		pw.close();
	        		
	        		try (BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(pathToFile, CREATE ))) 
	        		{
		    			while ((nRead = stream.read(data, 0, data.length)) != -1) 
		        		{
					      out.write(data, 0, nRead);
					      out.flush();
					      read += nRead;
		    			}
	  			    } catch (IOException x) {
	  			      System.err.println(x);
	  			      System.out.println("Error read bytes is " +  read + " to file " + path );
	  			    }
	        	}
				}catch (IOException e) {
				// TODO Auto-generated catch block
					System.out.println("@JettyHandlerZixiThread Error is " + e.getMessage());
					 System.out.println("Error ead bytes is " +  read  + " to file " + path);
				e.printStackTrace();
			}finally {
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Read bytes is " +  read  + " to file " + path);
			}
        }
	}
}
