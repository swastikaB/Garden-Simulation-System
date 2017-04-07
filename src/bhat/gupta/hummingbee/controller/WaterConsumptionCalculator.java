package bhat.gupta.hummingbee.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WaterConsumptionCalculator {

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://127.0.0.1:8889/db_garden_sprinkler_sbsg";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "root";
	Map<String, Map<String, Double>> dayVolumeMapByZones;
	Connection conn = null;
	Statement stmt = null;

	public WaterConsumptionCalculator() {
		this.dayVolumeMapByZones = new HashMap<String, Map<String, Double>>();

	}

	/**
	 * This opens the JDBC connection
	 */
	public void openConnection() {
		System.out.println("Connecting to database...");
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * This closes the JDBC connection
	 */
	public void closeConnection() {
		try {
			if (stmt != null)
				stmt.close();
		} catch (SQLException se2) {
		}
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

	/**
	 * This sets and returns the dayVolumeMapByZones for this WaterConsumptionCalculator .
	 * dayVolumeMapByZones is a map which contains the mapping of all zones 
	 * and their respective maps for day and water volume consumption of that day.
	 * @return dayVolumeMapByZones
	 */
	public Map<String, Map<String, Double>> getWaterConsumptionData() {
		try {
			openConnection();
			List<String> zoneNames = getZoneNames();

			for (String s : zoneNames) {
				Map<String, Double> dayVolumeMap = getDayVolumeCalculationForZone(s);
				dayVolumeMapByZones.put(s, dayVolumeMap);
			}

			for (Entry<String, Map<String, Double>> entry : dayVolumeMapByZones
					.entrySet()) {
				System.out.println("Zone: " + entry.getKey());
				printMap(entry.getValue());
			}
			closeConnection();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				closeConnection();
		}
		return this.dayVolumeMapByZones;

	}

	/**
	 * This prints the dayVolumeMap
	 * @param dayVolumeMap
	 */
	public void printMap(Map<String, Double> dayVolumeMap) {
		for (Entry<String, Double> entry : dayVolumeMap.entrySet()) {
			System.out.println("Date: " + entry.getKey() + ", Volume: "
					+ entry.getValue());
		}
	}

	/**
	 * This gets the list of all zones that were ever scheduled and activated.
	 * @return list of all zones that were ever scheduled and activated.
	 */
	public List<String> getZoneNames() {

		List<String> zoneNames = new ArrayList<String>();
		String sql;
		sql = "SELECT DISTINCT zone_id FROM  tbl_water_consumption_zonewise";
		ResultSet rs;
		try {
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				zoneNames.add(rs.getString("zone_id"));
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return zoneNames;
	}

	/**
	 * This returns a map having the mapping of day and the water volume consumption on that day for the zone having the given zoneId.
	 * @param strZoneId zoneId
	 * @return A map having the mapping of day and the water volume consumption on that day
	 */
	public Map<String, Double> getDayVolumeCalculationForZone(String strZoneId) {
		Map<String, Double> dayVolumeMap = new LinkedHashMap<String, Double>();

		try {
			String sql;
			sql = "SELECT * FROM  tbl_water_consumption_zonewise where zone_id='"
					+ strZoneId + "' order by start_time asc";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String date = rs.getDate("start_time").toString();
				String zone_id = rs.getString("zone_id");
				int rate_of_water_flow = rs.getInt("rate_of_water_flow");
				String startTime = rs.getTime("start_time").toString();
				String endTime = rs.getTime("stop_time").toString();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				Date d1 = sdf.parse(startTime);
				Date d2 = sdf.parse(endTime);
				double elapsedTimeInHours = (d2.getTime() - d1.getTime())
						/ (double) 3600000;
				double volume = (double) elapsedTimeInHours
						* rate_of_water_flow * 20;
				if (dayVolumeMap.containsKey(date)) {
					dayVolumeMap.put(date, dayVolumeMap.get(date) + volume);
				} else {
					dayVolumeMap.put(date, volume);
				}

			}
			rs.close();

		} catch (SQLException se) {
			se.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return dayVolumeMap;

	}

	/**
	 * This inserts the provided data into the database for the time the sprinklers in the zone having the given zoneId were activated.
	 * @param zoneId zoneId
	 * @param startDate Start DateTime of activation
	 * @param endDate End dateTime for activation
	 * @param rateOfFlow Rate of flow of water
	 */
	public void insertWaterConsumptionData(
			String zoneId, Date startDate, Date endDate, int rateOfFlow) {

		try {
			openConnection();

			java.text.SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sqlStartDate = sdf.format(startDate);
			String sqlEndDate = sdf.format(endDate);
			String sqlInsertStatement = "INSERT INTO `tbl_water_consumption_zonewise` ( `zone_id`, `start_time`, `stop_time`,"
					+ " `rate_of_water_flow`) VALUES ( '"
					+ zoneId + "','"
					+ sqlStartDate + "','"
					+ sqlEndDate + "','"
					+ rateOfFlow + "')";
			System.out.println(sqlInsertStatement);
			stmt.executeUpdate(sqlInsertStatement);
			closeConnection();
		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			if (conn != null)
				closeConnection();
		}
	}

}
