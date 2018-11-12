package test.edu.uga.miage.m1;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.uga.miage.m1.formatter.Formatter;
import edu.uga.miage.m1.tracer.Gravity;
import edu.uga.miage.m1.tracer.OutputDestination;
import edu.uga.miage.m1.tracer.StackTracer;
import edu.uga.miage.m1.tracer.TracerFactory;

public class LoggerTest 
{
	StackTracer stackTracer;
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss:ms");
	LocalDateTime currentDate = LocalDateTime.now();
	
	@Before
	public void setUp()
	{
		TracerFactory tracerFactory = TracerFactory.getStackTracerInstance();
		stackTracer = tracerFactory.getTracer();
		stackTracer.setGravity(Gravity.DEBUG);
		stackTracer.setFormatter(Formatter.stringFormatter);
		stackTracer.setOutputDest(OutputDestination.Console);
	}
	
	@Test
	public void testGravity()
	{
		assertEquals(Gravity.DEBUG, stackTracer.getGravity());
	}
	
	@Test
	public void testFormatter()
	{
		assertEquals(Formatter.stringFormatter, stackTracer.getFormatter());
	}
	
	@Test
	public void testOutputDestination()
	{
		assertEquals(OutputDestination.Console, stackTracer.getOutputDest());
	}
	
	@Test
	public void testLogFileCreation()
	{
		stackTracer.setOutputDest(OutputDestination.File);
		stackTracer.setLogFilePath("src//resource//logFile1.txt");
		stackTracer.info("Test Log File Creation");
		File file = new File(stackTracer.getLogFilePath());
		assertTrue(file.exists());
	}
	
	@Test
	public void testLogFileContent()
	{
		stackTracer.setOutputDest(OutputDestination.File);
		stackTracer.setLogFilePath("C://logFile2.txt");
		stackTracer.info("Test Log File Content");
		try
		{
			//get the new and the last line in the log file
			@SuppressWarnings("resource")
			BufferedReader input = new BufferedReader(new FileReader(stackTracer.getLogFilePath()));
		    String last="", line="";

		    while ((line = input.readLine()) != null) { last = line;}
		    
		    assertEquals("["+dtf.format(currentDate)+"] ("+LoggerTest.class.getSimpleName()+") {Info log} Test Log File Content",last);
		} 
		catch (IOException e) 
		{
			Assert.fail("Exception : "+e);
		} 
	}
	
	@Test
	public void testLogMessages() 
	{
		ByteArrayOutputStream  byteArrayOutputStream = new ByteArrayOutputStream(100);
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		PrintStream old = System.out;
		System.setOut(printStream);
		
		stackTracer.debug("Sum result :"+ calculateSum(5,5));
		stackTracer.info("Début du traitement ");
		stackTracer.warn("Fin du traitement");
		
		printStream.flush();
		System.setOut(old);
		
		assertEquals(ansi().fg(GREEN).a("["+dtf.format(currentDate)+"] ("+LoggerTest.class.getSimpleName()+") {Debug log} Sum result :10").reset()+System.lineSeparator()
		+ansi().fg(BLUE).a("["+dtf.format(currentDate)+"] ("+LoggerTest.class.getSimpleName()+") {Info log} Début du traitement ").reset()+System.lineSeparator()
		+ansi().fg(MAGENTA).a("["+dtf.format(currentDate)+"] ("+LoggerTest.class.getSimpleName()+") {Warn log} Fin du traitement").reset()+System.lineSeparator()
		, byteArrayOutputStream.toString());
	}
	
	@Test
	public void testErrorLogMessage()
	{
		ByteArrayOutputStream  byteArrayOutputStream = new ByteArrayOutputStream(100);
		PrintStream printStream = new PrintStream(byteArrayOutputStream);
		PrintStream old = System.err;
		System.setErr(printStream);

		double result = 0;
		try {
			result = 5 / 0;
			stackTracer.debug("Division result : " + result);
		} 
		catch (ArithmeticException e) 
		{
			stackTracer.error(e);
		}
		
		printStream.flush();
		System.setErr(old);
		
		assertThat(	byteArrayOutputStream.toString().substring(0,185), CoreMatchers.containsString("["+dtf.format(currentDate)+"] ("+LoggerTest.class.getSimpleName()+") {Error log} java.lang.ArithmeticException: / by zero"));
	}
	
	
	public int calculateSum(int x, int y) { return x+y;}
}
