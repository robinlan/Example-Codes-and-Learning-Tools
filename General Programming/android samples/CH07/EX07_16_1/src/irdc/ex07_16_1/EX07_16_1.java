package irdc.ex07_16_1; 

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity; 
import android.content.pm.ActivityInfo; 
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat; 
import android.hardware.Camera; 
import android.hardware.Camera.PictureCallback; 
import android.hardware.Camera.ShutterCallback; 
import android.os.Bundle; 
import android.os.Environment;
import android.view.SurfaceHolder; 
import android.view.SurfaceView; 
import android.view.View; 
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button; 
import android.widget.Toast;

public class EX07_16_1 extends Activity implements SurfaceHolder.Callback 
{
  private Camera mCamera; 
  private Button mButton,mButton1,mButton2;
  private SurfaceView mSurfaceView; 
  private SurfaceHolder holder; 
  private AutoFocusCallback mAutoFocusCallback = 
                            new AutoFocusCallback(); 
  private String path="MyPhoto";
  private Bitmap bmp;
  private int cnt=1;
  /** Called when the activity is first created. */ 
  @Override 
  public void onCreate(Bundle savedInstanceState) 
  { 
    super.onCreate(savedInstanceState); 
    /* 隱藏狀態列 */
    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                              WindowManager.LayoutParams.FLAG_FULLSCREEN);
    /* 隱藏標題列 */
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    /* 設定螢幕顯示為橫向 */
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
    
    setContentView(R.layout.main);
    /* SurfaceHolder設定 */ 
    mSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView); 
    holder = mSurfaceView.getHolder();
    holder.addCallback(EX07_16_1.this); 
    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    /* Button初始化 */
    mButton = (Button)findViewById(R.id.myButton);
    mButton1 = (Button)findViewById(R.id.myButton1);  
    mButton2 = (Button)findViewById(R.id.myButton2);  
    /* 拍照Button的事件處理 */
    mButton.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View arg0) 
      { 
        /* 自動對焦後拍照 */
        mCamera.autoFocus(mAutoFocusCallback);
      }
    });
    /* 存檔Button的事件處理 */
    mButton1.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View arg0) 
      { 
        /* 儲存檔案 */
        if(bmp!=null)
        {
          /* 檢視SDCard是否存在 */ 
          if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) 
          { 
            /* SD卡不存在，顯示Toast訊息 */
            Toast.makeText(EX07_16_1.this,"SD卡不存在!無法儲存相片",
                           Toast.LENGTH_LONG).show();
          }
          else
          {
            try
            {
              /* 資料夾不在就先建立 */
              File f=new File
              (
                Environment.getExternalStorageDirectory(),path
              );
              
              if(!f.exists())
              {
                f.mkdir();
              }
              /* 儲存相片檔 */
              File n=new File(f,cnt+".jpg");
              FileOutputStream bos = 
                new FileOutputStream(n.getAbsolutePath());
              /* 檔案轉換 */
              bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
              /* 呼叫flush()方法，更新BufferStream */
              bos.flush();
              /* 結束OutputStream */
              bos.close();
              Toast.makeText(EX07_16_1.this,cnt+".jpg儲存成功!",
                  Toast.LENGTH_LONG).show();
              cnt++;
            }
            catch (Exception e)
            {
              e.printStackTrace();
            }
          }
        }
        
        mButton.setVisibility(View.VISIBLE);
        mButton1.setVisibility(View.GONE);
        mButton2.setVisibility(View.GONE);
        /* 重新設定Camera */
        stopCamera(); 
        initCamera();
      } 
    }); 
    /* 放棄Button的事件處理 */
    mButton2.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View arg0) 
      { 
        mButton.setVisibility(View.VISIBLE);
        mButton1.setVisibility(View.GONE);
        mButton2.setVisibility(View.GONE);
        /* 重新設定Camera */
        stopCamera(); 
        initCamera();
      } 
    }); 
  }
  
  @Override 
  public void surfaceCreated(SurfaceHolder surfaceholder) 
  { 
    try
    {
      /* 打開相機， */
      mCamera = Camera.open();
      mCamera.setPreviewDisplay(holder);
    }
    catch (IOException exception)
    {
      mCamera.release();
      mCamera = null;
    }     
  } 
  
  @Override 
  public void surfaceChanged(SurfaceHolder surfaceholder,
                             int format,int w,int h) 
  {
    /* 相機初始化 */
    initCamera();
  } 

  @Override 
  public void surfaceDestroyed(SurfaceHolder surfaceholder) 
  { 
    stopCamera();
    mCamera.release();
    mCamera = null;
  }

  /* 拍照的method */
  private void takePicture()  
  { 
    if (mCamera != null)  
    { 
      mCamera.takePicture(shutterCallback, rawCallback,jpegCallback); 
    } 
  }

  private ShutterCallback shutterCallback = new ShutterCallback()  
  {  
    public void onShutter()  
    {  
      /* 按下快門瞬間會呼叫這裡的程式 */
    }  
  };  
    
  private PictureCallback rawCallback = new PictureCallback()  
  {  
    public void onPictureTaken(byte[] _data, Camera _camera)  
    {  
      /* 要處理raw data可寫在此 */
    }  
  };  

  private PictureCallback jpegCallback = new PictureCallback()  
  { 
    public void onPictureTaken(byte[] _data, Camera _camera) 
    { 
      /* 取得相片 */
      try 
      { 
        /* 設定Button可視性 */
        mButton.setVisibility(View.GONE);
        mButton1.setVisibility(View.VISIBLE);
        mButton2.setVisibility(View.VISIBLE);
        /* 取得相片Bitmap物件 */
        bmp = BitmapFactory.decodeByteArray(_data, 0,_data.length); 
      } 
      catch (Exception e) 
      { 
        e.printStackTrace(); 
      } 
    } 
  };
  
  /* 自定義class AutoFocusCallback */
  public final class AutoFocusCallback implements android.hardware.Camera.AutoFocusCallback 
  { 
    public void onAutoFocus(boolean focused, Camera camera) 
    { 
      /* 對到焦才拍照 */
      if (focused) 
      { 
        takePicture();
      } 
    } 
  };
  
  /* 相機初始化的method */
  private void initCamera() 
  { 
    if (mCamera != null) 
    {
      try 
      { 
        Camera.Parameters parameters = mCamera.getParameters(); 
        /* 設定相片大小為1024*768，
                             格式為JPG */
        parameters.setPictureFormat(PixelFormat.JPEG); 
        parameters.setPictureSize(1024,768);
        mCamera.setParameters(parameters); 
        /* 開啟預覽畫面 */
        mCamera.startPreview(); 
      } 
      catch (Exception e) 
      { 
          e.printStackTrace(); 
      } 
    } 
  } 

  /* 停止相機的method */ 
  private void stopCamera() 
  { 
    if (mCamera != null) 
    { 
      try 
      { 
        /* 停止預覽 */
        mCamera.stopPreview(); 
      } 
      catch(Exception e) 
      { 
        e.printStackTrace(); 
      } 
    } 
  }
}