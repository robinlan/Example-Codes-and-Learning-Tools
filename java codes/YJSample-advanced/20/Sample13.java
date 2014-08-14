import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample13 extends JApplet
{
   private JLabel lb;
   private JTextField tf;

   public void init()
   {
      // 建立元件
      lb = new JLabel("請選擇。");
      tf = new JTextField();

      // 新增到容器中
      add(lb, BorderLayout.NORTH);
      add(tf, BorderLayout.SOUTH);

      // 登錄傾聽者
      tf.addActionListener(new SampleActionListener());
  }

   // 傾聽者類別
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         JTextField tmp = (JTextField) e.getSource(); 
         lb.setText("要選擇" + tmp.getText() + "對吧！");
      }
   }
}