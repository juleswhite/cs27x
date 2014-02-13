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

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Lob;

import com.google.appengine.api.datastore.Text;

/**
 * A persistent object for storing marshalled json floorplans on AppEngine.
 * 
 * @author jules
 *
 */

@PersistenceCapable
public class StoredFloorplan {

	@PrimaryKey
	private String id;

	
	// We use a Text here rather than a String because AppEngine limits
	// persisted Strings to 500 characters.
	@Persistent
	@Lob
	private Text floorplanJson;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFloorplanJson() {
		return floorplanJson.getValue();
	}

	public void setFloorplanJson(String floorplanJson) {
		this.floorplanJson = new Text(floorplanJson);
	}

}
