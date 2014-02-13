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
package org.magnum.mcc.building;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.magnum.mcc.paths.DirectedGraph;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

/**
 * A class representing a floorplan. This class provides methods
 * for adding locations, setting the type of locations, and obtaining
 * a graph representation of a floorplan.
 * 
 * 
 * @author jules
 *
 */
public class Floorplan {

	private final Set<FloorplanLocation> locations_ = new HashSet<FloorplanLocation>();

	private final DirectedGraph<FloorplanLocation> graph_ = new DirectedGraph<FloorplanLocation>();

	private final SetMultimap<LocationType, FloorplanLocation> typesToLocations_ = HashMultimap
			.create();

	private final Map<String, LocationType> typesByName_ = new HashMap<String, LocationType>();

	private final Map<String, FloorplanLocation> locationsById_ = new HashMap<String, FloorplanLocation>();

	private final LocationType rootType_;

	public Floorplan(LocationType rootType) {
		super();
		checkArgument(
				rootType != null,
				"A Floorplan must have a rootType from which the tree it uses to categorize locations extends from.");

		rootType_ = rootType;
		addType(rootType_);
	}

	private void addType(LocationType type) {
		typesByName_.put(type.getName(), type);

		if (type.getChildren() != null) {
			for (LocationType child : type.getChildren()) {
				addType(child);
			}
		}
	}

	public DirectedGraph<FloorplanLocation> asGraph() {
		return graph_;
	}

	public FloorplanLocation addLocation(String id, LocationType type) {
		checkArgument(id != null, "A floorplan location must have a non-null id");
		checkArgument(type != null, "A floorplan location must have a non-null type");
		
		final FloorplanLocation l = new FloorplanLocation(id, type);
		locations_.add(l);
		locationsById_.put(id, l);
		graph_.addNode(l);
		typesToLocations_.put(type, l);
		addType(type);
		return l;
	}

	public FloorplanLocation locationById(String id) {
		return locationsById_.get(id);
	}

	public LocationType typeByName(String name) {
		return typesByName_.get(name);
	}

	public void connectsToOneWay(FloorplanLocation from, FloorplanLocation to,
			double weight) {
		graph_.addEdge(from, to, weight);
	}

	public void connectsTo(FloorplanLocation from, FloorplanLocation to,
			double weight) {
		connectsToOneWay(from, to, weight);
		connectsToOneWay(to, from, weight);
	}

	public Set<FloorplanLocation> getLocations() {
		return locations_;
	}

	public Map<FloorplanLocation, Double> getEdgesFrom(FloorplanLocation fpl) {
		return graph_.edgesFrom(fpl);
	}

	public Set<FloorplanLocation> getLocationsOfType(LocationType type) {
		return typesToLocations_.get(type);
	}

	/**
	 * The LocationTypes are connected in a tree structure. The root type is the
	 * most general type of location in the floorplan. All other types must be
	 * descendants of the root. This implementation detail could be changed...
	 * but is helpful when serializing a floorplan.
	 * 
	 * @return
	 */
	public LocationType getRootType() {
		return rootType_;
	}

	public Set<LocationType> getLocationTypes() {
		final Set<LocationType> types = typesToLocations_.keySet();
		final Set<LocationType> all = new HashSet<LocationType>();
		
		for (LocationType type : types) {
			while (type != null) {
				all.add(type);
				type = type.getParent();
			}
		}
		return all;
	}

}
