import java.sql.*;

public class SQLiteJDBC
{
	public static void main( String args[] )
	{

		String ass = "balls,,";
		System.out.println(ass.matches("/W"));

		Connection c = null;
		Statement stmt = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");

			// querying goes here!
			stmt = c.createStatement();
			String sql = "CREATE TABLE sim_config2 ( "+
					"id INTEGER PRIMARY KEY AUTOINCREMENT,"+
					 "sensor_num INT NOT NULL ,"+
					 "detection_range INT NOT NULL ,"+
					 "sensor_move_alg VARCHAR(50) NULL ,"+
					 "intruder_move_alg VARCHAR(50) NULL ,"+
					 "communication_range INT NOT NULL ,"+
					 "detection_type VARCHAR(50) NOT NULL ,"+
					 "intruder_detect_range INT NULL ,"+
					 "animated BOOLEAN NULL ,"+
					 "times_ran INT NULL ,"+
					 "successes_num INT NULL ,"+
					 "field_height INT NOT NULL ,"+
					 "field_width INT NOT NULL ,"+
					 "intruder_size INT NULL ,"+
					 "intruder_start INT NULL ,"+
					 "max_iterations INT NULL ,"+
					 "base_sensor_loc INT NULL "+
				 ")";

			stmt.executeUpdate(sql);//works for everything but select statements
			stmt.close();
			c.close();
		}
		catch ( Exception e ) 
		{
			try
			{
				String test = "java.sql.SQLException: table sim_config already exists";
				if(test.equals(e.getClass().getName() + ": " + e.getMessage()))
				{
					String sql = "INSERT INTO sim_config(sensor_num, detection_range, communication_range, detection_type, field_height, field_width) VALUES(3, 450, 345, 'things', 234, 234)";
					stmt.executeUpdate(sql);
					stmt.close();
				}
				else
				{
					System.err.println( e.getClass().getName() + ": " + e.getMessage() );
					System.exit(0);
				}
			}
			catch(Exception d)
			{
				System.err.println( d.getClass().getName() + ": " + d.getMessage() );
				System.exit(0);
			}
		}
		System.out.println("Opened database successfully");
	}
}