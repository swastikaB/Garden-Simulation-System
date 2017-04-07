package bhat.gupta.hummingbee.controller;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bhat.gupta.hummingbee.model.Garden;
import bhat.gupta.hummingbee.view.ViewGardenPanel;
import bhat.gupta.hummingbee.view.ViewStatusPanel;
import bhat.gupta.hummingbee.view.WaterConsumptionChartPanel;

/**
 * HummingBeeApp.java
 *
 */
public class HummingBeeApp {

	private JFrame frame;
	private JPanel titlePanel, onOffButtonPanel, mainPanel, blankPanel, viewGardenPanel, programInputPanel,
			viewStatusPanel, zoneSelectionPanel, programSprinklerPanel, checkWaterConsumptionPanel;
	private JTabbedPane tabbedPane;
	JPanel submitButtonPanel, timeConfigPanel, temperatureConfigPanel, waterConfigPanel;
	private JTextField startTimeTextField, stopTimeTextField, minTempTextField, maxTempTextField, waterFlowTextField;
	private JButton onOffButton, submitButton;
	JLabel durationLabel, waterFlowLabel, maxTempLabel, minTempLabel, zoneLabel, stopTimeLabel, startTimeLabel,
			dayLabel;
	GardenController gardenController;
	JComboBox timeComboBox, zoneComboBox, sprinklerComboBox, dayComboBox, waterRateComboBox;
	
	private JPanel gardenPanel;
	private JPanel panel;
	private JSlider temperatureSlider;
	WaterConsumptionCalculator waterConsumptioncalculator;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HummingBeeApp window = new HummingBeeApp();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HummingBeeApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Garden garden = new Garden("Humming Bee Garden");
		gardenController = new GardenController(garden);
		waterConsumptioncalculator = new WaterConsumptionCalculator();

		createTitlePanel();
		frame.getContentPane().add(titlePanel, BorderLayout.NORTH);
		createOnOffButtonPanel();

		frame.getContentPane().add(onOffButtonPanel, BorderLayout.SOUTH);
		createMainPanel();
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

