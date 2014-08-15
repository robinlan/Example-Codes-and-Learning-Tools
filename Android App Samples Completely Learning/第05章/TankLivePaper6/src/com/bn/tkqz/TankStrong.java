package com.bn.tkqz;
public class TankStrong extends Tank
{
	public TankStrong(int direction, int x, int y)
	{
		super(direction, x, y);
		this.tanki=AliveWallPaperTank.tanki3;
		this.span=2;
		this.life=3;
	}
	@Override
	void explode()
	{
		super.explode();
		AliveWallPaperTank.score+=3;
	}
}
