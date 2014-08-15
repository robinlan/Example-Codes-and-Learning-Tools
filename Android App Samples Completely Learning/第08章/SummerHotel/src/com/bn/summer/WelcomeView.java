package com.bn.summer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WelcomeView extends SurfaceView 
implements SurfaceHolder.Callback   //實現生命週期回調接口
{
	MainActivity activity;//activity的引用
	Paint paint;      //畫筆
	int currentAlpha=0;  //當前的不透明值
	int screenWidth=320;   //屏幕寬度
	int screenHeight=480;  //屏幕高度
	int sleepSpan=50;      //動畫的時延ms
	Bitmap[] logos=new Bitmap[2];//logo圖片數組
	Bitmap currentLogo;  //當前logo圖片引用
	int currentX;      //圖片位置
	int currentY;
	public WelcomeView(MainActivity activity)
	{
		super(activity);
		this.activity = activity;
		this.getHolder().addCallback(this);  //設置生命週期回調接口的實現者
		paint = new Paint();  //創建畫筆
		paint.setAntiAlias(true);  //打開抗鋸齒
		//加載圖片
		logos[0]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.baina);
		logos[1]=BitmapFactory.decodeResource(activity.getResources(), R.drawable.bnkjs);		
	}
	public void onDraw(Canvas canvas)
	{	
		//繪製黑填充矩形清背景
		paint.setColor(Color.BLACK);//設置畫筆顏色
		paint.setAlpha(255);//設置不透明度為255
		canvas.drawRect(0, 0, screenWidth, screenHeight, paint);
		//進行平面貼圖
		if(currentLogo==null)return;
		paint.setAlpha(currentAlpha);		
		canvas.drawBitmap(currentLogo, currentX, currentY, paint);	
	}
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
	{
	}
	public void surfaceCreated(SurfaceHolder holder) //創建時被調用	
	{	
		new Thread()
		{
			public void run()
			{
				for(Bitmap bm:logos)
				{
					currentLogo=bm;//當前圖片的引用
					currentX=screenWidth/2-bm.getWidth()/2;//圖片位置
					currentY=screenHeight/2-bm.getHeight()/2;
					for(int i=255;i>-10;i=i-10)
					{//動態更改圖片的透明度值並不斷重繪	
						currentAlpha=i;
						if(currentAlpha<0)//如果當前不透明度小於零
						{
							currentAlpha=0;//將不透明度置為零
						}
						SurfaceHolder myholder=WelcomeView.this.getHolder();//獲取回調接口
						Canvas canvas = myholder.lockCanvas();//獲取畫布
						try{
							synchronized(myholder)//同步
							{
								onDraw(canvas);//進行繪製繪製
							}
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
						finally
						{
							if(canvas!= null)//如果當前畫布不為空
							{
								myholder.unlockCanvasAndPost(canvas);//解鎖畫布
							}
						}
						try
						{
							if(i==255)//若是新圖片，多等待一會
							{
								Thread.sleep(1000);
							}
							Thread.sleep(sleepSpan);
						}
						catch(Exception e)//拋出異常
						{
							e.printStackTrace();
						}
					}
				}
				Message msg=new Message();
				msg.what=Constant.GOTOLOGIN;
				activity.hd.sendMessage(msg);//發送消息，開始加載棋子模型
			}
		}.start();
	}
	public void surfaceDestroyed(SurfaceHolder arg0)
	{//銷毀時被調用
	}
}
