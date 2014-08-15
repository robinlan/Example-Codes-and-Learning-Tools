package com.bn.map;			//聲明包語句
import java.util.Vector;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static com.bn.map.Constant.*;
public class GameView extends SurfaceView implements SurfaceHolder.Callback{
	MapMasetActivity activity;		//Activity引用
	Canvas c;
	SurfaceHolder holder;
    int scoreWidth = 10;
    int guanshuX;//關數文字X坐標
    int guanshuY;//關數文字Y坐標
    int guanshu=1;
    Bitmap iback;//背景圖
    Bitmap[] iscore=new Bitmap[10];//得分圖
    Bitmap JianHaotupian;//減號圖
    Bitmap JiaHaotupian;//加號圖
    Bitmap[] guanShu=new Bitmap[10];//關數圖
    Bitmap time_wz;//時間文字圖
    Bitmap gread_wz;//成績文字圖
    Bitmap hengXian;//橫線
	public GameView(MapMasetActivity activity) {
		super(activity);
		getHolder().addCallback(this);//註冊回調接口
		this.activity = activity;
		initBitmap();
	}
	//將圖片加載
	public void initBitmap(){
		iback = BitmapFactory.decodeResource(getResources(), R.drawable.main);
		iscore[0] = BitmapFactory.decodeResource(getResources(), R.drawable.d0);//數字圖
		iscore[1] = BitmapFactory.decodeResource(getResources(), R.drawable.d1);
		iscore[2] = BitmapFactory.decodeResource(getResources(), R.drawable.d2);
		iscore[3] = BitmapFactory.decodeResource(getResources(), R.drawable.d3);
		iscore[4] = BitmapFactory.decodeResource(getResources(), R.drawable.d4);
		iscore[5] = BitmapFactory.decodeResource(getResources(), R.drawable.d5);
		iscore[6] = BitmapFactory.decodeResource(getResources(), R.drawable.d6);
		iscore[7] = BitmapFactory.decodeResource(getResources(), R.drawable.d7);
		iscore[8] = BitmapFactory.decodeResource(getResources(), R.drawable.d8);
		iscore[9] = BitmapFactory.decodeResource(getResources(), R.drawable.d9);
		
		guanShu[0] = BitmapFactory.decodeResource(getResources(), R.drawable.guanka);//關卡圖
		guanShu[1] = BitmapFactory.decodeResource(getResources(), R.drawable.guanka1);
		guanShu[2] = BitmapFactory.decodeResource(getResources(), R.drawable.guanka2);
		guanShu[3] = BitmapFactory.decodeResource(getResources(), R.drawable.guanka3);
		guanShu[4] = BitmapFactory.decodeResource(getResources(), R.drawable.guanka4);
		guanShu[5] = BitmapFactory.decodeResource(getResources(), R.drawable.guanka5);
		JiaHaotupian = BitmapFactory.decodeResource(getResources(), R.drawable.right);
		JianHaotupian = BitmapFactory.decodeResource(getResources(), R.drawable.left);
		gread_wz = BitmapFactory.decodeResource(getResources(), R.drawable.grade);//成績文字
		time_wz= BitmapFactory.decodeResource(getResources(), R.drawable.time);//時間文字
		hengXian=BitmapFactory.decodeResource(getResources(), R.drawable.hengxian);//橫線
	}
	@Override
	protected void onDraw(Canvas canvas) 
	{
		super.onDraw(canvas); 
		canvas.drawColor(Color.argb(255, 0, 0, 0));
		canvas.drawBitmap(iback,30,0, null);//畫背景
		//繪製減號和加號圖片
		canvas.drawBitmap(JianHaotupian,SCREEN_WIDTH/6,SCREEN_HEIGHT/6+40, null);	
		//繪製關卡文字
		canvas.drawBitmap(guanShu[guanshu-1],SCREEN_WIDTH/2-60,SCREEN_HEIGHT/6+40, null);
		//繪製右邊加號
		canvas.drawBitmap(JiaHaotupian,SCREEN_WIDTH/2+80,SCREEN_HEIGHT/6+40, null);
		//繪製成績文字gread_wz
		canvas.drawBitmap(gread_wz,SCREEN_WIDTH/6,SCREEN_HEIGHT/6+63, null);
		//繪製遊戲時間文字
		canvas.drawBitmap(time_wz,SCREEN_WIDTH/2+80,SCREEN_HEIGHT/6+63, null);
		String sql_select="select grade,time from rank where level="+guanshu+" order by grade desc limit 0,5;";
    	Vector<Vector<String>> vector=SQLiteUtil.query(sql_select);//從數據庫中取出相應的數據
    	for(int i=0;i<vector.size();i++)//循環繪製排行榜的分數和對應時間
    	{
    		drawScoreStr(canvas,vector.get(i).get(0).toString(),SCREEN_WIDTH/6,SCREEN_HEIGHT/6+40+60+i*30);//成績，日期
    		drawRiQi(canvas,vector.get(i).get(1).toString(),SCREEN_WIDTH/2+65,SCREEN_HEIGHT/6+40+60+i*30);
    	}
	}
	public void drawScoreStr(Canvas canvas,String s,int width,int height)//繪製字符串方法
	{
    	//繪製得分
    	String scoreStr=s; 
    	for(int i=0;i<scoreStr.length();i++){//循環繪製得分
    		int tempScore=scoreStr.charAt(i)-'0';
    		canvas.drawBitmap(iscore[tempScore], width+i*scoreWidth,height, null);
    		}
	}
	public void drawRiQi(Canvas canvas,String s,int width,int height)//畫年月
	{
		String ss[]=s.split("-");//切割得到年月日
		drawScoreStr(canvas,ss[0],width,height);//畫年數數字
		canvas.drawBitmap(hengXian,width+scoreWidth*4,height, null);//畫橫線
		drawScoreStr(canvas,ss[1],width+scoreWidth*5,height);//畫月數數字
		canvas.drawBitmap(hengXian,width+scoreWidth*7,height, null);//畫橫線
		drawScoreStr(canvas,ss[2],width+scoreWidth*8,height);//畫日數字
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		int x = (int) event.getX();
		int y = (int) event.getY();
		if(x>SCREEN_WIDTH/6&&x<SCREEN_WIDTH/6+60&&
				y>SCREEN_HEIGHT/6+40&&y<SCREEN_HEIGHT/6+40+40)
		{			
			if(guanshu>1)
			{
				guanshu--;
				c = null;
	            try {
	            	// 鎖定整個畫布，在內存要求比較高的情況下，建議參數不要為null
	                c = holder.lockCanvas(null);
	                synchronized (holder) {
	                	onDraw(c);//繪製
	                }
	            } finally {
	                if (c != null) {
	                	//並釋放鎖
	                	holder.unlockCanvasAndPost(c);
	                }
	            }
			}
		}
		if(x>SCREEN_WIDTH/2+80&&x<SCREEN_WIDTH/2+140
				&&y>SCREEN_HEIGHT/6+40&&y<SCREEN_HEIGHT/6+80){			
			if(guanshu<MAPP.length)
			{
				guanshu++;
				c = null;
	            try {
	            	// 鎖定整個畫布，在內存要求比較高的情況下，建議參數不要為null
	                c = holder.lockCanvas(null);
	                synchronized (holder) {
	                	onDraw(c);//繪製
	                }
	            } finally {
	                if (c != null) {
	                	//並釋放鎖
	                	holder.unlockCanvasAndPost(c);
	                }
	            }
			}
		}		
		return super.onTouchEvent(event);
	}
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
	public void surfaceCreated(SurfaceHolder holder) {//創建時啟動相應進程		
		this.holder=holder;        
            c = null;
            try {
            	// 鎖定整個畫布，在內存要求比較高的情況下，建議參數不要為null
                c = holder.lockCanvas(null);
                synchronized (holder) {
                	onDraw(c);//繪製
                }
            } finally {
                if (c != null) {
                	//並釋放鎖
                	holder.unlockCanvasAndPost(c);
                }
            }
	}
	public void surfaceDestroyed(SurfaceHolder holder) {//摧毀時釋放相應進程
	}
}

