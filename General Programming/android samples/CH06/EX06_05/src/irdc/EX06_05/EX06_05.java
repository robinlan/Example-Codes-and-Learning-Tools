package irdc.EX06_05;

import android.app.Activity; 
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle; 
import android.telephony.SmsMessage;
import android.widget.TextView; 
import android.widget.Toast;

public class EX06_05 extends Activity 
{
  /*宣告一個TextView,String陣列與兩個文字字串變數*/
  private TextView mTextView1; 
  public String[] strEmailReciver;
  public String strEmailSubject;
  public String strEmailBody;
  /* 系統接收簡訊的廣播ACTION常數 */
  private static final String HIPPO_SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
  private mSMSReceiver mReceiver01;
  
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    setContentView(R.layout.main); 
    
    /*透過findViewById建構子建立TextView物件*/ 
    mTextView1 = (TextView) findViewById(R.id.myTextView1); 
    mTextView1.setText("等待接收簡訊..."); 
    
  }
  
  public class mSMSReceiver extends BroadcastReceiver 
  { 
    /*宣告靜態字串,並使用android.provider.Telephony.SMS_RECEIVED作為Action為簡訊的依據*/
    private static final String mACTION = "android.provider.Telephony.SMS_RECEIVED"; 
    private String str_receive="收到簡訊!";
    private String strRet = "";
    
    @Override 
    public void onReceive(Context context, Intent intent) 
    {
      // TODO Auto-generated method stub 
      Toast.makeText(context, str_receive.toString(), Toast.LENGTH_LONG).show(); 
      /*判斷傳來Intent是否為簡訊*/
      if (intent.getAction().equals(mACTION)) 
      { 
        /*建構一字串集合變數sb*/
        StringBuilder sb = new StringBuilder(); 
        /*接收由Intent傳來的資料*/
        Bundle bundle = intent.getExtras(); 
        /*判斷Intent是有資料*/
        if (bundle != null) 
        { 
          /* pdus為 android內建簡訊參數 identifier
           * 透過bundle.get("")回傳一包含pdus的物件*/
          Object[] myOBJpdus = (Object[]) bundle.get("pdus"); 
          
          /*建構簡訊物件array,並依據收到的物件長度來建立array的大小*/
          SmsMessage[] messages = new SmsMessage[myOBJpdus.length];  
          
          for (int i = 0; i<myOBJpdus.length; i++) 
          {  
            messages[i] = SmsMessage.createFromPdu ((byte[]) myOBJpdus[i]);  
          }
          
          strRet = "";
          /* 將送來的簡訊合併自訂訊息於StringBuilder當中 */  
          for (SmsMessage currentMessage : messages) 
          {
            strRet = "接收到來自:"+currentMessage.getDisplayOriginatingAddress()+" 傳來的簡訊"+currentMessage.getDisplayMessageBody();
            sb.append("接收到來自:\n");  
            /* 來訊者的電話號碼 */ 
            sb.append(currentMessage.getDisplayOriginatingAddress());  
            sb.append("\n------傳來的簡訊------\n");  
            /* 取得傳來訊息的BODY */  
            sb.append(currentMessage.getDisplayMessageBody());
          }  
        }       
        /* 以Notification(Toase)顯示來訊訊息  */
        Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
        
        /*自訂一Intent來執行寄送E-mail的工作*/
        Intent mEmailIntent = new Intent(android.content.Intent.ACTION_SEND);  
        /*設定郵件格式為"plain/text"*/
        mEmailIntent.setType("plain/text");
        
        /*取得EditText01,02,03,04的值作為收件人地址,附件,主旨,內文*/
        String strEmailReciver = "jay.mingchieh@gmail.com";
        String strEmailSubject = "你有一封簡訊!!";
        
        /*將取得的字串放入mEmailIntent中*/
        mEmailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, strEmailReciver); 
        mEmailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, strEmailSubject);
        mEmailIntent.putExtra(android.content.Intent.EXTRA_TEXT, strRet);
        context.startActivity(Intent.createChooser(mEmailIntent, getResources().getString(R.string.str_message)));
        mTextView1.setText(getResources().getString(R.string.str_message));
      }
    } 
  }

  @Override
  protected void onPause()
  {
    // TODO Auto-generated method stub
    super.onPause();
    unregisterReceiver(mReceiver01);
  }
  
  @Override
  protected void onResume()
  {
    // TODO Auto-generated method stub
    IntentFilter mFilter01;
    mFilter01 = new IntentFilter(HIPPO_SMS_ACTION);
    mReceiver01 = new mSMSReceiver();
    registerReceiver(mReceiver01, mFilter01);
    super.onResume();
  }
  
}


