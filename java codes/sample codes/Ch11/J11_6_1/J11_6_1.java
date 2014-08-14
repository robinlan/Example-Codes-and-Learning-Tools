import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.*;

class cChangeCircle extends JFrame {
  JScrollBar scroll;                        // 宣告調整圓形縮放的捲軸
  JSlider[] slid = new JSlider[3];          // 宣告調整圓形顏色的調整器陣列
  Color paintCr = new Color(0, 0, 0);       // 圓形內部填色
  CGPanel paneL = new CGPanel();            // 繪製圓形的畫布容器 
  
  cChangeCircle() {
    paneL.setBounds(0, 15, 220, 230); 
    add(paneL);                             // 視窗加入paneL捲軸容器
     
    JPanel paneR = new JPanel();            // 放置捲軸及調整器的工具容器
    paneR.setBounds(240, 0, 210, 240);
    paneR.setLayout(null);
    add(paneR);                             // 視窗加入paneR工具容器
    
    scroll = new JScrollBar(JScrollBar.HORIZONTAL, 50, 0, 20, 200);
    scroll.setBounds(0, 20, 200, 20); 
    scroll.setUnitIncrement(4);
    scroll.addAdjustmentListener(Lscroll);  // 註冊捲軸事件傾聽者
    paneR.add(scroll);                      // 工具容器加入scroll捲軸
   
    Color[] SlidBackCr = {Color.red, Color.green, Color.blue};
    for (int i = 0; i < slid.length; i++) {
      slid[i] = new JSlider(JSlider.HORIZONTAL, 0, 255, 0);
      slid[i].setBounds(0, 60*(i+1), 200, 50); 
      slid[i].setBackground(SlidBackCr[i]);
      slid[i].setMajorTickSpacing(50);
      slid[i].setMinorTickSpacing(10);
      slid[i].addChangeListener(Lslid);     // 註冊調整器事件傾聽者
      slid[i].setPaintLabels(true);
      slid[i].setPaintTicks(true);
      paneR.add(slid[i]);                   // 工具容器加入slid[]調整器
    }
           
    setTitle("變化的圓形");
    setLayout(null);
    setBounds(50, 50, 460, 280);   
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  } 
  
  public AdjustmentListener Lscroll = new AdjustmentListener() {
    public void adjustmentValueChanged(AdjustmentEvent e) {
      repaint();
    }
  };
 
  public ChangeListener Lslid = new ChangeListener() {
    public void stateChanged(ChangeEvent e) {
      paintCr = new Color(slid[0].getValue(), slid[1].getValue(), slid[2].getValue());
      repaint();
    }
  };

  class CGPanel extends JPanel {
    public void paintComponent(Graphics g) {
      int x1 = (paneL.getHeight() - scroll.getValue()) / 2;
      int x2 = (paneL.getWidth() - scroll.getValue()) / 2;
      g.setColor(paintCr);	
      g.fillOval(x1, x2, scroll.getValue(), scroll.getValue());
    }             
  }
}
 
public class J11_6_1 {
  public static void main(String[] args) {
    cChangeCircle myFrame = new cChangeCircle();
  }
}