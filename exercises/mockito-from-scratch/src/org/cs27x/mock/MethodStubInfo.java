package org.cs27x.mock;

/**
 * A class representing a stubbed method invocation. If a call is
 * made that matches the MethodInvocationSignature of the stub, the
 * associated ResultGenerator should be called to determine the return
 * value of the call.
 * 
 * @author jules
 *
 */
public class MethodStubInfo {
	private final MethodInvocationSignature invocationSignature_;
	private final ResultGenerator result_;

	public MethodStubInfo(MethodInvocationSignature signature,
			ResultGenerator result) {
		super();
		invocationSignature_ = signature;
		result_ = result;
	}

	public ResultGenerator getResult() {
		return result_;
	}

	public MethodInvocationSignature getSignature() {
		return invocationSignature_;
	}
}
