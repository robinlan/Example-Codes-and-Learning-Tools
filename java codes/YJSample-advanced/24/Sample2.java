import java.sql.*;

public class Sample2
{
   public static void main(String args[])
   {
      try{
         // 準備連線
         String url = "jdbc:derby:cardb;create=true";
         String usr = "";
         String pw = "";

         // 連接到資料庫
         Connection cn = DriverManager.getConnection(url, usr, pw);

         // 準備查詢
         Statement st = cn.createStatement();
         String qry = "SELECT * FROM 車子資料表 WHERE 編號 >=3";
         
         // 進行查詢
         ResultSet rs = st.executeQuery(qry);

         // 取得資料
         ResultSetMetaData rm = rs.getMetaData();
         int cnum = rm.getColumnCount();
         while(rs.next()){
            for(int i=1; i<=cnum; i++){
                System.out.print(rm.getColumnName(i) +  ":"+ rs.getObject(i) + "  ");
            }
            System.out.println("");
         }

         // 關閉連線
         rs.close();
         st.close();
         cn.close();
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }
}