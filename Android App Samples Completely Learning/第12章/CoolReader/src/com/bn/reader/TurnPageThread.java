package com.bn.reader;

public class TurnPageThread extends Thread 
{
	ReaderView readerView;
	private boolean pageflag=false;//線程停止的標識位
	private boolean thirtySecond=false;//30秒自動翻頁的標識位
	private boolean fortySecond=false;//40秒自動翻頁的標識位
	private boolean fiftySecond=false;//50秒自動翻頁的標識
	private int sleepSpan1=30000;//30秒後重畫
	private int sleepSpan2=40000;
	private int sleepSpan3=50000;
	
	TurnPageThread(ReaderView readerView)
	{
		this.readerView=readerView;
	}
	@Override
	public void run()
	{
		while(pageflag)
		{
			try{
				if(thirtySecond)
				{
					Thread.sleep(sleepSpan1);//睡眠指定毫秒數
				}else
					if(fortySecond)
					{
						Thread.sleep(sleepSpan2);//睡眠指定毫秒數
					}else
						if(fiftySecond)
						{
							Thread.sleep(sleepSpan3);//睡眠指定毫秒數
						}
            }
            catch(Exception e){
            	e.printStackTrace();//打印堆棧信息
            }
            
			readerView.currBook.put(readerView.currRR.pageNo, readerView.currRR);
		   
			//初始化到下一頁數據
			readerView.currRR=new ReadRecord(Constant.nextPageStart,0,Constant.nextPageNo);
			Constant.CURRENT_LEFT_START=readerView.currRR.leftStart;//記錄當前讀到處leftstart的值
			Constant.CURRENT_PAGE=readerView.currRR.pageNo;//記錄當前讀到處的page的值
		
			if(readerView.currRR.leftStart>Constant.CONTENTCOUNT){
				pageflag=false;//如果翻到最後一頁，則停止翻頁
			}else
			{
				//繪製左右兩幅圖片
				readerView.bmLeft=readerView.drawPage(readerView.currRR);
				readerView.bmRight=readerView.drawPage(readerView.currRR);
				readerView.repaint();
			}
		}
	}
	
	public void setPageflag(boolean pageflag) {
		this.pageflag = pageflag;
	}
	public boolean isPageflag() {
		return pageflag;
	}
	
	public void setFortySecond(boolean fortySecond) {
		this.fortySecond= fortySecond;
	}
	public boolean isFortySecond() {
		return fortySecond;
	}
	public void setThirtySecond(boolean thirtySecond) {
		this.thirtySecond = thirtySecond;
	}
	public boolean isThirtySecond() {
		return thirtySecond;
	}
	public void setFiftySecond(boolean fiftySecond) {
		this.fiftySecond = fiftySecond;
	}
	public boolean isFiftySecond() {
		return fiftySecond;
	}
}

