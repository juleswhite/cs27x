/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.test;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;
import org.magnum.mcc.paths.Node;
import org.magnum.mcc.paths.Path;

public class PathTest {

	private final Node a_ = new Node("a");
	private final Node b_ = new Node("b");
	private final Node c_ = new Node("c");
	private final Node d_ = new Node("d");
	
	private final Path<Node> path_ = new Path<Node>(new Node[]{a_,b_,c_},3.0);
	
	@Test
	public void testFieldCorrectness() {
		assertEquals(3.0, path_.getWeight(), 0);
		
		Iterator<Node> nodes = path_.iterator();
		assertEquals(a_, nodes.next());
		assertEquals(b_, nodes.next());
		assertEquals(c_, nodes.next());
		
		assertTrue(!nodes.hasNext());
	}
	
	@Test
	public void testObjectEqualsEqualObject(){
		Path<Node> same = new Path<Node>(new Node[]{a_,b_,c_},3.0);
		assertEquals(same, path_);
	}
	
	@Test
	public void testObjectEqualsDifferentNodes(){
		Path<Node> diff = new Path<Node>(new Node[]{a_,b_,d_},3.0);
		assertTrue(!path_.equals(diff));
		assertTrue(!diff.equals(path_));
	}
	
	@Test
	public void testObjectEqualsDifferentWeight(){
		Path<Node> diff = new Path<Node>(new Node[]{a_,b_,c_},4.0);
		assertTrue(!path_.equals(diff));
		assertTrue(!diff.equals(path_));
	}

}
