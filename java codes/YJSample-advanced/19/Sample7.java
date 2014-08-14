import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample7 extends JApplet
{
   private JLabel lb1, lb2;

   public void init()
   {
      // 建立元件
      lb1 = new JLabel("請選擇方向鍵。");
      lb2 = new JLabel();

      // 新增到容器中
      add(lb1, BorderLayout.NORTH);
      add(lb2, BorderLayout.SOUTH);

      // 登錄傾聽者
      addKeyListener(new SampleKeyListener());
   }

   // 傾聽者類別
   class SampleKeyListener extends KeyAdapter
   {
      public void keyPressed(KeyEvent e)
      {
         String str;
         int k = e.getKeyCode();
         switch(k){
            case KeyEvent.VK_UP:
              str = "上"; break;
            case KeyEvent.VK_DOWN:
              str = "下"; break;
            case KeyEvent.VK_LEFT:
              str = "左"; break;
            case KeyEvent.VK_RIGHT:
              str = "右"; break;
            default:
              str = "其它按鍵";
         }
         lb2.setText(str + "。");
      }
   }
}