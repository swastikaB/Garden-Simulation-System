package bhat.gupta.hummingbee.model;

public class Sprinkler {
	private String sprinklerId;
	private boolean isFunctional;
	private int waterFlow;
	private Zone zone;
	
	public Sprinkler(String sprinklerId){
		this.sprinklerId = sprinklerId;
		this.isFunctional=true;
	}
	public Sprinkler(String sprinklerId, boolean isFunctional){
		this.sprinklerId = sprinklerId;
		this.isFunctional=isFunctional;
	}
	
	/**
	 * This returns the current zone of this Sprinkler
	 * @return Zone of this Sprinkler
	 */
	public Zone getZone() {
		return zone;
	}
	
	/**
	 * This sets the current zone of this sprinkler as the provided zone
	 * @param zone
	 */
	public void setZone(Zone zone) {
		this.zone = zone;
	}
	
	/**
	 * This returns the current id of this Sprinkler
	 * @return Id of this Sprinkler
	 */
	public String getSprinklerId() {
		return sprinklerId;
	}
	/**
	 * This sets the current id of this sprinkler as the provided string
	 * @param String to be set as the id of this sprinkler
	 */
	public void setSprinklerId(String sprinklerId) {
		this.sprinklerId = sprinklerId;
	}
	
	/**
	 * This returns true if this Sprinkler is functional.
	 * @return True if this Sprinkler is functional.
	 */
	public boolean isFunctional() {
		return isFunctional;
	}
	
	/**
	 * This sets the current isFunctional member of this sprinkler to the value provided
	 * @param Boolean value to be set for the isFunctional member of this sprinkler
	 */
	public void setFunctional(boolean isFunctional) {
		this.isFunctional = isFunctional;
	}
	
	/**
	 * This returns the current water flow rate for this sprinkler
	 * @return the current water flow rate for this sprinkler
	 */
	public int getWaterFlow() {
		return waterFlow;
	}
	/**
	 * This sets the current water flow rate for this sprinkler as the provided value
	 * @param waterFlow Value in integer that should be set as the water flow rate for this sprinkler
	 */
	public void setWaterFlow(int waterFlow) {
		this.waterFlow = waterFlow;
	}
	
	/**
	 * This returns a string that concatenates the On/Off status as well as the Working/Non working status of this sprinkler
	 * @return A string that concatenates the On/Off status as well as the Working/Non working status of this sprinkler
	 */
	public String getStatus() {
		String strFunctional="";
		String strActive="";
		if(this.isFunctional)
			strFunctional="FUNCTIONAL";
		else
			strFunctional=" NOT FUNCTIONAL";
		
		if(this.getZone().isOn() && this.isFunctional)
		{
			strActive="ON";
		}
		else
		{
			strActive="OFF";
		}
		return this.getSprinklerId()+","+strFunctional+","+strActive;
	}
	@Override
	public String toString() {
		return this.sprinklerId;
	}
	

}
