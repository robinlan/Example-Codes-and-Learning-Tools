import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class CGFrame extends JFrame implements ActionListener {
  JButton btnShow, btnCls;
  JPanel pane = new JPanel();    
    
  CGFrame() {
    pane.setBounds(15, 20, 190, 50);
    pane.setBackground(Color.yellow);  // 設定畫布背景色
    add(pane);    

    btnShow = new JButton("呈現");
    Font f1 = new Font("新細明體" ,Font.BOLD, 16);
    btnShow.setFont(f1);
    Color rgb = new Color(255, 0, 0);
    btnShow.setForeground(rgb);        // 設定物件前景色
    btnShow.addActionListener(this);
    btnShow.setBounds(20, 80, 80, 25);
    add(btnShow);
        
    btnCls = new JButton("清除");
    btnCls.setFont(new Font("新細明體" ,Font.ITALIC, 16));
    btnCls.setBackground(new Color(255, 255, 0));   // 設定物件背景色 
    btnCls.addActionListener(this);
    btnCls.setBounds(120, 80, 80, 25);
    add(btnCls);
                
    setTitle("字形顏色設定");
    setLayout(null);
    setBounds(100, 100, 230, 150);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           
  }
    
  public void actionPerformed(ActionEvent e) {
    Graphics g = pane.getGraphics();
    g.setFont(new Font("標楷體", Font.ITALIC + Font.BOLD, 20));
    g.setColor(Color.red); 
    if (e.getSource() == btnShow) g.drawString("大家來學 Java2 !", 10, 30);
    if (e.getSource()== btnCls) pane.update(g);   // 清除pane畫布
  }
}

public class J9_4_1 {
  public static void main(String[] args)  {
    CGFrame frame = new CGFrame();
  }
}
