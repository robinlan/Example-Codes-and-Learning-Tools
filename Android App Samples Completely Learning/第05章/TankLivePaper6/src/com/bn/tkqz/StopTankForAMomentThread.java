package com.bn.tkqz;
public class StopTankForAMomentThread extends Thread
{
	@Override
	public void run()
	{
		AliveWallPaperTank.TankChangeDirectionFlag=false;
		AliveWallPaperTank.TankGoFlag=false;
		AliveWallPaperTank.tankSendBulletFlag=false;
		try
		{
			Thread.sleep(Constant.TIME_TANK_STOP);//一定时间后坦克正常恢复
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		AliveWallPaperTank.TankChangeDirectionFlag=true;
		AliveWallPaperTank.TankGoFlag=true;
		AliveWallPaperTank.tankSendBulletFlag=true;
		
		AliveWallPaperTank.go=new TankGoThread();
		AliveWallPaperTank.changeDirection=new TankChangeDirectionThread();
		AliveWallPaperTank.tankSendBullet=new TankSendBulletThread();
		
		AliveWallPaperTank.go.start();
		AliveWallPaperTank.changeDirection.start();
		AliveWallPaperTank.tankSendBullet.start();
	}
}
