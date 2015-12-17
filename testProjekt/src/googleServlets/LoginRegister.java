package googleServlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.kappel.mysql.readData.MySQLConnection;

public class LoginRegister extends HttpServlet {
	

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// login
		resp.setContentType("text/json");
		
			
			 Connection conn = null;
			try {			
			 conn = MySQLConnection.getInstance();
			}
			catch (Exception e) {
				resp.getWriter().println("{\"error\": \"connection wrooong\"");
		        e.printStackTrace();
			}
			try {
			 
				
			    if(conn != null)
			    {
			 
			      Statement query;
			      
			        query = conn.createStatement();
			        String paramName = "username";
			        String paramValue = req.getParameter(paramName);
			     
			        String sql = "SELECT * FROM user WHERE username = '" + paramValue + "' LIMIT 1";

			        ResultSet result = query.executeQuery(sql);
			
			       
			        String output = "{";
			        int rows = 0;
			        while (result.next()) {
			        	
			        	rows++;
			        	output = output + "\"userid\":" + "\"" + result.getString("userid") + "\",";
			        	output = output + "\"username\":" + "\"" + result.getString("username") + "\",";
			        	Boolean ergebnis = true;

			        }
			        if (rows != 0) {
			        	 output = output + "\"resultmessage\":" + "\"Sie sind eingeloggt\",";
					        output = output + "\"result\":" + "\"success\"";
			        } else {
			        	 output = output + "\"resultmessage\":" + "\"Dieser Benutzername ist nicht vorhanden\",";
					        output = output + "\"result\":" + "\"failure\"";
			        }

			       
			        output = output + "}";
			       
			        resp.getWriter().println(output);
			        
			     
			    }
			    
			} catch (SQLException e) {
				resp.getWriter().println("{\"error\": \"database wrong / sql statement wrong " + e.getMessage() + "\"");
	        } catch (Exception e) {
	        	resp.getWriter().println("{\"error\": \"code wrong " + e.getMessage() + "\"");
	        }
			

		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// login
		resp.setContentType("text/json");
	
		 String output = "";
		 Connection conn = null;
		try {			
		 conn = MySQLConnection.getInstance();
		}
		catch (Exception e) {
			resp.getWriter().println("{\"error\": \"connection wrooong\"");
	        e.printStackTrace();
		}
		try {
		 
			
		    if(conn != null)
		    {
		    	  output = "{";
		    	  
		    	Statement query; 
		        String paramName = "username";
		        String paramValue = req.getParameter(paramName);
		        
		     
		        query = conn.createStatement();
		        String sql3 = "SELECT * FROM user WHERE username = '" + paramValue + "' LIMIT 1";

		        ResultSet result = query.executeQuery(sql3);
		        
		        int rows = 0;
		        
		        while (result.next()) {	      
		        	rows++;
		        }
		        
		        if (rows != 0) {
		        	System.out.println("Username bereits vorhanden");
		        	
		        	output = output + "\"result \":" + "\"failure\",";
	        		 output = output + "\"resultmessage\":" + "\"Dieser Name ist bereits vorhanden\"";
	        		
		        } else {
		        	
		        	// nioch nicht voprhanden
			        query = conn.createStatement();
			        
			        String sql = "INSERT INTO user(username) VALUES ('" + paramValue + "')";		        
			        
			        PreparedStatement createPlayer = conn.prepareStatement(sql);
			        
			        int count = createPlayer.executeUpdate();

			        if(count > 0) {
			        	
			        	
				        output = output + "\"resultmessage\":" + "\"Sie sind registiert\"";
				        		    	
	  
			        } else {
		        	    // fail
		        		output = output + "\"result \":" + "\"failure\"";
		        		output = output + "\"resultmessage\":" + "\"Sie konnten nicht registriert werden\"";
		   		       
		        	}

		        }
		        output = output + "}";
		        resp.getWriter().println(output);
			      
		    }
		    
		} catch (SQLException e) {
			resp.getWriter().println("{\"error\": \"database wrong / sql statement wrong " + e.getMessage() + "\"");
        } catch (Exception e) {
        	resp.getWriter().println("{\"error\": \"code wrong " + e.getMessage() + "\"");
        }
		

	
	}
}
