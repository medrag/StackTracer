package test.edu.uga.miage.m1;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;

import edu.uga.miage.m1.tracer.StackTracer;
import edu.uga.miage.m1.tracer.TracerFactory;

public class SingletonTestCase
{
	StackTracer stackTracer;
	
	@Before
	public void setUp()
	{
		TracerFactory tracerFactory = TracerFactory.getStackTracerInstance();
		stackTracer = tracerFactory.getTracer();
	}
	
	@Test
	public void testSingletonCreation()
	{
		assertSame(stackTracer, TracerFactory.getStackTracerInstance().getTracer());
	}
}
