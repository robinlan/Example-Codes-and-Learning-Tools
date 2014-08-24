package irdc.ex10_12_1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class EX10_12_1 extends WallpaperService
{  
  /* 覆寫onCreateEngine()，回傳自訂的MyEngine */
  @Override
  public Engine onCreateEngine()
  {
    return new MyEngine();
  }
  
  /* 自定義MyEngine繼承Engine */
  class MyEngine extends Engine
  {
    private final Paint mPaint = new Paint();
    private float mTouchX;
    private float mTouchY;
    Bitmap bg;
    Bitmap bm;

    @Override
    public void onCreate(SurfaceHolder surfaceHolder)
    {
      /* 初始化背景Bitmap物件 */
      bg=BitmapFactory.decodeResource(getResources(),R.drawable.bg);
      /* 初始化手指按下時出現的Bitmap物件，寬高為120*66 */
      bm=BitmapFactory.decodeResource(getResources(),R.drawable.walk);
      /* enable TouchEvent(預設為false) */
      setTouchEventsEnabled(true);
      
      super.onCreate(surfaceHolder);
    }

    /* 覆寫onTouchEvent() */
    @Override
    public void onTouchEvent(MotionEvent event)
    {
      if(event.getAction()==MotionEvent.ACTION_DOWN)
      {
        /* 手指按下時記錄XY座標值，並執行draw() */
        mTouchX=event.getX();
        mTouchY=event.getY();
        draw();
      }
      else if(event.getAction()==MotionEvent.ACTION_UP)
      {
        /* 手指離開時重設桌布背景 */
        unDraw();
      }
      else if(event.getAction()==MotionEvent.ACTION_MOVE)
      {
        /* 手指移動時記錄XY座標值，並執行draw() */
        mTouchX=event.getX();
        mTouchY=event.getY();
        draw();
      }
      super.onTouchEvent(event);
    }

    @Override
    public void onSurfaceCreated(SurfaceHolder holder)
    {
      /* 設定桌布背景圖 */
      Canvas c=holder.lockCanvas();
      if (c != null)
        c.drawBitmap(bg,0,0, mPaint);
      holder.unlockCanvasAndPost(c);
      super.onSurfaceCreated(holder);
    }

    /* 在桌布上畫圖的method */
    void draw()
    {
      final SurfaceHolder holder = getSurfaceHolder();
      Canvas c=holder.lockCanvas();
      if (c != null)
      {
        /* 重設桌布背景 */
        c.drawBitmap(bg,0,0, mPaint);
        /* 設定手指按下時顯示國王走路的圖 */
        c.drawBitmap(bm,mTouchX-33,mTouchY-120, mPaint);
        holder.unlockCanvasAndPost(c);
      }
    }
    
    /* 重設桌布背景的method */
    void unDraw()
    {
      final SurfaceHolder holder = getSurfaceHolder();
      Canvas c=holder.lockCanvas();
      if (c != null)
      {
        /* 重設桌布背景 */
        c.drawBitmap(bg,0,0, mPaint);
        holder.unlockCanvasAndPost(c);
      }
    }
  }
}