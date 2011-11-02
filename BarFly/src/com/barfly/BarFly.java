package com.barfly;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class BarFly extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView tv = new TextView(this);
        tv.setText("BarFly");
        
        setContentView(tv);
        
        //setContentView(R.layout.main);
    }
}