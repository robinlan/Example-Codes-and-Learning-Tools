	package com.bn.tkqz;
	import java.util.ArrayList;
	public class TankSendBulletThread extends Thread
	{
		@Override
		public void run()
		{
			while(AliveWallPaperTank.tankSendBulletFlag)
			{
				try
				{
					ArrayList<Tank> alTank=new ArrayList<Tank>(AliveWallPaperTank.alTank);//获得敌坦克列表
					for(Tank t:alTank)
					{
						if
						(
								Math.random()<Constant.TANK_SEND_BULLET_POSSIBILITY&&
								AliveWallPaperTank.alBullet.size()<Constant.TANK_BULLET_MAX_NUM
						)
						{
							Bullet b=t.sendBullet();
							AliveWallPaperTank.alBullet.add(b);
						}
					}
					
					Thread.sleep(1000);//每隔一秒钟发射一次
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
