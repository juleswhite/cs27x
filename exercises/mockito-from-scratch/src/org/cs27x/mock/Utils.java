package org.cs27x.mock;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class Utils {

	private static final Map<?,?> DEFAULTS = ImmutableMap.builder()
			.put(int.class, Integer.valueOf(0))
			.put(float.class, Float.valueOf(0))
			.put(double.class, Double.valueOf(0))
			.put(long.class, Long.valueOf(0))
			.put(byte.class, Byte.MIN_VALUE)
			.put(Integer.class, Integer.valueOf(0))
			.put(Float.class, Float.valueOf(0))
			.put(Double.class, Double.valueOf(0))
			.put(Long.class, Long.valueOf(0))
			.put(Byte.class, Byte.MIN_VALUE)
			.put(boolean.class, Boolean.FALSE)
			.put(Boolean.class, Boolean.FALSE)
			.build();
	
	@SuppressWarnings("unchecked")
	public static <T> T defaultVal(Class<T> type){
		return (T)DEFAULTS.get(type);
	}
	
}
