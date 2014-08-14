import javax.swing.*;

class CheckFrame extends JFrame {
  CheckFrame(){
    JPanel pane =  new JPanel();
    add(pane);
    
    JLabel lbl = new JLabel("請選擇字型樣式：");
    pane.add(lbl); 
    
    JCheckBox[] check = new JCheckBox[4];
    check[0] = new JCheckBox("一般", true);
    check[1] = new JCheckBox("斜體");
    check[2] = new JCheckBox("粗體");
    check[3] = new JCheckBox("斜體+粗體");
    for(int i = 0; i < check.length; i++) 
      pane.add(check[i]);
  
    setTitle("JCheckBox");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setVisible(true);
  }
}

public class J10_6_2 {
  public static void main(String[] args) {
    CheckFrame frame = new CheckFrame();
  }
}