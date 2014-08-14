import java.awt.*;
import javax.swing.*;

class ScrollFrame extends JFrame {
  ScrollFrame() {
    int tot = 8;	
    ImageIcon icon[] = new ImageIcon[tot];
    JLabel lblPig[] = new JLabel[tot];
    JPanel panePig = new JPanel();
    for (int i = 0; i < tot; i++) {
       lblPig[i] = new JLabel(new ImageIcon("fig/fig_" + i + ".jpg"));
       lblPig[i].setBorder(BorderFactory.createLineBorder(Color.green));
       panePig.add(lblPig[i]);
    }
    JScrollPane scroll = new JScrollPane(panePig,ScrollPaneConstants.
        VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    scroll.setBounds(10, 10, 322, 190); 
    add(scroll);
    
    //add(paneScroll);
    setTitle("±²°Ê¶b");
    setLayout(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(50, 50, 350, 250);
    setVisible(true);
  }	
}

public class J10_3_2 {
  public static void main(String[] args) {
    ScrollFrame frame = new ScrollFrame();
  }
}