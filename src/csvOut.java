import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class csvOut {
	private String filename;
	private String[] header;
    private String data;

	public csvOut(String filename, String[] header) {
		this.filename = filename;
		this.header = header;
		//this.data = "";
	}
        
        public csvOut(String filename) {
            this.filename = filename;
            //this.data = "";
            this.header[0] = "";
	}

	public void append(String data) {
		    
            this.data = this.data + data + "\n";
            System.out.print("Appended: " + data);
	}
	
        public void close(int files){
        	FileWriter fw;
        	
            String filename = this.filename + new Integer(files).toString() + ".csv";
            try{
                System.out.println("Opening file " + filename);
                fw = new FileWriter(new File(filename));
                
                for(int i = 0; i < header.length; i++)
                	fw.write(this.header[i] + ",");
                	
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
