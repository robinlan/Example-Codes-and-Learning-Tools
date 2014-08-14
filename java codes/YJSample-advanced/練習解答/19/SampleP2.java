import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SampleP2 extends JApplet
{
   private JButton bt;

   public void init()
   {
      // 建立元件
      bt = new JButton("歡迎。");

      // 新增到容器中
      add(bt, BorderLayout.NORTH);

      // 登錄傾聽者
      bt.addMouseListener(new SampleMouseListener());
   }

   // 傾聽者類別
   class SampleMouseListener extends MouseAdapter
   {
      public void mouseEntered(MouseEvent e)
      {
         bt.setText("歡迎光臨。");
      }
      public void mouseExited(MouseEvent e)
      {
         bt.setText("歡迎。");
      }
   }
}