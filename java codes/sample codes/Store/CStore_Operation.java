import java.sql.*;
import java.util.*;

public class CStore_Operation {
  private Connection conn ;
  private Statement stmt;
  private ResultSet rs;
  private String Store_DB; //Database Name
  public CStore_Operation(String Store_DB){
    this.Store_DB = Store_DB;
	try { 
      Class.forName("com.mysql.jdbc.Driver").newInstance(); 
 	} catch (Exception ex) { 
 	  System.out.println("1. connection err"+ex);
 	}
 	try {
 	  conn = DriverManager.getConnection("jdbc:mysql://localhost/"
                    +Store_DB+"?user=root&password=mysql&useUnicode=true&characterEncoding=Big5");                        
 	} catch (SQLException ex) {
      System.out.println("2. SQLException: " + ex); 
 	}  	  
  }

  public void InsertDB(String code,float price,int num){
    try {	
      stmt = conn.createStatement(); 
	  stmt.execute("INSERT storetable(code,price,num)VALUES('"+code+"' ,"+price+","+num+")");
	} catch(SQLException sqlex){
	  System.out.println("3. SQLException : "+sqlex);
    }
    finally { 	    
	  if (rs != null) { 
	    try {
	      rs.close(); 
	    } catch (SQLException sqlEx) { 
          System.out.println("4. SQLException : "+sqlEx);
        } 
	    rs = null; 
	  }
      if (stmt != null) { 
	    try { 
	      stmt.close(); 
	    } catch (SQLException sqlEx) {  
          System.out.print("5. SQLException : "+sqlEx);
        } 
	    stmt = null; 
	  } 
    }
  }

  public void DeleteDB(String code) {
	try {	
      stmt = conn.createStatement(); 
	  stmt.executeUpdate("DELETE FROM storetable WHERE code ='"+ code +"'");
	} catch(SQLException sqlex) {
	  System.out.println("6. SQLException : "+sqlex);
	}
    finally {     
	  if (rs != null) { 
	    try {
	      rs.close(); 
	    } catch (SQLException sqlEx) { 
          System.out.print("7. SQLException :"+sqlEx);
        } 
	    rs = null; 
	  }	    
	  if (stmt != null) { 
	    try { 
	      stmt.close(); 
	    } catch (SQLException sqlEx) {  
          System.out.print("8. SQLException : "+sqlEx); 
        } 
	    stmt = null; 
	  }
    }
  }

  public void Display(){
    float money,total=0;
    try {	
	  stmt = conn.createStatement(); 
	  rs = stmt.executeQuery("SELECT * FROM storetable ");
      while(rs.next()) {
        System.out.print(rs.getString("code")+"\t"+rs.getFloat("price")+"\t"+rs.getInt("num"));
        money = rs.getFloat("price") * rs.getInt("num");
        System.out.println("\t"+money);
        total += money;
      }
      System.out.println("Á`ª÷ÃB\t\t\t" + total);
	} catch(SQLException sqlex) {
	  System.out.println("9. SQLException : "+sqlex);
	}
    finally {     
	  if (rs != null) { 
	    try {
	      rs.close(); 
	    } catch (SQLException sqlEx) { 
          System.out.print("10. SQLException : "+sqlEx);
        } 
	    rs = null; 
	  }	    
	  if (stmt != null) { 
	    try { 
	      stmt.close(); 
	    } catch (SQLException sqlEx) {  
          System.out.print("11. SQLException : "+sqlEx); 
        } 
	    stmt = null; 
	  }
    }
  }             
}