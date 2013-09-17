package org.cs27x.mock;

import java.lang.reflect.Method;

/**
 * A class that stores information about a method
 * invocation.
 * 
 * @author jules
 *
 */
public class MethodInvocation {
	
	private final Method method_;
	private final Object target_;
	private final Object[] args_;

	public MethodInvocation(Method method, Object target, Object[] args) {
		super();
		method_ = method;
		target_ = target;
		args_ = args;
	}

	public Method getMethod() {
		return method_;
	}

	public Object getTarget() {
		return target_;
	}

	public Object[] getArgs() {
		return args_;
	}

}