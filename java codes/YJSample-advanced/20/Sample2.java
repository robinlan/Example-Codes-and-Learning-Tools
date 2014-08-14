import java.awt.*;
import javax.swing.*;

public class Sample2 extends JApplet
{
   private JButton[] bt = new JButton[5];

   public void init()
   {
      // 建立元件
      bt[0] = new JButton("N");
      bt[1] = new JButton("S");
      bt[2] = new JButton("C");
      bt[3] = new JButton("W");
      bt[4] = new JButton("E");

      // 設定容器
      setLayout(new BorderLayout());

      // 新增到容器中
      add(bt[0], BorderLayout.NORTH);
      add(bt[1], BorderLayout.SOUTH);
      add(bt[2], BorderLayout.CENTER);
      add(bt[3], BorderLayout.WEST);
      add(bt[4], BorderLayout.EAST);
   }
}