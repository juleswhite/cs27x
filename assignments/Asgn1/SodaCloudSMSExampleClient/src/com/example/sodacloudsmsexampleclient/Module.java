package com.example.sodacloudsmsexampleclient;


/**
 * Asgn Step 4: Create an implementation of this interface
 * that creates a mapping of classes to specific object instances.
 * The goal of this module implementation will be to decouple the
 * use of the "components" from their creation. 
 * 
 */
public interface Module {

	/**
	 * 
	 * This method returns the component that is bound to a given
	 * type.
	 * 
	 * @param type - type type of component to retrieve
	 * @return
	 */
	public <T> T getComponent(Class<T> type);
	
	/**
	 * 
	 * Bind a component to a type.
	 * 
	 * @param type - the type to bind the component to
	 * @param component - the object instance to associate with the type key
	 */
	public <T> void setComponent(Class<T> type, T component);
	
}
