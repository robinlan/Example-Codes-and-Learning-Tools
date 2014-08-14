import java.sql.*;

public class SampleP1
{
   public static void main(String[] args)
   {
      try{
         // 準備連線
         String url = "jdbc:derby:fooddb;create=true";
         String usr = "";
         String pw = "";

         // 連接到資料庫
         Connection cn = DriverManager.getConnection(url, usr, pw);

         // 準備查詢
         DatabaseMetaData dm = cn.getMetaData();
         ResultSet tb = dm.getTables(null, null, "水果資料表", null);

         Statement st = cn.createStatement();

         String qry1 = "CREATE TABLE 水果資料表(編號 int, 名稱 varchar(50), 販賣店 varchar(50))";
         String[] qry2 = {"INSERT INTO 水果資料表 VALUES (1,'橘子','青山商店')",
                          "INSERT INTO 水果資料表 VALUES (2,'蘋果','東京市場')",
                          "INSERT INTO 水果資料表 VALUES (3,'香蕉','鈴木商店')",
                          "INSERT INTO 水果資料表 VALUES (4,'草莓','東京市場')",
                          "INSERT INTO 水果資料表 VALUES (5,'梨子','青山商店')",
                          "INSERT INTO 水果資料表 VALUES (6,'栗子','橫濱百貨')",
                          "INSERT INTO 水果資料表 VALUES (7,'桃子','橫濱百貨')",
                          "INSERT INTO 水果資料表 VALUES (8,'鳳梨','佐藤商店')",
                          "INSERT INTO 水果資料表 VALUES (9,'柿子','青山商店')",
                          "INSERT INTO 水果資料表 VALUES (10,'西瓜','東京市場')"};
         String qry3 = "SELECT * FROM 水果資料表";

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
                System.out.print(rm.getColumnName(i) + ":"+ rs.getObject(i) + "  ");
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