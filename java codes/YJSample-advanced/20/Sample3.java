import java.awt.*;
import javax.swing.*;

public class Sample3 extends JApplet
{
   private JButton[] bt = new JButton[5];

   public void init()
   {
      // 建立元件
      for(int i=0; i<bt.length; i++){
         bt[i] = new JButton(Integer.toString(i));
      }

      // 設定容器
      setLayout(new FlowLayout());

      // 新增到容器中
      for(int i=0; i<bt.length; i++){
         add(bt[i]);
      }
   }
}