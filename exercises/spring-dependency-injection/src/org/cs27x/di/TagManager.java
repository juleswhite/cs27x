package org.cs27x.di;

import java.util.List;
import java.util.Set;

/**
 * This is an interface for a class that manages associations
 * of tags to photos. 
 * 
 * @author jules
 *
 */
public interface TagManager {

	/**
	 * Associates the given tag with the specified photo.
	 * 
	 * @param photo
	 * @param tag
	 */
	public void addTag(String photoId, String tag);
	
	/**
	 * Removes the association between the given tag and photo.
	 * If no photos use the tag anymore, it should not be returned
	 * by the listTags() method.
	 * 
	 * @param photo
	 * @param tag
	 */
	public void removeTag(String photoId, String tag);
	
	/**
	 * Returns the list of all tags currently associated with at least
	 * one photo.
	 * 
	 * @param photo
	 * @param tag
	 */
	public Set<String> listTags();
	
	/**
	 * 
	 * Searches through the list of stored photo ids and returns any
	 * that have the specified tag.
	 * 
	 * @param tags
	 * @return
	 */
	public List<String> listPhotosWithTag(String tag);
	
}
