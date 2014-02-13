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

/**
 * A single measurement or user feedback taken from a client device. A
 * measurement can represent a user selection (e.g., the user chose a
 * current location on the map) or a sensor measurement (e.g., an iBeacon
 * distance). The format of the values is dictated by the type of measurement.
 * 
 * @author jules
 *
 */
public class Measurement {

	private MeasurementType type_;

	private String[] values_;

	public MeasurementType getType() {
		return type_;
	}

	public void setType(MeasurementType type) {
		type_ = type;
	}

	public String[] getValues() {
		return values_;
	}

	public void setValues(String[] value) {
		values_ = value;
	}

}
