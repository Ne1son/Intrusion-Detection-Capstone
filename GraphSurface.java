import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JPanel;


@SuppressWarnings("serial")
public class GraphSurface extends JPanel //implements ActionListener
{	
	// animation time delay
	// private int DELAY = 15;
	
	// timer that controls animation
	// private Timer timer;

	// variables for taking an average of how far the mouse has gotten
	// private int totalDistance = 0;
	// public int iterations = 0;

	// data for the pause at the end of each run
	// final private int ENDPAUSE = 150;
	// private int running = 0;

	public RunSim run;
	public int savingStart;
	public WSNFrame wsnFrame;
	public int start;
	public int end;
	public int increment;
	public String metric;
	public String yAxisType;
	
// constructor
	public GraphSurface(int savingStartID, int startSpot, int endSpot, int incrementValue, String metricValue, String yAxis) //, WSNFrame frame)
	{
		// run = new RunSim(0, 0, frame);
		savingStart = savingStartID;
		// wsnFrame = frame;
		start = startSpot;
		end = endSpot;
		increment = incrementValue;
		metric = metricValue;
		yAxisType = yAxis;
		// initTimer();
	}

// polymorphism for the constructor that takes an id of a simulation in the database,
// calls a function that gets the info and fills all the Surface properties,
// and then starts running
	// public Surface(int id, WSNFrame frame)
	// {
	// 	run = new RunSim(id, frame);
	// 	initTimer();
	// }
	
// starts the timer
// 	private void initTimer() 
// 	{
// 		timer = new Timer(DELAY, this);
// 		timer.start();
// 	}
	
// // gets the timer
// 	public Timer getTimer() 
// 	{	
// 		return timer;
// 	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D gd = (Graphics2D)g;
		Rectangle2D.Double rect = new Rectangle2D.Double(0,0,500,500);
		gd.setPaint(Color.white);
		gd.fill(rect);
		double probs[] = new double[(int)(end-start)/increment+1];
		int iterations =0;
		int successes = 0;
		// System.out.println((end-start)/increment);
		for(int i = 0 ; i < (end-start)/increment+1; i++)
		{
			Connection c = null;
			Statement stmt = null;
			try
			{
				Class.forName("org.sqlite.JDBC");
				c = DriverManager.getConnection("jdbc:sqlite:WSNSimulations.db");
				c.setAutoCommit(false);
				
				// querying goes here!
				stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT * FROM sim_trials WHERE sim_id == "+(savingStart+i)+";");
				iterations = 0;
				successes = 0;
				while(rs.next())
				{
					iterations++;
					successes += (rs.getInt("success") == 1 ? 1 : 0);
				}

				if(iterations == 0)
				{
					System.out.println(""+(savingStart+i)+" : doesnt work");
					probs[i] = 0;
				}
				else
				{
					switch(yAxisType)
					{
						case "Intruder Success Rate":
							probs[i] = (double)successes / iterations;
							break;
						
						case "Sensor Success Rate":
							probs[i] = (double)(1 - (double)successes / iterations);
							break;
					}
					// System.out.println(successes +" / "+iterations +" = "+ probs[i]);
				}
				rs.close();
				stmt.close();
				// c.close();
			} 
			catch ( Exception e )
			{
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		}

		// actual graphing now

		// draw outline and axes
		gd.setPaint(Color.black);
		gd.setStroke(new BasicStroke(2));
		gd.drawLine(0,500,500,500);
		gd.drawLine(500,0,500,500);
		gd.setStroke(new BasicStroke(1));
		gd.drawLine(10,490,480,490);
		gd.drawLine(10,490,10,10);

		// draw axis labels
		gd.setColor(Color.BLACK);
		gd.setPaint(Color.BLACK);
		gd.drawString(metric,250,10);
		AffineTransform orig = gd.getTransform();
		gd.rotate(-Math.PI/2);
		gd.drawString("Probability of Success",0,250);
		gd.setTransform(orig);

		// draw points and connecting line
		int range = end - start;
		
		// System.out.println(Arrays.toString(probs)+" "+probs.length);

		for (int i=0; i<probs.length; i++)
		{
			double x1 = 10 + (double)(i*increment)/range * 480;
			double y1 = 490 - probs[i] * 480;
			
			Ellipse2D.Float circle = new Ellipse2D.Float((float)x1, (float)y1, 5f, 5f);
			gd.fill(circle);

			if(i < probs.length-1)
			{
				double x2 = 10 + (double)((i+1)*increment)/range * 480;
				double y2 = 490 - probs[i+1] * 480;
				gd.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
			}
		}
	}

	// makes the animation happen
	// @Override
	// public void actionPerformed(ActionEvent e) 
	// {
	// 	repaint();
	// }	
}
