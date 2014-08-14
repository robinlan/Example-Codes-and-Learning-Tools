import java.awt.*;
import javax.swing.*;
import java.sql.*;        // 步驟1

class mysqlFrame {
  Connection connection;
  Statement statement;	  
  
  public mysqlFrame() {
    try {
      Class.forName("com.mysql.jdbc.Driver");    // 步驟2
    } catch (Exception e) {
      errorMessage("MySQL驅動程式安裝失敗！");
    }  
    
  	// 建立資料庫
    try {
 	  connection = DriverManager.getConnection("jdbc:mysql://localhost/"
                  +"?user=root&password=mysql");         // 步驟3
      statement = connection.createStatement();          // 步驟4 
      String createDB = "CREATE DATABASE members";       // 步驟5
      statement.executeUpdate(createDB);                 // 步驟5
    } catch (SQLException e) {
      if ( statement != null ) errorMessage("資料庫已存在！");
      else errorMessage("MySQL無法啟動！");
    } catch (Exception e) {
      errorMessage("發生其他錯誤！");
    }
    // 建立資料表
    try {
 	  connection = DriverManager.getConnection("jdbc:mysql://localhost/members"
                   +"?user=root&password=mysql");          // 步驟3
      statement = connection.createStatement();          // 步驟4 
      String createTB = "CREATE TABLE personal_data(";
      createTB += "acc_id     VARCHAR(10) PRIMARY KEY, ";  //帳號
      createTB += "password   VARCHAR(10), ";              //密碼
      createTB += "date_join  DATE, ";                     //註冊日期
      createTB += "name       VARCHAR(10), ";              //姓名
      createTB += "sex        TINYINT, ";                  //性別
      createTB += "age        TINYINT, ";                  //年齡
      createTB += "habbit1    BOOL, ";                     //興趣1
      createTB += "habbit2    BOOL, ";                     //興趣2
      createTB += "habbit3    BOOL, ";                     //興趣3
      createTB += "habbit4    BOOL, ";                     //興趣4
      createTB += "habbit5    BOOL, ";                     //興趣5
      createTB += "education  TINYINT, ";                  //學歷
      createTB += "home       TINYINT)";                   //居住地區
      statement.executeUpdate(createTB);
      JOptionPane.showMessageDialog(null, "資料庫和資料表建立成功！");
      statement.close();                                // 步驟12
      System.exit(0);
    } catch (SQLException e) {
      if (statement != null ) errorMessage("資料表已存在！");
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

public class J15_4_1 {
  public static void main(String[] args) {
    mysqlFrame frame = new mysqlFrame();
  }
}

  