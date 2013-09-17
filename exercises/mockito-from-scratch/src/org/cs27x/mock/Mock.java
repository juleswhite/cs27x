package org.cs27x.mock;

import static com.google.common.base.Preconditions.checkArgument;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/**
 * I am most definitely NOT a thread-safe class!
 * 
 * This class represents a Mock object. The static method when(...) can be
 * used to stub return values for method invocations. By default, any unstubbed
 * methods return null. Unstubbed methods with primitive return types return:
 * - Numeric: 0
 * - Boolean: false
 * 
 * Interactions with Mocks can be verified via calls to the static verify(...) method.
 * 
 * @author jules
 * 
 */
public class Mock implements InvocationHandler {

	private static final ProxyCreator proxyCreator_ = new CglibProxyCreator();

	private static Mock context_;

	private static List<ArgMatcher> matcherQueue_ = new LinkedList<>();

	/**
	 * Whenever mock(...) is called, a dynamic proxy is created with a new
	 * instance of Mock as the handler for all of the proxy's method invocations.
	 * 
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T mock(Class<T> type) {
		return (T) proxyCreator_.createProxy(type.getClassLoader(),
				new Class<?>[] { type }, new Mock());
	}

	private static void setContext(Mock m) {
		context_ = m;
	}

	/**
	 * Calls to when(...) look like this:
	 * 
	 * when(foo.bar(eq("someval"))).thenReturn;
	 * 
	 * When these expressions are evaluated, the following steps take place:
	 * 1. Calls to any(...) or eq(...) cause new ArgMatchers to be pushed onto the
	 *    static matcherQueue_ data structure.
	 * 2. Calls to the mock's method (e.g., foobar()) are intercepted by the Mock
	 *    in its invoke(Object target, Method m, Object[] args) method
	 *    and recorded in its invocations_ list (the calls return null). VERY
	 *    IMPORTANT: context_ is set to "this" by the mock that is being interacted
	 *    with.
	 * 3. When returns the mock, which happens to have a "thenReturn" method.
	 * 4. Mock.thenReturn() pops the last recorded method invocation off of
	 *    the list of invocations, creates a method signature for the invocation
	 *    using any queued ArgMatchers from the matcherQueue (otherwise it uses
	 *    the values passed to the method call), and then creates a new MethodStubInfo
	 *    with the same signature and the provided return value.
	 * 5. The context_ is set to null and the matcherQueue_ is cleared.
	 * 
	 * @param obj
	 * @return
	 */
	public static Mock when(Object obj) {
		return context_;
	}

	/**
	 * Calls to verify look like this:
	 * 
	 * verify(foo).bar(eq("abc"));
	 * 
	 * When these expressions are evaluated, the following steps take place:
	 * 1. Calls to any(...) or eq(...) cause new ArgMatchers to be pushed onto the
	 *    static matcherQueue_ data structure.
	 * 2. Calls to verify turn on "verify" mode in the mock by setting the verifying_
	 *    member variable to true
	 * 3. Calls to the mock's method (e.g., foobar()) are intercepted by the Mock
	 *    in its invoke(Object target, Method m, Object[] args) method
	 *    and recorded in its invocations_ list (the calls return null). The mock
	 *    then sees that "verifying_" is set to true. The mock generates a method
	 *    signature for the call (including any ArgMatchers) and then searches through
	 *    the list of recorded invocations to see if it can find a matching invocation.
	 *    If not, the method throws a VerificationException.
	 * 4. The context_ is set to null and the matcherQueue_ is cleared.
	 * 
	 * @param obj
	 * @return
	 */
	public static <T> T verify(T obj) {
		context_ = (Mock)proxyCreator_.getInvocationHandler(obj);
		context_.verifyStart();
		return obj;
	}

	private static ArgMatcher eqMatcher(final Object wanted) {
		return new ArgMatcher() {
			@Override
			public boolean matches(Object val) {
				return (wanted == val)
						|| (wanted != null && wanted.equals(val));
			}
			public String toString(){
				return "eq("+ wanted + ")";
			}
		};
	}

	private static ArgMatcher anyMatcher(final Class<?> wanted) {
		return new ArgMatcher() {
			@Override
			public boolean matches(Object val) {
				return val != null && wanted.isAssignableFrom(val.getClass());
			}
			public String toString(){
				return "any("+ wanted.getName() + ")";
			}
		};
	}

	/**
	 * Used to specify an argument that is expected to .equals()
	 * the passed in value. You can use eq() in a stub or verify
	 * expression:
	 * 
	 * when(foo.bar(eq("a"))).thenReturn("b");
	 * verify(foo.bar(eq("a")));
	 * 
	 * @param wanted
	 * @return
	 */
	public static <T> T eq(T wanted) {
		return queueMatcher(eqMatcher(wanted), wanted);
	}

	/**
	 * Used to specify an argument that is expected to be an instance of
	 * the passed in value. You can use any() in a stub or verify
	 * expression:
	 * 
	 * when(foo.bar(any(String.class))).thenReturn("b");
	 * verify(foo.bar(any(String.class)));
	 * 
	 * @param wanted
	 * @return
	 */
	public static <T> T any(Class<T> wanted) {
		return queueMatcher(anyMatcher(wanted), createInstance(wanted));
	}

