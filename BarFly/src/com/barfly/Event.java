package com.barfly;

import java.util.ArrayList;
import java.util.List;

public class Event {
	//private List<User> attendees;
	//private List<User> invited;
	private String event_name;
	private String info;
	//private List<Activity> activities;

	private List<String> attendees;
	private List<String> invited;
	//private List<String> activities;
	private String date;
	private String time;
	private List<Activity> activities;
	private String location;
	
	public Event() {
		this.attendees = new ArrayList<String>();
		this.invited = new ArrayList<String>();
		//this.activities = new ArrayList<String>();
		
	}
	
	public void setName(String name) {
		this.event_name = name;
	}
	
	public String getName() {
		return this.event_name;
	}
	
	public Event(String event_name, String info, String date, String time) {
		this.event_name = event_name;
		this.info = info;
		this.setDate(date);
		this.setTime(time);
	}
	
	public void addAttendee(String attendee) {
		this.attendees.add(attendee);
	}
	
	public void addAttendees(List<String> attendees) {
		this.attendees.addAll(attendees);
	}
	
	public void invite(String user) {
		this.invited.add(user);
	}
	
	public void inviteAll(List<String> invites) {
		this.invited.addAll(invites);
	}
	
	public boolean isInvited(String user) {
		return this.invited.contains(user);
	}
	
	public boolean isAttending(String user) {
		return this.attendees.contains(user);
	}
	
	public void unInvite(String user) {
		this.invited.remove(user);
	}
	
	public void unAttend(String user) {
		this.attendees.remove(user);
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	
	public String getInfo() {
		return this.info;
	}
	
	/*public void addActivity(String activity) {
		this.activities.add(activity);
	}
	
	public void addActivities(List<String> activities) {
		this.activities.addAll(activities);
	}*/
	
	public void removeActivity(String activity) {
		this.activities.remove(activity);
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
