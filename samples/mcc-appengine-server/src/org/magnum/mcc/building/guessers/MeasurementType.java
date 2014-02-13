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

import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

/**
 * Represents a type of measurement that can be taken from a device to help
 * narrow down the location of the user.
 * 
 * @author jules
 * 
 */
public class MeasurementType {

	private static final Map<String, MeasurementType> types_ = new WeakHashMap<String, MeasurementType>();
	
	public static MeasurementType forName(String name){
		MeasurementType type = types_.get(name);
		if(type == null){
			type = new MeasurementType(name);
			types_.put(name,type);
		}
		return type;
	}
	
	private final String typeName_;

	private MeasurementType(String typeName) {
		super();
		typeName_ = typeName;
	}

	public String getTypeName() {
		return typeName_;
	}

	/**
	 * This method returns true if the provided typename represents the same
	 * type as the the MeasurementType object.
	 * 
	 * @param typename
	 * @return
	 */
	public boolean sameType(String typename) {
		return typeName_.equals(typename);
	}

	@Override
	public int hashCode() {
		return Objects.hash(typeName_);
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof MeasurementType) ? Objects.equals(
				((MeasurementType) obj).getTypeName(), getTypeName()) : false;
	}

}
