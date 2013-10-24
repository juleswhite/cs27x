package org.cs27x.adv.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.cs27x.asgn.proc.AsgnStep;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class PreProcessorWrapper implements MethodInterceptor {
	
	@AsgnStep("2")
	/**
	 * 
	 * Update this method to wrap the passed in target object
	 * so that its method annotation logic will be executed
	 * on method calls. You should make the default 
	 * InvocationProcessorFactor an instance of your new
	 * implementation created in step 1.
	 * 
	 */
	public static <Type> Type wrap(Type target) {

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <Type> Type wrap(Type target, InvocationProcessorFactory factory) {
		PreProcessorWrapper wrapper = new PreProcessorWrapper(factory,target);

		// This section of code is a key piece of the CGLib
		// magic that makes this method pre/post processing possible.
		// The Enhancer takes a superclass and generates a new derived
		// class along the lines of:
		//
		// Super:
		//
		// public class Foo {
		//    public void bar(){
		//        do stuff...
		//    }
		// }
		//
		// Generated Class:
		//
		// public class FooGen {
		//    private Callback callback_;
		//
		// 
		//    public void bar(){
		//      callback_.methodInvoked("bar",null,new OriginalMethodHandle(){
		//         public Object callOriginalMethod(Object[] args){
		//            return FooGen.this.super.bar(args);
	    //         }
		//      );       
		//    }      
		// }
		//
		// The generated class automatically intercepts method calls
		// and then allows the Callback to decide to either handle the
		// call itself, take some pre/post action, or invoke the original
		// method with a method handle. It doesn't work exactly like this,
		// but this is the basic idea.
		
		// The code generator to create the derived class
		Enhancer enhancer = new Enhancer();
		
		// The class that the generated class should inherit from
        enhancer.setSuperclass(target.getClass());
        
        // The callback that will handle the method calls for the
        // generated derived class
        enhancer.setCallback(wrapper);
        
        // Generate the new derived class
        return (Type)enhancer.create();
	}

	private InvocationProcessorFactory factory_;

	private final Object target_;

	public PreProcessorWrapper(Object target) {
		super();
		target_ = target;
	}

	public PreProcessorWrapper(InvocationProcessorFactory factory, Object target) {
		super();
		factory_ = factory;
		target_ = target;
	}

	@Override
	public Object intercept(Object proxy, Method method, Object[] args,
			MethodProxy mproxy) throws Throwable {
		Invocation inv = new Invocation(target_, method, args);

		List<InvocationProcessor<?>> processors = getProcessors(method);

		for (InvocationProcessor<?> proc : processors) {
			proc.preProcess(inv);
		}

		inv.exec();

		for (InvocationProcessor<?> proc : processors) {
			proc.postProcess(inv);
		}

		return inv.getResult();
	}

	
	@AsgnStep("3")
	/**
	 * @Assignment Step 3: 
	 * 
	 * Modify this method to populate processors with the
	 * appropriate InvocationProcessor for each annotation.
	 * 
	 * You should probably make sure that processors isn't
	 * null...
	 * 
	 */
	public List<InvocationProcessor<?>> getProcessors(Method method) {
		List<InvocationProcessor<?>> processors = null;

		for (Annotation anno : method.getAnnotations()) {
			
			
		}

		return processors;
	}

	public InvocationProcessorFactory getFactory() {
		return factory_;
	}

	public void setFactory(InvocationProcessorFactory factory) {
		factory_ = factory;
	}

}
