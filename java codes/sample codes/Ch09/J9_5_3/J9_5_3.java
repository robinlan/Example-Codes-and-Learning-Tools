import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class CDraw extends JFrame {
  CGPanel pane = new CGPanel();
  Point p1, p2;                 // 宣告點坐標物件
  boolean isDraw = false;       // 決定是否繪製
                
  CDraw() {
    pane.setBounds(0, 0, 300, 200);
    add(pane);
    pane.addMouseListener(new CDrawPic1());    
    pane.addMouseMotionListener(new CDrawPic2());    
   
    setTitle("用滑鼠繪製矩形");
    setLayout(null);
    setBounds(50, 50, 300, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
  
  class CDrawPic1 extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
      p1 = e.getPoint();       // 取得起點坐標
    }
       
    public void mouseReleased(MouseEvent e) {
      isDraw = false;         // 不能繪製
    }
  }  
  
  class CDrawPic2 extends MouseMotionAdapter {
    public void mouseDragged(MouseEvent e) {
      p2 = e.getPoint();       // 取得終點坐標
      isDraw = true;           // 可以繪製       
      repaint();               // 重繪圖形   
    }
  }  
  
 class CGPanel extends JPanel {
    public void paintComponent(Graphics g) {
      if (!isDraw) return;    // 若不能繪製,返回
      g.drawRect(p1.x, p1.y, p2.x-p1.x, p2.y-p1.y); // 繪製矩形
   }             
 } 
}

public class J9_5_3 {
  public static void main(String[] args) {
    CDraw frame1 = new CDraw();
  }
}