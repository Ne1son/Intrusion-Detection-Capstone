
public interface IntrusionAlgorithm 
{
	public abstract String[] getParameterNames();
	public abstract void setParameters(double[] values);
	public abstract double[] doNextMove(double x, double y);
	public abstract void resetAlg();
}
