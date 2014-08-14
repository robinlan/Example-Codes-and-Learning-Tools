import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample12 extends JApplet
{
   private JLabel lb;
   private JPanel pn;
   private JRadioButton rb1, rb2, tmp;
   private ButtonGroup bg;

   public void init()
   {
      // 建立元件
      lb  = new JLabel("歡迎光臨。");
      pn = new JPanel();
      rb1 = new JRadioButton("汽車", true);
      rb2 = new JRadioButton("卡車", false);
      bg  = new ButtonGroup();

      // 新增到按鈕群組中
      bg.add(rb1);
      bg.add(rb2);

      // 新增到容器中
      pn.add(rb1);
      pn.add(rb2);
      add(lb, BorderLayout.NORTH);
      add(pn, BorderLayout.SOUTH);

      // 登錄傾聽者
      rb1.addActionListener(new SampleActionListener());
      rb2.addActionListener(new SampleActionListener());
   }

   // 傾聽者類別
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         tmp = (JRadioButton) e.getSource(); 
         lb.setText("您選擇了" + tmp.getText() + "。");
      }
   }
}