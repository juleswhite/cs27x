package org.clear.server.data;

import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * 
 * An example of a persistence capable class that can be stored in the
 * datastore.
 * 
 * @author jules
 * 
 */

@PersistenceCapable
public class Friends {

	// Every persistent object needs a primary key.
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	// All member variables that we want to persist
	// need to be annotated with @Persistent
	@Persistent
	private Set<String> friendIds;

	@Persistent
	private String user;

	public void save(PersistenceManager pm) {
		// To save an object annotated with @PersistenceCapable,
		// you just need to ask the PersistenceManager to make it
		// persistent (e.g., store it).
		pm.makePersistent(this);
	}

	public Set<String> getFriendIds() {
		return friendIds;
	}

	public void setFriendIds(Set<String> friendIds) {
		this.friendIds = friendIds;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String myId) {
		this.user = myId;
	}

	@JsonIgnore
	public Key getKey() {
		return key;
	}

	@JsonIgnore
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * You can either put these types of query operations directly in your
	 * controller or as simple utility functions like this on the class.
	 * 
	 * 
	 * @param key
	 * @return
	 */
	public static Friends byKey(String key, PersistenceManager pm) {
		Friends friends = null;

		try {
			// Fetching a specific object by key
			Key k = KeyFactory.stringToKey(key);
			friends = pm.getObjectById(Friends.class, k);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return friends;
	}

	public static Friends byUser(String user, PersistenceManager pm) {

		List<Friends> results = null;

		// We can declare a query that finds stored objects with
		// member variables that meet a specific criteria. This is
		// roughly equivalent to looping over a list of Java Friends objects
		// and finding the subset where getUser().equals(u). For security
		// purposes, we declare the type of the parameter "u".
		Query query = pm.newQuery("select from " + Friends.class.getName()
				+ " where user==u");
		query.declareParameters("String u");

		// When the query is executed, we pass in a value for every parameter.
		// In this case, we are binding a value for "u".
		results = (List<Friends>) query.execute(user);

		return (results != null && results.size() == 1) ? results.get(0) : null;
	}

}
