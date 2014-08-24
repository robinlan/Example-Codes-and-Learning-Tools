package irdc.ex10_08;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class EX10_08 extends Activity
{
  public static final String MY_PREFS = "MosPre";
  private ImageButton button01;
  private ImageView image01;
  private int mosStatus;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* 載入main.xml Layout */
    setContentView(R.layout.main);
    
    /* 取得儲存於SharedPreferences的防蚊狀態 */
    SharedPreferences pres = 
      getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE);
    if(pres !=null)
    {
      mosStatus = pres.getInt("status", 0);
    }
    
    image01 = (ImageView)findViewById(R.id.image01);
    button01 = (ImageButton)findViewById(R.id.button01);
    
    /*檢查mosStatus是否為開啟狀態(1) */
    if (mosStatus==1)
    {
      /* 設定開啟圖案 */
      image01.setImageResource(R.drawable.mos_open);
      button01.setBackgroundResource(R.drawable.power_on);
    }
    else
    {
      /* 設定關閉圖案 */
      image01.setImageResource(R.drawable.mos_close);
      button01.setBackgroundResource(R.drawable.power_off);
    }
    
    button01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        if (mosStatus==1)
        {
          SharedPreferences pres = 
            getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE);
          if(pres!=null)
          {
            /* 設定狀態為關閉(0) */
            mosStatus=0;
            SharedPreferences.Editor ed = pres.edit(); 
            ed.putInt("status",mosStatus); 
            ed.commit();
          }
          /* 設定關閉圖案 */
          image01.setImageResource(R.drawable.mos_close);
          button01.setBackgroundResource(R.drawable.power_off);
          /* 終止service */
          stopMyService(1);
        }
        else if(mosStatus==0)
        {
          SharedPreferences pres = 
            getSharedPreferences(MY_PREFS,Context.MODE_PRIVATE);
          if(pres!=null)
          {
            /* 設定狀態為開啟(1) */
            mosStatus=1;
            SharedPreferences.Editor ed = pres.edit(); 
            ed.putInt("status",mosStatus); 
            ed.commit();
          }
          /*設定開啟圖案*/
          image01.setImageResource(R.drawable.mos_open);
          button01.setBackgroundResource(R.drawable.power_on);
          /* 啟動service */
          startMyService();
        }
        else
        {
          Toast.makeText(EX10_08.this,"系統錯誤",Toast.LENGTH_LONG)
            .show();
        }  
      }
    });
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {    
    /* 加入離開的menu */
    menu.add(0,1,1,"").setIcon(R.drawable.menu_exit); 
    return super.onCreateOptionsMenu(menu);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch(item.getItemId())
    {
      case (1):
        /* 離開前ALERT提醒 */
        new AlertDialog.Builder(EX10_08.this)
        .setTitle("Message")
        .setMessage("確定要離開嗎？")
        .setPositiveButton("確定",
          new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface dialoginterface,int i)
            {           
              finish();
            }
          }
        ).setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialoginterface, int i)   
          {
          }
        }).show();
        break;
    }
    return super.onOptionsItemSelected(item);
  }
  
  public void startMyService()
  {
    try
    {
      /* 先終止之前可能還在執行的service */
      stopMyService(0);
      /* 啟動MyService */
      Intent intent = new Intent( EX10_08.this, MyService.class); 
      intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK ); 
      startService(intent);
      Toast.makeText(EX10_08.this,getResources().getString(R.string.start),
                     Toast.LENGTH_LONG).show();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  
  public void stopMyService(int flag)
  {
    try
    {
      /* 停止MyService */
      Intent intent = new Intent( EX10_08.this, MyService.class );
      stopService(intent);
      if(flag==1)
      {
        Toast.makeText(EX10_08.this,getResources().getString(R.string.stop),
                       Toast.LENGTH_LONG).show();
      }
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
}