import java.awt.*;
import javax.swing.*;

public class Sample5 extends JApplet
{
   private JButton[] bt = new JButton[10];
   private JPanel[]  pn = new JPanel[2];

   public void init()
   {
      // 建立元件
      for(int i=0; i<bt.length; i++){
         bt[i] = new JButton(Integer.toString(i));
      }
      for(int i=0; i<pn.length; i++){
         pn[i] = new JPanel();
      }

      // 設定容器
      setLayout(new GridLayout(2, 1));
      pn[0].setLayout(new GridLayout(2, 2));
      pn[1].setLayout(new GridLayout(2, 3));

      // 新增到容器中
      for(int i=0; i<4; i++){
         pn[0].add(bt[i]);
      }
      for(int i=4; i<bt.length; i++){
         pn[1].add(bt[i]);
      }
      for(int i=0; i<pn.length; i++){
         add(pn[i]);
      }
   }
}