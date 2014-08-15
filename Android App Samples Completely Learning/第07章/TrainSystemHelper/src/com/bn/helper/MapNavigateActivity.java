package com.bn.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.w3c.dom.Document;
import com.bn.helper.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;


public class MapNavigateActivity extends MapActivity {
	
	static Bitmap bitmapStart;//起點氣球圖片	 
	static Bitmap bitmapEnd;//終點氣球圖片
	static Bitmap bitmapDirection;//導航箭頭氣球圖片
	static Bitmap station;
	static Bitmap smallstation;
	static boolean inQuery=false;//正在查詢中標誌位
	static boolean ison=false;//是否繪製導航箭頭標誌
	
	static Handler hd;
	static String msg;
	
	MapController mc;//地圖控制器
	MyNavigateOverlay  currMyNa;//當前的導航Overlay 	
	GeoPoint gpLocation;
	boolean flag=false;//是否顯示了自己位置
	
	boolean yyhh=false;//動畫允許標誌位
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //設置為橫屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        //下兩句為設置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        
        Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();        
        String jdStr=bundle.getString("jdStr");//"118.190088";
        String wdStr=bundle.getString("wdStr");//"39.637877";
        String msg=bundle.getString("msg");//"華潤萬家\n中國河北省唐山市路南區文化路";
        double jd=Double.parseDouble(jdStr);
        double wd=Double.parseDouble(wdStr);
        //目標商店的經緯度
        GeoPoint gp = new GeoPoint
        (
        		(int)(wd*1E6), //緯度
        		(int)(jd*1E6) //經度
        );
               
       
        new Thread()
        {
        	public void run()
        	{
        		Looper.prepare();
        		
        		hd=new Handler()
                {
                	@Override
                	public void handleMessage(Message msg)
                	{
                		super.handleMessage(msg);
                		
                		switch(msg.what)
                		{
                		   case Constant.DISPLAY_TOAST:
                			Toast.makeText
                       		(
                       			MapNavigateActivity.this, 
                       			MapNavigateActivity.msg, 
                       			Toast.LENGTH_SHORT
                       		).show();
                		   break;
                		}
                	}
                };        		        		
        		Looper.loop();
        	}
        }.start();
        //初始化自定義消息處理器及其消息循環線程============end===========
        
        
        setContentView(R.layout.map);
        //初始化氣球圖片
        bitmapStart= BitmapFactory.decodeResource(this.getResources(), R.drawable.people);
        bitmapEnd= BitmapFactory.decodeResource(this.getResources(), R.drawable.cart);
        bitmapDirection=BitmapFactory.decodeResource(this.getResources(), R.drawable.carl);
        station=BitmapFactory.decodeResource(this.getResources(), R.drawable.tubiao);
        smallstation=BitmapFactory.decodeResource(this.getResources(), R.drawable.xiaotubiao);
        
        //對地圖進行初始化      
        final MapView mv=(MapView)findViewById(R.id.myMapView);
        mv.setBuiltInZoomControls(true);//設置地圖上要縮放控制條
        mc=mv.getController();//獲取地圖控制器
        mc.setZoom(14);//設置地圖縮放比例        
        //設置地圖中心點經緯度
        mc.animateTo(gp);
        
        //給地圖添加一個完全透明的Overlay用於捕捉觸控事件
       MyMapOverlay myOverlay = new MyMapOverlay();
       List<Overlay> overlays = mv.getOverlays();
        overlays.add(myOverlay); 
        
////        //============================================begin====
        overlays = mv.getOverlays(); 
    	Overlay first=overlays.get(0);
    	overlays.clear();
    	overlays.add(first);
    	
////    	
        
        
    	 //將用於獲取全局觸控事件的透明Overlay拿到
        MapView mv1=(MapView)findViewById(R.id.myMapView);
        List<Overlay> overlays1 = mv.getOverlays(); 
        Overlay first1=overlays1.get(0);
        //將其他的Overlay記錄到輔助列表中
		List<Overlay> tol=new ArrayList<Overlay>();
		for(int i=1;i<overlays1.size();i++)
		{
			tol.add(overlays1.get(i));
		}
		//清空所有Overlay
		overlays1.clear();
		//添加用於獲取全局觸控事件的透明Overlay
		overlays1.add(first1);
        //============================================end=======
    	
