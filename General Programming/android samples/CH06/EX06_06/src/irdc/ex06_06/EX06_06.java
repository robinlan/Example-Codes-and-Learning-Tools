package irdc.ex06_06;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class EX06_06 extends Activity
{
  private TextView myTextView1;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    myTextView1 = (TextView) findViewById(R.id.myTextView1);

    /* 新增自己實做的PhoneStateListener */
    exPhoneCallListener myPhoneCallListener = new exPhoneCallListener();
    /* 取得電話服務 */
    TelephonyManager tm = (TelephonyManager) this
        .getSystemService(Context.TELEPHONY_SERVICE);
    /* 註冊Listener */
    tm.listen(myPhoneCallListener, PhoneStateListener.LISTEN_CALL_STATE);

  }

  /* 內部class繼承PhoneStateListener */
  public class exPhoneCallListener extends PhoneStateListener
  {
    /* 覆寫onCallStateChanged當狀態改變時改變myTextView1的文字及顏色 */
    public void onCallStateChanged(int state, String incomingNumber)
    {
      switch (state)
      {
        /* 無任何狀態時 */
        case TelephonyManager.CALL_STATE_IDLE:
          myTextView1.setTextColor(getResources().getColor(R.drawable.red));
          myTextView1.setText("CALL_STATE_IDLE");
          break;
        /* 接起電話時 */
        case TelephonyManager.CALL_STATE_OFFHOOK:
          myTextView1.setTextColor(getResources().getColor(R.drawable.green));
          myTextView1.setText("CALL_STATE_OFFHOOK");
          break;
        /* 電話進來時 */
        case TelephonyManager.CALL_STATE_RINGING:
          getContactPeople(incomingNumber);
          break;
        default:
          break;
      }
      super.onCallStateChanged(state, incomingNumber);
    }
  }

  private void getContactPeople(String incomingNumber)
  {
    myTextView1.setTextColor(Color.BLUE);
    ContentResolver contentResolver = getContentResolver();
    Cursor cursor = null;

    /* cursor裡要放的欄位名稱 */
    String[] projection = new String[]
    { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER };

    /* 用來電電話號碼去找該連絡人 */
    cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection,
        ContactsContract.CommonDataKinds.Phone.NUMBER + "=?", new String[]
        { incomingNumber }, "");

    /* 找不倒連絡人 */
    if (cursor.getCount() == 0)
    {
      myTextView1.setText("unknown Number:" + incomingNumber);
    } else if (cursor.getCount() > 0)
    {
      cursor.moveToFirst();
      /* 在projection這個陣列裡名字是放在第1個位置 */
      String name = cursor.getString(1);
      myTextView1.setText(name + ":" + incomingNumber);
    }

  }
}