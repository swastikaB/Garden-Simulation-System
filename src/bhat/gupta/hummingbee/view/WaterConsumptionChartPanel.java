package bhat.gupta.hummingbee.view;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JPanel;

public class WaterConsumptionChartPanel extends JPanel {
	private int panelHeight;
	private int panelWidth,barPanelWidth,legendPanelWidth;
	private static final int BORDER_GAP = 35;
	private static final Stroke GRAPH_STROKE = new BasicStroke(3f);
	private static final int GRAPH_POINT_WIDTH = 8;
	private static final int MAX_DAYS = 7;
	private Map<String, Color> graphColorByZone;
	private Map<String, Map<String, Double>> dayVolumeMapByZones;
	Map<String, List<Point>> graphPointsByZone;
	private int max_Volume;
	private Graphics2D g2;

	public WaterConsumptionChartPanel(Map<String, Map<String, Double>> dayVolumeMapByZones) {
		createGraphColorByZoneMap();
		this.dayVolumeMapByZones = dayVolumeMapByZones;
		this.graphPointsByZone = new LinkedHashMap<String, List<Point>>();
		findMaxVolume();

	}

	/**
	 * This creates a map having the mapping of the zone and the color
	 */
	public void createGraphColorByZoneMap() {
		this.graphColorByZone = new HashMap<String, Color>();
		this.graphColorByZone.put("EAST", Color.GREEN);
		this.graphColorByZone.put("WEST", Color.RED);
		this.graphColorByZone.put("NORTH", Color.BLUE);
		this.graphColorByZone.put("SOUTH", Color.GRAY);
	}

	/**
	 * This finds the maximum volume among all the entries in the dayVoumeMapByZones 
	 * and sets the current max_Volume of this WaterConsumptionCalculator
	 */
	public void findMaxVolume() {
		int overall_max = Integer.MIN_VALUE;
		for (Map<String, Double> map : this.dayVolumeMapByZones.values()) {
			double val = Collections.max(map.values());
			int max = (int) val;
			if (max > overall_max)
				overall_max = max;
		}
		this.max_Volume = overall_max;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g2 = (Graphics2D) g;
		Dimension df = this.getSize();
		panelHeight = (int) df.getHeight();
		panelWidth = (int) df.getWidth();
		barPanelWidth=panelWidth / 4 * 3;
		legendPanelWidth = panelWidth / 4;

		createLineGraph();
		createLegend();

	}
	/**
	 * This creates the line graph
	 */
	public void createLineGraph()
	{
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		double xScale = ((double) barPanelWidth - BORDER_GAP) / MAX_DAYS;
		double yScale = ((double) getHeight() - 2 * BORDER_GAP)
				/ (this.max_Volume - 1);

		for (Entry<String, Map<String, Double>> entry : this.dayVolumeMapByZones
				.entrySet()) {
			Map<String, Double> dayVolumeMap = this.dayVolumeMapByZones
					.get(entry.getKey());

			List<Point> graphPoints = new ArrayList<Point>();
			int i = 0;
			for (Entry<String, Double> e : dayVolumeMap.entrySet()) {
				int x1 = (int) (i * xScale + BORDER_GAP);
				int vol = (e.getValue()).intValue();
				int y1 = (int) ((max_Volume - vol) * yScale + BORDER_GAP);
				graphPoints.add(new Point(x1, y1));
				i++;
			}
			graphPointsByZone.put(entry.getKey(), graphPoints);
		}

		// create x and y axes
		int axisStartPointX = BORDER_GAP;
		int axisStartPointY = getHeight() - BORDER_GAP;
		int xAxisEndPointX = getWidth()-legendPanelWidth;
		int xAxisEndPointY = getHeight() - BORDER_GAP;
		int yAxisEndPointX = BORDER_GAP;
		int yAxisEndPointY = BORDER_GAP;
		g2.drawLine(axisStartPointX, axisStartPointY, xAxisEndPointX, xAxisEndPointY);
		g2.drawLine(axisStartPointX, axisStartPointY, yAxisEndPointX, yAxisEndPointY);
		
		// Write Axis labels
				Font font = new Font("Arial", Font.ITALIC, 12);
				g2.setFont(font);
				g2.setPaint(Color.BLACK);
				FontMetrics metrics = g2.getFontMetrics(font);
				String xAxisName = "DAYS OF THE WEEK";
				int textWidthHf = metrics.stringWidth(xAxisName) / 2;
				int textAscent = metrics.getMaxAscent();
				int startPointX = (barPanelWidth/2)-textWidthHf;
				g2.drawString(xAxisName, startPointX, getHeight() - textAscent);
				
				String yAxisName="litres ";
				textWidthHf = metrics.stringWidth(yAxisName) / 2;
				startPointX = BORDER_GAP-metrics.stringWidth(yAxisName);
				g2.drawString(yAxisName, startPointX, getHeight()/2);

		Stroke oldStroke = g2.getStroke();
		for (Entry<String, List<Point>> entry : graphPointsByZone.entrySet()) {
			String zone = entry.getKey();
			g2.setColor(this.graphColorByZone.get(zone));
			g2.setStroke(GRAPH_STROKE);
			List<Point> graphPoints = entry.getValue();
			for (int i = 0; i < graphPoints.size() - 1; i++) {
				int x1 = graphPoints.get(i).x;
				int y1 = graphPoints.get(i).y;
				int x2 = graphPoints.get(i + 1).x;
				int y2 = graphPoints.get(i + 1).y;
				g2.drawLine(x1, y1, x2, y2);
			}
			g2.setStroke(oldStroke);
			g2.setColor(this.graphColorByZone.get(zone));
			for (int i = 0; i < graphPoints.size(); i++) {
				int x = graphPoints.get(i).x - GRAPH_POINT_WIDTH / 2;
				int y = graphPoints.get(i).y - GRAPH_POINT_WIDTH / 2;
				;
				int ovalW = GRAPH_POINT_WIDTH;
				int ovalH = GRAPH_POINT_WIDTH;
				g2.fillOval(x, y, ovalW, ovalH);
			}

		}
	}
	
	/** 
	 * This creates the legend for the line graph
	 */
	public void createLegend() {

		// create Font for Account name
		Font font = new Font("SansSerif", Font.BOLD, 12);
		g2.setFont(font);

		// get FontMetrics for calculating offsets and
		// positioning descriptions
		FontMetrics metrics = getFontMetrics(font);
		int ascent = metrics.getMaxAscent();
		int startX = barPanelWidth;
		int startY = BORDER_GAP;
		int spaceBtwnLegends = 30;

		// draw description for each data point
		for(Entry<String,Color> e:this.graphColorByZone.entrySet())
		{
			g2.setColor(e.getValue());
			g2.fillRect(startX, startY, ascent, ascent);
			// Write Legend Description
			int startX2 = startX + ascent * 2;
			g2.setColor(Color.black);
			g2.drawString(e.getKey(), startX2, startY+ascent);
			startY = startY + spaceBtwnLegends;
		}

	}

}
