import javax.swing.*;
import java.awt.event.*;

class Ccard extends JFrame {
  // 隨機取不重複亂數1～12的宣告
  int min = 1, max = 12, rnd_no = 12;
  int tot_no = max - min + 1;
  int data[] = new int[tot_no];
  BBKMath OData = new BBKMath();
  
  // 共用變數或物件
  int i, j, k;                               // 迴圈變數
  ImageIcon icon[] = new ImageIcon[13];      // 13個圖形物件
  JButton btn[] = new JButton[12];           // 12個放圖形的按鈕
  CbtnPic mouseObj = new CbtnPic();          // 傾聽者物件
  int p_num = 0;                             // 記錄翻1張或翻2張
  int press1 = -1, press2 = -1;              // 記錄第1,2次翻牌按鈕編號
  int correct = 0;                           // 累計配對成功次數 
  JLabel lblPass = new JLabel();             // 用來顯示完成訊息 
    
  Ccard() {
    OData.GenRnd(data, min, max, rnd_no);  // 產生1~12亂數        
    // 13個圖形物件
    icon[0] = new ImageIcon("fig_0.jpg");  // 牌背面圖
    for (i = 1; i <= 6; i++) {
      icon[i] = new ImageIcon("fig_" + i +".jpg");
      icon[i + 6] = new ImageIcon("fig_" + i +".jpg");
    }
    // 12個圖形按鈕，皆先放入牌背面圖　
    k = 0;
    for (i = 0; i <= 2; i++) {
      for (j = 0; j <= 3; j++){
        btn[k] = new JButton(icon[0]);
        btn[k].setBounds(10 + j * 160, 10 + i * 170, 150, 160);
        add(btn[k]);
        k++;
      }
    }
    for(k = 0; k <= 11; k++)
      btn[k].addMouseListener(mouseObj);    // 註冊傾聽者
      
    lblPass.setBounds(300, 515, 150, 20);     // 用來顯示完成訊息 
    add(lblPass);                      
    setTitle("4x3翻牌記憶遊戲");
    setLayout(null);
    setBounds(100, 100, 660, 570);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }  
 
  // 翻牌事件處理
  class CbtnPic extends MouseAdapter {
    public void mouseClicked(MouseEvent e){
      if(press1 != -1) {          
        if(e.getSource() == btn[press1]) return;
      }
      if(press2 != -1) {          
        if(e.getSource() == btn[press2]) return;
      }  
                  
      for(i = 0; i <= 11; i++) {
        // 偵測點按哪一個有效按鈕
        if (btn[i].isEnabled()) {
          if(e.getSource() == btn[i]) { 
            p_num++; 
            if (p_num > 2) {
              btn[press1].setIcon(icon[0]);
              btn[press2].setIcon(icon[0]);
              press1 = -1;      // 兩次翻牌的編號還原為-1
              press2 = -1;
              p_num = 1;
            }
                            
            btn[i].setIcon(icon[data[i]]);     // 圖片按鈕翻牌  
            if (p_num == 1) press1 = i;
              if (p_num == 2) press2 = i;
          }
        } 
      }
            
      if (press1 != -1 && press2 != -1) {
        if (Math.abs(data[press1] - data[press2]) == 6) {
          btn[press1].setEnabled(false);     // 取消兩張牌的按鈕功能
          btn[press2].setEnabled(false);
          p_num = 0;
          press1 = -1;      // 兩次翻牌的編號還原為-1
          press2 = -1;
          correct ++;       // 配對成功次數累加1
          // 如果配對成功次數等於全部牌的一半即遊戲成功完成
          if (correct == 6) lblPass.setText("恭喜成功！");
        }
      }
    }
  }         
} 

public class J7_9_2 {
  public static void main(String[] args)  {
    Ccard frame = new Ccard();
  }
}
