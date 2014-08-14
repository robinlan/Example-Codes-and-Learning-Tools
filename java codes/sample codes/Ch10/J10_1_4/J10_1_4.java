import javax.swing.*;
import java.awt.*;

class LayoutFrame extends JFrame {
  LayoutFrame() {
    JLabel lblPic[] = new JLabel[8];
    for (int i = 0; i < lblPic.length; i++) {
      lblPic[i] = new JLabel(new ImageIcon("fig/fig_" + i + ".jpg"));
      lblPic[i].setBorder(BorderFactory.createLineBorder(Color.blue));
      add(lblPic[i]);  
    }  
    
    setTitle("GridLayout §G§½¤è¦¡");
    setLayout(new GridLayout(2,4));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setVisible(true);
  }
}

public class J10_1_4 {
  public static void main(String[] args){
    LayoutFrame frame = new LayoutFrame();
  }
}