	package com.bn.tkqz;
	import java.util.ArrayList;
	public class TankGoThread extends Thread
	{		
		public void run()
		{
			while(AliveWallPaperTank.TankGoFlag)
			{
				try
				{
					ArrayList<Tank> alTank=new ArrayList<Tank>(AliveWallPaperTank.alTank);//获得已有坦克存放列表
					for(Tank tank:alTank)//循环控制各个坦克的运动
					{
						tank.go();
					}
					Thread.sleep(100);//每个100毫秒坦克运动一次
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
