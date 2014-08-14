import java.awt.*;
import java.awt.event.*;

class Ex20_3 extends Frame implements ActionListener {
   TextField input_Area = new TextField();
   TextField record_Area = new TextField();

   public Ex20_3() { 
      super("myFrame");

      add(input_Area, BorderLayout.SOUTH);
      add(record_Area, BorderLayout.CENTER);

      setSize(350, 300);
      setVisible(true); 

      input_Area.addActionListener(this);
   }

    public static void main(String[] args) {
      Ex20_3 teststart = new Ex20_3();
    }
   
    public void actionPerformed(ActionEvent evt) {
      if(evt.getSource() == input_Area) {
           System.out.println("An ActionEvent occurred on input_Area");
      }
    } 
}