	/*
	 * Called when eq() or any() expressions are evaluated to
	 * statically queue them up for later use.
	 * 
	 */
	private static <T> T queueMatcher(ArgMatcher matcher, T val) {
		matcherQueue_.add(matcher);
		return val;
	}

	/*
	 * Not used for this implementation...
	 */
	private static <T> T createInstance(Class<T> type) {
		return null;
	}

	private Set<MethodStubInfo> stubs_ = new HashSet<>();

	private List<MethodInvocation> invocations_ = new LinkedList<>();
	
	private boolean verifying_ = false;

	/**
	 * This method is called whenever any method call is maded on a
	 * Mock object. The method does one of three things:
	 * 
	 * 1. Sets the Mock as the current context_ so that subsequent
	 *    static calls to when() can determine the appropriate Mock object to
	 *    stub out
	 * 2. Records the MethodInvocation in the invocations_ list
	 * 3. Determines if a MethodStubInfo has been created that matches
	 *    the method call and uses the stub's ResultGenerator to produce
	 *    a return value for the call.
	 * 4. If verifying_ is set to true, rather than using a stub to generate
	 *    a return value, the method searches for a matching MethodInvocation
	 *    exists in the list of recorded MethodInvocations.
	 * 
	 * Note: stubbing relies on a subsequent call to thenReturn() after this 
	 * method is invoked.
	 */
	@Override
	public Object invoke(Object target, Method m, Object[] args) {
		setContext(this);
		
		pushInvocation(new MethodInvocation(m, target, args));

		return (verifying_) ?
				invokeVerify(target, m, args)
				: invokeStub(target, m, args);
	}
	
	/*
	 * Delegate method used to ensure that a method invocation in the past
	 * occurred that matches the current invocation signature.
	 * 
	 */
	private Object invokeVerify(final Object target, final Method m, final Object[] args){
		final MethodInvocation current = popInvocation();
		final MethodInvocationSignature sig = createInvocationSignature(current);
		
		boolean foundMatchingInvocation = false;
		
		// Step 3: Search through the list of MethodInvocations stored in invocations_
		// and see if an invocation exists that matches the signature of the current
		// invocation, which is what the caller is trying to verify.
		//
		// Your code here.
		
		verifyDone();
		
		if(!foundMatchingInvocation){
			throw new VerificationException(target,sig);
		}
		
		return null;
	}

	/*
	 * Push/pop method invocations for later use for stubbing or verification
	 */
	private void pushInvocation(MethodInvocation mi){
		invocations_.add(mi);
	}
	
	private MethodInvocation popInvocation() {
		return invocations_.remove(invocations_.size() - 1);
	}

	/*
	 * This method finds any existing MethodInvocationStub that matches
	 * the current method invocation and use its ResultGenerator to
	 * produce a return value. If no matching stub is found, the method
	 * returns null.
	 */
	private Object invokeStub(final Object target, final Method m,
			final Object[] args) {
		
		// Step 2: Find the MethodStub object that matches the current
		// call. 
		//
		// Your code here.
		MethodStubInfo stub = null;
		
		if (stub == null && m.getName().equals("hashCode")) {
			return super.hashCode();
		} else if (stub == null && m.getName().equals("equals")) {
			return super.equals(args[0]);
		} else if (stub == null && m.getName().equals("toString")){
			return proxyCreator_.getInvocationHandler(target).toString();
		}

		return (stub != null) ? stub.getResult()
				.generate(target, m, args) : null;
	}

	/**
	 * This method is used to stub a return value for any method invocations
	 * that match the method invocation signature that was produced from the
	 * most recent invocation on the Mock.
	 * 
	 * @param val
	 */
	public void thenReturn(final Object val) {
		MethodInvocation invocation = popInvocation();
		MethodInvocationSignature sig = createInvocationSignature(invocation);
		
		// Step 1: Create a new MethodStubInfo object that will match the
		// specified signature and return the specified value and add it to
		// the list of method stubs stored in stubs_.
		//
		//
		// Your code here.
		
		
		resetStubbingState();
	}

	private void resetStubbingState() {
		matcherQueue_.clear();
		context_ = null;
	}

	/*
	 * This method creates a signature object that will exactly match the passed in
	 * invocation. If any ArgMatchers were specified via calls to any() or eq(), those
	 * matchers will be incorporated into the signature.
	 * 
	 * Note: you must either completely rely on ArgMatchers or use all concrete values.
	 * You cannot specify a stub that relies on a mixture of the two.
	 */
	private MethodInvocationSignature createInvocationSignature(
			MethodInvocation mi) {

		checkArgument(
				mi.getArgs().length == matcherQueue_.size()
						|| matcherQueue_.size() == 0,
				"Invalid stub specification - you can either use arg matchers "
						+ "or straight argument values, but not both in a stub specification "
						+ "(e.g., you mixed use of eq() or any() with concrete values");

		List<ArgMatcher> matchers = getMatchers(mi.getArgs());

		return new MethodInvocationSignature(mi.getMethod(), matchers);
	}

	private List<ArgMatcher> getMatchers(Object[] args) {
		return (matcherQueue_.size() > 0) ? matcherQueue_ : Lists.transform(
				Arrays.asList(args), new Function<Object, ArgMatcher>() {
					public ArgMatcher apply(Object o) {
						return eqMatcher(o);
					}
				});
	}

	private void verifyStart(){
		verifying_ = true;
	}
	
	private void verifyDone(){
		verifying_ = false;
	}
}
