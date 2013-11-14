/*************************************************************************
* Copyright 2010 Jules White
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

package org.clear.server.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.BoundingBox;
import com.beoui.geocell.model.CostFunction;
import com.beoui.geocell.model.LocationCapable;
import com.beoui.geocell.model.Point;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.datanucleus.store.appengine.query.JDOCursorHelper;

@PersistenceCapable
public class LocatableData implements LocationCapable {

	public static final String ANYWHERE = "ANYWHERE";
	public static final String IN_RANGE = "INRANGE";

	public static List<LocatableData> nearby(double lat, double lon,
			int maxresults, double maxdist) {
		Point center = new Point(lat, lon);
		PersistenceManager pm = PMF.get().getPersistenceManager();// here put
		// your
		// persistent
		// manager
		
		List<LocatableData> objects = null;
		try {
			objects = GeocellManager.proximityFetch(center, maxresults,
					maxdist, LocatableData.class, null, pm);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return objects;
	}

	@SuppressWarnings("unchecked")
	public static List<LocatableData> inBoundingBox(double latSW,
			double lonSW, double latNE, double lonNE) {

		// Transform this to a bounding box
		BoundingBox bb = new BoundingBox(latNE, lonNE, latSW, lonSW);

		// Calculate the geocells list to be used in the queries (optimize list
		// of cells that complete the given bounding box)
		List<String> cells = GeocellManager.bestBboxSearchCells(bb, null);

		// OR if you want to use a custom "cost function"
		List<String> cells2 = GeocellManager.bestBboxSearchCells(bb,
				new CostFunction() {

					public double defaultCostFunction(int numCells,
							int resolution) {
						if (numCells > 100) {
							return Double.MAX_VALUE;
						} else {
							return 0;
						}
					}
				});
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String queryString = "select from " + LocatableData.class
				+ " where geocells.contains(geocellsv)";
		Query query = pm.newQuery(queryString);
		// query.declareParameters("String geocells");
		query.declareParameters("String geocellsv");
		List<LocatableData> objects = (List<LocatableData>) query
				.execute(cells);
		return objects;
	}

	@SuppressWarnings("unchecked")
	public static Result<LocatableData> all(int max, String bookmark) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<LocatableData> results = null;
		Query query = pm.newQuery(LocatableData.class);
		query.setRange(0, max);

		if (bookmark != null) {
			Cursor cursor = Cursor.fromWebSafeString(bookmark);
			Map<String, Object> extensionMap = new HashMap<String, Object>();
			extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
			query.setExtensions(extensionMap);
		}
		results = (List<LocatableData>) query.execute();

		return new Result<LocatableData>(results);
	}

	public static LocatableData byKey(String key) {
		LocatableData e = null;

		try {
			Key k = KeyFactory.stringToKey(key);
			e = PMF.get().getPersistenceManager().getObjectById(
					LocatableData.class, k);
		} catch (Exception ex) {
		}
		return e;
	}

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String name;

	@Persistent
	private String description;

	@Persistent
	private double latitude;

	@Persistent
	private double longitude;

	@Persistent
	private double maxDistance;

	@Persistent
	private List<String> geocells;

	public LocatableData() {
	}

	@JsonIgnore
	public Key getKey() {
		return key;
	}

	@JsonIgnore
	public void setKey(Key key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void save() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(this);
		pm.close();
	}

	public String getId() {
		return KeyFactory.keyToString(getKey());
	}

	public Point getLocation() {
		return new Point(getLatitude(), getLongitude());
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
		updateGeocells();
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
		updateGeocells();
	}

	public void setLocation(double lat, double lon) {
		latitude = lat;
		longitude = lon;
		updateGeocells();
	}

	@JsonIgnore
	public List<String> getGeocells() {
		return geocells;
	}

	public double getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(double maxDistance) {
		this.maxDistance = maxDistance;
	}

	@JsonIgnore
	public void setGeocells(List<String> geocells) {
		this.geocells = geocells;
	}

	public void updateGeocells() {
		// Transform it to a point
		Point p = new Point(getLatitude(), getLongitude());

		// Generates the list of GeoCells
		List<String> cells = GeocellManager.generateGeoCell(p);

		setGeocells(cells);
	}

	@JsonIgnore
	public String getKeyString() {
		return getId();
	}
	
	public String getUri(){
		return "/loc/question?id="+getId();
	}
}
