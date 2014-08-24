package irdc.ex07_16; 

import java.io.IOException;
import android.app.Activity; 
import android.content.pm.ActivityInfo; 
import android.graphics.PixelFormat; 
import android.hardware.Camera; 
import android.hardware.Camera.PictureCallback; 
import android.hardware.Camera.ShutterCallback; 
import android.os.Bundle; 
import android.view.SurfaceHolder; 
import android.view.SurfaceView; 
import android.view.View; 
import android.view.Window; 
import android.view.WindowManager;
import android.widget.Button; 

public class EX07_16 extends Activity implements SurfaceHolder.Callback 
{
  private Camera mCamera; 
  private Button mButton; 
  private SurfaceView mSurfaceView; 
  private SurfaceHolder holder; 
  private AutoFocusCallback mAutoFocusCallback = 
                            new AutoFocusCallback(); 
   
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
    holder.addCallback(EX07_16.this); 
    holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    /* 設定拍照Button的OnClick事件處理 */
    mButton = (Button)findViewById(R.id.myButton);  
    mButton.setOnClickListener(new Button.OnClickListener() 
    { 
      @Override 
      public void onClick(View arg0) 
      { 
        /* 自動對焦後拍照 */
        mCamera.autoFocus(mAutoFocusCallback);
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
        /* 讓相片顯示3秒後再重設相機 */
        Thread.sleep(3000);
        /* 重新設定Camera */
        stopCamera(); 
        initCamera(); 
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