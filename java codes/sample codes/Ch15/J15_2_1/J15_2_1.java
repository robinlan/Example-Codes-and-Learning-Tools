import java.awt.*;
import javax.swing.*;

class mysqlFrame extends JFrame {
  public mysqlFrame() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      JOptionPane.showMessageDialog(null, "MySQL驅動程式安裝成功！");
      System.exit(0);
    } catch (Exception e) {
      errorMessage("MySQL驅動程式安裝失敗！");
    }
  }
  
  public void errorMessage(String msg) {
    String message = msg;
    JOptionPane.showMessageDialog(null, message, "錯誤訊息", JOptionPane.ERROR_MESSAGE);
    System.exit(0);
  } 
}

public class J15_2_1 {
  public static void main(String[] args) {
    mysqlFrame frame = new mysqlFrame();
  }
}