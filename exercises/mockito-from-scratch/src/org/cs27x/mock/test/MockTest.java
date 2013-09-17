package org.cs27x.mock.test;

import static org.junit.Assert.*;

import org.cs27x.mock.VerificationException;
import org.junit.Test;

import static org.cs27x.mock.Mock.*;

public class MockTest {

	public interface TestMeInterface {
		public void foo();
	}
	
	public class TestMe {
		public boolean foo(){
			return false;
		}
		
		public String getName(){
			return "123";
		}
		
		public int getCount(){
			return -1;
		}
		
		public String echo(String msg){
			return msg;
		}
		
		public Object giveBack(Object o){
			return o;
		}
	}
	
	@Test
	public void testMockInterface(){
		TestMeInterface obj = mock(TestMeInterface.class);
		obj.foo();
	}
	
	@Test
	public void testMockClass(){
		TestMe obj = mock(TestMe.class);
		obj.foo();
	}
	
	@Test
	public void testStubIntReturn() {
		TestMe test = mock(TestMe.class);
		
		when(test.getCount()).thenReturn(1);
		
		assertEquals(1, test.getCount());
	}
	
	@Test
	public void testReturnFromUnstubbedPrimitiveMethods() {
		TestMe test = mock(TestMe.class);
		
		assertEquals(0, test.getCount());
		assertEquals(false, test.foo());
	}
	
	@Test
	public void testStubBooleanReturn() {
		TestMe test = mock(TestMe.class);
		
		when(test.foo()).thenReturn(true);
		
		assertTrue(test.foo());
	}
	
	@Test
	public void testStubStringReturn() {
		TestMe test = mock(TestMe.class);
		
		when(test.getName()).thenReturn("456");
		
		assertEquals("456", test.getName());
	}
	
	@Test
	public void testStubStringReturnWithEqMatcher() {
		TestMe test = mock(TestMe.class);
		
		when(test.echo("1")).thenReturn("2");
		
		assertEquals("2", test.echo("1"));
		assertNull(test.echo("3"));
		assertEquals("2", test.echo("1"));
	}
	
	@Test
	public void testStubObjectReturnWithAnyMatcher(){
		TestMe test = mock(TestMe.class);
		
		when(test.giveBack(any(String.class))).thenReturn("1");
		when(test.giveBack(any(Boolean.class))).thenReturn(true);
		
		assertEquals("1",test.giveBack("asdf"));
		assertEquals(true,test.giveBack(false));
		assertNull(test.giveBack(5));
	}

	@Test
	public void testVerify() {
		TestMe test = mock(TestMe.class);
		
		test.getCount();
		test.hashCode();
		
		verify(test).getCount();
		verify(test).hashCode();
		
		try{
			verify(test).foo();
			fail("Verify failed to throw an exception when an unmade method call was verified.");
		}catch(VerificationException ve){
			assertEquals("foo",ve.getSignature().getMethod().getName());
		}
	}
}
