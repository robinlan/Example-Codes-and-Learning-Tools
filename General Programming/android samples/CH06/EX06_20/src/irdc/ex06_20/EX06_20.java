package irdc.ex06_20;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class EX06_20 extends ListActivity
{
  private ListAdapter mListAdapter;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    /* 取得通訊錄裡的資料 */
    Cursor cursor = getContentResolver().query(
        ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    /* 取得筆數 */
    int c = cursor.getCount();
    if (c == 0)
    {
      Toast.makeText(EX06_20.this, "連絡人無資料\n請至連絡人新增資料", Toast.LENGTH_LONG)
          .show();
    }

    /* 用Activity管理Cursor */
    startManagingCursor(cursor);

    /* 欲顯示的欄位名稱 */
    String[] columns =
    { ContactsContract.Contacts.DISPLAY_NAME };

    /* 欲顯示欄位名稱的view */
    int[] entries =
    { android.R.id.text1 };

    mListAdapter = new SimpleCursorAdapter(this,
        android.R.layout.simple_list_item_1, cursor, columns, entries);
    /* 設定Adapter */
    setListAdapter(mListAdapter);

  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id)
  {
    // TODO Auto-generated method stub

    /* 取得點選的Cursor */
    Cursor c = (Cursor) mListAdapter.getItem(position);

    /* 取得_id這個欄位得值 */
    int contactId = c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID));

    /* 用_id去查詢電話的Cursor */
    Cursor phones = getContentResolver().query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
        null, null);

    StringBuffer sb = new StringBuffer();
    int type, typeLabelResource;
    String number;

    if (phones.getCount() > 0)
    {

      /* 2.0可以允許User設定多組電話號碼，依序撈出 */
      while (phones.moveToNext())
      {
        /* 取得電話的TYPE */
        type = phones.getInt(phones
            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
        /* 取得電話號碼 */
        number = phones.getString(phones
            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        /* 由電話的TYPE找出LabelResource */
        typeLabelResource = ContactsContract.CommonDataKinds.Phone
            .getTypeLabelResource(type);

        sb.append(getString(typeLabelResource) + ": " + number + "\n");

      }
    } else
    {
      sb.append("no Phone number found");
    }

    Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show();

    super.onListItemClick(l, v, position, id);
  }

}