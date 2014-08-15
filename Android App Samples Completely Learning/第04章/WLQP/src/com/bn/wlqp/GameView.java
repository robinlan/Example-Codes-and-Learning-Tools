package com.bn.wlqp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.bn.wlqp.R;

import android.R.color;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import static com.bn.wlqp.Constant.*;

public class GameView extends SurfaceView implements SurfaceHolder.Callback
{
	WLQPActivity activity;
	Paint paint;
	GameViewDrawThread viewdraw;
	
	static Bitmap iback; //背景圖片
	static Bitmap[] iscore=new Bitmap[10];  //0-9的貼圖
	static Bitmap[] wjSmall=new Bitmap[3];  //哪個玩家的貼圖
	static Bitmap[] wjHead=new Bitmap[3];  //哪個玩家對應的頭像的貼圖
	static Bitmap card2; //反面撲克圖
	static Bitmap down1;//左下角圖
	static Bitmap out;   //有人退出的界面
	static Bitmap fcard; //出牌貼圖
	static Bitmap giveup;  //放棄圖片
	static Bitmap people1;//左邊圖
	static Bitmap people2;//右邊圖
	static Bitmap cards[][];  //得到圖片的貼圖 
	static Bitmap own;//自己出牌提示圖
	static Bitmap other;//別人出牌提示
	
	ArrayList<CardForControl> alcfc=new ArrayList<CardForControl>();
	
