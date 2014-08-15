package com.bn.wlqp;
import static com.bn.wlqp.Constant.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class CardForControl 
{//控制牌
	Bitmap bitmapTmp;//牌的Bitmap
	int xOffset;     //牌的X轴偏移量
	boolean flag=false;  //是否出牌的标志
	int num;//0-53   牌号

	public CardForControl(Bitmap bitmapTmp,int xOffset,	int num)
	{//构造器
		this.bitmapTmp=bitmapTmp;
		this.xOffset=xOffset;
		this.num=num;		
	}
	
	
	public void drawSelf(Canvas canvas)
	{   /*绘制一张图片，flag为false时绘制平常的不发生触摸事件的，
		发生触摸事件之后flag变为true并且在绘制的时候使牌向上移动MOVE_YOFFSE距离*/
		if(!flag)
		{
			canvas.drawBitmap(bitmapTmp, xOffset, DOWN_Y, null);
		}
		else
		{
			canvas.drawBitmap(bitmapTmp, xOffset, DOWN_Y-MOVE_YOFFSET, null);
		}
	}
	
	public boolean isIn(int x,int y)
	{//判定要出的是哪张牌
		boolean result=false;
		int yUp=(flag)?DOWN_Y-MOVE_YOFFSET:DOWN_Y;

		
		if(x>xOffset&&x<xOffset+CARD_WIDTH
		   && y>yUp&&y<yUp+CARD_HEIGHT)
		{//判断点击处的坐标在哪张牌的范围内 并且设定标志位 并且设定flag的boolean值
			flag=!flag;
			result=true;
		}		
		return result;
	}
}