package com.barfly.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Takes requests of the form http://app-engine-url/addFriends?user=USERNAME&friends=FRIEND1,FRIEND2 ...
 */

@SuppressWarnings("serial")
public class AddFriends extends HttpServlet {
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");
		
		if (req.getParameterMap().containsKey("user") && 
			req.getParameterMap().containsKey("friends")) {
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			String username = req.getParameter("user");
			String friendsInput = req.getParameter("friends");
			String[] friendsList = friendsInput.split(",");
			
			Key userkey = KeyFactory.createKey("User", username);
			Key friendkey;
			
			try {
				
				Entity user = datastore.get(userkey);
				List<String> friends = new ArrayList<String>();
				if (user.hasProperty("friends")) {
					friends = (List<String>) user.getProperty("friends");
				}
					
				for (String friend: friendsList) {
					if (friends.contains(friend)) {
						resp.getWriter().println(friend+" is alread a Friend of "+username);
					} else {
						friendkey = KeyFactory.createKey("User", friend);
						try {
							datastore.get(friendkey);
							friends.add(friend);
						} catch (EntityNotFoundException e) {
							resp.getWriter().println("User "+friend+" does not exist");
						}
					}
				}
					
				user.setProperty("friends", friends);
				datastore.put(user);
				resp.getWriter().println("Friends added to User "+username);

			} catch (EntityNotFoundException e) {
				
				resp.getWriter().println("User "+username+" missing");
				
			}
			
		}
	}
}
