	package com.bn.tkqz;
	import java.util.ArrayList;
	public class HeroBulletGoThread extends Thread
	{
		public void run()
		{
			while(AliveWallPaperTank.heroBulletGoFlag)
			{
				try
				{
					ArrayList<HeroBullet> alHeroBullet=new ArrayList<HeroBullet>(AliveWallPaperTank.alHeroBullet);//复制敌子弹列表
					
					for(HeroBullet hb:alHeroBullet)//循环扫描英雄子弹列表
					{
						hb.go();					
						Thread.sleep(10);//每10毫秒检测一次
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}		
			}
		}
	}
