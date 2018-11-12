package edu.uga.miage.m1.formatter;

import static org.fusesource.jansi.Ansi.ansi; 
import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.fusesource.jansi.AnsiConsole;

import edu.uga.miage.m1.tracer.OutputDestination;

public class StringFormatter extends OutputFormatter 
{
	@Override
	public void format(String text, Object message, OutputDestination outputDestination, String messageLevel, String logFilePath, String className) 
	{
		// [<date>] <message>
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss:ms");
		LocalDateTime currentDate = LocalDateTime.now();
		System.setProperty("jansi.passthrough", "true");
		String msg = "["+dtf.format(currentDate)+"] ("+className+") "+text+" "+message.toString();
		
		switch(outputDestination) 
		{
			case Console:
				switch(messageLevel)
				{
					case "DEBUG":
						AnsiConsole.systemInstall();
						System.out.println(ansi().fg(GREEN).a(msg).reset());
						AnsiConsole.systemUninstall();
						break;
					case "INFO":
						AnsiConsole.systemInstall();
						System.out.println(ansi().fg(BLUE).a(msg).reset());
						AnsiConsole.systemUninstall();
						break;
					case "WARN":
						AnsiConsole.systemInstall();
						System.out.println(ansi().fg(MAGENTA).a(msg).reset());
						AnsiConsole.systemUninstall();
						break;
					case "ERROR":
						Throwable exception = (Throwable) message;
						System.err.print("["+dtf.format(currentDate)+"] ("+className+") "+text+" ");
						exception.printStackTrace();
						break;
				}
				break;
				
			case File:
				try
				{
					writeToLogFile(logFilePath,msg);
				} 
				catch(IOException | SecurityException e)
				{
					e.printStackTrace();
				}
				break;
		}
	}
	
	/*
	 * a method to create and write to a log File
	 */
	private void writeToLogFile(String logFilePath, String msg) throws IOException,SecurityException
	{
		Path file = Paths.get(logFilePath);
		/*
		 * instruction to write to a new file or append to it if it already exists
		 */
		Files.write(file, Arrays.asList(msg), StandardCharsets.UTF_8, Files.exists(file) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
	}

}
