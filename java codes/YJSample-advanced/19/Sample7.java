import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample7 extends JApplet
{
   private JLabel lb1, lb2;

   public void init()
   {
      // �悒澆献�
      lb1 = new JLabel("出随詳よ�V槍�C");
      lb2 = new JLabel();

      // �s�W�谺e捷い
      add(lb1, BorderLayout.NORTH);
      add(lb2, BorderLayout.SOUTH);

      // �n雀局泥��
      addKeyListener(new SampleKeyListener());
   }

   // 局泥�銘��O
   class SampleKeyListener extends KeyAdapter
   {
      public void keyPressed(KeyEvent e)
      {
         String str;
         int k = e.getKeyCode();
         switch(k){
            case KeyEvent.VK_UP:
              str = "�W"; break;
            case KeyEvent.VK_DOWN:
              str = "�U"; break;
            case KeyEvent.VK_LEFT:
              str = "オ"; break;
            case KeyEvent.VK_RIGHT:
              str = "�k"; break;
            default:
              str = "�筌Λ�槍";
         }
         lb2.setText(str + "�C");
      }
   }
}