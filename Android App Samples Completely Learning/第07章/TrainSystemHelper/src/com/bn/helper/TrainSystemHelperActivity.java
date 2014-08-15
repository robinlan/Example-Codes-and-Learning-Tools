package com.bn.helper;


import java.util.ArrayList;
import java.util.List;

import com.bn.helper.Address;
import com.bn.helper.DBUtil;
import com.bn.helper.MapNavigateActivity;
import com.bn.helper.R;
import com.bn.helper.DBUtil;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView.LayoutParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import static com.bn.helper.Constant.SUM;
import static com.bn.helper.Constant.*;

public class TrainSystemHelperActivity extends Activity {
    /** Called when the activity is first created. */
	  String currLine;
	  String currStation;
	  int screenWidth;
	  int screenHeight;
	  int state=0;//state=0時列表選擇state==1時最短路徑查詢
	  static boolean flag1=true,flag2=true,flag3=true;
	  static String str;
	  GeoPoint gpLocation1;
	  ArrayList<Address> nearlist=new ArrayList<Address>();
	  static final int NEAR_DISTANCE=1;
	  Dialog nearDialog;
	  SeekBar sb;
	  
	  Handler hd=new Handler() //接受信息界面跳轉
	    {
		   @Override
		  public void handleMessage(Message msg)//重寫方法  
		  {
			 switch(msg.what)
			 {
			     case 0:
			    	 gotoMainView();  //主界面
				    break;		     
			 }
		 }
	   };	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth=dm.widthPixels;
        screenHeight=dm.heightPixels;
        
