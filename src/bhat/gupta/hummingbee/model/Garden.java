package bhat.gupta.hummingbee.model;

import java.util.*;

public class Garden extends Observable{

	private String name;
	private List<Zone> zones;
	private int temp;
	private static Garden garden;

	/*
	public Garden(String name) {
		
		this.name = name;
		this.zones = new ArrayList<Zone>();
		this.temp = 0;
	}
	*/
	private Garden() {
		this.zones = new ArrayList<Zone>();
		this.name = "Humming Bee Garden";

	}
	
	public static Garden getGarden(){
		if (garden == null){
			garden = new Garden();
		}
		return garden;
	}
	
	/**
	 * This sets the current temperature of this Garden
	 * @param temp Temperature value to be set as the current temperature of this Garden
	 */
	public void setTemperature(int temp) {
		this.temp = temp;

		for(Zone z:zones)
		{
			z.checkTemperature(this.temp);
		}
		notifyView();
	}

	/**
	 * This adds the given zone to the current list of Zones of this Garden
	 * @param zone Zone object to be added
	 */
	public void addZone(Zone zone) {
		this.zones.add(zone);
	}

	/**
	 * This removes the given zone from the current list of Zones of this Garden
	 * @param zone Zone object to be removed
	 */
	public void removeZone(Zone zone) {
		this.zones.remove(zone);
	}
	
	/**
	 * This returns an array of String having the names of all the zones in the current list of zones of this Garden.
	 * @return An array of String having the names of all the zones in the current list of zones of this Garden.
	 */
	public String[] getZoneNames()
	{
		String[] zoneNames=new String[zones.size()];
		int i=0;
		for(Zone z:zones)
		{
			zoneNames[i]=z.getGroupId().toString();
			i++;
		}
		return zoneNames;
	}

	/**
	 * This returns the current name of this Garden
	 * @return Name of this Garden
	 */
	public String getName() {
		return name;
	}

	/**
	 * This returns the current list of zones of this Garden
	 * @return The current list of zones of this Garden
	 */
	public List<Zone> getZones() {
		return zones;
	}
	
	/**
	 * This notifies the views of the changes in this Garden.
	 */
	public void notifyView() {
		setChanged();
		notifyObservers();
	}
}
