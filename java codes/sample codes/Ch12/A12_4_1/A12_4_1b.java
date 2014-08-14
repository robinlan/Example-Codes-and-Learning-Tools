import java.awt.*;
import javax.swing.*;

public class A12_4_1b extends JApplet {
  A12_4_1a applet1;
  A12_4_1b applet2;
  JLabel lblPic = new JLabel();   // 接收圖片的標籤

  public void init() {
    setLayout(null);
  
    JLabel lblTitle = new JLabel("接收圖片");
    lblTitle.setBounds(10, 5, 80, 20);
    add(lblTitle);
        
    lblPic.setBounds(10, 25, 150, 160);
    lblPic.setBorder(BorderFactory.createLineBorder(Color.red));
    add(lblPic);
    
    String user = getParameter("user");
    JLabel lblUser = new JLabel("我是" + user);
    lblUser.setBounds(10, 190, 80, 20);
    add(lblUser);
  }
}