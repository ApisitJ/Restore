package state;

import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


//import databaseConnect.Connector;

public class Restore {	
	
	static String jdbcURL = "jdbc:postgresql://localhost:5432/restore" ;
	static String username = "postgres";
	static String password	= "dEciFeR@01";
	static String driver = "com.mysql.jdbc.Driver";
	
	private static final String SELECT_ID_SALE_ORDER ="select msisn from sale_order where order_name = ? and create_date is null ";
	private static final String UPDATE_ORDER_DATE = "update sale_order set create_date = ? Where msisn = ?";
	
	public static void main(String[] args) {
	      System.out.println("Start Restore");
	      getRestore();

		  }
	
	private static Connection getConnection() {
		Connection connection = null;
		try {
	//		Class.forName(driver);		
			connection = DriverManager.getConnection(jdbcURL, username, password);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	private static void getRestore() {
//		  Scanner myObj = new Scanner(System.in);
//		  Connection connection = Connector.getConnection();
		  Connection connection = getConnection();
		  java.util.Date utilDate = new java.util.Date();
		  java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		  LocalDateTime myDateObj = LocalDateTime.now();
		  DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
		  String formattedDate = myDateObj.format(myFormatObj);
		  String nameFolder= "D:\\FilePathMSISN" + formattedDate + ".txt";
		  String msisn = "";
		  
		  Path file = Paths.get(nameFolder);
		  
		    try  {
		        BufferedWriter writer = Files.newBufferedWriter(file, 
		                  StandardCharsets.UTF_8); 
		    			    	
//				System.out.println("Enter Order Name:");
//				String orderName = myObj.nextLine();// Read user input
				String orderName = "RESTORE";
				
				PreparedStatement statement = connection.prepareStatement(SELECT_ID_SALE_ORDER);
				statement.setString(1, orderName.toUpperCase());
				
				ResultSet rs = statement.executeQuery();			
				while(rs.next()) {
				msisn = rs.getString("msisn");
				writer.write(msisn);
				writer.newLine();
//              Connector.updateData(msisn, sqlDate);
              	updateData(msisn, sqlDate);
				}
				
				 writer.close();	
				 System.out.println("Restore Success");
				 				 
			}catch (Exception e) {
				
			}
		
	}
	
	private static boolean updateData(String msisn, Date date) throws SQLException {
		boolean update = false ;
		try(Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER_DATE);) {
			statement.setDate(1, date);
			statement.setInt(2, Integer.parseInt(msisn));
//			statement.setString(2, msisn);
			update = statement.executeUpdate() > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return update;
	}

}
 