import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

class cToolBar extends JFrame implements ActionListener {
  JPanel pane = new JPanel(); 
  JButton btn1 = new JButton();
  JButton btn2 = new JButton();
  JTextField txtFile = new JTextField("訊息列");
  
  cToolBar() {
  	JToolBar toolBar = new JToolBar("工具列", JToolBar.HORIZONTAL);
    ImageIcon icon1 = new ImageIcon("open_file.png");
    ImageIcon icon2 = new ImageIcon("select_color.png");
    btn1 = new JButton(icon1);
    btn2 = new JButton(icon2);
    btn1.addActionListener(this);
    btn2.addActionListener(this); 
    btn1.setToolTipText("開啟檔案面版"); 
    btn2.setToolTipText("開啟顏色面版");     
    toolBar.add(btn1);
    toolBar.add(btn2);
    pane.setLayout(new BorderLayout());
    pane.add(toolBar, BorderLayout.NORTH);
    txtFile.setBackground(Color.yellow);
    pane.add(txtFile ,BorderLayout.SOUTH);
       
    add(pane);
    setTitle("工具列");
    setBounds(50, 50, 300, 150);   
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  } 

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == btn1) {
      JFileChooser chooser = new JFileChooser(); 
      int press = chooser.showOpenDialog(null);      //開啟檔案
      if (press == JFileChooser.APPROVE_OPTION) 
        txtFile.setText("檔案為  " + chooser.getSelectedFile().getPath());
      else
        txtFile.setText("取消開啟檔案");
    }
    
    if (e.getSource() == btn2) {
      JColorChooser chooser = new JColorChooser();
      Color color = chooser.showDialog(null, "請選擇底色", Color.gray);
      pane.setBackground(color); 
      txtFile.setText("設定背景色");
    }  
  }
}
 
public class J11_3_1 {
  public static void main(String[] args) {
    cToolBar myFrame = new cToolBar();
  }
}