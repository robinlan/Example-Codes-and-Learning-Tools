package irdc.ex10_11_1;

import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EX10_11_1 extends Activity
{

  private TextView TextView01;
  private Button Button01, Button02;
  private EditText EditText01;
  private WebView WebView01;
  private Handler mHandler01 = new Handler();
  private TextToSpeech tts;

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    TextView01 = (TextView) this.findViewById(R.id.TextView01);
    Button01 = (Button) this.findViewById(R.id.Button01);
    Button02 = (Button) this.findViewById(R.id.myButton2);
    /* 先將發音按鈕不顯示 */
    Button02.setVisibility(View.INVISIBLE);
    EditText01 = (EditText) this.findViewById(R.id.EditText01);
    EditText01.setText("範例");
    WebView01 = (WebView) this.findViewById(R.id.myWebView1);

    /* 取得WebSettings */
    WebSettings webSettings = WebView01.getSettings();
    /* 設定可執行JavaScript */
    webSettings.setJavaScriptEnabled(true);
    /* 設定給html呼叫的物件及名稱 */
    WebView01.addJavascriptInterface(new runJavaScript(), "irdc");
    /* 將assets/google_translate.html載入 */
    WebView01.loadUrl("file:///android_asset/google_translate.html");
    /* 傳入context及OnInitListener */
    tts = new TextToSpeech(this, ttsInitListener);

    Button01.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        TextView01.setText("");
        Button02.setVisibility(View.INVISIBLE);
        if (EditText01.getText().toString().length() > 0)
        {
          /* 呼叫google_translate.html裡的javascript */
          WebView01.loadUrl("javascript:google_translate('"
              + EditText01.getText().toString() + "')");
        }
      }
    });

    Button02.setOnClickListener(new Button.OnClickListener()
    {
      @Override
      public void onClick(View v)
      {
        // TODO Auto-generated method stub
        if (TextView01.getText().toString().length() > 0)
        {
          String txt = TextView01.getText().toString();
          HashMap<String, String> myHash = new HashMap();
          String fileName = "/sdcard/" + txt + ".wav";
          /* 給予一個ID可在onUtteranceCompleted取得 */
          myHash.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID,
              "UTTERANCE_ID_01");
          /* 儲存檔案 */
          tts.synthesizeToFile(txt, myHash, fileName);
          tts.addSpeech(txt, fileName);
          /* 傳入要說的字串 */
          tts.speak(TextView01.getText().toString(), TextToSpeech.QUEUE_FLUSH,
              myHash);
        } else
        {
          tts.speak("Nothing to say", TextToSpeech.QUEUE_FLUSH, null);
        }
      }
    });

  }

  final class runJavaScript
  {
    public void runOnAndroidJavaScript(final String strRet)
    {
      mHandler01.post(new Runnable()
      {
        public void run()
        {
          if (!strRet.equals(""))
          {
            TextView01.setText(strRet);
            /* 顯示發音按鈕 */
            Button02.setVisibility(View.VISIBLE);
          } else
          {
            TextView01.setText("找不到請重按英文按鈕");
          }
        }
      });
    }
  }

  private TextToSpeech.OnInitListener ttsInitListener = new TextToSpeech.OnInitListener()
  {
    @Override
    public void onInit(int status)
    {
      // TODO Auto-generated method stub
      /* 使用美國時區目前不支援中文 */
      Locale loc = new Locale("us", "", "");
      /* 檢查是否支援輸入的時區 */
      if (tts.isLanguageAvailable(loc) == TextToSpeech.LANG_AVAILABLE)
      {
        /* 設定語言 */
        tts.setLanguage(loc);
      }
      tts.setOnUtteranceCompletedListener(ttsUtteranceCompletedListener);
    }
  };
  private TextToSpeech.OnUtteranceCompletedListener ttsUtteranceCompletedListener = new TextToSpeech.OnUtteranceCompletedListener()
  {
    @Override
    public void onUtteranceCompleted(String utteranceId)
    {
      // TODO Auto-generated method stub
    }
  };

  @Override
  protected void onDestroy()
  {
    // TODO Auto-generated method stub
    /* 釋放TextToSpeech的資源 */
    tts.shutdown();
    super.onDestroy();
  }
}