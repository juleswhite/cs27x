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

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.FloorplanLocation;
import org.magnum.mcc.building.LocationType;

/**
 * This class converts Floorplans to Json and Json to Floorplans. Although it
 * would have been theoretically possible to use an object mapping library, such
 * as Jackson, the internal structure of FloorplanLocation and DirectedGraph
 * make using Jackson non-trivial. The simplest solution (for now) is to use the
 * hand-written marshalling code below.
 * 
 * 
 * @author jules
 * 
 */
public class FloorplanJsonMarshaller implements FloorplanMarshaller {

	@Override
	public String toString(Floorplan fp) throws Exception {
		return floorplanToJson(fp).toJSONString();
	}

	@Override
	public Floorplan fromString(String fp) throws Exception {
		return jsonToFloorplan(fp);
	}

	private String locationTypeName(LocationType type) {
		return (type != null) ? type.getName() : null;
	}

	@SuppressWarnings("unchecked")
	private JSONObject locationTypeToJson(LocationType type) {
		final JSONObject typejson = new JSONObject();
		typejson.put("name", type.getName());
		typejson.put("parent", locationTypeName(type.getParent()));
		final JSONArray children = new JSONArray();
		for (LocationType child : type.getChildren()) {
			children.add(locationTypeToJson(child));
		}
		typejson.put("children", children);
		return typejson;
	}

	private LocationType jsonToLocationType(JSONObject type,
			Map<String, LocationType> types) {
		final String name = "" + type.get("name");

		final LocationType parent = types.get(type.get("parent"));
		final LocationType ltype = new LocationType(name, parent);
		types.put(name, ltype);

		final JSONArray children = (JSONArray) type.get("children");
		for (int i = 0; i < children.size(); i++) {
			final JSONObject child = (JSONObject) children.get(i);
			jsonToLocationType(child, types);
		}

		return ltype;
	}

	@SuppressWarnings("unchecked")
	private JSONObject locationToJson(FloorplanLocation loc) {
		final JSONObject locjson = new JSONObject();
		locjson.put("id", loc.getId());
		locjson.put("type", locationTypeName(loc.getLocationType()));
		return locjson;
	}

	private FloorplanLocation jsonToLocation(JSONObject loc, Floorplan fp,
			Map<String, LocationType> types) {
		final String id = "" + loc.get("id");
		final String typestr = "" + loc.get("type");
		final LocationType type = types.get(typestr);
		return fp.addLocation(id, type);
	}

	@SuppressWarnings("unchecked")
	private JSONObject edgeToJson(FloorplanLocation start,
			FloorplanLocation end, double length) {
		final JSONObject edge = new JSONObject();
		edge.put("start", start.getId());
		edge.put("end", end.getId());
		edge.put("length", length);
		return edge;
	}

	private void jsonToEdge(Floorplan floorplan, JSONObject edge) {
		final FloorplanLocation start = floorplan
				.locationById("" + edge.get("start"));
		final FloorplanLocation end = floorplan.locationById("" + edge.get("end"));
		final double length = Double.parseDouble("" + edge.get("length"));
		floorplan.connectsToOneWay(start, end, length);
	}

	@SuppressWarnings("unchecked")
	public JSONObject floorplanToJson(Floorplan fp) {
		final JSONObject fpjson = new JSONObject();

		final JSONObject typeTree = locationTypeToJson(fp.getRootType());
		fpjson.put("rootType", typeTree);

		final JSONArray locations = new JSONArray();
		for (FloorplanLocation loc : fp.getLocations()) {
			locations.add(locationToJson(loc));
		}
		fpjson.put("locations", locations);

		final JSONArray edgesjson = new JSONArray();
		for (FloorplanLocation loc : fp.getLocations()) {
			Map<FloorplanLocation, Double> edges = fp.getEdgesFrom(loc);
			for (FloorplanLocation end : edges.keySet()) {
				edgesjson.add(edgeToJson(loc, end, edges.get(end)));
			}
		}
		fpjson.put("edges", edgesjson);

		return fpjson;
	}

	public Floorplan jsonToFloorplan(String json) throws Exception {
		final JSONParser parser = new JSONParser();
		final JSONObject obj = (JSONObject) parser.parse(json);
		return jsonToFloorplan(obj);
	}

	public Floorplan jsonToFloorplan(JSONObject fpjson) {
		final JSONObject roottypejson = (JSONObject) fpjson.get("rootType");
		final Map<String, LocationType> types = new HashMap<String, LocationType>();
		final LocationType root = jsonToLocationType(roottypejson, types);

		final Floorplan fp = new Floorplan(root);
		final JSONArray locjson = (JSONArray) fpjson.get("locations");
		for (int i = 0; i < locjson.size(); i++) {
			JSONObject json = (JSONObject) locjson.get(i);
			jsonToLocation(json, fp, types);
		}

		final JSONArray edgesjson = (JSONArray) fpjson.get("edges");
		for (int i = 0; i < edgesjson.size(); i++) {
			JSONObject edge = (JSONObject) edgesjson.get(i);
			jsonToEdge(fp, edge);
		}

		return fp;
	}

}
