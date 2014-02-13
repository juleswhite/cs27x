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

/**
 * A guesser strategy that uses a type of room provided by the user to
 * narrow down the list of possible locations (e.g., I am in a hallway).
 * 
 * @author jules
 *
 */
public class UserLocationTypeFeedbackGuesser implements GuesserStrategy {
	
	public static final MeasurementType TYPE = MeasurementType.forName("user-location-type");
	
	@Override
	public void guess(final LocationGuesser guesser, Measurements ms) {
		
		for(Measurement m : ms.getMeasurements()){
			if(TYPE.equals(m.getType())){
				String[] values = m.getValues();
				if(values != null && values.length > 0){
					Floorplan fp = guesser.getFloorplan();
					for(String id : values) {
						guesser.narrowLocationsByType(fp.typeByName(id));
					}
				}
			}
		}
	}

}
