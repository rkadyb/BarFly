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

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BarFly extends MapActivity {

	// User object representing the current user
	User user = new User();
	private TextView textView;
	
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
	
	private void homeScreenLoad() {
		textView = (TextView) findViewById(R.id.username);
		textView.setText("Logged In as " + user.getName());
		
		//MapView mapView = (MapView) findViewById(R.id.mapView);
		//mapView.setBuiltInZoomControls(true);
		
        Button createEvent = (Button) findViewById(R.id.create_event);
        
        // Create New Event
        createEvent.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		setContentView(R.layout.create_event);
        		createEventScreenLoad();
        	}
        });	
        
        Button showEvents = (Button) findViewById(R.id.see_attending);
        // See Attending Events
        showEvents.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		setContentView(R.layout.event);
        		eventScreenLoad();
        	}
        });
        
        Button showInvites = (Button) findViewById(R.id.see_invites);
        // See Attending Events
        showInvites.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		setContentView(R.layout.event);
        		inviteScreenLoad();
        	}
        });
	}
	
	private void createEventScreenLoad() {
        Button createEvent = (Button) findViewById(R.id.create_event_button);
        
        // Create New Event
        createEvent.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		EditText eventNameET = (EditText) findViewById(R.id.event_name);
        		EditText eventInfoET = (EditText) findViewById(R.id.event_info);
        		
        		String eventName = eventNameET.getText().toString();
        		String eventInfo = eventInfoET.getText().toString();
        		
        		if (eventName.equals("")) {
        			Toast.makeText(BarFly.this, "Please Enter a Valid Crawl Name", Toast.LENGTH_SHORT).show();
        		} else {
        			
        			CreateEvent createEvent = new CreateEvent();
        			createEvent.execute(new String[] {eventName, eventInfo});
 			      			
        		}
        	}
        });
	}
	
	private void eventScreenLoad() {
		ListView listView = (ListView) findViewById(R.id.eventList);
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.event_list_item, user.getAttending()));
	}
	
	private void inviteScreenLoad() {
		ListView listView = (ListView) findViewById(R.id.eventList);
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.event_list_item, user.getInvites()));
	}
	
	private class GetUser extends AsyncTask<String, Void, HashMap<String, Object>> {

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
	
	private class CheckLogin extends AsyncTask<String, Void, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
						
			String response = "";
			String typeOfRequest = params[0];
			String username = params[1];
			String password = params[2];
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet("http://10.0.2.2:8888/"+typeOfRequest+"?user="+username+"&pw="+password);			
			try {
				HttpResponse httpresponse = httpclient.execute(httpget);
				
				if (httpresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream content = httpresponse.getEntity().getContent();
					BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
					
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}
				}
					
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return new String[] {response, username};
		}
		
		@Override
		protected void onPostExecute(String[] result) {
			
			if (result[0].equals("true") || result[0].equals("User Created")) {
				
				user.setName(result[1]);
				
				GetUser getUser = new GetUser();
				getUser.execute(user.getName());
				
				setContentView(R.layout.home);
				homeScreenLoad();
				
				
			} else if (result[0].equals("false")) {	
				Toast.makeText(BarFly.this, "Please Enter a Valid Username/Password", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(BarFly.this, "Username already exists", 0).show();
			}
		}
		
	}
	
	private class CreateEvent extends AsyncTask<String, Void, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
						
			String response = "";
			String eventName = params[0];
			String info = params[1];
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet("http://10.0.2.2:8888/createEvent?name="+eventName+"&info="+info);			
			try {
				HttpResponse httpresponse = httpclient.execute(httpget);
				
				if (httpresponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					InputStream content = httpresponse.getEntity().getContent();
					BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
					
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}
				}
					
				
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return new String[] {response, eventName};
		}
		
		@Override
		protected void onPostExecute(String[] result) {
			
			if (result[0].equals("Event Created")) {
				
				setContentView(R.layout.home);
				homeScreenLoad();
				Toast.makeText(BarFly.this, "Bar crawl "+ result[1] +" created", 0).show();
				
				
			} else {
				Toast.makeText(BarFly.this, "Bar crawl " + result[1] + " already exists. Please choose a new name", 0).show();
			}
		}
		
	}
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //user = new User();
        
        //TextView tv = new TextView(this);
                
        setContentView(R.layout.main);
        
        textView = (TextView) findViewById(R.id.TextView01);
        
        Button login = (Button) findViewById(R.id.button1);
        Button signup = (Button) findViewById(R.id.button2);
        
        
        // Login Button Listener
        login.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {

        		EditText usernameET = (EditText) findViewById(R.id.name);
        		EditText passwordET = (EditText) findViewById(R.id.password);
        		
        		String username = usernameET.getText().toString();
        		String password = passwordET.getText().toString();
        		
        		if (username.equals("") || password.equals("")) {
        			Toast.makeText(BarFly.this, "Please Enter a Valid Username/Password", Toast.LENGTH_SHORT).show();
        		} else {
        			
        			CheckLogin checkLogin = new CheckLogin();
        			checkLogin.execute(new String[] {"login", username, password});
 			      			
        		}
        		
        	}
        });
        
        // Signup Button Listener
        signup.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
        		EditText usernameET = (EditText) findViewById(R.id.name);
        		EditText passwordET = (EditText) findViewById(R.id.password);
        		
        		String username = usernameET.getText().toString();
        		String password = passwordET.getText().toString();
        		
        		if (username.equals("") || password.equals("")) {
        			Toast.makeText(BarFly.this, "Please Enter a Valid Username/Password", Toast.LENGTH_SHORT).show();
        		} else {
        			
        			// This will have to handle handle signing up, but for now just display a user screen
        			//Toast.makeText(BarFly.this, "username: " + username, Toast.LENGTH_LONG).show();

        			//TextView tv = new TextView(BarFly.this);
        			//tv.setText("Just Signed Up and Logged In");
        			//setContentView(tv);
        			CheckLogin checkLogin = new CheckLogin();
        			checkLogin.execute(new String[] {"newUser", username, password});
        			
        		}				
				
			}
		});
    }


	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}