         if(TrainSystemHelperActivity.flag2==true||TrainSystemHelperActivity.str.equals("地鐵2號線"))
         {
        	 Vector<String> list2=new Vector<String>();         
             list2=DBUtil.inserttotemp("地鐵2號線");               
             list2.add(list2.get(0));
             list2.add(list2.get(1));                
             Vector<GeoPoint> pointt=new Vector<GeoPoint>();
             pointt=MiddleTrainOverl.getPoint1(list2);
             TrainOverlay mno=new TrainOverlay(pointt,178,178,255,"地鐵2號線"); 
             overlays1.add(mno); 
         }
         else 
         {
        	 NothingOverlay mno=new NothingOverlay();
        	 overlays1.add(mno); 
         }
         
         if(TrainSystemHelperActivity.flag1==true||TrainSystemHelperActivity.str.equals("地鐵1號線"))
         {
        	 Vector<String> list3=new Vector<String>();         
             list3=DBUtil.inserttotemp("地鐵1號線");                  
             Vector<GeoPoint> pointt3=new Vector<GeoPoint>();
             pointt3=MiddleTrainOverl.getPoint1(list3);
             TrainOverlay mno3=new TrainOverlay(pointt3,250,0,0,"地鐵1號線");
             overlays1.add(mno3);
         }
         else 
         {
        	 NothingOverlay mno3=new NothingOverlay();
        	 overlays1.add(mno3);
         }
         
         if(TrainSystemHelperActivity.flag3==true||TrainSystemHelperActivity.str.equals("地鐵13號線"))
         {
        	 Vector<String> list4=new Vector<String>();         
             list4=DBUtil.inserttotemp("地鐵13號線");                  
             Vector<GeoPoint> pointt4=new Vector<GeoPoint>();
             pointt4=MiddleTrainOverl.getPoint1(list4);
             TrainOverlay mno4=new TrainOverlay(pointt4,255,128,0,"地鐵13號線");
             overlays1.add(mno4);
         }
         else 
         {
        	 NothingOverlay mno4=new NothingOverlay();
        	 overlays1.add(mno4);
         }
         
         
         //圖片的overlay
         
        
       //添加行車路線Overlay
		
		
		
		StationPicture mnopicture=new StationPicture();
		overlays1.add(mnopicture);
       //添加其他Overlay
		for(Overlay o:tol)
		{
			overlays1.add(o);  
       	}
       //上述之所以執行那麼多動作是為了將行車路線Overlay
       //放到起點、終點氣球Overlay下面，否則遮擋視覺效果不對
     //在點擊位置添加起點氣球
      	MyBallonOverlay mbo=new MyBallonOverlay
       	(
       			gp,		//氣球的坐標
       			msg,  //氣球的信息
       			MapNavigateActivity.bitmapEnd
       	);               
      		mbo.showWindow=true;
           overlays.add(mbo);                 
           //記錄起點位置
           MyMapOverlay.gpEnd=gp;
       
       //切換狀態到初始狀態
       MapController mc=mv1.getController();//獲取地圖控制器
                                            
        //開啟一個線程延時獲取MapView的尺寸
        new Thread()
        {
        	public void run()
        	{
        		try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        		Constant.MAP_VIEW_WIDTH=mv.getWidth();
                Constant.MAP_VIEW_HEIGHT=mv.getHeight();
        	}
        }.start(); 
        
    ////獲取位置管理器實例
        String serviceName=Context.LOCATION_SERVICE;
        LocationManager lm=(LocationManager)this.getSystemService(serviceName);        
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = lm.getBestProvider(criteria, true);
        Location  tempLo= lm.getLastKnownLocation(provider);
        if(tempLo!=null)
        {
            gpLocation = new GeoPoint
            (
            		(int)(tempLo.getLatitude()*1E6), //緯度
            		(int)(tempLo.getLongitude()*1E6) //經度
            );
        }
        else
        {
        	Toast.makeText(this, "請打開GPS，並確保其正常工作！", Toast.LENGTH_LONG).show();
        }

        
       
