package org.cs27x.di;

public interface DataStore {

	/**
	 * This method should store the provided data and associate
	 * it with the given data.
	 * 
	 * @param id
	 * @param data
	 */
	public void store(String id, byte[] data);
	
	/**
	 * This method should return the data associated with the
	 * given ID.
	 * 
	 * @param id
	 * @return
	 */
	public byte[] get(String id);
	
}
