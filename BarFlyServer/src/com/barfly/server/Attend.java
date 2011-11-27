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
 * Takes requests in the form of http://app-engine-url/attend?name=EVENTNAME&attending=USER1,USER2,USER3, ... 
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
			String[] attendingList = attendingInput.split(",");
			
			Key key = KeyFactory.createKey("Event", name);
			Key attendeeKey;
			
			try {
				
				Entity event = datastore.get(key);
				List<String> invitees = new ArrayList<String>();
				List<String> attendees = new ArrayList<String>();
				
				if (event.hasProperty("invited")) {
					invitees = (List<String>) event.getProperty("invited");
				}
				
				if (event.hasProperty("attendees")) {
					attendees = (List<String>) event.getProperty("attendees");
				}
				
				for (String attendee: attendingList) {
					attendeeKey = KeyFactory.createKey("User", attendee);
					
					try {
						
						Entity person = datastore.get(attendeeKey);
						
						if (invitees.contains(attendee)) {
							
							List<String> invites = new ArrayList<String>();
							List<String> attending = new ArrayList<String>();
							
							if (person.hasProperty("invites")) {
								invites = (List<String>) person.getProperty("invites");
								if (invites.contains(name)) {
									invites.remove(name);
									person.setProperty("invites", invites);
								}
							}
							
							invitees.remove(attendee);
							
							if (!attendees.contains(attendee)) {
								
								if (person.hasProperty("attending")) {
								attending = (List<String>) person.getProperty("attending");
								
									if (!attending.contains(name)) {
										attending.add(name);
										person.setProperty("attending", attending);
									}
								
								}
								
								attendees.add(attendee);
							}
							
							datastore.put(person);
							
						} else {
							resp.getWriter().println("User "+attendee+" was not invited");
						}
												
					} catch (EntityNotFoundException e) {
						resp.getWriter().println("User "+attendee+" does not exist");
					}
				}
				
				event.setProperty("invited", invitees);
				event.setProperty("attendees", attendees);
				datastore.put(event);
				
				resp.getWriter().println("Attendees for Event "+name+ " updated");
				
			} catch (EntityNotFoundException e) {
								
				resp.getWriter().println("Event "+name+" does not exist");

			}
			
		}
	}
}
