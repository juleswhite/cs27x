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
package org.magnum.mcc.building.guessers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.FloorplanLocation;
import org.magnum.mcc.building.LocationType;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * A Locator is responsible for narrowing down an unknown location.
 * The Locator is initialized with a floorplan and an initial LocationType.
 * The Locator is iteratively honed in on a location via successive calls
 * to narrow(...). 
 * 
 * Example:
 * 
 * Locator l = new Locator(floorplan, rootType);
 * l = l.narrow(roomType);
 * l = l.narrow(bedroomType);
 * 
 * Set<FloorplanLocation> possible = l.getPossibleLocations();
 * 
 * l = l.narrow(bedroom1);
 * assertTrue(l.located() == true);
 * 
 * FloorplanLocation location = l.getLocation();
 * 
 * 
 * @author jules
 *
 */
public class LocationGuesserImpl implements LocationGuesser {

	private final Floorplan floorplan_;

	private LocationType rootType_;

	private Set<FloorplanLocation> possibleLocations_;
	
	private final GuesserStrategyFactory strategies_;

	public LocationGuesserImpl(GuesserStrategyFactory factory, Floorplan floorplan, LocationType root) {
		this(factory, floorplan, root, floorplan.getLocations());
	}

	public LocationGuesserImpl(GuesserStrategyFactory factory, Floorplan floorplan, LocationType root,
			Set<FloorplanLocation> possible) {
		floorplan_ = floorplan;
		strategies_ = factory;
		rootType_ = root;
		possibleLocations_ = possible;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see org.magnum.mcc.building.guessers.LocationGuesser#narrow(org.magnum.mcc.building.guessers.Measurements)
	 */
	@Override
	public void narrow(Measurements ms) {
		for(Measurement m : ms){
			GuesserStrategy gs = strategies_.getGuesserStrategy(m.getType());
			if(gs != null){
				gs.guess(this, ms);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.magnum.mcc.building.LocatorI#narrow(org.magnum.mcc.building.LocationType)
	 */
	@Override
	public void narrowLocationsByType(LocationType t) {
		rootType_ = floorplan_.typeByName(t.getName());
		Set<FloorplanLocation> newpossible = floorplan_.getLocationsOfType(t);
		possibleLocations_ = Sets.intersection(new HashSet<FloorplanLocation>(possibleLocations_), newpossible);
	}
	
	/* (non-Javadoc)
	 * @see org.magnum.mcc.building.LocatorI#narrow(org.magnum.mcc.building.FloorplanLocation)
	 */
	@Override
	public void narrowLocations(FloorplanLocation...possible) {
		possibleLocations_ = new HashSet<FloorplanLocation>(Arrays.asList(possible));
	}

	/* (non-Javadoc)
	 * @see org.magnum.mcc.building.LocatorI#narrow(java.util.Set)
	 */
	@Override
	public void narrowLocations(Set<FloorplanLocation> possible) {
		possibleLocations_ = possible;
	}

	/* (non-Javadoc)
	 * @see org.magnum.mcc.building.LocatorI#getPossibleLocationTypes()
	 */
	@Override
	public Set<LocationType> getPossibleLocationTypes() {
		return (rootType_.getChildren() != null && rootType_.getChildren()
				.size() > 0) ? rootType_.getChildren()
				: new ImmutableSet.Builder<LocationType>().add(rootType_)
						.build();
	}

	/* (non-Javadoc)
	 * @see org.magnum.mcc.building.LocatorI#getPossibleLocations()
	 */
	@Override
	public Set<FloorplanLocation> getPossibleLocations() {
		return possibleLocations_;
	}

	/* (non-Javadoc)
	 * @see org.magnum.mcc.building.LocatorI#located()
	 */
	@Override
	public boolean located() {
		return possibleLocations_.size() == 1;
	}

	/* (non-Javadoc)
	 * @see org.magnum.mcc.building.LocatorI#getLocation()
	 */
	@Override
	public FloorplanLocation getLocation() {
		return (possibleLocations_.size() > 0) ? possibleLocations_.iterator().next()
				: null;
	}

	@Override
	public Floorplan getFloorplan() {
		return floorplan_;
	}
}
