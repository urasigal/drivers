package com.zixi.nio;
import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;

public class WriteerToFile  {

	  public static void writeToFile(String path, byte data[]) {

	    Path p = Paths.get(path);

	    try (BufferedOutputStream out = new BufferedOutputStream(
	      Files.newOutputStream(p, CREATE, APPEND ))) {
	      out.write(data, 0, data.length);
	      out.flush();
	    } catch (IOException x) {
	      System.err.println(x);
	    }
	  }
	}
