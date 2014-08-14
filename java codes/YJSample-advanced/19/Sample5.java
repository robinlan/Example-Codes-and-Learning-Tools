import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample5 extends JApplet
{
   private JLabel lb;

   public void init()
   {
      // 建立元件
      lb = new JLabel("歡迎光臨。");

      // 新增到容器中
      add(lb, BorderLayout.NORTH);

      // 登錄傾聽者
      addMouseListener(new SampleMouseListener());
   }

   // 傾聽者類別
   class SampleMouseListener extends MouseAdapter
   {
      public void mouseClicked(MouseEvent e)
      {
         lb.setText("謝謝惠顧。");
      }
   }
}
