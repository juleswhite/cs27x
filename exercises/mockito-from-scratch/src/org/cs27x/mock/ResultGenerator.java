package org.cs27x.mock;

import java.lang.reflect.Method;

/**
 * An interface for classes that can generate a return value for a mock
 * object when a method invocation is made on it.
 * 
 * @author jules
 *
 */
public interface ResultGenerator {

	public Object generate(Object target, Method m, Object[] args);
	
}
