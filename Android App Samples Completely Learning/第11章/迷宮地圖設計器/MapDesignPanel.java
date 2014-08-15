package wyf;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class MapDesignPanel extends JPanel 
implements MouseListener,MouseMotionListener
{
	int row;//聲明行數
	int col;//聲明列數
	int span=32;//單位間隔
	MapDesigner md;//聲明MapDesigner的引用
	
	int[][] mapData;//聲明地圖二維數組
	
	int[][] diamondMap;//聲明圓孔二維數組
		
	public MapDesignPanel(int row,int col,MapDesigner md)
	{
		this.row=row;//給行賦值
		this.col=col;//給列賦值
		this.md=md;//獲取MapDesigner引用
		
		this.setPreferredSize//設置面板的大小
		(
			new Dimension(span*col,span*row)
		);
		
		mapData=new int[row][col];//創建地圖數組
		diamondMap=new int[row][col];//創建圓孔數組
	
		
		this.addMouseListener(this);//添加鼠標監聽
		this.addMouseMotionListener(this);//添加鼠標移動監聽
	}
	
	public void paint(Graphics g)//設置繪製方法
	{
		g.setColor(Color.BLACK);//設置畫筆顏色為黑色
		g.fillRect(0,0,span*col,span*row);//繪製填充矩形
		
		for(int i=0;i<mapData.length;i++)
		{
			for(int j=0;j<mapData[0].length;j++)
			{
				if(mapData[i][j]==0)
				{//繪製白色格子
					g.setColor(Color.white);
					g.fillRect(j*span,i*span,span,span);
				}
			}
		}
		
		for(int i=0;i<diamondMap.length;i++)
		{
			for(int j=0;j<diamondMap[0].length;j++)
			{
				if(diamondMap[i][j]==1)
				{//繪製圓孔		
					g.drawImage(md.icrystal,j*span+1,i*span+3,this);
				}
			}
		}
		g.setColor(Color.green);		
		for(int i=0;i<row+1;i++)
		{
			g.drawLine(0,span*i,span*col,span*i);
		}
		
		for(int j=0;j<col+1;j++)
		{
			g.drawLine(span*j,0,span*j,span*row);
		}
	}	

	public void mouseClicked(MouseEvent e)
	{
		if(md.jrBlack.isSelected()||md.jrWhite.isSelected())
		{//設置地圖可通過性
			int x=e.getX();
			int y=e.getY();
			
			int rowC=y/span;
			int colC=x/span;
			
			if(rowC>=row||colC>=col)
			{
				return;
			}
			if(diamondMap[rowC][colC]==1&&md.jrBlack.isSelected())			
			{
				JOptionPane.showMessageDialog
			   	(
			   		this,
			   		"圓孔區域下不能建造牆！",
			   		"擺放錯誤",
			   		JOptionPane.ERROR_MESSAGE
			   	);
			   	return;
			}
			mapData[rowC][colC]=md.jrBlack.isSelected()?1:0;
		}
		else if(md.jrCrystal.isSelected())
		{//擺放圓孔
			int x=e.getX();
			int y=e.getY();
			
			int rowC=y/span;
			int colC=x/span;
			
			if(rowC>=row||colC>=col)
			{
				return;
			}
			if(mapData[rowC][colC]==1)
			{
			   	JOptionPane.showMessageDialog
			   	(
			   		this,
			   		"不可通過處不能擺放圓孔！",
			   		"擺放錯誤",
			   		JOptionPane.ERROR_MESSAGE
			   	);
			   	return;
			}
			diamondMap[rowC][colC]=(diamondMap[rowC][colC]+1)%2;
		}
		this.repaint();
	}
	
	public void mousePressed(MouseEvent e)
	{
		
	}
	
	public void mouseReleased(MouseEvent e)
	{
		
	}
	
	public void mouseEntered(MouseEvent e)
	{
		
	}
	
	public void mouseExited(MouseEvent e)
	{
		
	}
	
	public void mouseDragged(MouseEvent e)
	{
		if(md.jrBlack.isSelected()||md.jrWhite.isSelected())
		{
			int x=e.getX();
			int y=e.getY();
			
			int rowC=y/span;
			int colC=x/span;
			
			if(rowC>=row||colC>=col)
			{
				return;
			}
			if(diamondMap[rowC][colC]==1&&md.jrBlack.isSelected())			
			{
				JOptionPane.showMessageDialog
			   	(
			   		this,
			   		"圓孔區域下不能建造牆！",
			   		"擺放錯誤",
			   		JOptionPane.ERROR_MESSAGE
			   	);
			   	return;
			}	
			mapData[rowC][colC]=md.jrBlack.isSelected()?1:0;
		}		
		this.repaint();
	}
	
	public void mouseMoved(MouseEvent e){}
	public static void main(String args[])
	{
		new MapColRowDialog();
	}
}
