import java.util.*;
import java.text.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class SampleP2 extends JApplet
{
   private JLabel lb;
   private JList lst;
   private JScrollPane sp;

   public void init()
   {
      // 建立元件
      lb = new JLabel("顯示日期。");
      lst = new JList(new MyListModel());
      sp = new JScrollPane(lst);

      // 新增到容器中
      add(lb, BorderLayout.NORTH);
      add(sp, BorderLayout.SOUTH);

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
         lb.setText("是" + str + "吧。");
      }
   }

   // 模型類別
   class MyListModel extends AbstractListModel
   {
       DateFormat df;

       public MyListModel()
       {
           df = new SimpleDateFormat("yyyy/MM/dd");
       }
       public int getSize()
       {
          return 50;
       }
       public Object getElementAt(int index)
       {
           Calendar cl = Calendar.getInstance(); 
           cl.setTime(new Date());
           cl.add(Calendar.DATE, index);

           String str = df.format(cl.getTime());
           return str;
       }
   }
}