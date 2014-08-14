import java.io.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class Sample9 extends JFrame
{
   private JPanel pn;
   private JLabel lb1, lb2, lb3;
   private JTextArea ta;
   private JTextField tf1, tf2;
   private JButton bt;
   private JScrollPane sp;

   public static void main(String[] args)
   {
      Sample9 sm = new Sample9();
   }
   public Sample9()
   {
      // 設定標題
      super("範例");

      // 建立元件
      lb1 = new JLabel("請輸入文字。");
      lb2 = new JLabel("取代前");
      lb3 = new JLabel("取代後");
      ta = new JTextArea();
      sp = new JScrollPane(ta);

      pn = new JPanel();
      bt = new JButton("取代");
      tf1 = new JTextField();
      tf2 = new JTextField();

      // 設定容器
      pn.setLayout(new GridLayout(1,5));

      // 新增到容器中
      pn.add(lb2);
      pn.add(tf1);
      pn.add(lb3);
      pn.add(tf2);
      pn.add(bt);

      add(lb1, BorderLayout.NORTH);
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
         if(e.getSource() == bt){
            Pattern pn = Pattern.compile(tf1.getText());
            Matcher mt = pn.matcher(ta.getText());
            ta.setText(mt.replaceAll(tf2.getText()));
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
