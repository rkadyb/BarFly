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
 * Responds to http://app-engine-url/getUser?user=USER;
 */

@SuppressWarnings("serial")
public class GetUser extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");
		
		if (req.getParameterMap().containsKey("user")) {
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			String username = req.getParameter("user");
			
			Key key = KeyFactory.createKey("User", username);
			
			try {
				
				Entity user = datastore.get(key);
				resp.getWriter().println("<User>");
				if (user.hasProperty("name")) {
					resp.getWriter().println("<name>"+user.getProperty("name")+"</name>");
				}
				if (user.hasProperty("friends")) {
					resp.getWriter().println("<friends>"+user.getProperty("friends").toString()+"</friends>");
				}
				if (user.hasProperty("location")) {
					resp.getWriter().println("<location>"+user.getProperty("location")+"</location>");
				}
				resp.getWriter().println("</User>");
				
			} catch (EntityNotFoundException e) {
								
				resp.getWriter().println("User "+username+" does not exist");

			}
			
		}
	}
}
