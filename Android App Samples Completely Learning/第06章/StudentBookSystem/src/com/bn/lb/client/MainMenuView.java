package com.bn.lb.client;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import static com.bn.lb.client.Constant.*;

public class MainMenuView extends SurfaceView implements SurfaceHolder.Callback
{
	RootActivity activity;
	Paint paint;
	Bitmap bitmapselect;
	Bitmap bitmapyuyue;
	Bitmap bitmaploss;
	Bitmap bitmapBack;
	Bitmap bimapbackground;
	Bitmap logout;
	Bitmap about;
	Bitmap help;
	public MainMenuView(RootActivity activity) {
		super(activity);
		this.activity=activity;
		this.getHolder().addCallback(this);
		paint=new Paint();
		paint.setAntiAlias(true);
		initBitmap();
	}
	public void initBitmap()
	{
		bitmapselect=BitmapFactory.decodeResource(getResources(), R.drawable.select);
		about=BitmapFactory.decodeResource(getResources(), R.drawable.about);
		help=BitmapFactory.decodeResource(getResources(), R.drawable.help);
		bitmapyuyue=BitmapFactory.decodeResource(getResources(), R.drawable.order);
		bitmaploss=BitmapFactory.decodeResource(getResources(), R.drawable.lose);
		bitmapBack=BitmapFactory.decodeResource(getResources(), R.drawable.exit);
		bimapbackground=BitmapFactory.decodeResource(getResources(), R.drawable.background);
		logout=BitmapFactory.decodeResource(getResources(), R.drawable.logout);						
	}
	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);		
		//背景图片
		canvas.drawBitmap(bimapbackground, 0, 0, null);
		//查询按钮
    	canvas.drawBitmap(bitmapselect, BUTTON_SELECT_XOFFSET, BUTTON_SELECT_YOFFSET, null);
    	//预约按钮
    	canvas.drawBitmap(bitmapyuyue, BUTTON_ORDER_XOFFSET, BUTTON_ORDER_YOFFSET, null);
    	//挂失按钮
    	canvas.drawBitmap(bitmaploss, BUTTON_LOSS_XOFFSET, BUTTON_LOSS_YOFFSET, null);
    	//退出按钮
    	canvas.drawBitmap(bitmapBack, BUTTON_EXIT_XOFFSET, BUTTON_EXIT_YOFFSET, null);
    	//注销按钮
    	canvas.drawBitmap(logout, BUTTON_LOGOUT_XOFFSET, BUTTON_LOGOUT_YOFFSET, null);
    	//关于按钮
    	canvas.drawBitmap(about, BUTTON_ABOUT_XOFFSET, BUTTON_ABOUT_YOFFSET, null);
    	//帮助按钮
    	canvas.drawBitmap(help, BUTTON_HELP_XOFFSET, BUTTON_HELP_YOFFSET, null);
	}
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		
		int x=(int)(e.getX());
		int y=(int) (e.getY());
		
		switch(e.getAction())
		{
		    case MotionEvent.ACTION_DOWN:
		    	if(x>BUTTON_SELECT_XOFFSET&&x<BUTTON_SELECT_XOFFSET+BUTTON_SELECT_WIDTH
		    	   &&y>BUTTON_SELECT_YOFFSET&&y<BUTTON_SELECT_YOFFSET+BUTTON_SELECT_HEIGHT)
				{
		    		activity.hd.sendEmptyMessage(1);
				}
		        if(x>BUTTON_ORDER_XOFFSET&&x<BUTTON_ORDER_XOFFSET+BUTTON_ORDER_WIDTH
		           &&y>BUTTON_ORDER_YOFFSET&&y<BUTTON_ORDER_YOFFSET+BUTTON_ORDER_HEIGHT)
		        {
		        	activity.hd.sendEmptyMessage(3);
				}
				if(x>BUTTON_LOSS_XOFFSET&&x<BUTTON_LOSS_XOFFSET+BUTTON_LOSS_WIDTH
				   &&y>BUTTON_LOSS_YOFFSET&&y<BUTTON_LOSS_YOFFSET+BUTTON_LOSS_HEIGHT)
				{
					activity.hd.sendEmptyMessage(2);
				}
		//退出按钮
				if(x>BUTTON_EXIT_XOFFSET&&x<BUTTON_EXIT_XOFFSET+BUTTON_EXIT_WIDTH
				  &&y>BUTTON_EXIT_YOFFSET&&y<BUTTON_EXIT_YOFFSET+BUTTON_EXIT_HEIGHT)
			   {					
					System.exit(0);
			   }
			   if(x>BUTTON_LOGOUT_XOFFSET&&x<BUTTON_LOGOUT_XOFFSET+BUTTON_LOGOUT_WIDTH
				 &&y>BUTTON_LOGOUT_YOFFSET&&y<BUTTON_LOGOUT_YOFFSET+BUTTON_LOGOUT_HEIGHT)
			   {					
				  activity.hd.sendEmptyMessage(0);
			   }
			  if(x>BUTTON_HELP_XOFFSET&&x<BUTTON_HELP_XOFFSET+BUTTON_HELP_WIDTH
				&&y>BUTTON_HELP_YOFFSET&&y<BUTTON_HELP_YOFFSET+BUTTON_HELP_HEIGHT)
			  {
				   activity.hd.sendEmptyMessage(4);
			  }
			  if(x>BUTTON_ABOUT_XOFFSET&&x<BUTTON_LOSS_XOFFSET+BUTTON_about_WIDTH
				 &&y>BUTTON_ABOUT_YOFFSET&&y<BUTTON_ABOUT_YOFFSET+BUTTON_about_HEIGHT)
			  {
				   activity.hd.sendEmptyMessage(5);
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
	public void repaint()
	{
		SurfaceHolder holder=this.getHolder();
		Canvas canvas=holder.lockCanvas();
		try{
			synchronized(holder){
				onDraw(canvas);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(canvas!=null){
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
}