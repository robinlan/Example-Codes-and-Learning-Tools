package com.bn.tkqz;

public class HeroSendBulletThread extends Thread 
{
	@Override
	public void run()
	{
		while(AliveWallPaperTank.heroSendBulletFlag)
		{
			//Ó¢ÐÛÌ¹¿Ë·¢Éä×Óµ¯
 			if(AliveWallPaperTank.alHeroBullet.size()<AliveWallPaperTank.hero.heroBulletMaxNum)
 			{
 				HeroBullet hb=AliveWallPaperTank.hero.sendBullet();
 				AliveWallPaperTank.alHeroBullet.add(hb);
 			} 
		}
	}
}
