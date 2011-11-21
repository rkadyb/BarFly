package com.barfly;

import java.util.List;

public class Event {
	private List<User> attendees;
	private List<User> invited;
	private String event_name;
	private String info;
	private List<Activity> activities;
	
	public Event(String event_name, String info) {
		this.event_name = event_name;
		this.info = info;
	}
	
	public void addAttendee(User attendee) {
		this.attendees.add(attendee);
	}
	
	public void addAttendees(List<User> attendees) {
		this.attendees.addAll(attendees);
	}
	
	public void invite(User user) {
		this.invited.add(user);
	}
	
	public void inviteAll(List<User> invites) {
		this.invited.addAll(invites);
	}
	
	public boolean isInvited(User user) {
		return this.invited.contains(user);
	}
	
	public boolean isAttending(User user) {
		return this.attendees.contains(user);
	}
	
	public void unInvite(User user) {
		this.invited.remove(user);
	}
	
	public void unAttend(User user) {
		this.attendees.remove(user);
	}
	
	public void setInfo(String info) {
		this.info = info;
	}
	
	public void addActivity(Activity activity) {
		this.activities.add(activity);
	}
	
	public void addActivities(List<Activity> activities) {
		this.activities.addAll(activities);
	}
	
	public void removeActivity(Activity activity) {
		this.activities.remove(activity);
	}
}
