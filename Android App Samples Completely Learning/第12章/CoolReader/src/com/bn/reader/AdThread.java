package com.bn.reader;

public class AdThread  extends Thread
{
	boolean adFlag=true;//控制刷新的標誌位
	ReaderView reader;//閱讀界面的引用
	//創建即可得到引用
	public AdThread(ReaderView reader)//創建即可得到引用
	{
		this.reader=reader;//拿到ReaderView的引用
	}
	//線程運行
	public void run()//線程運行
	{ 
		while(adFlag)
		{				
		 try{
			 Constant.NUM=(Constant.NUM+1)%reader.ad.length;
			 if(reader.repaintAdFlag){				 
				 reader.repaint();//重繪界面
			 }
			 Thread.sleep(1000);//間隔1000ms重新繪製一次
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	//用來停止線程
	public synchronized void stopCurrentThread() {
        this.adFlag = false;//用來停止線程
    }
}
