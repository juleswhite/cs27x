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
package org.magnum.mcc.building.loader;

import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.FloorplanLocation;
import org.magnum.mcc.building.LocationType;

/**
 * This class can be edited, recompiled, and run to spit out the correct json format
 * for a given floorplan.
 * 
 * @author jules
 *
 */
public class FloorplanEditorTool {

	public static void main(String[] args) throws Exception {
		LocationType rootType = new LocationType("root", null);
		LocationType hallwayType = new LocationType("hallway", rootType);
		LocationType roomType = new LocationType("room", rootType);
		LocationType bedroomType = new LocationType("bedroom", roomType);
		LocationType kitchenType = new LocationType("kitchen", roomType);
		LocationType bathroomType = new LocationType("bathroom", roomType);

		Floorplan floorPlan = new Floorplan(rootType);

		FloorplanJsonMarshaller marshaller = new FloorplanJsonMarshaller();

		FloorplanLocation bedroom1 = floorPlan.addLocation("bedroom1",
				bedroomType);
		FloorplanLocation bedroom2 = floorPlan.addLocation("bedroom2",
				bedroomType);
		FloorplanLocation bedroom3 = floorPlan.addLocation("bedroom3",
				bedroomType);
		floorPlan.addLocation("bedroom4", bedroomType);
		floorPlan.addLocation("kitchen", kitchenType);
		FloorplanLocation bathroom1 = floorPlan.addLocation("bathroom1",
				bathroomType);
		floorPlan.addLocation("bathroom2", bathroomType);
		floorPlan.addLocation("bathroom3", bathroomType);
		FloorplanLocation uhall = floorPlan.addLocation("upstairsHallway",
				hallwayType);
		floorPlan.addLocation("downstairsHallway", hallwayType);

		floorPlan.connectsTo(bathroom1, bedroom1, 1);
		floorPlan.connectsTo(bedroom1, uhall, 1);
		floorPlan.connectsTo(bedroom2, uhall, 1);
		floorPlan.connectsTo(bedroom3, uhall, 1);

		String output = marshaller.toString(floorPlan);
		System.out.println(output);
	}

}
