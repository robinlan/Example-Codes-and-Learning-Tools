import javax.swing.*;
import java.awt.*;

class LayoutFrame extends JFrame {
  JPanel pane1 = new JPanel();
  JLabel lblPic1[] = new JLabel[3];
  JPanel pane2 = new JPanel(); 
  JLabel lblPic2[] = new JLabel[4];
   
  LayoutFrame() {
    // pane1容器--BoxLayout.X_AXIS
    pane1.setBounds(20, 20, 530, 200);
    pane1.setLayout(new BoxLayout(pane1, BoxLayout.X_AXIS));
    pane1.setBackground(Color.pink);
    add(pane1);    
    for(int i = 0; i < lblPic1.length; i++) {
      lblPic1[i] = new JLabel(new ImageIcon("pic1/pic" + i + ".jpg"));
      pane1.add(lblPic1[i]);  
    }
    // pane2容器--BoxLayout.Y_AXIS 
    pane2.setBounds(560, 20, 50, 200);
    pane2.setBorder(BorderFactory.createLineBorder(Color.blue));
    pane2.setLayout(new BoxLayout(pane2, BoxLayout.Y_AXIS));
    add(pane2);    
    for(int j = 0; j < lblPic2.length; j++) 
      lblPic2[j] = new JLabel(new ImageIcon("pic2/pic_" + j + ".jpg"));
    pane2.add(lblPic2[0]); 
    pane2.add(Box.createVerticalStrut(10)); 
    pane2.add(lblPic2[1]);
    pane2.add(Box.createGlue()); 
    pane2.add(lblPic2[2]);
    pane2.add(Box.createVerticalGlue()); 
    pane2.add(lblPic2[3]);
    
    setTitle("BoxLayout 佈局方式");
    setLayout(null);
    setBounds(50, 50, 640, 280);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
}

public class J10_1_5 {
  public static void main(String[] args){
    LayoutFrame frame = new LayoutFrame();
  }
}