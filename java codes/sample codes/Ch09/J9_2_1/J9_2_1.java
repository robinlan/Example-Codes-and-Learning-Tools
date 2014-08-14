import java.awt.*;
import javax.swing.*;

class CGFrame extends JFrame{
  CGFrame(){
    setTitle("JFrame畫布");
    setBounds(50, 50, 200, 150);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
 
  public void paint(Graphics g){
    g.drawString("大家來學 Java2 !", 50, 70);
  }
}

public class J9_2_1{
  public static void main(String[] args){
    CGFrame frame = new CGFrame();
  }
}