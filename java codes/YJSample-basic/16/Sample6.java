import java.applet.Applet;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Sample6 extends Applet implements ActionListener
{
   Button bt;

   public void init()
   {
      bt = new Button("¶}©l"); 
      add(bt);
      bt.addActionListener(this);
   }
   public void actionPerformed(ActionEvent ae)
   {
       bt.setLabel("°±¤î");
   }
}