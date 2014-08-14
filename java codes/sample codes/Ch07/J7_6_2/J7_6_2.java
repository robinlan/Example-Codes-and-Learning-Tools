import javax.swing.*;
import java.awt.event.*;

class CMouseFrame extends JFrame {
  JLabel lblX = new JLabel("x = ");
  JLabel lblY = new JLabel("y = ");
    
  CMouseFrame() {
    lblX.setBounds(180, 190, 50, 20);
    add(lblX);
    lblY.setBounds(240, 190, 50, 20);
    add(lblY);
       
    addMouseMotionListener(new ClblShow());
    setTitle("顯示滑鼠的指標坐標");
    setLayout(null);
    setBounds(100, 100, 300, 250);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }  
   
  class ClblShow extends MouseMotionAdapter {
    public void mouseMoved(MouseEvent e){
      lblX.setText("x = " + e.getX());
      lblY.setText("y = " + e.getY());
    }
  } 
}

public class J7_6_2 {
  public static void main(String[] aegs) {
    CMouseFrame frame = new CMouseFrame();
  }  
} 
