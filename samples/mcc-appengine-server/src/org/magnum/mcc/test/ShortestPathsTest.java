/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.test;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.magnum.mcc.paths.Node;
import org.magnum.mcc.paths.Path;
import org.magnum.mcc.paths.ShortestPaths;

public class ShortestPathsTest {

	private final Node a_ = new Node("a");
	private final Node b_ = new Node("b");
	private final Node c_ = new Node("c");
	private final Node d_ = new Node("d");
	private final Node e_ = new Node("e");
	private final Node f_ = new Node("f");
	private final Node g_ = new Node("g");
	private final Node h_ = new Node("h");
	private final Node i_ = new Node("i");
	private final Node j_ = new Node("j");
	
	private final ShortestPaths<Node> shortestPaths_ = new ShortestPaths<Node>(a_);
	
	/**
	 * 
	 * The test is based on the shortest
	 * paths from node "a" in the graph below
	 * to all other nodes.
	 * 
	 * a--2--b--4--e
	 * |           |
	 * 3           3
	 * |           |
	 * c--4--d--1--f
	 * |     |
	 * 7     2
	 * |     |
	 * i--2--g--2--h--3--j
	 * 
	 */
	@Before
	public void setUp(){
		shortestPaths_.setBestPath(a_, a_, 0);
		shortestPaths_.setBestPath(b_, a_, 2);
		shortestPaths_.setBestPath(e_, b_, 6);
		shortestPaths_.setBestPath(f_, d_, 8);
		shortestPaths_.setBestPath(d_, c_, 7);
		shortestPaths_.setBestPath(c_, a_, 3);
		shortestPaths_.setBestPath(i_, c_, 10);
		shortestPaths_.setBestPath(g_, d_, 9);
		shortestPaths_.setBestPath(h_, g_, 11);
		shortestPaths_.setBestPath(j_, h_, 14);
	}
	
	@Test
	public void testAToD() {
		Path<Node> path = shortestPaths_.getShortestPath(d_);
		
		assertTrue(path.equalsPath(7.0, a_, c_, d_));
		assertTrue(!path.equalsPath(7.0, a_, b_, d_));
	}
	
	
	@Test
	public void testAToJ() {
		Path<Node> path = shortestPaths_.getShortestPath(j_);
		
		assertTrue(path.equalsPath(14.0, a_, c_, d_, g_, h_, j_));
		assertTrue(!path.equalsPath(7.0, a_, b_, d_));
	}
	
	
	@Test
	public void testAToF() {
		Path<Node> path = shortestPaths_.getShortestPath(f_);
		
		assertTrue(path.equalsPath(8.0, a_, c_, d_, f_));
		assertTrue(!path.equalsPath(7.0, a_, c_, d_));
	}
	
	
	@Test
	public void testAToE() {
		Path<Node> path = shortestPaths_.getShortestPath(e_);
		
		assertTrue(path.equalsPath(6.0, a_, b_, e_));
		assertTrue(!path.equalsPath(7.0, a_, b_, e_));
	}

}
