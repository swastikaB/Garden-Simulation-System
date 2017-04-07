package bhat.gupta.hummingbee.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import bhat.gupta.hummingbee.model.Garden;
import bhat.gupta.hummingbee.model.Sprinkler;
import bhat.gupta.hummingbee.model.Zone;

public class ViewStatusPanel extends JPanel implements Observer{
	JTextArea statusTextArea;
	JPanel statusZonePanel,statusDisplayPanel;
	JLabel zoneLabel; 
	JComboBox statusZoneComboBox;
	JLabel[] statusLabels;
	private Garden garden;
	
	public ViewStatusPanel(Garden observableGarden)
	{
		
		this.garden=observableGarden;
		this.garden.addObserver(this);
		setLayout(new BorderLayout());
		createZoneSelectionPanelForStatus();
		add(statusZonePanel, BorderLayout.NORTH);
		setToolTipText("View the Status of the Sprinklers");
		createStatusDisplayPanel();
		add(statusDisplayPanel, BorderLayout.CENTER);
	}
	
	/**
	 * This returns the zone for the given zoneId
	 * @param zoneId id of the zone
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
	 * This returns a string that concatenates the status of all the sprinklers in the zone having the given zoneId
	 * @param zoneId id of the zone
	 * @return A string that concatenates the status of all the sprinklers in the zone having the given zoneId
	 */
	public String getZoneStatus(String zoneId) {
		Zone z = getZoneFromZoneIdString(zoneId);
		String status = "";
		for (Sprinkler s : z.getZoneSprinklerList()) {
			status+=s.getStatus()+",";
		}
		status=status.substring(0, status.length()-1);
		return status;
	}

	/**
	 * This creates the zone Selection panel for view status tab
	 */
	public void createZoneSelectionPanelForStatus() {
		
		statusZonePanel = new JPanel();
		zoneLabel = new JLabel("Zone: ");
		String[] zones = this.garden.getZoneNames();

		statusZoneComboBox = new JComboBox(zones);
		statusZoneComboBox.setSelectedIndex(0);
		statusZonePanel.add(zoneLabel);
		statusZonePanel.add(statusZoneComboBox);
		statusZoneComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				String zoneId = (String) cb.getSelectedItem();
				String strStatus=getZoneStatus(zoneId);
				String[] strMessages=strStatus.split(",");
				for(int i=0;i<statusLabels.length;i++)
				{
					statusLabels[i].setText(strMessages[i]);
				}

			}
		});

	}

	/**
	 * This creates the status display panel
	 */
	public void createStatusDisplayPanel() {
		statusLabels=new JLabel[12];
		statusDisplayPanel = new JPanel(new GridLayout(5, 3, 20, 20));
		statusDisplayPanel.setBackground(Color.WHITE);
		statusDisplayPanel.setBorder(new TitledBorder("Status"));
		JLabel[] headingLabel=new JLabel[3];
		headingLabel[0]=new JLabel("<html><b>Sprinkler ID</b></html>");
		headingLabel[0].setHorizontalAlignment(SwingConstants.CENTER);
		headingLabel[1]=new JLabel("<html><b>Functional/Non Functional</b></html>");
		headingLabel[1].setHorizontalAlignment(SwingConstants.CENTER);
		headingLabel[2]=new JLabel("<html><b>ON/OFF</b></html>");
		headingLabel[2].setHorizontalAlignment(SwingConstants.CENTER);
		statusDisplayPanel.add(headingLabel[0]);
		statusDisplayPanel.add(headingLabel[1]);
		statusDisplayPanel.add(headingLabel[2]);
		for(int i=0;i<statusLabels.length;i++)
		{
			statusLabels[i]=new JLabel();
			statusLabels[i].setHorizontalAlignment(SwingConstants.CENTER);
			statusLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
			statusDisplayPanel.add(statusLabels[i]);
		}
		reloadStatusLabels();
	}
	
	/**
	 * This reloads the status labels
	 */
	public void reloadStatusLabels()
	{
		String strStatus=getZoneStatus((String)(statusZoneComboBox.getSelectedItem()));
		String[] strMessages=strStatus.split(",");
		for(int i=0;i<statusLabels.length;i++)
		{
			statusLabels[i].setText(strMessages[i]);
		}
	}
	@Override
	public void update(Observable o, Object arg) {
		
		reloadStatusLabels();
	}
	
	

}
