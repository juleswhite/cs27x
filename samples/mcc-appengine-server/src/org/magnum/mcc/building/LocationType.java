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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.magnum.soda.proxy.SodaByValue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A semantic tag for a FloorplanLocation that indicates what type it is
 * (e.g., "bathroom"). Each type can have a parent (e.g., "room") and
 * children (e.g. bathroom is a child of room).
 * 
 * @author jules
 *
 */
@SodaByValue
public class LocationType {

	private final LocationType parent_;

	private final Set<LocationType> children_ = new HashSet<LocationType>();

	private final String name_;

	@JsonCreator
	public LocationType(@JsonProperty("name") String name, @JsonProperty("parent") LocationType parent) {
		super();
		name_ = name;
		parent_ = parent;

		if (parent != null) {
			parent_.getChildren().add(this);
		}
	}

	public LocationType getParent() {
		return parent_;
	}

	@JsonIgnore
	public Set<LocationType> getChildren() {
		return children_;
	}

	public String getName() {
		return name_;
	}
	
	public String toString(){
		return getName();
	}

	@Override
	public int hashCode() {
		return Objects.hash(name_, parent_);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof LocationType)) {
			return false;
		} else {
			LocationType lt = (LocationType) obj;
			return lt.getName().equals(getName())
					&& ((lt.getParent() == null && getParent() == null)
					|| lt.getParent().getName().equals(getParent().getName()));
		}
	}

}
