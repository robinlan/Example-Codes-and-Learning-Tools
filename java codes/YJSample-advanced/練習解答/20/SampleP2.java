import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class SampleP2 extends JApplet
{
   private JLabel lb;
   private JCheckBox cb;
   private Icon ic;

   public void init()
   {
      // 建立元件
      lb = new JLabel("這是車子。");
      cb = new JCheckBox("顯示影像");
      ic = new ImageIcon(getImage(getDocumentBase(), "car.gif"));

      // 設定元件
      lb.setBorder(new LineBorder(Color.blue, 10));

      // 新增到容器中
      add(lb, BorderLayout.CENTER);
      add(cb, BorderLayout.SOUTH);

      // 登錄傾聽者
      cb.addItemListener(new SampleItemListener());
   }
   // 傾聽者類別
   class SampleItemListener implements ItemListener
   {
      public void itemStateChanged(ItemEvent e)
      {
         if(e.getStateChange() == ItemEvent.SELECTED)
         {
            lb.setIcon(ic);
         }
         else if(e.getStateChange() == ItemEvent.DESELECTED)
         {
            lb.setIcon(null);
         }
      }
   }
}