import java.awt.*;
import javax.swing.*;
import java.sql.*;                // 步驟1

class mysqlFrame {
  Connection connection;
  Statement statement;	  
 
  public mysqlFrame() {
    try {
      Class.forName("com.mysql.jdbc.Driver");         // 步驟2
    } catch (Exception e) {
      errorMessage("MySQL驅動程式安裝失敗！");
    }
  	// 建立資料庫
    try {
 	  connection = DriverManager.getConnection("jdbc:mysql://localhost/"
                   +"?user=root&password=mysql");     // 步驟3
      statement = connection.createStatement();       // 步驟4  
      String createDB = "CREATE DATABASE members1";   // 步驟5
      statement.executeUpdate(createDB);              // 步驟5
      JOptionPane.showMessageDialog(null, "資料庫建立成功！");
      statement.close();                              // 步驟12
      System.exit(0);
    } catch (SQLException e) {
      if ( statement != null ) errorMessage("資料庫已存在！");
      else errorMessage("MySQL無法啟動！");
    } catch (Exception e) {
      errorMessage("發生其他錯誤！");
    }
  }

  public void errorMessage(String msg) {
    String message = msg;
    JOptionPane.showMessageDialog(null, message, "錯誤訊息", JOptionPane.ERROR_MESSAGE);
    System.exit(0);
  } 
}

public class J15_3_1 {
  public static void main(String[] args) {
    mysqlFrame frame = new mysqlFrame();
  }
}