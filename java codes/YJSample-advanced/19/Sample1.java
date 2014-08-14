import javax.swing.*;

public class Sample1 extends JApplet
{
   private JLabel lb;

   public void init()
   {
      // 建立元件
      lb = new JLabel();

      // 設定元件
      lb.setText("歡迎光臨。");

      // 新增到容器中
      add(lb);
   }
}

