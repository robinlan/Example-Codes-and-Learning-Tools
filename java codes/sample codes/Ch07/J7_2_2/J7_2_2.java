import javax.swing.*;
class CFrame extends JFrame {
  CFrame() {
    super("J7_2_1應用程式");
    setLocation(100, 120);
    setSize(400, 200);   
    setVisible(true);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}    
    
public class J7_2_2 {
  public static void main(String[] args) {
    CFrame frame1 = new CFrame();
    CFrame frame2 = new CFrame();
    frame2.setTitle("Ａ視窗");
    frame2.setBounds(550,60,150,250);
  }
}
    