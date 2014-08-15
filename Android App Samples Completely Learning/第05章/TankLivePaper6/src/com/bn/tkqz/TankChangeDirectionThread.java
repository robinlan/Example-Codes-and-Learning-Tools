	package com.bn.tkqz;
	import java.util.ArrayList;
	public class TankChangeDirectionThread extends Thread 
	{		
		public void run()
		{
			while(AliveWallPaperTank.TankChangeDirectionFlag)
			{
				try
				{
					ArrayList<Tank> alTank=new ArrayList<Tank>(AliveWallPaperTank.alTank);//获取已存在坦克存放列表
					
					for(Tank tank:alTank)//循环每一个坦克
					{
						tank.changeDirection();
					}
					Thread.sleep(1000);//每隔一秒钟检测一次
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
