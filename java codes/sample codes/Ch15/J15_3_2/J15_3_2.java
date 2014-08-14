import java.awt.*;
import javax.swing.*;
import java.sql.*;           // 步驟1

class mysqlFrame {
  Connection connection;
  Statement statement;	  
  
  public  mysqlFrame() {
    try {
      Class.forName("com.mysql.jdbc.Driver");    // 步驟2
    } catch (Exception e) {
      errorMessage("MySQL驅動程式安裝失敗！");
    }
  	// 刪除資料庫
    try {
 	  connection = DriverManager.getConnection("jdbc:mysql://localhost/members1"
                  +"?user=root&password=mysql");     // 步驟3
      statement = connection.createStatement();      // 步驟4
      String deleteDB = "DROP DATABASE members1";    // 步驟6
      statement.execute(deleteDB);             // 步驟6
      JOptionPane.showMessageDialog(null, "資料庫刪除成功！");
      statement.close();                        // 步驟12
      System.exit(0);
    } catch (SQLException e) {
      if ( statement == null ) errorMessage("資料庫不存在！");
      else errorMessage("MySQL無法啟動！");
    } catch (Exception e) {
      errorMessage("發生其他錯誤！");
    }
  }

  public void errorMessage(String msg) {
    String message = msg;
    JOptionPane.showMessageDialog(null, message, "錯誤訊息",
                       JOptionPane.ERROR_MESSAGE);
    System.exit(0);
  } 
}

public class J15_3_2 {
  public static void main(String[] args) {
    mysqlFrame frame = new mysqlFrame();
  }
}
