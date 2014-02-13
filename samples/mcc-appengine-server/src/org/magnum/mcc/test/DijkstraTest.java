/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.test;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.magnum.mcc.paths.Dijkstra;
import org.magnum.mcc.paths.DirectedGraph;
import org.magnum.mcc.paths.Node;
import org.magnum.mcc.paths.Path;
import org.magnum.mcc.paths.ShortestPaths;

/**
 * This test does a minimal check of the Dijkstra
 * implementation of the ShortestPathSolver. The
 * test is coupled to several other classes, like
 * the ShortestPaths class. Ideally, this test should
 * be rewritten to decouple it via mock objects from
 * the other dependent classes.
 * 
 * 
 * @author jules
 *
 */
public class DijkstraTest {

	private DirectedGraph<Node> testGraph_;
	
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
	
	private final Dijkstra solver_ = new Dijkstra();
	
	
	/**
	 * 
	 * Create the test graph
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
		testGraph_ = new DirectedGraph<Node>();
		
		testGraph_.addNode(a_);
		testGraph_.addNode(b_);
		testGraph_.addNode(c_);
		testGraph_.addNode(d_);
		testGraph_.addNode(e_);
		testGraph_.addNode(f_);
		testGraph_.addNode(g_);
		testGraph_.addNode(h_);
		testGraph_.addNode(i_);
		testGraph_.addNode(j_);
		
		testGraph_.addDualEdge(a_, b_, 2);
		testGraph_.addDualEdge(a_, c_, 3);
		testGraph_.addDualEdge(b_, e_, 4);
		testGraph_.addDualEdge(e_, f_, 3);
		testGraph_.addDualEdge(c_, d_, 4);
		testGraph_.addDualEdge(d_, f_, 1);
		testGraph_.addDualEdge(d_, g_, 2);
		testGraph_.addDualEdge(c_, i_, 7);
		testGraph_.addDualEdge(i_, g_, 2);
		testGraph_.addDualEdge(g_, h_, 2);
		testGraph_.addDualEdge(h_, j_, 3);
	}
	
	
	/*
	 * All of the test cases are the same, solve for the
	 * ShortestPaths from a source node to all other nodes
	 * and then check one of the paths against the known
	 * optimal value.
	 * 
	 */
	
	@Test
	public void testShortestPathFromAToF() {
		ShortestPaths<Node> paths = solver_.shortestPaths(testGraph_, a_);
	
		Path<Node> path = paths.getShortestPath(f_);
		assertEquals(8.0, path.getWeight(), 0);
		
		Iterator<Node> iter = path.iterator();
		assertEquals(iter.next(), a_);
		assertEquals(iter.next(), c_);
		assertEquals(iter.next(), d_);
		assertEquals(iter.next(), f_);
	}
	
	@Test
	public void testShortestPathFromAToJ() {
		ShortestPaths<Node> paths = solver_.shortestPaths(testGraph_, a_);
	
		Path<Node> path = paths.getShortestPath(j_);
		assertEquals(14.0, path.getWeight(), 0);
		
		Iterator<Node> iter = path.iterator();
		assertEquals(iter.next(), a_);
		assertEquals(iter.next(), c_);
		assertEquals(iter.next(), d_);
		assertEquals(iter.next(), g_);
		assertEquals(iter.next(), h_);
		assertEquals(iter.next(), j_);
	}

	@Test
	public void testShortestPathFromGToE() {
		ShortestPaths<Node> paths = solver_.shortestPaths(testGraph_, g_);
	
		Path<Node> path = paths.getShortestPath(e_);
		assertEquals(6.0, path.getWeight(), 0);
		
		Iterator<Node> iter = path.iterator();
		assertEquals(iter.next(), g_);
		assertEquals(iter.next(), d_);
		assertEquals(iter.next(), f_);
		assertEquals(iter.next(), e_);
	}

	@Test
	public void testShortestPathFromJToB() {
		ShortestPaths<Node> paths = solver_.shortestPaths(testGraph_, j_);
	
		Path<Node> path = paths.getShortestPath(b_);
		assertEquals(15.0, path.getWeight(), 0);
		
		Iterator<Node> iter = path.iterator();
		assertEquals(iter.next(), j_);
		assertEquals(iter.next(), h_);
		assertEquals(iter.next(), g_);
		assertEquals(iter.next(), d_);
		assertEquals(iter.next(), f_);
		assertEquals(iter.next(), e_);
		assertEquals(iter.next(), b_);
	}

	
}
