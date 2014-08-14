import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;

public class SampleP1 extends Applet
{
   public void paint(Graphics g)
   {
      g.setColor(Color.blue);
      g.setFont(new Font("Serif", Font.BOLD, 20));
      g.drawString("Hello", 20, 20);
   }
}
