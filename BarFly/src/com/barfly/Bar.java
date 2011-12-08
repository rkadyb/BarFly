package com.barfly;

public class Bar {
	private String name;
	private String address;
	private String city;
	private String state;
	private int zip;
	
	public Bar(String name, String address, String city, String state, int zip) {
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getZip() {
		return zip;
	}
	public void setZip(int zip) {
		this.zip = zip;
	}
}
