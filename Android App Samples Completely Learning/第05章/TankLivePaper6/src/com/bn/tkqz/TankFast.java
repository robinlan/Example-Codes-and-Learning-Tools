package com.bn.tkqz;
public class TankFast extends Tank
{
	
	public TankFast(int direction, int x, int y)
	{
		super(direction, x, y);
		this.tanki=AliveWallPaperTank.tanki2;
		this.span=4;
		this.life=1;
	}
	@Override
	void explode()
	{
		super.explode();
		AliveWallPaperTank.score+=2;
	}
}
