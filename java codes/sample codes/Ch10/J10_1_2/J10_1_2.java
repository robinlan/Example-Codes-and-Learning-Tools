import javax.swing.*;
import java.awt.*;

class CDraw extends JFrame {
  JPanel pane = new JPanel();        // pane放置按鈕
  String txtDraw[] = {"畫直線", "畫矩形", "畫橢圓", "實心矩", "實心圓"}; 
  JButton btnDw[] = new JButton[txtDraw.length];    // 繪圖工具按鈕
   
  CDraw(){
    // 放置繪圖工具按鈕容器
    pane.setBounds(300, 20, 120, 220);
    pane.setBackground(Color.pink);
    pane.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
    add(pane);    
    for(int i = 0; i < txtDraw.length; i++) {
      btnDw[i] = new JButton(txtDraw[i]);
      pane.add(btnDw[i]);  
    }
       
    setTitle("繪圖工具");
    setLayout(null);
    setBounds(50, 50, 450, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
}

public class J10_1_2 {
  public static void main(String[] args){
    CDraw frame = new CDraw();
  }
}