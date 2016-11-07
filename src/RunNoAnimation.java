public class RunNoAnimation
{
	public RunSim run;
	public int iterations;
	private int files = 1;

	public RunNoAnimation(int x, int y, WSNFrame fram)
	{
		run = new RunSim(x,y,fram);
	}

	public RunNoAnimation(int id, WSNFrame fram)
	{
		run = new RunSim(id, fram);
	}

	public void run()
	{
		// boolean finished = false;
		iterations = 0;
		int times = 0;
		while(iterations+1 <= run.maxIterations)
		{
			int caught = run.doSimulationIteration();
			if(caught == -1 && !run.mouseSuccess() && caught != 0)
			{
				times++;
				// this only happens when the mouse isnt caught or succeeding
			}
			else
			{
				System.out.println(times);
				times = 0;
				run.saveTrial(iterations++);
			}
		}
		
		run.csv.close(files);
		files++;
	}

	public void runForGraph()
	{
		// boolean finished = false;
		iterations = 0;
		while(iterations+1 <= run.maxIterations)
		{
			int caught = run.doSimulationIteration();
			if(caught == -1 && !run.mouseSuccess() && caught != 0)
			{
				// this only happens when the mouse isnt caught or succeeding
			}
			else
			{
				// System.out.println("Save Trail: "+run.wsnFrame.saving);
				// System.out.println(run.mouse.getX());
				run.saveTrial(iterations++, run.wsnFrame.saving);
				run.resetSurface();
			}
		}
		
		run.csv.close(files);
		files++;
	}

	public boolean graphData(String metric, int start, int end, int increment)
	{
		if(end-start <= increment)
			return false;
		for(int i = start; i <= end; i += increment)
		{
			if(metric.equals("Sensor Count"))
				run.sensorCount = i;
			else if(metric.equals("Detection Range"))
				run.sensingRange = i;
			else if(metric.equals("Communication Range"))
				run.communicationRange = i;
			else
			{
				// System.out.println("Return false.");
				return false;
			}
			// run.saveConfig();
			run.wsnFrame.saving++;
			run.saveConfig();
			runForGraph();
			// in order to always get the end value
			if(i+increment > end && i != end)
				i = end-increment;
		}
		return true;
	}
}
