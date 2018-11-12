package edu.uga.miage.m1.tracer;

public class TracerFactory 
{
	private static TracerFactory tracerFactory = null;
	private static StackTracer stackTracer_instance;
	private static String className;
	
	private TracerFactory() 
	{
		stackTracer_instance = new StackTracer(className);
	};

	public static synchronized TracerFactory getStackTracerInstance()
	{
		Throwable t = new Throwable();
		StackTraceElement[] elements = t.getStackTrace();
		
		className = getCallerClass(elements);
		
		if(tracerFactory == null)
		{
			tracerFactory = new TracerFactory();
		} 
		return tracerFactory;
	} 
	
	public StackTracer getTracer()
	{
		return stackTracer_instance;
	}
	
	//Method called to get the caller className
	public static String getCallerClass(StackTraceElement[] elements)
	{
		int lastOccurenceIndex = elements[1].getClassName().lastIndexOf(".");
		return elements[1].getClassName().substring(lastOccurenceIndex+1);
	}
	
}
