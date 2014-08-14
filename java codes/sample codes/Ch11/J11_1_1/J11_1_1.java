import javax.swing.*;
import java.awt.*;

class CMenuFrame extends JFrame {
 JMenuItem item11, item12, item13;
  
  CMenuFrame() {
    JMenuBar mnuBar = new JMenuBar();
    JMenu menu1 = new JMenu("內容");
    mnuBar.add(menu1);
    item11 = new JMenuItem("文字一");
    item12 = new JMenuItem("文字二");
    item13 = new JMenuItem("結束");
    menu1.add(item11);
    menu1.add(item12);
    menu1.addSeparator();
    menu1.add("結束");
        
    add(mnuBar, BorderLayout.NORTH);
    setTitle("功能表");
    setBounds(50, 50, 300, 200);   
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
}

public class J11_1_1 {
  public static void main(String[] args){
    CMenuFrame myFrame = new CMenuFrame();
  }
}