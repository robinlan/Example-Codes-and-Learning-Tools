package com.bn.tkqz;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Reward 
{
	public static final int STAR=0;
	public static final int BOMB=1;
	public static final int LIFE=2;
	public static final int SHOVEL=3;
	public static final int PROTECTOR=4;
	public static final int TIMER=5;
	
	//奖励物的种类数，新添奖励物时要及时修改
	public static final int REWARD_KIND_COUNT=6;
	int x;
	int y;
	Bitmap bitmap;
	public Reward()
	{
		//随机生成奖励物的位置
		x=(int)(Math.random()*(Constant.GAME_VIEW_WIDTH-Constant.REWARD_SIZE))+Constant.GAME_VIEW_X;
		y=(int)(Math.random()*(Constant.GAME_VIEW_HEIGHT-Constant.REWARD_SIZE))+Constant.GAME_VIEW_Y;
	}
	abstract void drawSelf(Canvas canvas,Paint paint);
	//随机产生一个奖励物的方法
	static Reward generateAReward()
	{
		Reward result=null;
		int i=(int)(Math.random()*Reward.REWARD_KIND_COUNT);
		switch(i)
		{
		case Reward.STAR:
			result=new Star(AliveWallPaperTank.starBitmap);
			break;
		case Reward.BOMB:
			result=new Bomb(AliveWallPaperTank.bombBitmap);
			break;
		case Reward.LIFE:
			result=new Life(AliveWallPaperTank.lifeBitmap);
			break;
		case Reward.SHOVEL:
			result=new Shovel(AliveWallPaperTank.shovelBitmap);
			break;
		case Reward.PROTECTOR:
			result=new Protector(AliveWallPaperTank.protectorBitmap);
			break;
		case Reward.TIMER:
			result=new Timer(AliveWallPaperTank.timerBitmap);
			break;
		}
		return result;		
	}
}
