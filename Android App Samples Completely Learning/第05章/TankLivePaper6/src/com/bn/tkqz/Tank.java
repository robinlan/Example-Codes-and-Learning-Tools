package com.bn.tkqz;

import static com.bn.tkqz.Constant.GAME_VIEW_HEIGHT;
import static com.bn.tkqz.Constant.GAME_VIEW_WIDTH;
import java.util.ArrayList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Tank 
{ 
  public static final int DERECTION_UP=0;
  public static final int DERECTION_RIGHT=1;	
  public static final int DERECTION_DOWN=2;
  public static final int DERECTION_LEFT=3;
  
  Bitmap[] tanki;
  int direction;
  int x;
  int y;
  int span=2;
  int life=1;
 
  public Tank(Bitmap[] tanki,int span,int life,int direction ,int x,int y)
  {
	  this.tanki=tanki;
	  this.span=span;
	  this.life=life;
	  this.direction=direction;
	  this.x=x;
	  this.y=y;
  }
  public Tank(int direction ,int x,int y)
  {
	  this.direction=direction;
	  this.x=x;
	  this.y=y;
  }
  boolean lifeMinusOne()//生命值减1不成功，则返回false
  {
	  if(life>1)
	  {
		  life--;
		  return true;
	  }
	  return false;
  }
  void go()
  {
	  int xTemp=x;
	  int yTemp=y;

		switch(direction)//判断当前坦克的运动方向
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
		}
		else//不能移动立即转向
		{
			this.sendBullet();
			this.changeDirection();
			//go();//递归调用会抛异常
		}
  }
  boolean canGo(int xTemp,int yTemp)//判断能不能移动到新的位置
  {
	  boolean canGoFlag=true;
	//检测坦克（包括英雄坦克）在游戏界面中
	  if(!Constant.isTankInGameView(xTemp, yTemp))
	  {
		  canGoFlag=false;
	  }	  
	  //检测敌方坦克之间是否发生碰撞
	  ArrayList<Tank> alTank=new ArrayList<Tank>(AliveWallPaperTank.alTank);
	  for(Tank t:alTank)
	  {
		  if
		  (	  
			  t!=this&&
			  Constant.oneIsInAnother
			  (
					  xTemp, yTemp, Constant.TANK_SIZE, Constant.TANK_SIZE, 
					  t.x, t.y, Constant.TANK_SIZE, Constant.TANK_SIZE
			  )
		  )
		  {
			  canGoFlag=false;
		  }
	  }
	  if
	  (
		  Constant.oneIsInAnother
		  (
				  xTemp, yTemp, Constant.TANK_SIZE, Constant.TANK_SIZE, 
				  AliveWallPaperTank.hero.x, AliveWallPaperTank.hero.y, Constant.TANK_SIZE, Constant.TANK_SIZE
		  )
	  )
	  {
		  canGoFlag=false;
	  }
	  //检测坦克是否遇到障碍物
	  if(AliveWallPaperTank.map.isTankMetWithBarrier(xTemp,yTemp))
	  {
		  canGoFlag=false;
	  }
	  return canGoFlag;
  }
  void changeDirection()
  { 
	  if(Math.random()<=Constant.TANK_CHANGE_DIRECTION_POSSIBILITY)//当获得的随机数小于阈值时，改变坦克方向
		{
			if(x>0||x<GAME_VIEW_WIDTH||
			   y>0||y<GAME_VIEW_HEIGHT-Constant.TANK_SIZE)//判断当前坦克是否在界面中
			{
				int random=(int)(Math.random()*Constant.VALUE_TANK_GO_DOWN);//随机改变坦克方向				
				if(random>3)
				{
					direction=Tank.DERECTION_DOWN;
				}
				else
				{
					this.direction=random;
				}
			}						
		}
  }
  Bullet sendBullet()
  {
		int direction=this.direction;//获得当前敌坦克的运动方向和位置
		int x=this.x;
		int y=this.y;
			
		switch(direction)//初始化子弹的初始位置
		{
			case Tank.DERECTION_UP://up
			 x=x+Constant.TANK_SIZE/2-Constant.BULLET_SIZE/2-1;
			 y=y-Constant.BULLET_SIZE/2+1;
			break;
			case Tank.DERECTION_DOWN://down
			 x=x+Constant.TANK_SIZE/2-Constant.BULLET_SIZE/2-1;
			 y=y+Constant.TANK_SIZE-Constant.BULLET_SIZE/2-1;
			break;
			case Tank.DERECTION_RIGHT://right
			 x=x+Constant.TANK_SIZE-Constant.BULLET_SIZE/2-1;
			 y=y+Constant.TANK_SIZE/2-Constant.BULLET_SIZE/2-1;
			break;
			case Tank.DERECTION_LEFT://left
			 x=x-Constant.BULLET_SIZE/2+1;
			 y=y+Constant.TANK_SIZE/2-Constant.BULLET_SIZE/2-1;
			break;
		}
		
		return new Bullet(AliveWallPaperTank.bullet,direction,x,y);//创建新的敌子弹
  }
  void drawSelf(Canvas canvas,Paint paint)
  {
	  canvas.drawBitmap(tanki[direction], x,y, paint);
  }
  void explode() 
  {
	  AliveWallPaperTank.alTank.remove(this);
	  AliveWallPaperTank.countTankDestoryed=AliveWallPaperTank.countTankDestoryed+1;
	  if(AliveWallPaperTank.isCurrentMissionCompleted())
	  {
		  AliveWallPaperTank.map.goToNextMission();
	  }	  
  }
}
