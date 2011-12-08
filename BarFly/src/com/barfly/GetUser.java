package com.barfly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class GetUser extends AsyncTask<String, Void, HashMap<String, Object>> {
	User user = new User();
	
	@Override
	protected HashMap<String, Object> doInBackground(String... params) {
		HashMap<String, Object> response = new HashMap<String, Object>();
		
		String username = params[0];
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet("http://10.0.2.2:8888/getUser?user="+username);	
		
		try {
			HttpResponse httpresponse = httpclient.execute(httpget);
			
			if (httpresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream content = httpresponse.getEntity().getContent();
				BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
				
				String s = "";
				while ((s = buffer.readLine()) != null) {

					if (s.startsWith("<friends>")) {
						s = s.replace("<friends>", "");
						s = s.replace("[", "");
						s = s.replace("]", "");
						List<String> friends =  Arrays.asList(s.split(","));
						response.put("friends", friends);
						
					}
					
					if (s.startsWith("<invites>")) {
						s = s.replace("<invites>", "");
						s = s.replace("[", "");
						s = s.replace("]", "");
						List<String> invites = Arrays.asList(s.split(","));
						response.put("invites", invites);
					}
					
					if (s.startsWith("<attending>")) {
						s = s.replace("<attending>", "");
						s = s.replace("[", "");
						s = s.replace("]", "");
						List<String> attending =  new ArrayList<String>();
						attending = Arrays.asList(s.split(","));
						response.put("attending", attending);
					}
					
					if (s.startsWith("<location>")) {
						s = s.replace("<location>", "");
						s = s.replace("[", "");
						s = s.replace("]", "");
						
					}
				}
			}
				
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return response;
	}
	
	@SuppressWarnings("unchecked")
	protected void onPostExecute(HashMap<String, Object> result) {
		if (result.containsKey("friends")) {
			List<String> friends = (List<String>) result.get("friends");
			for (String friend: friends) {
				user.addFriend(friend);
			}
		}
		
		if (result.containsKey("invites")) {
			user.addInvites((List<String>) result.get("invites"));
		}
		
		if (result.containsKey("attending")) {
			user.addAttending((List<String>) result.get("attending"));
		}
		
		if (result.containsKey("location")) {
			//user.setLocation(location)
		}
		
	}
	
	public User getUser(){
		return user;
	}
	
}
