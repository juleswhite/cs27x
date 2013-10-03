package org.cs27x.di;

import java.util.List;

import org.cs27x.di.model.Photo;

/**
 * An interface for a storage system for photos.
 * 
 * @author jules
 *
 */
public interface PhotoStorage {

	/**
	 * Returns the DataStore currently in use by the
	 * PhotoStorage implementation.
	 * 
	 * @return
	 */
	public DataStore getDataStore();
	
	/**
	 * Sets the DataStore that should be used by the
	 * PhotoStorage implementation
	 * 
	 * @param ds
	 */
	public void setDataStore(DataStore ds);
	
	/**
	 * This method stores a photo and returns a unique
	 * ID that can be used to retrieve that photo.
	 * 
	 * @param p
	 * @return
	 */
	public Photo storePhoto(byte[] imgdata);
	
	
	/**
	 * This method returns the photo associated with the
	 * given ID.
	 * 
	 * @param id
	 * @return
	 */
	public Photo getPhotoByID(String id);
	
	/**
	 * Returns the list of all photos in the store.
	 * 
	 * @return
	 */
	public List<Photo> listPhotos();
	
}
