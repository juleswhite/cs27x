package org.cs27x.adv.java;

import java.lang.reflect.Method;

/**
 * A data structure representing a method invocation on
 * an object. Changes to the Invocation object will alter
 * how the method call is performed and the return value
 * that is provided back to the caller. Invocation objects
 * are created by the PreProcessorWrapper and passed to 
 * InvocationProcessors in order to apply pre/post execution
 * logic.
 * 
 * @author jules
 *
 */
public class Invocation {

	private boolean canceled_;
	private Object[] args_;
	private Object target_;
	private Object result_;
	private Method targetMethod_;

	public Invocation(Object target, Method targetMethod, Object[] args) {
		super();
		target_ = target;
		targetMethod_ = targetMethod;
		args_ = args;
	}

	public boolean isCanceled() {
		return canceled_;
	}

	public void setCanceled(boolean canceled) {
		canceled_ = canceled;
	}

	public Object[] getArgs() {
		return args_;
	}

	public void setArgs(Object[] args) {
		args_ = args;
	}

	public Method getTargetMethod() {
		return targetMethod_;
	}

	public void setTargetMethod(Method targetMethod) {
		targetMethod_ = targetMethod;
	}

	public Object getTarget() {
		return target_;
	}

	public void setTarget(Object target) {
		target_ = target;
	}

	public Object getResult() {
		return result_;
	}

	public void setResult(Object result) {
		result_ = result;
	}

	public void exec() throws Throwable {
		if (!isCanceled()) {
			result_ = targetMethod_.invoke(target_, args_);
		}
	}
}
