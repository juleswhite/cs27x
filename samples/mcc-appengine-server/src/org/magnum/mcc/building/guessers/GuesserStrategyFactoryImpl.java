/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building.guessers;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;

public class GuesserStrategyFactoryImpl implements GuesserStrategyFactory {

	private Map<MeasurementType, GuesserStrategy> strategies_ = new HashMap<MeasurementType, GuesserStrategy>();

	@Inject
	public GuesserStrategyFactoryImpl(
			Map<MeasurementType, GuesserStrategy> strategies) {
		super();
		strategies_ = strategies;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.magnum.mcc.building.guessers.GuesserStrategyFactoryI#getGuesserStrategy
	 * (org.magnum.mcc.building.guessers.MeasurementType)
	 */
	@Override
	public GuesserStrategy getGuesserStrategy(MeasurementType measurementType) {
		return strategies_.get(measurementType);
	}

}
