package com.bn.tkqz;

public class HeroBulletNormal extends HeroBullet
{

	public HeroBulletNormal(int direction, int x, int y)
	{
		super(AliveWallPaperTank.heroBullet,direction, x, y);
		this.span=Constant.HERO_BULLET_INIT_SPAN;
	}

}
