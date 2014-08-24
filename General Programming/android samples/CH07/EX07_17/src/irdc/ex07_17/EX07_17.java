package irdc.ex07_17;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class EX07_17 extends Activity
{
  private VideoView v;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* 載入start.xml Layout */
    setContentView(R.layout.start);
    /* 設定VideoView */
    v = (VideoView) findViewById(R.id.mVideoView1);
    Uri uri = Uri.parse
              (
                "android.resource://irdc.ex07_17/"+ R.raw.start
              );
    v.setVideoURI(uri);
    v.requestFocus(); 
    /* 開始撥放影片 */
    v.start();
    
    /* 影片撥完後會執行的OnCompletionListener */
    v.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
    {
      @Override
      public void onCompletion(MediaPlayer arg0)
      {
        /* 開啟Activity EX07_17_1.java */
        Intent intent = new Intent();
        intent.setClass(EX07_17.this,EX07_17_1.class);
        startActivity(intent);
        EX07_17.this.finish();
      }
    });
  }
}