package com.bn.tkqz;
public class RedTankFast extends TankFast
{
	public RedTankFast(int direction, int x,int y) 
	{
		super(direction, x, y);
		this.tanki=AliveWallPaperTank.tankRedi2;
	}
	@Override
	void explode()
	{
		super.explode();
		//爆炸后产生奖励物
		AliveWallPaperTank.map.reward=Reward.generateAReward();		
	}
}
