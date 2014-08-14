import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample1 extends JFrame
{
   private JTextField tf;
   private JEditorPane ep;
   private JScrollPane sp;
   private JPanel pn;
   private JButton bt;

   public static void main(String[] args)
   {
      Sample1 sm = new Sample1();
   }
   public Sample1()
   {
      // 設定標題
      super("範例");

      // 建立元件
      tf = new JTextField();
      ep = new JEditorPane();
      sp = new JScrollPane(ep);
      pn = new JPanel();
      bt = new JButton("讀入");

      // 新增到容器中
      pn.add(bt);

      add(tf, BorderLayout.NORTH);
      add(sp, BorderLayout.CENTER);
      add(pn, BorderLayout.SOUTH);

      // 登錄傾聽者
      bt.addActionListener(new SampleActionListener());
      addWindowListener(new SampleWindowListener());

      // 設定框架
      setSize(300, 300);
      setVisible(true);

      tf.requestFocus();
  }

   // 傾聽者類別
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         try{
            URL url = new URL(tf.getText());
            ep.setPage(url);
         }
         catch(Exception ex){
            ex.printStackTrace();
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