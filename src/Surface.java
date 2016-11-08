import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;


@SuppressWarnings("serial")
public class Surface extends JPanel implements ActionListener
{	
	// animation time delay
	private int DELAY = 15;
	
	// timer that controls animation
	private Timer timer;

	// variables for taking an average of how far the mouse has gotten
	private int totalDistance = 0;
	public int iterations = 0;

	// data for the pause at the end of each run
	final private int ENDPAUSE = 150;
	private int running = 0;
	private int steps = 0;

	public RunSim run;
	
	// draw variables
	public boolean drawComRng = true;
	public boolean drawComLines = false;
	public int drawSnsOpacity = 125;
	
// constructor
	public Surface(int x, int y, WSNFrame frame)
	{
		run = new RunSim(x, y, frame);
		initTimer();
	}

// polymorphism for the constructor that takes an id of a simulation in the database,
// calls a function that gets the info and fills all the Surface properties,
// and then starts running
	public Surface(int id, WSNFrame frame)
	{
		run = new RunSim(id, frame);
		initTimer();
	}
	
// starts the timer
	private void initTimer() 
	{
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
// gets the timer
	public Timer getTimer() 
	{	
		return timer;
	}
	
// draws all of the run.cats in the simulation
	private void drawCats(Graphics2D gd)
	{
		//System.out.print(run.cats[25].getSensingRange() + "\n");
		for(int i = 1; i < run.cats.length; i++){
			run.cats[i].drawCat(new Color(255,0,0,drawSnsOpacity), new Color(0,0,255,drawSnsOpacity), run.cats[i].getSensingRange(), gd);
			run.cats[0].drawCat(new Color(0,255,0,drawSnsOpacity), run.cats[0].getSensingRange(), gd);
		}
		// 	run.cats[i].drawCat(Color.red, Color.blue, run.sensingRange, gd);
		// run.cats[0].drawCat(Color.green, run.sensingRange, gd);
	}

// draws the run.communication radii of all the run.cats in the network
	private void drawCatsCommunication(Color color, Graphics2D gd)
	{
		run.cats[0].drawCatCommunication(color, run.communicationRange, gd);
		for(int i = 1; i < run.cats.length; i++)
			if(run.cats[i].hasParent())
				run.cats[i].drawCatCommunication(color, run.communicationRange, gd);
	}

// draws all connections of all run.cats in the network
	// is currently not being used anywhere
	/*
	private void drawConnections(Color color, Graphics2D gd)
	{
		gd.setPaint(color);
		for(int i=0; i<run.cats.length; i++)
			if(run.cats[i].hasParent())
				gd.drawLine(Math.round(run.cats[i].getX()),Math.round(run.cats[i].getY()),Math.round(run.cats[i].getParent().getX()),Math.round(run.cats[i].getParent().getY()));
	}
	*/
//	private void drawGrid(Graphics2D gd)
//	{
//		gd.setColor(Color.black);
//		
//		int[] nm = ((AStarIntrusion)run.mouse.alg).getColsRows();
//		int n = nm[0];
//		int m = nm[1];
//		
//		float size = run.w/n;
//		for (int c=0; c<n+1; c++)
//			gd.drawLine((int)(c*size), 0, (int)(c*size), run.h);
//		
//		for (int r=0; r<m+1; r++)
//			gd.drawLine(0, (int)(r*size), run.w, (int)(r*size));
//		
//		double[][] sensorField = ((AStarIntrusion)run.mouse.alg).getFieldGrid();
//		for (int c=0; c<n; c++)
//		{
//			for (int r=0; r<m; r++)
//			{
//				double value = (double)Math.round(sensorField[c][r] * 100) / 100;
//				String data = ""+value;
//				double[] pos = ((AStarIntrusion)run.mouse.alg).cellToPoint(c, r);
//				gd.drawChars(data.toCharArray(), 0, data.length(), (int)pos[0] - 10, (int)pos[1]);
//			}
//		}
//	}
	
	private void drawConnections(Color color, Graphics2D gd)
	{
		gd.setPaint(color);
		for (int i=0; i<run.cats.length; i++)
		{
			for (int j=0; j<run.cats.length; j++)
			{
				if (i != j)
				{
					double dist = Point.distance(run.cats[i].getX(), run.cats[i].getY(), run.cats[j].getX(), run.cats[j].getY());
					if (dist <= run.communicationRange)
					{
						gd.drawLine((int)run.cats[i].getX(), (int)run.cats[i].getY(), (int)run.cats[j].getX(), (int)run.cats[j].getY());
					}
				}
			}
		}
	}

	private void drawDetectingSensors(Graphics2D gd, int[] sensors)
	{
		for (int i=0; i<sensors.length; i++)
			run.cats[sensors[i]].drawCat(new Color(255,0,255,drawSnsOpacity), run.cats[sensors[i]].getSensingRange(), gd);
		
		gd.setPaint(Color.black);
		gd.setStroke(new BasicStroke(2));
		
		int n = sensors.length;
		if (n == 2)
			n = 1;
		else if (n < 2)
			n = 0;
		
		for (int i=0; i<n; i++)
		{
			int x1 = (int)run.cats[sensors[i]].getX();
			int y1 = (int)run.cats[sensors[i]].getY();
			
			int j = (i+1)%sensors.length;
			int x2 = (int)run.cats[sensors[j]].getX();
			int y2 = (int)run.cats[sensors[j]].getY();
			gd.drawLine(x1, y1, x2, y2);
		}
	}
	
	private void drawIntruderPath(Graphics2D gd)
	{
		gd.setPaint(Color.red);
		gd.setStroke(new BasicStroke(2));
		
		ArrayList<Point> travelPoints = run.mouse.getPathTraveled();
		switch(run.mouseAlgorithmType)
		{
			case "Linear":
				gd.drawLine(0, (int)run.mouseStart, (int)run.mouse.getX(), (int)run.mouse.getY());
				break;
			
			default:
				for (int i=1; i<travelPoints.size(); i++)
				{
					Point p1 = travelPoints.get(i-1);
					Point p2 = travelPoints.get(i);
					gd.drawLine(p1.x, p1.y, p2.x, p2.y);
				}
		}
	}
	
// draws the path from the node that detected the mouse to the base node
	private void drawDetectionPath(int i, Graphics2D gd)
	{
		Cat u = run.cats[i];
		gd.setPaint(Color.black);
		gd.setStroke(new BasicStroke(5));
		while(u.hasParent())
		{
			gd.drawLine(Math.round(u.getParent().getX()),Math.round(u.getParent().getY()),Math.round(u.getX()),Math.round(u.getY()));
			u = u.getParent();
		}
		u = run.cats[i];
		while(u.hasParent())
		{
			u.drawCat(Color.black,10,gd);
			u=u.getParent();
		}
		run.cats[0].drawCat(Color.black,10,gd);
	}

	private boolean drawSimulationIteration(Graphics2D gd, int caught)
	{
		gd.setPaint(Color.black);
		gd.setStroke(new BasicStroke(2));
		gd.drawLine(0,run.h,run.w,run.h);
		gd.drawLine(run.w,0,run.w,run.h);
		gd.setStroke(new BasicStroke(1));
		
		if (drawComRng)
			drawCatsCommunication(new Color(100,100,100,100), gd);
		
		if (drawComLines)
			drawConnections(Color.BLACK, gd);
		
		drawCats(gd);
		
//		drawGrid(gd);
		
		drawIntruderPath(gd);
		run.mouse.drawMouse(Color.black,gd);
		// gd.drawString(Integer.toString(totalDistance/iterations),10,10);	
		// if the mouse is not detected by a cat, or detected by a cat not in the network, but it is also not at the end, then run this piece
		if(caught == -1 && !run.mouseSuccess() && caught != 0)
		{
			// this only happens when the mouse isnt caught or succeeding
			// mouse.moveNext();
			gd.setPaint(Color.black);
		}
		else
		{
			// this happens when the mouse is caught
			if(!run.mouseSuccess())
				// drawDetectionPath(caught, gd);
				drawDetectingSensors(gd, run.caughtArray);
			return false;
		}
		return true;
	}

	// calls the animation and allows for the Graphics2D object
	private boolean doneRunning = false;
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(iterations+1 <= run.maxIterations)
		{
			doneRunning = true;
			int caught = run.doSimulationIteration();
			steps++;
			if(!drawSimulationIteration((Graphics2D)g, caught))
			{
				if(running++ > ENDPAUSE || run.mouseSuccess())
				{
					running = 0;
					System.out.println(steps);
					steps = 0;
					run.saveTrial(iterations++);
				}
			}
		}
		else
		{
			if(doneRunning)
			{
				run.wsnFrame.setOptions(true);
				run.wsnFrame.resetButtons();
				run.updateSettings();
				doneRunning = false;
			}
		}
	}

	// makes the animation happen
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		repaint();
	}	
}
