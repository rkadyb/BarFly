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

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.barfly.R;
import com.barfly.User;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class Home extends MapActivity {

	// User object representing the current user
	User user = new User();
	private TextView textView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle state = getIntent().getExtras();
        if (state != null) {
        	GetUser getUser = new GetUser();
        	getUser.execute(state.getString("username"));
        	user.setName(state.getString("username"));
        }
        
        if (state.getString("message") != null){
        	Toast.makeText(Home.this, state.getString("username"), 0).show();
    	}
		
		setContentView(R.layout.home);

		((MapView) findViewById(R.id.homeMap)).setBuiltInZoomControls(true);
		
		textView = (TextView) findViewById(R.id.username);
		textView.setText("Logged In as " + user.getName());
		
        Button createEvent = (Button) findViewById(R.id.create_event);
        
        // Create New Event
        createEvent.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
				// Pass the name with which to log in on to the home screen
				Intent intent = new Intent(getApplicationContext(), CreateEvent.class);
				intent.putExtra("username", user.getName());		
				startActivity(intent);
        	}
        });	
        
        Button showEvents = (Button) findViewById(R.id.see_attending);
        
        // inactivate
        showEvents.setEnabled(false);
        
        // Join a Barcrawl
        showEvents.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		// Pass the name with which to login
				Intent intent = new Intent(getApplicationContext(), CrawlsInProgress.class);
				intent.putExtra("username", user.getName());		
				startActivity(intent);
				
        		//setContentView(R.layout.event);
        		//eventScreenLoad();
        	}
        });
        
        Button showInvites = (Button) findViewById(R.id.see_invites);
        
        // inactivate
        showInvites.setEnabled(false);
        
        // See all Invites
        showInvites.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		setContentView(R.layout.event);
        		inviteScreenLoad();
        	}
        });
	}
	
	// Create a message handling object as an anonymous class.
	private OnItemClickListener eventClickedListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View view, int position,
				long id) {
			eventDetailsScreenLoad();
			
		}
	};
    
	private void eventScreenLoad() {
		ListView listView = (ListView) findViewById(R.id.eventList);
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.event_list_item, user.getAttending()));
		listView.setOnItemClickListener(eventClickedListener);
	}
	
	private void eventDetailsScreenLoad() {
		Toast.makeText(Home.this, "Date", Toast.LENGTH_SHORT).show();
	}

	
	private void inviteScreenLoad() {
		ListView listView = (ListView) findViewById(R.id.eventList);
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.event_list_item, user.getInvites()));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.logout:
	    	user = new User();
	    	setContentView(R.layout.main);
	        return true;
	    case R.id.settings:
	        return true;
	    case R.id.exit:
	    	this.finish();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
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
		protected void onPostExecute(HashMap<String, Object> result) {
			if (result.containsKey("friends")) {
				List<String> friends = (List<String>) result.get("friends");
				for (String friend: friends) {
					user.addFriend(friend);
				}
			}
			
			if (result.containsKey("invites")) {
				user.addInvites((List<String>) result.get("invites"));
				
				// Enable our button
				((Button) findViewById(R.id.see_invites)).setEnabled(true);
			}
			
			if (result.containsKey("attending")) {
				user.addAttending((List<String>) result.get("attending"));
				
				// Enable our button
				((Button) findViewById(R.id.see_attending)).setEnabled(true);
				
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
