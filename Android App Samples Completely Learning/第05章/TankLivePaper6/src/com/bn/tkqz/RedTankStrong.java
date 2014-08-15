package com.bn.tkqz;

public class RedTankStrong extends TankStrong
{
	public RedTankStrong(int direction, int x,int y) 
	{
		super(direction, x, y);
		this.tanki=AliveWallPaperTank.tankRedi3;
	}
	@Override
	void explode()
	{
		super.explode();
		//爆炸后产生奖励物
		AliveWallPaperTank.map.reward=Reward.generateAReward();
	}
}
