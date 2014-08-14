import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

class JSpinnerFrame extends JFrame implements ChangeListener {
  JLabel lblAge = new JLabel("¦~ÄÖ¡G");
  JSpinner spin = new JSpinner(new SpinnerNumberModel(20, 1, 100, 1));
  JPanel pane = new JPanel();     
  JTextField texta = new JTextField();
 
  JSpinnerFrame() {
    pane.add(lblAge);
    spin.setBounds(210, 10, 80, 20);
    spin.addChangeListener(this);
    pane.add(spin);
    
    add(pane, BorderLayout.NORTH);
    add(texta, BorderLayout.SOUTH);      
    setTitle("Spinner");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
    setBounds(50, 50, 150, 120);
    setVisible(true);   
  }

  public void stateChanged(ChangeEvent e) {
    String text = lblAge.getText() + spin.getValue();
    texta.setText(text);
  }
}

public class J10_8_1 {
  public static void main(String[] args){
    JSpinnerFrame frame = new JSpinnerFrame();
  }
}