package com.barfly;

import java.util.List;

import android.location.Location;

public class User {
	private String name;
	private String password;
	private List<User> friends;
	private Location location;

	public User(String name, String password) {
		this.name = name;
		this.password = password;
	}
	
	public void addFriend(User friend) {
		this.friends.add(friend);
	}
	
	public List<User> getFriends() {
		return this.friends;
	}
	
	public void removeFriend(User friend) {
		this.friends.remove(friend);
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean checkPW(String input) {
		return (this.password == input);
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Location getLocation() {
		return this.location;
	}
}
