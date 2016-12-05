import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class csvOut {
    private String data;
    private boolean clearOut;
        
        public csvOut(boolean clearOutput) {
            this.data = "";
            
            clearOut = clearOutput;
           
	}

	public void append(String data) {
		    
            this.data = this.data + data + "\n";
            System.out.print("Appended: " + data);
	}
	
        public void close(String name, String[] header){
        	FileWriter fw;
        	
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
            
            if(clearOut)
            	data = "";
        }
}