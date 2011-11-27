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
 * Responds to requests in the form of http://app-engine-url/login?user=USERNAME&pw=PASSWORD
 */

@SuppressWarnings("serial")
public class Login extends HttpServlet {
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
				
				Entity user = datastore.get(key);
				
				if (user.getProperty("password").equals(password)) {
					resp.getWriter().println(true);
				} else {
					resp.getWriter().println(false);
				}
				
			} catch (EntityNotFoundException e) {

				resp.getWriter().println(false);

			}
			
		}
	}
}
