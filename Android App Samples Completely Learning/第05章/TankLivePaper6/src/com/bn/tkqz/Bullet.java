package com.bn.tkqz;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Bullet {
	int direction;//  0-up 1-right 2-down 3-left
	int x;
	int y;
	int span=Constant.TANK_BULLET_SPAN;	
	Bitmap bitmap;
	public Bullet(Bitmap bitmap,int direction,int x,int y)
	{
		this.bitmap=bitmap;
		this.direction=direction;
		this.x=x;
		this.y=y;
	}
	boolean canGo(int xTemp,int yTemp)//判断能不能移动到新的位置
	{
		  boolean canGoFlag=true;
		  if(!Constant.isBulletInGameView(xTemp, yTemp))
		  {
			  canGoFlag=false;
		  }
		  Hero hero=AliveWallPaperTank.hero;
			//判断子弹是否击中英雄
			if
			(
				Constant.oneIsInAnother
				(
						x, y,Constant.BULLET_SIZE, Constant.BULLET_SIZE,
						hero.x, hero.y, Constant.TANK_SIZE, Constant.TANK_SIZE
				)
			)
			{
				if(!hero.isProtected())
				{
					hero.explode();
				}				
				canGoFlag=false;
			}
			//检测坦克是否遇到障碍物
			if(AliveWallPaperTank.map.isBulletMetWithBarrier(xTemp,yTemp))
			{
				canGoFlag=false;
			}
			return canGoFlag;
	}
	void go()
	{
		int xTemp=x;
		int yTemp=y;
		
		
		//子弹行进
		switch(direction)
		{
			case Tank.DERECTION_UP:
				yTemp=y-span;
			break;
			case Tank.DERECTION_DOWN:
				yTemp=y+span;
			break;	
			case Tank.DERECTION_RIGHT:
				xTemp=x+span;
			break;
			case Tank.DERECTION_LEFT:
				xTemp=x-span;
			break;											
		}
		if(canGo(xTemp,yTemp))//判断能不能移动到新的位置
		{
			x=xTemp;
			y=yTemp;
			//子弹走到新位置后，判断是否击中老窝
			if(AliveWallPaperTank.map.isBulletMetWithHome(x, y))
			{
				this.explode();
				AliveWallPaperTank.map.home.explode();
			}
		}
		else
		{
			explode();
		}
	}
	void drawSelf(Canvas canvas, Paint paint) 
	{
		canvas.drawBitmap(bitmap, x,y, paint);
	}
	void explode() 
	{
		AliveWallPaperTank.alBullet.remove(this);
	}
}
