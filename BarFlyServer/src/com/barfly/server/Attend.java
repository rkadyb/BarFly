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
 * Takes requests in the form of http://app-engine-url/attend?name=USER&attending=EVENT1,EVENT2,EVENT3 
 *
 */

@SuppressWarnings("serial")
public class Attend extends HttpServlet {
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");
		
		if (req.getParameterMap().containsKey("name") && 
			req.getParameterMap().containsKey("attending")) {
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			String name = req.getParameter("name");
			String attendingInput = req.getParameter("attending");
			String[] eventList = attendingInput.split(",");
			
			Key userkey = KeyFactory.createKey("User", name);
			Key eventKey;
			
			try {
				Entity user = datastore.get(userkey);
				
				List<String> invites = new ArrayList<String>();
				
				if (user.hasProperty("invites")) {
					invites = (List<String>) user.getProperty("invites");
				}

				List<String> attending = new ArrayList<String>();
				if (user.hasProperty("attending")) {
					attending = (List<String>) user.getProperty("attending");
				}
				
				for (String eventName: eventList) {
					eventKey = KeyFactory.createKey("Event", eventName);
					
					if (invites.contains(name)) {
						invites.remove(name);
						user.setProperty("invites", invites);
					}
					
					try {
						
						Entity event = datastore.get(eventKey);
						
						List<String> invitees = new ArrayList<String>();
						
						if (event.hasProperty("invited")) {
							invitees = (List<String>) event.getProperty("invited");
						}
						if (invitees.contains(name)) {
							invitees.remove(name);
							event.setProperty("invited", invitees);
						}

						List<String> attendees = new ArrayList<String>();
						if (event.hasProperty("attendees")) {
							attendees = (List<String>) event.getProperty("attendees");
						}
							
						if (!attendees.contains(name)) {
							attendees.add(name);
							event.setProperty("attendees", attendees);
						}
								
						if (!attending.contains(name)) {
								attending.add(name);
								user.setProperty("attending", attending);
						}
						
						datastore.put(user);
						datastore.put(event);	
												
					} catch (EntityNotFoundException e) {
						resp.getWriter().println("Event "+ eventName +" does not exist");
					}
				}
				
				
				resp.getWriter().println("Attendees for Event "+name+ " updated");
				
			} catch (EntityNotFoundException e) {
								
				resp.getWriter().println("User "+name+" does not exist");

			}
			
		}
	}
}
