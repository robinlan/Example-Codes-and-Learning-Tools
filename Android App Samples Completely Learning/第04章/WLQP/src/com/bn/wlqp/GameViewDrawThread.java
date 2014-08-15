package com.bn.wlqp;
import static com.bn.wlqp.Constant.*;
public class GameViewDrawThread extends Thread
{//绘制界面时的刷帧线程
	GameView gameview;
	boolean flag=true;
	public GameViewDrawThread(GameView gameview)
	{
		this.gameview=gameview;
	}
	@Override
	public void run()
	{
		while(flag){
			gameview.repaint();//定时刷帧  这样的要写在一个方法中
			try{
				Thread.sleep(sleeptime);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}