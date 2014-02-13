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
import java.util.List;

import org.magnum.mcc.building.FloorplanLocation;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * A guesser strategy that uses a list of possible locations provided by the user
 * to narrow down where they could be (e.g., the user selected one or more locations
 * on a list of rooms or map). 
 * 
 * @author jules
 *
 */
public class UserLocationsFeedbackGuesser implements GuesserStrategy {
	
	public static final MeasurementType TYPE = MeasurementType.forName("user-locations");
	
	@Override
	public void guess(final LocationGuesser guesser, Measurements ms) {
		
		for(Measurement m : ms.getMeasurements()){
			if(TYPE.equals(m.getType())){
				String[] values = m.getValues();
				if(values != null && values.length > 0){
					List<FloorplanLocation> possible = Lists.transform(Arrays.asList(values), new Function<String, FloorplanLocation>() {
						public FloorplanLocation apply(String id){
							return guesser.getFloorplan().locationById(id);
						}
					});
					guesser.narrowLocations(new HashSet<FloorplanLocation>(possible));
				}
			}
		}
	}

}
