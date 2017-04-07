package bhat.gupta.hummingbee.controller;

import java.util.Map;

import bhat.gupta.hummingbee.model.Garden;
import bhat.gupta.hummingbee.model.Sprinkler;
import bhat.gupta.hummingbee.model.Zone;

public class GardenController{

	Garden garden;
	Sprinkler east_sp1, east_sp2, east_sp3, east_sp4,west_sp1,west_sp2,west_sp3,west_sp4,north_sp1,north_sp2,north_sp3,north_sp4,south_sp1,south_sp2,south_sp3,south_sp4;
	Zone zoneEast, zoneWest, zoneSouth, zoneNorth, currentZone;
	private static final int DEFAULT_WATER_RATE = 30;
	public enum ZoneId {NORTH, EAST, WEST, SOUTH}

	public GardenController(Garden garden) {

		
		this.garden = garden;

		// Initialize sprinklers 
		east_sp1 = new Sprinkler("E1", true);
		east_sp2 = new Sprinkler("E2", true);
		east_sp3 = new Sprinkler("E3", true);
		east_sp4 = new Sprinkler("E4", false);

		west_sp1 = new Sprinkler("W1", true);
		west_sp2 = new Sprinkler("W2", false);
		west_sp3 = new Sprinkler("W3", true);
		west_sp4 = new Sprinkler("W4", true);

		north_sp1 = new Sprinkler("N1", true);
		north_sp2 = new Sprinkler("N2", true);
		north_sp3 = new Sprinkler("N3", true);
		north_sp4 = new Sprinkler("N4", true);

		south_sp1 = new Sprinkler("S1", true);
		south_sp2 = new Sprinkler("S2", true);
		south_sp3 = new Sprinkler("S3", false);
		south_sp4 = new Sprinkler("S4", true);

		currentZone = null;
		
		// Initialize zones with the sprinklers 
		zoneEast = new Zone(garden, ZoneId.EAST, east_sp1, east_sp2, east_sp3, east_sp4);
		zoneWest = new Zone(garden, ZoneId.WEST, west_sp1, west_sp2, west_sp3, west_sp4);
		zoneNorth = new Zone(garden, ZoneId.NORTH, north_sp1, north_sp2, north_sp3, north_sp4);
		zoneSouth = new Zone(garden, ZoneId.SOUTH, south_sp1, south_sp2, south_sp3, south_sp4);

		//add zones to the garden
		garden.addZone(zoneEast);
		garden.addZone(zoneWest);
		garden.addZone(zoneNorth);
		garden.addZone(zoneSouth);

	}

	public Garden getGarden() {
		return garden;
	}

	/**
	 * This returns the Zone object for the given zone id
	 * @param zoneId Id of the zone
	 * @return Zone
	 */
	public Zone getZoneFromZoneIdString(String zoneId) {
		for (Zone z : garden.getZones()) {
			if (z.getGroupId().toString().equalsIgnoreCase(zoneId))
				return z;
		}
		return null;
	}

	/**
	 * This sets the currentZone with the Zone object having the given zoneId
	 * @param zoneIdString zoneId of the zone
	 */
	public void setZoneForProgramming(String zoneIdString) {
		currentZone = getZoneFromZoneIdString(zoneIdString);
	}

	/**
	 * This sets the maximum temperature for the current zone as the temperature provided 
	 * @param maxTempTextField The integer value that should be set as the maximum temperature of the current zone
	 */
	public void setMaxTemp(String maxTempTextField) {

		int maxTemp = maxTempTextField != null && !maxTempTextField.equals("") ? Integer.parseInt(maxTempTextField)
				: -1;
		currentZone.setMaxTemperature(maxTemp);
	}

	/**
	 * This sets the minimum temperature for the current zone as the temperature provided 
	 * @param minTempTextField The integer value that should be set as the Minimum temperature of the current zone
	 */
	public void setMinTemp(String minTempTextField) {
		int minTemp = minTempTextField != null && !minTempTextField.equals("") ? Integer.parseInt(minTempTextField)
				: -1;
		currentZone.setMinTemperature(minTemp);
	}

	/**
	 * This sets the water flow rate for the current zone as the value provided
	 * @param waterRateString The integer value that should be set as the water flow rate of the current zone
	 */
	public void setWaterRate(String waterRateString) {
		int waterRate = waterRateString != null && !waterRateString.equals("") ? Integer.parseInt(waterRateString)
				: DEFAULT_WATER_RATE;
		currentZone.setWaterFlow(waterRate);
	}
	
	/**
	 * This sets the start time for the current zone as the value provided. 
	 * The start time is for activating the sprinklers in this zone.
	 * @param beginTime The String value that should be set as the start time of the current zone
	 */
	public void setStartTime(String beginTime) {
		currentZone.setStartDate(beginTime);

	}

	/**
	 * This sets the stop time for the current zone as the value provided. 
	 * The stop time is for stopping the activated sprinklers of this zone.
	 * @param endTime The String value that should be set as the stop time of the current zone
	 */
	public void setStopTime(String endTime) {
		currentZone.setEndDate(endTime);
	}

	/**
	 * This saves the data programmed for the current zone
	 */
	public void saveProgramDataForZone() {
		ZoneId zoneId = currentZone.getGroupId();
		switch(zoneId){
		case EAST:
			zoneEast = currentZone;
			break;
			
		case WEST:
			zoneWest = currentZone;
			break;
		
		case NORTH:
			zoneNorth = currentZone;
			
		case SOUTH:
			zoneSouth = currentZone;
			
		}
		System.out.println("Zone Details = " + currentZone.toString());
	}
	
	/** 
	 * This sets the current garden of this GardenController as the garden object provided
	 * @param garden Garden
	 */
	public void setGarden(Garden garden) {
		this.garden = garden;
	}
	
	/**
	 * This sets the current temperature of this GardenController as the temperature value provided
	 * @param temp Temperature value in integer to be set as the garden temperature
	 */
	public void setCurTemperature(int temp) {
		this.garden.setTemperature(temp);
	}
	
	/**
	 * This saves the minimum temperature, maximum temperature, start time and stop time of the current zone.
	 * @param beginTime Start time for the zone
	 * @param endTime Stop time for the zone
	 * @param minTemp Minimum temperature for the zone
	 * @param maxTemp Maximum temperature for the zone
	 */
	public void saveTimeTemperature(String beginTime, String endTime, String minTemp, String maxTemp){
		currentZone.setStartDate(beginTime);
		currentZone.setEndDate(endTime);
		setMinTemp(minTemp);
		setMaxTemp(maxTemp);
		currentZone.setSchedule();
		
	}
	
}
