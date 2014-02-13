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

import java.util.HashSet;
import java.util.Set;

import org.magnum.mcc.building.Floorplan;

public class iBeaconGuesser implements GuesserStrategy {

	public static MeasurementType TYPE = MeasurementType.forName("ibeacon");

	@Override
	public void guess(LocationGuesser g, Measurements ms) {
		for (Measurement m : ms) {

			Set<IBeaconMeasurement> beacons = new HashSet<IBeaconMeasurement>();

			if (TYPE.equals(m.getType())) {
				beacons.add(new IBeaconMeasurement(m));
			}

			Floorplan fp = g.getFloorplan();
			// Do something here to guess the location based on the beacon
			// measurements. 
		}
	}
}
