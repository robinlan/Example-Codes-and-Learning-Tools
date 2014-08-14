import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class CFrame extends JFrame implements ActionListener {
  JButton btnShow;
  JButton btnCls;
    
  CFrame() {
    btnShow = new JButton("呈現");
    btnShow.addActionListener(this);
    btnShow.setBounds(25,80,70,20);
    add(btnShow);
        
    btnCls = new JButton("清除");
    btnCls.addActionListener(this);
    btnCls.setBounds(120,80,70,20);
    add(btnCls);
                
    setTitle("JFrame畫布");
    setLayout(null);
    setBounds(100, 100, 220, 150);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           
  }
    
  public void actionPerformed(ActionEvent e)  {
    Graphics g = getGraphics();
    if (e.getSource() == btnShow) {
      g.drawString("在順境中要節制", 60, 60);
      g.drawString("在逆境中要謹慎", 60, 90);
    }  
    if (e.getSource()== btnCls) update(g);  // 清除畫布
  }
}

class J9_2_2 {
  public static void main(String[] args)  {
    CFrame frame = new CFrame();
  }
}
