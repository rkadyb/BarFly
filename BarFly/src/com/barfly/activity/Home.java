package com.barfly.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.barfly.GetUser;
import com.barfly.R;
import com.barfly.User;

public class Home extends Activity {

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
        	user = getUser.getUser();
        	user.setName(state.getString("username"));
        }
        
        if (state.getString("message") != null){
        	Toast.makeText(Home.this, state.getString("username"), 0).show();
    	}
		
		setContentView(R.layout.home);

		textView = (TextView) findViewById(R.id.username);
		textView.setText("Logged In as " + user.getName());
		
		//MapView mapView = (MapView) findViewById(R.id.mapView);
		//mapView.setBuiltInZoomControls(true);
		
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
	
	private void eventScreenLoad() {
		ListView listView = (ListView) findViewById(R.id.eventList);
		listView.setAdapter(new ArrayAdapter<String>(this, R.layout.event_list_item, user.getAttending()));
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
}
