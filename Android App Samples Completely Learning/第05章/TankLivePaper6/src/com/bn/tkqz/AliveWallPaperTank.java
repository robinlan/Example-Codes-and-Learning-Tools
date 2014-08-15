package com.bn.tkqz;//聲明包語句
import static com.bn.tkqz.Constant.SCREEN_HEIGHT;//引入相關類
import static com.bn.tkqz.Constant.SCREEN_WIDTH;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.SoundPool;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class AliveWallPaperTank extends WallpaperService
{	
	static Handler hd = new Handler();//創建靜態Handler對像
	EngineCrazyTank tankEngine;//EngineCrazyTank對象的引用
    
    static Bitmap bullet;//子彈位圖
    static Bitmap heroBullet;//英雄子彈位圖
    //敵方白坦克
    static Bitmap[] tanki1;//坦克位圖數組1
    static Bitmap[] tanki2;//坦克位圖數組2
    static Bitmap[] tanki3;//坦克位圖數組3
    //敵方紅坦克
    static Bitmap[] tankRedi1;//紅坦克位圖數組1
    static Bitmap[] tankRedi2;//紅坦克位圖數組2
    static Bitmap[] tankRedi3;//紅坦克位圖數組3
    //英雄坦克
    static Bitmap[] heroTanki1;//英雄坦克位圖數組1
    static Bitmap[] heroTanki2;//英雄坦克位圖數組2
    static Bitmap[] heroTanki3;//英雄坦克位圖數組3
    static Bitmap[] heroTanki4;//英雄坦克位圖數組4
    
    //障礙物
    static Bitmap brickBitmap;
    static Bitmap stoneBitmap;
    static Bitmap seaBitmap;
    static Bitmap iceBitmap;
    static Bitmap grassBitmap;
    //老窩
    static Bitmap homeBitmap;
    static Bitmap homediedBitmap;
    //獎勵物
    static Bitmap starBitmap;
    static Bitmap bombBitmap;
    static Bitmap lifeBitmap;
    static Bitmap shovelBitmap;
    static Bitmap protectorBitmap;
    static Bitmap timerBitmap;
    //英雄坦克保護器外殼
    static Bitmap coveringBitmap;
    //虛擬按鈕
    static Bitmap controlBitmap;
    static Bitmap redDotBitmap;
    static Bitmap fireBtnUpBitmap;
    static Bitmap fireBtnDownBitmap;
    //其它
    static Bitmap[] numbers;
    static Bitmap gameOver;
    static Bitmap restartBitmap;
    static SoundPool soundPool;
    static Map<Integer,Integer> soundPoolMap;
    //================================== 非資源 變量 begin ==========================================
    static BattleMap map;//地圖引用
 	static Hero hero;
 	static int keyState=0;//按鍵狀態,1——up,2——down,4——left,8——right
 	
 	static boolean heroGoFlag=true;//刷新界麵線程標誌位
 	
 	static ArrayList<Tank> alTank;//敵方坦克列表
 	private TankGeneratorThread  generator;//隨機產生敵方坦克線程的引用
 	static TankGoThread go;//敵方坦克行進線程的引用
 	HeroGoThread heroGo;//英雄坦克行進線程的引用
 	static HeroSendBulletThread heroSendBullet;//英雄坦克發射子彈線程的引用
 	static boolean heroSendBulletFlag=true;//英雄坦克發射子彈的標誌位
 	static TankChangeDirectionThread changeDirection;//敵方坦克隨機改變方向線程的引用
 	static boolean TankGeneratorFlag=true;//隨機產生坦克的標誌位
 	static boolean TankGoFlag=true;//英雄坦克行進的標誌
 	static boolean TankChangeDirectionFlag=true;//坦克隨機改變方向的標誌位
 	
 	static Vector<HeroBullet> alHeroBullet;//英雄子彈列表
 	private HeroBulletGoThread heroBulletGo;
 	static boolean heroBulletGoFlag=true;
 	
 	static ArrayList<Bullet> alBullet;//敵方子彈列表
 	static TankSendBulletThread tankSendBullet;
 	private TankBulletGoThread tankBulletGo;
 	static boolean tankSendBulletFlag=true;
 	static boolean tankBulletGoFlag=true;
 	
 	static boolean gameOverFlag=false;			//遊戲結束標誌位
 	static int countTankDestoryed=0;//記錄擊中坦克數量
 	static long gameStartTime;//遊戲開始時間
 	static int time=0;//遊戲進行時間
 	static int fullTime=0;//記錄遊戲界面現在總時間，從開始遊戲 到退出遊戲
 	static int score=0;//遊戲得分
 	private boolean fireButtonDownFlag=false;//發射按鈕是否被按下的標誌
 	static ScreenScaleResult ssr;
 	 //================================== 非資源 變量 end ==========================================
	@Override
    public Engine onCreateEngine() 
    {
		initBitmap();//初始化位圖資源 
	    tankEngine=new EngineCrazyTank(); 
        return tankEngine;
    }
	private void initBitmap()
    {//初始化位圖資源
    	bullet=BitmapFactory.decodeResource(this.getResources(), R.drawable.b);//創建子彈位圖
    	heroBullet=BitmapFactory.decodeResource(this.getResources(), R.drawable.hb);//創建子彈位圖
    	tanki1=new Bitmap[]
    	{//創建坦克位圖1
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.up1),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.right1),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.down1),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.left1)    			
    	};
    	tanki2=new Bitmap[]
    	{//創建坦克位圖1
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.up2),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.right2),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.down2),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.left2)    			
    	};
    	tanki3=new Bitmap[]
    	{//創建坦克位圖2
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.up3),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.right3),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.down3),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.left3)    			
    	};
    	//英雄坦克
    	heroTanki1=new Bitmap[]
    	                      {//創建英雄坦克位圖1
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.heroup1),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.heroright1),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.herodown1),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.heroleft1)    			
    	};
    	heroTanki2=new Bitmap[]
    	                      {//創建英雄坦克位圖2
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.heroup2),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.heroright2),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.herodown2),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.heroleft2)    			
    	};
    	heroTanki3=new Bitmap[]
    	                      {//創建英雄坦克位圖3
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.heroup3),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.heroright3),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.herodown3),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.heroleft3)    			
    	};
    	heroTanki4=new Bitmap[]
    	                      {//創建英雄坦克位圖3
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.heroup4),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.heroright4),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.herodown4),
    			BitmapFactory.decodeResource(this.getResources(), R.drawable.heroleft4)    			
    	};
    	//紅坦克
    	tankRedi1=new Bitmap[]
					    	{//創建紅坦克位圖1
					    			BitmapFactory.decodeResource(this.getResources(), R.drawable.upred1),
					    			BitmapFactory.decodeResource(this.getResources(), R.drawable.rightred1),
					    			BitmapFactory.decodeResource(this.getResources(), R.drawable.downred1),
					    			BitmapFactory.decodeResource(this.getResources(), R.drawable.leftred1)    			
					    	};
    	tankRedi2=new Bitmap[]
    	                 	{//創建紅坦克位圖2
    	                 			BitmapFactory.decodeResource(this.getResources(), R.drawable.upred2),
    	                 			BitmapFactory.decodeResource(this.getResources(), R.drawable.rightred2),
    	                 			BitmapFactory.decodeResource(this.getResources(), R.drawable.downred2),
    	                 			BitmapFactory.decodeResource(this.getResources(), R.drawable.leftred2)    			
    	                 	};
    	tankRedi3=new Bitmap[]
    	                 	{//創建紅坦克位圖3
    	                 			BitmapFactory.decodeResource(this.getResources(), R.drawable.upred3),
    	                 			BitmapFactory.decodeResource(this.getResources(), R.drawable.rightred3),
    	                 			BitmapFactory.decodeResource(this.getResources(), R.drawable.downred3),
    	                 			BitmapFactory.decodeResource(this.getResources(), R.drawable.leftred3)    			
    	                 	};
    	//障礙物
    	brickBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.brick);//創建磚牆位圖
    	stoneBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.stone);//創建石牆位圖
    	seaBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.sea);//創建海洋位圖
    	iceBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.ice);//創建冰位圖
    	grassBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.grass);//創建草地位圖
    	//老窩
    	homeBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.home);//創建老窩位圖
    	homediedBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.homedied);//創建老窩死後位圖
    	//獎勵物
    	starBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.star);//星星
    	bombBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.bomb);//炸彈
    	lifeBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.life);//命
    	shovelBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.shovel);//鐵鍬
    	protectorBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.protector);//保護器
    	timerBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.timer);//定時
    	//外殼
    	coveringBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.covering);
    	//數字
		numbers=new Bitmap[]{//數組位圖
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number0),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number1),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number2),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number3),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number4),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number5),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number6),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number7),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number8),
				BitmapFactory.decodeResource(this.getResources(), R.drawable.number9)
			};
		gameOver =BitmapFactory.decodeResource(this.getResources(), R.drawable.gameover);//遊戲結束位圖
		restartBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.restart);//提示重新開始的位圖
		controlBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.control);//虛擬按鈕位圖
		redDotBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.reddot);//紅點位圖
		fireBtnUpBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.fireup);//發射位圖
		fireBtnDownBitmap=BitmapFactory.decodeResource(this.getResources(), R.drawable.firedown);//發射位圖
    }  
	//===========================================非資源方法== begin ====================
	//遊戲結束的方法
 	public static void overGame()
 	{
 		gameOverFlag=true;
 		TankGeneratorFlag=false;
 		heroGoFlag=true;
 		TankGoFlag=true;
 		TankChangeDirectionFlag=true;
 		heroBulletGoFlag=false;
 		tankSendBulletFlag=false;
 		tankBulletGoFlag=false;
 		AliveWallPaperTank.keyState=0;//按鍵狀態置空
 	}
 	//檢測當前任務是否完成的方法
 	public static boolean isCurrentMissionCompleted()
 	{
 		return AliveWallPaperTank.countTankDestoryed>=Constant.TANK_TOTAL_NUM;	
 	}
 	//判斷是否為豎屏的方法
 	public static boolean isShuPing()
 	{
 		return (ssr.so==ScreenOrien.SP);
 	}
 	//===========================================?親試捶椒?== end ====================
 	
