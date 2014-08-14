import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;

public class Sample2 extends JFrame
{
   private JLabel lb1, lb2;
   private JTextField tf1, tf2;

   public static void main(String[] args)
   {
      Sample2 sm = new Sample2();
   }
   public Sample2()
   {
      // 設定標題
      super("範例");

      try{
         InetAddress ia = InetAddress.getLocalHost();

         // 建立元件
         lb1 = new JLabel("主機名稱");
         lb2 = new JLabel("IP位址");
         tf1 = new JTextField(ia.getHostName());    
         tf2 = new JTextField(ia.getHostAddress()); 

         // 設定容器
         setLayout(new GridLayout(2, 2));

         // 新增到容器中
         add(lb1);
         add(tf1);
         add(lb2);
         add(tf2);

         // 登錄傾聽者
         addWindowListener(new SampleWindowListener());

         // 設定框架
         pack();
         setVisible(true);

      }
      catch(Exception e){
         e.printStackTrace();
      }
   }

   // 傾聽者類別
   class SampleWindowListener extends WindowAdapter
   {
      public void windowClosing(WindowEvent e)
      {
         System.exit(0);
      }
   }
}