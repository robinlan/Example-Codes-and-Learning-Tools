import javax.swing.*;
import java.awt.event.*;

class CMouseFrame extends JFrame {
  ImageIcon icon1 = new ImageIcon("fig_1.jpg");
  ImageIcon icon2 = new ImageIcon("fig_2.jpg");
  JLabel lblPic = new JLabel(icon1);
  int pos_x = 70, pos_y = 30;

  CMouseFrame() {
    lblPic.addMouseListener(new ClblPic());
    lblPic.setBounds(pos_x, pos_y, 150, 160);
    add(lblPic);
                        
    setTitle("ÂI«ö·Æ¹« ¤Á´«¹Ï§Î");
    setLayout(null);
    setBounds(100,100,300,250);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }  
   
  class ClblPic extends MouseAdapter {
    public void mouseClicked(MouseEvent e){
      if (lblPic.getIcon() == icon1) 
        lblPic.setIcon(icon2);
      else
        lblPic.setIcon(icon1);
    }
  } 
}

public class J7_6_1 {
  public static void main(String[] aegs) {
    CMouseFrame frame = new CMouseFrame();
  }  
} 
