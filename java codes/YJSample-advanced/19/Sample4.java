import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample4 extends JApplet
{
   private JLabel lb;
   private JButton bt;

   public void init()
   {
      // 建立元件
      lb = new JLabel("歡迎光臨。");
      bt = new JButton("購買");

      // 新增到容器中
      add(lb, BorderLayout.NORTH);
      add(bt, BorderLayout.SOUTH);

      // 登錄傾聽者
      bt.addActionListener(new SampleActionListener());
   }

   // 傾聽者類別
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         lb.setText("謝謝惠顧。");
      }
   }
}
