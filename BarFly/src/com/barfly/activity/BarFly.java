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

import com.barfly.R;
import com.barfly.User;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BarFly extends Activity {

	// User object representing the current user
	User user = new User();
	
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
				
				// Pass the name with which to log in on to the home screen
				Intent intent = new Intent(getApplicationContext(), Home.class);
				intent.putExtra("username", user.getName());		
				startActivity(intent);
				
			} else if (result[0].equals("false")) {	
				Toast.makeText(BarFly.this, "Please Enter a Valid Username/Password", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(BarFly.this, "Username already exists", 0).show();
			}
		}
		
	}
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        Button login = (Button) findViewById(R.id.login);
        Button signup = (Button) findViewById(R.id.signUp);
        
        
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
        			
        			CheckLogin checkLogin = new CheckLogin();
        			checkLogin.execute(new String[] {"newUser", username, password});
        			
        		}				
				
			}
		});
    }

}