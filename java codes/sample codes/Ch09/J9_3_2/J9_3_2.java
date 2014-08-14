import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class CFrame extends JFrame implements ActionListener {
  JButton btnShow, btnCls;
  JPanel pane = new JPanel();    
    
  CFrame() {
    pane.setBounds(0, 0, 140, 71);
    // 為pane畫布設定紅色框線
    pane.setBorder(BorderFactory.createLineBorder(Color.red)); 
    add(pane);    
        
    btnShow = new JButton("呈現");
    btnShow.addActionListener(this);
    btnShow.setBounds(25, 80, 70, 20);
    add(btnShow);
        
    btnCls = new JButton("清除");
    btnCls.addActionListener(this);
    btnCls.setBounds(120, 80, 70, 20);
    add(btnCls);
           
    setTitle("JPanel畫布");
    setLayout(null); 
    setBounds(100, 100, 220, 150);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           
  }
    
  public void actionPerformed(ActionEvent e)  {
    Graphics g = pane.getGraphics();
    if (e.getSource() == btnShow) g.drawString("屋寬，不如心寬!", 50, 70);
    if (e.getSource()== btnCls) pane.update(g);  // 清除畫布
  }
}

public class J9_3_2 {
  public static void main(String[] args)  {
    CFrame frame = new CFrame();
  }
}
