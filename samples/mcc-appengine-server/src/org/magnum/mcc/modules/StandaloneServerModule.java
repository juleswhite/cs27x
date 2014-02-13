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
package org.magnum.mcc.modules;

import org.magnum.mcc.building.guessers.GuesserStrategy;
import org.magnum.mcc.building.guessers.GuesserStrategyFactory;
import org.magnum.mcc.building.guessers.GuesserStrategyFactoryImpl;
import org.magnum.mcc.building.guessers.LocationGuesserService;
import org.magnum.mcc.building.guessers.LocationGuesserServiceImpl;
import org.magnum.mcc.building.guessers.MeasurementType;
import org.magnum.mcc.building.guessers.UserLocationTypeFeedbackGuesser;
import org.magnum.mcc.building.guessers.UserLocationsFeedbackGuesser;
import org.magnum.mcc.building.loader.FloorplanJsonMarshaller;
import org.magnum.mcc.building.loader.FloorplanLoader;
import org.magnum.mcc.building.loader.FloorplanMarshaller;
import org.magnum.mcc.building.loader.JDOFloorplanLoader;
import org.magnum.mcc.paths.Dijkstra;
import org.magnum.mcc.paths.ShortestPathSolver;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

/**
 * A module implementation for Google Guice so that it can be used
 * for dependency injection in the NavController. Ideally, this class
 * should go away and Spring depenency injection should be used instead
 * of Guice.
 * 
 * @author jules
 *
 */
public class StandaloneServerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(LocationGuesserService.class).to(LocationGuesserServiceImpl.class);
		bind(GuesserStrategyFactory.class).to(GuesserStrategyFactoryImpl.class);
		bind(FloorplanLoader.class).to(JDOFloorplanLoader.class);
		bind(FloorplanMarshaller.class).to(FloorplanJsonMarshaller.class);
		bind(ShortestPathSolver.class).to(Dijkstra.class);

		// Configure the GuesserStrategies
		MapBinder<MeasurementType, GuesserStrategy> mapbinder = MapBinder
				.newMapBinder(binder(), MeasurementType.class,
						GuesserStrategy.class);
		mapbinder.addBinding(UserLocationsFeedbackGuesser.TYPE).toInstance(
				new UserLocationsFeedbackGuesser());
		mapbinder.addBinding(UserLocationTypeFeedbackGuesser.TYPE).toInstance(
				new UserLocationTypeFeedbackGuesser());

	}

}
