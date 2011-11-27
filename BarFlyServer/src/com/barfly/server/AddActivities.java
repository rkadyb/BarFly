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
 * Adds activities to an Event
 * Takes requests in the form of http://app-engine-url/addActivities?name=EVENTNAME&activities=ACT1,ACT2,ACT3, ... 
 *
 * Individual activities are stored as Name:Info:Location where Location is optional
 */

@SuppressWarnings("serial")
public class AddActivities extends HttpServlet {
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");
		
		if (req.getParameterMap().containsKey("name") && 
			req.getParameterMap().containsKey("activities")) {
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			String name = req.getParameter("name");
			String activitiesInput = req.getParameter("attending");
			String[] activitiesList = activitiesInput.split(",");
			
			Key key = KeyFactory.createKey("Event", name);
			
			try {
				
				Entity event = datastore.get(key);
				List<String> activities = new ArrayList<String>();
				
				if (event.hasProperty("activities")) {
					activities = (List<String>) event.getProperty("activities");
				}
				
				for (String activity: activitiesList) {
					
					if (!activities.contains(activity)) {
						activities.add(activity);
					}
				}
				
				event.setProperty("activities", activities);
				datastore.put(event);
				
				resp.getWriter().println("Activities for Event "+name+ " updated");
				
			} catch (EntityNotFoundException e) {
								
				resp.getWriter().println("Event "+name+" does not exist");

			}
			
		}
	}
}
