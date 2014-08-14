import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class CGFrame extends JFrame implements ActionListener {
  JButton btnSmall, btnChange, btnCls;
  CGPanel pane1 = new CGPanel();
  JPanel pane2 = new JPanel();
  ImageIcon icon = new ImageIcon("pic.gif");
  Image img = icon.getImage();
    
  CGFrame(){
    pane1.setBounds(20, 20, 200, 200);
    pane1.setBorder(BorderFactory.createLineBorder(Color.red));
    add(pane1);
        
    pane2.setBounds(240, 20, 200, 200);
    pane2.setBackground(Color.yellow);   // 畫布背景色為黃色
    add(pane2);
        
    btnSmall = new JButton("縮小");
    btnSmall.addActionListener(this);
    btnSmall.setBounds(240, 230, 90, 20);
    add(btnSmall);
      
    btnChange = new JButton("左右相反");
    btnChange.addActionListener(this);
    btnChange.setBounds(350, 230, 90, 20);
    add(btnChange);
        
    setTitle("影像變形");
    setLayout(null);
    setBounds(50, 50, 470, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
 
  class CGPanel extends JPanel {
    public void paintComponent(Graphics g) {
      g.drawImage(img, 10, 10, this);	
    }        
  }
     
  public void actionPerformed(ActionEvent e) {
    Graphics g = pane2.getGraphics();
    pane2.update(g);      // 清除畫布
      
    if (e.getSource() == btnSmall) {
      int x = 10;
      int y = 10;
      int w = img.getWidth(this) / 2;
      int h = img.getHeight(this) / 2;
      g.drawImage(img, x, y, w, h, this);
    }
    if (e.getSource() == btnChange) {
      int x1 = 10;
      int y1 = 10;
      int x2 = img.getWidth(this) + x1;
      int y2 = img.getHeight(this) + y1;
      int sx1 = img.getWidth(this);
      int sy1 = 0;
      int sx2 = 0;
      int sy2 = img.getHeight(this);
      g.drawImage(img, x1, y1, x2, y2, sx1, sy1, sx2, sy2, this);  
    }
  }
}

public class J9_6_2{
  public static void main(String[] args){
    CGFrame frame = new CGFrame();
  }
}