import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample11 extends JFrame
{
   private JLabel lb;
   private JList lst;
   private JScrollPane sp;

   public static void main(String[] args)
   {
      Sample11 sm = new Sample11();
   }
   public Sample11()
   {
      // 設定標題
      super("範例");

      // 準備資料
      String str[] = {"汽車", "卡車", "戰車",
                      "計程車", "跑車", "迷你車",
                      "腳踏車", "三輪車", "摩托車",
                      "飛機", "直升機", "火箭",};

      // 建立元件
      lb = new JLabel("歡迎光臨。");
      lst = new JList(str);
      sp = new JScrollPane(lst);

      // 新增到容器中
      add(lb, BorderLayout.NORTH);
      add(sp, BorderLayout.CENTER);

      // 登錄傾聽者
      addWindowListener(new SampleWindowListener());

      // 設定框架
      setSize(200, 200);
      setVisible(true);
   }

   // 傾聽者類別
   class SampleWindowListener extends WindowAdapter
   {
      public void windowClosing(WindowEvent e)
      {
         System.exit(0);
      }
   }
}