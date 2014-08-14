import javax.swing.*;
import java.awt.event.*;

class CFrame extends JFrame implements ActionListener {
  // 共用變數或物件
  int i, j, k;                             // 迴圈變數
  ImageIcon icon[] = new ImageIcon[7];     // 圖形物件
  JButton btn[] = new JButton[6];          // 放圖形的按鈕
  JButton btnOK = new JButton("可以翻牌");
  JButton btnNo = new JButton("不可以翻牌");
  CbtnPic mouseObj = new CbtnPic();        // 傾聽者物件
    
  CFrame() {
    // 圖形按鈕
    icon[0] = new ImageIcon("fig_0.jpg"); // 牌背面圖
    for (i = 1; i <= 3; i++) {
      icon[i] = new ImageIcon("fig_" + i + ".jpg");
      icon[i + 3] = new ImageIcon("fig_" + i + ".jpg");
    }
    k = 0;
    for (i = 0; i <= 1; i++) {
      for (j = 0; j <= 2; j++){
        btn[k] = new JButton(icon[0]);    // 牌背面圖放入六個按鈕
        btn[k].setBounds(10 + j * 160, 10 + i * 170, 150, 160);
        add(btn[k]);
        k++;
      }
    }
    // 按鈕
    btnOK.addActionListener(this);    // 註冊傾聽者
    btnOK.setBounds(120, 350, 120, 25);
    add(btnOK);
    btnNo.addActionListener(this);    // 註冊傾聽者
    btnNo.setBounds(250, 350, 120, 25);
    add(btnNo);
        
    setTitle("翻牌");
    setLayout(null);
    setBounds(100, 100, 500, 420);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }  
 
  // 按鈕事件處理
  public void actionPerformed(ActionEvent e)  {
    if (e.getSource() == btnOK) {
      for(k = 0; k <= 5; k++)
        btn[k].addMouseListener(mouseObj);    // 註冊傾聽者
    }
    if (e.getSource() == btnNo) {
      for(k = 0; k <= 5; k++)
      btn[k].removeMouseListener(mouseObj); // 取消傾聽者
    }
  }
   
  // 翻牌事件處理
  class CbtnPic extends MouseAdapter {
    public void mouseClicked(MouseEvent e){
      for(i = 0; i <= 5; i++)
        if(e.getSource() == btn[i])     // 偵測點按哪一個按鈕
      btn[i].setIcon(icon[i + 1]);    // 圖片按鈕翻牌         
    }
  }
} 

public class J7_7_1 {
  public static void main(String[] args)  {
    CFrame frame = new CFrame();
  }
}
