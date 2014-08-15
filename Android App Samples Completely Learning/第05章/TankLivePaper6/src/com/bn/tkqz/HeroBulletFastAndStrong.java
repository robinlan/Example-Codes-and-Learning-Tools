package com.bn.tkqz;
import java.util.ArrayList;
public class HeroBulletFastAndStrong extends HeroBullet
{
	public HeroBulletFastAndStrong(int direction, int x, int y)
	{
		super(AliveWallPaperTank.heroBullet,direction, x, y);
		this.span=Constant.HERO_BULLET_SPAN_FAST;
	}
	@Override
	boolean canGo(int xTemp,int yTemp)//判断能不能移动到新的位置
	{
		  boolean canGoFlag=true;
		  //判断子弹是否飞出边界
		  if(!Constant.isBulletInGameView(xTemp, yTemp))
		  {
			  canGoFlag=false;
		  }
		  //判断子弹是否击中敌人坦克
			
			ArrayList<Tank> alTank=new ArrayList<Tank>(AliveWallPaperTank.alTank);
			for(Tank t:alTank)
			{
				if(
						Constant.oneIsInAnother
						(
								x, y,Constant.BULLET_SIZE, Constant.BULLET_SIZE,
								t.x, t.y, Constant.TANK_SIZE, Constant.TANK_SIZE
						)
				)//判断是否击中敌坦克
				{
					if(!t.lifeMinusOne())
					{
						t.explode();				
					}
					canGoFlag=false;
				}
			}
			ArrayList<Bullet> alBullet=new ArrayList<Bullet>(AliveWallPaperTank.alBullet);
			for(Bullet b:alBullet)
			{
				if(
						Constant.oneIsInAnother
						(
								x, y,Constant.BULLET_SIZE, Constant.BULLET_SIZE,
								b.x, b.y,Constant.BULLET_SIZE, Constant.BULLET_SIZE
						)
				)//判断是否与敌子弹相撞
				{
					b.explode();
					canGoFlag=false;
				}
			}
			//检测子弹是否遇到障碍物
			if(AliveWallPaperTank.map.isStrongBulletMetWithBarrier(xTemp,yTemp))
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
			case 0:
				yTemp=y-span;
			break;
			case 2:
				yTemp=y+span;
			break;	
			case 1:
				xTemp=x+span;
			break;
			case 3:
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
}
