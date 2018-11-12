package edu.uga.miage.m1.tracer;

import edu.uga.miage.m1.formatter.Formatter;

public class Client 
{
	private static TracerFactory tracerFactory = TracerFactory.getStackTracerInstance();
	private static StackTracer stackTracer = tracerFactory.getTracer();

	public static void main(String[] args) 
	{
		/*
		 * StackTracer example using properties file 
		 * log messages are written in Console 
		 */
		stackTracer.debug("Sum result : " + calculateSum(5, 5));
		stackTracer.info("Début du traitement ");
		stackTracer.warn("Fin du traitement");

		double result = 0;
		try {
			result = 5 / 0;
			stackTracer.debug("Division result : " + result);
		} 
		catch (ArithmeticException e) {
			stackTracer.error(e);
		}
		
		
		/*
		 * StackTracer example using programmatic config
		 * log messages are written in log file
		 */
		stackTracer.setGravity(Gravity.DEBUG);
		stackTracer.setOutputDest(OutputDestination.File);
		stackTracer.setLogFilePath("src//resource//logFile1.txt");
		stackTracer.setFormatter(Formatter.stringFormatter);
		
		stackTracer.debug("Sum result : " + calculateSum(5, 5));
		stackTracer.info("Début du traitement ");
		stackTracer.warn("Fin du traitement");

		double result2 = 0;
		try {
			result2 = 5 / 0;
			stackTracer.debug("Division result : " + result2);
		} 
		catch (ArithmeticException e) {
			stackTracer.error(e);
		}
	}

	public static int calculateSum(int x, int y) {return x + y;}
}
