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
 * Responds to http://app-engine-url/getEvent?name=EVENTNAME
 */

@SuppressWarnings("serial")
public class GetEvent extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");
		
		if (req.getParameterMap().containsKey("name")) {
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			String eventname = req.getParameter("name");
			
			Key key = KeyFactory.createKey("Event", eventname);
			
			try {
				
				Entity event = datastore.get(key);
				resp.getWriter().println("<Event>");
				if (event.hasProperty("event_name")) {
					resp.getWriter().println("<event_name>"+event.getProperty("event_name")+"</event_name>");
				}
				if (event.hasProperty("info")) {
					resp.getWriter().println("<info>"+event.getProperty("info")+"</info>");
				}
				
				if (event.hasProperty("invited")) {
					resp.getWriter().println("<invited>"+event.getProperty("invited").toString()+"</invited>");
				}
				
				if (event.hasProperty("attendees")) {	
					resp.getWriter().println("<attendees>"+event.getProperty("attendees").toString()+"</attendees>");
				}
				
				if (event.hasProperty("activities")) {
					resp.getWriter().println("<activities>"+event.getProperty("activities").toString()+"</activities>");
				}
				
				if (event.hasProperty("location")) {
					resp.getWriter().println("<location>"+event.getProperty("location")+"</location>");
				}
				resp.getWriter().println("</Event>");
				
			} catch (EntityNotFoundException e) {
								
				resp.getWriter().println("Event "+eventname+" does not exist");

			}
			
		}
	}
}
