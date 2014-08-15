package wyf;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MapDesigner extends JFrame
implements ActionListener
{
	int row;//行數
	int col;//列數
	MapDesignPanel mdp;//聲明MapDesignPanel的引用
	JScrollPane jsp;//聲明JScrollPane的引用
	JButton jbGenerate=new JButton("生成地圖");//生成地圖按鈕
	JButton jbGenerateD=new JButton("生成圓孔");//生成圓孔按鈕
	JRadioButton jrBlack=new JRadioButton("牆",null,true);//牆單選按鈕
	JRadioButton jrWhite=new JRadioButton("非牆",null,false);//白色單選按鈕
	JRadioButton jrCrystal=new JRadioButton("圓孔",null,false);//圓孔單選按鈕
	ButtonGroup bg=new ButtonGroup();//創建ButtonGroup
	Image icrystal;//圓孔圖標
	JPanel jp=new JPanel();//創建JPanel面板
	
	public MapDesigner(int row,int col)
	{
		this.row=row;//設置行數
		this.col=col;//設置列數		
		this.setTitle("3D迷宮重力球地圖設計器");//設置標題
		icrystal=new ImageIcon("img/Diamond.png").getImage();//圓孔的標誌圖	
		mdp=new MapDesignPanel(row,col,this);//創建地圖設計器面板
		jsp=new JScrollPane(mdp);//創建JScrollPane面板
		
		this.add(jsp);//添加JScrollPane面板
		
		jp.add(jbGenerate);//添加生成地圖按鈕
		jp.add(jbGenerateD);//添加生成圓孔按鈕
		jp.add(jrBlack);bg.add(jrBlack);//向jp中添加黑色單選按鈕
		jp.add(jrWhite);bg.add(jrWhite);//向jp中添加白色單選按鈕
		jp.add(jrCrystal);bg.add(jrCrystal);//向jp中添加圓孔單選按鈕
		this.add(jp,BorderLayout.NORTH);//jp添加進窗體中
		jbGenerate.addActionListener(this);//生成地圖按鈕設置監聽
		jbGenerateD.addActionListener(this);//生成圓孔按鈕設置監聽
		this.setBounds(10,10,800,600);//設置窗口大小
		this.setVisible(true);//設置可見
		this.mdp.requestFocus(true);//MapDesignPanel獲取焦點	
	}
	
	public void actionPerformed(ActionEvent e)
	{		
	    if(e.getSource()==this.jbGenerate)//生成地圖代碼
	    {
	    	String s="public static final int[][] MAP=//0 可通過 1 不可通過\n{";
			for(int i=0;i<mdp.row;i++)
			{
				s=s+"\n\t{";
				for(int j=0;j<mdp.col;j++)
				{
					s=s+mdp.mapData[i][j]+",";
				}
				s=s.substring(0,s.length()-1)+"},";
			}
			s=s.substring(0,s.length()-1)+"\n};";
			
			new CodeFrame(s,"3D重力球遊戲地圖");			
	    }
	    else if(e.getSource()==this.jbGenerateD)
	    {//生成圓孔位置代碼
	    	String s="public static final int[][] MAP_OBJECT=//表示可遇圓孔位置的矩陣\n{";
		
			
			for(int i=0;i<mdp.row;i++)
			{
				s=s+"\n\t{";
				for(int j=0;j<mdp.col;j++)
				{
					s=s+mdp.diamondMap[i][j]+",";
				}
				s=s.substring(0,s.length()-1)+"},";//去掉最後的逗號
			}
			s=s.substring(0,s.length()-1)+"\n};";
			new CodeFrame(s,"圓孔分佈矩陣");
	    }
	}
	public static void main(String args[])
	{
		new MapColRowDialog();
	} 
}
