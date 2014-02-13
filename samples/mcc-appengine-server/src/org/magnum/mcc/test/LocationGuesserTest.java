/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.FloorplanLocation;
import org.magnum.mcc.building.LocationType;
import org.magnum.mcc.building.guessers.GuesserStrategyFactory;
import org.magnum.mcc.building.guessers.LocationGuesserImpl;

import com.google.common.collect.ImmutableSet;

public class LocationGuesserTest {

	private final Floorplan floorplan_ = mock(Floorplan.class);
	
	private final LocationType rootType_ = new LocationType("root", null);
	
	private final GuesserStrategyFactory strategies_ = mock(GuesserStrategyFactory.class);
	
	private final LocationType hallwayType = new LocationType("hallway",rootType_);
	private final LocationType roomType = new LocationType("room", rootType_);
	private final LocationType bedroomType = new LocationType("bedroom", roomType);
	private final LocationType kitchenType = new LocationType("kitchen", roomType);
	private final LocationType bathroomType = new LocationType("bathroom", roomType);
	
	private final FloorplanLocation bedroom1 = new FloorplanLocation("bedroom1", bedroomType);
	private final FloorplanLocation bedroom2 = new FloorplanLocation("bedroom2", bedroomType);
	private final FloorplanLocation bedroom3 = new FloorplanLocation("bedroom3", bedroomType);
	private final FloorplanLocation bedroom4 = new FloorplanLocation("bedroom4", bedroomType);
	
	private final FloorplanLocation kitchen = new FloorplanLocation("kitchen", kitchenType);
	
	private final FloorplanLocation bathroom1 = new FloorplanLocation("bathroom1", bathroomType);
	private final FloorplanLocation bathroom2 = new FloorplanLocation("bathroom2", bathroomType);
	private final FloorplanLocation bathroom3 = new FloorplanLocation("bathroom3", bathroomType);
	
	private final FloorplanLocation hallway1 = new FloorplanLocation("upstairsHallway", hallwayType);
	private final FloorplanLocation hallway2 = new FloorplanLocation("downstairsHallway", hallwayType);
	
	private final Set<FloorplanLocation> bedrooms =
		       new ImmutableSet.Builder<FloorplanLocation>()
		           .add(bedroom1, bedroom2, bedroom3, bedroom4)
		           .build();
	
	private final Set<FloorplanLocation> bathrooms =
		       new ImmutableSet.Builder<FloorplanLocation>()
		           .add(bathroom1, bathroom2, bathroom3)
		           .build();
	
	private final Set<FloorplanLocation> kitchens =
		       new ImmutableSet.Builder<FloorplanLocation>()
		           .add(kitchen)
		           .build();
	
	private final Set<FloorplanLocation> hallways =
		       new ImmutableSet.Builder<FloorplanLocation>()
		           .add(hallway1, hallway2)
		           .build();
	
	private final Set<FloorplanLocation> allRooms =
		       new ImmutableSet.Builder<FloorplanLocation>()
		           .addAll(bedrooms)
		           .addAll(bathrooms)
		           .addAll(kitchens)
		           .build();
	
	private final Set<FloorplanLocation> all =
		       new ImmutableSet.Builder<FloorplanLocation>()
		           .addAll(bedrooms)
		           .addAll(bathrooms)
		           .addAll(kitchens)
		           .addAll(hallways)
		           .build();
	
	
	@Before
	public void setUp(){
		when(floorplan_.getLocations()).thenReturn(all);
		when(floorplan_.getLocationsOfType(rootType_)).thenReturn(all);
		when(floorplan_.getLocationsOfType(roomType)).thenReturn(allRooms);
		when(floorplan_.getLocationsOfType(bathroomType)).thenReturn(bathrooms);
		when(floorplan_.getLocationsOfType(bedroomType)).thenReturn(bedrooms);
		when(floorplan_.getLocationsOfType(hallwayType)).thenReturn(hallways);
		when(floorplan_.getLocationsOfType(kitchenType)).thenReturn(kitchens);
	}
	
	@Test
	public void testBathroomLocation() {
		LocationGuesserImpl locator = new LocationGuesserImpl(strategies_, floorplan_, rootType_);
		
		assertThat(locator.getPossibleLocations(), is(all));
		
		locator.narrowLocationsByType(roomType);
		assertThat(locator.getPossibleLocations(), is(allRooms));
		assertThat(locator.getPossibleLocationTypes(), is(roomType.getChildren()));
		
		locator.narrowLocationsByType(bathroomType);
		assertThat(locator.getPossibleLocations(), is(bathrooms));
		
		locator.narrowLocations(bathroom2);
		assertThat(locator.getPossibleLocations().size(), is(1));
		assertTrue(locator.located());
		assertEquals(bathroom2, locator.getLocation());
	}
	
	@Test
	public void testHallway11Location() {
		LocationGuesserImpl locator = new LocationGuesserImpl(strategies_, floorplan_, rootType_);
		
		assertThat(locator.getPossibleLocations(), is(all));
		
		locator.narrowLocationsByType(hallwayType);
		assertThat(locator.getPossibleLocations(), is(hallways));
		
		locator.narrowLocations(hallway2);
		assertThat(locator.getPossibleLocations().size(), is(1));
		assertTrue(locator.located());
		assertEquals(hallway2, locator.getLocation());
	}
	
	@Test
	public void testSetNarrowingLocation() {
		LocationGuesserImpl locator = new LocationGuesserImpl(strategies_, floorplan_, rootType_);
		
		assertThat(locator.getPossibleLocations(), is(all));
		
		locator.narrowLocations(hallway1, bedroom1, bedroom2);
		assertThat(locator.getPossibleLocationTypes(), is(rootType_.getChildren()));
		
		locator.narrowLocationsByType(hallwayType);
		assertThat(locator.getPossibleLocations().size(), is(1));
		assertTrue(locator.located());
		assertEquals(hallway1, locator.getLocation());
	}
	
	
	@Test
	public void testBedroom1Location() {
		LocationGuesserImpl locator = new LocationGuesserImpl(strategies_, floorplan_, rootType_);
		
		assertThat(locator.getPossibleLocations(), is(all));
		
		locator.narrowLocationsByType(roomType);
		assertThat(locator.getPossibleLocations(), is(allRooms));
		
		locator.narrowLocationsByType(bedroomType);
		assertThat(locator.getPossibleLocations(), is(bedrooms));
		
		locator.narrowLocations(bedroom4);
		assertThat(locator.getPossibleLocations().size(), is(1));
		assertTrue(locator.located());
		assertEquals(bedroom4, locator.getLocation());
	}

}
