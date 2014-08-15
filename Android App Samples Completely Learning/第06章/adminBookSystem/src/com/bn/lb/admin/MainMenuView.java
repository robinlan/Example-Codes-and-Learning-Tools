package com.bn.lb.admin;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static com.bn.lb.admin.Constant.*;

public class MainMenuView extends SurfaceView implements SurfaceHolder.Callback
{
    RootActivity activity;
	Paint paint; 
	Bitmap bitmapStudentmanager;
	Bitmap bitmapBookmanager;
	Bitmap bitmapSelect;
	Bitmap bitmaporderbook;
	Bitmap bitmaplosebook;
	Bitmap bitmapFee;
	Bitmap bitmapAdminmanager;
	Bitmap bitmapExit;
	Bitmap bitmapLogout;
	Bitmap bitmapBack;
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
	   bitmapBack=BitmapFactory.decodeResource(getResources(), R.drawable.background);
	   bitmapStudentmanager=BitmapFactory.decodeResource(getResources(), R.drawable.xueshengguanli);
	   bitmapBookmanager=BitmapFactory.decodeResource(getResources(), R.drawable.tushuguanli);
	   bitmapSelect=BitmapFactory.decodeResource(getResources(), R.drawable.chaxun);
	   bitmaporderbook=BitmapFactory.decodeResource(getResources(), R.drawable.yuyue);
	   bitmaplosebook=BitmapFactory.decodeResource(getResources(), R.drawable.guashi);
	   bitmapFee=BitmapFactory.decodeResource(getResources(), R.drawable.jiaofei);
	   bitmapAdminmanager=BitmapFactory.decodeResource(getResources(), R.drawable.admin); 
	   bitmapLogout=BitmapFactory.decodeResource(getResources(), R.drawable.zhuxiao);
	   bitmapExit=BitmapFactory.decodeResource(getResources(), R.drawable.tuichu);
	}
	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		//int k1=SCREEN_WIDTH;
		//int k2=SCREEN_HEIGHT;
		//int cols=SCREEN_WIDTH/BACK_SIZE+((k1==0)?0:1);//列数
    	//int rows=SCREEN_HEIGHT/BACK_SIZE+((k2==0)?0:1);//行数
    	/*for(int i=0;i<rows;i++)
    	{
    		for(int j=0;j<cols;j++)
    		{
    			canvas.drawBitmap(bitmapBack, 16*j,16*i, paint);
    		}
    	}*/
		//canvas.drawBitmap(bitmapBack,SCREEN_WIDTH,SCREEN_HEIGHT, paint);
    	
		//select按钮
		canvas.drawBitmap(bitmapBack,0,0,null);
        canvas.drawBitmap(bitmapStudentmanager,BUTTON_STUDENTMANAGEMENT_XOFFSET,BUTTON_STUDENTMANAGEMENT_YOFFSET,null);
     	canvas.drawBitmap(bitmapBookmanager,BUTTON_BOOKMANAGEMENT_XOFFSET,BUTTON_BOOKMANAGEMENT_YOFFSET,null);
     	canvas.drawBitmap(bitmapSelect,BUTTON_SELECT_XOFFSET,BUTTON_SELECT_YOFFSET,null);
     	canvas.drawBitmap(bitmaporderbook,BUTTON_ORDER_XOFFSET,BUTTON_ORDER_YOFFSET,null);
     	canvas.drawBitmap(bitmaplosebook,BUTTON_LOSS_XOFFSET,BUTTON_LOSS_YOFFSET,null);
     	canvas.drawBitmap(bitmapFee,BUTTON_FEE_XOFFSET,BUTTON_FEE_YOFFSET,null);
     	canvas.drawBitmap(bitmapAdminmanager,BUTTON_ADMINMANAGEMENT_XOFFSET,BUTTON_ADMINMANAGEMENT_YOFFSET,null);
     	canvas.drawBitmap(bitmapLogout,BUTTON_LOGOUT_XOFFSET,BUTTON_LOGOUT_YOFFSET,null);
   	    canvas.drawBitmap(bitmapExit,BUTTON_EXIT_XOFFSET,BUTTON_EXIT_YOFFSET,null);
		
    	
	}
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		
		int x=(int) (e.getX());
		int y=(int) (e.getY());
		
		switch(e.getAction())
		{
		    case MotionEvent.ACTION_DOWN:
		    	if(x>BUTTON_STUDENTMANAGEMENT_XOFFSET&&x<BUTTON_STUDENTMANAGEMENT_WIDTH+BUTTON_STUDENTMANAGEMENT_XOFFSET
		    	   &&y>BUTTON_STUDENTMANAGEMENT_YOFFSET&&y<BUTTON_STUDENTMANAGEMENT_HEIGHT+BUTTON_STUDENTMANAGEMENT_YOFFSET)
				{
		    		activity.gotostumainView();
		    		activity.curr=WhichView.STUMAIN_VIEW;
				}
		        if(x>BUTTON_BOOKMANAGEMENT_XOFFSET&&x<BUTTON_BOOKMANAGEMENT_XOFFSET+BUTTON_BOOKMANAGEMENT_WIDTH
		           &&y>BUTTON_BOOKMANAGEMENT_YOFFSET&&y<BUTTON_BOOKMANAGEMENT_YOFFSET+BUTTON_BOOKMANAGEMENT_HEIGHT)
		        {
		        	activity.gotobookmanagementmainView();
		    		activity.curr=WhichView.BOOKMAIN_VIEW;
				}
				if(x>BUTTON_SELECT_XOFFSET&&x<BUTTON_SELECT_XOFFSET+BUTTON_SELECT_WIDTH
				   &&y>BUTTON_SELECT_YOFFSET&&y<BUTTON_SELECT_YOFFSET+BUTTON_SELECT_HEIGHT)
				{
					activity. gotoQueryMainView();
					activity.curr=WhichView.QUERYMAIN_VIEW;
				}
				if(x>BUTTON_ORDER_XOFFSET&&x<BUTTON_ORDER_XOFFSET+BUTTON_ORDER_WIDTH
						   &&y>BUTTON_ORDER_YOFFSET&&y<BUTTON_ORDER_YOFFSET+BUTTON_ORDER_HEIGHT)
						{
							activity.gotoyuyueView();
							activity.curr=WhichView.YUYUE_VIEW;
						}
				if(x>BUTTON_LOSS_XOFFSET&&x<BUTTON_LOSS_XOFFSET+BUTTON_LOSS_WIDTH
						   &&y>BUTTON_LOSS_YOFFSET&&y<BUTTON_LOSS_YOFFSET+BUTTON_LOSS_HEIGHT)
						{
							activity. gotoloseView(); 
							activity.curr=WhichView.LOSE_VIEW;
						}
				if(x>BUTTON_FEE_XOFFSET&&x<BUTTON_FEE_XOFFSET+BUTTON_FEE_WIDTH
						   &&y>BUTTON_FEE_YOFFSET&&y<BUTTON_FEE_YOFFSET+BUTTON_FEE_HEIGHT)
						{
							activity.gotoFee();
							activity.curr=WhichView.FEE_VIEW;
						}
				if(x>BUTTON_ADMINMANAGEMENT_XOFFSET&&x<BUTTON_ADMINMANAGEMENT_XOFFSET+BUTTON_ADMINMANAGEMENT_WIDTH
						   &&y>BUTTON_ADMINMANAGEMENT_YOFFSET&&y<BUTTON_ADMINMANAGEMENT_YOFFSET+BUTTON_ADMINMANAGEMENT_HEIGHT)
						{
							activity.gotoandminmainView();
							activity.curr=WhichView.ADMINMAIN_VIEW;
						}
				if(x>BUTTON_EXIT_XOFFSET&&x<BUTTON_EXIT_XOFFSET+BUTTON_EXIT_WIDTH
						   &&y>BUTTON_EXIT_YOFFSET&&y<BUTTON_EXIT_YOFFSET+BUTTON_EXIT_HEIGHT)
				{
					System.exit(0);
				}
				if(x>BUTTON_LOGOUT_XOFFSET&&x<BUTTON_LOGOUT_XOFFSET+BUTTON_LOGOUT_WIDTH
						   &&y>BUTTON_LOGOUT_YOFFSET&&y<BUTTON_LOGOUT_YOFFSET+BUTTON_LOGOUT_HEIGHT)
				{
					activity.gotoDengLuView();
					activity.curr=WhichView.DENGLU_VIEW;
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