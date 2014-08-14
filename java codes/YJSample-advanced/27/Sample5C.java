import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;

public class Sample5C extends JFrame implements Runnable
{
   public static final String HOST = "localhost";
   public static final int PORT = 10000;

   private JTextField tf;
   private JTextArea ta;
   private JScrollPane sp;
   private JPanel pn;
   private JButton bt;

   private Socket sc;
   private BufferedReader br;
   private PrintWriter pw;

   public static void main(String[] args)
   {
      Sample5C sm = new Sample5C();
   }
   public Sample5C()
   {
      // 設定標題
      super("範例");

      // 建立元件
      tf = new JTextField();
      ta = new JTextArea();
      sp = new JScrollPane(ta);
      pn = new JPanel();
      bt = new JButton("傳送");
 
      // 新增到容器中
      pn.add(bt);
      add(tf, BorderLayout.NORTH);
      add(sp, BorderLayout.CENTER);
      add(pn, BorderLayout.SOUTH);

      // 登錄傾聽者
      bt.addActionListener(new SampleActionListener());
      addWindowListener(new SampleWindowListener());

      // 設定頁框
      setSize(300, 200);
      setVisible(true);

      // 連線
      Thread th = new Thread(this);
      th.start();
   }
   public void run()
   {
      try{
         sc = new Socket(HOST, PORT);
         br = new BufferedReader
            (new InputStreamReader(sc.getInputStream()));
         pw = new PrintWriter
            (new BufferedWriter
(new OutputStreamWriter
(sc.getOutputStream())));

         while(true){
            try{
               String str = br.readLine();
               ta.append(str + "\n");
            }
            catch(Exception e){
               br.close();
               pw.close();
               sc.close();
               break;
            }
         }
      }
      catch(Exception e){
         e.printStackTrace();
      }
   }

   // 傾聽者類別
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         try{
            String str = tf.getText();
            pw.println(str);
            ta.append(str + "\n");
            pw.flush();
            tf.setText("");
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