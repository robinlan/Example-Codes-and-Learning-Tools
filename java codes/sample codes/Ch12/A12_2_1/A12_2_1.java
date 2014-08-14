import javax.swing.*;
import java.awt.event.*;

public class A12_2_1 extends JApplet implements ActionListener {
  JLabel lblCel = new JLabel("請輸入攝氏溫標：");
  JTextField txtCel = new JTextField();
  JButton btnChange = new JButton("攝氏轉華氏");
  JLabel lblFah = new JLabel();
  
  public void init() {
    setLayout(null);
    lblCel.setBounds(20,10,110,20);
    add(lblCel);                     // 加入標籤到Applet
    txtCel.setBounds(130,10,40,20);
    add(txtCel);                     // 加入文字方塊到Applet
    btnChange.setBounds(30,50,120,20);
    add(btnChange);                 // 加入命令按鈕到Applet
    btnChange.addActionListener(this);   // 加入命令按鈕Listerer
    lblFah.setBounds(20,90,140,20);
    add(lblFah);                      // 加入標籤到Applet
  }

  public void actionPerformed(ActionEvent e) {
    float f = Integer.parseInt(txtCel.getText());  // 取得攝氏溫標
    f = f * 9 / 5 + 32;                      // 轉成華氏溫標
    String txt = "華氏溫標為： " + f;                          
    lblFah.setText(txt);	                // 輸出到華氏文字欄位
  }
}