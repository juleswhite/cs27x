package org.cs27x.mock;

/**
 * ArgMatchers can be used to check that an argument passed
 * to a method call matches expectations.
 * 
 * @author jules
 *
 */
public interface ArgMatcher {

	public boolean matches(Object val);
	
}
