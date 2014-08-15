package com.bn.map;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import static com.bn.map.GameSurfaceView.*;
import static com.bn.map.Constant.*;
enum WhichView {WELCOME_VIEW,MAIN_MENU,SETTING_VIEW,
	MAPLEVEL_VIEW,GAME_VIEW,RANKING_VIEW,WIN_VIEW,FAIL_VIEW}
public class MapMasetActivity extends Activity{
	WhichView curr;//當前枚舉值
	WelcomeView wv;//進入歡迎界面
	GameSurfaceView msv;//進入遊戲界面
	GameView gameView;	//排行榜界面
	boolean collision_soundflag=true;//是否開啟碰撞聲音
	Vibrator mVibrator;//聲明振動器
	boolean shakeflag=true;//是否震動
	int level;//當前所選關卡
	int map_level_index=1;//排行榜中所選關數
	int curr_grade;//當前遊戲的得分
	SensorManager mySensorManager;	//SensorManager對像引用
//	SensorManagerSimulator mySensorManager;	//使用SensorSimulator模擬時聲明SensorSensorManager對像引用的方法
	SoundPool soundPool;//聲音池
	HashMap<Integer, Integer> soundPoolMap; //聲音池中聲音ID與自定義聲音ID的Map
    Handler hd=new Handler(){
			@Override
			public void handleMessage(Message msg){
        		switch(msg.what){
	        		case 0://切換主菜單界面
	        			goToMainView();
	        		break;
	        		case 1://切換到贏的界面
	        			goToWinView();
	                    break;
	        		case 2://切換到輸的界面
	        			goToFailView();
	        			break;
	        		case 3://切換到遊戲的界面
	        			goToGameView();
	        			break;
	        		case 4://切換到選關界面
	        			goToMapLevelView();
	        			break;
	        		case 5://切換到設置界面
	        			goToSettingView();
	        			break;
	        		case 6://切換到排行榜界面
	        			goToRankView();
	        			break;
        		}}};
    @Override
    public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);        
        //設置全屏顯示
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(
        		WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        //強制為橫屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        SCREEN_HEIGHT=dm.heightPixels;
        SCREEN_WIDTH=dm.widthPixels;        
		//獲得SensorManager對像
        mySensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);	           
        //使用SensorSimulator模擬時聲明SensorSensorManager對像引用的方法
