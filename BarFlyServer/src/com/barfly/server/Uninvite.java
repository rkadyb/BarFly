package com.barfly.server;

import java.io.IOException;
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
 * Takes requests in the form of http://app-engine-url/uninvite?name=EVENTNAME&user=USER1 ... 
 *
 */

@SuppressWarnings("serial")
public class Uninvite extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");
		
		if (req.getParameterMap().containsKey("name") && 
			req.getParameterMap().containsKey("user")) {
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			String name = req.getParameter("name");
			String user = req.getParameter("user");
			
			Key eventKey = KeyFactory.createKey("Event", name);
			Key userKey = KeyFactory.createKey("User", user);
			
			try {
				
				Entity event = datastore.get(eventKey);
				
				if (event.hasProperty("invited")) {
					@SuppressWarnings("unchecked")
					List<String> invitees = (List<String>) event.getProperty("invited");
					
					try {
						
						datastore.get(userKey);
						invitees.remove(user);
						
						event.setProperty("invited", invitees);
						datastore.put(event);
						
						resp.getWriter().println("Invited list for Event "+name+ " updated");

					} catch (EntityNotFoundException e) {
						
						resp.getWriter().println("User "+user+" does not exist");
						
					}
					
				}
				
			} catch (EntityNotFoundException e) {
								
				resp.getWriter().println("Event "+name+" does not exist");

			}
			
		}
	}
}
