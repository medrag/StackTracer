package edu.uga.miage.m1.tracer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import edu.uga.miage.m1.formatter.Formatter;
import edu.uga.miage.m1.formatter.StringFormatter;

public class StackTracer
{
	//Default Gravity is set to DEBUG
	private Gravity gravity;
	private OutputDestination outputDest;
	private Formatter formatter;
	private String logFilePath="";
	private StringFormatter stringFormatter = new StringFormatter();
	private String className;

	
	public Gravity getGravity() {
		return gravity; 
	}

	public void setGravity(Gravity gravity) { 
		this.gravity = gravity;
	}

	public OutputDestination getOutputDest() {
		return outputDest;
	}

	public void setOutputDest(OutputDestination outputDest) {
		this.outputDest = outputDest;
	}
	
	public Formatter getFormatter() {
		return formatter;
	}

	public void setFormatter(Formatter formatter) {
		this.formatter = formatter;
	}

	public String getLogFilePath() {
		return logFilePath;
	}

	public void setLogFilePath(String logFilePath) {
		this.logFilePath = logFilePath;
	}

	/*
	 * Default constructor used for looking for config file
	 */
	public StackTracer(String className)
	{
		this.className = className;
		finder("src/resource");
	}
	
	/*
	 * Debug Method
	 */
	public void debug(Object message)
	{
		if(getGravity() == Gravity.DEBUG)
		{
			String prefix = "{Debug log}";
			String msgLevel = "DEBUG";
			switch(formatter)
			{
				case stringFormatter:
					stringFormatter.format(prefix,message,getOutputDest(),msgLevel,getLogFilePath(),className);
					break;
			}
		}
	}
	
	/*
	 * Info Method
	 */
	public void info(Object message)
	{
		if(getGravity() == Gravity.INFO || getGravity() == Gravity.DEBUG)
		{
			String prefix = "{Info log}";
			String msgLevel = "INFO";
			switch(formatter)
			{
				case stringFormatter:
					stringFormatter.format(prefix,message,getOutputDest(),msgLevel,getLogFilePath(),className);
					break;
			}
		}
	}

	/*
	 * Warning method
	 */
	public void warn(Object message) 
	{
		if(getGravity() != Gravity.ERROR)
		{
			String prefix = "{Warn log}";
			String msgLevel = "WARN";
			switch(formatter)
			{
				case stringFormatter:
					stringFormatter.format(prefix,message,getOutputDest(),msgLevel,getLogFilePath(),className);
					break;
			}
		}
	}
	
	/*
	 * Error method
	 */
	public void error(Throwable message)
	{
			String prefix = "{Error log}";
			String msgLevel = "ERROR";
			switch(formatter)
			{
				case stringFormatter:
					stringFormatter.format(prefix,message,getOutputDest(),msgLevel,getLogFilePath(),className);
					break;
			}
	}
	
	/*
	 * a private method to check if a config file exist 
	 * and set the stackTracer config
	 */
	private void fileConfig(File logFile)
	{
		Properties prop = new Properties();
		InputStream input = null;
		
		try
		{
			input = new FileInputStream(logFile);
    		 
			prop.load(input);
			
			/*
			 * Setting gravity from config file
			 */
			switch(prop.getProperty("gravity").toUpperCase())
			{
				case "DEBUG":
					setGravity(Gravity.DEBUG);
					break;
				case "INFO":
					setGravity(Gravity.INFO);
					break;
				case "WARN":
					setGravity(Gravity.WARN);
					break;
				case "ERROR":
					setGravity(Gravity.ERROR);
					break;
			}
			
			/*
			 * Setting OutputDestination from config file
			 */
			switch(prop.getProperty("outputDestination").toUpperCase())
			{
				case "CONSOLE":
					setOutputDest(OutputDestination.Console);
					break;
				case "FILE":
					setOutputDest(OutputDestination.File);
					/*
					 * getlogFilePath from Config file
					 */
					setLogFilePath(prop.getProperty("path"));
					break;
			}
			
			/*
			 * Setting formatter from config file
			 */
			switch(prop.getProperty("formatter").toUpperCase())
			{
				case "STRINGFORMATTER":
					setFormatter(Formatter.stringFormatter);
					break;
			}
			
		} catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	 public void finder(String dirName)
	 {
		 File dir = new File(dirName);

	     File[] propertiesFile = dir.listFiles(new FilenameFilter() 
	     {
	             public boolean accept(File dir, String filename){ return filename.endsWith(".properties"); }
	     });
	     
	     for(int i=0;i<propertiesFile.length;i++)
	     {
	    	 Properties prop = new Properties();
	    	 InputStream input = null;
	    	 
	    	 try 
	    	 {
	    		 input = new FileInputStream(propertiesFile[i]);
	    		 
	    		 prop.load(input);
	    		 
		    	 if(prop.getProperty("gravity") != null || prop.getProperty("outputDestination") != null || prop.getProperty("formatter") != null)
		    	 {
		    		 fileConfig(propertiesFile[i]);
		    		 break;
		    	 }
	    	 } catch(IOException e)
	    	 {
	    		 e.printStackTrace();
	    	 }
	     }
	 }
}
