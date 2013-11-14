/*************************************************************************
 * Copyright 2010 Jules White
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

package org.clear.server.controllers;

import java.util.HashSet;

import javax.jdo.PersistenceManager;

import org.clear.server.data.Friends;
import org.clear.server.data.PMF;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FriendsController {

	private static final String JSON_VIEW = "jsonView";
	public static final int MAX_DC_LIST_RESULTS = 25;

	/**
	 * 
	 * @param key
	 *            - the key of the Friends object to query for.
	 * @param model
	 *            - this Map is converted to JSON and returned to the client.
	 * @return
	 */
	@RequestMapping(value = "/data/friends/find", method = RequestMethod.GET)
	public String myFriends(@RequestParam("user") String user, Model model) {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Friends friends = Friends.byUser(user, pm);

		if (friends != null)
			model.addAttribute("data", friends);
		else
			model.addAttribute("data", "null");

		return JSON_VIEW;
	}

	/**
	 * Do a proximity search for LocationData near a lat/lon coordinate.
	 * 
	 * @param lat
	 * @param lon
	 * @param maxresults
	 * @param maxdist
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/data/friends/add", method = RequestMethod.GET)
	public String addFriend(@RequestParam("user") String user,
			@RequestParam("friend") String friend, Model model) {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		Friends friends = Friends.byUser(user,pm);

		if (friends == null) {
			friends = new Friends();
			friends.setFriendIds(new HashSet<String>());
			friends.setUser(user);
		}

		friends.getFriendIds().add(friend);
		friends.save(pm);

		
		pm.close();
		
		model.addAttribute("data", true);

		return JSON_VIEW;
	}

}
