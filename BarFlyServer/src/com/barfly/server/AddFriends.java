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

@SuppressWarnings("serial")
public class AddFriends extends HttpServlet {
	@SuppressWarnings({ "unchecked", "null" })
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");
		
		if (req.getParameterMap().containsKey("user") && 
			req.getParameterMap().containsKey("friend")) {
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			String username = req.getParameter("user");
			String friend = req.getParameter("friend");
			
			Key userkey = KeyFactory.createKey("User", username);
			Key friendkey = KeyFactory.createKey("User", friend);
			
			try {
				
				datastore.get(friendkey);
				Entity user = datastore.get(userkey);
				List<String> friends = new ArrayList<String>();
				if (user.hasProperty("friends")) {
					friends = (List<String>) user.getProperty("friends");
					friends.add(friend);
					user.setProperty("friends", friends);
				} else {
					friends.add(friend);
					user.setProperty("friends", friends);
				}
				datastore.put(user);
				
				resp.getWriter().println("Friend "+friend+" added to User "+username);
				
			} catch (EntityNotFoundException e) {
				
				resp.getWriter().println("User missing");

			}
			
		}
	}
}
