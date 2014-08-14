import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class A12_5_1 extends JApplet implements ActionListener {
  A12_5_1 applet, applet1, applet2;
  int now_pic = 0;                         // 目前圖片編號
  ImageIcon[] icon = new ImageIcon[6];     // 6張圖片的陣列
  JButton btnPic = new JButton();          // 圖片按鈕
  JButton btnSend = new JButton("傳送");   // 傳送按鈕
  TextField txtSend = new TextField();     // 輸入要傳送的地方
  JLabel lblPic = new JLabel();            // 接收圖片的標籤件
  
  public void init() {
    setLayout(null);
       
    JLabel lblTitle1 = new JLabel("按圖片選取");
    lblTitle1.setBounds(10, 5, 80, 20);
    add(lblTitle1);
    
    for (int i = 0; i < icon.length ; i++)
      icon[i] = new ImageIcon(getImage(getCodeBase(),"fig/fig_" + i +".jpg"));
    btnPic.setIcon(icon[now_pic]);            // btnPic圖片按鈕放入圖片
    btnPic.setBounds(10, 25, 150, 160);
    add(btnPic);
    btnPic.addActionListener(this);
    
    String user = getParameter("user");      // 從<param>取得自身user名稱  
    JLabel lblUser = new JLabel("我是" + user);
    lblUser.setBounds(10, 190, 80, 20);
    add(lblUser);
    
    JLabel lblSend = new JLabel("傳送到：");
    lblSend.setBounds(10, 215, 55, 20);
    add(lblSend);
    txtSend.setBounds(70, 215, 55, 20);
    add(txtSend);
    btnSend.setBounds(130, 215, 60, 20);
    add(btnSend);
    btnSend.addActionListener(this);

    JLabel lblTitle2 = new JLabel("接收圖片");
    lblTitle2.setBounds(170, 5, 80, 20);
    add(lblTitle2);
        
    lblPic.setBounds(170, 25, 150, 160);
    lblPic.setBorder(BorderFactory.createLineBorder(Color.red));
    add(lblPic);
  }
  
  public void actionPerformed(ActionEvent e) {  // 監聽事件處理
    if (e.getSource() == btnPic) {
      now_pic ++;                               // 圖片編號加1
      if (now_pic == icon.length) now_pic = 0;  // 編號超過從0開始
        btnPic.setIcon(icon[now_pic]);          // 顯示圖片按鈕上圖片
    }
     
    if (e.getSource() == btnSend) {
      applet1 = (A12_5_1)getAppletContext().getApplet("user1");
      applet2 = (A12_5_1)getAppletContext().getApplet("user2");
      String rece_user = txtSend.getText();
      applet = (A12_5_1)getAppletContext().getApplet(rece_user);
      if(applet == applet1) 
        applet1.lblPic.setIcon(icon[now_pic]);
      if(applet == applet2) 
        applet2.lblPic.setIcon(icon[now_pic]);
    }
  }
}