package bhat.gupta.hummingbee.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.Timer;

import bhat.gupta.hummingbee.controller.GardenController.ZoneId;
import bhat.gupta.hummingbee.controller.WaterConsumptionCalculator;

public class Zone implements  ActionListener, Comparator<Zone>{

	private ZoneId groupId;
	private int minTemperature;
	private int maxTemperature;
	private int waterFlow;
	private boolean isProgrammed;
	private Timer temperatureTimer, scheduleTimer;
	private boolean isOn;
	private Garden garden;
	private Date startDate = null, endDate = null, sysDate = null;
	private Map dayVolumeMap;
	private static final int TEMP_SPRINKLER_TIME = 60000; //in miliiseconds
	private static final int TEMP_RATE_OF_FLOW = 50;

	private List<Sprinkler> zoneSprinklerList;

	public Zone(Garden garden) {
		this.isProgrammed = false;
		this.garden = garden;
		zoneSprinklerList = new ArrayList<Sprinkler>();
		this.temperatureTimer = new Timer(TEMP_SPRINKLER_TIME, this);
	}

	public Zone(Garden garden, ZoneId groupId) {
		this.groupId = groupId;
		this.garden = garden;
		this.isProgrammed = false;
		this.temperatureTimer = new Timer(TEMP_SPRINKLER_TIME, this);
		this.zoneSprinklerList = new ArrayList<Sprinkler>();
	}

	public Zone(Garden garden, ZoneId groupId, Sprinkler... sprinklers) {
		this.groupId = groupId;
		this.isProgrammed = false;
		this.garden = garden;
		this.zoneSprinklerList = new ArrayList<Sprinkler>();
		this.temperatureTimer = new Timer(TEMP_SPRINKLER_TIME, this);
		for (Sprinkler s : sprinklers) {
			s.setZone(this);
			this.zoneSprinklerList.add(s);
		}
	}

	/**
	 * This adds the sprinklers in the given list of sprinklers to the current list of sprinklers of this Zone
	 * @param sprinklers List of sprinklers
	 */
	public void addSprinklers(ArrayList<Sprinkler> sprinklers) {
		this.zoneSprinklerList.addAll(sprinklers);
	}

	/**
	 * This adds the given sprinkler to the current list of sprinklers of this Zone
	 * @param sprinklers Sprinkler
	 */
	public void addSprinkler(Sprinkler sprinkler) {
		this.zoneSprinklerList.add(sprinkler);
	}

	/**
	 * This returns the current id of this zone
	 * @return The current id of this zone
	 */
	public ZoneId getGroupId() {
		return groupId;
	}

	/**
	 * This sets the current id of this zone as the zoneId provided
	 * @param groupId Value to be set as the current id of this zone
	 */
	public void setGroupId(ZoneId groupId) {
		this.groupId = groupId;
	}

	/**
	 * This returns the current minimum temperature for this zone
	 * @return the current minimum temperature for this zone
	 */
	public int getMinTemperature() {
		return minTemperature;
	}

	/**
	 * This sets the current minimum temperature for this zone
	 * @param minTemperature Value to be set for the current minimum temperature of this zone
	 */
	public void setMinTemperature(int minTemperature) {
		this.minTemperature = minTemperature;
		if(this.minTemperature > 0){
			activate();
		}else{
			deactivate();
		}
	}

	/**
	 * This returns the current maximum temperature for this zone
	 * @return the current maximum temperature for this zone
	 */
	public int getMaxTemperature() {
		return maxTemperature;
	}

	/**
	 * This sets the current maximum temperature for this zone
	 * @param maxTemperature Value to be set for the current maximum temperature of this zone
	 */
	public void setMaxTemperature(int maxTemperature) {
		this.maxTemperature = maxTemperature;
		if(this.maxTemperature > 0){
			activate();
		}else{
			deactivate();
		}
	}

	
	/**
	 *This returns the current start date time for this zone
	 * @return the current start date time for this zone
	 */
	  public Date getStartDate() { return startDate; }
	  
	  /**
		 * This sets the current start date time for this zone
		 * @param startTime Value to be set for the current start date time for this zone
		 */
	  public void setStartDate(String startTime) 
	  
	  { 
		  String startHr = startTime.substring(0, 2);
		String startMin = startTime.substring(3,5);
		Calendar startCal = Calendar.getInstance();
		int hr = Integer.parseInt(startHr);
		startCal.set(Calendar.HOUR_OF_DAY, hr);
		startCal.set(Calendar.MINUTE, Integer.parseInt(startMin));
		startCal.set(Calendar.SECOND, 00);
		startDate = startCal.getTime(); 
		}
	  
	  /**
		 *This returns the current end date time for this zone
		 * @return the current end date time for this zone
		 */
	 public Date getEndDate() { return endDate; }
	  
