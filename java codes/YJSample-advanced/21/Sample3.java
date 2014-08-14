import java.awt.*;
import javax.swing.*;

public class Sample3 extends JApplet
{
   private JLabel lb;
   private JTable tb;
   private JScrollPane sp;

   public void init()
   {
      // 準備資料
      String[] colname = {"名稱", "價格", "日期"};
      String[][] data = {
         {"汽車", "1200元","10-01"},
         {"卡車", "2400元","10-05"},
         {"戰車", "3600元","10-06"},
         {"計程車", "2500元","10-10"},
         {"跑車", "2600元","10-11"},
         {"迷你車", "300元","10-12"},
         {"腳踏車", "800元","10-15"},
         {"三輪車", "600元","10-18"},
         {"飛機", "15000元","10-19"},
         {"直升機", "3500元","10-21"},
         {"火箭", "32800元","10-22"}
      };

      // 建立元件
      lb = new JLabel("歡迎光臨。");
      tb = new JTable(data, colname);
      sp = new JScrollPane(tb);

      // 新增到容器中
      add(lb, BorderLayout.NORTH);
      add(sp, BorderLayout.CENTER);
    }
}