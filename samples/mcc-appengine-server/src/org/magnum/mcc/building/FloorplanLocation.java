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
package org.magnum.mcc.building;

import java.util.Objects;

import org.magnum.mcc.paths.Node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A location in a floorplan, such as a room, hallway, etc.
 * 
 * 
 * @author jules
 *
 */
public class FloorplanLocation extends Node {

	private final LocationType locationType_;
	
	@JsonCreator
	public FloorplanLocation(@JsonProperty("id") String id, @JsonProperty("type") LocationType type) {
		super(id);
		locationType_ = type;
	}

	public LocationType getLocationType() {
		return locationType_;
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getLocationType());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FloorplanLocation other = (FloorplanLocation) obj;
		if (locationType_ == null) {
			if (other.locationType_ != null)
				return false;
		} else if (!locationType_.equals(other.locationType_))
			return false;
		return true;
	}

	
}
