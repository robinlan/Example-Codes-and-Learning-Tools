import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

class CDraw extends JFrame {
  CGPanel pane1 = new CGPanel();      // pane1做為畫布
  JPanel pane2 = new JPanel();        // pane2放置繪圖工具按鈕
  JPanel pane3 = new JPanel();        // pane3放置顏色按鈕
  JButton btnDw[] = new JButton[5];    // 6個繪圖工具按鈕
  String draw_btn = new String();      // 繪製的圖形類型
  String txtDraw[] = {"直線", "矩形", "橢圓", "實心矩形", "實心橢圓"}; 
  JButton btnCr[] = new JButton[6];   // 6個顏色按鈕
  Color cr[] = {Color.black, Color.red, Color.magenta, Color.green, Color.pink, Color.cyan};
  Color draw_color = Color.black;      // 畫筆顏色，預設為黑色
  Point p1, p2;                        // 最新的起點、終點坐標 
  boolean isDraw = false;              // 決定滑鼠是否能在畫布上繪圖
  String draw[] = new String[200];     // 存放圖形類型陣列變數
  Point p_b[] = new Point[200];        // 存放起點陣列變數
  Point p_e[] = new Point[200];        // 存放終點陣列變數
  Color pen_color[] = new Color[200];  // 存放顏色陣列變數
  int count = 0;                       // 存放圖形個數變數 
           
  CDraw() {
    // 畫布容器
    pane1.setBounds(20, 20, 300, 300);
    pane1.setBorder(BorderFactory.createLineBorder(Color.black, 5));
    add(pane1);
    pane1.addMouseListener(new CDrawPic1());    
    pane1.addMouseMotionListener(new CDrawPic2());       
    // 放置繪圖工具按鈕容器
    pane2.setBounds(330, 20, 100, 170);
    pane2.setLayout(null);
    add(pane2);    
    for(int i = 0; i < 5; i++) {
      btnDw[i] = new JButton(txtDraw[i]);
      btnDw[i].setBounds(10, i*30, 80, 20);  
      btnDw[i].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
      pane2.add(btnDw[i]);
      btnDw[i].addMouseListener(new CbtnDw());  
    }
    // 放置顏色按鈕容器   
    pane3.setBounds(330, 190, 100, 130);
    pane3.setLayout(null);
    add(pane3);        
    int k = 0 ;      
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 2; j++) {
        btnCr[k] = new JButton();
        btnCr[k].setBounds(15+j*40, 15+i*40, 30, 30);
        btnCr[k].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        btnCr[k].setBackground(cr[k]);
        pane3.add(btnCr[k]);
        btnCr[k].addMouseListener(new CbtnCr());
        k++;
      }
    }
 
    setTitle("簡易繪圖程式");
    setLayout(null);
    setBounds(50, 50, 450, 370);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
  
  class CbtnDw extends MouseAdapter {
    public void mouseClicked(MouseEvent e){
      for(int i = 0; i < 5; i++) {
        btnDw[i].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        if(e.getSource() == btnDw[i]) { 
          btnDw[i].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
          draw_btn = btnDw[i].getText();
        }    
      }
    }
  }  
    
  class CbtnCr extends MouseAdapter {
    public void mouseClicked(MouseEvent e){
      for(int k = 0; k < 6; k++) {
        btnCr[k].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        if(e.getSource() == btnCr[k]) { 
          draw_color = cr[k];
          btnCr[k].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }    
      }
    }
  }
  
  class CDrawPic1 extends MouseAdapter {
    public void mousePressed(MouseEvent e) {
      isDraw = false;
      p1 = e.getPoint();
    }
       
    public void mouseReleased(MouseEvent e) {
      if (isDraw) count++;
      isDraw = false;
    }
  }  
  
  class CDrawPic2 extends MouseMotionAdapter {
    public void mouseDragged(MouseEvent e) {
      p2 = e.getPoint();
      isDraw = true;
      repaint();
    }
  }  
 
  class CGPanel extends JPanel {
    public void paintComponent(Graphics g) {
      if (!isDraw) return;
      // 重繪即有圖形
      for(int i =0; i<count; i++) {
        g.setColor(pen_color[i]);
        drawing(g, draw[i], p_b[i].x, p_b[i].y, p_e[i].x, p_e[i].y);
      }  
      // 繪製新圖形     
      g.setColor(draw_color);
      drawing(g, draw_btn, p1.x, p1.y, p2.x, p2.y);
      // 將最新繪製的圖形記錄在陣列變數中
      pen_color[count] = draw_color;
      p_b[count] = p1;
      p_e[count] = p2;
      draw[count]= draw_btn; 
    }             
  }
 
  public void drawing(Graphics g, String st, int px1, int py1, int px2, int py2) {
  	// 在畫布繪製圖形
  	if (st.equals("直線")) g.drawLine(px1, py1, px2, py2);   
    if (st.equals("矩形")) g.drawRect(px1, py1, px2-px1, py2-py1);   
    if (st.equals("橢圓")) g.drawOval(px1, py1, px2-px1, py2-py1);   
    if (st.equals("實心矩形")) g.fillRect(px1, py1, px2-px1, py2-py1);   
    if (st.equals("實心橢圓")) g.fillOval(px1, py1, px2-px1, py2-py1);    
  } 
 
}
public class J9_9_1c {
  public static void main(String[] args) {
    CDraw frame1 = new CDraw();
  }
}