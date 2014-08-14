import javax.swing.*;
import java.awt.*;


class RadioFrame extends JFrame{
  RadioFrame(){
    JPanel pane =  new JPanel();
    add(pane);
    
    JLabel lbl = new JLabel("請選擇對齊方式：");
    pane.add(lbl); 
    
    JRadioButton[] radio = new JRadioButton[3];
    radio[0] = new JRadioButton("靠左", true);
    radio[1] = new JRadioButton("置中");
    radio[2] = new JRadioButton("靠右"); 
    ButtonGroup group = new ButtonGroup();
    for(int i = 0; i < radio.length; i++) {
      group.add(radio[i]);
      pane.add(radio[i]);
    }  
  
    setTitle("JRadioButton");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setVisible(true);
  }
}

public class J10_6_1 {
  public static void main(String[] args){
    RadioFrame frame = new RadioFrame();
  }
}