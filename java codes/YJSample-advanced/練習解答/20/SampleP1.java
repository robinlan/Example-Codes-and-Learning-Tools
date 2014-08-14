import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SampleP1 extends JApplet
{
   private JLabel lb;
   private JPanel pn;
   private JRadioButton rb1, rb2, rb3, tmp;
   private ButtonGroup bg;

   public void init()
   {
      // 建立元件
      lb  = new JLabel("歡迎光臨。");
      pn = new JPanel();
      rb1 = new JRadioButton("黃", true);
      rb2 = new JRadioButton("紅", false);
      rb3 = new JRadioButton("藍", false);
      bg  = new ButtonGroup();

      // 設定元件
      lb.setOpaque(true);
      lb.setBackground(Color.yellow);

      // 新增到按鈕群組中
      bg.add(rb1);
      bg.add(rb2);
      bg.add(rb3);

      // 新增到容器中
      pn.add(rb1);
      pn.add(rb2);
      pn.add(rb3);
      add(lb, BorderLayout.NORTH);
      add(pn, BorderLayout.SOUTH);

      // 登錄傾聽者
      rb1.addActionListener(new SampleActionListener());
      rb2.addActionListener(new SampleActionListener());
      rb3.addActionListener(new SampleActionListener());
   }

   // 傾聽者類別
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
          tmp = (JRadioButton) e.getSource(); 
          if(tmp == rb1)
             lb.setBackground(Color.yellow);
          else if(tmp == rb2)
             lb.setBackground(Color.red);
          else if(tmp == rb3)
             lb.setBackground(Color.blue);
      }
   }
}