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

@SuppressWarnings("serial")
public class CreateEvent extends HttpServlet {
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
				
				datastore.get(key);
				resp.getWriter().println("Event "+name+" already exists");
				
			} catch (EntityNotFoundException e) {
				
				Entity event = new Entity("Event", name);
				event.setProperty("event_name", name);
				event.setProperty("info", info);
				
				datastore.put(event);
				resp.getWriter().println("Event "+event+" Created");

			}
			
		}
	}
}
