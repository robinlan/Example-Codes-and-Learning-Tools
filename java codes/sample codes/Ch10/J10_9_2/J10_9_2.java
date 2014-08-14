import javax.swing.*;
import java.awt.*;

class InterFrame extends JFrame {
  InterFrame() {
  	JTabbedPane tabPane = new JTabbedPane(JTabbedPane.LEFT); 
  	tabPane.setBounds(10, 10, 480, 300);
  	String[] title = {"FlowLayout", "BorderLayout", "GridLayout", "BoxLayout-X", "BoxLayout-Y"};
  	int tot_tab = title.length;
  	JPanel[] pane = new JPanel[tot_tab];
    for (int i = 0; i < tot_tab; i++) {
      pane[i]= new JPanel();
      pane[i].setLayout(null);
      pane[i].setBackground(Color.blue);
      tabPane.addTab(title[i],pane[i]);
    }
    add(tabPane);
     
    JInternalFrame[] iframe = new JInternalFrame[tot_tab];
    // FlowLayout 佈局方式
  	iframe[0] = new JInternalFrame("FlowLayout", true, false, true, false);
    iframe[0].setSize(240, 100);
    iframe[0].setLayout(new FlowLayout());
    iframe[0].setVisible(true);
    for(int i = 0; i < 4; i++) iframe[0].add(new JButton("按鈕" + i));
    pane[0].add(iframe[0]);	
    // BorderLayout 佈局方式
    iframe[1] = new JInternalFrame("BorderLayout", true, false, true, false);
    iframe[1].setSize(240, 200);
    iframe[1].setVisible(true);
    iframe[1].add(new JButton("東"), BorderLayout.EAST);
    iframe[1].add(new JButton("西"), BorderLayout.WEST);
    iframe[1].add(new JButton("南"), BorderLayout.SOUTH);
    iframe[1].add(new JButton("北"), BorderLayout.NORTH);
    iframe[1].add(new JButton("中"), BorderLayout.CENTER);
    pane[1].add(iframe[1]);	
    // GridLayout 佈局方式
    iframe[2] = new JInternalFrame("GridLayout", true, false, true, false);
    iframe[2].setSize(150, 150);
    iframe[2].setLayout(new GridLayout(2,2));
    iframe[2].setVisible(true);
    for(int i = 0; i < 4; i++) iframe[2].add(new JButton("按鈕" + i));
    pane[2].add(iframe[2]);	
    // BoxLayout-X 佈局方式
    iframe[3] = new JInternalFrame("BoxLayout-X", true, false, true, false);
    iframe[3].setSize(300, 100);
    JPanel c3 = new JPanel(); 
    c3.setLayout(new BoxLayout(c3, BoxLayout.X_AXIS));
    iframe[3].setVisible(true);
    c3.add(new JButton("0"));
    c3.add(Box.createHorizontalStrut(10));
    c3.add(new JButton("1"));
    c3.add(Box.createGlue());
    c3.add(new JButton("2"));
    c3.add(Box.createHorizontalGlue());
    c3.add(new JButton("3"));
    iframe[3].add(c3);
    pane[3].add(iframe[3]);	
    // BoxLayout-Y 佈局方式
    iframe[4] = new JInternalFrame("BoxLayout-Y", true, false, true, false);
    iframe[4].setSize(150, 240);
    JPanel c4 = new JPanel(); 
    c4.setLayout(new BoxLayout(c4, BoxLayout.Y_AXIS));
    iframe[4].setVisible(true);
    c4.add(new JButton("0"));
    c4.add(Box.createVerticalStrut(10));
    c4.add(new JButton("1"));
    c4.add(Box.createGlue());
    c4.add(new JButton("2"));
    c4.add(Box.createVerticalGlue());
    c4.add(new JButton("3"));
    iframe[4].add(c4);
    pane[4].add(iframe[4]);	
   
    setTitle("頁籤容器、內部視窗、佈局方式");
    setLayout(null);
    setBounds(50, 50, 500, 350);
    setBackground(Color.GRAY);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }
}

public class J10_9_2 {
  public static void main(String[] args) {
    InterFrame frame = new InterFrame();
  }
}