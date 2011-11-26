package com.barfly;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BarFly extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //TextView tv = new TextView(this);
        
        setContentView(R.layout.main);
        
        Button login = (Button) findViewById(R.id.button1);
        Button signup = (Button) findViewById(R.id.button2);
        
        login.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		EditText usernameET = (EditText) findViewById(R.id.name);
        		EditText passwordET = (EditText) findViewById(R.id.password);
        		
        		String username = usernameET.getText().toString();
        		String password = passwordET.getText().toString();
        		
        		if (username.equals("") || password.equals("")) {
        			Toast.makeText(BarFly.this, "Please Enter a Valid Username/Password", Toast.LENGTH_SHORT).show();
        		} else {
        			
        			// This will have to handle login, but for now just display a user screen
        			Toast.makeText(BarFly.this, "username: " + username, Toast.LENGTH_LONG).show();

        			TextView tv = new TextView(BarFly.this);
        			tv.setText("Logged In");
        			setContentView(tv);
        			
        		}
        		
        	}
        });
        
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
}