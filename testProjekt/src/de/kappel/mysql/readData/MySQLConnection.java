package de.kappel.mysql.readData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLConnection {
	private static Connection conn = null;

	// Hostname
	private static String dbHost = "173.194.229.123";
	
	// Port -- Standard: 3306
	private static String dbPort = "3306";
	
	// Datenbankname
	private static String database = "user_informations";
	
	// Datenbankuser
	private static String dbUser = "root3";
	
	// Datenbankpasswort
	private static String dbPassword = "passwort123";
	
	private MySQLConnection() {
	  try {
	
	    // Datenbanktreiber für ODBC Schnittstellen laden.
	    // Für verschiedene ODBC-Datenbanken muss dieser Treiber
	    // nur einmal geladen werden.
	    Class.forName("com.mysql.jdbc.Driver");
	
	    // Verbindung zur ODBC-Datenbank 'sakila' herstellen.
	    // Es wird die JDBC-ODBC-Brücke verwendet.
	    conn = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":"
	        + dbPort + "/" + database + "?" + "user=" + dbUser + "&"
	        + "password=" + dbPassword);
	  } catch (ClassNotFoundException e) {
	    System.out.println("Treiber nicht gefunden");
	  } catch (SQLException e) {
	    System.out.println("Connect nicht moeglich");
	  }
	}
	
	public static Connection getInstance()
	{
	  if(conn == null)
	    new MySQLConnection();
	  return conn;
	}
	
	/**
	 * Schreibt die Namensliste in die Konsole
	 */
	public static void printNameList()
	{
	  conn = getInstance();
	
	  if(conn != null)
	  {
	    // Anfrage-Statement erzeugen.
	    Statement query;
	    try {
	      query = conn.createStatement();
	
	      // Ergebnistabelle erzeugen und abholen.
	      String sql = "SELECT first_name, last_name FROM actor "
	          + "ORDER BY last_name";
	      ResultSet result = query.executeQuery(sql);
	
	      // Ergebnissätze durchfahren.
	      while (result.next()) {
	        String first_name = result.getString("first_name"); // Alternativ: result.getString(1);
	        String last_name = result.getString("last_name"); // Alternativ: result.getString(2);
	        String name = last_name + ", " + first_name;
	        System.out.println(name);
	      }
	    } catch (SQLException e) {
	      e.printStackTrace();
	    }
	  }
	}
}
