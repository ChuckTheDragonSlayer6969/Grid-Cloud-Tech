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

import org.apache.commons.lang3.StringEscapeUtils;

import de.kappel.mysql.readData.MySQLConnection;

public class Favoriten extends HttpServlet {
	
	private User currentUser;
	
	public Favoriten() {
		this.currentUser = new User(2);
	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/json");
		
		if (this.currentUser.getUserId() == null) {
			
			resp.getWriter().println("{\"error\": \"no access");
		} else {
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
			      // Anfrage-Statement erzeugen.
			      Statement query;
			      
			        query = conn.createStatement();
			        String usernameValue = req.getParameter("username");
			        // Ergebnistabelle erzeugen und abholen.
			        String sql = "SELECT * FROM favoriten WHERE username = '" + usernameValue + "'";
			        System.out.println(sql);
			        ResultSet result = query.executeQuery(sql);
			 
			        // Ergebnissätze durchfahren.
			        output = output + "[";
			     
			        List<String> stringArray = new ArrayList<String>();
			        while (result.next()) {
			        	String singleRow = "";
			        	singleRow = singleRow + "{";
			        	singleRow = singleRow + "\"url\":" + "\"" + result.getString("url") + "\",";
			        	singleRow = singleRow + "\"plattform\":" + "\"" + result.getString("plattform") + "\",";
			        	singleRow = singleRow + "\"beschreibung\":" + "\"" + result.getString("beschreibung") + "\",";
			        	singleRow = singleRow + "\"favoritenid\":" + "\"" + result.getInt("favoritenid") + "\"";
			        	singleRow = singleRow + "}";	
			        	
			        	stringArray.add(singleRow);
			        }
			       
			        int i = 0;
			        for( String k: stringArray )
			        {
			        	output = output + k;
			        	if (i != (stringArray.size() - 1)) {
			        		output = output + ",";
			        	}
			        	i++;
			        }
			        System.out.println(stringArray.size());
			        output = output + "]";
			        resp.getWriter().println("{\"results\": " + output + "}");
			        
			      
			    }
			    
			} catch (SQLException e) {
				resp.getWriter().println("{\"error\": \"database wrong / sql statement wrong " + e.getMessage() + "\"");
	        } catch (Exception e) {
	        	resp.getWriter().println("{\"error\": \"code wrong " + e.getMessage() + "\"");
	        }
			
		}
		
		// retrieve data
		System.out.println(this.currentUser.getUserId());
		
		
		// convert data to JSON
		
	}
	/*
	 * 
	 *  //String sql = "SELECT * FROM favoriten WHERE UserId = '" + this.currentUser.getUserId() + "'";
			        System.out.println(sql);
			        ResultSet result = query.executeQuery(sql);
			 
			        // Ergebnissätze durchfahren.
			        output = output + "[";
			        
			        while (result.next()) {
			        	output = output + "{";
			        	output = output + "\"username:\"" + "\"" + result.getString("username") + "\",";
			        	output = output + "\"userid:\"" + "\"" + result.getInt("userid") + "\"";
			        	//output = output + "\"favoritenid:\"" + "\"" + result.getInt("favoritenid") + "\"";
			        	output = output + "},";
			          
			        }
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// checken, ob bereits in Favoriten vorhanden ist
		
		
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
		 
		      Statement query;
	
		        query = conn.createStatement();

		        String usernameValue = req.getParameter("username");
		        String bildplattformValue = req.getParameter("bildpattform");
		        String bildbeschreibungValue = req.getParameter("bildbeschreibung");
		        String bildurlValue = req.getParameter("bildurl");
		     
		        String sql = "SELECT * FROM favoriten WHERE url = \"" + bildurlValue + "\" AND username = \"" + usernameValue + "\" LIMIT 1";
		        ResultSet result = query.executeQuery(sql);
		        
		        int rows = 0;
		        output = "{";
		        while (result.next()) {	      
		        	rows++;
		        }
		        
		        if (rows != 0) {
		        
		        	// bereits vorhanden
		        	
		        	 output = output + "\"result \":" + "\"failure\"";
	        		 output = output + "\"resultmessage\":" + "\"Favorit konnte nicht hinzugefügt werden - bereits vorhanden\"";
	        		 
		        } else {
		        	 
		        	String sql2 = "INSERT INTO favoriten(username, beschreibung, plattform, url) VALUES (\""
		        	+  StringEscapeUtils.escapeJava(usernameValue) + "\",\"" 
		        	+ StringEscapeUtils.escapeJava(bildbeschreibungValue) + "\", \"" 
		        	+ StringEscapeUtils.escapeJava(bildplattformValue) + "\", \"" 
		        	+ StringEscapeUtils.escapeJava(bildurlValue) + "\")";
		    
			        
			        PreparedStatement createPlayer2 = conn.prepareStatement(sql2);
			        
			        int count = createPlayer2.executeUpdate();

			        if(count > 0) {
			        	
			        	output = output + "\"result\":" + "\"success\",";
				        output = output + "\"resultmessage\":" + "\"Favorit wurde hinzugefügt\",";
				        output = output + "\"url\":" + "\"" + bildurlValue + "\",";
				        output = output + "\"plattform\":" + "\"" + bildplattformValue + "\",";
				        output = output + "\"beschreibung\":" + "\"" + bildbeschreibungValue + "\"";
				         		    	
			        } else {
		        	    // fail
			        
		        		output = output + "\"result \":" + "\"failure\"";
		        		 output = output + "\"resultmessage\":" + "\"Favorit konnte nicht hinzugefügt werden\"";
		   		       
		        	}
			       
			        output = output + "}";
			        resp.getWriter().println(output);
	   		       
	        	}
	
		     
		    }
		    
		} catch (SQLException e) {
			resp.getWriter().println("{\"error\": \"database wrong / sql statement wrong " + e.getMessage() + "\"");
       } catch (Exception e) {
       	resp.getWriter().println("{\"error\": \"code wrong " + e.getMessage() + "\"");
       }
		

	
		
	}
	
}
