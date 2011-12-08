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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.barfly.R;
import com.barfly.User;


public class CreateEvent extends Activity {
	
	// User object representing the current user
	User user = new User();
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_PICKER_DIALOG_ID = 1;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle state = getIntent().getExtras();
        if (state != null) {
        	GetUser getUser = new GetUser();
        	getUser.execute(state.getString("username"));

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
        
        EditText date = (EditText) findViewById(R.id.date);
        date.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				showDialog(DATE_DIALOG_ID);
			}
        });
        
        EditText time = (EditText) findViewById(R.id.time);
        time.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				showDialog(TIME_PICKER_DIALOG_ID);
			}
        });
	}
    
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
		   // set date picker as current date
		   return new DatePickerDialog(this, datePickerListener, 
                         2011, 12, 8);
		case TIME_PICKER_DIALOG_ID:
			   // set date picker as current date
			   return new TimePickerDialog(this, timePickerListener, 
					   hour, minute, false);
		}
		return null;
	}
 
	private DatePickerDialog.OnDateSetListener datePickerListener 
                = new DatePickerDialog.OnDateSetListener() {
 
		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth + 1;
			day = selectedDay;

			// set selected date into textview
			EditText date = (EditText) findViewById(R.id.date);
			date.setText(month+"/"+day+"/"+year);
		}
	};
	
	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

		// when dialog box is closed, below method will be called.
		public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfHour) {
			hour = hourOfDay;
			minute = minuteOfHour;

			// set selected date into textview
			EditText time = (EditText) findViewById(R.id.time);
			String timeText = new String();
			if (hour > 12) {
				timeText = hourOfDay + ":" + minute + " PM";
			} else if (hour == 0){
				timeText = "12:" + minute + " AM";
			} else {
				timeText = hourOfDay + ":" + minute + " AM";
			}
			time.setText(timeText);

		}
	};
	
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
			}
			
			if (result.containsKey("attending")) {
				user.addAttending((List<String>) result.get("attending"));
				
			}
			
			if (result.containsKey("location")) {
				//user.setLocation(location)
			}
			
		}
		
	}
}
