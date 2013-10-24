package org.cs27x.adv.java;

import org.cs27x.asgn.proc.AsgnStep;

/**
 * This pre / post execution logic framework is extended
 * by adding two classes: 1) an implementation of InvocationProcessor
 * that performs the pre / post execution logic in its preProcess and
 * postProcess methods and 2) an implementation of an annotation that 
 * is used to mark where the pre / post execution logic should be 
 * applied.
 * 
 * Note: Your InvocationProcessor must be parameterized by its annotation
 * type (e.g, EchoProcessor is parameterized with Echo).
 * 
 * 
 */

@AsgnStep("4")
/**
 * Create three interesting / useful implementations
 * of an InvocationProcessor and associated annotations
 * for each InvocationProcessor. The
 * 
 */
public interface InvocationProcessor<AnnoType> {

	public void preProcess(Invocation i);
	public void postProcess(Invocation i);
	public void setAnnotation(AnnoType anno);
	
}
