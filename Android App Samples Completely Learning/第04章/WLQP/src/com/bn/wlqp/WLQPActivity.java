package com.bn.wlqp;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

enum WhichView {WELCOMEVIEW,MAIN_MENU,IP_VIEW,GAME_VIEW,WAIT_OTHER,WIN,LOST,EXIT,FULL,ABOUT,HELP}  //界面枚舉  

public class WLQPActivity extends Activity 
{
	MainMenuView mmv;  //主界面
	GameView gameview; //遊戲界面
	WhichView curr;  //選擇哪個界面
	ClientAgent ca; //客戶端代理線程
	SoundPool  soundPool;
	HashMap<Integer, Integer>  soundPoolMap; 
	static String cardListStr; 
	
	Handler hd=new Handler()//聲明消息處理器
	{
			@Override
			public void handleMessage(Message msg)//重寫方法
        	{
        		switch(msg.what)
        		{
	        		case 0:   //進入等待界面
	        			setContentView(R.layout.wait);
	        			curr=WhichView.WAIT_OTHER;
	        		break;
	        		case 1:  //進入遊戲界面
	        			gotoGameView();
	        		break;
	        		case 2:  //進入你贏了界面
	        			setContentView(R.layout.win);
	        			curr=WhichView.WIN;
	        		break;
	        		case 3:  //進入你輸了界面
	        			setContentView(R.layout.lost);
	        			curr=WhichView.LOST;
	        		break; 
	        		case 4:  //進入有玩家退出界面
	        			setContentView(R.layout.exit);
	        			curr=WhichView.EXIT;
	        		break;
	        		case 5:  //人數已滿
	        			setContentView(R.layout.full);
	        			curr=WhichView.FULL;
	        		break;
	        		case 6:   //進入幫助頁面
	        			setContentView(R.layout.help);
	        			curr=WhichView.HELP;
	        		break;
	        		case 7:   //進入關於界面
	        			setContentView(R.layout.about);
	        			curr=WhichView.ABOUT;
	        		break;
	        		case 8:
	        			goToMainMenu();
	        			curr=WhichView.WELCOMEVIEW;
	        		break;
        		}
        	}  
	};  
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);        
        //設置全屏顯示
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags
        (
        		WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        //強制為橫屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initSounds();
        goToWelcomeView();
          
    }

    //聲音緩衝池的初始化
    public void initSounds()
    {
    	 //創建聲音緩衝池
	     soundPool = new SoundPool
	     (
	    		 4, 							//同時能最多播放的個數
	    		 AudioManager.STREAM_MUSIC,     //音頻的類型
	    		 100							//聲音的播放質量，目前無效
	     );
	     
	     //創建聲音資源Map	     
	     soundPoolMap = new HashMap<Integer, Integer>();   
	     //將加載的聲音資源id放進此Map
	     soundPoolMap.put(1, soundPool.load(this, R.raw.tweet, 1));
	     //有幾個音效就有當前這個幾句  R.raw.gamestart返回編號 不定     後面的1為優先級 目前不考慮
	} 
   
   //播放聲音的方法
   public void playSound(int sound, int loop) {	    
	    AudioManager mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);   
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);//當前音量   
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);//最大音量       
	    float volume = streamVolumeCurrent / streamVolumeMax;   
	    
	    soundPool.play
	    (
    		soundPoolMap.get(sound), //聲音資源id
    		volume, 				 //左聲道音量
    		volume, 				 //右聲道音量
    		1, 						 //優先級				 
    		loop, 					 //循環次數 -1帶表永遠循環
    		0.5f					 //回放速度0.5f～2.0f之間
	    );
	}
   
   
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent e)
    {   //監聽手機鍵盤按下事件
    	if(keyCode==4)//調製上一個界面的鍵
    	{//根據記錄的當前是哪個界面信息的curr可以知道要跳轉到的是哪個界面
    		if(curr==WhichView.WIN||curr==WhichView.LOST||curr==WhichView.EXIT)
    		{
    			goToMainMenu();  
    			return true;
    		}
    		if(curr==WhichView.WELCOMEVIEW)
    		{
    			return true;
    		}
    		if(curr==WhichView.IP_VIEW)
    		{//跳轉到MainMenu
    			goToMainMenu();
    			return true;
    		}
    		if(curr==WhichView.GAME_VIEW)
    		{//跳轉到EXIT界面
    			try 
    			{
					ca.dout.writeUTF("<#EXIT#>");
				} catch (IOException e1) 
				{
					e1.printStackTrace();
				}
    			return true;          
    		}
    		if(curr==WhichView.WAIT_OTHER)
    		{//不跳轉
    			return true;
    		}
    		if(curr==WhichView.MAIN_MENU)
    		{//退出遊戲
    			System.exit(0);
    		}
    		if(curr==WhichView.FULL)
    		{//跳轉到IPView
    			gotoIpView();
    			return true;
    		}
    		if(curr==WhichView.HELP)
    		{//跳轉到MainMenu
    			goToMainMenu();
    			return true;
    		}
    		if(curr==WhichView.ABOUT)
    		{//跳轉到MainMenu
    			goToMainMenu();
    			return true;
    		}
    	}
    	
    	
    	return false;
    }
    
    public void goToWelcomeView()
    {
    	WelcomeView mySurfaceView = new WelcomeView(this);
        this.setContentView(mySurfaceView);
        curr=WhichView.WELCOMEVIEW;
    }
    public void goToMainMenu()
    {//去主界面的方法
    	if(mmv==null)
    	{
    		mmv=new MainMenuView(this);
    	}    	
    	setContentView(mmv);    
    	//當前的View為MAIN_MENU;
    	curr=WhichView.MAIN_MENU;
    }
    
    public void gotoIpView()
    {//去主IP和端口號的界面的方法
    	setContentView(R.layout.main);   
    	final Button blj=(Button)this.findViewById(R.id.Button01);
    	final Button bfh=(Button)this.findViewById(R.id.Button02);
    	
        blj.setOnClickListener
        (
    		new  OnClickListener()
    		{
				@Override
				public void onClick(View v) 
				{
					//得到每個EditText的引用
					final EditText eta=(EditText)findViewById(R.id.EditText01);
			    	final EditText etb=(EditText)findViewById(R.id.EditText02);
					String ipStr=eta.getText().toString();//得到EditText裡面的信息					
					String portStr=etb.getText().toString();
					
					String[] ipA=ipStr.split("\\.");
					if(ipA.length!=4)
					{//判斷IP的格式是否合法
						Toast.makeText
						(
								WLQPActivity.this,
								"服務器IP地址不合法", 
								Toast.LENGTH_SHORT
						).show();
						
						return;
					}
					
					for(String s:ipA)
					{//在IP的格式合法的前提下判斷端口號是否合法
						try
						{
							int ipf=Integer.parseInt(s);
							if(ipf>255||ipf<0)
							{//判斷Ip的合法性
								Toast.makeText
								(//界面彈出Toast顯示信息 --->服務器IP地址不合法!
										WLQPActivity.this,
										"服務器IP地址不合法", 
										Toast.LENGTH_SHORT
								).show();							
								return;
							}
						}
						catch(Exception e)
						{
							Toast.makeText
							(//界面彈出Toast顯示信息 --->服務器IP地址不合法!
									WLQPActivity.this,
									"服務器IP地址不合法!", 
									Toast.LENGTH_SHORT
							).show();							
							return;
						}
					}
					
					try
					{
						int port=Integer.parseInt(portStr);
						if(port>65535||port<0)
						{//判斷端口號是否合法
							Toast.makeText
							(//界面彈出Toast顯示信息 --->服務器端口號不合法!
									WLQPActivity.this,
									"服務器端口號不合法!", 
									Toast.LENGTH_SHORT
							).show();							
							return;
						}						
					}
					catch(Exception e)
					{
						Toast.makeText
						(//界面彈出Toast顯示信息 --->服務器端口號不合法!
								WLQPActivity.this,
								"服務器端口號不合法!", 
								Toast.LENGTH_SHORT
						).show();							
						return;
					}	
					
					//驗證過關
					int port=Integer.parseInt(portStr);
					try
					{//驗證過關後啟動代理的客戶端線程
						Socket sc=new Socket(ipStr,port);
						DataInputStream din=new DataInputStream(sc.getInputStream());
						DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
						ca=new ClientAgent
						(
							WLQPActivity.this,
							sc,
							din,
							dout
						);
						ca.start();
					}
					catch(Exception e)
					{
						Toast.makeText
						(//界面彈出Toast顯示信息 
								WLQPActivity.this,
								"聯網失敗，請稍後再試!", 
								Toast.LENGTH_SHORT
						).show();							
						return;
					}	
				}    			
    		} 
        );
        bfh.setOnClickListener
        (//對返回按鈕設置監聽   跳轉到主界面
    		new  OnClickListener()
    		{
				@Override
				public void onClick(View v) 
				{
					goToMainMenu();
				}    			
    		}
        );
    	//當前的View為IP_VIEW;
    	curr=WhichView.IP_VIEW;
    }
    
    public void gotoGameView()
    { 	
    	gameview=new GameView(this); 
    	setContentView(gameview);
    	//當前的View為GAME_VIEW;
    	curr=WhichView.GAME_VIEW;
    }
}
