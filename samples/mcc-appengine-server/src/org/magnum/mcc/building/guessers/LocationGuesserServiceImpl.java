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

import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.loader.FloorplanLoader;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;

public class LocationGuesserServiceImpl implements LocationGuesserService {

	private final FloorplanLoader loader_;
	
	private final GuesserStrategyFactory strategies_;
	
	// Caching floorplans here may not be necessary...or wanted...
	private final LoadingCache<String, Floorplan> floorplans_ = CacheBuilder.newBuilder()
		       .maximumSize(1000)
		       .build(
		           new CacheLoader<String, Floorplan>() {
		             public Floorplan load(String id) throws Exception {
		               return loader_.load(id);
		             }
		           });
	
	@Inject
	public LocationGuesserServiceImpl(GuesserStrategyFactory strategies, FloorplanLoader loader) {
		loader_ = loader;
		strategies_ = strategies;
	}
	
	@Override
	public LocationGuesser getGuesser(String floorplanId) {
		try{
			final Floorplan fp = floorplans_.get(floorplanId);
			return new LocationGuesserImpl(strategies_, fp, fp.getRootType());
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}

}
