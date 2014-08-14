import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample3 extends JFrame
{
   private JPanel pn1, pn2;
   private JLabel lb1, lb2, lb3, lb4;
   private JButton bt;

   public static void main(String[] args)
   {
      Sample3 sm = new Sample3();
   }
   public Sample3()
   {
      // 設定標題
      super("範例");

      // 建立元件
      lb1 = new JLabel("請選擇檔案。");
      lb2 = new JLabel();
      lb3 = new JLabel();
      lb4 = new JLabel();

      pn1 = new JPanel();
      pn2 = new JPanel();
      bt = new JButton("選擇");

      // 設定容器
      pn1.setLayout(new GridLayout(3,1));

      // 新增到容器中
      pn1.add(lb2);
      pn1.add(lb3);
      pn1.add(lb4);
      pn2.add(bt);

      add(lb1, BorderLayout.NORTH);
      add(pn1, BorderLayout.CENTER);
      add(pn2, BorderLayout.SOUTH);

      // 登錄傾聽者
      bt.addActionListener(new SampleActionListener());
      addWindowListener(new SampleWindowListener());

      // 設定框架
      setSize(300, 300);
      setVisible(true);
  }

   // 傾聽者類別
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         Container cnt = getContentPane();

         JFileChooser fc = new JFileChooser();
         int res = fc.showOpenDialog(cnt);
       
         if(res == JFileChooser.APPROVE_OPTION){
            File fl = fc.getSelectedFile();
            lb2.setText("檔名是" + fl.getName() + "。");
            lb3.setText("絕對路徑是" + fl.getAbsolutePath() + "。");
            lb4.setText("大小是" + fl.length() + "。");
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