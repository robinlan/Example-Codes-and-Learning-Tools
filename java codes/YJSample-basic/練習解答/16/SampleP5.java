import java.applet.Applet;
import java.awt.Graphics;

public class SampleP5 extends Applet implements Runnable
{
   int num;
   int x;

   public void init()
   {
      Thread th;
      th = new Thread(this);
      th.start();
   }
   public void run()
   {
      try{
         for(int i=0; i<10; i++){
            num = i;
            x = i*10;
            repaint();
            Thread.sleep(1000);
         }
      }catch(InterruptedException e){}
   }
   public void paint(Graphics g)
   {
      String str = "³o¬O¼Æ¦r" + num + "³á¡C";
      g.drawString(str, x, 10);
   }
}

