import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

public class Sample extends JFrame
{
   private JMenuBar mb;
   private JMenu[] mn = new JMenu[3];
   private JMenuItem[] mi = new JMenuItem[6];
   private SamplePanel sp;

   private ArrayList<Shape> shapeList;    // 圖形列表
   private int currentShape;    // 選擇圖形
   private Color currentColor;  // 選擇顏色
  
   public static void main(String args[])
   {
      Sample sm = new Sample();
   }
   public Sample()
   {
      // 設定標題
      super("範例");

      // 建立元件
      mb = new JMenuBar();
   
      mn[0] = new JMenu("檔案"); 
      mn[1] = new JMenu("設定"); 
      mn[2] = new JMenu("圖形"); 

      mi[0] = new JMenuItem("開啟舊檔");
      mi[1] = new JMenuItem("儲存檔案");
      mi[2] = new JRadioButtonMenuItem("四角形");
      mi[3] = new JRadioButtonMenuItem("橢圓形");
      mi[4] = new JRadioButtonMenuItem("直線");
      mi[5] = new JMenuItem("顏色");

      sp = new SamplePanel();

      // 新增到容器中
      mn[0].add(mi[0]);
      mn[0].add(mi[1]);

      mn[2].add(mi[2]);
      mn[2].add(mi[3]);
      mn[2].add(mi[4]);

      mn[1].add(mn[2]);
      mn[1].add(mi[5]);

      mb.add(mn[0]);
      mb.add(mn[1]);

      ButtonGroup bg = new ButtonGroup();
      bg.add(mi[2]);
      bg.add(mi[3]);
      bg.add(mi[4]);

      add(sp, BorderLayout.CENTER);
      add(mb, BorderLayout.NORTH);

      // 登錄傾聽者
      for(int i=0; i<mi.length; i++)
      {
         mi[i].addActionListener(new SampleActionListener()); 
      }
      addWindowListener(new SampleWindowListener());

      // 進行初始化
      shapeList = new ArrayList<Shape>();
      currentShape = Shape.RECT;
      currentColor = Color.blue;
      mi[2].setSelected(true);

      // 設定框架
      setSize(600, 400);
      setVisible(true);
   }

   // 傾聽者類別
   class SampleWindowListener extends WindowAdapter
   {
      public void windowClosing(WindowEvent e)
      {
         System.exit(0);
      }
   }      
   class SampleActionListener implements ActionListener
   {
      public void actionPerformed(ActionEvent e)
      {
         Container cnt = getContentPane();
         JFileChooser fc = new JFileChooser();
         fc.setFileFilter(new MyFileFilter());
         try{
            // 讀入檔案
            if(e.getSource() == mi[0]){
               int res = fc.showOpenDialog(cnt);
               if(res == JFileChooser.APPROVE_OPTION){       
                  File fl = fc.getSelectedFile();
                  ObjectInputStream oi = new ObjectInputStream(new FileInputStream(fl));
                  Shape tmp = null;
                  shapeList.clear();
                  try{
                     while((tmp = (Shape)oi.readObject()) != null){
                        shapeList.add(tmp);
                     }
                  }catch(EOFException ex){}                  
                  oi.close();
               }
            }
            // 輸出檔案
            else if(e.getSource() == mi[1]){
               int res = fc.showSaveDialog(cnt);
               if(res == JFileChooser.APPROVE_OPTION){       
                  File fl = fc.getSelectedFile();
                  ObjectOutputStream oo = new ObjectOutputStream(new FileOutputStream(fl));
                  for(int i=0; i < shapeList.size(); i++){ 
                    oo.writeObject(shapeList.get(i));
                  }
                 oo.close();
               }
            }
            // 設定四角形
            else if(e.getSource() == mi[2]){
               currentShape = Shape.RECT;
            }
            // 設定橢圓形
            else if(e.getSource() == mi[3]){
               currentShape = Shape.OVAL;
            }
            // 設定直線
            else if(e.getSource() == mi[4]){
               currentShape = Shape.LINE;
            }
            // 顯示顏色的選擇畫面
            else if(e.getSource() == mi[5]){
               currentColor = JColorChooser.showDialog(cnt, "選擇顏色", Color.black);
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
            if(fn.toLowerCase().endsWith(".g")){
               return true;
            }
            return false;
          }
          public String getDescription()
          {
            return "圖像檔";
          }
      }
   }
      
   // 面板類別
   class SamplePanel extends JPanel
   {
      SamplePanel()
      {
          setOpaque(false);
          SampleMouseListener ml= new SampleMouseListener();
          addMouseListener(ml);
      }
      public void update(Graphics g)
      {
      }
      public void paint(Graphics g)
      {
         for(int i=0; i < shapeList.size(); i++){
            // 從列表中取出圖形物件
            Shape  sh = (Shape) shapeList.get(i);
            // 以圖形物件自己來進行描繪
            sh.draw(g);
         }
      }
      // 傾聽者類別
      class SampleMouseListener extends MouseAdapter
      {
         public void mousePressed(MouseEvent e)
         {
            // 建立圖形物件
            Shape sh = null;
            if(currentShape == Shape.RECT){
                sh = new Rect();
            }
            else if(currentShape == Shape.OVAL){
                sh = new Oval();
            }
            else if(currentShape == Shape.LINE){
                sh = new Line();
            }
            // 設定圖形物件的顏色
            sh.setColor(currentColor);
            // 設定圖形物件的座標
            sh.setStartPoint(e.getX(), e.getY());
            sh.setEndPoint(e.getX(), e.getY());
            // 將圖形物件新增到列表的尾端
            shapeList.add(sh);
            // 重新描繪面板
            repaint();
         }
         public void mouseReleased(MouseEvent e)
         {
            // 從列表的尾端取出圖形物件
            Shape sh = (Shape)shapeList.get(shapeList.size()-1);
            // 設定圖形物件的結束座標
            sh.setEndPoint(e.getX(), e.getY());
            // 重新描繪面板
            repaint();
         }
      }
   }
}