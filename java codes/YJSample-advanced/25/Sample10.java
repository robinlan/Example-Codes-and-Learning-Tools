import java.io.*;
import java.util.regex.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class Sample10 extends JFrame
{
   private JPanel pn;
   private JLabel lb;
   private JTextArea ta;
   private JTextField tf;
   private JButton bt;
   private JScrollPane sp;

   public static void main(String[] args)
   {
      Sample10 sm = new Sample10();
   }
   public Sample10()
   {
      // 設定標題
      super("範例");

      // 建立元件
      lb = new JLabel("請輸入文字。");
      ta = new JTextArea();
      sp = new JScrollPane(ta);

      pn = new JPanel(new GridLayout(1,2));
      bt = new JButton("搜尋");
      tf = new JTextField();

      // 新增到容器中
      pn.add(tf);
      pn.add(bt);

      add(lb, BorderLayout.NORTH);
      add(sp, BorderLayout.CENTER);
      add(pn, BorderLayout.SOUTH);

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
         try{
            if(e.getSource() == bt){
               Highlighter hl = ta.getHighlighter();            
               hl.removeAllHighlights();
               Pattern pn = Pattern.compile(tf.getText());
               Matcher mt = pn.matcher(ta.getText());

               while(mt.find()){
                  hl.addHighlight(mt.start(), mt.end(), new DefaultHighlighter.DefaultHighlightPainter(null));
               }
            }
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