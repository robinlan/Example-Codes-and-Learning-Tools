package com.bn.tkqz;

public class HeroBulletFast extends HeroBullet
{

	public HeroBulletFast(int direction, int x, int y)
	{
		super(AliveWallPaperTank.heroBullet,direction, x, y);
		this.span=Constant.HERO_BULLET_SPAN_FAST;
	}
}