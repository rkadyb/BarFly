package com.barfly;

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

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MapView.LayoutParams;  

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class BarFly extends MapActivity {
	
	// User object representing the current user
	//private String user = null;
	private TextView textView;
	
	private class CheckLogin extends AsyncTask<String, Void, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
						
			String response = "";
			
			String username = params[0];
			String password = params[1];
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet("http://10.0.2.2:8888/login?user="+username+"&pw="+password);			
			
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
			
			if (result[0].equals("true")) {
				setContentView(R.layout.home);
				textView = (TextView) findViewById(R.id.username);
				textView.setText("Logged In as "+ result[1]);
				
				MapView mapView = (MapView) findViewById(R.id.mapView);
				mapView.setBuiltInZoomControls(true);
				
			} else {
				Toast.makeText(BarFly.this, "Please Enter a Valid Username/Password", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
        			checkLogin.execute(new String[] {username, password});
 			      			
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
        			Toast.makeText(BarFly.this, "username: " + username, Toast.LENGTH_LONG).show();

        			TextView tv = new TextView(BarFly.this);
        			tv.setText("Just Signed Up and Logged In");
        			setContentView(tv);
        			
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