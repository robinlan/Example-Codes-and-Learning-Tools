package irdc.ex05_25; 

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import android.app.Activity; 
import android.gesture.Gesture; 
import android.gesture.GestureLibraries; 
import android.gesture.GestureLibrary; 
import android.gesture.GestureOverlayView; 
import android.os.Bundle; 
import android.os.Environment;  
import android.view.KeyEvent;
import android.view.MotionEvent; 
import android.view.View; 
import android.widget.Button; 
import android.widget.EditText; 
import android.widget.Toast; 

public class EX05_25 extends Activity 
{
  private Gesture ges;
  private GestureLibrary lib; 
  private GestureOverlayView overlay; 
  private Button button01,button02; 
  private EditText et;
  private String gesPath;
  
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState);
    /* 載入main.xml Layout */
    setContentView(R.layout.main); 
     
    /* 檢視SDCard是否存在 */ 
    if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) 
    { 
      /* SD卡不存在，顯示Toast訊息 */
      Toast.makeText(EX05_25.this,"SD卡不存在!程式無法執行",
                     Toast.LENGTH_LONG).show(); 
      finish(); 
    }
    /* 以findViewById()取得物件 */
    et = (EditText)this.findViewById(R.id.myEditText1); 
    button01 = (Button)this.findViewById(R.id.myButton1); 
    button02 = (Button)this.findViewById(R.id.myButton2); 
    overlay = (GestureOverlayView) findViewById(R.id.myGestures1); 
    
    /* 取得GestureLibrary的檔案路徑 */
    gesPath = new File
    (
      Environment.getExternalStorageDirectory(),"gestures" 
    ).getAbsolutePath();

    /* 設定EditText的OnKeyListener */
    et.setOnKeyListener(new EditText.OnKeyListener(){
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event)
      {
        /* 名稱與手勢都已設定時將新增的Button enable */
        if(ges!=null&&et.getText().length()!=0)
        {
          button01.setEnabled(true);
        }
        else
        {
          button01.setEnabled(false);
        }
        return false;
      }
    });
    
    /* 設定Overlay的OnGestureListener */
    overlay.addOnGestureListener(new GestureOverlayView.OnGestureListener()
    {
      @Override
      public void onGesture(GestureOverlayView overlay,MotionEvent event) 
      {
      }
      /* 開始畫手勢時將新增的Button disable，並清除Gesture */
      @Override
      public void onGestureStarted(GestureOverlayView overlay,MotionEvent event) 
      { 
        button01.setEnabled(false); 
        ges = null; 
      }
      /* 手勢畫完時判斷名稱與手勢是否完整建立 */
      @Override
      public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) 
      { 
        ges = overlay.getGesture(); 
        if (ges!=null&&et.getText().length()!=0) 
        { 
          button01.setEnabled(true);  
        }
      } 
      @Override
      public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) 
      { 
      }
    });
        
    /* 設定button01的OnClickListener */
    button01.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        String gesName=et.getText().toString();       
        try 
        { 
          File file = new File(gesPath);
          lib = GestureLibraries.fromFile(gesPath); 
          
          if(!file.exists())
          {
            /* 檔案不存在就直接寫入 */
            lib.addGesture(gesName,ges);
            if(lib.save()) 
            { 
              /* 將設定畫面資料清除 */
              et.setText(""); 
              button01.setEnabled(false);
              overlay.clear(true); 
              /* 儲存成功，顯示Toast訊息 */
              Toast.makeText(EX05_25.this,getString(R.string.save_success)+":"+gesPath,
                             Toast.LENGTH_LONG).show(); 
            }
            else 
            {
              /* 儲存失敗，顯示Toast訊息 */
              Toast.makeText(EX05_25.this, getString(R.string.save_failed)+":"+gesPath,
                             Toast.LENGTH_LONG).show(); 
            } 
          }
          else
          {
            /* 檔案存在時先讀取已儲存的Gesture */
            if (!lib.load()) 
            { 
              /* Library讀取失敗，顯示Toast訊息 */
              Toast.makeText(EX05_25.this, getString(R.string.load_failed)+":"+gesPath,
                             Toast.LENGTH_LONG).show(); 
            }
            else
            {
              /* 如果Library中存在相同名稱，則先將其移除再寫入 */
              Set<String> en=lib.getGestureEntries();
              if(en.contains(gesName))
              {
                ArrayList<Gesture> al=lib.getGestures(gesName);
                for(int i=0;i<al.size();i++){
                  lib.removeGesture(gesName,al.get(i)); 
                }
              } 
              lib.addGesture(gesName,ges);
              if(lib.save()) 
              { 
                /* 將設定畫面資料清除 */
                et.setText(""); 
                button01.setEnabled(false);
                overlay.clear(true);
                /* 儲存成功，顯示Toast訊息 */
                Toast.makeText(EX05_25.this,getString(R.string.save_success)+":"+gesPath,
                               Toast.LENGTH_LONG).show(); 
              }
              else 
              {  
                /* 儲存失敗，顯示Toast訊息 */
                Toast.makeText(EX05_25.this, getString(R.string.save_failed)+":"+gesPath,
                               Toast.LENGTH_LONG).show(); 
              } 
            }
          }
        }
        catch(Exception e) 
        { 
          e.printStackTrace(); 
        } 
      } 
    });
    /* 設定button02的OnClickListener */
    button02.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View v) 
      { 
        et.setText(""); 
        button01.setEnabled(false); 
        overlay.clear(true); 
      } 
    }); 
  }
} 