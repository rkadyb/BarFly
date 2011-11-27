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
 * Takes requests in the form of http://app-engine-url/invite?name=EVENTNAME&invited=USER1,USER2,USER3, ... 
 *
 */

@SuppressWarnings("serial")
public class Invite extends HttpServlet {
	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");
		
		if (req.getParameterMap().containsKey("name") && 
			req.getParameterMap().containsKey("invited")) {
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			String name = req.getParameter("name");
			String invitedInput = req.getParameter("invited");
			String[] invitedList = invitedInput.split(",");
			
			Key key = KeyFactory.createKey("Event", name);
			Key inviteeKey;
			
			try {
				
				Entity event = datastore.get(key);
				List<String> invitees = new ArrayList<String>();
				
				if (event.hasProperty("invited")) {
					invitees = (List<String>) event.getProperty("invited");
				}
				
				for (String invitee: invitedList) {
					inviteeKey = KeyFactory.createKey("User", invitee);
					
					try {
						
						datastore.get(inviteeKey);
						invitees.add(invitee);
						
					} catch (EntityNotFoundException e) {
						resp.getWriter().println("User "+invitee+" does not exist");
					}
				}
				
				event.setProperty("invited", invitees);
				datastore.put(event);
				
				resp.getWriter().println("Invited list for Event "+name+ " updated");
				
			} catch (EntityNotFoundException e) {
								
				resp.getWriter().println("Event "+name+" does not exist");

			}
			
		}
	}
}
