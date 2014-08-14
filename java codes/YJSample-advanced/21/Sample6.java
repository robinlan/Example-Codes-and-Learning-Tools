import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

public class Sample6 extends JApplet
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

      // 設定元件
      lst.setCellRenderer(new SampleCellRenderer());

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
         lb.setText("您選擇了" + str + "。");
      }
   }

   // 儲存格描繪器
   class SampleCellRenderer extends JLabel implements ListCellRenderer
   {
      public SampleCellRenderer()
      {
          setOpaque(true);
      }
      public Component getListCellRendererComponent(JList list, Object value, int index,
                                                 boolean isSelected, boolean cellHasFocus)
      {
         setText(value.toString());
         setIcon(new ImageIcon(getImage(getDocumentBase(), "br.gif")));
 
         if(isSelected){
            setBackground(Color.yellow);
         }
         else{
            setBackground(list.getBackground());
         }
         return this;
      }
   }
}