import java.util.*;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class SampleP3 extends JFrame
{
   private JTable tb;
   private JScrollPane sp;

   public static void main(String[] args)
   {
      SampleP3 sm = new SampleP3();
   }
   public SampleP3()
   {
      // 設定標題
      super("範例");

      // 建立元件
      tb = new JTable();
      sp = new JScrollPane(tb);

      // 新增到容器中
      add(sp);

      // 登錄傾聽者
      addWindowListener(new SampleWindowListener());

      // 設定框架
      setSize(300, 300);
      setVisible(true);

      try{
         // 準備連線
         String url = "jdbc:derby:fooddb;create=true";
         String usr = "";
         String pw = "";

         // 連接到資料庫
         Connection cn = DriverManager.getConnection(url, usr, pw);

         // 準備查詢
         Statement st = cn.createStatement();
         String qry = "SELECT * FROM 水果資料表";

         // 進行查詢
         ResultSet rs = st.executeQuery(qry);
         tb.setModel(new MyTableModel(rs));

         // 關閉連線
         rs.close();
         st.close();
         cn.close();
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }

   // 傾聽者類別
   class SampleWindowListener extends WindowAdapter
   {
      public void windowClosing(WindowEvent e)
      {
         System.exit(0);
      }
   }

   // 模型類別
   class MyTableModel extends AbstractTableModel
   {
      private ArrayList<String> colname;
      private ArrayList<ArrayList> data;

      public MyTableModel(ResultSet rs)
      {
         try{

            // 取得欄位數量
            ResultSetMetaData rm = rs.getMetaData();
            int cnum = rm.getColumnCount();
            colname = new ArrayList<String>(cnum);

            // 取得欄位名稱
            for(int i=1; i<=cnum; i++){
               colname.add(rm.getColumnName(i));
            }

            // 取得列
            data = new ArrayList<ArrayList>(); 
            while(rs.next()){
               ArrayList<String> rowdata = new ArrayList<String>();
               for(int i=1; i<=cnum; i++){
                  rowdata.add(rs.getObject(i).toString());
               }
               data.add(rowdata);
            }
         }
         catch(Exception e){
            e.printStackTrace();
         }
      }
      public int getRowCount() 
      {
         return data.size();
      }
      public int getColumnCount() 
      {
         return colname.size();
      }
      public Object getValueAt(int row, int column) 
      {
         ArrayList rowdata = (ArrayList) (data.get(row));
         return rowdata.get(column);
      }
      public String getColumnName(int column) 
      {
         return (String) colname.get(column);
      }
   }
}