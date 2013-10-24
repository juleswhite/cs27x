package org.cs27x.adv.java.test;

import org.cs27x.adv.java.Invocation;
import org.cs27x.adv.java.InvocationProcessor;

public class TestProcessor implements InvocationProcessor<TestDirective> {

	private boolean preprocessCalled_;
	private boolean postProcessCalled_;
	private boolean setAnnotationCalled_;
	
	@Override
	public void preProcess(Invocation i) {
		preprocessCalled_ = true;
	}

	@Override
	public void postProcess(Invocation i) {
		postProcessCalled_ = true;
	}

	@Override
	public void setAnnotation(TestDirective anno) {
		setAnnotationCalled_ = true;
	}

	public boolean wasPreprocessCalled() {
		return preprocessCalled_;
	}

	public void setPreprocessCalled(boolean preprocessCalled) {
		preprocessCalled_ = preprocessCalled;
	}

	public boolean wasPostProcessCalled() {
		return postProcessCalled_;
	}

	public void setPostProcessCalled(boolean postProcessCalled) {
		postProcessCalled_ = postProcessCalled;
	}

	public boolean wasSetAnnotationCalled() {
		return setAnnotationCalled_;
	}

	public void setSetAnnotationCalled(boolean setAnnotationCalled) {
		setAnnotationCalled_ = setAnnotationCalled;
	}
	
	public void reset(){
		preprocessCalled_ = false;
		postProcessCalled_ = false;
		setAnnotationCalled_ = false;
	}
	
}