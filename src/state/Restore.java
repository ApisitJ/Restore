package state;

import java.io.BufferedWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
//import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import databaseConnect.Connector;

public class Restore {	
	private static final String SELECT_ID_SALE_ORDER ="select msisn from sale_order where order_name = ? and create_date is null ";
	
	public static void main(String[] args) {
		  
//		  Scanner myObj = new Scanner(System.in);
		  Connection connection = Connector.getConnection();
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
                Connector.updateData(msisn, sqlDate);
				}
				
				 writer.close();							 
				 				 
			}catch (Exception e) {
			}

		  }

}
 