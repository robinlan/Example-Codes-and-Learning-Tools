package com.bn.lc;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static com.bn.lc.Constant.*;

public class Lc_View extends SurfaceView implements SurfaceHolder.Callback
{
	Lc_Activity activity;
	Paint paint; 
	 
	Bitmap iback;      //背景
	Bitmap icategory;  //类别
	Bitmap iproceeds;  //收入
	Bitmap iremove;    //支出 
	Bitmap isum;       //收入统计
	Bitmap izhi;       //支出统计
	Bitmap isselect;   //收入查询
	Bitmap izselect;   //支出查询
	Bitmap isystem;    //系统设置
	Bitmap iout;       //退出
	Bitmap bnlc;       //标题
  
	public Lc_View(Lc_Activity activity)
	{
        super(activity);
        this.activity=activity;
        this.getHolder().addCallback(this);
        
        paint=new Paint();
        paint.setAntiAlias(true); //抗锯齿
        
        initBitmap(activity.getResources());  //加载图片
	}

	public void initBitmap(Resources r)  //加载图片
	{
		iback=BitmapFactory.decodeResource(r, R.drawable.back);     //背景
		icategory=BitmapFactory.decodeResource(r,R.drawable.category);   //收支类别
		iproceeds=BitmapFactory.decodeResource(r, R.drawable.income); //日常收入
		iremove=BitmapFactory.decodeResource(r, R.drawable.spend);    //日常支出
		isum=BitmapFactory.decodeResource(r, R.drawable.sum);  //收入统计
		izhi=BitmapFactory.decodeResource(r, R.drawable.jsq);       //支出统计
		isselect=BitmapFactory.decodeResource(r, R.drawable.sselect);//收入查询
		izselect=BitmapFactory.decodeResource(r, R.drawable.zselect); //支出查询
		isystem=BitmapFactory.decodeResource(r, R.drawable.person);   //系统设置
		iout=BitmapFactory.decodeResource(r, R.drawable.out);        //退出系统
		bnlc=BitmapFactory.decodeResource(r, R.drawable.bnlc);
	}
	   
	@Override   
	public void onDraw(Canvas canvas) //绘制 
	{
		canvas.drawBitmap(iback, BACK_XOFFSET,BACK_YOFFSET, paint);   //绘制背景
		canvas.drawBitmap(bnlc, BNLC_XOFFSET,BNLC_YOFFSET, paint);   //绘制背景
		canvas.drawBitmap(icategory, LEI_XOFFSET,LEI_YOFFSET, paint); //收支类别
		canvas.drawBitmap(iproceeds,SHOU_XOFFSET,SHOU_YOFFSET,paint); //日常收入
		canvas.drawBitmap(iremove, ZHI_XOFFSET,ZHI_YOFFSET, paint);   //日常支出
		canvas.drawBitmap(isum, STONG_XOFFSET,STONG_YOFFSET, paint);  //收入统计
		canvas.drawBitmap(izhi, ZTONG_XOFFSET,ZTONG_YOFFSET, paint);  //支出统计
		canvas.drawBitmap(isselect, SCHA_XOFFSET,SCHA_YOFFSET, paint);//收入查询
		canvas.drawBitmap(izselect,ZCHA_XOFFSET ,ZCHA_YOFFSET, paint);//支出查询
		canvas.drawBitmap(isystem, XI_XOFFSET,XI_YOFFSET, paint);     //系统设置
		canvas.drawBitmap(iout, OUT_XOFFSET,OUT_YOFFSET, paint);      //退出系统
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e)  //触屏
	{
		int x=(int)(e.getX());
		int y=(int)(e.getY());
		
		switch(e.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				if(x>LEI_XOFFSET&&x<LEI_XOFFSET+PIC_WIDTH&&y>LEI_YOFFSET&&y<LEI_YOFFSET+PIC_HEIGHT)
				{
					activity.hd.sendEmptyMessage(0);
				}				
				if(x>SHOU_XOFFSET&&x<SHOU_XOFFSET+PIC_WIDTH&&y>SHOU_YOFFSET&&y<SHOU_YOFFSET+PIC_HEIGHT)
				{
					activity.hd.sendEmptyMessage(1);
				}				
				if(x>ZHI_XOFFSET&&x<ZHI_XOFFSET+PIC_WIDTH&&y>ZHI_YOFFSET&&y<ZHI_YOFFSET+PIC_HEIGHT)
				{
					activity.hd.sendEmptyMessage(2);
				}				
				if(x>STONG_XOFFSET&&x<STONG_XOFFSET+PIC_WIDTH&&y>STONG_YOFFSET&&y<STONG_YOFFSET+PIC_HEIGHT)
				{
					activity.hd.sendEmptyMessage(3);
				}				
				if(x>ZTONG_XOFFSET&&x<ZTONG_XOFFSET+PIC_WIDTH&&y>ZTONG_YOFFSET&&y<ZTONG_YOFFSET+PIC_HEIGHT)
				{
					activity.hd.sendEmptyMessage(4);
				}				
				if(x>SCHA_XOFFSET&&x<SCHA_XOFFSET+PIC_WIDTH&&y>SCHA_YOFFSET&&y<SCHA_YOFFSET+PIC_HEIGHT)
				{
					activity.hd.sendEmptyMessage(5);
				}				
				if(x>ZCHA_XOFFSET&&x<ZCHA_XOFFSET+PIC_WIDTH&&y>ZCHA_YOFFSET&&y<ZCHA_YOFFSET+PIC_HEIGHT)
				{
					activity.hd.sendEmptyMessage(6);
				}				
				if(x>XI_XOFFSET&&x<XI_XOFFSET+PIC_WIDTH&&y>XI_YOFFSET&&y<XI_YOFFSET+PIC_HEIGHT)
				{
					activity.hd.sendEmptyMessage(7);
				}				
				if(x>OUT_XOFFSET&&x<OUT_XOFFSET+PIC_WIDTH&&y>OUT_YOFFSET&&y<OUT_YOFFSET+PIC_HEIGHT)
				{
					System.exit(0);
				}
				break;
		}
		return true;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) 
	{
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		this.repaint();
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{	
	}
	
	public void repaint()  //重绘方法
	{
		SurfaceHolder surfaceholder=this.getHolder();
		Canvas canvas=surfaceholder.lockCanvas();
		try
		{
			synchronized(surfaceholder)
			{
				onDraw(canvas);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(canvas!=null)
			{
				surfaceholder.unlockCanvasAndPost(canvas);
			}
		}
     }
}
