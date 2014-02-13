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
import java.util.Iterator;

/**
 * This class represents a list of measurements taken on a client device
 * that can aid in localizing the user.
 * 
 * @author jules
 *
 */
public class Measurements implements Iterable<Measurement>{

	private Measurement[] measurements_;

	public Measurement[] getMeasurements() {
		return measurements_;
	}

	public void setMeasurements(Measurement[] measurements) {
		measurements_ = measurements;
	}

	@Override
	public Iterator<Measurement> iterator() {
		return Arrays.asList(measurements_).iterator();
	}

}
