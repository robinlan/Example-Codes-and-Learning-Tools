import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample4 extends JFrame
{
   private JPanel pn;
   private JLabel lb;
   private JTextArea ta;
   private JScrollPane sp;
   private JButton bt1, bt2;

   public static void main(String[] args)
   {
      Sample4 sm = new Sample4();
   }
   public Sample4()
   {
      // 設定標題
      super("範例");

      // 建立元件
      lb = new JLabel("請選擇檔案。");
      ta = new JTextArea();
      sp = new JScrollPane(ta);

      pn = new JPanel();
      bt1 = new JButton("讀取");
      bt2 = new JButton("儲存");

      // 新增到容器中
      pn.add(bt1);
      pn.add(bt2);

      add(lb, BorderLayout.NORTH);
      add(sp, BorderLayout.CENTER);
      add(pn, BorderLayout.SOUTH);

      // 登錄傾聽者
      bt1.addActionListener(new SampleActionListener());
      bt2.addActionListener(new SampleActionListener());
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
         try{
            if(e.getSource() == bt1){
               int res = fc.showOpenDialog(cnt);
               if(res == JFileChooser.APPROVE_OPTION){       
                  File fl = fc.getSelectedFile();
                  BufferedReader br = new BufferedReader(new FileReader(fl));
                  ta.read(br, null);
                  br.close();
               }
            }
            else if(e.getSource() == bt2){
               int res = fc.showSaveDialog(cnt);
               if(res == JFileChooser.APPROVE_OPTION){       
                  File fl = fc.getSelectedFile();
                  BufferedWriter bw = new BufferedWriter(new FileWriter(fl));
                  ta.write(bw);
                  bw.close();
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