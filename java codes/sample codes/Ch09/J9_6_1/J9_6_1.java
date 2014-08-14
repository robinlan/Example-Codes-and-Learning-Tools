import java.awt.*;
import javax.swing.*;

class CGFrame extends JFrame {
  CGFrame() {
    CGPanel pane = new CGPanel();
    pane.setBounds(20, 20, 200, 200);
    add(pane);
        
    setTitle("顯示影像");
    setLayout(null);
    setBounds(50, 50, 240, 250);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
 
  class CGPanel extends JPanel {
    public void paintComponent(Graphics g) {
      ImageIcon icon = new ImageIcon("pic.gif");
      Image img = icon.getImage();
      g.drawImage(img, 0, 0, this);	
    }
  }
}

public class J9_6_1 {
  public static void main(String[] args) {
    CGFrame frame = new CGFrame();
  }
}