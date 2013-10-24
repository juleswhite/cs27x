package org.cs27x.adv.java.test;

import java.lang.annotation.Annotation;

import org.cs27x.adv.java.InvocationProcessor;
import org.cs27x.adv.java.InvocationProcessorFactory;

public class SimpleFactory implements InvocationProcessorFactory {

	private TestProcessor testProcessor_ = new TestProcessor();
	
	@SuppressWarnings("unchecked")
	@Override
	public <PreProc extends Annotation> InvocationProcessor<PreProc> getProcessor(
			PreProc proc) {
		InvocationProcessor<PreProc> processor = null;
		if(proc.annotationType().equals(TestDirective.class)){
			processor = (InvocationProcessor<PreProc>)testProcessor_; 
		}
		return processor;
	}

	public TestProcessor getTestProcessor() {
		return testProcessor_;
	}

	public void setTestProcessor(TestProcessor testProcessor) {
		testProcessor_ = testProcessor;
	}
	
}
