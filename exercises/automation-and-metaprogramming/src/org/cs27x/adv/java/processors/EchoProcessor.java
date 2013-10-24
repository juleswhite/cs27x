package org.cs27x.adv.java.processors;

import org.cs27x.adv.java.Invocation;
import org.cs27x.adv.java.InvocationProcessor;
import org.cs27x.adv.java.directives.Echo;

public class EchoProcessor implements InvocationProcessor<Echo> {

	private String echoMsg_;
	
	@Override
	public void preProcess(Invocation i) {
		System.out.println(">> "+echoMsg_);
	}

	@Override
	public void postProcess(Invocation i) {
		System.out.println("<< "+echoMsg_);
	}

	@Override
	public void setAnnotation(Echo anno) {
		echoMsg_ = anno.value();
	}

	public String getEchoMsg() {
		return echoMsg_;
	}

	public void setEchoMsg(String echoMsg) {
		echoMsg_ = echoMsg;
	}

}
