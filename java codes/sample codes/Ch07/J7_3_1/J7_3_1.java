import javax.swing.*;
class CFrame extends JFrame {
  CFrame() {
    JLabel lbl1 = new JLabel("1 坪 = 3.3 平方公尺");
    lbl1.setBounds(50, 10, 200, 20);
    add(lbl1);
        
    JLabel lbl2 = new JLabel("輸入坪數：");
    lbl2.setBounds(20,50,100,20);
    add(lbl2);
       
    setTitle("土地面積換算");
    setLayout(null);
    setBounds(100, 100, 220, 180);
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }   
}

public class J7_3_1 {
  public static void main(String[] args) {
    CFrame frame = new CFrame();
  }
}
