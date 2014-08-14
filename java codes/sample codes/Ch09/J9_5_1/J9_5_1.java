import java.awt.*;
import javax.swing.*;

class CGFrame extends JFrame {
  CGFrame() {
    setTitle("繪製各式圖形");
    setBounds(50, 50, 490, 250);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
 
  public void paint(Graphics g) {
    g.setColor(Color.black);
    g.drawLine(20, 50, 100, 130);           // 繪製直線
	g.drawRect(140, 50, 80, 80); 	        // 繪製矩形框線
	g.drawOval(260, 50, 80, 80);            // 繪製圓形圓周線
	g.drawArc(380, 50, 80, 80, 30, 180);    // 繪製弧形
	  
	g.setColor(new Color(128, 255, 0));	
	g.fillArc(20, 150, 80, 80, 30, 180);        // 扇形內部填色
	g.setColor(new Color(180, 0, 200));	
	g.fillRoundRect(140, 150, 80, 82, 10, 10);  // 圓角矩形內部填色
	g.setColor(new Color(255, 255, 128));
	g.fillOval(270, 140, 60, 100);	            // 橢圓形內部填色
	g.setColor(new Color(255, 0, 255));
 	int x[] = {380, 440, 460, 450, 400, 390};
    int y[] = {150, 190, 180, 220, 200, 230};
    g.fillPolygon(x, y, 6);                     // 多邊形內部填色
  }
}

public class J9_5_1 {
  public static void main(String[] args) {
    CGFrame frame = new CGFrame();
  }
}
