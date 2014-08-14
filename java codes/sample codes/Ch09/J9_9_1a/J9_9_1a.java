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
 
  CDraw() {
    // 畫布容器
    pane1.setBounds(20, 20, 300, 300);
    pane1.setBorder(BorderFactory.createLineBorder(Color.black, 5));
    add(pane1);
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
  
  class CGPanel extends JPanel {
    public void paintComponent(Graphics g) {
      // 畫布
    }             
  }
}
public class J9_9_1a {
  public static void main(String[] args) {
    CDraw frame = new CDraw();
  }
}