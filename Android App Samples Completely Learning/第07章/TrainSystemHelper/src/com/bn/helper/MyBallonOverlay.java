package com.bn.helper;

import java.util.ArrayList;
import java.util.List;

import android.view.MotionEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import android.graphics.*;

//表示氣球的Overlay
class MyBallonOverlay extends Overlay{
	final static int picWidth=33;  //氣球圖的寬度
	final static int picHeight=34; //氣球圖的高度
	final static int R=8;//信息窗口的圓角半徑
	
	static MyBallonOverlay currentPIC=null;//表示當前選中的氣球
	String msg;	//此氣球對應的文字信息
	Bitmap bm;//對應的圖標
	
	boolean showWindow=false;//是否顯示文字信息窗口的標誌位     為true顯示文字信息窗口
	
	GeoPoint gp;//此氣球對應的經緯度 
   
	public MyBallonOverlay(GeoPoint gp,String msg,Bitmap bm)
	{
		this.gp=gp;
		this.msg=msg;
		this.bm=bm;
	}
	
    @Override 
    public boolean onTouchEvent(MotionEvent event, MapView mv) {
        if(currentPIC!=null&&currentPIC!=this)
        {
        	return false;
        }
    	
    	if(event.getAction() == MotionEvent.ACTION_DOWN)
        {    	
        	int x=(int)event.getX();
            int y=(int)event.getY();
            Point p= getPoint(mv); 
            
            int xb=p.x-picWidth/2;
            int yb=p.y-picHeight;
            
            if(x>=xb&&x<xb+picWidth&&y>=yb&&y<yb+picHeight)
            {    	
            	currentPIC=this;
            	return true;
            }
        }
    	else if (event.getAction() == MotionEvent.ACTION_MOVE) 
    	{
    		return currentPIC!=null;
    	}    		
        else if (event.getAction() == MotionEvent.ACTION_UP) 
        {
        	//獲取觸控筆位置
            int x=(int)event.getX();
            int y=(int)event.getY();
            
            //獲取氣球在屏幕上的坐標範圍
            Point p= getPoint(mv);             
            int xb=p.x-picWidth/2;
            int yb=p.y-picHeight;           
            
            if(currentPIC==this&&x>=xb&&x<xb+picWidth&&y>=yb&&y<yb+picHeight)
            {
            	//顯示完內容後清空當前氣球
            	currentPIC=null;     
            	showWindow=!showWindow;
            	
            	List<Overlay> overlays = mv.getOverlays();
            	overlays.remove(this);//刪除此氣球再添加
            	overlays.add(this);//此氣球就位於最上面了
            	return true;
            }
            else if(currentPIC==this)
            {//若當前氣球為自己但抬起觸控不再自己上則清空氣球狀態並返回true
            	currentPIC=null;
            	return true;            	
            }            
        }
        return false;
    }
    
    @Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {			   	
    	Point p= getPoint(mapView);  	    	
		canvas.drawBitmap(bm, p.x-picWidth/2+3, p.y-picHeight-12, null);
		
		if(showWindow)
		{
			drawWindow(canvas,p,170);
		}
		
		//調用父類繪製
		super.draw(canvas, mapView, shadow);
	}
    
    public Point getPoint(MapView mapView)
    {//將經緯度翻譯成屏幕上的XY坐標
    	Projection projettion = mapView.getProjection();
		Point p = new Point();
		projettion.toPixels(gp, p); 
		return p;
    }
    
	public void drawWindow(Canvas canvas,Point p,int winWidth) 
	{//繪製信息窗口的方法
		int singleSize=15;
		int textSize=16;
		int leftPadding=10;
		
		//為信息分行
		int line_Width=winWidth-leftPadding*2;//每行的寬度
		int lineCharCount=line_Width/(singleSize);	//每行字符數
		//掃瞄整個信息字符串進行分行
		ArrayList<String> totalRows=new ArrayList<String>();//記錄所有行的ArrayList
		String currentRow="";//當前行的字符串
		for(int i=0;i<msg.length();i++)
		{			
			char c=msg.charAt(i);
			if(c!='\n')
			{//若當前字符不為換行符則加入到當前行中
				currentRow=currentRow+c;
			}
			else
			{
				if(currentRow.length()>0)
				{
					totalRows.add(currentRow);
				}				
				currentRow="";//清空當前行
			}
			if(currentRow.length()==lineCharCount)	
			{//若當前行的長度達到一行規定的字符數則
		     //?�堇騛v屑尤爰鍬妓�畢瘙騏漷rayList
				totalRows.add(currentRow);
				currentRow="";//清空當前行
			}
		}
		if(currentRow.length()>0)
		{//若當前行長度大於零則將當前行加入記錄所有行的ArrayList
			totalRows.add(currentRow);
		}
		int lineCount=totalRows.size();//獲得總行數
		int winHeight=lineCount*(singleSize)+2*R;//自動計算信息窗體高度
		//創建paint對像
		Paint paint=new Paint();
		paint.setAntiAlias(true);//打開抗鋸齒
		paint.setTextSize(textSize);//設置文字大小	
		//繪製 信息窗體圓角矩形
		paint.setARGB(255, 255,251,239);
		int x1=p.x-winWidth/2;
		int y1=p.y-picHeight-winHeight-12;
		int x2=p.x+winWidth/2;
		int y2=p.y-picHeight-12;		
		RectF r=new RectF(x1,y1,x2,y2);		
		canvas.drawRoundRect(r, R, R, paint);
		//繪製 信息窗體圓角矩形邊框
		paint.setARGB(255,198,195,198);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		canvas.drawRoundRect(r, R, R, paint);
		
		//循環繪製每行文字
		paint.setStrokeWidth(0);
		paint.setARGB(255, 10, 10, 10);		
		int lineY=y1+R+singleSize;
		for(String lineStr:totalRows)
		{//對每一行進行循環	
			for(int j=0;j<lineStr.length();j++)
			{//對一行中的每個字循環
				String colChar=lineStr.charAt(j)+"";				
				canvas.drawText(colChar, x1+leftPadding+j*singleSize, lineY, paint);
			}
			lineY=lineY+singleSize;//y坐標移向下一行
		}
	}
}



