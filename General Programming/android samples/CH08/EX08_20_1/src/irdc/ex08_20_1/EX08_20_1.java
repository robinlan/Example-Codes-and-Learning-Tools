package irdc.ex08_20_1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/* 實作Runnable Interface */
public class EX08_20_1 extends Activity implements Runnable
{
  private ProgressDialog d;
  private TextView tv;
  private Button b1;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* 載入main.xml Layout */
    setContentView(R.layout.main);
    /* 物件初始化 */
    tv = (TextView) findViewById(R.id.text1);
    b1 = (Button) findViewById(R.id.button1);
    
    b1.setOnClickListener(new OnClickListener()
    {
      public void onClick(View v)
      {
        tv.setText("");
        /* 跳出ProgressDialog */
        d=new ProgressDialog(EX08_20_1.this);
        /* 設定ProgressDialog的style為條狀 */
        d.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        /* 設定最大單位 */
        d.setMax(100);
        /* 設定初始值 */
        d.setProgress(0);
        d.setMessage("檔案下載中..");
        d.show();
        /* 啟動另一個Thread，執行run() */
        Thread thread = new Thread(EX08_20_1.this);
        thread.start();
      }
    });
    
  }
  
  /* Handler建構之後，會聆聽傳來的訊息代碼  */
  private Handler handler = new Handler()
  {
    @Override 
    public void handleMessage(Message msg)
    { 
      /* 取得回傳的進度值 */
      int p=msg.getData().getInt("percent");
      if(p==100)
      {
        /* 下載完成關閉ProgressDialog */
        tv.setText("下載完成!");
        d.dismiss();
      }
      else
      {
        /* 下載中UPDATE進度數值 */
        d.setProgress(p);
      }
    }
  };

  @Override
  public void run()
  {
    /* 模擬檔案下載耗時約10秒 */
    try
    {
      for(int i=0;i<10;i++)
      {
        /* 每執行一次迴圈，即暫停1秒 */
        Thread.sleep(1000);
        
        /* 迴圈每執行一次進度加10% */
        int percent=(i+1)*10;
        Message m=new Message();
        Bundle data=m.getData();
        /* 將進度放入Message中 */
        data.putInt("percent",percent);
        m.setData(data);
        /* send Message */
        handler.sendMessage(m);
      }
    }
    catch (InterruptedException e)
    {
      e.printStackTrace();
    }
  }
}