package org.cs27x.adv.java.processors;

import org.cs27x.adv.java.Invocation;
import org.cs27x.adv.java.InvocationProcessor;
import org.cs27x.adv.java.directives.TimeMe;

public class TimeMeProcessor implements InvocationProcessor<TimeMe>{

	private String label_;
	private long start_;
	
	@Override
	public void preProcess(Invocation i) {
		label_ = i.getTarget().getClass().getName()+"."+i.getTargetMethod().getName()+"()";
		start_ = System.currentTimeMillis();
	}

	@Override
	public void postProcess(Invocation i) {
		long end = System.currentTimeMillis();
		long total = end - start_;
		
		System.out.println(label_ +": " + total + "(ms)");
	}

	@Override
	public void setAnnotation(TimeMe anno) {}

}