		Toolkit toolkit = frame.getToolkit();
		Dimension size = toolkit.getScreenSize();
		frame.setLocation(size.width / 3 - frame.getWidth() / 3, size.height / 3 - frame.getHeight() / 3);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = screenSize.height;
		int width = screenSize.width;
		System.out.println(width);
		frame.setSize(width / 2, height / 2);
	}

	/**
	 * This creates the Title Panel
	 */
	public void createTitlePanel() {
		titlePanel = new JPanel();
		JLabel lblHummingBee = new JLabel("Humming Bee Garden Sprinkler System");
		lblHummingBee.setFont(new Font("Baghdad", Font.BOLD, 16));
		titlePanel.add(lblHummingBee);
	}

	/**
	 * This creates the Panel for OnOff button
	 */
	public void createOnOffButtonPanel() {
		onOffButtonPanel = new JPanel();
		onOffButton = new JButton("ON");
		onOffButton.setFont(new Font("Baghdad", Font.PLAIN, 13));
		onOffButtonPanel.add(onOffButton);
		onOffButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CardLayout cd = (CardLayout) mainPanel.getLayout();
				if (e.getActionCommand().equalsIgnoreCase("On")) {
					cd.show(mainPanel, "tabbedPane");
					onOffButton.setText("OFF");
				} else {
					cd.show(mainPanel, "blankPanel");
					onOffButton.setText("On");
				}
			}
		});
	}

	/**
	 * This creates the Water Consumption Panel
	 */
	public void createWaterConsumptionPanel() {
		Map<String, Map<String, Double>> dayVolumeMapByZones = waterConsumptioncalculator.getWaterConsumptionData();
		checkWaterConsumptionPanel = new WaterConsumptionChartPanel(dayVolumeMapByZones);
		checkWaterConsumptionPanel.setToolTipText("View Water Consumption data");
	}

	/**
	 * This creates the View Status Panel
	 */
	public void createViewStatusPanel() {
		viewStatusPanel = new ViewStatusPanel(gardenController.getGarden());
	}

	/**
	 * This creates the Main Panel
	 */
	public void createMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.BLACK);
		mainPanel.setLayout(new CardLayout(0, 0));
		blankPanel = new JPanel();
		blankPanel.setBackground(Color.BLACK);
		mainPanel.add(blankPanel, "blankPanel");

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		mainPanel.add(tabbedPane, "tabbedPane");

		createProgramSprinklerPanel();

		createViewStatusPanel();
		createWaterConsumptionPanel();
		gardenPanel = new JPanel();
		
		//associates the panels with the different tabs
		tabbedPane.addTab("View Garden", null, gardenPanel, null);
		tabbedPane.addTab("Status", null, viewStatusPanel, null);
		tabbedPane.addTab("Program", null, programSprinklerPanel, null);
		tabbedPane.addTab("Water Report", null, checkWaterConsumptionPanel, null);
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				System.out.println("Tab: " + tabbedPane.getSelectedIndex() + "has been selected");
				if (tabbedPane.getSelectedIndex() == 3) {
					createWaterConsumptionPanel();
				}

			}

		});

		gardenPanel.setLayout(new BorderLayout(0, 0));
		viewGardenPanel = new ViewGardenPanel(gardenController.getGarden());
		gardenPanel.add(viewGardenPanel, BorderLayout.CENTER);

		panel = new JPanel();
		panel.setBackground(new Color(46, 139, 87));
		gardenPanel.add(panel, BorderLayout.SOUTH);

		createTemperatureSlider();
		panel.add(temperatureSlider);
	}

	/**
	 * This creates the Program Sprinkler Panel
	 */
	public void createProgramSprinklerPanel() {
		programSprinklerPanel = new JPanel();
		programSprinklerPanel.setLayout(new BorderLayout());
		createZoneSelectionPanel();
		programSprinklerPanel.add(zoneSelectionPanel, BorderLayout.NORTH);
		createProgramInputPanel();
		programSprinklerPanel.add(programInputPanel, BorderLayout.CENTER);
		createSubmitButtonPanel();
		programSprinklerPanel.add(submitButtonPanel, BorderLayout.SOUTH);
	}

	/**
	 * This creates the Zone selection Panel in the Program Input Panel
	 */
	public void createZoneSelectionPanel() {
		zoneSelectionPanel = new JPanel();
		zoneLabel = new JLabel("Zone: ");
		String[] zones = gardenController.getGarden().getZoneNames();
		zoneComboBox = new JComboBox<String>(zones);
		zoneSelectionPanel.add(zoneLabel);
		zoneSelectionPanel.add(zoneComboBox);
	}

	/**
	 * This creates the Program Input Panel
	 */
	public void createProgramInputPanel() {
		programInputPanel = new JPanel();
		programInputPanel.setLayout(new GridLayout(3, 1));

		createTimeConfigPanel();
		programInputPanel.add(timeConfigPanel);
		createTemperatureConfigPanel();
		programInputPanel.add(temperatureConfigPanel);

		createWaterConfigPanel();
		programInputPanel.add(waterConfigPanel);
	}

	/**
	 * This creates the Panel for Submit button in Program Input Panel
	 */
	public void createSubmitButtonPanel() {
		submitButtonPanel = new JPanel();
		submitButton = new JButton("Submit");
		submitButtonPanel.add(submitButton);
		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isValidData()) {
					setProgramDataForZone();
					resetProgramInputPanel();
				}
			}
		});
	}

	/**
	 * This validates the data entered by the user in the Program Input Panel
	 */
	public boolean isValidData() {
		String strMinTemp = minTempTextField.getText().trim();
		String strMaxTemp = maxTempTextField.getText().trim();
		String strStartTime = startTimeTextField.getText().trim();
		String strStopTime = stopTimeTextField.getText().trim();
		if ((strMinTemp != null && !strMinTemp.isEmpty() && !strMinTemp.matches("[0-9]+"))
				|| (strMaxTemp != null && !strMaxTemp.isEmpty() && !strMaxTemp.matches("[0-9]+"))) {
			JOptionPane.showMessageDialog(null, "Invalid temperature! Temperature should be in whole numbers");
			return false;
		}
		if ((strMinTemp.length() > 0 && (Integer.valueOf(strMinTemp) < 40 || Integer.valueOf(strMinTemp) > 110))
				|| (strMaxTemp.length() > 0
						&& (Integer.valueOf(strMaxTemp) < 40 || Integer.valueOf(strMaxTemp) > 110))) {
			JOptionPane.showMessageDialog(null, "Temperarture range is 40 to 110");
			return false;
		}
		if (strMinTemp.length() > 0 && strMaxTemp.length() > 0) {
			int minTemp = Integer.valueOf(strMinTemp);
			int maxTemp = Integer.valueOf(strMaxTemp);
			if (maxTemp <= minTemp) {
				JOptionPane.showMessageDialog(null, "Maximum temperature should be greater than minimum temperature");
				return false;
			}
		}
		if (strStartTime == null || strStartTime.isEmpty() || strStopTime == null || strStopTime.isEmpty()) {
			JOptionPane.showMessageDialog(null, "Start time and stop time cannt be left blank");
			return false;

		}
		if ((strStartTime.length() > 0 && !strStartTime.matches("([0-1]\\d|2[0-3]):([0-5]\\d)"))
				|| (strStopTime.length() > 0 && !strStopTime.matches("([0-1]\\d|2[0-3]):([0-5]\\d)"))) {
			JOptionPane.showMessageDialog(null, "Invalid time! Time should be entered in hh:mm format");
			return false;
		}
		if (strStartTime.length() > 0 && strStopTime.length() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			try {
				Date d1 = sdf.parse(strStartTime);
				Date d2 = sdf.parse(strStopTime);
				long elapsedTime = d2.getTime() - d1.getTime();
				if (elapsedTime < 0) {
					JOptionPane.showMessageDialog(null, "Stop time should be greater than start time");
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	/**
	 * This creates the Time Configuration Panel of Program Input Panel
	 */
	public void createTimeConfigPanel() {
		timeConfigPanel = new JPanel();
		timeConfigPanel.setLayout(new GridLayout(2, 4));
		timeConfigPanel.setBorder(new TitledBorder("Time"));
		durationLabel = new JLabel("Duration: ");
		timeConfigPanel.add(durationLabel);

		timeComboBox = new JComboBox(new String[] { "Daily" });
		timeConfigPanel.add(timeComboBox);

		dayLabel = new JLabel("Day: ");
		String[] days = new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
		dayComboBox = new JComboBox(days);
		timeConfigPanel.add(dayLabel);
		timeConfigPanel.add(dayComboBox);
		dayLabel.setVisible(false);
		dayComboBox.setVisible(false);

		startTimeLabel = new JLabel("Start Time (hh:mm):");
		timeConfigPanel.add(startTimeLabel);
		startTimeTextField = new JTextField();
		startTimeTextField.setToolTipText("Enter the Start Time for the Sprinkler(s)");
		startTimeTextField.setText("");
		startTimeTextField.setColumns(10);
		timeConfigPanel.add(startTimeTextField);

		stopTimeLabel = new JLabel("  Stop Time (hh:mm):");
		timeConfigPanel.add(stopTimeLabel);

		stopTimeTextField = new JTextField();
		stopTimeTextField.setToolTipText("Enter the Stop Time for the Sprinkler(s)");
		stopTimeTextField.setText("");
		stopTimeTextField.setColumns(10);
		timeConfigPanel.add(stopTimeTextField);

		timeComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				String time = (String) cb.getSelectedItem();
				if (time == "Day") {
					dayLabel.setVisible(true);
					dayComboBox.setVisible(true);
				} else {
					dayLabel.setVisible(false);
					dayComboBox.setVisible(false);

				}
			}
		});

	}

	/**
	 * This creates the Temperature Configuration Panel of Program Input Panel
	 */
	public void createTemperatureConfigPanel() {
		temperatureConfigPanel = new JPanel(new GridLayout(2, 1));
		JPanel panel1 = new JPanel(new GridLayout(1, 4));
		temperatureConfigPanel.setBorder(new TitledBorder("Temperature"));
		minTempLabel = new JLabel("Min Temperature (°F): ");
		panel1.add(minTempLabel);

		minTempTextField = new JTextField("");
		minTempTextField.setToolTipText("Enter the Minimum temperature in degree fahrenheit");
		minTempTextField.setColumns(10);
		panel1.add(minTempTextField);
		maxTempLabel = new JLabel("  Max Temperature (°F): ");
		panel1.add(maxTempLabel);

		maxTempTextField = new JTextField("");
		maxTempTextField.setToolTipText("Enter the maximum temperature in degree fahrenheit");
		maxTempTextField.setColumns(10);
		panel1.add(maxTempTextField);

		temperatureConfigPanel.add(panel1);
		JPanel panel2 = new JPanel();
		JLabel tempInstructionLabel = new JLabel("<html><i>[Temperature range: 40 to 110]</i></html>");
		panel2.add(tempInstructionLabel);
		temperatureConfigPanel.add(panel1);
		temperatureConfigPanel.add(panel2);
	}

	/**
	 * This creates the Water Configuration Panel of Program Input Panel
	 */
	public void createWaterConfigPanel() {
		waterConfigPanel = new JPanel();
		waterConfigPanel.setBorder(new TitledBorder("Water"));
		waterFlowLabel = new JLabel("Water Flow (litres/hour): ");
		waterConfigPanel.add(waterFlowLabel);
		waterFlowTextField = new JTextField();

		String[] waterRateArray = new String[] { "10", "20", "30", "40", "50" };
		waterRateComboBox = new JComboBox(waterRateArray);
		waterRateComboBox.setSelectedIndex(2);
		waterConfigPanel.add(waterRateComboBox);
	}

	/**
	 * This sets the data for the selected zone as input by the user.
	 */
	public void setProgramDataForZone() {
		String zoneIdString = (String) zoneComboBox.getSelectedItem();
		gardenController.setZoneForProgramming(zoneIdString);
		DateFormat sdf = new SimpleDateFormat("hh:mm");
		String startTime = startTimeTextField.getText();// "15:30:18";
		String stopTime = stopTimeTextField.getText();
		String minTemp = minTempTextField.getText();
		String maxTemp = maxTempTextField.getText();
		if (minTemp.length() < 3 && minTemp.matches("[0-9]+")) {
		}
		if (maxTemp.length() < 3 && maxTemp.matches("[0-9]+")) {
		}
		String wterRate = (String) waterRateComboBox.getSelectedItem();
		gardenController.setWaterRate(wterRate);
		gardenController.saveTimeTemperature(startTime, stopTime, minTemp, maxTemp);
		gardenController.saveProgramDataForZone();
	}

	/**
	 * This creates the Temperature Slider
	 */
	public void createTemperatureSlider() {

		temperatureSlider = new JSlider();
		temperatureSlider.setBackground(new Color(255, 255, 255));
		temperatureSlider.setForeground(new Color(0, 0, 0));
		temperatureSlider.setValue(70);
		temperatureSlider.setMaximum(110);
		temperatureSlider.setMinimum(40);
		temperatureSlider.setMajorTickSpacing(10);
		temperatureSlider.setToolTipText("simulate environmental temperature\n");
		temperatureSlider.setPaintTicks(true);
		temperatureSlider.setPaintLabels(true);
		temperatureSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {

				JSlider currentTempSlider = (JSlider) e.getSource();
				int selectedTemp = currentTempSlider.getValue();
				gardenController.setCurTemperature(selectedTemp);

			}
		});
	}

	/**
	 * This resets the Program Input Panel
	 */
	public void resetProgramInputPanel() {
		zoneComboBox.setSelectedIndex(0);
		timeComboBox.setSelectedIndex(0);
		startTimeTextField.setText("");
		stopTimeTextField.setText("");
		minTempTextField.setText("");
		maxTempTextField.setText("");
		waterRateComboBox.setSelectedIndex(2);

	}
}
