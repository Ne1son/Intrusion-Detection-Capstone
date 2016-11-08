import java.awt.Point;
import java.util.LinkedList;

public class GoAroundMethod implements IntrusionAlgorithm {

	private RunSim run;
	private boolean intelligent = false;
	private int around = 1;
	
	public GoAroundMethod(RunSim runSim)
	{
		run = runSim;
	}
	
	@Override
	public String[] getParameterNames() {
		return new String[]{"Intelligent"};
	}

	@Override
	public void setParameters(double[] values) {
		try
		{
			if(values[0] != 0)
				intelligent = true;
		}
		catch(Exception e)
		{}
	}

	@Override
	public double[] doNextMove(double x, double y) {
		double radius;
		
//		hard-coding this value for now
		intelligent = true;
		
		if(run.intruderSensingRange > run.sensingRange)
		{
			if(intelligent)
				radius = run.sensingRange + 2;
			else
				radius = run.intruderSensingRange;
		}
		else
		{
			radius = run.intruderSensingRange;			
		}
		
		double minDist = 1000;
		int minIndex = 0;
		double dist;
		Cat cat;
		for(int i = 0; i < run.cats.length; i++)
		{
			dist = Point.distance(x, y, run.cats[i].getX(), run.cats[i].getY());
			if(dist < radius &&  run.cats[i].hasParent())
			{
				if(dist < minDist)
				{
					minDist = dist;
					minIndex = i;
				}
			}
		}
		double thing =  Math.acos((x-run.cats[minIndex].getX())/minDist);
		if(minIndex == 0 || thing < 0.00001)
			return new double[]{x+1,y};
		cat = run.cats[minIndex];
		if(/*y < cat.getY() ||*/ y == run.h || y == run.h-1)
			around = -1;
		else if(y == 1 || y == 0)
			around = 1;
		int xCoeff = around;
		if(cat.getY() > y)
			xCoeff = -1*around;
		Point.Double vectorDist = new Point.Double(cat.getX() - x, y - cat.getY());
		Point.Double perpVector = new Point.Double(around*-1*vectorDist.y/minDist, around*vectorDist.x/minDist);
		return new double[]{x+xCoeff*(float)perpVector.x, y+(float)perpVector.y};
	}

	@Override
	public void resetAlg() {
		// TODO Auto-generated method stub
		
	}
}
