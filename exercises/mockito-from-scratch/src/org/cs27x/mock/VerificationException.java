package org.cs27x.mock;

/**
 * An exception that is thrown when a call to Mock.verify(...) does
 * not meet expectations.
 * 
 * @author jules
 *
 */
public class VerificationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final Object target_;
	private final MethodInvocationSignature signature_;

	public VerificationException(Object target,
			MethodInvocationSignature signature) {
		super();
		target_ = target;
		signature_ = signature;
	}

	@Override
	public String getMessage() {
		return "Expected a call to " + target_ + " matching the signature: "
				+ signature_.toString() + " but no matching calls were made.";
	}

	public Object getTarget() {
		return target_;
	}

	public MethodInvocationSignature getSignature() {
		return signature_;
	}

}
