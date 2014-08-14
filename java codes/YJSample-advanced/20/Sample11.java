import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample11 extends JApplet
{
   private JLabel lb;
   private JPanel pn;
   private JCheckBox ch1, ch2, tmp;

   public void init()
   {
      // 建立元件
      lb = new JLabel("歡迎光臨。");
      pn = new JPanel();
      ch1 = new JCheckBox("汽車");
      ch2 = new JCheckBox("卡車");

      // 新增到容器中
      pn.add(ch1);
      pn.add(ch2);
      add(lb, BorderLayout.NORTH);
      add(pn, BorderLayout.SOUTH);

      // 登錄傾聽者
      ch1.addItemListener(new SampleItemListener());
      ch2.addItemListener(new SampleItemListener());
   }

   // 傾聽者類別
   class SampleItemListener implements ItemListener
   {
      public void itemStateChanged(ItemEvent e)
      {
         if(e.getStateChange() == ItemEvent.SELECTED){
            tmp = (JCheckBox) e.getSource(); 
            lb.setText("選擇了" + tmp.getText() + "。");
         }
         else if(e.getStateChange() == ItemEvent.DESELECTED){
            tmp = (JCheckBox) e.getSource(); 
            lb.setText("放棄了" + tmp.getText() + "。");
         }
      }
   }
}
