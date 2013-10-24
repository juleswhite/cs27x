package org.cs27x.adv.java;

import java.lang.annotation.Annotation;

import org.cs27x.asgn.proc.AsgnStep;


@AsgnStep("1")
/**
 * Create an implementation of this interface that allows
 * annotations to be mapped to InvocationProcessors. Your
 * implementation should have a method to add InvocationProcessors
 * to it. Once an InvocationProcessor is added to your factory,
 * calling getProcessor(someannotation) should return an
 * instance of the correct InvocationProcessor.
 * 
 */
public interface InvocationProcessorFactory {

	public <PreProc extends Annotation> InvocationProcessor<PreProc> getProcessor(PreProc proc);
	
}
