import javax.swing.*;

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

class CFrame extends JFrame {
  int w1, w2, y1, y2, time1, time2;
  JLabel lbl1 = new JLabel(new ImageIcon("fig/pic1.jpg"));
  JLabel lbl2 = new JLabel(new ImageIcon("fig/pic2.jpg"));
    
  CFrame() {
    lbl1.setSize(70, 70);
    add(lbl1);
    w1 = 400; y1 = 20; time1 = 50;
    Thread movePic1 = new Thread(new movePic(lbl1, w1, y1, time1));  
    movePic1.start();  
        
    lbl2.setSize(70, 70);
    add(lbl2);
    w2 = 400; y2 = 90; time2 = 200;
    Thread movePic2 = new Thread(new movePic(lbl2, w2, y2, time2));  
    movePic2.start();         

    setTitle("圖形動畫");
    setLayout(null);
    setBounds(100, 100, 400, 200);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

public class J8_6_1 {
  public static void main(String[] args) {
    CFrame frame = new CFrame();
  }    
}
