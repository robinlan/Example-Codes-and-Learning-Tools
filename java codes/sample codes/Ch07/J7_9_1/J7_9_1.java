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
        
    setTitle("4x3翻牌記憶遊戲");
    setLayout(null);
    setBounds(100, 100, 660, 570);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }  
 
  // 翻牌事件處理
  class CbtnPic extends MouseAdapter {
    public void mouseClicked(MouseEvent e){
      for(i = 0; i <= 11; i++) {
        // 偵測點按哪一個有效按鈕
        if (btn[i].isEnabled()) {
          if(e.getSource() == btn[i]) { 
            btn[i].setIcon(icon[data[i]]);     // 圖片按鈕翻牌  
          }    
        } 
      }
    }
  }         
} 

public class J7_9_1 {
  public static void main(String[] args)  {
    Ccard frame = new Ccard();
  }
}
