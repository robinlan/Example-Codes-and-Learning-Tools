import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample6 extends JFrame
{
   private JLabel lb1, lb2;
   private JPanel pn1, pn2;
   private JTextField tf1, tf2;
   private JButton bt;

   public static void main(String[] args)
   {
      Sample6 sm = new Sample6();
   }
   public Sample6()
   {
      // 設定標題
      super("範例");

      // 建立元件
      lb1 = new JLabel("請問要讀入第幾筆資料呢？(1~5)");
      lb2 = new JLabel("資料:");

      tf1 = new JTextField("1");
      tf2 = new JTextField();

      pn1 = new JPanel();
      pn2 = new JPanel();

      bt = new JButton("讀取、更新");

      // 設定容器
      pn1.setLayout(new GridLayout(2, 2));

      // 新增到容器中
      pn1.add(lb1);
      pn1.add(tf1);
      pn1.add(lb2);
      pn1.add(tf2);
      pn2.add(bt);
      add(pn1, BorderLayout.CENTER);
      add(pn2, BorderLayout.SOUTH);

      // 登錄傾聽者
      bt.addActionListener(new SampleActionListener());
      addWindowListener(new SampleWindowListener());

      // 設定框架
      pack();
      setVisible(true);
  }

   // 傾聽者類別
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         Container cnt = getContentPane();
         JFileChooser fc = new JFileChooser();
         fc.setFileFilter(new MyFileFilter());
         try{
            int res = fc.showOpenDialog(cnt);
            if(res == JFileChooser.APPROVE_OPTION){       
               File fl = fc.getSelectedFile();
               RandomAccessFile raf =
                 new RandomAccessFile(fl, "r");
               int pos =Integer.parseInt(tf1.getText());
               raf.seek(pos-1);
               int num = raf.read();
               tf2.setText((new Integer(num)).toString());
               raf.close();
            }
         }
         catch(Exception ex){
            ex.printStackTrace();
         }
      }
      // 過濾器類別
      class MyFileFilter extends javax.swing.filechooser.FileFilter
      {
         public boolean accept(File f)
         {
            if(f.isDirectory()){
               return true;
            }

            String fn = f.getName();
            if(fn.toLowerCase().endsWith(".bin")){
               return true;
            }
            return false;
         }
         public String getDescription()
         {
            return "二進位檔";
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
}

