import javax.swing.*;
import java.awt.event.*;
class CMouseFrame extends JFrame implements MouseListener, MouseMotionListener {
  JLabel lblMove = new JLabel( new ImageIcon("fig_0.jpg"));
  int pos_x = 70, pos_y = 30, x1, y1, x2, y2;
  boolean isDrag = false;

  CMouseFrame() {
    lblMove.addMouseListener(this);
    lblMove.addMouseMotionListener(this);    
    lblMove.setBounds(pos_x, pos_y, 150, 160);
    add(lblMove);
       
    setTitle("滑鼠指標拖曳圖形");
    setLayout(null);
    setBounds(100, 100, 300, 250);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }  
  
  public void mousePressed(MouseEvent e) {
    if (isDrag) return;
    if (e.getButton() == 1) isDrag = true;  
    x1 = e.getX();
    y1 = e.getY();
  }
  public void mouseClicked(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  public void mouseReleased(MouseEvent e) {
    if(!isDrag) return;
    isDrag = false;
  }
  public void mouseDragged(MouseEvent e) {
    if(!isDrag) return;
    x2 = e.getX();
    y2 = e.getY();
    pos_x = pos_x + (x2 - x1);
    pos_y = pos_y + (y2 - y1); 
    lblMove.setLocation(pos_x, pos_y);
  }
  public void mouseMoved(MouseEvent e){}
}

public class J7_6_4 {
  public static void main(String[] aegs) {
    CMouseFrame frame = new CMouseFrame();
  }  
} 
