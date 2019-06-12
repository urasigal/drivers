package com.zixi.tools;

import java.io.File;
import java.io.IOException;

public class FileManagerTools 
{
	public static File createFile( String fileName)
    {	
    	try 
    	{
	      File file = new File(fileName);
	      if (file.createNewFile())
	      {
	        System.out.println("File is created!");
	        return file;
	      }
	      else
	      {
	        System.out.println("File already exists.");
	        return file;
	      }
    	} 
    	catch (IOException e) 
    	{
	      e.printStackTrace();
	      return null;
    	}
    }
}
