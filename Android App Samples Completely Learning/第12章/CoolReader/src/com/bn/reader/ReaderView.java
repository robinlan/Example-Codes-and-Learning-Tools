package com.bn.reader;

import java.util.HashMap;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.widget.Toast;
import static com.bn.reader.Constant.*;

enum TurnDir{
	noTurning,left,right,//不翻頁，向前翻，向後翻
}

public class ReaderView extends SurfaceView implements SurfaceHolder.Callback
{
	ReaderActivity readerActivity;//ReaderActivity的引用
	Paint paint;//畫筆的引用
	//將要繪製的左右兩幅圖的引用
	Bitmap bmLeft;//左邊的
	Bitmap bmRight;//右邊的
	
	ReadRecord currRR;//當前頁數據
	
	//翻頁中用到的臨時對像
	Bitmap bmLeft_temp;//左邊圖片臨時引用
	Bitmap bmRight_temp;//右邊圖片臨時引用
	
	ReadRecord currRR_temp;//記錄ReadRecord的一個臨時對像
	Bitmap bmBack;// 底紋圖片
	Bitmap title;// 標頭圖片

	AdThread at;//廣告條的刷新線程
	//廣告圖片數組
	int ad[]={R.drawable.ad_a,R.drawable.ad_b,R.drawable.ad_c,R.drawable.ad_d,
			R.drawable.ad_e,R.drawable.ad_f,R.drawable.ad_g,R.drawable.ad_h};
	//加載的廣告圖片數組
	Bitmap adb[]=new Bitmap[ad.length];
	
	//當前這個文本文件（此本書）的閱讀數據
	HashMap<Integer,ReadRecord> currBook=new HashMap<Integer,ReadRecord>();
	
	//當前翻頁觸控點坐標
	float ax=-1;
	float ay=-1;	
	//右下角坐標
	int bx;
	int by;
	
	int[] cd;//c、d兩點坐標數組,其中c、d兩點分別為翻折線與頁面寬和高的交點
	TurnDir turnDir=TurnDir.noTurning;//翻頁方向，枚舉類型
	boolean repaintAdFlag=true;//繪製廣告的標誌
	//ReaderView的構造方法
    public ReaderView(ReaderActivity readerActivity) {
		super(readerActivity);		
		this.readerActivity=readerActivity;
		
		this.getHolder().addCallback((Callback) this);
		//創建畫筆
		paint=new Paint();		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) 
	{
		at=new AdThread(this);//創建廣告刷新線程
		bmBack=PicLoadUtil.LoadBitmap(this.getResources(), BITMAP);//自適應屏幕的背景圖片
		bmBack=PicLoadUtil.scaleToFit(bmBack, PAGE_WIDTH, PAGE_HEIGHT);
		
		title=PicLoadUtil.LoadBitmap(this.getResources(), R.drawable.bt);//自適應屏幕的標頭圖片
		title=PicLoadUtil.scaleToFit(title, SCREEN_WIDTH, BLANK);
		
		for(int i=0;i<ad.length;i++)//自適應屏幕的廣告圖片
		{
			adb[i]=PicLoadUtil.LoadBitmap(this.getResources(), ad[i]);
			adb[i]=PicLoadUtil.scaleToFit(adb[i], AD_WIDTH, BLANK);
		}
		//初始化到當前文件第X頁
		currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);
		
		if(CURRENT_PAGE==0)//如果是第一次打開某一本書
		{
			currBook.put(currRR.pageNo, currRR);//第一頁的信息放入hashMap中			
		}
		
