import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.awt.*;

class CMenuFrame extends JFrame implements ActionListener {
  JLabel lbl = new JLabel("顯示文字用"); 
  JMenuItem item11, item12, item13;
  JCheckBoxMenuItem sItem211, sItem212;
  JRadioButtonMenuItem[] sItem22 = new JRadioButtonMenuItem[3];
 
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
    
    JMenu menu2 = new JMenu("格式");
    mnuBar.add(menu2);
    JMenu sMenu21 = new JMenu("樣式");
    menu2.add(sMenu21);
    sItem211 = new JCheckBoxMenuItem("斜體");
    sItem212 = new JCheckBoxMenuItem("粗體");
    sMenu21.add(sItem211);
    sMenu21.add(sItem212);
    sItem211.addActionListener(this);
    sItem212.addActionListener(this);
    
    menu2.addSeparator(); 
    JMenu sMenu22 = new JMenu("對齊");
    menu2.add(sMenu22);
    ButtonGroup group = new ButtonGroup();
    sItem22[0] = new JRadioButtonMenuItem("靠左", true);
    sItem22[1] = new JRadioButtonMenuItem("置中");
    sItem22[2] = new JRadioButtonMenuItem("靠右");
    for(int i = 0; i < sItem22.length; i++) {
      sItem22[i].addActionListener(this);
      group.add(sItem22[i]);
      sMenu22.add(sItem22[i]);
    }  
    
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
    
    int style = Font.PLAIN;
    if (sItem211.isSelected()) style = Font.ITALIC;
    if (sItem212.isSelected()) style = Font.BOLD;
    if (sItem211.isSelected() & sItem212.isSelected()) 
                               style = Font.BOLD + Font.ITALIC;
    lbl.setFont(new Font("標楷體", style, 20)); 

    if (sItem22[0].isSelected()) lbl.setHorizontalAlignment(JLabel.LEFT); 
    if (sItem22[1].isSelected()) lbl.setHorizontalAlignment(JLabel.CENTER); 
    if (sItem22[2].isSelected()) lbl.setHorizontalAlignment(JLabel.RIGHT); 
  }  
}

public class J11_1_3 {
  public static void main(String[] args){
    CMenuFrame myFrame = new CMenuFrame();
  }
}