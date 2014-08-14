import java.awt.*;
import java.awt.event.*;

class Ex20_2 extends Frame implements ActionListener {
   public Ex20_2() { 
      super("myFrame");

      TextField tfS = new TextField("South Area");
      add(tfS, BorderLayout.SOUTH);

      TextField tfC = new TextField("Center Area");
      add(tfC, BorderLayout.CENTER);

      setSize(350, 300);
      setVisible(true); 

      tfS.addActionListener(this);
   }

    public static void main(String[] args) {
      Ex20_2 teststart = new Ex20_2();
    }
   
    public void actionPerformed(ActionEvent evt) {
      System.out.println("An ActionEvent occurred on tfS");
    } 
}
