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

/**
 * 
 * Converts a floorplan to a String for storage. 
 * 
 * @author jules
 *
 */
public interface FloorplanMarshaller {

	public String toString(Floorplan fp) throws Exception ;
	
	public Floorplan fromString(String fp) throws Exception ;
	
}
