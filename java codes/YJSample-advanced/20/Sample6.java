import java.awt.*;
import javax.swing.*;

public class Sample6 extends JApplet
{
   private JLabel[] lb = new JLabel[3];

   public void init()
   {
      // 建立元件
      for(int i=0; i<lb.length; i++){ 
         lb[i] = new JLabel("您覺得汽車" + i + "怎麼樣呢？");
      }

      // 設定元件
      lb[0].setForeground(Color.black);
      lb[1].setForeground(Color.black);
      lb[2].setForeground(Color.black);

      lb[0].setBackground(Color.white);
      lb[1].setBackground(Color.gray);
      lb[2].setBackground(Color.white);

      lb[0].setOpaque(true);
      lb[1].setOpaque(true);
      lb[2].setOpaque(true);

      lb[0].setHorizontalAlignment(JLabel.LEFT);
      lb[1].setHorizontalAlignment(JLabel.CENTER);
      lb[2].setHorizontalAlignment(JLabel.RIGHT);

      lb[0].setVerticalAlignment(JLabel.TOP);
      lb[1].setVerticalAlignment(JLabel.CENTER);
      lb[2].setVerticalAlignment(JLabel.BOTTOM);

      // 設定容器
      setLayout(new GridLayout(3, 1, 3, 3)); 

      // 在容器中新增元件
      for(int i=0; i<lb.length; i++){ 
         add(lb[i]);
      }
   }
}