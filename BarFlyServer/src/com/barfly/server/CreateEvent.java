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
 * 
 * Takes requests in the form of http://app-engine-url/createEvent?name=EVENTNAME&info=INFO&creator=NAME
 *
 */

@SuppressWarnings("serial")
public class CreateEvent extends HttpServlet {
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");
		
		if (req.getParameterMap().containsKey("name") && 
			req.getParameterMap().containsKey("info") && 
			req.getParameterMap().containsKey("creator")) {
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			String name = req.getParameter("name");
			String info = req.getParameter("info");
			String userName = req.getParameter("creator");
			
			Key eventkey = KeyFactory.createKey("Event", name);
			
			try {
				
				datastore.get(eventkey);
				resp.getWriter().println("Event "+name+" already exists");
				
			} catch (EntityNotFoundException e) {
				
				Entity event = new Entity("Event", name);
				event.setProperty("event_name", name);
				event.setProperty("info", info);
				
				List<String> attendees = new ArrayList<String>();
				if (event.hasProperty("attendees")) {
					attendees = (List<String>) event.getProperty("attendees");
				}
				if (!attendees.contains(userName)) {
					attendees.add(userName);
				}
				event.setProperty("attendees", attendees);
				
				datastore.put(event);
				resp.getWriter().println("Event Created");

			}
			
			Key userkey = KeyFactory.createKey("User", userName);
			
			try {
				
				Entity user = datastore.get(userkey);
				
				List<String> attending = new ArrayList<String>();
				
				if (user.hasProperty("attending")) {
					attending = (List<String>) user.getProperty("attending");
				}	
				if (!attending.contains(name)) {
					attending.add(name);
					user.setProperty("attending", attending);
				}
				
				datastore.put(user);
				
			} catch (EntityNotFoundException e) {
				
				resp.getWriter().println("User " + userName + " does not exist");

			}
			
		}
	}
}
