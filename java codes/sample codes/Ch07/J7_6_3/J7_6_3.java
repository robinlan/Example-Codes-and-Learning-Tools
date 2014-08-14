import javax.swing.*;
import java.awt.event.*;

class CMouseFrame extends JFrame implements MouseListener {
  JLabel lblMove = new JLabel( new ImageIcon("fig_0.jpg"));
  int pos_x = 70, pos_y = 30;

  CMouseFrame() {
    lblMove.setBounds(pos_x, pos_y, 150, 160);
    add(lblMove);
                        
    addMouseListener(this);
    setTitle("滑鼠指標牽引圖形");
    setLayout(null);
    setBounds(100, 100, 300, 250);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }  
  
  public void mousePressed(MouseEvent e){
    if (e.getClickCount() == 2) {
      pos_x = e.getX() - 4 - 75;
      pos_y = e.getY() - 30 - 80; 
    } else {
      pos_x = e.getX() - 4 ;
      pos_y = e.getY() - 30 ;
    }
    lblMove.setLocation(pos_x, pos_y);
  }
  public void mouseClicked(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  public void mouseReleased(MouseEvent e){}
}

public class J7_6_3 {
  public static void main(String[] aegs) {
    CMouseFrame frame = new CMouseFrame();
  }  
} 
