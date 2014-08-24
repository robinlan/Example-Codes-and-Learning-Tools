package irdc.ex03_23_1;

import java.util.Locale;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class EX03_23_1 extends Activity
{
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
  }

  @Override
  protected void onStart()
  {
    Resources res = getResources();
    /* 更改語系為JAPAN */
    Configuration conf = res.getConfiguration();
    conf.locale = Locale.JAPAN;
    DisplayMetrics dm = res.getDisplayMetrics();
    /* 儲存語系變更 */
    res.updateConfiguration(conf, dm);
    
    super.onStart(); 
  }
}