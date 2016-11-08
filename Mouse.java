import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.*;
import java.util.ArrayList;

public class Mouse
{
	private double x = 0;
	private double y = 0;
	private String algorithmType = "Linear";
	private int fieldH;
	
	final private float sensingRange = 0; // not utilized yet

	private float stepSize = 1;
	private ArrayList<Point> travelPoints;
	public IntrusionAlgorithm alg;
	
	// polymorphism that sets the algorithm type
	public Mouse(double xPlace, double yPlace, String type, int fieldH, RunSim run)
	{
		x = xPlace;
		y = yPlace;
		// System.out.println("constructor"+type);
		algorithmType = type;
		this.fieldH = fieldH;
		travelPoints = new ArrayList<Point>();
		
		switch(type)
		{
			case "Random":
				alg = new RandomIntrusion();
				break;
			
//			case "A*":
//				alg = new AStarIntrusion(run);
//				break;
			
			case "FollowTheGapMethod":
				alg = new FollowTheGapMethod(run);
			break;
			
			case "GoAroundMethod":
				alg = new GoAroundMethod(run);
			break;
			
			case "SimultaneousAvoidance":
				alg = new SimultaneousAvoidance(run);
				break;
			
			case "FollowTheGap2":
				alg = new FollowTheGap2(run);
			break;
				
			case "Linear":
				alg = new LinearIntrusion();
			break;
			
			default:
				alg = new LinearIntrusion();
		}
	}
	
	// polymorphism that sets the place
	public Mouse(double xPlace, double yPlace)
	{
		x = xPlace;
		y = yPlace;
		travelPoints = new ArrayList<Point>();
	}

	// polymorphism that allows for not setting the place
	public Mouse()
	{
		x = 0;
		y = 0;
		travelPoints = new ArrayList<Point>();
	}

// getters and setters
	public double getX()
	{
		return x;
	}

	public double getY()
	{
		return y;
	}

	public void setX(float newX)
	{
		x = newX;
	}

	public void setY(float newy)
	{
		y = newy;
	}

	public void setStepSize(float step)
	{
		stepSize = step;
	}

	public float getStepSize()
	{
		return stepSize;
	}

	public String getAlgorithmType()
	{
		return algorithmType;
	}

	public void setAlgorithmType(String type)
	{
		algorithmType = type;
	}
	
	public void setAlgParameters(double[] values)
	{
		alg.setParameters(values);	
	}
	
	public void resetPath()
	{
		travelPoints = new ArrayList<Point>();
	}
	
	public ArrayList<Point> getPathTraveled()
	{
		return travelPoints;
	}
	
// the movement method for the mouse where the movement algorithms go
	public void moveNext()
	{
		double[] pos = alg.doNextMove(x, y);
		
//		if (pos[0] < 0)
//			x = 0;
//		else
			x = pos[0];
		
		if (pos[1] < 0)
			y = 0;
		else if (pos[1] > fieldH)
			y = fieldH;
		else
			y = pos[1];
		
		travelPoints.add(new Point((int)x, (int)y));
	}

// drawing method
	public void drawMouse(Color color, Graphics2D gd)
	{
		gd.setPaint(color);
		Ellipse2D.Float circle = new Ellipse2D.Float((float)(x-5), (float)(y-5), (float)5*2, (float)5*2);
		gd.fill(circle);
	}
	
	public ArrayList<Point> getTravels()
	{
		return travelPoints;
	}
}