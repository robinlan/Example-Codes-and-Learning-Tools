import java.awt.event.*;
import javax.swing.*;

public class A12_4_1a extends JApplet implements ActionListener {
  A12_4_1a applet1;
  A12_4_1b applet2;
  ImageIcon icon = new ImageIcon();        // 圖片
  JButton btnPic = new JButton();          // 圖片按鈕
  
  public void init() {
    setLayout(null);
       
    JLabel lblTitle = new JLabel("按圖片傳送");
    lblTitle.setBounds(10, 5, 80, 20);
    add(lblTitle);
    
    icon = new ImageIcon(getImage(getCodeBase(),"fig_0.jpg"));
    btnPic.setIcon(icon);                 // btnPic按鈕放入圖片
    btnPic.setBounds(10, 25, 150, 160);
    add(btnPic);
    btnPic.addActionListener(this);
    
    String user = getParameter("user");      // 從<param>取得自身user名稱  
    JLabel lblUser = new JLabel("我是" + user);
    lblUser.setBounds(10, 190, 80, 20);
    add(lblUser);
  }
  
  public void actionPerformed(ActionEvent e) { // 監聽事件處理
    if (e.getSource() == btnPic) {
      applet2 = (A12_4_1b)getAppletContext().getApplet("user2");
      applet2.lblPic.setIcon(icon);
    }  
  }
}