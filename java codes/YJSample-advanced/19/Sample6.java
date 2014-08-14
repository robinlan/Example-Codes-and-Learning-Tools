import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample6 extends JApplet
{
   private JLabel lb;

   public void init()
   {
      // 建立元件
      lb = new JLabel("您好。");

      // 新增到容器中
      add(lb, BorderLayout.NORTH);

      // 登錄傾聽者
      addMouseListener(new SampleMouseListener());
   }

   // 傾聽者類別
   class SampleMouseListener extends MouseAdapter
   {
      public void mouseEntered(MouseEvent e)
      {
         lb.setText("歡迎光臨。");
      }
      public void mouseExited(MouseEvent e)
      {
         lb.setText("您好。");
      }
   }
}