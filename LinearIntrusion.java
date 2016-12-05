
public class LinearIntrusion implements IntrusionAlgorithm 
{
	@Override
	public String[] getParameterNames()
	{
		return null;
	}

	@Override
	public void setParameters(double[] values)
	{
		// No parameters needed
	}
	
	@Override
	public double[] doNextMove(double x, double y)
	{
		return new double[]{x+1, y};
	}

	@Override
	public void resetAlg() {
		// TODO Auto-generated method stub
		
	}
}