	 /**
		 * This sets the current end date time for this zone
		 * @param endTime Value to be set for the current end date time for this zone
		 */
	  public void setEndDate(String endTime) { 
		  String endHr = endTime.substring(0, 2);
			String endMin = endTime.substring(3,5);
			Calendar endCal = Calendar.getInstance();
			endCal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endHr));
			endCal.set(Calendar.MINUTE, Integer.parseInt(endMin));
			endCal.set(Calendar.SECOND, 00);
			endDate = endCal.getTime();
		  }
	 

	public int getWaterFlow() {
		return waterFlow;
	}

	public void setWaterFlow(int waterFlow) {
		this.waterFlow = waterFlow;
	}

	public List<Sprinkler> getZoneSprinklerList() {
		return zoneSprinklerList;
	}

	public void setZoneSprinklerList(List<Sprinkler> zoneSprinklerList) {
		this.zoneSprinklerList = zoneSprinklerList;
	}

	/**
	 * This removes the given sprinkler from the current list of sprinklers of this zone
	 * @param sprinkler Sprinkler
	 */
	public void removeSprinkler(Sprinkler sprinkler) {
		this.zoneSprinklerList.remove(sprinkler);
	}

	@Override
	public String toString() {
		return "GroupID = " + this.groupId + "\tStart time = " + this.getStartDate().toString() + "\tStop time = "
				+ this.getEndDate().toString() + "\tMin Temp = " + this.minTemperature + "\t Max Temp = " + this.maxTemperature
				+ "\twateRate = " + this.waterFlow;
	}

	
	public boolean isOn() {
		return isOn;
	}
	
	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}
	
	public void activate() {
		this.isProgrammed = true;
	}
	
	public void deactivate() {
		if (!isProgrammed) {
			return;
		}
		this.isProgrammed = false;
		this.setOn(true);
	}
	
	/**
	 * This sets the On member of this zone as true, if the given temperature is greater  than or equal to the current maximum temperature of this zone.
	 * Sets the On member of this zone as false, if the given temperature is less  than or equal to the current minimum temperature of this zone.
	 * 
	 * @param temp Temperature
	 */
	public void checkTemperature(int temp) {
		if (!isProgrammed) {
			return;
		}
		
		if (temp >= this.maxTemperature) {
			//if the temperature timer is not running
			if (!temperatureTimer.isRunning()) {
				temperatureTimer.start();
			}
			
			//if isOn for the zone is not set and the temperature timer is running
			if (!isOn && temperatureTimer.isRunning()) {
				temperatureTimer.stop();
			}
				this.setOn(true);
		}
		
		//
		if (temp <= this.minTemperature) {
				this.setOn(false); 
				//if the sprinklers are running according to schedule & temperature decreases then stop the scheduleTimmer
				//and reset it
				if(scheduleTimer != null){
					if(scheduleTimer.isRunning()){
						scheduleTimer.stop();
					}
				}
		}
	}

	/**
	 * This is performed by the temperature timer
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		temperatureTimer.stop();
		recordWaterDataForTemperature();
		//Before setting to false check if this is within schedule and turn off only if it is outside the schedule.
		if(!isScheduled()){
		this.isOn = false;
		garden.notifyView();
		
		}
	}

	
	/**
	 * This compares the given zones
	 */
	@Override
	public int compare(Zone zone1, Zone zone2) {
		return zone1.getGroupId().compareTo(zone2.getGroupId());
	}
	
	/**
	 * This returns true if the current system date time lies between current start date and end date for this zone
	 * @return  true if the current system date time lies between current start date and end date for this zone
	 */
	public boolean isScheduled(){
		
		
		// if sysdate between startDate and endDate then return true.
		if(startDate != null && endDate != null){ 
			if(sysDate.before(endDate) && sysDate.after(startDate) ){
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * This schedules the scheduleTimer which runs based on start time for this zone
	 */
	public void setSchedule(){
		
		if(startDate != null && endDate != null){
			
			sysDate = getSysDate();
			
			
			double initialDelay = startDate.getTime() - sysDate.getTime();
			double stopTimeDelay = endDate.getTime() - startDate.getTime();
			
			scheduleTimer = new Timer((int) initialDelay, new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if(isOn){
						isOn = false;
						scheduleTimer.stop();//or make it repeat timer.repeat()
						recordWaterData();
						garden.notifyView();
						return;
					}
					if(!isOn){
					isOn = true;
					garden.notifyView();
					scheduleTimer.setDelay((int) stopTimeDelay);
					}
					
				}
			});
			scheduleTimer.start();
		}
	}
	
	/**
	 * This inserts the data for water consumption of this zone (when zone was activated based on time) into the database 
	 */
	public void recordWaterData(){
		WaterConsumptionCalculator pushWaterConsumptionData = new WaterConsumptionCalculator();
		int rateOfFlow = this.getWaterFlow();
	    String zoneId = groupId.toString();
	    pushWaterConsumptionData.insertWaterConsumptionData(zoneId, startDate , endDate, rateOfFlow);
				
	}
	
	/**
	 * This inserts the data for water consumption of this zone (when zone was activated based on temperature) into the database
	 */
	public void recordWaterDataForTemperature(){
		
		WaterConsumptionCalculator pushWaterConsumptionData = new WaterConsumptionCalculator();
		Calendar sysCal = Calendar.getInstance();
		Date endDate = sysCal.getTime();
		sysCal.set(Calendar.MILLISECOND, -TEMP_SPRINKLER_TIME);
		Date startDate = sysCal.getTime();
	    String zoneId = groupId.toString();
	    pushWaterConsumptionData.insertWaterConsumptionData(zoneId, startDate , endDate, TEMP_RATE_OF_FLOW);
	}
	
	/**
	 * This returns the current system date 
	 * @return the current system date 
	 */
	private Date getSysDate(){
		Calendar sysCal = Calendar.getInstance();
		Date sysDate = sysCal.getTime(); 
		return sysDate;
	}
	
}
