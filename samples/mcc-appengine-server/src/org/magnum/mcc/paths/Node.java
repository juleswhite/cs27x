/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.paths;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 
 * A simple class that represents a node in a graph.
 * 
 * @author jules
 *
 */
public class Node {

	private String id_;

	public Node(){
		
	}
	
	public Node(String id){
		checkArgument(id != null, "Each node must have a non-null id");
		
		id_ = id;
	}
	
	public String getId() {
		return id_;
	}
	
	public String toString(){
		return id_;
	}

	public void setId(String id) {
		checkArgument(id != null, "Each node must have a non-null id");
		
		id_ = id;
	}

}
