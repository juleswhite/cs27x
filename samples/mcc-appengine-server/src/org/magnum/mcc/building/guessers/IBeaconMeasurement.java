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

import static com.google.common.base.Preconditions.checkArgument;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

public class IBeaconMeasurement {
	private final double distance_;
	private final String id_;

	@JsonCreator
	public IBeaconMeasurement(@JsonProperty("id") String id,
			@JsonProperty("distance") double distance) {
		checkArgument(id != null, "An iBeacon Measurement must contain a non-null id.");
		
		id_ = id;
		distance_ = distance;
	}

	public IBeaconMeasurement(Measurement m) {
		super();
		checkArgument(
				iBeaconGuesser.TYPE.equals(m.getType()),
				"Cannot create a BeaconMeasurement from a Measurement that is not for an iBeacon.");
		checkArgument(
				m.getValues().length > 1,
				"An iBeacon Measurement must contain at least an id (value[0]) and a distance estimation (value[1]).");
		checkArgument(m.getValues()[0] != null,
				"An iBeacon Measurement must contain a non-null id at value[0].");
		checkArgument(
				m.getValues()[1] != null,
				"An iBeacon Measurement must contain a non-null numeric distance estimation at value[1].");

		id_ = m.getValues()[0];
		distance_ = Double.parseDouble(m.getValues()[1]);
	}

	public double getDistance() {
		return distance_;
	}

	public String getId() {
		return id_;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id_, distance_);
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof IBeaconMeasurement)
				&& ((IBeaconMeasurement) obj).id_.equals(id_)
				&& ((IBeaconMeasurement) obj).distance_ == distance_;
	}

	public String toString(){
		return Objects.toStringHelper(getClass()).add("id", getId()).add("distance", getDistance()).toString();
	}
}
