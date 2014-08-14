import java.awt.*;
import javax.swing.*;

class TabsFrame extends JFrame {
  TabsFrame() {
    JTabbedPane tabPane = new JTabbedPane();
    tabPane.setBounds(10, 10, 220, 180);
    add(tabPane);
    
    String tabTxt1 = "告白";
    String txt = "您好！我是豬小弟。\n今年是豬年，祝各位豬年行大運。\n我有一個外號，叫『天蓬大元帥』。您看，我帥不帥呢？";
    JTextArea txtPig = new JTextArea(txt);
    txtPig.setLineWrap(true);
    tabPane.addTab(tabTxt1, txtPig);	
    
    String tabTxt2 = "玉照";
    JLabel lblPig = new JLabel(new ImageIcon("pig.jpg"));
    tabPane.addTab(tabTxt2, lblPig);
   
    setTitle("頁籤");
    setLayout(null);
    setBounds(50, 50, 250, 230);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }	
}

public class J10_4_1 {
  public static void main(String[] args) {
    TabsFrame frame = new TabsFrame();
  }
}