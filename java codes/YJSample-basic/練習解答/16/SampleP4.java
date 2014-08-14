import java.applet.Applet;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class SampleP4 extends Applet implements MouseListener
{
   boolean bl = true;

   public void init()
   {
      addMouseListener(this);
   }
   public void mouseClicked(MouseEvent e)
   {
   }
   public void mouseEntered(MouseEvent e)
   {
      bl = true;
      repaint();
   }
   public void mouseExited(MouseEvent e)
   {
      bl = false;
      repaint();
   }
   public void mousePressed(MouseEvent e)
   {
   }
   public void mouseReleased(MouseEvent e)
   {
   }
   public void paint(Graphics g)
   {
      if(bl == true){
         g.drawString("您好", 10, 10);
      }
      else{
         g.drawString("再見", 10, 10);
      }
   }
}
