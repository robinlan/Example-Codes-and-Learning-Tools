import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample11 extends JFrame
{
   private JPanel pn;
   private JList ls;
   private JScrollPane sp;
   private JButton bt;

   public static void main(String[] args)
   {
      Sample11 sm = new Sample11();
   }
   public Sample11()
   {
      // 設定標題
      super("範例");

      // 建立元件
      File fl = new File(".");
      File[] fls = fl.listFiles(new MyFileFilter());
      String[] st = new String[fls.length];
      for(int i=0; i<fls.length; i++){
         st[i] = fls[i].getName();
      }

      ls = new JList(st);
      sp = new JScrollPane(ls);
      bt = new JButton("啟動");
      pn = new JPanel();

      // 新增到容器中
      pn.add(bt);

      add(sp, BorderLayout.CENTER);
      add(pn, BorderLayout.SOUTH);

      // 登錄傾聽者
      bt.addActionListener(new SampleActionListener());
      addWindowListener(new SampleWindowListener());

      // 設定框架
      setSize(350, 300);
      setVisible(true);
  }

   // 傾聽者類別
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         try{
            if(e.getSource() == bt){
               Desktop dp = Desktop.getDesktop();
                  dp.open(new File((String)ls.getSelectedValue()));
            }
         }
         catch(IOException ex){
            System.out.println("");
         }
      }
   }
   class SampleWindowListener extends WindowAdapter
   {
      public void windowClosing(WindowEvent e)
      {
         System.exit(0);
      }
   }

   // 過濾器類別
   class MyFileFilter implements FileFilter
   {
      public boolean accept(File f)
      {
         if(f.isFile()){
            return true;
         }
         return false;
      }
   }
}