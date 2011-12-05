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
 * Responds to requests in the form of http://app-engine-url/newUser?user=USERNAME&pw=PASSWORD
 */

@SuppressWarnings("serial")
public class NewUser extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");
		
		if (req.getParameterMap().containsKey("user") && 
			req.getParameterMap().containsKey("pw")) {
			
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			
			String username = req.getParameter("user");
			String password = req.getParameter("pw");
			
			Key key = KeyFactory.createKey("User", username);
			
			try {
				
				datastore.get(key);
				resp.getWriter().println("User "+username+" already exists");
				
			} catch (EntityNotFoundException e) {
				
				Entity user = new Entity("User", username);
				user.setProperty("name", username);
				user.setProperty("password", password);
				
				datastore.put(user);
				resp.getWriter().println("User Created");

			}
			
		}
	}
}
