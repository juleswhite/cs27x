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
package org.magnum.mcc.controllers;

import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.FloorplanLocation;
import org.magnum.mcc.building.loader.FloorplanLoader;
import org.magnum.mcc.building.loader.FloorplanMarshaller;
import org.magnum.mcc.modules.StandaloneServerModule;
import org.magnum.mcc.paths.Path;
import org.magnum.mcc.paths.ShortestPathSolver;
import org.magnum.mcc.paths.ShortestPaths;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * This class is the entry point for all HTTP requests to the MCC server.
 * 
 * 
 * @author jules
 *
 */
@Controller
public class NavController {

	private FloorplanMarshaller floorplanMarshaller_;
	private FloorplanLoader floorplanLoader_;
	private ShortestPathSolver solver_;
	
	public NavController() {
		Injector injector = Guice.createInjector(new StandaloneServerModule());
		floorplanLoader_ = injector
				.getInstance(FloorplanLoader.class);
		solver_ = injector.getInstance(ShortestPathSolver.class);
		floorplanMarshaller_ = injector.getInstance(FloorplanMarshaller.class);
	}


	// A duplicate of the next method to make posting floorplans
	// via form without javascript possible. The key difference is
	// that this method accepts the floorplanId as a post parameter
	// rather than a path parameter.
	@RequestMapping(value="/floorplan",method=RequestMethod.POST)
	public @ResponseBody Floorplan setFloorplanUsingForm(
			@RequestParam("floorplanId") String floorplanId, 
			@RequestParam("floorplan") String floorplanjson) throws Exception {
		return setFloorplan(floorplanId, floorplanjson);
	}
	
	@RequestMapping(value="/floorplan/{floorplanId}",method=RequestMethod.POST)
	public @ResponseBody Floorplan setFloorplan(
			@PathVariable("floorplanId") String floorplanId, 
			@RequestParam("floorplan") String floorplanjson) throws Exception {
		
		// This is highly insecure. We are blindly trusting the client
		// and storing data it provides into the datastore. This data
		// should be highly sanitized here...
		Floorplan fp = floorplanMarshaller_.fromString(floorplanjson);
		floorplanLoader_.save(floorplanId, fp);
		
		return fp;
	}
	
	
	/**
	 * Returns the raw floorplan definition for the specified ID.
	 * 
	 * 
	 * @param floorplanId
	 * @return
	 */
	@RequestMapping(value="/floorplan/{floorplanId}",method=RequestMethod.GET)
	public @ResponseBody Floorplan getFloorplan(@PathVariable("floorplanId") String floorplanId){
		return floorplanLoader_.load(floorplanId);
	}
	
	
	/**
	 * Returns the shortest path from the given starting location to the
	 * given ending location for the specified floorplan.
	 * 
	 * @param floorplanId
	 * @param from
	 * @param to
	 * @return
	 */
	@RequestMapping("/path/{floorplanId}/{from}/{to}")
	public @ResponseBody
	Path<FloorplanLocation> getDirections(
			@PathVariable("floorplanId") String floorplanId,
			@PathVariable("from") String from, @PathVariable("to") String to) {

		Floorplan fp = getFloorplan(floorplanId);
		
		FloorplanLocation source = fp.locationById(from);
		FloorplanLocation end = fp.locationById(to);
		
		ShortestPaths<FloorplanLocation> paths = solver_.shortestPaths(fp.asGraph(), source);

		return paths.getShortestPath(end);
	}

}