        LocationListener ll=new LocationListener()
        {
			@Override
			public void onLocationChanged(Location location) 
			{
				gpLocation = new GeoPoint
	            (
	            		(int)(location.getLatitude()*1E6), //緯度
	            		(int)(location.getLongitude()*1E6) //經度
	            );	
			}

			@Override
			public void onProviderDisabled(String provider) 
			{//Location Provider被禁用時更新				
				
			}

			@Override
			public void onProviderEnabled(String provider) 
			{//Location Provider被啟用時更新					
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) 
			{
				//當Provider硬件狀態變化是更新
				
			}      	
        };
        
  
        lm.requestLocationUpdates
        (
        		LocationManager.GPS_PROVIDER, 	//使用GPS定位
        		2000, 							//時間分辨率 ms
        		5, 								//距離分辨率m
        		ll								//位置變化監聽器
        );
        Toast.makeText(this, "請按下菜單鍵操作，按下返回鍵返回！", Toast.LENGTH_LONG).show();
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	//對菜單進行初始化
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuItem ok=menu.add(0,0,0,"導航");
		//ok.setIcon(R.drawable.daohang); 
    	OnMenuItemClickListener lsn=new OnMenuItemClickListener()
    	{//實現菜單項點擊事件監聽接口
			@Override
			public boolean onMenuItemClick(MenuItem item) { 
				//若動畫已經在播放中，則返回
				if(MapNavigateActivity.ison==true)
				{
					Toast.makeText
					(
							MapNavigateActivity.this, 
							"導航進行中不能再次導航，\n請等動畫播放完畢再次啟動導航！", 
							Toast.LENGTH_LONG
					).show();
					return false;
				}	
				
				if(flag==false) 
				{
					Toast.makeText
					(
							MapNavigateActivity.this, 
							"請先選擇\"顯示自己位置\"菜單項\n地圖中顯示自己的位置再導航！", 
							Toast.LENGTH_LONG
					).show();
					return false;
				}
				
				//獲取地圖中所有Overlay的列表
				MapView mv=(MapView)findViewById(R.id.myMapView);
				List<Overlay> overlays = mv.getOverlays();
				for(Overlay o:overlays)
				{
					if(o instanceof MyNavigateOverlay)
					{						
						//拿到導航Overlay的引用
						currMyNa=(MyNavigateOverlay)o;	
						yyhh=true;
						new Thread()
						{
							public void run()
							{
								//獲取MapView
								MapView mv=(MapView)findViewById(R.id.myMapView);
								GeoPoint[] points=currMyNa.points;								
								for(int i=0;i<points.length-1;i++)
								{	
									if(yyhh==false)
									{										
										MapNavigateActivity.ison=false;
										break;
									}
									int dLat=points[i].getLatitudeE6()-points[i+1].getLatitudeE6();
									int dLon=points[i].getLongitudeE6()-points[i+1].getLongitudeE6();
									double distanceE6=Math.sqrt(dLat*dLat+dLon*dLon);
									int totalSteps=(int)(distanceE6/100);//100為每一步的長度單位
									//若當前子路徑總步數為0，則忽略
									if(totalSteps==0)
									{
										continue;
									}									
									//計算當前子路徑的方向角	
									currMyNa.calDirection(i, mv);
									currMyNa.total=totalSteps;	
									currMyNa.step=0;																		
									if(i==0)
									{
										mv.getController().animateTo(points[i]);
										MapNavigateActivity.this.sleep(2000);										         
							            mv.postInvalidate();
										MapNavigateActivity.ison=true;
									}									
									//循環走路當前子路徑中的每一步
									for(int s=0;s<totalSteps;s++)
									{
										currMyNa.step=s;										
										MapNavigateActivity.this.sleep(40);
									}										
								}
								//導航動畫完畢後恢復是否繪製導航箭頭標誌位為false
								MapNavigateActivity.ison=false;
							}
						}.start();
						break;
					}
				}				
				return true;
			}    		
    	};
    	ok.setOnMenuItemClickListener(lsn);//給確定菜單項添加監聽器 
    	
    	MenuItem dzjjwd=menu.add(0,0,0,"顯示自己位置");
    	
