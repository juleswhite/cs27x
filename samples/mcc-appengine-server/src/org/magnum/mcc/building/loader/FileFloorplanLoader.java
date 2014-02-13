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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.magnum.mcc.building.Floorplan;

import com.google.inject.Inject;

/**
 * This class is not used on AppEngine. This class is here simply for
 * possible offline testing if needed.
 * 
 * @author jules
 *
 */
public class FileFloorplanLoader implements FloorplanLoader {

	private final FloorplanMarshaller marshaller_;

	private final String floorPlanDir_ = "./resources/floorplans/";

	@Inject
	public FileFloorplanLoader(FloorplanMarshaller marshaller) {
		marshaller_ = marshaller;
	}

	@Override
	public Floorplan load(String id) {
		Floorplan fp = null;

		final File f = findFloorplanFile(id);

		if (f.exists()) {
			FileInputStream fin = null;
			try {
				fin = new FileInputStream(f);
				fp = load(fin);
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				if (fin != null) {
					try {
						fin.close();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}

		return fp;
	}

	private File findFloorplanFile(String id) {
		return new File(floorPlanDir_ + id + ".floorplan");
	}

	private Floorplan load(InputStream in) throws Exception {
		String json = IOUtils.toString(in);
		return marshaller_.fromString(json);
	}

	@Override
	public void save(String id, Floorplan fp) {
		//Won't compile in an AppEngine project -- but is valid Java
//		File f = findFloorplanFile(id);
//		FileOutputStream fout = null;
//		try{
//			fout = new FileOutputStream(f);
//			String json = marshaller_.toString(fp);
//			IOUtils.write(json, f);
//		}catch(Exception e){
//			throw new RuntimeException(e);
//		}finally{
//			try{
//				fout.close();
//			}catch(Exception e){
//				throw new RuntimeException(e);
//			}
//		}
	}

}
