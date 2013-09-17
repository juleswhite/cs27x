package org.cs27x.mock;

import java.lang.reflect.InvocationHandler;

/**
 * 
 * This is an interface for classes that can create dynamic proxies to either
 * interfaces or concrete classes. The returned proxy should forward all method
 * invocations on the proxy to the InvocationHandler specified in the
 * createProxy() method.
 * 
 * @author jules
 * 
 */
public interface ProxyCreator {

	/**
	 * Create a proxy for the specified types and forward all
	 * method calls to the specified InvocationHandler.
	 * 
	 * @param cl
	 * @param types
	 * @param hdlr
	 * @return
	 */
	public Object createProxy(ClassLoader cl, Class<?>[] types,
			InvocationHandler hdlr);

	/**
	 * Calls to getInvocationHandler(...) with a proxy object should return the
	 * InvocationHandler that was passed in when that proxy object was created.
	 * 
	 * @param o
	 * @return
	 */
	public InvocationHandler getInvocationHandler(Object o);

	/**
	 * Only implementers that can proxy concrete classes should return true. If
	 * this method returns true, it is safe to call createProxy() and pass in a
	 * Class object pointing to a non-interface class.
	 * 
	 * @return
	 */
	public boolean supportsNonInterfaceProxies();
}
