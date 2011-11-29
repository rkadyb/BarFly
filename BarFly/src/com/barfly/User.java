package com.barfly;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;

public class User {
	private String name;
	//private String password;
	//private List<User> friends;
	private List<String> friends;

	private Location location;
	//private List<Event> invites;
	private List<String> invites;
	//private List<Event> attending;
	private List<String> attending;

//	public User(String name, String password) {
//		this.name = name;
//		this.password = password;
//	}
	public User() {
		this.invites = new ArrayList<String>();
		this.friends = new ArrayList<String>();
		this.attending = new ArrayList<String>();
	}
	
	public void addFriend(String friend) {
		this.friends.add(friend);
	}
	
	public List<String> getFriends() {
		return this.friends;
	}
	
	public void removeFriend(User friend) {
		this.friends.remove(friend);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
//	public boolean checkPW(String input) {
//		return (this.password == input);
//	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public void addInvites(List<String> invites) {
		this.invites.addAll(invites);
	}
	
	public List<String> getInvites() {
		return this.invites;
	}
	
	public void addAttending(List<String> attending) {
		this.attending.addAll(attending);
	}
	
	public List<String> getAttending() {
		return this.attending;
	}
}
