/*************************************************************************
* Copyright 2014 Jules White
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at http://www.apache.org/licenses/
* LICENSE-2.0 Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an "AS IS"
* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
* implied. See the License for the specific language governing permissions
* and limitations under the License.
**************************************************************************/
package org.magnum.mcc.paths;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Arrays;
import java.util.Iterator;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * 
 * This class represents a path between two nodes. The weight is the length of
 * the path. This class does not assume any semantics about the edges between
 * the nodes. All that is assumed is that some path from the node at index 0 to
 * the last node in the array exists and that the sum of the lengths of the
 * edges is the same as the weight of the path.
 * 
 * @author jules
 * 
 * @param <T>
 */
public class Path<T> implements Iterable<T> {

	private final T[] nodes_;

	private final double weight_;

	/**
	 * Construct a path using an ordered set of nodes and a weight for the path.
	 * 
	 * Assuming this graph:
	 * 
	 * a--2--b--4--e | | 3 3 | | c--4--d--1--f | | 7 2 | | i--2--g--2--h--3--j
	 * 
	 * The path from a to f through d and c would be constructed as follows:
	 * 
	 * Path<String> path = new Path(new String[]{"a","c","d","f"},8.0);
	 * 
	 * The nodes can be of arbitrary types. If you have a Node class, then you
	 * simply parameterize as follows:
	 * 
	 * Path<Node> path = ...;
	 * 
	 * @param nodes
	 * @param weight
	 */
	public Path(T[] nodes, Double weight) {
		checkArgument(nodes != null,
				"A path must be provided with a non-null list of nodes.");

		checkArgument(!Iterables.any(Arrays.asList(nodes), new Predicate<T>() {
			@Override
			public boolean apply(T arg0) {
				return arg0 == null;
			}
		}), "A path cannot have any null values in its array of nodes.");

		weight_ = weight;
		nodes_ = nodes;
	}

	@Override
	public Iterator<T> iterator() {
		return Arrays.asList(nodes_).iterator();
	}

	public T[] getNodes() {
		return nodes_;
	}

	public double getWeight() {
		return weight_;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(nodes_, weight_);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Path
				&& Objects.equal(((Path<?>) obj).weight_, weight_)
				&& Arrays.equals(((Path<?>) obj).nodes_, nodes_);
	}

	/**
	 * This method returns true if the Path is equal to the Path that would be
	 * created with the provided weight and nodes. That is, the provided nodes
	 * must exactly match the nodes in the Path in the exact order and the
	 * weight must be the same.
	 * 
	 * @param weight
	 * @param nodes
	 * @return
	 */
	public boolean equalsPath(double weight, T... nodes) {
		return equals(new Path<T>(nodes, weight));
	}

}
