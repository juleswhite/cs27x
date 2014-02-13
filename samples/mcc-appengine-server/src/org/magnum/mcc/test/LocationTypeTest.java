/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.magnum.mcc.building.LocationType;

public class LocationTypeTest {

	private final LocationType rootType_ = new LocationType("root", null);
	private final LocationType hallwayType_ = new LocationType("hallway",rootType_);
	private final LocationType roomType_ = new LocationType("room", rootType_);
	private final LocationType bedroomType_ = new LocationType("bedroom", roomType_);
	private final LocationType kitchenType_ = new LocationType("kitchen", roomType_);
	private final LocationType bathroomType_ = new LocationType("bathroom", roomType_);
	
	@Test
	public void testEquals() {
		assertTrue(rootType_.equals(new LocationType("root", null)));
		
		assertEquals(hallwayType_, new LocationType("hallway", rootType_));
		assertEquals(bedroomType_, new LocationType("bedroom", roomType_));
		assertTrue(!kitchenType_.equals(bedroomType_));
		assertTrue(!kitchenType_.equals(bathroomType_));
	}

}
