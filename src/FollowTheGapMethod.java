import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;


public class FollowTheGapMethod implements IntrusionAlgorithm
{
	final RunSim runSim;
	
	public FollowTheGapMethod(RunSim r)
	{
		runSim = r;
	}

	@Override
	public String[] getParameterNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParameters(double[] values) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double[] doNextMove(double x, double y) {
		
//		finds that cats with centers in the range of the intruders sensing range
		ArrayList<Point.Double> catArray = new ArrayList<Point.Double>();
//		ArrayList<Cat> foundCats = new ArrayList<Cat>();
		double[] gapArray;
		double baseAngle;
		double edgeAngle;
		for(Cat cat : runSim.cats)
		{
			//if(cat.hasParent() && runSim.intruderSensingRange >= Math.sqrt(Math.pow((cat.getY()-y),2)+Math.pow((cat.getX()-x),2)) && x < cat.getX())
			double dist = Point.distance(x, y, cat.getX(), cat.getY());
			runSim.intruderSensingRange = cat.getSensingRange() + 10;
			if(cat.isOn() && dist < runSim.intruderSensingRange && x < cat.getX())
			{
				baseAngle = Math.atan((y-cat.getY())/(cat.getX()-x));
				edgeAngle = Math.atan(cat.getSensingRange()/(Math.sqrt(Math.pow((cat.getY()-y),2)+Math.pow((cat.getX()-x),2))));
				if(catArray.size() == 0)
				{
					catArray.add(0,new Point.Double(baseAngle+edgeAngle,baseAngle-edgeAngle));
//					foundCats.add(cat);
				}
				else 
				{
					for(int i=0; i<catArray.size(); i++)
					{
						if(baseAngle+edgeAngle >= catArray.get(i).x)
						{
							catArray.add(i,new Point.Double(baseAngle+edgeAngle,baseAngle-edgeAngle));
//							foundCats.add(i,cat);
							break;
						}
						else if(i == catArray.size()-1)
						{
							catArray.add(new Point.Double(baseAngle+edgeAngle,baseAngle-edgeAngle));
//							foundCats.add(cat);
							break;
						}
					}
				}
			}
		}
		if(catArray.size() == 0)
		{
			double[] ret = {x+1,y};
			return ret;
		}
		
		if(catArray.get(0).x < Math.PI/2 || runSim.h-y < runSim.intruderSensingRange && !(catArray.get(0).x < Math.PI/2 && runSim.h-y < runSim.intruderSensingRange))
		{
			catArray.add(0, new Point.Double(-1,Math.PI/2));
//			foundCats.add(0,new Cat(x+1, y-runSim.intruderSensingRange+1,false));
		}
		
		if(catArray.get(catArray.size()-1).y > -1*Math.PI/2 || y < runSim.intruderSensingRange && !(catArray.get(catArray.size()-1).y > -1*Math.PI/2 && y < runSim.intruderSensingRange))
		{
			catArray.add(catArray.size(), new Point.Double(-1*Math.PI/2,-1));
//			foundCats.add(new Cat(x+1,y+runSim.intruderSensingRange-1,false));
		}
//		catArray.trimToSize();
		gapArray = new double[catArray.size()-1];
		for (int i = 1; i < catArray.size(); i++)
		{
			gapArray[i-1] = catArray.get(i-1).y-catArray.get(i).x;
		}

		int index = minimumIndex(gapArray);
		double headingAngle = (catArray.get(index).y+catArray.get(index+1).x)/2;
//		double phi1 = catArray.get(index).y;
//		double phi2 = catArray.get(index+1).x;
//		double d1 = Math.sqrt(Math.pow(foundCats.get(index).getY()-y, 2)+Math.pow(foundCats.get(index).getX()-x, 2));
//		double d2 = Math.sqrt(Math.pow(foundCats.get(index+1).getY()-y, 2)+Math.pow(foundCats.get(index+1).getX()-x, 2));
//		double headingAngle = Math.acos((d1+d2*Math.cos(phi1+phi2))/(Math.sqrt(d1*d1+d2*d2+2*d1*d2*Math.cos(phi1+phi2))))-phi1;
		double newX = (double)(x+2*Math.cos(headingAngle));
//		double newX = x+1;
		double newY = (double)(y-2*Math.sin(headingAngle));
		if(newY < 0)
			newY = 0;
		else if(newY > runSim.h)
			newY = runSim.h;
		double[] ret = {newX, newY};
		return ret;
	}
	
	private int minimumIndex(double[] array)
	{
		double temp = array[0];
		int tempIndex = 0;
		for(int i=0; i < array.length; i++)
		{
			if(temp < array[i])
			{
				temp = array[i];
				tempIndex = i;
			}
		}
		return tempIndex;
	}

	@Override
	public void resetAlg() {
		// TODO Auto-generated method stub
		
	}
	
}
