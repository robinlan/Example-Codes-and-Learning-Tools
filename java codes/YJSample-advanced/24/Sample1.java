import java.sql.*;

public class Sample1
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
         DatabaseMetaData dm = cn.getMetaData();
         ResultSet tb = dm.getTables(null, null, "車子資料表", null);

         Statement st = cn.createStatement();

         String qry1 = "CREATE TABLE 車子資料表(編號 int, 名稱 varchar(50))";
         String[] qry2 = {"INSERT INTO 車子資料表 VALUES (2, '汽車')",
                          "INSERT INTO 車子資料表 VALUES (3, '戰車')",
                          "INSERT INTO 車子資料表 VALUES (4, '卡車')"};
         String qry3 = "SELECT * FROM 車子資料表";

         if(!tb.next()){
            st.executeUpdate(qry1);
            for(int i=0; i<qry2.length; i++){
               st.executeUpdate(qry2[i]);
            }
         }

         // 進行查詢
         ResultSet rs = st.executeQuery(qry3);

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