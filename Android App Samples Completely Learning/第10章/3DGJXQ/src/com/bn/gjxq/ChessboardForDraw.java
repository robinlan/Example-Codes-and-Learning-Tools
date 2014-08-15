package com.bn.gjxq;
import static com.bn.gjxq.Constant.*;

import javax.microedition.khronos.opengles.GL10;
//繪製棋盤類
public class ChessboardForDraw
{
	//顏色矩形數組
    ColorRect[] cr;
	public ChessboardForDraw()//構造器
	{
		ColorRect cr1=new ColorRect(COLORARR[0]);//白色矩形
		ColorRect cr2=new ColorRect(COLORARR[1]);//黑色矩形
		ColorRect cr3=new ColorRect(COLORARR[2]);//紅色矩形
		//創建三個對像
		cr=new ColorRect[]
		        {
				 cr1,
				 cr2,
				 cr3
		        };
	}
	public void drawself(GL10 gl)
	{
		//colorRect移動
		for(int j=-4;j<4;j++)//循環繪製棋盤
		{
			for(int i=-4;i<4;i++)
			{ 
				if(MySurfaceView.road[j+4][i+4]!=1)//如果當前沒有光標
				{
			      gl.glPushMatrix();
	              gl.glTranslatef(i*UNIT_SIZE, 0, j*UNIT_SIZE);//將顏色矩形移動到指定位置
	              cr[Math.abs((i+j)%2)].drawSelf(gl);//繪製棋盤
	              gl.glPopMatrix();
				}
				else//如果當前有光標,棋盤顏色變紅
				{
					 gl.glPushMatrix();
		             gl.glTranslatef(i*UNIT_SIZE, 0, j*UNIT_SIZE);//將顏色矩形移動到指定位置
		             cr[2].drawSelf(gl);//繪製路徑
		             gl.glPopMatrix();
				}
			}
		}
	}
}
