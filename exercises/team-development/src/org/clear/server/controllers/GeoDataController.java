/*************************************************************************
* Copyright 2010 Jules White
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

package org.clear.server.controllers;

import java.util.List;

import org.clear.server.data.LocatableData;
import org.clear.server.data.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GeoDataController {

	private static final String JSON_VIEW = "jsonView";
	public static final int MAX_DC_LIST_RESULTS = 25;

	/**
	 * Do a proximity search for LocationData near a lat/lon
	 * coordinate.
	 * 
	 * @param lat
	 * @param lon
	 * @param maxresults
	 * @param maxdist
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/data/nearby", method = RequestMethod.GET)
	public String getDataInProximityTo(@RequestParam("lat") double lat,
			@RequestParam("lon") double lon,
			@RequestParam(value="maxResults",defaultValue="10") int maxresults,
			@RequestParam("maxDist") double maxdist, Model model) {

		List<LocatableData> nearby = LocatableData.nearby(lat, lon, maxresults,
				maxdist);

		if (nearby != null)
			model.addAttribute("data", nearby);
		else
			model.addAttribute("data", "null");

		return JSON_VIEW;
	}

	/**
	 * Lookup a specific LocationData by id.
	 * @param id - the id of the LocationData
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/data/byid/{id}", method = RequestMethod.GET)
	public String getData(@PathVariable("id") String id, Model model) {
		LocatableData info = LocatableData.byKey(id);

		if (info != null)
			model.addAttribute("data", info);
		else
			model.addAttribute("data", "null");

		return JSON_VIEW;
	}

	@RequestMapping(value = "/data/all", method = RequestMethod.GET)
	public String listData(Model model) {
		return listDataBookmarked(MAX_DC_LIST_RESULTS, null, model);
	}

	/**
	 * This handler can be used to page through the set of all
	 * LocationData elements.
	 * 
	 * @param max - max results per page
	 * @param bookmark - bookmark to where we left off in the data
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/data/plans/{max}/{bookmark}", method = RequestMethod.GET)
	public String listDataBookmarked(@PathVariable("max") int max,
			@PathVariable("bookmark") String bookmark, Model model) {
		if (max < 1 || max > MAX_DC_LIST_RESULTS)
			max = MAX_DC_LIST_RESULTS;

		Result<LocatableData> data = LocatableData.all(max, bookmark);

		model.addAttribute("data", data.getResults());
		model.addAttribute("bookmark", data.getBookmark());

		return JSON_VIEW;
	}

	
	/**
	 * This handler method creates a new location data with the
	 * provided name, description, lat, and lon.
	 * 
	 * You can add some data like this:
	 * http://localhost:8888/data/create?lat=37.215&lon=-80.40980000000002&name=foo&description=cs278
	 * 
	 * 
	 * @param name - name of the location data to save
	 * @param desc - description of the location data
	 * @param lat - latitude
	 * @param lon - longitude
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/data/create")
	public String reportData(@RequestParam("name") String name,
			@RequestParam("description") String desc,
			@RequestParam(value = "lat", required = false) String lat,
			@RequestParam(value = "lon", required = false) String lon,
			Model model) {

		try {
			LocatableData info = new LocatableData();
			info.setName(name);
			info.setDescription(desc);

			if (lat != null && lon != null) {
				double latitude = Double.parseDouble(lat);
				double longitude = Double.parseDouble(lon);
				info.setLocation(latitude, longitude);
			}

			info.save();
			model.addAttribute("data", info);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("data", "null");
		}

		return JSON_VIEW;
	}

}
