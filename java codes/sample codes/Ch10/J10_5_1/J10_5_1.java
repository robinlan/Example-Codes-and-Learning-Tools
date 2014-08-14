import javax.swing.*;
import java.awt.*;

class LayoutFrame extends JFrame {
  LayoutFrame() {
    JInternalFrame iframe = new JInternalFrame("FlowLayout", true, true, true, true);
    iframe.setBounds(50, 30, 150, 220);
    iframe.setLayout(new FlowLayout(FlowLayout.LEFT));
    iframe.setVisible(true);
    add(iframe);
  	    
    JButton button[] = new JButton[5];
    for(int i = 0; i < button.length; i++) {
      button[i] = new JButton("«ö¶s" + i);
      iframe.add(button[i]);
    }
    
    setTitle("¤º³¡µøµ¡");
    setLayout(null);
    setBounds(50, 50, 300, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
}

public class J10_5_1 {
  public static void main(String[] args){
    LayoutFrame frame = new LayoutFrame();
  }
}