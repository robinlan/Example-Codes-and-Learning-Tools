import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

class cScrollBar extends JFrame implements AdjustmentListener {
  JScrollBar scrollbar;
 
  cScrollBar() {
    CGPanel pane = new CGPanel();          // 宣告建立pane畫布
    add(pane);                             // 加入pane畫布
    scrollbar = new JScrollBar(JScrollBar.HORIZONTAL, 50, 10, 20, 250);
    scrollbar.setUnitIncrement(4);
    scrollbar.addAdjustmentListener(this); // 註冊事件傾聽者
    add(scrollbar, BorderLayout.SOUTH);    // 加入scrollbar捲動軸
       
    setTitle("捲動軸");
    setBounds(50, 50, 280, 320);   
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  } 

  public void adjustmentValueChanged(AdjustmentEvent e) {
    repaint();
  }

  class CGPanel extends JPanel {
    public void paintComponent(Graphics g) {
      g.fillOval(10, 10, scrollbar.getValue(), scrollbar.getValue());
    }             
  }
}
 
public class J11_4_1 {
  public static void main(String[] args) {
    cScrollBar myFrame = new cScrollBar();
  }
}