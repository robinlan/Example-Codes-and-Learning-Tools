import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SampleP4 extends JFrame
{
   JLabel lb;
   JMenuBar mb;
   JMenu mn[] = new JMenu[4];
   JMenuItem mi[] = new JMenuItem[6];

   public static void main(String[] args)
   {
        SampleP4 sm = new SampleP4();
   }
   public SampleP4()
   {
      // 設定標題
      super("範例");

      // 建立元件
      lb = new JLabel("歡迎光臨。");
      mb = new JMenuBar();
   
      mn[0] = new JMenu("主選單1"); 
      mn[1] = new JMenu("主選單2"); 
      mn[2] = new JMenu("子選單1"); 
      mn[3] = new JMenu("子選單2"); 

      mi[0] = new JMenuItem("汽車");
      mi[1] = new JMenuItem("卡車");
      mi[2] = new JMenuItem("戰車");
      mi[3] = new JMenuItem("計程車");
      mi[4] = new JMenuItem("跑車");
      mi[5] = new JMenuItem("迷你車");

      mn[0].add(mi[0]);
      mn[0].add(mi[1]);

      mn[2].add(mi[2]);
      mn[2].add(mi[3]);

      mn[3].add(mi[4]);
      mn[3].add(mi[5]);

      mn[1].add(mn[2]);
      mn[1].addSeparator();
      mn[1].add(mn[3]);

      mb.add(mn[0]);
      mb.add(mn[1]);

      // 新增到容器中
      add(mb, BorderLayout.NORTH);
      add(lb, BorderLayout.CENTER);

      // 登錄傾聽者
      for(int i=0; i<mi.length; i++){
         mi[i].addActionListener(new SampleActionListener()); 
      }
      addWindowListener(new SampleWindowListener());

      // 設定框架
      setSize(200, 200);
      setVisible(true);
   }

   // 傾聽者類別
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         JMenuItem tmp =(JMenuItem) e.getSource();
         String str = tmp.getText();
         lb.setText("是" + str + "吧。");
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