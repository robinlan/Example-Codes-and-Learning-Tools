import java.awt.*;
import javax.swing.*;

public class Sample7 extends JApplet
{
   private JLabel[] lb = new JLabel[3];
   private Icon ic;

   public void init()
   {
      // 建立元件
      for(int i=0; i<lb.length; i++){ 
         lb[i] = new JLabel("您覺得汽車" + i + "怎麼樣呢？");
      }
      ic = new ImageIcon(getImage(getDocumentBase(), "car.gif"));

      // 設定元件
      lb[0].setIcon(ic);
      lb[1].setIcon(ic);
      lb[2].setIcon(ic);

      lb[0].setHorizontalTextPosition(JLabel.LEFT);
      lb[1].setHorizontalTextPosition(JLabel.CENTER);
      lb[2].setHorizontalTextPosition(JLabel.RIGHT);

      lb[0].setVerticalTextPosition(JLabel.TOP);
      lb[1].setVerticalTextPosition(JLabel.CENTER);
      lb[2].setVerticalTextPosition(JLabel.BOTTOM);

      // 設定容器
      setLayout(new GridLayout(3, 1)); 

      // 新增到容器中
      for(int i=0; i<lb.length; i++){ 
         add(lb[i]);
      }
   }
}