
public class RunNoAnimation {

    public RunSim run;
    public int iterations;
    private int files = 1;
    
    public csvOut finalCSV;

    public RunNoAnimation(int x, int y, WSNFrame fram) {
        run = new RunSim(x, y, fram);
        finalCSV = new csvOut(false);
    }

    public RunNoAnimation(int id, WSNFrame fram) {
        run = new RunSim(id, fram);
        finalCSV = new csvOut(false);
    }

    public void run() {
        // boolean finished = false;
        iterations = 0;
        int times = 0;
        while (iterations + 1 <= run.maxIterations) {
            int caught = run.doSimulationIteration();
            if (caught == -1 && !run.mouseSuccess() && caught != 0) {
                times++;
                // this only happens when the mouse isnt caught or succeeding
            } else {
                System.out.println(times);
                times = 0;
                run.saveTrial(iterations++);
            }
            
        }
        
        finalCSV.append(run.lastLine);
        System.out.println("Here" + run.lastLine);
        String[] header =  {"Trial #", "Success", "Distance Traveled", "Successes", 
				"Total Distance", "Average Distance"};        
        String fileName = "Rs1" + (int)run.sensingRange1 + " Rc1" + (int)run.communicationRange1 + " N1" + run.sensorCount1 + 
				  " Rs2" + (int)run.sensingRange2 + " Rc2" + (int)run.communicationRange2 + " N2" + run.sensorCount2 +  
				  " T" + run.detectionThreshold + " IDR" + run.intruderSensingRange + " " + run.mouseAlgorithmType;
		
		run.csv.close(fileName, header);

        String[] finalHeader = {"Algorithm", "Rs1", "Rc1", "N1", "Rs2", "Rc2", "N2", "T", "IDR", "Avg Dist", "Succ Ratio"};
		finalCSV.close("Master CSV", finalHeader);
    }

    public void runForGraph() {
        // boolean finished = false;
        iterations = 0;
        while (iterations + 1 <= run.maxIterations) {
            int caught = run.doSimulationIteration();
            if (caught == -1 && !run.mouseSuccess() && caught != 0) {
                // this only happens when the mouse isnt caught or succeeding
            } else {
				// System.out.println("Save Trail: "+run.wsnFrame.saving);
                // System.out.println(run.mouse.getX());
                run.saveTrial(iterations++, run.wsnFrame.saving);
                run.resetSurface();
            }
        }

        String[] header =  {"Trial #", "Success", "Distance Traveled", "Successes", 
				"Total Distance", "Average Distance"};
		
		run.csv.close("Rs1" + (int)run.sensingRange1 + " Rc1" + (int)run.communicationRange1 + " N1" + run.sensorCount1 + 
					 " Rs2" + (int)run.sensingRange2 + " Rc2" + (int)run.communicationRange2 + " N2" + run.sensorCount2 +  
					  " T" + run.detectionThreshold + " IDR" + run.intruderSensingRange + " " + run.mouseAlgorithmType, header);
    }

    public boolean graphData(String metric, int start, int end, int increment) {
        if (end - start <= increment) {
            return false;
        }
        for (int i = start; i <= end; i += increment) {
            if (metric.equals("Sensor Count")) {
                run.sensorCount1 = i;
            } else if (metric.equals("Detection Range")) {
                run.sensingRange1 = i;
            } else if (metric.equals("Communication Range")) {
                run.communicationRange1 = i;
            } else {
                // System.out.println("Return false.");
                return false;
            }
            // run.saveConfig();
            run.wsnFrame.saving++;
            //run.saveConfig();
            runForGraph();
            // in order to always get the end value
            if (i + increment > end && i != end) {
                i = end - increment;
            }
        }
        return true;
    }
}
