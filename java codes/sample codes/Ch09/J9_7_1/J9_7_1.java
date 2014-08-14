import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

class CGFrame extends JFrame implements ActionListener {
  JButton btnCr[] = new JButton[5];    // 建立五個顏色按鈕
  JPanel pane = new JPanel();
  Color cr[] ={Color.red, Color.yellow, Color.green, Color.pink, Color.cyan};
            
  CGFrame() {
    pane.setBounds(20, 20, 200, 200);
    // 設定pane畫布的框線厚度為 4 像素
    pane.setBorder(BorderFactory.createLineBorder(Color.blue, 4));
    add(pane);
        
    for (int i = 0; i <= 4; i++) {
      btnCr[i] = new JButton();
      btnCr[i].setBounds(21 + i * 42, 230, 30, 30);
      // 設定顏色按鈕目前全部呈浮凸狀
      btnCr[i].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
      btnCr[i].setBackground(cr[i]);
      add(btnCr[i]);
      btnCr[i].addActionListener(this);
    }
      
    setTitle("點選顏色");
    setLayout(null);
    setBounds(50, 50, 248, 310);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
    
  public void actionPerformed(ActionEvent e) {
    for(int k = 0; k <= 4; k++) {
      // 先將全部顏色按鈕還原為浮凸狀	
      btnCr[k].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
      if(e.getSource() == btnCr[k]) { 
        pane.setBackground(cr[k]);
        // 被點按的顏色按鈕呈凹陷狀	
        btnCr[k].setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
      }    
    }
  }
}

public class J9_7_1 {
  public static void main(String[] args) {
    CGFrame frame = new CGFrame();
  }
}