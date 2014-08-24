package irdc.ex07_19_1;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class EX07_19_1 extends Activity
{
  private ImageView image1;
  private Bitmap bm;
  private int width=0;
  private int height=0;
  private int pointX=0;
  private int pointY=0;
  private int scale=0;
  private List<Bitmap> li=new ArrayList<Bitmap>();
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
    
    /* 原始Bitmap設定(960*1440) */
    bm=BitmapFactory.decodeResource(getResources(),R.drawable.photo);
    /* 預先產生三種尺寸的Bitmap */
    int[][] f={{320,480},{640,960}};
    for(int i=0;i<f.length;i++)
    {
      Bitmap tmp=Bitmap.createScaledBitmap(bm,f[i][0],f[i][1],true);
      li.add(tmp);
    }
    li.add(bm);
        
    /* ImageView初始化 */
    image1=(ImageView)findViewById(R.id.image1);
    image1.setImageBitmap(li.get(0));
    /* GestureDetector設定 */
    gListener = new myGestureListener();
    detector = new GestureDetector(EX07_19_1.this,gListener);
    /* 設定GestureDetector的OnDoubleTapListener */
    detector.setOnDoubleTapListener(
        new GestureDetector.OnDoubleTapListener()
    {
      /* 點一下畫面做放大的動作 */
      @Override
      public boolean onSingleTapConfirmed(MotionEvent e)
      {
        /* 僅允許放大兩次 */
        if(scale!=2)
        {
          scale++;
          pointX+=(width/2);
          pointY+=(height/2);
          /* 重新設定顯示的影像 */
          Bitmap newB=Bitmap.createBitmap(li.get(scale),pointX,
                                          pointY,width,height);
          image1.setImageBitmap(newB);
        }
        return false;
      }
      
      @Override
      public boolean onDoubleTapEvent(MotionEvent e)
      {
        return false;
      }
      /* 連點兩下畫面做縮小的動作 */
      @Override
      public boolean onDoubleTap(MotionEvent e)
      {
        /* 縮最小就是跟畫面一樣大 */
        if(scale!=0)
        {
          scale--;
          Bitmap b=li.get(scale);
          int tmpW=b.getWidth();
          int tmpH=b.getHeight();
          /* 計算X軸基準點縮小後的位置 */
          if(pointX-(width/2)>=0)
          {
            if(pointX-(width/2)+width<=tmpW)
            {
              pointX-=(width/2);    
            }
            else
            {
              pointX=tmpW-width;
            }
          }
          else
          {
            pointX=0;
          }
          /* 計算Y軸基準點縮小後的位置 */
          if(pointY-(height/2)>=0)
          {
            if(pointY-(height/2)+height<=tmpH)
            {
              pointY-=(height/2);    
            }
            else
            {
              System.out.println("Y2");
              pointY=tmpH-height;
            }
          }
          else
          {
            pointY=0;
          }
          /* 重新設定顯示的影像 */
          Bitmap newB=Bitmap.createBitmap(b,pointX,pointY,
                                          width,height);
          image1.setImageBitmap(newB);
        }
        return false;
      }
    });
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
      /* 取得目前顯示的Bitmap */
      Bitmap b=li.get(scale);
      int tmpW=b.getWidth();
      int tmpH=b.getHeight();
      /* 計算X軸基準點移動後的位置 */
      if(pointX+distanceX>=0){
        if((pointX+distanceX)>(tmpW-width))
        {  
          pointX=tmpW-width;
        }
        else
        {
          pointX+=distanceX;
        }
      }
      else
      {
        pointX=0;
      }
      /* 計算Y軸基準點移動後的位置 */
      if(pointY+distanceY>=0)
      {  
        if((pointY+distanceY)>(tmpH-height))
        {  
          pointY=tmpH-height;
        }
        else
        {
          pointY+=distanceY;
        }
      }
      else
      {
        pointY=0;
      }
      /* 如果有移動，則更新Bitmap設定 */
      if(distanceX!=0&&distanceY!=0)
      {
        Bitmap newB=Bitmap.createBitmap(b,pointX,pointY,
                                        width,height);
        image1.setImageBitmap(newB);
      }
      return false;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {
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
  }
}