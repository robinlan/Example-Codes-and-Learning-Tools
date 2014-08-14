import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SampleP5 extends JApplet
{
   private JLabel  lb;
   private JPanel pn;
   private JButton bt;

   public void init()
   {
      // 建立元件
      lb= new JLabel("歡迎光臨。");
      pn = new JPanel();
      bt = new JButton("購買");

      // 新增到容器中
      pn.add(bt);
      add(lb, BorderLayout.NORTH);
      add(pn, BorderLayout.SOUTH);

      // 登錄傾聽者
      bt.addActionListener(new SampleActionListener());
   }

   // 傾聽者類別
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         String title = "感謝卡"; 
         String msg = "非常感謝您的購買。";
         int type = JOptionPane.INFORMATION_MESSAGE;

         JOptionPane.showMessageDialog(getContentPane(), msg, title, type);
      }
   }
}