import javax.swing.*;

public class Sample2 extends JApplet
{
   private JButton bt; 

   public void init()
   {
      // 建立元件
      bt = new JButton("購買");

      // 新增到容器中
      add(bt);
   }
}

