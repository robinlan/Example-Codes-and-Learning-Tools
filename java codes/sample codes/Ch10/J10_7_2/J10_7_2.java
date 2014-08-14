import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

class JComboFrame extends JFrame implements ItemListener {
  JLabel lblAcademic = new JLabel("學歷：");
  String[] items = {"博士", "碩士", "大學", "高中", "國中", "國小"};
  JComboBox combo = new JComboBox(items);
  JPanel pane = new JPanel();     
  JTextField texta = new JTextField();
 
  JComboFrame() {
    pane.add(lblAcademic);
    combo.addItemListener(this);
    pane.add(combo);
    add(pane, BorderLayout.NORTH);
   
    add(texta, BorderLayout.SOUTH);      
    
    setTitle("下拉式清單");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
    setBounds(50, 50, 200, 220);
    setVisible(true);   
  }

  public void itemStateChanged(ItemEvent e) {
    String text = lblAcademic.getText() + e.getItem().toString();
    texta.setText(text);
  }
}

public class J10_7_2 {
  public static void main(String[] args){
    JComboFrame frame = new JComboFrame();
  }
}