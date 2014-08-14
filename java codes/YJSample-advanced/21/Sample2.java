import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample2 extends JApplet
{
   private JLabel lb;
   private JComboBox cb;

   public void init()
   {
      // 準備資料
      String[] str = {"汽車", "卡車", "戰車",
                      "計程車", "跑車", "迷你車"};

      // 建立元件
      lb = new JLabel("歡迎光臨。");
      cb = new JComboBox(str);

      // 新增到容器中
      add(lb, BorderLayout.NORTH);
      add(cb, BorderLayout.SOUTH);

      // 登錄傾聽者
      cb.addActionListener(new SampleActionListener()); 
   }

   // 傾聽者類別
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         JComboBox tmp = (JComboBox) e.getSource();
         String str = (String) tmp.getSelectedItem();
         lb.setText("您選擇了" + str + "。");
      }
   }
}