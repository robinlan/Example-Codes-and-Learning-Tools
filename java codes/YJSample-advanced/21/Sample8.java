import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sample8 extends JApplet
{
   private JLabel lb;
   private JButton bt[] = new JButton[3];
   private JToolBar tl;
   private Icon ic;

   public void init()
   {
      // 建立元件
      lb = new JLabel("歡迎光臨。");
      tl = new JToolBar();
      ic = new ImageIcon(getImage(getDocumentBase(), "car.gif"));
   
      for(int i=0; i<bt.length; i++){
         bt[i] = new JButton(ic); 
      }

      // 設定元件
      for(int i=0; i<bt.length; i++){
         bt[i].setToolTipText((i+1) + "號車"); 
      }

      // 新增到工具列上
      tl.add(bt[0]);
      tl.add(bt[1]);
      tl.addSeparator();
      tl.add(bt[2]);

      // 新增到容器上
      add(tl, BorderLayout.NORTH);
      add(lb, BorderLayout.CENTER);

      // 登錄傾聽者
      for(int i=0; i<bt.length; i++){
         bt[i].addActionListener(new SampleActionListener()); 
      }
   }

   // 傾聽者類別
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         int num = 0;
         JButton tmp = (JButton) e.getSource();

         if(tmp == bt[0])
             num = 1;
         else if(tmp == bt[1])
             num = 2;
         else if(tmp == bt[2])
             num = 3;

         lb.setText("您選擇了" + num + "號車。");
      }
   }
}