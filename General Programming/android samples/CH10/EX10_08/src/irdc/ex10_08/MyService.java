package irdc.ex10_08;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;

/* 自定義MyService繼承Service */
public class MyService extends Service
{
  private String MY_PREFS = "MosPre";
  private NotificationManager notiManager;
  private int mosStatus;
  private int notiId=99;
  private MediaPlayer myPlayer;
    
  @Override
  public void onCreate()
  {
    try
    {
      /* 取得NotificationManager */
      notiManager=
        (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
      /* Create MediaPlayer */
      myPlayer=new MediaPlayer();
      myPlayer = MediaPlayer.create(MyService.this, R.raw.killmosall);
      
      /* 讀取防蚊服務狀態(1:啟動，0:關閉) */
      SharedPreferences pres = 
        getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE);
      if(pres !=null)
      {
        mosStatus = pres.getInt("status", 0);
      }  
      
      if(mosStatus==1)
      {
        /* 加一個Notification */
        setNoti(R.drawable.antimos,notiId,"防蚊服務啟動");     
        /* 播放防蚊鈴聲 */
        if(!myPlayer.isPlaying())
        {
          myPlayer.seekTo(0);
          myPlayer.setLooping(true);
          myPlayer.start();
        }
      }
      else if(mosStatus==0)
      {
        /* 刪除Notification */
        deleteNoti(notiId);
        /* 關閉防蚊鈴聲 */
        if(myPlayer.isPlaying())
        {
          myPlayer.setLooping(false);
          myPlayer.pause();
        }
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    super.onCreate();
  }
  
  @Override
  public void onDestroy()
  {
    try
    {
      /* Service關閉時釋放MediaPlayer，
       * 並刪除Notification */
      myPlayer.release();
      deleteNoti(notiId);
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    super.onDestroy();
  }

  
  /* 新增Notification的method */
  public void setNoti(int iconImg,int iconId,String icontext)
  {
    /* 建立點選Notification留言條時，會執行的Activity */
    Intent notifyIntent=new Intent(this,EX10_08.class);  
    notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
    /* 建立PendingIntent作為設定遞延執行的Activity */ 
    PendingIntent appIntent=PendingIntent.getActivity(this,0,notifyIntent,0); 
    /* 建立Notification，並設定相關參數 */ 
    Notification myNoti=new Notification();
    /* 設定status bar顯示的icon */
    myNoti.icon=iconImg;
    /* 設定notification發生時同時發出預設聲音 */
    myNoti.defaults=Notification.DEFAULT_SOUND;
    myNoti.setLatestEventInfo(this,"防蚊服務啟動",icontext,appIntent);
    /* 送出Notification */
    notiManager.notify(iconId,myNoti);
  } 
  
  /* 刪除Notification的method */
  public void deleteNoti(int iconId)
  {
    notiManager.cancel(iconId);
  }
  
  @Override
  public IBinder onBind(Intent arg0)
  {
    return null;
  }
}