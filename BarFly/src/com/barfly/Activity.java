package com.barfly;

import android.location.Location;
import android.view.Menu;
import android.view.MenuInflater;

public class Activity {
	private String name;
	private String info;
	private Location location;

	public Activity(String name, String info) {
		this.name = name;
		this.info = info;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setInfo(String info) {
		this.info = info;
	}

}