		//繪製左右兩幅圖片
		bmLeft=this.drawPage(currRR);
		bmRight=this.drawPage(currRR);
		repaint();
		at.start();//開啟廣告刷新線程
	}
    @Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
    }

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		at.stopCurrentThread();//終止進程刷新
	}
	public void onDraw(Canvas canvas)
	{
		synchronized(paint)
		{	
			canvas.drawColor(Color.BLACK);//擦空界面
			canvas.drawBitmap(title, 0, 0, paint);//繪製標頭圖片
			canvas.drawBitmap(adb[NUM],Constant.SCREEN_WIDTH-AD_WIDTH, 0, paint);//繪製廣告條
			drawcut_line(canvas);//繪製分割線
			drawTitle(canvas);//繪製題目			
			if(turnDir==TurnDir.right)
			{	
				canvas.drawBitmap(bmLeft, LEFT_OR_RIGHT_X, BLANK, paint);//繪製左側自定義的圖片
	
				//根據a點的位置繪製右邊用來看的頁====================begin===========================
				//保存畫筆狀態
				canvas.save();
				//新建一個路徑
				Path path2=new Path();
				//讓路徑圈住的範圍為e、f、c、d、g
				path2.moveTo(RIGHT_OR_LEFT_x,0);
				path2.lineTo(RIGHT_OR_LEFT_x, PAGE_HEIGHT+BLANK);			
				path2.lineTo(cd[0], cd[1]);
				path2.lineTo(cd[2], cd[3]);
				path2.lineTo(RIGHT_OR_LEFT_x+PAGE_WIDTH, BLANK);
				path2.lineTo(RIGHT_OR_LEFT_x,0);
				//為畫筆設置繪製剪裁
				canvas.clipPath(path2);
				
				canvas.drawBitmap(bmRight, RIGHT_OR_LEFT_x,BLANK, paint);//繪製右側圖片
				//恢復畫筆狀態
				canvas.restore();
				//根據a點的位置繪製右邊用來看的頁=====================end============================
				
				
				//根據a點的位置繪製被翻出來的反的頁畫面===============begin==========================
				//計算反過來頁面的旋轉修正角
				float angle=(float)Math.toDegrees(Math.atan((ay-cd[3])/(ax-cd[2])));
				//創建並設置旋轉矩陣
				Matrix m1=new Matrix();
				m1.setRotate(90+angle, LEFT_OR_RIGHT_X,PAGE_HEIGHT);
				//將反過來旋轉頁的左下角對到觸控點上
				Matrix m2=new Matrix();
				m2.setTranslate
				(
					ax,	
					ay-PAGE_HEIGHT
				);
				//創建總矩陣
				Matrix mz=new Matrix();
				mz.setConcat(m2, m1);
				//保存畫筆狀態
				canvas.save();
				//讓路徑圈住的範圍為a、c、d
				Path path3=new Path();
				path3.moveTo(ax,ay);		
				path3.lineTo(cd[0], cd[1]);
				path3.lineTo(cd[2], cd[3]);		
				path3.lineTo(ax,ay);
				canvas.clipPath(path3);
							
	
				canvas.drawBitmap(bmLeft_temp, mz, paint);//繪製左側自定義的圖片
	
				//恢復畫筆狀態
				canvas.restore();
				//根據a點的位置繪製被翻出來的反的頁畫面================end===========================
				
				
				//根據a點的位置繪製最下面要被翻出來的頁的畫面================begin====================
				//保存畫筆狀態
				canvas.save();
				//新建一個路徑
				Path path1=new Path();
				//讓路徑圈住的範圍為c、b、d
				path1.moveTo(cd[0], cd[1]);
				path1.lineTo(bx, by);
				path1.lineTo(cd[2], cd[3]);
				path1.lineTo(cd[0], cd[1]);
				//為畫筆設置繪製剪裁
				canvas.clipPath(path1);
				paint.setAlpha(220);
				
				canvas.drawBitmap(bmRight_temp, RIGHT_OR_LEFT_x,BLANK, paint);//繪製右側圖片
				paint.setAlpha(255);
				//恢復畫筆狀態
				canvas.restore();	
				//根據a點的位置繪製最下面要被翻出來的頁的畫面=================end=====================			
	
			}//如果向左翻
			else if(turnDir==TurnDir.left)			
			{
				//根據a點的位置繪製左邊用來看的頁====================begin===========================
				//保存畫筆狀態
				canvas.save();
				//新建一個路徑
				Path path2=new Path();
				//讓路徑圈住的範圍為e、f、c、d、g
				path2.moveTo(PAGE_WIDTH,0);
				path2.lineTo(PAGE_WIDTH, PAGE_HEIGHT+BLANK);			
				path2.lineTo(cd[0], cd[1]);
				path2.lineTo(cd[2], cd[3]);
				path2.lineTo(0, BLANK);	
				path2.lineTo(PAGE_WIDTH,0);	
				//為畫筆設置繪製剪裁
				canvas.clipPath(path2);
				canvas.drawBitmap(bmLeft, LEFT_OR_RIGHT_X, BLANK, paint);//繪製左側自定義的圖片
				//恢復畫筆狀態
				canvas.restore();
				//根據a點的位置繪製左邊用來看的頁=====================end============================
				
				//繪製右邊用來看的頁
				canvas.drawBitmap(bmRight, RIGHT_OR_LEFT_x,BLANK, paint);
				
				//根據a點的位置繪製最下面要被翻出來的頁的畫面================begin====================
				//保存畫筆狀態
				canvas.save();
				//新建一個路徑
				Path path1=new Path();
				//讓路徑圈住的範圍為c、b、d
				path1.moveTo(cd[0], cd[1]);
				path1.lineTo(bx, by);
				path1.lineTo(cd[2], cd[3]);
				path1.lineTo(cd[0], cd[1]);
				//為畫筆設置繪製剪裁
				canvas.clipPath(path1);
				paint.setAlpha(220);//翻出的角為半透明
				
				canvas.drawBitmap(bmLeft_temp, LEFT_OR_RIGHT_X, BLANK, paint);//繪製左側自定義的圖片
			
				paint.setAlpha(255);
				//恢復畫筆狀態
				canvas.restore();
				//根據a點的位置繪製最下面要被翻出來的頁的畫面=================end=====================
				
				
				//根據a點的位置繪製被翻出來的反的頁畫面===============begin==========================
				//計算反過來頁面的旋轉修正角
				float angle=(float)Math.toDegrees(Math.atan((ax-cd[0])/(ay-cd[1])));//向前翻頁時，計算角度用的c點坐標
				//創建並設置旋轉矩陣
				Matrix m1=new Matrix();
				m1.setRotate(-90-angle, PAGE_WIDTH ,PAGE_HEIGHT );//以圖片右下角為旋轉中心點，逆時針旋轉90+angle
				//將反過來旋轉頁的右下角對到觸控點上
				Matrix m2=new Matrix();
				m2.setTranslate
				(
					ax-PAGE_WIDTH ,	
					ay-PAGE_HEIGHT
				);
				//創建總矩陣
				Matrix mz=new Matrix();
				mz.setConcat(m2, m1);
				//保存畫筆狀態
				canvas.save();
				//讓路徑圈住的範圍為a、c、d
				Path path3=new Path();
				path3.moveTo(ax,ay);		
				path3.lineTo(cd[0], cd[1]);
				path3.lineTo(cd[2], cd[3]);		
				path3.lineTo(ax,ay);
				canvas.clipPath(path3);
				
				canvas.drawBitmap(bmRight_temp, mz, paint);
				//恢復畫筆狀態
				canvas.restore();
				//根據a點的位置繪製被翻出來的反的頁畫面================end===========================
			}
			else
			{
				canvas.drawBitmap(bmLeft, LEFT_OR_RIGHT_X, BLANK, paint);//繪製左側自定義的圖片
				canvas.drawBitmap(bmRight, RIGHT_OR_LEFT_x,BLANK, paint);//繪製右側自定義的圖片
			}
		}
	}
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent e)
	{
		
		 switch(keyCode)
		 {
		    case 4:
		    	readerActivity.showDialog(readerActivity.EXIT_READER);//退出對話框
				break;
		    case 22:
		    	repaintAdFlag=false;//繪製廣告的標誌設為false
				//初始化到下一頁數據
		    	currRR=new ReadRecord(Constant.nextPageStart,0,Constant.nextPageNo);
	
		    	Constant.CURRENT_LEFT_START=currRR.leftStart;//記錄當前讀到處leftstart的值
		    	Constant.CURRENT_PAGE=currRR.pageNo;//記錄當前讀到處的page的值
	    	
		    	currBook.put(currRR.pageNo, currRR);//當前頁的信息加入hashMap
		    	
		    	
		    	if(currRR.leftStart>Constant.CONTENTCOUNT){
				   Toast.makeText
				   (
						this.getContext(), 
						"已經到最後一頁了，不可以再往後了！", 
						Toast.LENGTH_SHORT
					).show();
				}else
				{
					//繪製左右兩幅圖片
					bmLeft=drawPage(currRR);
					bmRight=drawPage(currRR);
					repaint();
				}
		    	repaintAdFlag=true;//換完圖片再重繪
				break;
			    case 21:
			    	repaintAdFlag=false;//繪製廣告的標誌設為false
				   if(currRR.pageNo==0){
						Toast.makeText
						(
							this.getContext(), 
							"已經到第一頁，不可以再往前翻了！", 
							Toast.LENGTH_SHORT
						).show();				
					}
					else
					{
						currRR=currBook.get(currRR.pageNo-1);
						
						Constant.CURRENT_LEFT_START=currRR.leftStart;//記錄當前讀到處leftstart的值
						Constant.CURRENT_PAGE=currRR.pageNo;//記錄當前讀到處的page的值

						currRR.isLeft=true;
						bmLeft=drawPage(currRR);
						bmRight=drawPage(currRR);
						repaint();
					}
				   repaintAdFlag=true;//換完圖片再重繪
				   break;		    	
		    case 82:
		    	readerActivity.openOptionsMenu();
				   break; 
		 }
		   return true;
	}
	public boolean onTouchEvent(MotionEvent e) 
	{  
		repaintAdFlag=false;
    	float x = e.getX();//獲取觸控點X坐標
        float y = e.getY();//獲取觸控點Y坐標    	
        
        switch (e.getAction()) 
        {
            case MotionEvent.ACTION_DOWN:
            	/*
            	 * 當按下時判斷是要向後翻還是要向前翻，
            	 * 再初始化對應的b點的坐標值
            	 */
      	
            	if(x>RIGHT_OR_LEFT_x )//如果按在右邊，確定為要向後翻頁
            	{
            		//初始化為右下角坐標
            		bx=SCREEN_WIDTH;
            		by=SCREEN_HEIGHT;
            	}
            	else//如果按在左邊，確定為要向前翻頁
            	{
            		//初始化為左下角坐標
            		bx=0;
            		by=PAGE_HEIGHT+BLANK;
            	}
            	//計算c、d兩點坐標
            	cd=CalUtil.calCD(x, y, bx, by);

            	//若初次按下的位置在右下角指定範圍內則允許繪製翻頁效果
            	if(x>PAGE_WIDTH*1.7f&&x<SCREEN_WIDTH&&cd[0]>RIGHT_OR_LEFT_x)   
                {
            		if(Constant.nextPageStart>Constant.CONTENTCOUNT){
        				Toast.makeText
        				(
        					this.getContext(), 
        					"已經到最後一頁了，不可以再往後了！", 
        					Toast.LENGTH_SHORT
        				).show();
        				repaintAdFlag=true;//換完圖片再重繪
        				return true;
        			}

            	turnDir=TurnDir.right;             	   
            	 //繪製臨時下一頁的ReadRecord類的對象
       			currRR_temp=new ReadRecord(Constant.nextPageStart,0,Constant.nextPageNo);
       			//保護Constant.nextPageNo、Constant.nextPageNo兩個值
           		int t1=Constant.nextPageNo;
           		int t2=Constant.nextPageStart;
           		//創建下一頁的兩張圖片
       			bmLeft_temp=drawPage(currRR_temp);        			
       			bmRight_temp=drawPage(currRR_temp);        			
				Constant.nextPageNo=t1;
           		Constant.nextPageStart=t2;        	
            	   
                }//若初次按下的位置在左下角指定範圍內則允許繪製向前的翻頁效果
            	else 
            		if(x<PAGE_WIDTH*0.3&&cd[0]<PAGE_WIDTH)
                {
        			if(currRR.pageNo<=0){
						Toast.makeText
						(
							this.getContext(), 
							"已經到第一頁，不可以再往前翻了！", 
							Toast.LENGTH_SHORT
						).show();	
						repaintAdFlag=true;//換完圖片再重繪
						return true;
					}
        			
            		turnDir=TurnDir.left;
            		
            		currRR_temp=currBook.get(currRR.pageNo-1);

            		int t1=Constant.nextPageNo;
            		int t2=Constant.nextPageStart;
            		currRR_temp.isLeft=true;    		
					bmLeft_temp=drawPage(currRR_temp);
					bmRight_temp=drawPage(currRR_temp);
					Constant.nextPageNo=t1;
            		Constant.nextPageStart=t2;	
                }
            	ax=x;
          	   	ay=y;          	 
            break;        
            case MotionEvent.ACTION_MOVE: 
            	//翻頁時動態計算c、d兩點坐標
            	cd=CalUtil.calCD(x, y, bx, by);
            	
            	//若移動過程中沒有撕紙則允許繪製翻頁效果
                if(x>0&&x<SCREEN_WIDTH&&
             		   (turnDir==TurnDir.right&&cd[0]>PAGE_WIDTH)||//向後翻頁時沒有撕紙
             		   turnDir==TurnDir.left&&cd[0]<PAGE_WIDTH)//向前翻頁時沒有撕紙 
            	
                {
             	   ax=x;
             	   ay=y;            	   
                }
                else
                {
             	   turnDir=TurnDir.noTurning;
                }
              //若抬起的位置在左邊指定範圍則實施向後翻頁並且下一頁的索引值小於文章總字數
                if(turnDir==TurnDir.right && ax<PAGE_WIDTH*0.1f)	
                { 
                	currRR=new ReadRecord(Constant.nextPageStart,0,Constant.nextPageNo);
  				   	Constant.CURRENT_LEFT_START=currRR.leftStart;//記錄當前讀到處leftstart的值
  				   	Constant.CURRENT_PAGE=currRR.pageNo;//記錄當前讀到處的page的值
  				   	
  				   	currBook.put(currRR.pageNo, currRR);//當前頁的信息加入hashMap
			   
					//繪製左右兩幅圖片
					bmLeft=drawPage(currRR);
					bmRight=drawPage(currRR);  
					
					turnDir=TurnDir.noTurning;
                }
                //若抬起的位置在右邊指定範圍則實施向前翻頁
                else if(turnDir==TurnDir.left && ax>PAGE_WIDTH*1.9f)	
                {
					currRR=currBook.get(currRR.pageNo-1);
					
					Constant.CURRENT_LEFT_START=currRR.leftStart;//記錄當前讀到處leftstart的值
					Constant.CURRENT_PAGE=currRR.pageNo;//記錄當前讀到處的page的值
					
					currRR.isLeft=true;
					bmLeft=drawPage(currRR);
					bmRight=drawPage(currRR);
					
					turnDir=TurnDir.noTurning;
                }
            break;
            case MotionEvent.ACTION_UP:            	
                turnDir=TurnDir.noTurning;                 
              break;              
        }  
        this.repaint();
        repaintAdFlag=true;//換完圖片再重繪
        return true;
    }
   //繪製Bitmap的方法
	public Bitmap drawPage(ReadRecord rr)
	{
		int start=0;
		if(rr.isLeft)
		{
			start=rr.leftStart;
		}
		else
		{
			start=rr.rightStart;
		}
		
		Bitmap bm=Bitmap.createBitmap(PAGE_WIDTH, PAGE_HEIGHT,Bitmap.Config.ARGB_8888);
		Canvas canvas=new Canvas(bm);
		
		canvas.drawBitmap(bmBack,0,0, paint);
		canvas.drawBitmap(bmBack,0,0, paint);
		
		try
		{
			synchronized(paint)
			{
				String str=null;
				paint.setColor(COLOR);
				paint.setTextSize(TEXT_SIZE);//設置字的大小
				if(Constant.FILE_PATH==null)
				{
					str=TextLoadUtil.loadFromSDFile(this,start,PAGE_LENGTH,Constant.DIRECTIONSNAME);//讀取說明
					CONTENTCOUNT=TextLoadUtil.getCharacterCountApk(this, Constant.DIRECTIONSNAME);
				}else//否則讀正文
				{
					str=TextLoadUtil.readFragment(start, PAGE_LENGTH, FILE_PATH);//讀取正文
				}
				int index=0;
				int index2=0;//』\n'佔兩個字符
				char c=str.charAt(index);
				boolean finishFlag=false;		
				int currRow=0;
				int currX=0;
				while(!finishFlag)
				{
					if(c=='\n')  
					{//如果是換行 
						currRow++;
						currX=0;
						index2++;
					}
					else if((c<='z'&&c>='a')||(c<='Z'&&c>='A')||(c<='9'&&c>='0'))
					{//英文大小寫或數字
						canvas.drawText(c+"", currX+TEXT_SIZE/2, currRow*TEXT_SIZE+TEXT_SIZE, paint);
						currX=currX+TEXT_SPACE_BETWEEN_EN;
					}
					else
					{//中文
						canvas.drawText(c+"", currX+TEXT_SIZE/2, currRow*TEXT_SIZE+TEXT_SIZE, paint);
						currX=currX+TEXT_SPACE_BETWEEN_CN;
					}
					index++;
					c=str.charAt(index);
					
					if(currX>=Constant.PAGE_WIDTH-TEXT_SIZE)
					{
						currRow=currRow+1;
						currX=0;
					}
					if(currRow==ROWS)
					{
						finishFlag=true;
						if(rr.isLeft)
						{
							rr.isLeft=false;
							rr.rightStart=index+index2+rr.leftStart;
						}
						else
						{
							nextPageStart=rr.rightStart+index+index2;
							nextPageNo=rr.pageNo+1;
						}
					}
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return bm;
	}
	
	   //繪製虛擬頁Bitmap的方法
	public void drawVirtualPage(ReadRecord rr)
	{
		int start=0;
		if(rr.isLeft)
		{
			start=rr.leftStart;
		}
		else
		{
			start=rr.rightStart;
		}
		
		try
		{
			synchronized(paint)
			{
				String str=null;
				paint.setColor(COLOR);
				paint.setTextSize(TEXT_SIZE);//設置字的大小
				
				
				str=TextLoadUtil.readFragment(start, PAGE_LENGTH, FILE_PATH);//讀取正文
				
				int index=0;
				int index2=0;//』\n'佔兩個字符
				char c=str.charAt(index);
				boolean finishFlag=false;		
				int currRow=0;
				int currX=0;
				while(!finishFlag)
				{
					if(c=='\n')  
					{//如果是換行 
						currRow++;
						currX=0;
						index2++;
					}
					else if((c<='z'&&c>='a')||(c<='Z'&&c>='A')||(c<='9'&&c>='0'))
					{//英文大小寫或數字
						currX=currX+TEXT_SPACE_BETWEEN_EN;
					}
					else
					{//中文
						currX=currX+TEXT_SPACE_BETWEEN_CN;
					}
					index++;
					c=str.charAt(index);
					
					if(currX>=Constant.PAGE_WIDTH-TEXT_SIZE)
					{
						currRow=currRow+1;
						currX=0;
					}
					if(currRow==ROWS)
					{
						finishFlag=true;
						if(rr.isLeft)
						{
							rr.isLeft=false;
							rr.rightStart=index+index2+rr.leftStart;
						}
						else
						{
							nextPageStart=rr.rightStart+index+index2;
							nextPageNo=rr.pageNo+1;
						}
					}
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	

	public void drawTitle(Canvas canvas)
	{
		try
		{
			synchronized(paint)
			{
				paint.setColor(Color.BLACK);
				paint.setTextSize(TITLE_SIZE);
				canvas.drawText("酷讀閱讀器", 0, TITLE_SIZE, paint);
				if(Constant.FILE_PATH==null)
				{
					canvas.drawText("說明", Constant.SCREEN_WIDTH/2-TITLE_SIZE, TITLE_SIZE, paint);//繪製「說明」
					
				}else//否則書寫文章txt的名字
				{
					//將書名字大約畫在中間位置
					canvas.drawText(Constant.TEXTNAME,Constant.SCREEN_WIDTH/2-3*TITLE_SIZE,TITLE_SIZE, paint);//後期需要調
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void drawcut_line(Canvas canvas)
	{
		try
		{
			synchronized(paint)
			{
				paint.setColor(Color.YELLOW);//繪製分割線
				canvas.drawRect(CENTER_LEFT_X, CENTER_LEFT_Y, CENTER_RIGHT_X, CENTER_RIGHT_Y, paint);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//重新繪製的方法
    public void repaint()
	{
		Canvas canvas=this.getHolder().lockCanvas();
		try
		{
			synchronized(canvas)
			{
				onDraw(canvas);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(canvas!=null)
			{
				this.getHolder().unlockCanvasAndPost(canvas);
			}
		}
	}
    
}

