package irdc.ex03_22;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

public class EX03_22 extends Activity
{
  private EditText et;
  private CheckBox cb;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* 載入main.xml Layout */
    setContentView(R.layout.main);
    /* 以findViewById()取得物件 */
    et=(EditText)findViewById(R.id.mPassword);
    cb=(CheckBox)findViewById(R.id.mCheck);
    /* 設定CheckBox的OnCheckedChangeListener */
    cb.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener()
    {
      @Override
      public void onCheckedChanged(CompoundButton arg0, boolean arg1)
      {
        if(cb.isChecked())
        {
          /* 設定EditText的內容為可見的 */
          et.setTransformationMethod(
              HideReturnsTransformationMethod.getInstance());
        }
        else
        {
          /* 設定EditText的內容為隱藏的 */
          et.setTransformationMethod(
              PasswordTransformationMethod.getInstance());
        }
      }
    });
  }
}