import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class CDraw extends JFrame {
  Point p1, p2;               // 宣告點坐標物件
              
  CDraw() {
    addMouseListener(new CDrawPic1());    
    addMouseMotionListener(new CDrawPic2());       
 
    setTitle("用滑鼠繪製直線");
    setLayout(null);
    setBounds(50, 50, 300, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
  
  class CDrawPic1 extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
      p1 = e.getPoint();      // 取得起點坐標
    }
       
    public void mouseReleased(MouseEvent e) {
      Graphics g = getGraphics();
      g.drawLine(p1.x, p1.y, p2.x, p2.y);   // 繪製直線
    }
  }  
  
  class CDrawPic2 extends MouseMotionAdapter {
    public void mouseDragged(MouseEvent e) {
      p2 = e.getPoint();      // 取得終點坐標
    }
  }  
}

public class J9_5_2 {
  public static void main(String[] args) {
    CDraw frame = new CDraw();
  }
}