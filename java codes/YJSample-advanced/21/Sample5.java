import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample5 extends JApplet
{
   private JList lst;
   private JScrollPane sp;
   private JButton bt;
   private JPanel pn;

   public void init()
   {
      // 準備資料
      String[] str = {"汽車", "卡車", "戰車",
                      "計程車", "跑車", "迷你車",
                      "腳踏車", "三輪車", "摩托車",
                      "飛機", "直升機", "火箭"};

      // 建立元件
      lst = new JList(str);
      sp = new JScrollPane(lst);
      bt = new JButton("變更檢視");
      pn = new JPanel();

      // 新增到容器中
      pn.add(bt);
      add(sp, BorderLayout.CENTER);
      add(pn, BorderLayout.SOUTH);

      // 登錄傾聽者
      bt.addActionListener(new SampleActionListener());
  }

   // 傾聽者類別
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         Container cnt = getContentPane();
         try{
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
            SwingUtilities.updateComponentTreeUI(cnt);
         }
         catch(Exception ex){
            ex.printStackTrace();
         }
      }
   }
}