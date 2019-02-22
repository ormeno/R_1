package rojbot;

import java.sql.*;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.event.Level;

import rojbot.Pregunta;

/**
 * Basic connection to PostgreSQL database. 
 * Conexión básica a la base de datos PostgreSQL.
 *
 * @author Xules You can follow me on my website http://www.codigoxules.org/en
 * Puedes seguirme en mi web http://www.codigoxules.org).
 */
public class database {
 
	private final String url = "jdbc:postgresql://localhost:5432/telegramBotDB";
	private final String user = "postgres";
	private final String password = "1234";	       
    
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
 
        return conn;
    }
    
    public int usuarioExiste(long id) {
    	int res = 0;    	
        Connection con = connect();
    	try {    		
        	PreparedStatement pst = con.prepareStatement("SELECT count(id) FROM usuarios where id = ?");        	
          	pst.setLong(1, id);  
          	ResultSet rs = pst.executeQuery();                	
            while (rs.next()) {        	        		
            	res = rs.getInt(1);
            }      
            rs.close();
            pst.close();
            } catch (SQLException ex) {
        	 System.out.println("Error SQL: " + ex);	                 
            }
           return res;
    }               
    
    public void altaUsuario(long user_id, String user_username, String user_first_name, String user_last_name) {
    	Connection con = connect();
    	try {    		
    		String query = " insert into usuarios (id, nombre, apellido1, apellido2, fecha_alta)"
    			           + " values (?, ?, ?, ?, ?)";
    		Calendar calendar = Calendar.getInstance();
    	    java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
    	      
        	PreparedStatement pst = con.prepareStatement(query); 
        			
          	pst.setLong(1, user_id);
          	pst.setString(2, user_username);
          	pst.setString(3, user_first_name);
          	pst.setString(4, user_last_name);
          	pst.setDate(5, startDate);
          	pst.execute();                	            
            pst.close();
            System.out.println("Alta de usuario: " + user_username + " " + user_first_name);
            } catch (SQLException ex) {
        	 System.out.println("Error SQL: " + ex);	                 
            }           
    }
    
    public Pregunta obtenPregunta(Connection con, int id) {
    	
      Pregunta pregunta = null;
      
      try {
    	PreparedStatement pst = con.prepareStatement("SELECT id, pregunta, respuesta FROM preguntas where id = ?");
      	pst.setInt(1, id);
      	ResultSet rs = pst.executeQuery();                	
        while (rs.next()) {        	        		
        	pregunta = new Pregunta(rs.getInt(1), rs.getString(2), rs.getString(3));
        }      
        rs.close();
        pst.close();
        } catch (SQLException ex) {
    	 System.out.println("Error SQL: " + ex);	                 
        }
       return pregunta;
      }               
   }
