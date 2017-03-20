import java.awt.Point;
import java.util.LinkedList;

public class ReciprocalOrientation implements IntrusionAlgorithm {

	private RunSim run;
	private boolean intelligent = true;
	private int around = 1;
	
	public double dir = 0;
	public float speed = 1;
	
	public int sightIndex = -1;
	public double tangentX = -1;
	
	public ReciprocalOrientation(RunSim runSim)
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
	
	public int checkPoints(double x, double y) {
		int inSight = -1;
		for(int i = 0; i < run.cats.length; i++){
			double dist = Point.distance(x, y, run.cats[i].getX(), run.cats[i].getY());
			if(dist < run.cats[i].getSensingRange() &&  run.cats[i].isOn()){
				return i;
			}
		}
		return -1;
	}
	
	public double checkDir(double myDir, double intX, double intY){
		int i = 0;
		double retDir = myDir;
		int itr = 50;
		while (i < itr){
			double lookX = intX + (Math.cos(retDir) * i);
			double lookY = intY + (Math.sin(retDir) * i);
			int ret = checkPoints(lookX, lookY);
			if (ret > -1){
				double slope = (intY - run.cats[ret].getY()) / (intX - run.cats[ret].getX());
				retDir = (Math.atan(slope));
				
				double hyp = Math.sqrt( Math.pow(intX - run.cats[ret].getX(), 2) + Math.pow(intY - run.cats[ret].getY(), 2) );
				float rad = run.cats[ret].getSensingRange();
				
				if (intY < run.cats[ret].getY()){
					retDir = retDir - Math.asin(rad/hyp);
				} else {
					retDir = retDir + Math.asin(rad/hyp);
				}
				sightIndex = ret;
				tangentX = intX + (Math.cos(retDir) * (Math.sqrt(Math.pow(hyp, 2) - Math.pow(rad, 2))));
				
				retDir = checkDir(retDir, intX, intY); // stop doing the 50 points ahead!
				i = itr;
			} else {
				i = i + 1;
			}
		}
		return retDir;
	}

	@Override
	public double[] doNextMove(double x, double y) {
		double radius;
		
//		hard-coding this value for now
		intelligent = false;
		
		if(run.intruderSensingRange > run.sensingRange1)
		{
			if(intelligent)
				radius = run.sensingRange1 + 50;
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
			//if(dist < radius &&  run.cats[i].hasParent())
			if(dist < run.cats[i].getSensingRange() + 50 &&  run.cats[i].isOn() && dist < radius)
			{
				if(dist < minDist)
				{
					minDist = dist;
					minIndex = i;
				}
			}
		}
		
		if (sightIndex == -1){
			dir = checkDir(dir, x, y);
		} else {
			if (x > tangentX){
				sightIndex = -1;
				tangentX = -1;
			}
		}
		
		
		//System.out.println(dir);
		return new double[]{x + Math.cos(dir), y + Math.sin(dir)};
	}

	@Override
	public void resetAlg() {
		// TODO Auto-generated method stub
		
	}
}
