package com.bn.gjxq;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

enum WhichView {WELCOME_VIEW,MAIN_MENU,IP_VIEW,GL_VIEW,LOAD_VIEW,
	   WAIT_VIEW,WIN_VIEW,EXIT_VIEW,FULL_VIEW,ABOUT_VIEW,HELP_VIEW}

public class GJXQActivity extends Activity 
{
	WelcomeView wv;//進入歡迎界面
	MainMenuView mmv;//進入主菜單
	MySurfaceView msv;//進入遊戲界面
	WhichView curr;//當前枚舉值
	ClientAgent ca;//客戶端代理線程
	SoundPool soundPool;//聲音池
	HashMap<Integer, Integer> soundPoolMap; //聲音池中聲音ID與自定義聲音ID的Map
	Handler hd=new Handler()//聲明消息處理器
	{
			@Override
			public void handleMessage(Message msg)//重寫方法
        	{
        		switch(msg.what)
        		{
	        		case 0://切換等待界面
	        			setContentView(R.layout.wait);
	        			curr=WhichView.WAIT_VIEW;
	        		break;
	        		case 1://切換進入3D遊戲界面
	        			gotoGameView();	        			
	        		break;
	        		case 2://切換贏的界面
	        			setContentView(R.layout.win);
	        			curr=WhichView.WIN_VIEW;
	        		break;
	        		case 3://切換輸的界面
	        			setContentView(R.layout.lost);
	        			curr=WhichView.WIN_VIEW;
	        		break;
	        		case 4://切換有人退出界面
	        			setContentView(R.layout.exit);
	        			curr=WhichView.EXIT_VIEW;
	        		break;
	        		case 5://進入加載界面
	        			gotoLoadView();
	        		break;
	        		case 6://進入菜單界面
	        			goToMainMenu();
	        		break;
	        		case 7://進入人數已滿界面
	        			setContentView(R.layout.full);
	        			curr=WhichView.FULL_VIEW;
	        		break;
	        		case 8://初始化棋子模型
	        			initChess();
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
        goToWelcomeView();//進入歡迎界面
    }
    //啟動線程加載棋子模型
    public void initChess()
    {
    	 new Thread()
         {
         	public void run()
         	{
         		hd.sendEmptyMessage(5);//進入加載界面
         		MySurfaceView.initChessForDraw(GJXQActivity.this.getResources());//初始化棋子
         		hd.sendEmptyMessage(6);	//進入主菜單
         	}
         }.start();
    }
    //進入歡迎界面
    public void goToWelcomeView()
    {
    	if(wv==null)
    	{
    		wv=new WelcomeView(this);
    	}
    	setContentView(wv);
    	curr=WhichView.WELCOME_VIEW;
    }
    //進入主菜單
    public void goToMainMenu()
    {
    	if(mmv==null)
    	{
    		mmv=new MainMenuView(this);
    	}
    	setContentView(mmv);	
    	curr=WhichView.MAIN_MENU;
    }
    //進入ip地址和port界面
    public void gotoIpView()
    {
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
					final EditText eta=(EditText)findViewById(R.id.EditText01);
			    	final EditText etb=(EditText)findViewById(R.id.EditText02);
					String ipStr=eta.getText().toString();					
					String portStr=etb.getText().toString();
					//ip地址本地驗證
					String[] ipA=ipStr.split("\\.");
					if(ipA.length!=4)
					{
						Toast.makeText
						(
								GJXQActivity.this,
								"服務器IP地址不合法", 
								Toast.LENGTH_SHORT
						).show();
						
						return;
					}
					
					for(String s:ipA)
					{
						try
						{
							int ipf=Integer.parseInt(s);
							if(ipf>255||ipf<0)
							{
								Toast.makeText
								(
										GJXQActivity.this,
										"服務器IP地址不合法", 
										Toast.LENGTH_SHORT
								).show();							
								return;
							}
						}
						catch(Exception e)
						{
							Toast.makeText
							(
									GJXQActivity.this,
									"服務器IP地址不合法!", 
									Toast.LENGTH_SHORT
							).show();							
							return;
						}
					}
					//端口號本地驗證
					try
					{
						int port=Integer.parseInt(portStr);
						if(port>65535||port<0)
						{
							Toast.makeText
							(
									GJXQActivity.this,
									"服務器端口號不合法!", 
									Toast.LENGTH_SHORT
							).show();							
							return;
						}						
					}
					catch(Exception e)
					{
						Toast.makeText
						(
								GJXQActivity.this,
								"服務器端口號不合法!", 
								Toast.LENGTH_SHORT
						).show();							
						return;
					}	
					
					//驗證過關
					int port=Integer.parseInt(portStr);
					try
					{
						Socket sc=new Socket(ipStr,port);
						DataInputStream din=new DataInputStream(sc.getInputStream());
						DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
						ca=new ClientAgent
						(
								GJXQActivity.this,
								sc,
								din,
								dout
						);
						ca.start();
					}
					catch(Exception e)
					{
						Toast.makeText
						(
								GJXQActivity.this,
								"網絡連接失敗,請稍後重試!", 
								Toast.LENGTH_SHORT
						).show();	
						return;	
					}	
				}    			
    		} 
        );
        bfh.setOnClickListener
        (
    		new  OnClickListener()
    		{
				@Override
				public void onClick(View v) 
				{
					goToMainMenu();//進入主菜單
				}    			
    		}
        );
    	
    	curr=WhichView.IP_VIEW;
    }
    //進入遊戲界面
    public void gotoGameView()
    {
    	 msv=new MySurfaceView(this); 
         msv.requestFocus();//獲取焦點
         msv.setFocusableInTouchMode(true);//設置為可觸控
         initSound();//初始化聲音池
         setContentView(msv);    	
    	 curr=WhichView.GL_VIEW;    	
    }
    //進入加載界面
    public void gotoLoadView()
    {
    	setContentView(R.layout.load);
    	curr=WhichView.LOAD_VIEW;
    }
    
    //進入幫助界面
    public void gotoHelpView()
    {
    	setContentView(R.layout.help);
    	curr=WhichView.HELP_VIEW;
    }
    //進入關於界面
    public void gotoAboutView()
    {
    	setContentView(R.layout.about);
    	curr=WhichView.ABOUT_VIEW;
    }
    //返回主菜單
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent e)
    { 
    	if(keyCode!=4)
    	{
    		return false;
    	}	
    	if(curr==WhichView.WIN_VIEW||curr==WhichView.IP_VIEW||
    	   curr==WhichView.EXIT_VIEW||curr==WhichView.FULL_VIEW||
    	   curr==WhichView.ABOUT_VIEW||curr==WhichView.HELP_VIEW)
    	{
    		goToMainMenu();
    		return true;
    	}
    	if(curr==WhichView.MAIN_MENU)
    	{
    		System.exit(0);
    	}
    	if(curr==WhichView.GL_VIEW)
    	{
    		try 
    		{
				ca.dout.writeUTF("<#EXIT#>");
			}
    		catch (IOException e1) 
			{
				e1.printStackTrace();
			}
    		return true;
    	}
    	if(curr==WhichView.LOAD_VIEW||curr==WhichView.WAIT_VIEW)
    	{
    		return true;
    	}
    	return false;
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
	    AudioManager mgr = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);   
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);   
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);       
	    float volume = streamVolumeCurrent / streamVolumeMax;   
	    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, loop, 1f);
	}
}









