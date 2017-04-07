package bhat.gupta.hummingbee.view;



import bhat.gupta.hummingbee.controller.GardenController.ZoneId;
import bhat.gupta.hummingbee.model.Garden;
import bhat.gupta.hummingbee.model.Sprinkler;
import bhat.gupta.hummingbee.model.Zone;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class ViewGardenPanel extends JPanel implements Observer {

	private Graphics2D g2d;
	private final String ACTIVE_SPRINKLER_IMG = "/bhat/gupta/hummingbee/resources/ActiveSprinkler.jpg";
	private final String INACTIVE_SPRINKLER_IMG = "/bhat/gupta/hummingbee/resources/InactiveSprinkler.jpg";
	private final String WATER_SPRINKLER_IMG = "/bhat/gupta/hummingbee/resources/WaterSprinkler1.jpg";
	private int panelWidth, panelHeight, zoneHeight, zoneWidth, columnWidth, imageHeight;
	private BufferedImage activeImage, inactiveImage, waterSprinkler;
	private Timer timer;
	private boolean initialize;
	private int startX, startY, endX1, endX2, endY1, endY2;
	private int numOfSprinklers;
	private Map<ZoneId, ArrayList<Boolean>> sprinklerConditionMap;
	private Garden garden;

	public ViewGardenPanel(Garden observableGarden) {
		garden = observableGarden;
		garden.addObserver(this);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2d = (Graphics2D) g;

		// initialize few required variables
		initializeVariables();

		// give background garden color
		setBackgroundColorForGarden();

		// draw zone boundary lines
		drawBoundaryLines();

		// Write the zone names
		writeZoneNames();

		// draw sprinklers in each zone
		drawSprinklers();
	}

	/*
	 * This initialize few member variables
	 */
	private void initializeVariables() {
		Dimension df = this.getSize();
		panelWidth = (int) df.getWidth();
		panelHeight = (int) df.getHeight();
		zoneHeight = panelHeight / 2;
		zoneWidth = panelWidth / 2;
		columnWidth = panelWidth / 10;
	}

	/*
	 * This draws a rectangle of screen size and fill it with green gradient
	 */
	private void setBackgroundColorForGarden() {

		int startPointX = 0;
		int startPointY = 0;

		int endPointX = panelWidth;
		int endPointY = panelHeight;
		Color startColor = new Color(50, 120, 44);
		Color endColor = new Color(140, 210, 133);

		boolean isCyclic = true;
		g2d.setPaint(new GradientPaint(startPointX, startPointY, startColor, endPointX, endPointY, endColor, isCyclic));
		g2d.fill(getVisibleRect());
	}

	/*
	 * This draws the separating lines for the four zones
	 *
	 */
	private void drawBoundaryLines() {
		int startX, startY, endX, endY;
		g2d.setPaint(Color.black);
		// draw vertical line
		startX = panelWidth / 2;
		startY = 0;
		endX = panelWidth / 2;
		endY = panelHeight;
		Line2D lineV = new Line2D.Double(startX, startY, endX, endY);
		g2d.draw(lineV);

		// draw horizontal line
		startX = 0;
		startY = panelHeight / 2;
		endX = panelWidth;
		endY = panelHeight / 2;

		Line2D lineH = new Line2D.Double(startX, startY, endX, endY);
		g2d.draw(lineH);
		g2d.fill(lineH);
	}

	/*
	 * This writes the names of each zone.
	 */
	private void writeZoneNames() {
		int startX, startY;
		int fontAscent = g2d.getFontMetrics().getMaxAscent();
		Font font = g2d.getFont();
		FontMetrics metrics = g2d.getFontMetrics(font);
		String nZoneText = "North Zone";
		int textWidthHf = metrics.stringWidth(nZoneText) / 2;
		// Write North Zone
		startX = (panelWidth / 4 - textWidthHf);
		startY = panelHeight / 2 - fontAscent;
		g2d.drawString(nZoneText, (int) startX, (int) startY);

		// Write East Zone
		startX = ((3 * panelWidth) / 4 - textWidthHf);
		startY = panelHeight / 2 - fontAscent;
		g2d.drawString("East Zone", (int) startX, (int) startY);

		// Write West Zone
		startX = ((panelWidth) / 4 - textWidthHf);
		startY = panelHeight - fontAscent;
		g2d.drawString("West Zone", (int) startX, (int) startY);

		// Write South Zone
		startX = ((3 * panelWidth) / 4 - textWidthHf);
		startY = panelHeight - fontAscent;
		g2d.drawString("South Zone", (int) startX, (int) startY);

	}

	/*
	 * This draws sprinklers in each Zone
	 */
	private void drawSprinklers() {

		activeImage = createImageFromFile(ACTIVE_SPRINKLER_IMG);
		inactiveImage = createImageFromFile(INACTIVE_SPRINKLER_IMG);
		waterSprinkler = createImageFromFile(WATER_SPRINKLER_IMG);
		imageHeight = activeImage.getHeight();

		// draw the image 4 times in each zone

		startX = columnWidth;
		startY = panelHeight / 2 - imageHeight - 30;
		int sprinklerCounter = 0;
		/*
		 * Sorting the Zones according to their Zone Id for convenience of drawing
		 */
		Collections.sort(garden.getZones(), (Zone r1, Zone r2) ->
		r1.getGroupId().compareTo(r2.getGroupId()));
		for (Zone zone : garden.getZones()) {
			for (Sprinkler s : zone.getZoneSprinklerList()) {
				BufferedImage image = zone.isOn() ? waterSprinkler : activeImage;
				if (!s.isFunctional()) {
					image = inactiveImage;
				}
				g2d.drawImage(image, startX, startY, null);
				startX += columnWidth;
				if (sprinklerCounter < 7) { // For north and east zone
					if (sprinklerCounter == 3) {
						startX += columnWidth; // to skip a place and move to
												// the next
												// zone (from north to east)
					}
				} else if (sprinklerCounter == 7) { // to skip from (north and
													// east) to (west and
					// south) zone
					startX = columnWidth;
					startY = panelHeight - imageHeight - 30;
				} else if (sprinklerCounter == 11) {
					startX += columnWidth; // to skip a place and move to the
											// next
											// zone (from west to south)
				}
				sprinklerCounter++;
			}
		}
	}
	
	

	/*
	 * This retrieves active and inactive sprinkler image from files
	 */
	private BufferedImage createImageFromFile(String path) {
		BufferedImage image;
		try {
			// read from file in working directory
			File file = new File(path);
			if (file.isFile()) {
				image = ImageIO.read(file);
			}
			// or read from file in same directory as this .class file
			else {
				URL url = getClass().getResource(path);
				if (url == null) {
					url = new URL(path);
				}
				image = ImageIO.read(url);
			}

		} catch (IOException e) {
			throw new RuntimeException("Could not open file: " + path);
		}
		return image;
	}
	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}
	
	
}
