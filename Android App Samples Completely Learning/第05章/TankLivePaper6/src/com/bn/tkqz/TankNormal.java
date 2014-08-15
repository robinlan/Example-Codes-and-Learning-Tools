package com.bn.tkqz;
public class TankNormal extends Tank
{
	public TankNormal(int direction, int x, int y)
	{
		super(direction, x, y);
		this.tanki=AliveWallPaperTank.tanki1;
		this.span=2;
		this.life=1;
	}
	@Override
	void explode()
	{
		super.explode();
		AliveWallPaperTank.score+=1;
	}
}
