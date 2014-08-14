import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SampleP3 extends JApplet
{
   private JLabel lb1, lb2;
   private String str;

   public void init()
   {
      // 建立元件
      lb1 = new JLabel("請按下鍵盤按鍵。");
      lb2 = new JLabel();

      // 新增到容器中
      add(lb1, BorderLayout.NORTH);
      add(lb2, BorderLayout.CENTER);

      // 登錄傾聽者
      addKeyListener(new SampleKeyListener());
   }

   // 傾聽者類別
   class SampleKeyListener extends KeyAdapter
   {
      public void keyPressed(KeyEvent e)
      {
         char c = e.getKeyChar();
         lb2.setText("是" + c + "吧。");
      }
   }
}