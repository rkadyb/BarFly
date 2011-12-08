package com.barfly;

import java.util.ArrayList;
import java.util.List;

public class BarCrawl extends Event {
	public BarCrawl(String event_name, String info, String date, String time) {
		super(event_name, info, date, time);
		// TODO Auto-generated constructor stub
	}
	
	public ArrayList<Bar> getStops() {
		List<Bar> stops = new ArrayList<Bar>();
		stops.add(new Bar("Crossroads Irish Pub", "457 Commonwealth Ave", "Boston", "MA", 02215));
		stops.add(new Bar("The Corner Tavern", "421 Marlborough St", "Boston", "MA", 02215));
		stops.add(new Bar("Lower Depths", "476 Commonwealth Avenue", "Boston", "MA", 02215));
		stops.add(new Bar("The Other Side", "407 Newbury Street", "Boston", "MA", 02115));
		stops.add(new Bar("Cactus Club", "939 Bolyston St.", "Boston", "MA", 02115));
		return (ArrayList<Bar>) stops;
	}
}
