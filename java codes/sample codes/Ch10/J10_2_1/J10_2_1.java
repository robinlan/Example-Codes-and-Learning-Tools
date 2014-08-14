import java.awt.*;
import javax.swing.*;

class CTextArea extends JFrame {
  CTextArea() {
    JLabel label = new JLabel("豬的告白：");
    String txt1 = "您好，我是豬小弟！\n";
    String txt2 = "今年是豬年，祝各位豬年行大運。\n";
    String txt3 = "我有一個外號，叫『天蓬大元帥』！您看，我帥不帥呢？";
    JTextArea txtPig = new JTextArea(txt1,5,18);
    txtPig.setBorder(BorderFactory.createLineBorder(Color.blue));
    txtPig.setLineWrap(true);
    txtPig.append(txt2);
    txtPig.append(txt3);
        
    JPanel pane =  new JPanel();
    pane.add(label);
    pane.add(txtPig);
   
    add(pane);
    setTitle("多行文字方塊");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack();
    setVisible(true);
  }	
}

public class J10_2_1 {
  public static void main(String[] args) {
    CTextArea frame = new CTextArea();
  }
}