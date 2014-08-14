import javax.swing.*;
import java.awt.event.*;

class CKeyFrame extends JFrame implements KeyListener {
  ImageIcon icon1 = new ImageIcon("fig_1.jpg");
  JLabel lblPic = new JLabel(icon1);
  int pos_x = 70, pos_y = 30;

  CKeyFrame() {
    lblPic.setBounds(pos_x, pos_y, 150, 160);
    add(lblPic);
        
    addKeyListener(this);        
    setTitle("按方向鍵 移動圖形");
    setLayout(null);
    setBounds(100, 100, 300, 250);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }  
     
  public void keyPressed(KeyEvent e) {
    switch(e.getKeyCode()) {
      case KeyEvent.VK_UP:     // 向上
           pos_y -= 5;
           break;
      case KeyEvent.VK_DOWN:   // 向下
           pos_y += 5; 
           break;   
      case KeyEvent.VK_LEFT:   // 向左
           pos_x -= 5;
           break;  
      case KeyEvent.VK_RIGHT:  // 向右
           pos_x += 5;
           break;  
    }
    lblPic.setLocation(pos_x, pos_y);
  }
  public void keyTyped(KeyEvent e) {}    
  public void keyReleased(KeyEvent e) {}
}

public class J7_5_1 {
  public static void main(String[] aegs) {
    CKeyFrame frame = new CKeyFrame();
  }  
}