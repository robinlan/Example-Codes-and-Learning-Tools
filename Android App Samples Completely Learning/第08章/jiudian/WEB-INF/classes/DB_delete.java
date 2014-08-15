package wyf.wyy;
import java.sql.*;
import javax.sql.*;
import java.util.*;
public class DB_delete{
	private static Connection con=null;
	private static Statement stat=null;
	private static ResultSet rs=null;
	public static boolean isDelete(String oid){
		boolean b = false;
		int count=0;
		try{
			oid = new String(oid.getBytes(),"iso8859-1");
		    con = DB.getCon();
		    stat = con.createStatement();
		    //rs = stat.executeQuery("select orid from oinfo where rgid='"+rid+"'");
		    //if(rs.next())
			//{orid=new String(rs.getString(1).getBytes("iso8859-1"),"big5");}
			System.out.println(oid);
			String sqla="delete from olist where oid="+oid;
			String sqlb="delete from oinfo where orid="+oid;
		    count+=stat.executeUpdate(sqla);
		    count+=stat.executeUpdate(sqlb);
		    if(count==2){b=true;}
		}
		catch(Exception e){e.printStackTrace();}
		finally{DB.closeCon();}
		return b;
	}
}