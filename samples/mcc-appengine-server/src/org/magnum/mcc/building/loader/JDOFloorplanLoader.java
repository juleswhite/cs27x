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

import javax.jdo.PersistenceManager;

import org.magnum.mcc.building.Floorplan;

import com.google.inject.Inject;

/**
 * This class uses JDO and AppEngine to persist and load
 * floorplans.
 * 
 * @author jules
 *
 */
public class JDOFloorplanLoader implements FloorplanLoader {

	private final FloorplanMarshaller marshaller_;

	@Inject
	public JDOFloorplanLoader(FloorplanMarshaller marshaller) {
		super();
		marshaller_ = marshaller;
	}

	@Override
	public Floorplan load(String id) {

		final PersistenceManager pm = getPersistenceManager();
		final StoredFloorplan sf = pm.getObjectById(StoredFloorplan.class, id);
		final String json = sf.getFloorplanJson();
		Floorplan fp = null;

		try {
			fp = marshaller_.fromString(json);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			pm.close();
		}

		return fp;
	}

	@Override
	public void save(String id, Floorplan fp) {
		final PersistenceManager pm = getPersistenceManager();
		try {
			final StoredFloorplan sf = new StoredFloorplan();
			sf.setId(id);
			final String json = marshaller_.toString(fp);
			sf.setFloorplanJson(json);
			pm.makePersistent(sf);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			pm.close();
		}
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