	public GameView(WLQPActivity activity) {		
		super(activity);
		this.activity=activity;
		this.getHolder().addCallback(this);
		paint=new Paint(); 
		paint.setAntiAlias(true);	  
	} 
	public static void initBitmap(Resources r) //加載圖片方法  
	{		
		iback=BitmapFactory.decodeResource(r, R.drawable.backg);  //背景圖
		iscore[0]=BitmapFactory.decodeResource(r, R.drawable.zero);
		iscore[1]=BitmapFactory.decodeResource(r, R.drawable.one);
		iscore[2]=BitmapFactory.decodeResource(r, R.drawable.two);
		iscore[3]=BitmapFactory.decodeResource(r, R.drawable.three);
		iscore[4]=BitmapFactory.decodeResource(r, R.drawable.four);
		iscore[5]=BitmapFactory.decodeResource(r, R.drawable.five);
		iscore[6]=BitmapFactory.decodeResource(r, R.drawable.six);
		iscore[7]=BitmapFactory.decodeResource(r, R.drawable.seven);
		iscore[8]=BitmapFactory.decodeResource(r, R.drawable.eight);
		iscore[9]=BitmapFactory.decodeResource(r, R.drawable.nine);
		//右上角對應的玩家的頭像的圖
		wjHead[0]=BitmapFactory.decodeResource(r, R.drawable.head1);
		wjHead[1]=BitmapFactory.decodeResource(r, R.drawable.head2);
		wjHead[2]=BitmapFactory.decodeResource(r, R.drawable.head3);
		//右上角圖
		wjSmall[0]=BitmapFactory.decodeResource(r, R.drawable.personc);
		wjSmall[1]=BitmapFactory.decodeResource(r, R.drawable.personb);
		wjSmall[2]=BitmapFactory.decodeResource(r, R.drawable.persona); 
		card2=BitmapFactory.decodeResource(r, R.drawable.card2); //上面的撲克圖
		down1=BitmapFactory.decodeResource(r, R.drawable.down1);//左下角圖
		out=BitmapFactory.decodeResource(r, R.drawable.ret);
		fcard=BitmapFactory.decodeResource(r, R.drawable.fc);
		giveup=BitmapFactory.decodeResource(r, R.drawable.giveup);
		people1=BitmapFactory.decodeResource(r,R.drawable.people1);//左邊圖
		people2=BitmapFactory.decodeResource(r, R.drawable.people2);//右邊圖
		own=BitmapFactory.decodeResource(r, R.drawable.own);//自己出牌提示圖
		other=BitmapFactory.decodeResource(r, R.drawable.other);// 別人出牌提示圖
	}
	public static void initCards(Resources r)
	{//得到撲克牌
		Bitmap srcPic=PicLoadUtil.LoadBitmap(r,R.drawable.cards);
		cards=PicLoadUtil.splitPic(6, 9, srcPic, CARD_WIDTH, CARD_HEIGHT);
	}
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		//觸摸點的坐標
		int x=(int)(e.getX());
		int y=(int)(e.getY());
		switch(e.getAction())
		{
		    case MotionEvent.ACTION_DOWN:
		    	 //點擊在自己撲克牌的範圍內
			     if(x>CARD_SMALL_XOFFSET&&x<CARD_BIG_XOFFSET
			        &&y>DOWN_Y-MOVE_YOFFSET&&y<CARD_LEFT_YOFFSET&&activity.ca.perFlag)
			     {
			    	 int size=alcfc.size();
			    	 for(int i=size-1;i>=0;i--)
			    	 {
			    		 CardForControl cfcTemp=alcfc.get(i);//得到在點擊範圍內的牌的引用 
			    		 if(cfcTemp.isIn(x, y)) 
			    		 {//判斷是哪張牌並且讓牌向上移動一定的距離  並且跳出該if語句
			    			 break;
			    		 } 
			    	 }  
			     }	 
			     
			     //點擊返回按鈕
			     if(x>LEFT_RETURN_XOFFSET&&x<LEFT_RETURN_XOFFSET+BUTTON_RETURN_WIDTH
			        &&y>LEFT_RETURN_YOFFSET&&y<LEFT_RETURN_YOFFSET+BUTTON_RETURN_HEIGHT)
			     {
			    	try 
			    	{//通過輸出流輸出<#EXIT#>信息
						activity.ca.dout.writeUTF("<#EXIT#>");
					} catch (IOException e1) 
					{
						e1.printStackTrace();
					}
			     }
			     //點擊出牌按鈕
			     if(x>RIGHT_FCARD_XOFFSET&&x<RIGHT_FCARD_XOFFSET+BUTTON_FCARD_WIDTH
					&&y>RIGHT_FCARD_YOFFSET&&y<RIGHT_FCARD_YOFFSET+BUTTON_RETURN_HEIGHT)
				 {
					 if(activity.ca.perFlag)
					 {
						String lastCards=activity.ca.lastCards;//上一個玩家出的牌
						String currCards="";
						
						
						ArrayList<CardForControl> currSelected=new ArrayList<CardForControl>();
						
						for(CardForControl cfc:alcfc)
						{//遍歷alcfc並且判定該牌的flag標誌位   並將其存入currSelected中   currSelected要存放點擊到的牌   
							if(cfc.flag)
							{
								currSelected.add(cfc);
							}
						}
						
					    for(CardForControl cfc:currSelected)
					    {//遍歷手中的點擊到的牌並且將牌號存入String中
					    	currCards=currCards+","+cfc.num;
					    }
					    
					    //若有出牌，去掉前導逗號
					    if(currCards.length()>0)
					    {
					    	currCards=currCards.substring(1);
					    }
					    
					    if(activity.ca.selfNum==activity.ca.lastNum)
					    {//若別人不要又輪到自己出牌
					    	if(RuleUtil.ruleSelf(currCards)!=RuleUtil.N_A)
					    	{//判斷牌是否合法
					    		try 
						    	{//在手中的牌可以出的情況下發送消息並且設定該玩家的牌權的標誌位為false
									activity.ca.dout.writeUTF("<#PLAY#>"+currCards);
									activity.ca.perFlag=false;
									//播放聲音
									activity.playSound(1, 0);
									
									for(CardForControl cfc:currSelected)
								    {//將發的牌從存牌的ArrayList中移除
										alcfc.remove(cfc);
								    }
									
									for(int i=0;i<alcfc.size();i++)
									{//玩家手中還有的牌的X位移量
										alcfc.get(i).xOffset=DOWN_X+MOVE_SIZE*i;
									}
									//客戶端向服務器發送消息<#COUNT#>+手中剩餘牌的數量+當前玩家的編號
									activity.ca.dout.writeUTF("<#COUNT#>"+alcfc.size()+","+activity.ca.selfNum);
									
									if(alcfc.size()==0)
									{//當手中的牌為0時發送<#I_WIN#>消息
										Constant.SCORE=Constant.SCORE+15;
										activity.ca.dout.writeUTF("<#I_WIN#>");
									}
									
								} catch (IOException e1) 
								{
									e1.printStackTrace();
								}
					    	}
					    	else
					    	{//否則彈出Toast對話框--->不合規則，不允許出牌！
					    		Toast.makeText(activity,"不合規則，不允許出牌！",Toast.LENGTH_SHORT).show();
					    	}
					    }
					    else
					    {//若不是自己則按照規則出牌
					    	if(RuleUtil.rule(lastCards, currCards))
					    	{//判斷手中的牌是否比上一家的要大
					    		try 
						    	{//並發送<#PLAY#>+currCards消息      並設定標誌位為false
									activity.ca.dout.writeUTF("<#PLAY#>"+currCards);									
									activity.ca.perFlag=false;
									//播放聲音
									activity.playSound(1, 0);
									
									for(CardForControl cfc:currSelected)
								    {//將發的牌從存牌的ArrayList中移除
										alcfc.remove(cfc);
								    }
									for(int i=0;i<alcfc.size();i++)
									{//玩家手中還有的牌的X位移量
										alcfc.get(i).xOffset=DOWN_X+MOVE_SIZE*i;
									}
									//客戶端向服務器發送消息<#COUNT#>+手中剩餘牌的數量+當前玩家的編號
									activity.ca.dout.writeUTF("<#COUNT#>"+alcfc.size()+","+activity.ca.selfNum);
									if(alcfc.size()==0)
									{//當手中的牌為0時發送<#I_WIN#>消息
										activity.ca.dout.writeUTF("<#I_WIN#>");
									}
								} catch (IOException e1) 
								{
									e1.printStackTrace();
								}
					    	}
					    	else
					    	{//否則彈出Toast對話框--->不合規則，不允許出牌！
					    		Toast.makeText(activity,"不合規則，不允許出牌！",Toast.LENGTH_SHORT).show();
					    	}
					    }
					 }
				 }
			     //點擊放棄按鈕
			     if(x>RIGHT_GIVEUP_XOFFSET&&x<RIGHT_GIVEUP_XOFFSET+BUTTON_GIVEUP_WIDTH
					&&y>RIGHT_GIVEUP_YOFFSET&&y<RIGHT_GIVEUP_YOFFSET+BUTTON_GIVEUP_HEIGHT)
				 {
			    	if(activity.ca.perFlag)
			    	{//activity.ca.lastNum==activity.ca.selfNum自己出了牌後別人都沒有要 自己不能放棄 自己是第一個的時候不能放棄
			    		if(activity.ca.lastNum==activity.ca.selfNum||activity.ca.lastNum==-1)
			    		{
			    			Toast.makeText(activity,"不合規則，不允許放棄！",Toast.LENGTH_SHORT).show();
			    			return true;
			    		}
			    		
			    		for(CardForControl cfc:alcfc)
						{//遍歷玩家手中的牌 設定標誌位
							cfc.flag=false;
						}
						try 
						{//發送以<#NO_PLAY#>為開頭的信息  即點擊的是放棄按鈕所要發送的信息 並讓出牌權
							activity.ca.dout.writeUTF("<#NO_PLAY#>");
							activity.ca.perFlag=false; 
							
						} catch (IOException e1) 
						{
							e1.printStackTrace();
						}

			    	}
				}
			break;
		}
		return true;
	}
	 
	public void initCardsForControl(String cardListStr)
	{//得到撲克牌的整數標誌位並且將其存在CardForControl的alcfc對像中
		alcfc.clear();
		System.out.println(cardListStr);
		String[] cardNums=cardListStr.split("\\,");
		int c=cardNums.length;
		
		int numsTemp[]=new int[17];
		for(int i=0;i<c;i++)
		{
			numsTemp[i]=Integer.parseInt(cardNums[i]);			
		}		
		Arrays.sort(numsTemp);
		
		for(int i=0;i<c;i++)
		{
			int num=numsTemp[i];
			int[] ab=Constant.fromNumToAB(num);
			CardForControl cc=new CardForControl(cards[ab[0]][ab[1]],DOWN_X+MOVE_SIZE*i,num);
			alcfc.add(cc);
		}
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		//設置背景圖片
		canvas.drawBitmap(iback, BACK_XOFFSET, BACK_YOFFSET, paint);
    	for(int i=0;i<3;i++) //上面的撲克
        {
        	 canvas.drawBitmap(card2, UP_X,UP_Y,paint);  
        	 UP_X=UP_X+45;
        }
        	UP_X=150;
        //右側上角名稱對應的頭像
        canvas.drawBitmap(wjHead[0], RIGHT_UP_HEAD1_XOFFSET, RIGHT_UP_HEAD1_YOFFSET, paint);
        canvas.drawBitmap(wjHead[1], RIGHT_UP_HEAD2_XOFFSET, RIGHT_UP_HEAD2_YOFFSET, paint);
        canvas.drawBitmap(wjHead[2], RIGHT_UP_HEAD3_XOFFSET, RIGHT_UP_HEAD3_YOFFSET, paint);
        //右上角名稱
    	canvas.drawBitmap(wjSmall[0], RIGHT_UP_PE1_X, RIGHT_UP_PE1_Y,paint); 
    	canvas.drawBitmap(wjSmall[1], RIGHT_UP_PE1_X, RIGHT_UP_PE2_Y,paint);
    	canvas.drawBitmap(wjSmall[2], RIGHT_UP_PE1_X, RIGHT_UP_PE3_Y,paint);
    	//分數    	
    	for(int i=0;i<activity.ca.scores.length;i++)
    	{
    		int sct=activity.ca.scores[i];
    		String ts=sct+"";
    		for(int j=0;j<ts.length();j++)
    		{
    			canvas.drawBitmap
    			(
    				iscore[ts.charAt(j)-'0'], 
    				RIGHT_UP_PEJ_X+j*RIGHT_UP_PEJ_X_SPAN, 
    				RIGHT_UP_PEJ_Y+i*RIGHT_UP_PEJ_Y_SPAN,
    				paint
    			);
    		}
    	}
    	 	
        
        canvas.drawBitmap(out, LEFT_RETURN_XOFFSET, LEFT_RETURN_YOFFSET,paint);//右上角的按鈕
        //動態的適應為玩家分配頭像
        
        //右下角的兩個按鈕
        canvas.drawBitmap(fcard, RIGHT_FCARD_XOFFSET, RIGHT_FCARD_YOFFSET,paint);
        canvas.drawBitmap(giveup, RIGHT_GIVEUP_XOFFSET, RIGHT_GIVEUP_YOFFSET,paint);
        
        for(int i=0;i<activity.ca.shangjiaCount;i++)//上家剩餘的撲克的數量繪製的撲克圖
        {
        	canvas.drawBitmap(card2, LEFT_CARD_XOFFSET, LEFT_CARD_YOFFSET,paint);
        	LEFT_CARD_YOFFSET=LEFT_CARD_YOFFSET+5;
        }
        LEFT_CARD_YOFFSET=100;
        
        
        for(int i=0;i<activity.ca.xiajiaCount;i++)//下家剩餘的撲克的數量繪製的撲克圖
        {
        	canvas.drawBitmap(card2, RIGHT_CARD_XOFFSET, RIGHT_CARD_YOFFSET,paint);
        	RIGHT_CARD_YOFFSET=RIGHT_CARD_YOFFSET+5;
        }
        RIGHT_CARD_YOFFSET=100;
        
        
        //循環手中的排得控制量並且繪製自己手中的牌
    	for(CardForControl cc:alcfc)
    	{
    		cc.drawSelf(canvas);
    	}
    	//?嬤僕婕?
    	if((activity.ca.selfNum-1)<=0)
        {
        	canvas.drawBitmap(down1, LEFT_DOWN_X, LEFT_DOWN_Y,paint);//當前玩家
        	canvas.drawBitmap(people1, LEFT_X, LEFT_Y,paint);//上家玩家
        	canvas.drawBitmap(people2, RIGHT_PERSON_XOFFSET, RIGHT_PERSON_YOFFSET,paint);//下家玩家 
        }
        else
        if((activity.ca.selfNum+1)>3)
        {
        	canvas.drawBitmap(people1, LEFT_DOWN_X, LEFT_DOWN_Y,paint);//當前玩家
        	canvas.drawBitmap(people2, LEFT_X, LEFT_Y,paint);//上家玩家
        	canvas.drawBitmap(down1, RIGHT_PERSON_XOFFSET, RIGHT_PERSON_YOFFSET,paint);//下家玩家
        }
        else 
        {
        	canvas.drawBitmap(people2, LEFT_DOWN_X, LEFT_DOWN_Y,paint);//當前玩家
        	canvas.drawBitmap(down1, LEFT_X, LEFT_Y,paint);//上家玩家
        	canvas.drawBitmap(people1, RIGHT_PERSON_XOFFSET, RIGHT_PERSON_YOFFSET,paint);//下家玩家
        }
    	//繪製自己還是別人出牌的提示
    	if(activity.ca.perFlag)
    	{
    		canvas.drawBitmap(own, TIP_OWN_XOFFSET, TIP_OWN_YOFFSET, paint);
    	}
    	else
    	{ 
    		canvas.drawBitmap(other, TIP_OWN_XOFFSET, TIP_OTHER_YOFFSET, paint);
    	}
    	
    	//
    	String lastTemp=activity.ca.lastCards;
    	if(lastTemp!=null)
    	{
    		String[] saTemp=lastTemp.split("\\,");
    		for(int i=0;i<saTemp.length;i++)
    		{
    			int nTemp=Integer.parseInt(saTemp[i]);
    			int[] abTemp=Constant.fromNumToAB(nTemp);    			
    			canvas.drawBitmap
    			(
    				cards[abTemp[0]][abTemp[1]], 
    				MIDDLE_CARD1_XOFFSET+i*MIDDLE_CARD_SPAN, 
    				MIDDLE_CARD1_YOFFSET,
    				paint
    			);    			
    		}
    	}
    	
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) 
	{
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{//創建SurfaceView時要初始化initCardsForControl()方法，同事要啟動線程viewdraw   (後台不斷刷幀的線程)
		initCardsForControl(WLQPActivity.cardListStr);	
		if(viewdraw==null)
		{
			viewdraw=new GameViewDrawThread(this);
			viewdraw.flag=true;
			viewdraw.start();
		}
		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) 
	{//SurfaceView銷毀時
		boolean reatry=true;
		viewdraw.flag=false;
		while(reatry){
			try{
				viewdraw.join();
				reatry=false;
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	public void repaint()
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
