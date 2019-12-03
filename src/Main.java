
import java.sql.*;
import java.util.Scanner;
// main function
public class Main {
  public static void main(String args[]) {
	  Scanner scanner = new Scanner(System.in);
	  try {
		    DBConnect connection = new DBConnect();
		    
		    
		    connection.initialization();
		    
		    // What kinds of partition?
		  	System.out.println("----Please choose any partitions as below----");
		  	System.out.println("1. ID");		  	
		  	System.out.println("2. Popularity");
		  	System.out.println("3. Trending");
		  	System.out.println("------------------------------------------");
		  	System.out.println("Please input a number. ( 1, 2 or 3) ");		
		    connection.setPartition(scanner.nextInt());
		    // What kinds of query?
		  	System.out.println("----Please choose any query as below----");
		  	System.out.println("1. Normal Query");
		  	System.out.println("2. Advanced Query");
		  	System.out.println("------------------------------------------");
		  	System.out.println("Please input a number. ( 1 or 2) ");
		  	int cmd = scanner.nextInt();
		  	String str;
		  	Scanner in = new Scanner(System.in);
		  	switch(cmd) {
		  		case 1:
					System.out.println("Please input a name of the song you wanna find? (Normal Query)");	  
					str = in.nextLine();  
					System.out.println("You wanna find：\n" + str );
					connection.queryAllTheData(str);
					break;
		  		case 2:
					System.out.println("Please input a name of the song you wanna find? (Advanced Query)");	  
					str = in.nextLine();  
					System.out.println("You wanna find：\n" + str );
					connection.queryPartitionData(str);
					break;
		  		case 3:
		  			break;
		  		default:
		  			System.out.println("You didn't choose any command!!");
		  			break;
		  	}
		  	
		  	in.close();
		  	System.out.println("End");
		  	 
	  }catch(Exception ex) {
		  System.out.println("Error: " + ex);
	 }
	  finally {
		  scanner.close();
	 }	  
  }
}

class DBConnect {
  private Connection con;
  private Statement st;
  private ResultSet rs;
  
  // DB connection
  public DBConnect() {
    try {
      // MYSQL connection
      Class.forName("com.mysql.cj.jdbc.Driver");
      con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Test", "root", "root");
      st = con.createStatement();

    } 
    catch (Exception ex) {
      System.out.println("Error: " + ex);
    }
  }
  
  // set the query log
  public void initialization() {
	  try {
		  String update = "SET Global slow_query_log = 'ON'; ";
		  st.executeQuery(update);
		  update = "SET GLOBAL log_output='TABLE'; ";
		  st.executeQuery(update);
		  update = "SET Global long_query_time = 0; ";
		  st.executeQuery(update);
		  update = "SET Global log_queries_not_using_indexes =1; ";
		  st.executeQuery(update);		  
	  }catch (Exception ex) {
	      System.out.println(ex);
	  }
  }
  
  
  // doing the partition
  public void setPartition(int num) {
	  try {
		  String update = null;
		  switch (num) {
		  	case 1:
		  		  update = "ALTER table songs PARTITION BY Hash(id) PARTITIONS 4;";
		  		  System.out.println("Partitioning based on the ID......");
				  break;		  
		  	case 2:
				  update = "	ALTER table songs\r\n" + 
				  		"	PARTITION BY range(artist_familiarity_int)\r\n" + 
				  		"	(\r\n" + 
				  		"		PARTITION p0 VALUES LESS THAN (25),\r\n" + 
				  		"        PARTITION p1 VALUES LESS THAN (50),\r\n" + 
				  		"        PARTITION p2 VALUES LESS THAN (75),\r\n" + 
				  		"		PARTITION p3 VALUES LESS THAN maxvalue\r\n" + 
				  		"	);";
				  System.out.println("Partitioning based on the Popularity......");
				  break;		  
		  	case 3:
				  update = "	ALTER table songs\r\n" + 
				  		"	PARTITION BY range(artist_hotttnesss_int)\r\n" + 
				  		"	(\r\n" + 
				  		"		PARTITION p0 VALUES LESS THAN (25),\r\n" + 
				  		"        PARTITION p1 VALUES LESS THAN (50),\r\n" + 
				  		"        PARTITION p2 VALUES LESS THAN (75),\r\n" + 
				  		"		PARTITION p3 VALUES LESS THAN maxvalue\r\n" + 
				  		"	);";
				  System.out.println("Partitioning based on the Trending......");
				  break;				  
			default:
				  update = "ALTER table songs PARTITION BY Hash(id) PARTITIONS 4;";
				  System.out.println("Partitioning based on the ID......");
				  break;
			      			
		  }
		  st.executeUpdate(update);	

	  } 
	  catch (Exception ex) {
	      System.out.println(ex);
	  }
  } 
  
  // query from all the data
  public void queryAllTheData(String songName) {
	    try {
	      String query = "SELECT * FROM songs where title like \'%"+songName+"%\' order by artist_hotttnesss desc limit 5;";
	      System.out.println("Loading from all the dataset.");
	      rs = st.executeQuery(query);
	      int counter = 0;
	      while (rs.next()) {
	    	  counter+=1;
	    	  String title = rs.getString("title");
	    	  String artist = rs.getString("artist_name");
	    	  System.out.println("Rank: " + counter + " || Artist: " + artist+ " || Song: "  + title ); 
	      }
	      double queryTime= getQueryTime(1, st);
	      System.out.println("QueryTime:"+queryTime);
	    } catch (Exception ex) {
	      System.out.println(ex);
	    }
	  }
  
  // query from partition of the data
  public void queryPartitionData(String songName) {
    try {
       boolean stop = false;
       int counter = 0;
       int numQuery = 0;
       System.out.println("Loading from partial dataset.");
       for(int i = 3; i > 0; i--) {
    	  numQuery += 1;
          String query = "SELECT * FROM songs PARTITION (p"+i+") where title like \'%"+songName+"%\' order by artist_hotttnesss desc";
          rs = st.executeQuery(query);
          
          while (rs.next()) {
        	counter+=1;
            String title = rs.getString("title");
            String artist = rs.getString("artist_name");
            System.out.println("Rank: " + counter + " || Artist: " + artist+ " || Song: "  + title );
            stop = true;
            if(counter == 5) {
            	break;
            }
          }
     
          if ((stop && numQuery > 1)||(stop && counter == 5)) {
        	  break;
          }
      }
       double queryTime = getQueryTime(numQuery, st);
       System.out.println("QueryTime:"+queryTime);
    } catch (Exception ex) {
      System.out.println(ex);
    }
  }
  	// get query time
	public double getQueryTime(int numQuery, Statement st) {
		double time = 0.0;
		try {
	        String query = "SELECT * FROM mysql.slow_log where rows_sent <> \"0\" order by start_time desc limit "+numQuery+";";
	        rs = st.executeQuery(query);
	        while (rs.next()) {
	        	String[] strs = rs.getString("query_time").split(":");
	        	time += Double.parseDouble(strs[2]);
	        }
		}catch (Exception ex) {
		      System.out.println(ex);
	    }
		return time;
	}
  
  }

