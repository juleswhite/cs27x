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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class maintains a list of the shortest paths from any source node
 * to all other nodes in the graph.
 * 
 * @author jules
 *
 * @param <T>
 */
public class ShortestPaths<T> {

	private T start_;
	
	private Map<T,T> precursors_ = new HashMap<T,T>();
	private Map<T,Double> weights_ = new HashMap<T,Double>();
	
	public ShortestPaths(T start){
		checkArgument(start != null, "The starting node for a ShortestPaths object must not be null.");
		
		start_ = start;
	}
	
	public void setBestPath(T end, T precursor, double weight){
		precursors_.put(end, precursor);
		weights_.put(end, weight);
	}
	
	@SuppressWarnings("unchecked")
	public Path<T> getShortestPath(T end){
		List<T> nodes = new LinkedList<T>();
		nodes.add(end);
		getShortestPath(end,nodes);
		return new Path<T>((T[])nodes.toArray(), weights_.get(end));
	}

	private void getShortestPath(T end, List<T> nodes){
		if(!start_.equals(end)){
			T precursor = precursors_.get(end);
			nodes.add(0, precursor);
			getShortestPath(precursor, nodes);
		}
	}
}
