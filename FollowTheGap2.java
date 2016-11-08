import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;


public class FollowTheGap2 implements IntrusionAlgorithm
{
	public RunSim run;
	
	public FollowTheGap2(RunSim r)
	{
		run = r;
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
//		return null;
		
//		finds that cats with centers in the range of the intruders sensing range
		ArrayList<Double> catArray = new ArrayList<Double>();
//		ArrayList<Cat> foundCats = new ArrayList<Cat>();
		double[] gapArray;
		double baseAngle;
		for(Cat cat : run.cats)
		{
			if(cat.hasParent() && run.intruderSensingRange >= Math.sqrt(Math.pow((cat.getY()-y),2)+Math.pow((cat.getX()-x),2)) && x < cat.getX())
			{
				baseAngle = Math.atan((y-cat.getY())/(cat.getX()-x));
				if(catArray.size() == 0)
				{
					catArray.add(0, baseAngle);
//					foundCats.add(cat);
				}
				else 
				{
					for(int i=0; i<catArray.size(); i++)
					{
						if(baseAngle >= catArray.get(i))
						{
							catArray.add(i,baseAngle);
//							foundCats.add(i,cat);
							break;
						}
						else if(i == catArray.size()-1)
						{
							catArray.add(baseAngle);
//							foundCats.add(cat);
							break;
						}
					}
				}
			}
		}
		if(catArray.size() == 0)
		{
			System.out.println("0");
			double[] ret = {x+1,y};
			return ret;
		}
		
		if(catArray.get(0) < Math.PI/2 || run.h-y < run.intruderSensingRange && !(catArray.get(0) < Math.PI/2 && run.h-y < run.intruderSensingRange))
		{
			catArray.add(0, Math.PI/2);
//			foundCats.add(0,new Cat(x+1, y-runSim.intruderSensingRange+1,false));
		}
		
		if(catArray.get(catArray.size()-1) > -1*Math.PI/2 || y < run.intruderSensingRange && !(catArray.get(catArray.size()-1) > -1*Math.PI/2 && y < run.intruderSensingRange))
		{
			catArray.add(catArray.size(),-1*Math.PI/2);
//			foundCats.add(new Cat(x+1,y+runSim.intruderSensingRange-1,false));
		}
//		catArray.trimToSize();
		gapArray = new double[catArray.size()-1];
		for (int i = 1; i < catArray.size(); i++)
		{
			gapArray[i-1] = catArray.get(i-1)-catArray.get(i);
		}

		int index = minimumIndex(gapArray);
		double headingAngle = (catArray.get(index)+catArray.get(index+1))/2;
//		double phi1 = catArray.get(index).y;
//		double phi2 = catArray.get(index+1).x;
//		double d1 = Math.sqrt(Math.pow(foundCats.get(index).getY()-y, 2)+Math.pow(foundCats.get(index).getX()-x, 2));
//		double d2 = Math.sqrt(Math.pow(foundCats.get(index+1).getY()-y, 2)+Math.pow(foundCats.get(index+1).getX()-x, 2));
//		double headingAngle = Math.acos((d1+d2*Math.cos(phi1+phi2))/(Math.sqrt(d1*d1+d2*d2+2*d1*d2*Math.cos(phi1+phi2))))-phi1;
		double newX = (double)(x+Math.cos(headingAngle));
//		double newX = x+1;
		double newY = (double)(y-Math.sin(headingAngle));
		if(newY < 0)
			newY = 0;
		else if(newY > run.h)
			newY = run.h;
		double[] ret = {newX, newY};
		System.out.println(headingAngle*180/Math.PI);
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
