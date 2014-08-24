package irdc.ex07_19;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class EX07_19 extends Activity
{
  private ImageView image1;
  private Bitmap bm;
  private int bmWidth=0;
  private int bmHeight=0;
  private int width=0;
  private int height=0;
  private int pointX=0;
  private int pointY=0;
  private GestureDetector detector;
  private myGestureListener gListener;
  
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
    /* 載入main.xml Layout */
    setContentView(R.layout.main);
    /* 取得螢幕寬高 */    
    width=this.getWindowManager().getDefaultDisplay().getWidth();
    height=this.getWindowManager().getDefaultDisplay().getHeight();   
    /* Bitmap設定 */
    bm=BitmapFactory.decodeResource(getResources(),R.drawable.photo);
    bmWidth=bm.getWidth();
    bmHeight=bm.getHeight();
    /* ImageView初始化 */
    image1=(ImageView)findViewById(R.id.image1);
    Bitmap newB=Bitmap.createBitmap(bm,pointX,pointY, width, height);
    image1.setImageBitmap(newB);
    /* GestureDetector設定 */
    gListener = new myGestureListener();
    detector = new GestureDetector(EX07_19.this,gListener);
  }
  /* 當Activity的onTouchEvent()被觸發時，
   * 觸發GestureDetector的onTouchEvent() */
  @Override
  public boolean onTouchEvent(MotionEvent event)
  {
    if (detector.onTouchEvent(event))
    {
      return detector.onTouchEvent(event);
    }  
    else
    {
      return super.onTouchEvent(event);
    }
  }
  
  /* 自定義GestureListener */
  public class myGestureListener implements GestureDetector.OnGestureListener
  {
    /* 手指在螢幕上拖拉時觸發此method */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2,
                            float distanceX,  float distanceY)
    {
      /* 計算X軸基準點移動後的位置 */
      if(pointX+distanceX>=0){
        if((pointX+distanceX)>(bmWidth-width)){  
          pointX=bmWidth-width;
        }else{
          pointX+=distanceX;
        }
      }else{
        pointX=0;
      }
      /* 計算Y軸基準點移動後的位置 */
      if(pointY+distanceY>=0){  
        if((pointY+distanceY)>(bmHeight-height)){  
          pointY=bmHeight-height;
        }else{
          pointY+=distanceY;
        }
      }else{
        pointY=0;
      }
      /* 如果有移動，則更新Bitmap設定 */
      if(distanceX!=0&&distanceY!=0)
      {
        Bitmap newB=Bitmap.createBitmap(bm,pointX,pointY,width,height);
        image1.setImageBitmap(newB);
      }
      return false;
    }

    @Override
    public boolean onDown(MotionEvent arg0)
    {
      return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2,
        float velocityX, float velocityY)
    {
      return false;
    }

    @Override
    public void onLongPress(MotionEvent e)
    {
    }

    @Override
    public void onShowPress(MotionEvent e)
    {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {
      return false;
    }
  }
}