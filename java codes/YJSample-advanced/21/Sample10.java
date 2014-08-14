import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample10 extends JApplet
{
   private JLabel lb;
   private JPanel pn;
   private JButton bt;

   public void init()
   {
      // 建立元件
      lb = new JLabel("歡迎光臨。");
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
         Container cnt = getContentPane();

         String title1 = "確認"; 
         String msg1 = "真的要購買嗎？";
         int type1 = JOptionPane.YES_NO_OPTION;

         String title2 = "購買"; 
         String msg2 = "謝謝惠顧。";
         int type2 = JOptionPane.INFORMATION_MESSAGE;

         int res =  JOptionPane.showConfirmDialog(cnt, msg1, title1, type1);
         if(res == JOptionPane.YES_OPTION)
            JOptionPane.showMessageDialog(cnt, msg2, title2, type2);
      }
   }
}