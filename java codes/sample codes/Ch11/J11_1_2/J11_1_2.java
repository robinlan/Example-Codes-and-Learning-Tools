import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.awt.*;

class CMenuFrame extends JFrame implements ActionListener {
  JLabel lbl = new JLabel("顯示文字用"); 
  JMenuItem item11, item12, item13;
  
  CMenuFrame() {
    JMenuBar mnuBar = new JMenuBar();
    JMenu menu1 = new JMenu("內容(C)");
    menu1.setMnemonic('C');
    mnuBar.add(menu1);
    item11 = new JMenuItem("文字一");
    item12 = new JMenuItem("文字二");
    item13 = new JMenuItem("結束(X)");
    item13.setMnemonic('X');
    menu1.add(item11);
    menu1.add(item12);
    menu1.addSeparator();
    menu1.add(item13);
    item11.addActionListener(this);
    item12.addActionListener(this);
    item13.addActionListener(this);
      
    lbl.setBorder(BorderFactory.createLineBorder(Color.black)); 
    lbl.setFont(new Font("標楷體", Font.PLAIN, 20));
    
    add(mnuBar, BorderLayout.NORTH);
    add(lbl, BorderLayout.SOUTH);
    setTitle("功能表");
    setBounds(50, 50, 300, 200);   
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
  
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == item11) {
      lbl.setText("成功是給堅持到底的人"); 
      lbl.setForeground(Color.blue);
    }
    if (e.getSource() == item12) {
      lbl.setText("機會是給時刻積極的人"); 
      lbl.setForeground(Color.red);
    }
    if (e.getSource() == item13) System.exit(0);
  }  
}

public class J11_1_2 {
  public static void main(String[] args){
    CMenuFrame myFrame = new CMenuFrame();
  }
}