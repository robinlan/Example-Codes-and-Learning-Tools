import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample5 extends JFrame
{
   private JLabel lb;
   private JPanel pn1, pn2;
   private JTextField[] tf = new JTextField[5];
   private JButton bt1, bt2;

   public static void main(String[] args)
   {
      Sample5 sm = new Sample5();
   }
   public Sample5()
   {
      // 設定標題
      super("範例");

      // 建立元件
      lb = new JLabel("請輸入整數。");
      pn1 = new JPanel();
      pn2 = new JPanel();

      for(int i=0; i<tf.length; i++){
         String num = (new Integer(i)).toString();
         tf[i] = new JTextField(num, 5);
      }

      bt1 = new JButton("讀取");
      bt2 = new JButton("儲存");

      // 新增到容器中
      for(int i=0; i<tf.length; i++){
         pn1.add(tf[i]);
      } 

      pn2.add(bt1);
      pn2.add(bt2);

      add(lb, BorderLayout.NORTH);
      add(pn1, BorderLayout.CENTER);
      add(pn2, BorderLayout.SOUTH);

      // 登錄傾聽者
      bt1.addActionListener(new SampleActionListener());
      bt2.addActionListener(new SampleActionListener());
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
            if(e.getSource() == bt1){
               int res = fc.showOpenDialog(cnt);
               if(res == JFileChooser.APPROVE_OPTION){       
                  File fl = fc.getSelectedFile();
                  BufferedInputStream bis =
                  new BufferedInputStream(new FileInputStream(fl));
                  for(int i=0; i<tf.length; i++){
                     int num = bis.read();
                     tf[i].setText((new Integer(num)).toString());
                  }
                  bis.close();
               }
            }
            else if(e.getSource() == bt2){
               int res = fc.showSaveDialog(cnt);
               if(res == JFileChooser.APPROVE_OPTION){       
                  File fl = fc.getSelectedFile();
                  BufferedOutputStream bos =
                  new BufferedOutputStream(new FileOutputStream(fl));
                  for(int i=0; i<tf.length; i++){
                     int num =Integer.parseInt(tf[i].getText());
                     bos.write(num);
                  }
                  bos.close();
               }
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