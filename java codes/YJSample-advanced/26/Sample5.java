import java.sql.*;
import java.io.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;
import org.w3c.dom.*;

public class Sample5
{
   public static void main(String[] args)
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
                          "INSERT INTO 車子資料表 VALUES (3, '卡車')",
                          "INSERT INTO 車子資料表 VALUES (4, '戰車')"};
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

         // 進行DOM的準備
         DocumentBuilderFactory dbf
            = DocumentBuilderFactory.newInstance();
         DocumentBuilder db
            = dbf.newDocumentBuilder();

         // 建立新文件
         Document doc = db.newDocument();

         // 建立根元素
         Element root = doc.createElement("cars");
         doc.appendChild(root);

         // 建立元素
         while(rs.next()){
            Element car = doc.createElement("car");
            root.appendChild(car);
            for(int i=1; i<=cnum; i++){
               Element elm = doc.createElement(rm.getColumnName(i).toString());
               Text txt = doc.createTextNode(rs.getObject(i).toString());
               elm.appendChild(txt);
               car.appendChild(elm);
            }
         }

         // 輸出文件
         TransformerFactory tff
            = TransformerFactory.newInstance();
         Transformer tf
            = tff.newTransformer();
         tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
         tf.transform(new DOMSource(doc), new StreamResult("result.xml"));
         System.out.println("輸出到result.xml了。");

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