    	lsn=new OnMenuItemClickListener()
    	{//實現菜單項點擊事件監聽接口
			@Override
			public boolean onMenuItemClick(MenuItem item) {	 
				if(gpLocation==null)
				{
					Toast.makeText(MapNavigateActivity.this, "請打開GPS，並確保其正常工作！", Toast.LENGTH_LONG).show();
					return true;
				}
				
				if(MapNavigateActivity.ison==true)
				{
					yyhh=false;
				}	
				
				MyMapOverlay.gpStart=gpLocation;
				MapView mv=(MapView)findViewById(R.id.myMapView);
				List<Overlay> overlays = mv.getOverlays();    
				
				Overlay ovfd=null;
				for(Overlay ov:overlays)
				{
					if(ov instanceof MyNavigateOverlay)
					{
						ovfd=ov;
					}
				}
				overlays.remove(ovfd);
				
				
            	//在點擊位置添加起點氣球
            	MyBallonOverlay mbo=new MyBallonOverlay
            	(
            			MyMapOverlay.gpStart,		//氣球的坐標
            			"您的位置",  //氣球的信息
            			MapNavigateActivity.bitmapEnd
            	);  
            	mbo.showWindow=true;
                overlays.add(mbo);                 
                //切換狀態到起點設置好狀態
                flag=true;
                
                //將正在查詢中標誌位設置為true，準備開始查詢
                MapNavigateActivity.inQuery=true;
                //開啟一個線程從網絡上查詢從起點到終點的行車路徑
                new Thread()
                {
                	public void run()
                	{                		               		
                		//從網絡上獲取起點到終點行車路徑信息的XML文檔
                		Document doc=NavigateUtil.getPointsroute(MyMapOverlay.gpStart,MyMapOverlay.gpEnd);
                		if(doc==null)
                		{//若文檔獲取失敗，則顯示錯誤提示
                			MapNavigateActivity.msg="網絡故障，請重新選擇起點、終點再試！";
                			MapNavigateActivity.hd.sendEmptyMessage(Constant.DISPLAY_TOAST);
                    		MapNavigateActivity.inQuery=false;  
                    		return;
                		}
                		//通過解析XML文檔得到行車路徑中的節點數組
                        GeoPoint[] points=NavigateUtil.getRoutePoints(doc);
                        if(points==null)
                		{//若文檔分析失敗，則顯示錯誤提示
                        	MapNavigateActivity.msg="網絡故障，請重新選擇起點、終點再試！";
                			MapNavigateActivity.hd.sendEmptyMessage(Constant.DISPLAY_TOAST);
                    		MapNavigateActivity.inQuery=false; 
                    		return;
                		}
                        //創建行車路線Overlay
                        MyNavigateOverlay mno=new MyNavigateOverlay(points); 
                        //將用於獲取全局觸控事件的透明Overlay拿到
                        MapView mv=(MapView)findViewById(R.id.myMapView);
                        List<Overlay> overlays = mv.getOverlays(); 
                        Overlay first=overlays.get(0);
                        Overlay second=overlays.get(1);
                        Overlay third=overlays.get(2);
                        Overlay four=overlays.get(3);
                        Overlay five=overlays.get(4);
                        //將其他的Overlay記錄到輔助列表中
                		List<Overlay> tol=new ArrayList<Overlay>();
                		for(int i=5;i<overlays.size();i++)
                		{
                			tol.add(overlays.get(i));
                		}
                		//清空所有Overlay
                		overlays.clear();
                		//添加用於獲取全局觸控事件的透明Overlay
                		overlays.add(first);
                		overlays.add(second);
                		overlays.add(third);
                		overlays.add(four);
                		overlays.add(five);
                        //添加行車路線Overlay
                        overlays.add(mno); 
                        //添加其他Overlay
                        for(Overlay o:tol)
                        {
                        	overlays.add(o);  
                        }
                        //上述之所以執行那麼多動作是為了將行車路線Overlay
                        //放到起點、終點氣球Overlay下面，否則遮擋視覺效果不對
                        
                        //切換狀態到初始狀態
                        MapController mc=mv.getController();//獲取地圖控制器
                        mc.animateTo(MyMapOverlay.gpStart);//將地圖中心點移到路線起點位置
                        //將正在查詢中標誌位設置為false
                        MapNavigateActivity.inQuery=false;
                	}
                }.start();                  
                
				return true;
			}    		
    	};
    	dzjjwd.setOnMenuItemClickListener(lsn);//給確定菜單項添加監聽器 
		
		return true;		
	}
	
	//休息一定毫秒數的方法
	public void sleep(int ms)
	{
		try 
		{
			Thread.sleep(ms);
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
    @Override
    public void onPause()
    {
    	super.onPause();
		inQuery=false;//正在查詢中標誌位
		ison=false;//是否繪製導航箭頭標誌				
		flag=false;//是否顯示了自己位置			
		yyhh=false;//動畫允許標誌位
    }
}
