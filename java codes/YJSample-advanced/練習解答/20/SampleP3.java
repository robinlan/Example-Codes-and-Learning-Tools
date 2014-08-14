import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SampleP3 extends JApplet
{
   private JLabel lb;
   private JPanel pn;
   private JRadioButton rb1, rb2, rb3, tmp;
   private ButtonGroup bg;

   public void init()
   {
      // 建立元件
      lb = new JLabel("Hello!");
      pn = new JPanel();
      rb1 = new JRadioButton("普通", true);
      rb2 = new JRadioButton("粗體", false);
      rb3 = new JRadioButton("斜體", false);
      bg  = new ButtonGroup();

      // 設定元件
      lb.setFont(new Font("Serif", Font.PLAIN, 20));
      lb.setHorizontalAlignment(JLabel.CENTER);

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
             lb.setFont(new Font("Serif", Font.PLAIN, 20));
          else if(tmp == rb2)
             lb.setFont(new Font("Serif", Font.BOLD, 20));
          else if(tmp == rb3)
             lb.setFont(new Font("Serif", Font.ITALIC, 20));
      }
   }
}