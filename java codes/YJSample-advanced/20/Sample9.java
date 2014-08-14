import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class Sample9 extends JApplet
{
   private JLabel lb[] = new JLabel[3];
   Icon ic;

   public void init()
   {
      // 建立元件
      for(int i=0; i<lb.length; i++){ 
         lb[i] = new JLabel("您覺得汽車" + i + "怎麼樣呢？");
      }
      ic = new ImageIcon(getImage(getDocumentBase(), "br.gif"));

      // 設定元件
      lb[0].setBorder(new BevelBorder(BevelBorder.RAISED));
      lb[1].setBorder(new EtchedBorder());
      lb[2].setBorder(new MatteBorder(5, 5, 5, 5, ic));

      // 設定容器
      setLayout(new GridLayout(3, 1, 5, 5)); 

      // 新增到容器中
      for(int i=0; i<lb.length; i++){ 
         add(lb[i]);
      }
   }
}
