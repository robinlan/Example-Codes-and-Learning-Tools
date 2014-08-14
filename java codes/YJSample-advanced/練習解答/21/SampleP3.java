import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SampleP3 extends JFrame
{
   private JLabel lb;
   private JPanel pn;
   private JButton bt;

   public static void main(String[] args)
   {
      SampleP3 sm = new SampleP3();
   }
   public SampleP3()
   {
      // 設定標題
      super("範例");

      // 建立元件
      lb = new JLabel("歡迎光臨。");
      pn = new JPanel();
      bt = new JButton("購買");

      // 新增到容器中
      pn.add(bt);
      add(lb, BorderLayout.NORTH);
      add(pn, BorderLayout.SOUTH);

      // 登錄傾聽者
      addWindowListener(new SampleWindowListener());
      bt.addActionListener(new SampleActionListener());

      // 設定框架
      setSize(200, 200);
      setVisible(true);
   }

   // 傾聽者類別
   class SampleWindowListener extends WindowAdapter
   {
      public void windowClosing(WindowEvent e)
      {
         System.exit(0);
      }
   }
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         lb.setText("謝謝惠顧。");
      }
   }
}