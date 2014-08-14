import java.awt.*;
import javax.swing.*;

public class Sample8 extends JApplet
{
   private JLabel[] lb = new JLabel[3];
   private Icon ic;

   public void init()
   {
      // 建立元件
      for(int i=0; i<lb.length; i++){ 
         lb[i] = new JLabel("This is a Car.");
      }
      ic = new ImageIcon(getImage(getDocumentBase(), "car.gif"));

      // 設定元件
      lb[0].setIcon(ic);
      lb[1].setIcon(ic);
      lb[2].setIcon(ic);

      lb[0].setFont(new Font("SansSerif", Font.BOLD, 12));
      lb[1].setFont(new Font("Helvetica", Font.BOLD, 14));
      lb[2].setFont(new Font("Century", Font.BOLD, 16));

      // 設定容器
      setLayout(new GridLayout(3, 1)); 

      // 新增到容器中
      for(int i=0; i<lb.length; i++){ 
         add(lb[i]);
      }
   }
}