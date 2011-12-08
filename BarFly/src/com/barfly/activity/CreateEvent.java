package com.barfly.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.barfly.GetUser;
import com.barfly.R;
import com.barfly.User;


public class CreateEvent extends Activity {
	
	// User object representing the current user
	User user = new User();
	static final int DATE_DIALOG_ID = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle state = getIntent().getExtras();
        if (state != null) {
        	GetUser getUser = new GetUser();
        	getUser.execute(state.getString("username"));
        	user = getUser.getUser();
        	user.setName(state.getString("username"));
        }
		
		setContentView(R.layout.create_event);

        Button createEvent = (Button) findViewById(R.id.create_event_button);
        
        // Create New Event
        createEvent.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		EditText eventNameET = (EditText) findViewById(R.id.event_name);
        		EditText eventInfoET = (EditText) findViewById(R.id.event_info);
        		
        		String eventName = eventNameET.getText().toString();
        		String eventInfo = eventInfoET.getText().toString();
        		
        		if (eventName.equals("")) {
        			Toast.makeText(CreateEvent.this, "Please Enter a Valid Crawl Name", Toast.LENGTH_SHORT).show();
        		} else {
        			CreateEventAsync createEvent = new CreateEventAsync();
        			createEvent.execute(new String[] {eventName, eventInfo});	
        		}
        	}
        });
        
	}
	
	private class CreateEventAsync extends AsyncTask<String, Void, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
						
			String response = "";
			String eventName = params[0];
			String info = params[1];
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet("http://10.0.2.2:8888/createEvent?name="+eventName+"&info="+info+"&creator="+user.getName());			
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
				
				Intent intent = new Intent(getApplicationContext(), Home.class);
				intent.putExtra("username", user.getName());
				intent.putExtra("message", "Bar crawl "+ result[1] +" created");
				startActivity(intent);	
				
			} else {
				Toast.makeText(CreateEvent.this, "Bar crawl " + result[1] + " already exists. Please choose a new name", 0).show();
			}
		}
		
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
}
