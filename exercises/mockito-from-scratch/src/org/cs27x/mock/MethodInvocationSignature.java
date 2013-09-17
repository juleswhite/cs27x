package org.cs27x.mock;

import java.lang.reflect.Method;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

/**
 * A class representing a method invocation signature, which includes:
 * 
 * 1. A Java reflection method object that is expected to be equal() to
 *    the invoked method
 * 2. A list of ArgMatchers that are called to verify that each argument
 *    passed to the invoked method matches the signature. 
 * 
 * 
 * @author jules
 *
 */
public class MethodInvocationSignature {
	private final Method method_;
	private final List<ArgMatcher> matchers_;

	public MethodInvocationSignature(Method method,
			List<ArgMatcher> matchers) {
		super();
		method_ = method;
		matchers_ = ImmutableList.copyOf(matchers);
	}

	public Method getMethod() {
		return method_;
	}

	public List<ArgMatcher> getMatchers() {
		return matchers_;
	}
	
	public boolean matches(MethodInvocation mi){
		return matches(mi.getTarget(), mi.getMethod(), mi.getArgs());
	}

	public boolean matches(final Object target, final Method m,
			final Object[] args) {
		boolean match = false;

		if (method_.equals(m)) {
			match = true;

			for (int i = 0; i < args.length; i++) {
				if (!matchers_.get(i).matches(args[i])) {
					match = false;
					break;
				}
			}
		}

		return match;
	}

	public String toString(){
		return method_.getName()+"(" + Joiner.on(",").join(matchers_)+")";
	}
}