import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class Sample1 extends JApplet
{
   private JLabel lb;
   private JList lst;
   private JScrollPane sp;

   public void init()
   {
      // 準備資料
      String[] str = {"汽車", "卡車", "戰車",
                      "計程車", "跑車", "迷你車",
                      "腳踏車", "三輪車", "摩托車",
                      "飛機", "直升機", "火箭"};

      // 建立元件
      lb = new JLabel("歡迎光臨。");
      lst = new JList(str);
      sp = new JScrollPane(lst);

      // 新增到容器中
      add(lb, BorderLayout.NORTH);
      add(sp, BorderLayout.CENTER);

      // 登錄傾聽者
      lst.addListSelectionListener(new SampleListSelectionListener()); 
   }

   // 傾聽者類別
   class SampleListSelectionListener implements ListSelectionListener
   {
      public void valueChanged(ListSelectionEvent e)
      {
         JList tmp = (JList) e.getSource();
         String str = (String) tmp.getSelectedValue();
         lb.setText("您選擇了" + str + "。");
      }
   }
}