package com.bn.tkqz;

public class HeroGoThread extends Thread
{	
	public void run()
	{ 
		while(AliveWallPaperTank.heroGoFlag)
		{	
		  if(!AliveWallPaperTank.gameOverFlag)
		  {
			  AliveWallPaperTank.hero.go();	
		  }					
			try
			{
				Thread.sleep(100);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
}
