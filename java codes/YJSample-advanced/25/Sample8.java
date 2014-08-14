import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample8 extends JFrame
{
   private JTextArea[] ta; 
   private JScrollPane[] sp; 
   private JPanel pn;
   private JDesktopPane dp;
   private JInternalFrame[] itf;

   public static void main(String[] args)
   {
      Sample8 sm = new Sample8();
   }
   public Sample8()
   {
      // 設定標題
      super("範例");

      // 建立元件
      dp = new JDesktopPane();

      File fl = new File(".");
      File[] fls = fl.listFiles(new MyFileFilter());

      try{
         ta = new JTextArea[fls.length];
         sp = new JScrollPane[fls.length];
         itf = new JInternalFrame[fls.length];

         for(int i=0; i<fls.length; i++){
            ta[i] = new JTextArea();
            sp[i] = new JScrollPane(ta[i]);
            itf[i] = new JInternalFrame(fls[i].getName(), true, true, true, true);
            BufferedReader br = new BufferedReader(new FileReader(fls[i]));
            ta[i].read(br, null);
            br.close();
         }

         // 新增到容器中
         for(int i=0; i<fls.length; i++){
            itf[i].add(sp[i]);
            dp.add(itf[i]);
            itf[i].setLocation(i*10,i*10);
            itf[i].setSize(300,200);
            itf[i].setVisible(true);
         }         
         add(dp, BorderLayout.CENTER);

         // 登錄傾聽者
         addWindowListener(new SampleWindowListener());

         // 設定框架
         setSize(500, 400);
         setVisible(true);
      }  
      catch(Exception e){
         e.printStackTrace();
      }
   }
   // 過濾器類別
   class MyFileFilter implements FilenameFilter
   {
      public boolean accept(File f, String n)
      {
         if(n.toLowerCase().endsWith(".java")){
            return true;
         }
         return false;
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
