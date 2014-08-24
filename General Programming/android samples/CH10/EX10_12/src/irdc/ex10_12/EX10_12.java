package irdc.ex10_12;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class EX10_12 extends WallpaperService
{
  private final Handler mHandler = new Handler();
  
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
    private float centerX;
    private float centerY;
    private boolean mVisible;
    Bitmap bm1;
    Bitmap bm2;
    Bitmap bm3;
    Bitmap bm4;
    private int x=0;
    
    private final Runnable myDraw = new Runnable()
    {
      public void run()
      {
        /* 執行draw() */
        draw();
      }
    };

    @Override
    public void onCreate(SurfaceHolder surfaceHolder)
    {
      /* 初始化四個預存的Bitmap物件 */
      bm1=BitmapFactory.decodeResource(getResources(),R.drawable.d1);
      bm2=BitmapFactory.decodeResource(getResources(),R.drawable.d2);
      bm3=BitmapFactory.decodeResource(getResources(),R.drawable.d3);
      bm4=BitmapFactory.decodeResource(getResources(),R.drawable.d4);
      super.onCreate(surfaceHolder);
    }

    @Override
    public void onVisibilityChanged(boolean visible)
    {
      mVisible = visible;
      if(visible)
      {
        /* 桌布為可視時執行draw() */
        draw();
      }
      else
      {
        /* 桌布不可視時停止myDraw */
        mHandler.removeCallbacks(myDraw);
      }
    }

    @Override
    public void onSurfaceChanged(SurfaceHolder holder, int format,
                                 int width, int height)
    {
      /* 儲存螢幕中點座標 */
      centerX = width/2.0f;
      centerY = height/2.0f;
      draw();
      super.onSurfaceChanged(holder, format, width, height);
    }
    
    @Override
    public void onDestroy()
    {
      super.onDestroy();
      /* 程式結束時停止myDraw */
      mHandler.removeCallbacks(myDraw);
    }
    
    /* 產生桌布動畫的method */
    public void draw()
    {
      final SurfaceHolder holder = getSurfaceHolder();
      Canvas c=holder.lockCanvas();
      if (c != null)
      {
        /* 依照x的值決定要出現哪個Bitmap */
        if(x==0)
        {
          c.drawBitmap(bm1,centerX-90,centerY-90, mPaint);
          x++;
        }
        else if(x==1)
        {
          c.drawBitmap(bm2,centerX-90,centerY-90,mPaint);
          x++;
        }
        else if(x==2)
        {
          c.drawBitmap(bm3,centerX-90,centerY-90, mPaint);
          x++;
        }
        else
        {
          c.drawBitmap(bm4,centerX-90,centerY-90, mPaint);
          x=0;
        }
        holder.unlockCanvasAndPost(c);
      }
      /* 停止myDraw */
      mHandler.removeCallbacks(myDraw);
      /* 設定一秒之後再次執行 */
      if (mVisible)
      {
        mHandler.postDelayed(myDraw, 1000);
      }
    }
  }
}