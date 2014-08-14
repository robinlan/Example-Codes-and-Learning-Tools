import java.applet.Applet;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Sample4 extends Applet implements MouseListener
{
   int x = 10;
   int y = 10;

   public void init()
   {
      addMouseListener(this);
   }
   public void mouseClicked(MouseEvent e)
   {
   }
   public void mouseEntered(MouseEvent e)
   {
   }
   public void mouseExited(MouseEvent e)
   {
   }
   public void mousePressed(MouseEvent e)
   {
       x = e.getX();
       y = e.getY();
       repaint();
   }
   public void mouseReleased(MouseEvent e)
   {
   }
   public void paint(Graphics g)
   {
      g.fillOval(x, y, 10, 10);
   }
}
