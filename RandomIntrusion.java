import java.util.Random;

public class RandomIntrusion implements IntrusionAlgorithm
{
	// Algorithm parameters;
	private int maxRange = 180;		// Angle range intruder can move within (e.g. 180 is between -90 and 90)
	private int minDist  = 5;		// Minimum distance the intruder can move in a straight line
	private int maxDist  = 25;		// Maximum distance the intruder can move in a straight line
	
	private Random rng;				// Random number generator
	private float moveDist;
	private double moveAng;

	public RandomIntrusion()
	{
		rng = new Random();
	}
	
	@Override
	public String[] getParameterNames()
	{
		return new String[] {
			"Max Range",
			"Min Distance",
			"Max Distance"
		};
	}

	@Override
	public void setParameters(double[] values)
	{
		try
		{
			maxRange = (int)values[0];
			minDist  = (int)values[1];
			maxDist  = (int)values[2];
		}
		catch(Exception e)
		{
			maxRange = 180;
			minDist  = 5;	
			maxDist  = 25;	
		}
	}
	
	@Override
	public double[] doNextMove(double x, double y)
	{
		if (moveDist <= 0)
		{
			moveAng = rng.nextDouble() * maxRange - 90;
			moveDist = rng.nextInt(maxDist-minDist) + minDist;
		}
		else
			moveDist--;
		
		double r = Math.toRadians(moveAng);
		x += Math.cos(r);
		y += Math.sin(r);
		
		return new double[]{x,y};
	}

	@Override
	public void resetAlg() {
		// TODO Auto-generated method stub
		
	}
}