        //設置為橫屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      //下兩句為設置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  
		gotoSurfaceView();
		
		
    }
    
  //歡迎界面
    public void gotoSurfaceView()
    {
    	MySurfaceView mView=new MySurfaceView(this);
    	setContentView(mView);
    }
    
    public void gotoMainView()
    {
    	setContentView(R.layout.main);  
		DBUtil.initData();
		initlinesSpinner();    
		List<String> linesList=DBUtil.searchLineList();
		currLine=linesList.get(0);
		initstationSpinner(currLine);
		List<String> stationList=DBUtil.searchStationList(currLine);
		currStation=stationList.get(0);
		
		final CheckBox cb1=(CheckBox)this.findViewById(R.id.checkbox01);
		final CheckBox cb2=(CheckBox)this.findViewById(R.id.checkbox02);
		final CheckBox cb3=(CheckBox)this.findViewById(R.id.checkbox03);
		
		cb1.setOnClickListener(
			new OnClickListener()
			{

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					flag1=!flag1;
				}
				
			}
		);
		cb2.setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						flag2=!flag2;
					}
					
				}
			);
		cb3.setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						flag3=!flag3;
					}
					
				}
			);
		
		ImageButton im=(ImageButton)this.findViewById(R.id.ImageButton01);
		im.setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if(state==0)
						{
							if(cb1.isChecked()==true)
							{
								flag1=true;
							}
							if(cb2.isChecked()==true)
							{
								flag2=true;
							}
							if(cb3.isChecked()==true)
							{
								flag3=true;
							}
							String[] jwd=DBUtil.searchJWD(currLine,currStation);
							Bundle bundle=new Bundle();
							bundle.putString("jdStr", jwd[0]);
							bundle.putString("wdStr", jwd[1]);
							bundle.putString("msg",currStation+"\n經度："+getjwdfromString(jwd[0])+"\n緯度："+getjwdfromString(jwd[1]));
							str=currLine;
							Intent intent=new Intent(TrainSystemHelperActivity.this,MapNavigateActivity.class);
							intent.putExtras(bundle);					
							TrainSystemHelperActivity.this.startActivity(intent);
						}
						else if(state==1) 
						{
							flag1=true;
							flag2=true;
							flag3=true;
							sb=(SeekBar)findViewById(R.id.seekbar011);						
							int current=sb.getProgress();
							double middle=(current)/100.00*SUM;
							
							List<String> totallist=new ArrayList<String>();
							totallist.clear();
							totallist=DBUtil.searchTotaljw();
							//定位自己的位置
						    ////獲取位置管理器實例
					        String serviceName=Context.LOCATION_SERVICE;
					        LocationManager lm=(LocationManager)getSystemService(serviceName);        
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
					            gpLocation1 = new GeoPoint
					            (
					            		(int)(tempLo.getLatitude()*1E6), //緯度
					            		(int)(tempLo.getLongitude()*1E6) //經度
					            );
					        }
					        else
					        {
					        	Toast.makeText(TrainSystemHelperActivity.this, "請打開GPS，並確保其正常工作！", Toast.LENGTH_LONG).show();
					        }				        
					        //位置變化監聽器
					        LocationListener ll=new LocationListener()
					        {
								@Override
								public void onLocationChanged(Location location) 
								{//當位置變化時觸發
									gpLocation1 = new GeoPoint
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
					        
					        //添加位置變化監聽器
					        lm.requestLocationUpdates
					        (
					        		LocationManager.GPS_PROVIDER, 	//使用GPS定位
					        		2000, 							//時間分辨率 ms
					        		5, 								//距離分辨率m
					        		ll								//位置變化監聽器
					        );
					        
					        
					        nearlist.clear();
					        try {				        	
					        	for(int i=0;i<totallist.size();)
								{
					        		int j=i;								
									if(calculationdistance(tempLo.getLongitude(),tempLo.getLatitude(),totallist.get(j),totallist.get(j+1))<=middle)
									{
									Address ai=new Address 
									(
											totallist.get(j),    //經度
											totallist.get(j+1),     //緯度
											totallist.get(j+3)+"\n經度："+getjwdfromString(totallist.get(j))+"\n緯度："+getjwdfromString(totallist.get(j+1)),
											totallist.get(j+3)
									);
															
									nearlist.add(ai); 							
									}							
										i=i+4;	
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
													
							if(nearlist.size()>0)
			                {//如果成功獲取了經緯度取列表中的第一條								
								if(nearlist.size()==1)  
								{											                				                   			                   	
									Bundle bundle=new Bundle(); //初始化Bandle
									bundle.putString("jdStr", totallist.get(0));  //添加數據
									bundle.putString("wdStr", totallist.get(1));
									bundle.putString("msg",totallist.get(3)+"\n經度："+getjwdfromString(totallist.get(0))+"\n緯度："+getjwdfromString(totallist.get(1)));
									Intent intent=new Intent(TrainSystemHelperActivity.this,MapNavigateActivity.class);//Intent
								    intent.putExtras(bundle);		//添加數據包			
								    TrainSystemHelperActivity.this.startActivity(intent);  //開啟另一個Activity
								}
								else
								{								
									showDialog(NEAR_DISTANCE);      //Dialog顯示
								}
										
			                }
							else            //沒有查到地址的情況
							{
								Toast.makeText            //Toast信息顯示
								(
										TrainSystemHelperActivity.this,      
									"對不起，在此範圍內沒有您要找的俱樂部！", //提示信息
									Toast.LENGTH_SHORT                 //短時間顯示
								).show();
							}
							
							
							
						}
					}				      		
	        	}
	        );
		
		final LinearLayout l001=(LinearLayout)findViewById(R.id.linear01);
        final LinearLayout l002=(LinearLayout)findViewById(R.id.linear02);
        RadioButton rb1=(RadioButton)findViewById(R.id.radio01);
        rb1.setOnClickListener(
        	new OnClickListener()
        	{

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					state=0;
					l001.setVisibility(1);
					l002.setVisibility(-1);
				}       		
        	}
        );
        
        RadioButton rb2=(RadioButton)findViewById(R.id.radio02);
        rb2.setOnClickListener(
        	new OnClickListener()
        	{

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					state=1;
					l001.setVisibility(-1);
					l002.setVisibility(1);
				}       		
        	}
        );
        
        
        
    }
    
    @Override
    public Dialog onCreateDialog(int id)
    {    	
    	Dialog result=null;
    	switch(id)
    	{	    	
	    	case NEAR_DISTANCE:
	    		AlertDialog.Builder b2=new AlertDialog.Builder(this); 
				  b2.setItems(
					null, 
				    null
				   );
				  nearDialog=b2.create();       //創建Dialog
				  result=nearDialog;	    		
		    	break;
    	}
        return result;
    }
    
    //初始化線路列表
    public void initlinesSpinner()
    {
    	//初始化Spinner
        Spinner sp=(Spinner)this.findViewById(R.id.Spinner00);
        final List<String> linesList=DBUtil.searchLineList();//通過線路名得到站名
        
        //為Spinner準備內容適配器
        BaseAdapter ba=new BaseAdapter()
        {
			@Override
			public int getCount() {
				return linesList.size();//總共三個選項
			}

			@Override
			public Object getItem(int arg0) { return null; }

			@Override
			public long getItemId(int arg0) { return 0; }

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				/*
				 * 動態生成每個下拉項對應的View，每個下拉項View由一個TextView構成
				*/
				
				//初始化TextView
				TextView tv=new TextView(TrainSystemHelperActivity.this);
				tv.setText(linesList.get(arg0));//設置內容
				tv.setTextSize(18);//設置字體大小
				tv.setTextColor(Color.BLACK);//設置字體顏色				
				return tv;
			}        	
        };        
        sp.setAdapter(ba);//為Spinner設置內容適配器
        //設置選項選中的監聽器
        sp.setOnItemSelectedListener(
           new OnItemSelectedListener()
           {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {//重寫選項被選中事件的處理方法				
				TextView tvn=(TextView)arg1;//獲取其中的TextView 
				currLine=tvn.getText().toString();
		        initstationSpinner(currLine);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) { }        	   
           }
        );
}
    public void initstationSpinner(String L_name)
    {
    	Spinner sp=(Spinner)this.findViewById(R.id.Spinner01);
    	final List<String> stationList=DBUtil.searchStationList(L_name);
    	 //為Spinner準備內容適配器
        BaseAdapter ba=new BaseAdapter()
        {
			@Override
			public int getCount() {
				return stationList.size();//總共三個選項
			}

			@Override
			public Object getItem(int arg0) { return null; }

			@Override
			public long getItemId(int arg0) { return 0; }

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				/*
				 * 動態生成每個下拉項對應的View，每個下拉項View由一個TextView構成
				*/
				
				//初始化TextView
				TextView tv=new TextView(TrainSystemHelperActivity.this);
				tv.setText(stationList.get(arg0));//設置內容
				tv.setTextSize(18);//設置字體大小
				tv.setTextColor(Color.BLACK);//設置字體顏色				
				return tv;
			}        	
        }; 
        sp.setAdapter(ba);//為Spinner設置內容適配器
        sp.setOnItemSelectedListener(
                new OnItemSelectedListener()
                {
     			@Override
     			public void onItemSelected(AdapterView<?> arg0, View arg1,
     					int arg2, long arg3) {//重寫選項被選中事件的處理方法				
     				TextView tvn=(TextView)arg1;//獲取其中的TextView 
     				if(tvn!=null)
     				{
     					currStation=tvn.getText().toString();
     				}
     			}

     			@Override
     			public void onNothingSelected(AdapterView<?> arg0) { }        	   
                }
             );  
    }
    
    @Override
    public void onPrepareDialog(int id, Dialog dialog)//每次彈出對話框時被回調以動態更新對話框內容的方法
    {
    	//若不是歷史對話框則返回
    	if(id!=NEAR_DISTANCE)return;
	   	
	   	//對話框對應的總垂直方向LinearLayout
	   	LinearLayout ll=new LinearLayout(TrainSystemHelperActivity.this);
		ll.setOrientation(LinearLayout.VERTICAL);		//設置朝向	
		ll.setGravity(Gravity.CENTER_HORIZONTAL);
		ll.setBackgroundResource(R.drawable.dialog);
		
		//標題行的水平LinearLayout
		LinearLayout lln=new LinearLayout(TrainSystemHelperActivity.this);
		lln.setOrientation(LinearLayout.HORIZONTAL);		//設置朝向	
		lln.setGravity(Gravity.LEFT);
		lln.setLayoutParams(new ViewGroup.LayoutParams(280, LayoutParams.WRAP_CONTENT));
		
		//標題行的圖標
		ImageView iv=new ImageView(TrainSystemHelperActivity.this);
		iv.setImageResource(R.drawable.history);
		iv.setLayoutParams(new ViewGroup.LayoutParams(24, 24));
		lln.addView(iv);
		
		//標題行的文字
		TextView tvTitle=new TextView(TrainSystemHelperActivity.this);
		tvTitle.setText("請選擇地點");
		tvTitle.setTextSize(20);//設置字體大小
		tvTitle.setTextColor(Color.WHITE);//設置字體顏色
		lln.addView(tvTitle);
		
		//將標題行添加到總LinearLayout
		ll.addView(lln);		
	   	
	   	//為對話框中的歷史記錄條目創建ListView
	    //初始化ListView
        ListView lv=new ListView(this);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);     
	   	if(id==NEAR_DISTANCE) 
	   	{
	   	//為ListView準備內容適配器
	        BaseAdapter ba=new BaseAdapter()
	        {
				@Override
				public int getCount() {
					return nearlist.size();//總共幾個選項
				}

				@Override
				public Object getItem(int arg0) { return null; }

				@Override
				public long getItemId(int arg0) { return 0; }

				@Override
				public View getView(int arg0, View arg1, ViewGroup arg2) {
					//動態生成每條歷史記錄對應的TextView
					TextView tv=new TextView(TrainSystemHelperActivity.this);
					tv.setGravity(Gravity.LEFT);
					tv.setText(nearlist.get(arg0).listStr);//設置內容
					tv.setTextSize(20);//設置字體大小
					tv.setTextColor(Color.WHITE);//設置字體顏色
					tv.setPadding(0,0,0,0);//設置四周留白				
					return tv;
				}        	
	        };       
	        lv.setAdapter(ba);//為ListView設置內容適配器
	        
	        //設置選項被單擊的監聽器
	        lv.setOnItemClickListener(
	           new OnItemClickListener()
	           {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {//重寫選項被單擊事件的處理方法
					//獲取歷史記錄中當前選中的TextView 
					Address ai=nearlist.get(arg2);
					Bundle bundle=new Bundle();
					bundle.putString("jdStr", ai.jdStr);
					bundle.putString("wdStr", ai.wdStr);
					bundle.putString("msg",ai.msgStr);
					Intent intent=new Intent(TrainSystemHelperActivity.this,MapNavigateActivity.class);
				    intent.putExtras(bundle);					
				    TrainSystemHelperActivity.this.startActivity(intent);				
					nearDialog.cancel();
				}        	   
	           }
	        );             
	        //將歷史記錄條的ListView加入總LinearLayout
	        ll.addView(lv);
	        dialog.setContentView(ll);
	   	}
	   		
    } 
    
    public static double calculationdistance(double jA,double wA,String jd2,String wd2) 
	{
		double wB=Double.parseDouble(wd2.trim());		
		double jB=Double.parseDouble(jd2.trim());
		double AB=6371.004*Math.acos((Math.sin(wA*Math.PI/180))*(Math.sin(wB*Math.PI/180))+(Math.cos(wA*Math.PI/180))*(Math.cos(wB*Math.PI/180))*(Math.cos(jB*Math.PI/180-jA*Math.PI/180)));
		return AB;
	}
    
    public static String getjwdfromString(String num)
    {
		String result;
    	double nummiddle=Double.parseDouble(num);
		double nummiddle1=Math.round(nummiddle*1000)/1000.0;
		result=nummiddle1+"";
    	return result;
   	
    }
    
    protected boolean isRouteDisplayed() {
		return false;
	}
}
