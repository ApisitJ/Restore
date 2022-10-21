package databaseConnect;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Connector {
	
	static String jdbcURL = "jdbc:postgresql://localhost:5432/restore" ;
	static String username = "postgres";
	static String password	= "dEciFeR@01";

	static String driver = "com.mysql.jdbc.Driver";
	
	
	private static final String UPDATE_ORDER_DATE = "update sale_order set create_date = ? Where msisn = ?";

	public static Connection getConnection() {
		Connection connection = null;
		try {

	//		Class.forName(driver);
			connection = DriverManager.getConnection(jdbcURL, username, password);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	public static boolean updateData(String msisn, Date date) throws SQLException {
		boolean update = false ;
		try(Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement(UPDATE_ORDER_DATE);) {
			statement.setDate(1, date);
			statement.setInt(2, Integer.parseInt(msisn));
			update = statement.executeUpdate() > 0;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return update;
	}
}