import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class csvOut {
    private String data;
        
        public csvOut() {
            this.data = "";
           
	}

	public void append(String data) {
		    
            this.data = this.data + data + "\n";
            System.out.print("Appended: " + data);
	}
	
        //public void close(String name, String[] header){
		public void close(RunSim run){
        	FileWriter fw;
        	String[] header =  {"Trial #", "Success", "Distance Traveled", "Successes", 
    				"Total Distance", "Average Distance"};
        	String name = "rs1_" + (int)run.sensingRange1 + "_rc1_" + (int)run.communicationRange1 + "_n1_" + run.sensorCount1 + "_rs2_" + (int)run.sensingRange2 + "_rc2_" + (int)run.communicationRange2 + "_n2_" + run.sensorCount2 +  "_t_" + run.detectionThreshold + "_w_" + run.w + "_h_" + run.h + "_" + run.mouseAlgorithmType;
        	
            String filename = name + ".csv";
            try{
                System.out.println("Opening file " + filename);
                fw = new FileWriter(new File(filename));
                
                for(int i = 0; i < header.length; i++)
                	fw.write(header[i] + ",");
                	
                fw.write("\n");
                
                fw.write(data);
                fw.write(System.lineSeparator());
                fw.close();
                System.out.println("Wrote output to " + filename);
            } catch (IOException ex) {
		ex.printStackTrace();
            }
            data = "";
        }
}