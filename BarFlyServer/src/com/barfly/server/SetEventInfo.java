package com.barfly.server;

import java.io.IOException;

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
 * Takes requests in the form http://app-engine-url/setEventInfo?name=EVENTNAME&info=INFO
 * 
 */

@SuppressWarnings("serial")
public class SetEventInfo extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");
		
		if (req.getParameterMap().containsKey("name") && 
			req.getParameterMap().containsKey("info")) {
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			String name = req.getParameter("name");
			String info = req.getParameter("info");
			
			Key key = KeyFactory.createKey("Event", name);
			
			try {
				
				Entity event = datastore.get(key);
				event.setProperty("info", info);
				datastore.put(event);
				
				resp.getWriter().println("Event "+name+ " info changed to "+info);
				
			} catch (EntityNotFoundException e) {
								
				resp.getWriter().println("Event "+name+" does not exist");

			}
			
		}
	}
}
