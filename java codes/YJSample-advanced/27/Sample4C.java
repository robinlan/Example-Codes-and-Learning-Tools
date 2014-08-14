import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;

public class Sample4C extends JFrame
{
   public static final String HOST = "localhost";
   public static final int PORT = 10000;

   private JTextArea ta;
   private JPanel pn;
   private JButton bt;

   public static void main(String[] args)
   {
      Sample4C sm = new Sample4C();
   }
   public Sample4C()
   {
      // 設定標題
      super("範例");

      // 建立元件
      ta = new JTextArea();
      pn = new JPanel();
      bt = new JButton("連線");
 
      // 新增到容器中
      pn.add(bt);
      add(ta, BorderLayout.CENTER);
      add(pn, BorderLayout.SOUTH);

      // 登錄傾聽者
      bt.addActionListener(new SampleActionListener());
      addWindowListener(new SampleWindowListener());

      // 設定框架
      setSize(300, 200);
      setVisible(true);
   }

   // 傾聽者類別
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         try{
            Socket sc = new Socket(HOST, PORT);
            BufferedReader  br = new BufferedReader
                 (new InputStreamReader(sc.getInputStream()));
            String str = br.readLine();
            ta.setText(str);
            br.close();
            sc.close();
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