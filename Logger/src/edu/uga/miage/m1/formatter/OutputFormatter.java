package edu.uga.miage.m1.formatter;

import edu.uga.miage.m1.tracer.OutputDestination;
 
public abstract class OutputFormatter 
{
	public abstract void format(String text, Object message, OutputDestination outputDestination, String messageLevel, String logFilePath, String className);
}
