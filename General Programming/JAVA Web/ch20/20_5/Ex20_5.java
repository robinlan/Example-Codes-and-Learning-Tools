import java.awt.*;
import java.awt.event.*;

class Ex20_5 extends Frame {
   public Ex20_5() { 
      super("myFrame");
      setSize(300, 350);
      setVisible(true);  

      addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent evt) {
              System.exit(0);
         }
      });
   }

    public static void main(String[] args) {
         Ex20_5 teststart = new Ex20_5();

    }
}