//        mySensorManager = SensorManagerSimulator.getSystemService(this, SENSOR_SERVICE);
//        mySensorManager.connectSimulator();
        initSound();
        collisionShake();
        initDatabase();  
        goToWelcomeView();//進入歡迎界面
       // goToWinView();
    }
    //創建數據庫
    public  void initDatabase(){
    	//創建表
    	String sql="create table if not exists rank(id int(2) primary key not" +
    			" null,level int(2),grade int(4),time char(20));";
    	SQLiteUtil.createTable(sql);
    }
    //插入時間的方法
    public  void insertTime(int level,int grade)
    {
    	Date d=new Date();
    	int curr_Id;
        String curr_time=(d.getYear()+1900)+"-"+(d.getMonth()+1<10?"0"+
        		(d.getMonth()+1):(d.getMonth()+1))+"-"+d.getDate();
    	String sql_maxId="select max(id) from rank";
    	Vector<Vector<String>> vector=SQLiteUtil.query(sql_maxId);
    	if(vector.get(0).get(0)==null)
    	{
    		curr_Id=0;
    	}
    	else
    	{
    		curr_Id=Integer.parseInt(vector.get(0).get(0))+1;
    	}
    	String sql_insert="insert into rank values("+curr_Id+","+level+
    								","+grade+","+"'"+curr_time+"');";
    	SQLiteUtil.insert(sql_insert);
    }
  //進入歡迎界面
    public void goToWelcomeView(){
    	if(wv==null){
    		wv=new WelcomeView(this);
    	}
    	setContentView(wv);
    	curr=WhichView.WELCOME_VIEW;
    }
    //進入主菜單
    public void goToMainView(){
    	setContentView(R.layout.main);
    	curr=WhichView.MAIN_MENU;
    	ImageButton ib_start=(ImageButton)findViewById(R.id.ImageButton_Start);
    	ImageButton ib_rank=(ImageButton)findViewById(R.id.ImageButton_Rank);
    	ImageButton ib_set=(ImageButton)findViewById(R.id.ImageButton_Set);
    	ib_start.setOnClickListener(//進入到選關界面
              new OnClickListener(){
				@Override
				public void onClick(View v){
					shake();
					hd.sendEmptyMessage(4);
				}});
    	ib_rank.setOnClickListener(//切換到排行榜界面
              new OnClickListener(){
				@Override
				public void onClick(View v){
					shake();
					hd.sendEmptyMessage(6);
				}});
    	ib_set.setOnClickListener(//切換到設置界面
              new OnClickListener(){
				@Override
				public void onClick(View v){
					shake();
					hd.sendEmptyMessage(5);
				}});}
    //進入設置界面
    public void goToSettingView()
    {
    	setContentView(R.layout.setting);
    	curr=WhichView.SETTING_VIEW;
    	final CheckBox cb_collision=(CheckBox)findViewById(R.id.CheckBox_collision);
    	cb_collision.setChecked(collision_soundflag);
    	final CheckBox cb_shake=(CheckBox)findViewById(R.id.CheckBox_shake);
    	cb_shake.setChecked(shakeflag);
    	ImageButton ib_ok=(ImageButton)findViewById(R.id.ImageButton_ok);
    	ib_ok.setOnClickListener
    	( 
              new OnClickListener() 
              {
				@Override
				public void onClick(View v) 
				{
					collision_soundflag=cb_collision.isChecked();
					shakeflag=cb_shake.isChecked();
					shake();
					//前往主菜單
					hd.sendEmptyMessage(0);
				}
			}
    	);
    }
    //進入開始遊戲選關界面
    public void goToMapLevelView()
    {
    	setContentView(R.layout.level_map);
    	curr=WhichView.MAPLEVEL_VIEW;
    	final ImageButton ib_map[]=
    	{
    			(ImageButton)findViewById(R.id.ImageButton_map01),
    			(ImageButton)findViewById(R.id.ImageButton_map02),
    			(ImageButton)findViewById(R.id.ImageButton_map03),
    			(ImageButton)findViewById(R.id.ImageButton_map04),
    			(ImageButton)findViewById(R.id.ImageButton_map05),
    			(ImageButton)findViewById(R.id.ImageButton_map06)
    	};
    		ib_map[0].setOnClickListener//進入遊戲
        	( 
                  new OnClickListener() 
                  {
    				@Override
    				public void onClick(View v) 
    				{
    					//初始化地圖數據
    					shake();
    					guankaID=level=0;
    					BallGDThread.initDiTu(); 
    					hd.sendEmptyMessage(3);
    				}
    			}
        	);
    		ib_map[1].setOnClickListener//進入遊戲
        	( 
                  new OnClickListener() 
                  {
    				@Override
    				public void onClick(View v) 
    				{
    					//初始化地圖數據
    					shake();
    					guankaID=level=1;
    					BallGDThread.initDiTu(); 
    					hd.sendEmptyMessage(3);
    				}
    			}
        	);
    		ib_map[2].setOnClickListener//進入遊戲
        	( 
                  new OnClickListener() 
                  {
    				@Override
    				public void onClick(View v) 
    				{
    					//初始化地圖數據
    					shake();
    					guankaID=level=2;
    					BallGDThread.initDiTu(); 
    					hd.sendEmptyMessage(3);
    				}
    			}
        	);
    		ib_map[3].setOnClickListener//進入遊戲
        	( 
                  new OnClickListener() 
                  {
    				@Override
    				public void onClick(View v) 
    				{
    					//初始化地圖數據
    					shake();
    					guankaID=level=3;
    					BallGDThread.initDiTu(); 
    					hd.sendEmptyMessage(3);
    				}
    			}
        	);
    		ib_map[4].setOnClickListener//進入遊戲
        	( 
                  new OnClickListener() 
                  {
    				@Override
    				public void onClick(View v) 
    				{
    					//初始化地圖數據
    					shake();
    					guankaID=level=4;
    					BallGDThread.initDiTu(); 
    					hd.sendEmptyMessage(3);
    				}
    			}
        	);
    		ib_map[5].setOnClickListener//進入遊戲
        	( 
                  new OnClickListener() 
                  {
    				@Override
    				public void onClick(View v) 
    				{
    					//初始化地圖數據
    					shake();
    					guankaID=level=5;
    					BallGDThread.initDiTu(); 
    					hd.sendEmptyMessage(3);
    				}
    			}
        	);
    		
    }
    //進入遊戲界面
    public void goToGameView()
    {
    	 msv=new GameSurfaceView(this); 
    	 msv.requestFocus();//獲取焦點
         msv.setFocusableInTouchMode(true);//設置為可觸控
         curr=WhichView.GAME_VIEW;
    	 setContentView(msv);
    }
    //進入排行榜
    public void goToRankView()
    {
    	if(gameView==null)
    	{
    		 gameView = new GameView(this);
    	}    	   	
         setContentView(gameView);         
    	curr=WhichView.RANKING_VIEW;
    }
    //如果闖關成功
    public void goToWinView()
    {
    	setContentView(R.layout.win);
    	curr=WhichView.WIN_VIEW;
        TextView tv_score=(TextView)findViewById(R.id.TextView_score);//當前得分
        TextView tv_flag=(TextView)findViewById(R.id.TextView_flag);//是否刷新紀錄
        ImageButton ib_replay=(ImageButton)findViewById(R.id.ImageButton_Replay);//重玩按鈕
        ImageButton ib_next=(ImageButton)findViewById(R.id.ImageButton_Next);//下一關按鈕
        ImageButton ib_back=(ImageButton)findViewById(R.id.ImageButton_Back);//返回按鈕
        tv_score.setText("本關得分為:"+curr_grade);
        //查詢本關最大的分數記錄
        String sql_maxScore="select max(grade) from rank where level="+(level+1);
        System.out.println(sql_maxScore);
    	Vector<Vector<String>> vector=SQLiteUtil.query(sql_maxScore);
    	//如果當前分數大於歷史記錄,則刷新記錄
    	
    	if(vector.get(0).get(0)==null||curr_grade>Integer.parseInt(vector.get(0).get(0)))
    	{
    		tv_flag.setText("刷新紀錄!");
    	}
    	else
    	{
    		tv_flag.setText("沒有刷新紀錄!");
    	}
    	insertTime(level+1,curr_grade);
    	//如果當前已到達關底 則下一關按鈕不可用
    	if(level==5)
    	{
    		ib_next.setEnabled(false);
    		ib_next.setVisibility(INVISIBLE);
    	}
        ib_replay.setOnClickListener//重玩按鈕監聽   
    	( 
              new OnClickListener() 
              {
				@Override
				public void onClick(View v) 
				{
					shake();
					BallGDThread.initDiTu();
					hd.sendEmptyMessage(3);
				}
			}
    	);
        ib_next.setOnClickListener//下一關按鈕監聽
    	( 
              new OnClickListener() 
              {
				@Override
				public void onClick(View v) 
				{
					shake();
					if(level<5)
					{
						level++;
					}
					guankaID=level;//進入下一關
					BallGDThread.initDiTu();
					hd.sendEmptyMessage(3);
				}
			}
    	);
        ib_back.setOnClickListener//返回按鈕監聽   返回到選關界面
    	( 
              new OnClickListener() 
              {
				@Override
				public void onClick(View v) 
				{
					shake();
					hd.sendEmptyMessage(4);
				}
			}
    	);
    }
    //如果闖關失敗
    public void goToFailView()
    {
    	setContentView(R.layout.fail);
    	curr=WhichView.FAIL_VIEW;
        ImageButton ib_replay=(ImageButton)findViewById(R.id.Fail_ImageButton_Replay);
        ImageButton ib_back=(ImageButton)findViewById(R.id.Fail_ImageButton_Back);
        ib_replay.setOnClickListener//重玩按鈕監聽
    	( 
              new OnClickListener() 
              {
				@Override
				public void onClick(View v) 
				{
					shake();
					BallGDThread.initDiTu();
					hd.sendEmptyMessage(3);
				}
			}
    	);
        ib_back.setOnClickListener//返回按鈕監聽   返回到選關界面
    	( 
              new OnClickListener() 
              {
				@Override
				public void onClick(View v) 
				{
					shake();
					hd.sendEmptyMessage(4);
				}
			}
    	);
    }
    @SuppressWarnings("deprecation")
	private SensorListener mySensorListener = new SensorListener(){
		@Override
		public void onAccuracyChanged(int sensor, int accuracy) 
		{	
		}
		@Override
		public void onSensorChanged(int sensor, float[] values) 
		{
			if(sensor == SensorManager.SENSOR_ORIENTATION)
			{//判斷是否為加速度傳感器變化產生的數據	
				int directionDotXY[]=RotateUtil.getDirectionDot
				(
						new double[]{values[0],values[1],values[2]} 
			    );
				
				ballGX=-directionDotXY[0]*3.2f;//得到X和Z方向上的加速度
				ballGZ=directionDotXY[1]*3.2f;
			}	
		}		
	};
    @Override
	protected void onResume() //重寫onResume方法
    {		
    	super.onResume();
		mySensorManager.registerListener
		(			//註冊監聽器
				mySensorListener, 					//監聽器對像
				SensorManager.SENSOR_ORIENTATION,	//傳感器類型
				SensorManager.SENSOR_DELAY_UI		//傳感器事件傳遞的頻度
		);
	}
	@Override
	protected void onPause() //重寫onPause方法
	{		
		super.onPause();
		mySensorManager.unregisterListener(mySensorListener);	//取消註冊監聽器
	}
	public void initSound()
    {
			//聲音池
			soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		    soundPoolMap = new HashMap<Integer, Integer>();   
		    //吃東西音樂
		    soundPoolMap.put(1, soundPool.load(this, R.raw.dong, 1)); 
    }
    //播放聲音
    public void playSound(int sound, int loop) 
    {
	   if(collision_soundflag)
	   {
		   AudioManager mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);   
		    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);   
		    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
		    float volume = streamVolumeCurrent / streamVolumeMax; 
		    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
	   }
	}
    //手機震動
    public void collisionShake()
    {
    		mVibrator=(Vibrator)getApplication().getSystemService
            (Service.VIBRATOR_SERVICE);	
    }
    //震動
    public void shake()
    {
    	if(shakeflag)
    	{
    		mVibrator.vibrate( new long[]{0,50},-1);
    	}
    }
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent e)
    { 
    	if(keyCode==4&&(curr==WhichView.MAPLEVEL_VIEW||curr==WhichView.SETTING_VIEW||
    			curr==WhichView.RANKING_VIEW))//返回選關界面
    	{
    		goToMainView();
    		return true;
    	}
    	if(keyCode==4&&(curr==WhichView.WIN_VIEW||curr==WhichView.FAIL_VIEW))//如果當前在贏輸界面
    	{
    		goToMapLevelView();
    		return true;
    	}
    	if(keyCode==4&&curr==WhichView.MAIN_MENU)//如果當前在主菜單界面
    	{
    		System.exit(0);
    		return true;
    	}
    	if(keyCode==4&&curr==WhichView.GAME_VIEW)//如果當前在遊戲界面
    	{
    		msv.ballgdT.flag=false;
    		goToMapLevelView();
    		return true;
    	}
    	return false;
    }
}
