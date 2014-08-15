package com.bn.lccx;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class GGViewCX extends View 
{
	int COMPONENT_WIDTH;							//該控件寬度
	int COMPONENT_HEIGHT;							//該控件高度
	boolean initflag=false;								//是否要獲取控件的高度和寬度標誌
	Bitmap[] bma;										//需要播放的圖片的數組
	Paint paint;										//畫筆
	static int[] drawablesId;									//圖片ID數組
	int currIndex=0;										//圖片ID數組下標，根據此變量畫圖片
	boolean workFlag=true;								//播放圖片線程標誌位
	public GGViewCX(Context father,AttributeSet as) { 			//構造器
		super(father,as);								
		drawablesId=new int[]{						//初始化圖片ID數組
		
			R.drawable.adv7,	
			R.drawable.adv8,	
			R.drawable.adv9
				
		};
		bma=new Bitmap[drawablesId.length];				//創建存放圖片的數組
		initBitmaps();									//調用初始化圖片函數，初始化圖片數組
		paint=new Paint();								//創建畫筆
		paint.setFlags(Paint.ANTI_ALIAS_FLAG);				//消除鋸齒	
		new Thread(){									//創建播放圖片線程
			public void run(){
				while(workFlag){
					currIndex=(currIndex+1)%drawablesId.length;//改變ID數組下標值
					GGViewCX.this.postInvalidate();			//繪製
					try {
						Thread.sleep(3000);				//休息三秒
					} catch (InterruptedException e) {						
						e.printStackTrace();
					}}}}.start();							//啟動線程
	}	
	public void initBitmaps(){								//初始化圖片函數
		Resources res=this.getResources();					//獲取Resources對像
		for(int i=0;i<drawablesId.length;i++){					
			bma[i]=BitmapFactory.decodeResource(res, drawablesId[i]);
		}}	
	public void onDraw(Canvas canvas){						//繪製函數
		if(!initflag) {									//第一次繪製時需要獲取寬度和高度
			COMPONENT_WIDTH=this.getWidth();			//獲取view的寬度
			COMPONENT_HEIGHT=this.getHeight();			//獲取view的高度
			initflag=true;
		}
		int picWidth=bma[currIndex].getWidth();				//獲取當前繪製圖片的寬度
		int picHeight=bma[currIndex].getHeight();				//獲取當前繪製圖片的高度
		int startX=(COMPONENT_WIDTH-picWidth)/2;			//得到繪製圖片的左上角X坐標
		int startY=(COMPONENT_HEIGHT-picHeight)/2; 		//得到繪製圖片的左上角Y坐標
		canvas.drawARGB(255, 200, 128, 128);				//設置背景色
		canvas.drawBitmap(bma[currIndex], startX,startY, paint);	//繪製圖片
	}}



