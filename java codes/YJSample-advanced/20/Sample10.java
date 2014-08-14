import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample10 extends JApplet
{
   private JLabel lb;
   private JPanel pn;
   private JButton bt;
   private Icon ic;

   public void init()
   {
      // 建立元件
      lb = new JLabel("歡迎光臨。");
      pn = new JPanel();
      bt = new JButton("購買");
      ic = new ImageIcon(getImage(getDocumentBase(), "car.gif"));

      // 設定元件
      bt.setIcon(ic);

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
         lb.setText("謝謝惠顧。");
         bt.setEnabled(false);
      }
   }
}