package com.barfly.activity;

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

import com.barfly.Event;
import com.barfly.R;
import com.barfly.User;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class ViewCrawl extends MapActivity {
	
	User user = new User();
	Event crawl = new Event();
	
	// Global scope for this activity, used to assign dynamic buttons
	String temp;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle state = getIntent().getExtras();
        if (state != null) {
        	GetUser getUser = new GetUser();
        	getUser.execute(state.getString("username"));
        	        	
        	user.setName(state.getString("username"));
        	
        	GetEvent getEvent = new GetEvent();
        	getEvent.execute(state.getString("crawl"));
        	
        	crawl.setName(state.getString("crawl"));
        	
        }
		
		setContentView(R.layout.view_crawl);
		((TextView) findViewById(R.id.event_name)).setText(crawl.getName());
		
		((MapView) findViewById(R.id.crawlMap)).setBuiltInZoomControls(true);
	}
	
	public class GetEvent extends AsyncTask<String, Void, HashMap<String, Object>> {

		@Override
		protected HashMap<String, Object> doInBackground(String... params) {
			HashMap<String, Object> response = new HashMap<String, Object>();
			
			String eventName = params[0];
			eventName = eventName.replace(" ", "%20");
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet("http://10.0.2.2:8888/getEvent?name="+eventName);
			try {
				HttpResponse httpresponse = httpclient.execute(httpget);
				
				if (httpresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream content = httpresponse.getEntity().getContent();
					BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
					
					String s = "";
					while ((s = buffer.readLine()) != null) {

						if (s.startsWith("<info>")) {
							s = s.replace("<info>", "");
							String info =  s;
							response.put("info", info);
							
						}
						
						if (s.startsWith("<invited>")) {
							s = s.replace("<invited>", "");
							s = s.replace("[", "");
							s = s.replace("]", "");
							List<String> invited = Arrays.asList(s.split(","));
							response.put("invited", invited);
						}
						
						if (s.startsWith("<attendees>")) {
							s = s.replace("<attendees>", "");
							s = s.replace("[", "");
							s = s.replace("]", "");
							List<String> attendees =  new ArrayList<String>();
							attendees = Arrays.asList(s.split(","));
							response.put("attendees", attendees);
						}
						
						if (s.startsWith("<activities>")) {
							s = s.replace("<activities>", "");
							s = s.replace("[", "");
							s = s.replace("]", "");
							List<String> activities =  new ArrayList<String>();
							activities = Arrays.asList(s.split(","));
							response.put("activities", activities);
						}
						
						if (s.startsWith("<location>")) {
							s = s.replace("<location>", "");
							String location =  s;
							response.put("location", location);
						}
						if (s.startsWith("<date>")) {
							s = s.replace("<date>", "");
							String date =  s;
							response.put("date", date);
						}
						if (s.startsWith("<time>")) {
							s = s.replace("<time>", "");
							String time =  s;
							response.put("time", time);
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
		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			if (result.containsKey("info")) {
				crawl.setInfo((String) result.get("info"));
			}
			
			if (result.containsKey("invited")) {
				crawl.inviteAll((List<String>) result.get("invited"));
			}
			
			if (result.containsKey("attendees")) {
				crawl.addAttendees((List<String>) result.get("attendees"));				
			}
			
			//if (result.containsKey("activities")) {
			//	crawl.addActivities((List<String>) result.get("activities"));
			//}
			
			if (result.containsKey("location")) {
				crawl.setLocation((String) result.get("location"));
			}
			
			if (result.containsKey("date")) {
				crawl.setDate((String) result.get("date"));
			}
			
			if (result.containsKey("time")) {
				crawl.setTime((String) result.get("time"));
			}
			
			// Deal with the UI elements
			TextView infoBox = (TextView) findViewById(R.id.info);
			infoBox.setText(crawl.getInfo());
			
			((TextView) findViewById(R.id.dateTime)).setText(crawl.getDate()+ " " + crawl.getTime());
		}
	}
	
	public class GetUser extends AsyncTask<String, Void, HashMap<String, Object>> {
		
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
		@Override
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
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
}
