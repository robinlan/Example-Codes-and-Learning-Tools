import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class CPanel extends JFrame implements ActionListener {
  JPanel pane1 = new JPanel(), pane2 = new JPanel();
  JButton btn1, btn2;
  JTextField txt1, txt2;
  boolean tf1 = true, tf2 = true;
             
  CPanel() {
    // 容器A
    pane1.setBounds(20, 20, 140, 120);
    pane1.setBackground(Color.pink);
    pane1.setLayout(null);
    add(pane1);
    txt1 = new JTextField("我是容器A");
    txt1.setBounds(20, 20, 100, 20);
    pane1.add(txt1);
    btn1 = new JButton("容器B 消失");
    btn1.setBounds(20, 80, 100, 20);
    btn1.addActionListener(this);
    pane1.add(btn1);
    // 容器B
    pane2.setBounds(160, 20, 140, 120);
    pane2.setLayout(null);
    add(pane2);
    txt2 = new JTextField("我是容器B");
    txt2.setBounds(20, 20, 100, 20);
    pane2.add(txt2);
    btn2 = new JButton("容器A 消失");
    btn2.setBounds(20, 80, 100, 20);
    btn2.addActionListener(this);
    pane2.add(btn2);
    // 視窗      
    setTitle("使用JPanel容器");
    setLayout(null);
    setBounds(50, 50, 300, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
    
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == btn1) {
      if(tf1) {
        pane2.setVisible(false);
        tf1 = false;
        btn1.setText("容器B 出現");
      } else {
        pane2.setVisible(true);
        tf1 = true;
        btn1.setText("容器B 消失");  
      }
    }
    if(e.getSource() == btn2) {
      if(tf2) {
        pane1.setVisible(false);
        tf2 = false;
        btn2.setText("容器A 出現");
      } else {
        pane1.setVisible(true);
        tf2 = true;
        btn2.setText("容器A 消失");  
      }
    }  
  }    
}

public class J9_8_1 {
  public static void main(String[] args) {
    CPanel frame = new CPanel();
  }
}