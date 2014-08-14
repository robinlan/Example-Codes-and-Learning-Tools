import java.awt.*;
import java.io.*;

abstract class Shape implements Serializable
{
   static final int RECT = 0;
   static final int OVAL = 1;
   static final int LINE = 2;

   int x1, y1, x2, y2;
   Color c;

   abstract public void draw(Graphics g);

   public void setColor(Color c)
   {
       this.c = c;
   }  
   public void setStartPoint(int x, int y)
   {
      x1 = x; y1 = y;
   }
   public void setEndPoint(int x, int y)
   {
      x2 = x; y2 = y;
   }
}
class Rect extends Shape implements Serializable
{
   public void draw(Graphics g)
   {
       g.setColor(c);
       g.fillRect(x1, y1, x2-x1, y2-y1);   
   }
}
class Oval extends Shape implements Serializable
{
   public void draw(Graphics g)
   {
       g.setColor(c);
       g.fillOval(x1, y1, x2-x1, y2-y1);   
   }
}
class Line extends Shape implements Serializable
{
   public void draw(Graphics g)
   {
       g.setColor(c);
       g.drawLine(x1, y1, x2, y2);   
   }
}