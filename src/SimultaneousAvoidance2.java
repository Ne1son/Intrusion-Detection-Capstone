import java.awt.Point;

public class SimultaneousAvoidance2 implements IntrusionAlgorithm {

	private RunSim run;
	private int[][] knownCatMoves;
	private int currentCat;
	private byte quadrant;
	private boolean onWall = false;
	
	public SimultaneousAvoidance2(RunSim runSim)
	{
		run = runSim;
		knownCatMoves = new int[run.cats.length][];
		currentCat = -1;
		quadrant = 0;
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
		
		int numFront = 0;
		int numUp = 0;
		int numBack = 0;
		int numDown = 0;
		double dist;
		byte newQuadrant = 0;
		for(int i = 0; i < run.cats.length; i++)
		{
			dist = Point.distance(x,y,run.cats[i].getX(),run.cats[i].getY());
			run.intruderSensingRange = run.cats[i].getSensingRange() + 1;
			if(dist <= run.intruderSensingRange)
			{
				if(numFront == 0 && Point.distance(x+1,y,run.cats[i].getX(),run.cats[i].getY()) <= run.cats[i].getSensingRange())
					numFront = i;
	
				if(numDown == 0 && Point.distance(x,y+1,run.cats[i].getX(),run.cats[i].getY()) <= run.cats[i].getSensingRange())
					numDown = i;
	
				if(numUp == 0 && Point.distance(x,y-1,run.cats[i].getX(),run.cats[i].getY()) <= run.cats[i].getSensingRange())
					numUp = i;
				
				if(numBack == 0 && Point.distance(x-1,y,run.cats[i].getX(),run.cats[i].getY()) <= run.cats[i].getSensingRange())
					numBack = i ;
			}
			
			if(dist <= run.intruderSensingRange && knownCatMoves[i] == null)
			{
				knownCatMoves[i] = new int[4];
				knownCatMoves[i][0] = -2;
				knownCatMoves[i][1] = -2;
				knownCatMoves[i][2] = -2;
				knownCatMoves[i][3] = -2;
										
			}
		}
		
		if(currentCat == -1)
		{
			if(numFront == 0)
				return new double[]{x+1,y};
			
			currentCat = numFront;
			
			quadrant = findQuadrant(currentCat,x,y);
			
			if(knownCatMoves[currentCat] != null && knownCatMoves[currentCat][quadrant-1] == -2)
				knownCatMoves[currentCat][quadrant-1] = quadrant == 2 ? -1 : 1;
			
			if(quadrant == 2 && numUp == 0)
				return new double[]{x,y-1};
			if(quadrant == 3 && numDown == 0)
				return new double[]{x,y+1};
			if(numBack == 0)
			{
				return new double[]{x-1,y};
			}
		}
		else if(currentCat != -1)
		{
			newQuadrant = findQuadrant(currentCat,x,y);
			if(!onWall && (y == run.h || y == 0))
			{
				knownCatMoves[currentCat][quadrant-1] *= -1;
				onWall = true;
			}
			else if(onWall && y < run.h && y > 0)
				onWall = false;
			
			if(numFront == 0 && (newQuadrant == 1 && quadrant == 4) || (newQuadrant == 4 && quadrant == 1))
			{
				knownCatMoves[currentCat][newQuadrant-1] = newQuadrant == 4 ? -1 : 1;
				quadrant = 0;
				currentCat = -1;
				return new double[]{x+1,y};
			}
			else
			{
				if(quadrant == 1)
				{
					if(knownCatMoves[currentCat][quadrant-1] == -1)
					{
						if(newQuadrant == 2)
						{
							if(knownCatMoves[currentCat][newQuadrant-1] != 1)
							{
								quadrant = 2;
								knownCatMoves[currentCat][quadrant-1] = 1;
							}
							else
								knownCatMoves[currentCat][quadrant-1] *= -1;
							return new double[]{x,y};
						}
						
						if(numBack == 0)
							return new double[]{x-1,y};
						else if(numUp == 0)
							return new double[]{x,y-1};
						else
						{
							byte q = findQuadrant(numUp,x,y);
							if(knownCatMoves[numUp] != null && knownCatMoves[numUp][q-1] != (q == 3 || q == 2 ? 1 : -1))
							{
								currentCat = numUp;
								quadrant = q;
								knownCatMoves[numUp][quadrant-1] = (quadrant == 3 || quadrant == 2) ? 1 : -1;
//								might put something here in order to fix the intruder getting stuck in a cave
							}
							else
							{
								knownCatMoves[currentCat][quadrant-1] *= -1;
								return new double[]{x,y};
							}
						}
					}
					else
					{
						if(newQuadrant == 4)
						{
							if(knownCatMoves[currentCat][newQuadrant-1] != 1)
							{
								quadrant = 4;
								knownCatMoves[currentCat][quadrant-1] = 1;
							}
							else
								knownCatMoves[currentCat][quadrant-1] *= -1;
							return new double[]{x,y};
						}
						else
						{
							if(numDown == 0)
								return new double[]{x,y+1};
							else if(numFront == 0)
								return new double[]{x+1,y};
							else
							{
								byte q = findQuadrant(numFront,x,y);
								if(knownCatMoves[numFront] != null && knownCatMoves[numFront][q-1] != (q == 2 || q == 3 ? -1 : 1))
								{
									currentCat = numFront;
									quadrant = q;
									knownCatMoves[numFront][quadrant-1] = quadrant == 2 || q == 3 ? -1 : 1;
								}
								else
								{
									knownCatMoves[currentCat][quadrant-1] *= -1;
									return new double[]{x,y};
								}
							}
						}
					}
				}
				else if(quadrant == 2)
				{
					if(knownCatMoves[currentCat][quadrant-1] == -1)
					{
						if(newQuadrant == 1)
						{
							if(knownCatMoves[currentCat][newQuadrant-1] != 1)
							{
								quadrant = 1;
								knownCatMoves[currentCat][quadrant-1] = 1;
							}
							else
								knownCatMoves[currentCat][quadrant-1] *= -1;
							return new double[]{x,y};
						}
						
						if(numFront == 0)
							return new double[]{x+1,y};
						else if(numUp == 0)
							return new double[]{x,y-1};
						else
						{
							byte q = findQuadrant(numUp, x, y);
							if(knownCatMoves[numUp] != null && knownCatMoves[numUp][q-1] != (q == 4 ? 1 : -1))
							{
								currentCat = numUp;
								quadrant = q;
								knownCatMoves[numUp][quadrant-1] = quadrant == 4 ? 1 : -1;
							}
							else
							{
								knownCatMoves[currentCat][quadrant-1] *= -1;
								return new double[]{x,y};
							}
						}
					}
					else
					{
						if(newQuadrant == 3)
						{
							if(knownCatMoves[currentCat][newQuadrant-1] != 1)
							{
								quadrant = 3;
								knownCatMoves[currentCat][quadrant-1] = 1;
							}
							else
								knownCatMoves[currentCat][quadrant-1] *= -1;
							return new double[]{x,y};
						}
						else
						{
							if(numDown == 0)
								return new double[]{x,y+1};
							else if(numBack == 0)
								return new double[]{x-1,y};
							else
							{
								byte q = findQuadrant(numBack, x, y);
								if(knownCatMoves[numBack] != null && knownCatMoves[numBack][q-1] != (q == 2 ? 1 : -1))
								{
									currentCat = numBack;
									quadrant = q;
									knownCatMoves[numBack][quadrant-1] = quadrant == 2 ? 1 : -1;
								}
								else
								{
									knownCatMoves[currentCat][quadrant-1] *= -1;
									return new double[]{x,y};
								}
							}
						}
					}
				}
				else if(quadrant == 3)
				{
					if(knownCatMoves[currentCat][quadrant-1] == 1)
					{
						if(newQuadrant == 4)
						{
							if(knownCatMoves[currentCat][newQuadrant-1] != -1)
							{
								quadrant = 4;
								knownCatMoves[currentCat][quadrant-1] = -1;
							}
							else
								knownCatMoves[currentCat][quadrant-1] *= -1;
							return new double[]{x,y};
						}
						
						if(numFront == 0)
							return new double[]{x+1,y};
						else if(numDown == 0)
							return new double[]{x,y+1};
						else
						{
							byte q = findQuadrant(numDown,x,y);
							if(knownCatMoves[numDown] != null && knownCatMoves[numDown][q-1] != (q == 1 ? -1 : 1))
							{
								currentCat = numDown;
								quadrant = q;
								knownCatMoves[numDown][quadrant-1] = quadrant == 2 ? 1 : -1;
							}
							else
							{
								knownCatMoves[currentCat][quadrant-1] *= -1;
								return new double[]{x,y};
							}
						}
					}
					else
					{
						if(newQuadrant == 2)
						{
							if(knownCatMoves[currentCat][newQuadrant-1] != -1)
							{
								quadrant = 2;
								knownCatMoves[currentCat][quadrant-1] = -1;
							}
							else
								knownCatMoves[currentCat][quadrant-1] *= -1;
							return new double[]{x,y};
						}
						else
						{
							if(numUp == 0)
								return new double[]{x,y-1};
							else if(numBack == 0)
								return new double[]{x-1,y};
							else
							{
								byte q = findQuadrant(numBack,x,y);
								if(knownCatMoves[numBack] != null && knownCatMoves[numBack][q-1] !=  (q == 3 ? -1 : 1))
								{
									currentCat = numBack;
									quadrant = q;
									knownCatMoves[numBack][quadrant-1] = quadrant == 3 ? -1 : 1;
								}
								else
								{
									knownCatMoves[currentCat][quadrant-1] *= -1;
									return new double[]{x,y};
								}
							}
						}
					}
				}
				else if(quadrant == 4)
				{
					if(knownCatMoves[currentCat][quadrant-1] == -1)
					{
						if(newQuadrant == 1)
						{
							if(knownCatMoves[currentCat][newQuadrant-1] != -1)
							{
								quadrant = 1;
								knownCatMoves[currentCat][quadrant-1] = -1;
							}
							else
								knownCatMoves[currentCat][quadrant-1] *= -1;
							return new double[]{x,y};
						}
						
						if(numUp == 0)
							return new double[]{x,y-1};
						else if(numFront == 0)
							return new double[]{x+1,y};
						else
						{
							byte q = findQuadrant(numFront,x,y);
							if(knownCatMoves[numFront] != null && knownCatMoves[numFront][q-1] != (q == 2 || q == 3 ? 1 : -1))
							{
								currentCat = numFront;
								quadrant = q;
								knownCatMoves[numFront][quadrant-1] = quadrant == 3 || quadrant == 2 ? 1 : -1;
//								might put something here in order to fix the intruder getting stuck in a cave
							}
							else
							{
								knownCatMoves[currentCat][quadrant-1] *= -1;
								return new double[]{x,y};
							}
						}
					}
					else
					{
						if(newQuadrant == 3)
						{
							if(knownCatMoves[currentCat][newQuadrant-1] != -1)
							{
								quadrant = 3;
								knownCatMoves[currentCat][quadrant-1] = -1;
							}
							else
								knownCatMoves[currentCat][quadrant-1] *= -1;
							return new double[]{x,y};
						}
						else
						{
							if(numBack == 0)
								return new double[]{x-1,y};
							else if(numDown == 0)
								return new double[]{x,y+1};
							else
							{
								byte q = findQuadrant(numDown,x,y);
								if(knownCatMoves[numDown] != null && knownCatMoves[numDown][q-1] != (q == 2 ? -1 : 1))
								{
									currentCat = numDown;
									quadrant = q;
									knownCatMoves[numDown][quadrant-1] = quadrant == 2 ? -1 : 1;
								}
								else
								{
									knownCatMoves[currentCat][quadrant-1] *= -1;
									return new double[]{x,y};
								}
							}
						}
					}
				}
			}
			
		}
		
		return new double[]{x,y};
	}

	private byte findQuadrant(int cat, double x, double y)
	{
		double xCat = run.cats[cat].getX();
		double yCat = run.cats[cat].getY();
		if(y <= yCat)
		{
			if(x <= xCat)
			{
				return 2;
			}
			else
			{
				return 1;
			}
		}
		else
		{
			if(x <= xCat)
			{
				return 3;
			}
			else
			{
				return 4;
			}
		}
	}

	@Override
	public void resetAlg() 
	{
		knownCatMoves = new int[run.cats.length][];
		currentCat = -1;
		quadrant = 0;
	}
	
}

