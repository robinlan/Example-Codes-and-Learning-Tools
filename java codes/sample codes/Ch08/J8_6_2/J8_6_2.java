import javax.swing.*;
import java.awt.event.*;

class movePic implements Runnable {
  private JLabel lblPic;
  private int wide, pos_y, time;

  movePic(JLabel lbl, int w, int y, int t) {
    lblPic = lbl;
    wide = w;
    pos_y = y;
    time = t;    
  } 
    
  public void run() {
    int pos_x = 0;
    while (true) {
      pos_x += 2;
      lblPic.setLocation(pos_x, pos_y);
      Pause(time);
      if (pos_x >= wide) pos_x = 0;    
    }
  }
    
  void Pause(int msec) {
     try { Thread.sleep(msec); }
     catch(InterruptedException e) {}
  }
}

class Ccard extends JFrame {
  // 隨機取不重複亂數0∼8的宣告
  int min = 0, max = 8, rnd_no = 9;
  int tot_no = max - min + 1;
  int data[] = new int[tot_no];
  BBKMath OData = new BBKMath();
  // 共用變數或物件
  int i, j, k;                            // 迴圈變數
  ImageIcon icon[] = new ImageIcon[9];    // 9個圖形物件
  JLabel lbl[] = new JLabel[9];           // 9個放圖形的標籤
  ClblPic mouseObj = new ClblPic();       // 傾聽者物件
  int p_num = 0;                          // 記錄點按標籤的張數
  int press1 = -1, press2 = -1;           // 記錄第1,2次點按標籤的編號
  JLabel lblCar = new JLabel(new ImageIcon("fig/car.gif"));
       
  Ccard() {
    OData.GenRnd(data, min, max, rnd_no);  // 產生0~8亂數        
    // 9個圖形物件
    for (i = 0; i <= 8; i++) {
      icon[i] = new ImageIcon("fig/p_" + i + ".jpg");
    }
    // 9個標籤亂數放圖
    k = 0;
    for (i = 0; i <= 2; i++) {
      for (j = 0; j <= 2; j++){
        lbl[k]= new JLabel(icon[data[k]]);
        lbl[k].setBounds(10 + j * 160, 10 + i * 120, 160, 120);
        add(lbl[k]);
        lbl[k].addMouseListener(mouseObj);    // 註冊傾聽者
        k++;
      }
    }
        
    lblCar.setSize(65, 43);
    add(lblCar);
    int width = 510, c_y = 375, c_time = 25;
    Thread ThCar = new Thread(new movePic(lblCar, width, c_y, c_time));  
    ThCar.start();     
                      
    setTitle("3x3拼圖遊戲");
    setLayout(null);
    setBounds(100, 100, 510, 460);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }  
 
  // 事件處理
  class ClblPic extends MouseAdapter {
    public void mouseClicked(MouseEvent e) {
      for(i = 0; i <= 8; i++) {
        if(e.getSource() == lbl[i]) { 
          p_num++; 
          if (p_num == 1) press1 = i;
          if (p_num == 2) press2 = i;
        }    
      }
      // 兩張標籤交換圖片
      if (press1 != -1 && press2 != -1) {
        int temp = data[press1];
        data[press1] = data[press2];
        data[press2] = temp;
        lbl[press1].setIcon(icon[data[press1]]);
        lbl[press2].setIcon(icon[data[press2]]);
        p_num=0;
        press1 = -1;      // 兩次點按的記錄還原為-1
        press2 = -1;
      }
    }
  }         
} 

public class J8_6_2 {
  public static void main(String[] args)  {
    Ccard frame = new Ccard();
  }
}
