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

/**
 * An edge connecting two locations in a floorplan representing
 * a "can get from start to end" relationship (e.g., I can walk from
 * room A to room B). 
 * 
 * @author jules
 *
 */
public class FloorplanLocationEdge {

	private final FloorplanLocation start_;

	private final FloorplanLocation end_;

	private final long length_;

	public FloorplanLocationEdge(FloorplanLocation start,
			FloorplanLocation end, boolean isBidirectional, long length) {
		super();
		start_ = start;
		end_ = end;
		length_ = length;
	}

	public FloorplanLocation getStart() {
		return start_;
	}

	public FloorplanLocation getEnd() {
		return end_;
	}

	public long getLength() {
		return length_;
	}

}
