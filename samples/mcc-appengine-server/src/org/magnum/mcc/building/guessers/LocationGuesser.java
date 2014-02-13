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

import java.util.Set;

import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.FloorplanLocation;
import org.magnum.mcc.building.LocationType;

/**
 * A class that can be used to guess the user's location. The LocationGuesser
 * delegates to a series of GuesserStrategy objects to do the real work. The
 * LocationGuesser keeps a list of possible locations / types. The GuesserStrategy
 * objects call narrow on the LocationGuesser to continually refine/reduce the list
 * of possible locations until the LocationGuesser has only a single possible
 * location left.
 * 
 * @author jules
 *
 */
public interface LocationGuesser {

	/**
	 * Narrow down the list of possible locations
	 * using the provided measurements
	 * 
	 * @param m
	 */
	public void narrow(Measurements m);
	
	/**
	 * 
	 * Narrow down the list of possible locations by
	 * specifying a LocationType.
	 * 
	 * @param t
	 * @return
	 */
	public void narrowLocationsByType(LocationType t);

	/**
	 * 
	 * Narrow down the list of possible locations by
	 * specifying a subset that are possible.
	 * 
	 * @param possible
	 * @return
	 */
	public void narrowLocations(FloorplanLocation... possible);

	/**
	 * 
	 * Narrow down the list of possible locations by
	 * specifying a subset that are possible.
	 * 
	 * @param possible
	 * @return
	 */
	public void narrowLocations(Set<FloorplanLocation> possible);

	/**
	 * 
	 * List the top-level (e.g., most abstract) LocationTypes
	 * that the final location could be. 
	 * 
	 * @return
	 */
	public Set<LocationType> getPossibleLocationTypes();

	/**
	 * 
	 * List the possible locations based on the current narrowing.
	 * 
	 * @return
	 */
	public Set<FloorplanLocation> getPossibleLocations();

	/**
	 * 
	 * Has the narrowing singled out one location?
	 * 
	 * @return
	 */
	public boolean located();

	/**
	 * 
	 * Return the current most probable location. If this Guesser
	 * does not support a sorted ordering on possible locations and
	 * there is more than one possible location left, it will simply 
	 * return one of the possible locations. 
	 * 
	 * @return
	 */
	public FloorplanLocation getLocation();
	
	
	/**
	 * 
	 * Returns the floorplan currently in use by the .
	 * @return
	 */
	public Floorplan getFloorplan();

}