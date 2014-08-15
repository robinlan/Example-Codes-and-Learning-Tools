package com.bn.helper;
import android.view.MotionEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import android.graphics.*;

//用於繪製導航路線圖的Overlay
class MyNavigateOverlay extends Overlay
{    
	
	static int DWidth=20;
	static int DHeight=29;
	
	GeoPoint[] points;//路線中節點經緯度數組
    Paint paint;//畫筆

    double direction;//方向角    
    int StartIn;//當前子路徑起點索引
    int step;//當前子路徑步數索引
    int total;//當前子路徑總步數
    GeoPoint gpCurr;//導航三角形當前位置
    
	public MyNavigateOverlay(GeoPoint[] points)
	{
		this.points=points;	
		paint = new Paint();//創建畫筆
		paint.setAntiAlias(true);//打開抗鋸齒
		paint.setARGB(90,0,0,255);;//設置畫筆顏色
		paint.setStrokeWidth(5);//設置路線繪製寬度
	}
    
	@Override 
    public boolean onTouchEvent(MotionEvent event, MapView mv) 
	{//不接收處理觸控事件
		return false;
	}
	
    @Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
    	
    	//循環繪製導航路徑
    	for(int i=0;i<points.length-1;i++)
    	{
    		//取出每條子路徑的起點、終點
    		Point Start=getPoint(mapView,points[i]);
    		Point End=getPoint(mapView,points[i+1]);
    			
    		paint.setARGB(160,0,0,255);;//設置畫筆顏色
    		canvas.drawLine(Start.x, Start.y, End.x, End.y, paint);
    	}
    	
    		
    	if(MapNavigateActivity.ison&total!=0)
    	{
    		
            GeoPoint gp=points[StartIn];
            
            GeoPoint gpNext=points[StartIn+1];
            
            gpCurr=new GeoPoint
            (
                  gp.getLatitudeE6()+(gpNext.getLatitudeE6()-gp.getLatitudeE6())*step/total,
                  gp.getLongitudeE6()+(gpNext.getLongitudeE6()-gp.getLongitudeE6())*step/total
            );
            
            Point pCurr=getPoint(mapView,gpCurr); 
            
           
            if(pCurr.x<Constant.BORDER_WIDTH||pCurr.x>Constant.MAP_VIEW_WIDTH-Constant.BORDER_WIDTH||
               pCurr.y<Constant.BORDER_WIDTH||pCurr.y>Constant.MAP_VIEW_HEIGHT-Constant.BORDER_WIDTH)
            {
               mapView.getController().setCenter(gpCurr);            	
            }
            
            //重繪地圖            
            mapView.postInvalidate();
            //拿到導航三角形的XY坐標
			float dnX=pCurr.x;
			float dnY=pCurr.y;
			
    		Matrix m1=new Matrix();
    		m1.setRotate((float)direction,dnX,dnY);	  
    		Matrix m2=new Matrix();
    		m2.setTranslate(dnX-DWidth/2,dnY-DHeight/2);
    		Matrix mz=new Matrix();
    		mz.setConcat(m1, m2);    	
    		paint.setARGB(200,0,0,255);;//設置畫筆顏色
    		canvas.drawBitmap(MapNavigateActivity.bitmapDirection, mz, paint);
    	}
    	
    	//調用父類繪製
    	super.draw(canvas, mapView, shadow);
	}
    
    public Point getPoint(MapView mapView,GeoPoint gp)
    {//將經緯度翻譯成屏幕上的XY坐標
    	Projection projettion = mapView.getProjection();
		Point p = new Point();
		projettion.toPixels(gp, p); 
		return p;
    }
    
    //根據當前的節點計算導航三角形方向角 
    public void calDirection(int dIndex, MapView mapView)
    {
    	StartIn=dIndex;    	
    	Point dp=getPoint(mapView,points[dIndex]);   
		if(dIndex<points.length-1)
		{//沒有到最後一個點需要計算方向
			Point dpNext=getPoint(mapView,points[dIndex+1]); 					
			float dx=dpNext.x-dp.x;
			float dy=-(dpNext.y-dp.y);

			int c=1;
			//若下一個點與此點重合，則再取下一個點
            while(dx==0&&dy==0)
            {
            	c++;
            	dpNext=getPoint(mapView,points[dIndex+c]); 
            	dx=dpNext.x-dp.x;
    			dy=-(dpNext.y-dp.y);
            }
            
			if(dx!=0||dy!=0)
			{
				if(dx>0&&dy>0)
    			{//第一象限
					direction=Math.toDegrees(Math.atan(dx/dy));
    			}
				else if(dx<0&&dy>0)
				{//第二象限
					direction=360-Math.toDegrees(Math.atan(-dx/dy));
				} 
				else if(dx<0&&dy<0)
				{//第三象限
					direction=180+Math.toDegrees(Math.atan(dx/dy));
				}
				else if(dx>0&&dy<0)
				{//第四象限
					direction=180-Math.toDegrees(Math.atan(dx/-dy));
				}	
				else if(dx==0)
				{
					if(dy>0)
					{
						direction=0;
					}
					else
					{
						direction=180;
					}
				}
				else if(dy==0)
				{
					if(dx>0)
					{
						direction=90;
					}
					else
					{
						direction=270;
					}
				}
			}
		}
		
    }
}



