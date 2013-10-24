package org.cs27x.adv.java.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import org.cs27x.adv.java.InvocationProcessor;
import org.cs27x.adv.java.PreProcessorWrapper;
import org.cs27x.adv.java.directives.Echo;
import org.cs27x.adv.java.directives.TimeMe;
import org.cs27x.adv.java.processors.EchoProcessor;
import org.cs27x.adv.java.processors.TimeMeProcessor;
import org.cs27x.asgn.proc.AsgnStep;
import org.junit.Test;

@AsgnStep("5")
/**
 * Create new test methods, annotated with @Test, that
 * checks to ensure that your InvocationProcssorFactory,
 * new InvocationProcessors, and associated annotations 
 * work correctly. 
 * 
 */
public class PreProcessorWrapperTest {

	public static class TestObj {

		@TestDirective
		@Echo("foo()")
		public void foo() {
		}

		
		@Echo("bar()")
		@TimeMe
		public void bar() {
			try{
				Thread.sleep(10);
			}catch(Exception e){}
		}
	}

	@Test
	public void testInvocationProcessorExtraction() throws Exception {
		TestObj obj = new TestObj();

		Method foo = TestObj.class.getMethod("foo", new Class[0]);
		Method bar = TestObj.class.getMethod("bar", new Class[0]);

		PreProcessorWrapper wrapper = new PreProcessorWrapper(obj);
		List<InvocationProcessor<?>> procs = wrapper.getProcessors(foo);
		assertEquals(1, procs.size());

		InvocationProcessor<?> proc = procs.get(0);
		assertEquals(EchoProcessor.class, proc.getClass());
		assertEquals("foo()", ((EchoProcessor) proc).getEchoMsg());

		procs = wrapper.getProcessors(bar);
		assertEquals(2, procs.size());

		proc = procs.get(0);
		assertEquals(EchoProcessor.class, proc.getClass());
		assertEquals(TimeMeProcessor.class, procs.get(1).getClass());
		assertEquals("bar()", ((EchoProcessor) proc).getEchoMsg());
	}

	@Test
	public void testInvocationProcessorExecution() throws Exception {
		SimpleFactory factory = new SimpleFactory();

		TestObj obj = new TestObj();
		TestObj wrapped = PreProcessorWrapper.wrap(obj, factory);

		wrapped.foo();

		TestProcessor proc = factory.getTestProcessor();
		assertTrue(proc.wasPreprocessCalled());
		assertTrue(proc.wasPostProcessCalled());
		proc.reset();

		wrapped.bar();
		assertTrue(!proc.wasPreprocessCalled());
		assertTrue(!proc.wasPostProcessCalled());

		wrapped = PreProcessorWrapper.wrap(obj);
		wrapped.foo();
		wrapped.bar();
	}

}
