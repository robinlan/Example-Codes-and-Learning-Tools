import javax.swing.*;
import java.awt.*;

class LayoutFrame extends JFrame {
  JPanel pane = new JPanel();        // pane放置按鈕
  String txt[] = {"東", "西", "南", "北", "中"}; 
  JButton btn[] = new JButton[txt.length];    // 建立5個按鈕

  LayoutFrame(){
    pane.setBorder(BorderFactory.createLineBorder(Color.red));
    pane.setLayout(new BorderLayout());
    add(pane);
    for(int i = 0; i < btn.length; i++) 
      btn[i] = new JButton(txt[i]);
    pane.add(btn[0], BorderLayout.EAST);
    pane.add(btn[1], BorderLayout.WEST);
    pane.add(btn[2], BorderLayout.SOUTH);
    pane.add(btn[3], BorderLayout.NORTH);
    pane.add(btn[4], BorderLayout.CENTER);
   
    setTitle("BorderLayout 佈局方式");
    setLocation(50,50);
    pack();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
}

public class J10_1_3 {
  public static void main(String[] args){
    LayoutFrame frame = new LayoutFrame();
  }
}