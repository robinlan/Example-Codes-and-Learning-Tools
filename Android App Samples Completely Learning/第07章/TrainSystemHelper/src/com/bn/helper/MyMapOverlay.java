package com.bn.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.bn.helper.MyBallonOverlay;
import static com.bn.helper.Constant.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

//覆蓋整個地圖捕捉觸控事件的透明OverLay
class MyMapOverlay extends Overlay{
	boolean flagMove=false;//是否為移動動作標誌位
	static int nState=0; //狀態標誌為 0表示開始狀態 1表示已經設置起始點的狀態
	static GeoPoint gpStart;//起始點經緯度
	static GeoPoint gpEnd;//結束點經緯度
	
	float previousX;//上次觸控的X坐標
	float previousY;//上次觸控的Y坐標
	
	static final int MOVE_THRESHOLD=10;//當觸控點移動範圍不大於閾值時還認為是點擊
	static Bitmap bitmapStart;
	 @Override 
	    public boolean onTouchEvent(MotionEvent event, MapView mv) {
	        if(event.getAction() == MotionEvent.ACTION_MOVE)
	        {//若移動了觸控筆則設置移動標誌為true
	        	if(flagMove!=true)
	        	{
	        		float dx=Math.abs(event.getX()-previousX);
	        		float dy=Math.abs(event.getY()-previousY);	        		
	        		if(dx>MOVE_THRESHOLD||dy>MOVE_THRESHOLD)
	        		{
	        			flagMove=true;
	        		}
	        	}
	        }
	        else if(event.getAction() == MotionEvent.ACTION_DOWN)
	        {//若按下了觸控筆則設置移動標誌為false，並記錄按下觸控筆的位置
	        	flagMove=false;
	        	previousX=event.getX();
	        	previousY=event.getY();
	        }
	        else if (MyBallonOverlay.currentPIC==null&&
	        		 !flagMove&&
	        		 event.getAction() == MotionEvent.ACTION_UP ) 
	        {
	        	Vector<String> ve=new Vector<String>();
	        	ve=DBUtil.searchjwsn();	        	
	        	GeoPoint gp = mv.getProjection().fromPixels
	            (
	        		 (int) event.getX(),  //觸控筆在屏幕上的X坐標
	        		 (int) event.getY()   //觸控筆在屏幕上的Y坐標
	            );	            
	        	//顯示點擊處的經緯度
	            String latStr=Math.round(gp.getLatitudeE6()/1000.00)/1000.0+"";//緯度
	        	String longStr=Math.round(gp.getLongitudeE6()/1000.00)/1000.0+"";//經度
	        	
	        	
	        	for(int n=0;n<ve.size()-1;n=n+3)
	        	{
	        		if(TrainOverlay.calculationdistance(longStr, latStr, ve.get(n).toString(), ve.get(n+1).toString())<XIU_ZHENG)
	        		{
	        			double jj=Double.parseDouble(ve.get(n));  //轉化數據
	        	        double ww=Double.parseDouble(ve.get(n+1));  //轉化為數據
	        	        GeoPoint gpp = new GeoPoint
	        	        (
	        	        		(int)(ww*1E6),  //緯度
	        	        		(int)(jj*1E6)  //經度
	        	        );
	        			
	    	        	List<Overlay> overlays = mv.getOverlays(); 
	    	        	int i=0;
	    	        	for(Overlay ol:overlays)
	    	        	{//清除其他氣球的showWindow標記
	    	        				i++;	    	        		
	    	        	} 
	    	        	if(i<6)
	    	        	{
	    	        		MyBallonOverlay mbo=new MyBallonOverlay
	    		        	(
	    		        			gpp,		//氣球的坐標
	    		        			ve.get(n+2).toString()+"\n經度："+longStr+"\n緯度："+latStr, //氣球的信息
	    		        			MapNavigateActivity.bitmapEnd
	    		        	);
	    	        		gpEnd=gp;
	    	        		mbo.showWindow=true;
	    		            overlays.add(mbo); 
	    	        	}
	    	        	if(i>=6)
	    	        	{	    	        		
	    	        	   	Overlay first1=overlays.get(0);
	    	        	   	Overlay first2=overlays.get(1);
	    	        	   	Overlay first3=overlays.get(2);
	    	        	   	Overlay first4=overlays.get(3);
	    	        	   	Overlay first5=overlays.get(4);
	    	        		List<Overlay> tol=new ArrayList<Overlay>();
	    	        		for(int j=5;j<overlays.size();j++)
	    	        		{
	    	        			tol.add(overlays.get(j));
	    	        		}
	    	        		//清空所有Overlay
	    	        		overlays.clear();
	    	        		//添加用於獲取全局觸控事件的透明Overlay
	    	        		overlays.add(first1);
	    	        		overlays.add(first2);
	    	        		overlays.add(first3);
	    	        		overlays.add(first4);
	    	        		overlays.add(first5);
	    	        		MyBallonOverlay mbo=new MyBallonOverlay
	    		        	(
	    		        			gpp,		//氣球的坐標
	    		        			ve.get(n+2).toString()+"\n經度："+longStr+"\n緯度："+latStr, //氣球的信息
	    		        			MapNavigateActivity.bitmapEnd
	    		        	);
	    	        		gpEnd=gpp;
	    	        		mbo.showWindow=true;
	    		            overlays.add(mbo); 
	    	        	}	        	
	        		}//"當前站點：\n"+ve.get(n+2).toString()'''"地理坐標為：\n經度："+longStr+"\n緯度："+latStr
	        	}	        		        	
	            return true;
	        }
	        return false;
	    }

}