class EngineCrazyTank extends Engine 
 {
     private final Paint paint = new Paint();
     boolean ifDraw;

     private final Runnable drawTask = new Runnable() {
         public void run() {
             repaint();
         }
     };
     
     EngineCrazyTank() 
     {
    	 
     }

     @Override
     public void onCreate(SurfaceHolder surfaceHolder) 
     {
         super.onCreate(surfaceHolder);            
         setTouchEventsEnabled(true); 
     } 

     @Override
     public void onDestroy() 
     {
         super.onDestroy();       
     }

     @Override
     public void onVisibilityChanged(boolean visible) 
     {
    	 ifDraw=visible;  	 
    	 if(ifDraw)
         {
    		 //如果可見，重新初始化所有數據
    		 this.initAllData();
        	 hd.postDelayed(drawTask, 1000 / 25);
         }
    	 else
    	 {
    		 //如果不可見，停止所有線程
    		 this.stopAllThreads();
    	 }
     }

     @Override
     public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) 
     {System.out.println(width+","+height);
        super.onSurfaceChanged(holder, format, width, height);
        //自動判斷橫屏豎屏
 		ssr=ScreenScaleUtil.calScale(width, height);
 		Constant.LEFT_TOP_X=ssr.lucX;
 		Constant.LEFT_TOP_Y=ssr.lucY;
 		this.initAllData();//初始化所有數據
 	}

     @Override
     public void onSurfaceCreated(SurfaceHolder holder) 
     {
         super.onSurfaceCreated(holder);
     }

     @Override
     public void onSurfaceDestroyed(SurfaceHolder holder) 
     {
        super.onSurfaceDestroyed(holder);
        this.stopAllThreads();// 停止所有線程
     }

     //桌面換子屏幕時的回調方法
     @Override
     public void onOffsetsChanged(float xOffset, float yOffset,
             float xStep, float yStep, int xPixels, int yPixels) 
     {
    	 Constant.LEFT_TOP_X=(int) (ssr.lucX+xOffset);
  		 Constant.LEFT_TOP_Y=(int) (ssr.lucY+yOffset);
     }

     @Override
     public void onTouchEvent(MotionEvent event) 
     {
         float y = event.getY();
         float x = event.getX();
         switch (event.getAction()) {
 	        case MotionEvent.ACTION_DOWN://===========按下虛擬鍵盤==========
 	            if(//===== up ===
 	            		Constant.pointIsInRect
 	            		(
 	            				x, y, 
 	            				Constant.UP_X, Constant.UP_Y,Constant.BUTTON_WIDTH, Constant.BUTTON_HEIGHT
 	            		)
 	            )
 	            {
 	            	keyState=(keyState==0x1)?0:0x1;
 	            }
 	            else if(//===== down ===
 		            		Constant.pointIsInRect
 		            		(
 		            				x, y, 
 		            				Constant.DOWN_X, Constant.DOWN_Y,Constant.BUTTON_WIDTH, Constant.BUTTON_HEIGHT
 		            		)
 	            		)
 	            {
 	            	keyState=(keyState==0x2)?0:0x2;
 	            }
 	            else if(//===== left ===
 	            		Constant.pointIsInRect
 	            		(
 	            				x, y, 
 	            				Constant.LEFT_X, Constant.LEFT_Y,Constant.BUTTON_WIDTH, Constant.BUTTON_HEIGHT
 	            		)
             		)
 		            {
 	            		keyState=(keyState==0x4)?0:0x4;
 		            }
 	            else if(//===== right ===
 	            		Constant.pointIsInRect
 	            		(
 	            				x, y, 
 	            				Constant.RIGHT_X, Constant.RIGHT_Y,Constant.BUTTON_WIDTH, Constant.BUTTON_HEIGHT
 	            		)
             		)
 	            {
 	            	keyState=(keyState==0x8)?0:0x8;
 	            }
 	            else if(//===== fire area ===
 	            		Constant.pointIsInRect
 	            		(
 	            				x, y, 
 	            				Constant.FIRE_BTN_X, Constant.FIRE_BTN_Y, Constant.FIRE_BTN_WIDTH, Constant.FIRE_BTN_HEIGHT
 	            		)
             		)
 	            {
 	            	fireButtonDownFlag=((fireButtonDownFlag==true)?false:true);
 	            	if(fireButtonDownFlag)
 	            	{
 	            		AliveWallPaperTank.heroSendBulletFlag=true;
 	            		AliveWallPaperTank.heroSendBullet=new HeroSendBulletThread();
 	            		heroSendBullet.start();
 	            	}
 	            	else
 	            	{
 	            		AliveWallPaperTank.heroSendBulletFlag=false;
 	            	}
 	            	
 	            }
 	            //如果遊戲結束，點擊重玩的觸控區域，重新開始遊戲
 	            if(gameOverFlag)
 	            {
 	            	if(
 	            			Constant.pointIsInRect
	 	            		(
	 	            				x, y, 
	 	            				Constant.FIRE_BTN_X, Constant.FIRE_BTN_Y, Constant.FIRE_BTN_WIDTH, Constant.FIRE_BTN_HEIGHT
	 	            		)
 	            	)
 	            	{
 	            		stopAllThreads();//停止以前所有線程
 	            		initAllData();//重新初始化所有數據和線程
 	            	}
 	            }
 		        break;
 		        //===================分界===============================================
         }
     }


     void repaint()
     {
         final SurfaceHolder holder = getSurfaceHolder();
         Canvas c = null;
         try 
         {
             c = holder.lockCanvas();
             if (c != null) 
             {
            	 onDraw(c);
             }
         } 
         finally 
         {
             if (c != null) holder.unlockCanvasAndPost(c);
         }
         if(ifDraw)
         {
        	 hd.postDelayed(drawTask, 1000 / 20);
         }
     }
     public void onDraw(Canvas canvas)
 	{	
 		canvas.drawColor(Color.argb(255, 0, 0, 0));//擦空界面
 		//繪製下層地圖
 		map.drawSelfBelow(canvas, paint);
 		hero.drawSelf(canvas, paint);//繪製英雄坦克
 		//繪製敵方坦克
 		ArrayList<Tank> alTank=new ArrayList<Tank>(AliveWallPaperTank.alTank);//獲得當前已存在敵坦克存放列表
 		for(Tank t:alTank)
 		{
 			t.drawSelf(canvas, paint);
 		}
 		//繪製英雄子彈
 		ArrayList<HeroBullet> alHeroBullet=new ArrayList<HeroBullet>(AliveWallPaperTank.alHeroBullet);//複製敵子彈列表
 		for(HeroBullet hb:alHeroBullet)
 		{
 			hb.drawSelf(canvas, paint);
 		}
 		//繪製敵方子彈
 		ArrayList<Bullet> alBullet=new ArrayList<Bullet>(AliveWallPaperTank.alBullet);//複製敵子彈列表
 		for(Bullet b:alBullet)
 		{
 			b.drawSelf(canvas, paint);
 		}
 		//繪製上層地圖
 		map.drawSelfAbove(canvas, paint);
 		if(AliveWallPaperTank.isShuPing())
 		{
 			//繪製屏幕右側數據信息
 			drawAllDataMessageSP(canvas,paint);
 			//繪製虛擬按鍵
 			drawVirtualButtonSP(canvas,paint);
 		}
 		else
 		{
 			//繪製屏幕上側數據信息
 			drawAllDataMessageHP(canvas,paint);
 			//繪製虛擬按鍵
 			drawVirtualButtonHP(canvas,paint);
 			
 		}
 		//當遊戲結束時，繪製Game Over提示信息
 		long currentTime=System.currentTimeMillis();//記錄當前時間
 		fullTime=(int) ((currentTime-gameStartTime)/1000);//記錄遊戲總時間
 		
 		if(gameOverFlag)
 		{
 			if(fullTime%2==0)
 			{//繪製遊戲結束界面
 				canvas.drawBitmap(gameOver, SCREEN_WIDTH/2-100, SCREEN_HEIGHT/2-26, paint);
 				canvas.drawBitmap(restartBitmap, SCREEN_WIDTH/2-100, SCREEN_HEIGHT-120, paint);
 			}
 		}

 	}
 	//=============================================== SP =========================== begin =======
 	//繪製屏幕右側數據信息的方法
 	void drawAllDataMessageSP(Canvas canvas,Paint paint)
 	{
 		paint.setColor(Color.RED);	//將畫筆顏色設置為紅色
 		//繪製上側灰色條
 		canvas.drawRect
 		(
 				Constant.GAME_VIEW_X,						//left, 
 				Constant.GAME_VIEW_Y-Constant.UP_BAR-2,			//top, 
 				Constant.GAME_VIEW_X+Constant.GAME_VIEW_WIDTH,	//right, 
 				Constant.GAME_VIEW_Y-2,	//bottom, 
 				paint
 		);
 		paint.setColor(Color.YELLOW);	//將畫筆顏色設置為黃色
 		paint.setTextSize(13);		//設置字體大小
 		
 		drawOneDataMessageSP(5,"得分",score,canvas,paint);
 		drawOneDataMessageSP(3,"擊毀",countTankDestoryed,canvas,paint);
 		drawOneDataMessageSP(4,"英雄",Hero.HEROLIFE,canvas,paint);
 		drawOneDataMessageSP(1,"關卡",map.getMissionNum(),canvas,paint);
 		drawOneDataMessageSP(2,"敵坦克",Constant.TANK_TOTAL_NUM,canvas,paint);
 	}
 	void drawOneDataMessageSP(int order,String msg,int number,Canvas canvas,Paint paint)
 	{		
 		canvas.drawText
 		(
 				msg, 
 				Constant.GAME_VIEW_X+Constant.FIRST_HANZI_WIDTH+(order-1)*Constant.HANZI_WIDTH, 
 				Constant.GAME_VIEW_Y-Constant.HANZI_HEIGHT-Constant.NUMBER_HEIGHT, 
 				paint
 		);
 		String numberStr=number+"";
 		for(int i=0;i<numberStr.length();i++)
 		{
 			char c=numberStr.charAt(i);
 			canvas.drawBitmap
 			(
 					numbers[c-'0'], 
 					Constant.GAME_VIEW_X+Constant.FIRST_NUMBER_WIDTH+order*Constant.NUMBER_TOTAL_WIDTH-Constant.NUMBER_WIDTH*(numberStr.length()-i), 
 					Constant.GAME_VIEW_Y-Constant.NUMBER_HEIGHT, 
 					paint
 			);
 		}
 	}
 	//畫虛擬按鈕的方法
 	void drawVirtualButtonSP(Canvas canvas,Paint paint)
 	{
 		canvas.drawBitmap
 		(
 				AliveWallPaperTank.controlBitmap, 
 				Constant.BUTTON_X,
 				Constant.BUTTON_Y, 
 				paint
 		);
// 		//用有色矩形標記可觸控區域
// 		paint.setColor(Color.BLUE);
// 		drawColorRect
// 		(
// 				canvas,paint,
// 				Constant.UP_X, Constant.UP_Y,Constant.BUTTON_WIDTH, Constant.BUTTON_HEIGHT
// 		);
// 		drawColorRect
// 		(
// 				canvas,paint,
// 				Constant.DOWN_X, Constant.DOWN_Y,Constant.BUTTON_WIDTH, Constant.BUTTON_HEIGHT
// 		);
// 		drawColorRect
// 		(
// 				canvas,paint,
// 				Constant.LEFT_X, Constant.LEFT_Y,Constant.BUTTON_WIDTH, Constant.BUTTON_HEIGHT
// 		);
// 		drawColorRect
// 		(
// 				canvas,paint,
// 				Constant.RIGHT_X, Constant.RIGHT_Y,Constant.BUTTON_WIDTH, Constant.BUTTON_HEIGHT
// 		);
// 		//標記發射子彈的區域
// 		drawColorRect
// 		(
// 				canvas,paint,
// 				Constant.FIRE_BTN_X, Constant.FIRE_BTN_Y, Constant.FIRE_BTN_WIDTH, Constant.FIRE_BTN_HEIGHT
// 		);
 		//畫虛擬方向鍵圖片
 		canvas.drawBitmap
 		(
 				AliveWallPaperTank.controlBitmap, 
 				Constant.BUTTON_X,
 				Constant.BUTTON_Y, 
 				paint
 		);
 		switch(keyState)
 		{
 			case 0x1:	//上
 			{
 				//畫紅點
 				canvas.drawBitmap
 				(
 						AliveWallPaperTank.redDotBitmap, 
 						Constant.RED_DOT_CENTER_X,
 						Constant.RED_DOT_CENTER_Y-Constant.BUTTON_HEIGHT, 
 						paint
 				);
 			}
 			break;
 			case 0x2:	//下
 			{
 				//畫紅點
 				canvas.drawBitmap
 				(
 						AliveWallPaperTank.redDotBitmap, 
 						Constant.RED_DOT_CENTER_X,
 						Constant.RED_DOT_CENTER_Y+Constant.BUTTON_HEIGHT, 
 						paint
 				);
 			}
 			break;
 			case 0x4:	//左
 			{
 				//畫紅點
 				canvas.drawBitmap
 				(
 						AliveWallPaperTank.redDotBitmap, 
 						Constant.RED_DOT_CENTER_X-Constant.BUTTON_WIDTH,
 						Constant.RED_DOT_CENTER_Y, 
 						paint
 				);
 			}
 			break;
 			case 0x8:	//右
 			{
 				//畫紅點
 				canvas.drawBitmap
 				(
 						AliveWallPaperTank.redDotBitmap, 
 						Constant.RED_DOT_CENTER_X+Constant.BUTTON_WIDTH,
 						Constant.RED_DOT_CENTER_Y, 
 						paint
 				);
 			}
 			break;
 			case 0:
 			{
 				//畫紅點
 				canvas.drawBitmap
 				(
 						AliveWallPaperTank.redDotBitmap, 
 						Constant.RED_DOT_CENTER_X,
 						Constant.RED_DOT_CENTER_Y, 
 						paint
 				);
 			}
 			break;
 		}//switch
 		//畫發射圖片
 		if(fireButtonDownFlag)
 		{//System.out.println("+++++fireButtonDownFlag++++++ true ++++++");
 			canvas.drawBitmap
 			(
 					AliveWallPaperTank.fireBtnDownBitmap, 
 					Constant.FIR_MAP_X,
 					Constant.FIR_MAP_Y, 
 					paint
 			);
 		}
 		else
 		{//System.out.println("+++++fireButtonDownFlag+++++++ false        +++++");
 			canvas.drawBitmap
 			(
 					AliveWallPaperTank.fireBtnUpBitmap, 
 					Constant.FIR_MAP_X,
 					Constant.FIR_MAP_Y, 
 					paint
 			);
 		}
 	}
 	//=============================================== SP =========================== end =======
 	//=============================================== HP ====== begin =======
 	//繪製屏幕右側數據信息的方法
 	void drawAllDataMessageHP(Canvas canvas,Paint paint)
 	{
 		paint.setColor(Color.RED);	//將畫筆顏色設置為灰色
 		//繪製右側灰色條
 		canvas.drawRect
 		(
 				Constant.GAME_VIEW_X+Constant.GAME_VIEW_WIDTH+1,						//left, 
 				Constant.GAME_VIEW_Y,												//top, 
 				Constant.GAME_VIEW_X+Constant.GAME_VIEW_WIDTH+Constant.RIGHT_BAR,	//right, 
 				Constant.GAME_VIEW_Y+Constant.GAME_VIEW_HEIGHT,						//bottom, 
 				paint
 		);
 		paint.setColor(Color.YELLOW);	//將畫筆顏色設置為黃色
 		paint.setTextSize(13);		//設置字體大小
 		
 		drawOneDataMessageHP(5,"得分",score,canvas,paint);
 		drawOneDataMessageHP(3,"擊毀",countTankDestoryed,canvas,paint);
 		drawOneDataMessageHP(4,"英雄",Hero.HEROLIFE,canvas,paint);
 		drawOneDataMessageHP(1,"關卡",map.getMissionNum(),canvas,paint);
 		drawOneDataMessageHP(2,"敵坦克",Constant.TANK_TOTAL_NUM,canvas,paint);
 	}
 	void drawOneDataMessageHP(int order,String msg,int number,Canvas canvas,Paint paint)
 	{
 		//繪製英雄數量		
 		canvas.drawText
 		(
 				msg, 
 				Constant.GAME_VIEW_X+Constant.GAME_VIEW_WIDTH+Constant.RIGHT_BAR+2, 
 				Constant.GAME_VIEW_Y+Constant.FIRST_MESSAGE_HEIGHT+(order-1)*Constant.HANZI_HEIGHT+(order-1)*Constant.NUMBER_HEIGHT, 
 				paint
 		);
 		String numberStr=number+"";
 		for(int i=0;i<numberStr.length();i++)
 		{
 			char c=numberStr.charAt(i);
 			canvas.drawBitmap
 			(
 					numbers[c-'0'], 
 					Constant.GAME_VIEW_X+SCREEN_WIDTH-Constant.NUMBER_WIDTH*(numberStr.length()-i), 
 					Constant.GAME_VIEW_Y+Constant.FIRST_MESSAGE_HEIGHT+order*Constant.HANZI_HEIGHT+(order-1)*Constant.NUMBER_HEIGHT, 
 					paint
 			);
 		}
 	}
 	//畫虛擬按鈕的方法
 	void drawVirtualButtonHP(Canvas canvas,Paint paint)
 	{
 		//畫虛擬方向鍵圖片
 		canvas.drawBitmap
 		(
 				AliveWallPaperTank.controlBitmap, 
 				Constant.BUTTON_X,
 				Constant.BUTTON_Y, 
 				paint
 		);
// 		//用有色矩形標記可觸控區域
// 		paint.setColor(Color.BLUE);
// 		drawColorRect
// 		(
// 				canvas,paint,
// 				Constant.UP_X, Constant.UP_Y,Constant.BUTTON_WIDTH, Constant.BUTTON_HEIGHT
// 		);
// 		drawColorRect
// 		(
// 				canvas,paint,
// 				Constant.DOWN_X, Constant.DOWN_Y,Constant.BUTTON_WIDTH, Constant.BUTTON_HEIGHT
// 		);
// 		drawColorRect
// 		(
// 				canvas,paint,
// 				Constant.LEFT_X, Constant.LEFT_Y,Constant.BUTTON_WIDTH, Constant.BUTTON_HEIGHT
// 		);
// 		drawColorRect
// 		(
// 				canvas,paint,
// 				Constant.RIGHT_X, Constant.RIGHT_Y,Constant.BUTTON_WIDTH, Constant.BUTTON_HEIGHT
// 		);
// 		//標記發射子彈的區域
// 		drawColorRect
// 		(
// 				canvas,paint,
// 				Constant.FIRE_BTN_X, Constant.FIRE_BTN_Y, Constant.FIRE_BTN_WIDTH, Constant.FIRE_BTN_HEIGHT
// 		);
 		//畫虛擬方向鍵圖片
 		canvas.drawBitmap
 		(
 				AliveWallPaperTank.controlBitmap, 
 				Constant.BUTTON_X,
 				Constant.BUTTON_Y, 
 				paint
 		);
 		switch(keyState)
 		{
 			case 0x1:	//上
 			{
 				//畫紅點
 				canvas.drawBitmap
 				(
 						AliveWallPaperTank.redDotBitmap, 
 						Constant.RED_DOT_CENTER_X,
 						Constant.RED_DOT_CENTER_Y-Constant.BUTTON_HEIGHT, 
 						paint
 				);
 			}
 			break;
 			case 0x2:	//下
 			{
 				//畫紅點
 				canvas.drawBitmap
 				(
 						AliveWallPaperTank.redDotBitmap, 
 						Constant.RED_DOT_CENTER_X,
 						Constant.RED_DOT_CENTER_Y+Constant.BUTTON_HEIGHT, 
 						paint
 				);
 			}
 			break;
 			case 0x4:	//左
 			{
 				//畫紅點
 				canvas.drawBitmap
 				(
 						AliveWallPaperTank.redDotBitmap, 
 						Constant.RED_DOT_CENTER_X-Constant.BUTTON_WIDTH,
 						Constant.RED_DOT_CENTER_Y, 
 						paint
 				);
 			}
 			break;
 			case 0x8:	//右
 			{
 				//畫紅點
 				canvas.drawBitmap
 				(
 						AliveWallPaperTank.redDotBitmap, 
 						Constant.RED_DOT_CENTER_X+Constant.BUTTON_WIDTH,
 						Constant.RED_DOT_CENTER_Y, 
 						paint
 				);
 			}
 			break;
 			case 0:
 			{
 				//畫紅點
 				canvas.drawBitmap
 				(
 						AliveWallPaperTank.redDotBitmap, 
 						Constant.RED_DOT_CENTER_X,
 						Constant.RED_DOT_CENTER_Y, 
 						paint
 				);
 			}
 			break;
 		}//switch
 		//畫發射圖片
 		if(fireButtonDownFlag)
 		{
 			canvas.drawBitmap
 			(
 					AliveWallPaperTank.fireBtnDownBitmap, 
 					Constant.FIR_MAP_X,
 					Constant.FIR_MAP_Y, 
 					paint
 			);
 		}
 		else
 		{
 			canvas.drawBitmap
 			(
 					AliveWallPaperTank.fireBtnUpBitmap, 
 					Constant.FIR_MAP_X,
 					Constant.FIR_MAP_Y, 
 					paint
 			);
 		}
 	}
 	//=============================================== HP ====== end =======
 	//按比例畫矩形的方法
 	void drawColorRect
 	(
 			Canvas canvas,Paint paint,
 			float xLeftTop,float yLeftTop,float length,float width		//坐標值在0到1之間
 	)
 	{
 		canvas.drawRect
 		(
 				xLeftTop,
 				yLeftTop,
 				(xLeftTop+length),
 				(yLeftTop+width),
 				paint
 		);
 	}
 	//初始化所有數據的方法
 	void initAllData()
 	{
 		//要先初始化常量，再初始化地圖數據！
 		Constant.initConst();//初始化常量
 		map=new BattleMap();//創建地圖對像
 		map.intiMapData();//初始化地圖數據		
 		hero=new Hero(AliveWallPaperTank.heroTanki1);//創建英雄坦克對像(位置與常量有關，所以要放在初始化常量和數據後)
 		hero.backHome();//英雄回家
 		//初始化管理列表
 		alTank=new ArrayList<Tank>();		
 		alHeroBullet=new Vector<HeroBullet>();	
 		alBullet=new ArrayList<Bullet>();
 		//恢復初值
 		score=0;
 		countTankDestoryed=0;
 		Hero.HEROLIFE=Constant.HERO_LIFE;
 		map.setMissionNum(1);
 		AliveWallPaperTank.map.reward=null;//清空獎勵物
 		//恢復線程標誌
 		AliveWallPaperTank.gameOverFlag=false;
 		AliveWallPaperTank.heroGoFlag=true;
 		AliveWallPaperTank.TankGeneratorFlag=true;
 		AliveWallPaperTank.TankGoFlag=true;
 		AliveWallPaperTank.TankChangeDirectionFlag=true;
 		AliveWallPaperTank.heroBulletGoFlag=true;
 		AliveWallPaperTank.tankSendBulletFlag=true;
 		AliveWallPaperTank.tankBulletGoFlag=true;
 		//創建線程
 		generator=new TankGeneratorThread();
 		go=new TankGoThread();
 		heroGo=new HeroGoThread();
 		changeDirection=new TankChangeDirectionThread();
 		heroBulletGo=new HeroBulletGoThread();		
 		tankSendBullet=new TankSendBulletThread();
 		tankBulletGo=new TankBulletGoThread();
 		//啟動線程
 		generator.start();
 		go.start();
 		heroGo.start();
 		changeDirection.start();
 		heroBulletGo.start();
 		tankSendBullet.start();
 		tankBulletGo.start();
 	}
 	//停止所有線程的方法
 	void stopAllThreads()
 	{
 		//將所有控制線程的標誌設為false
  		AliveWallPaperTank.heroGoFlag=false;
  		AliveWallPaperTank.TankGeneratorFlag=false;
  		AliveWallPaperTank.TankGoFlag=false;
  		AliveWallPaperTank.TankChangeDirectionFlag=false;
  		AliveWallPaperTank.heroBulletGoFlag=false;
  		AliveWallPaperTank.tankSendBulletFlag=false;
  		AliveWallPaperTank.tankBulletGoFlag=false;
 	}
  }
}
