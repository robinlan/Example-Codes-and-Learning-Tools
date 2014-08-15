package com.bn.gjxq;

import java.io.IOException;
import java.io.InputStream;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;
import static com.bn.gjxq.Constant.*;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MySurfaceView extends GLSurfaceView
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320*3;//角度縮放比例
    private SceneRenderer mRenderer;//場景渲染器
	
	private float mPreviousY;//上次的觸控位置Y坐標
    private float mPreviousX;//上次的觸控位置Y坐標
    
    static float cx;//攝像機x坐標
    static float cy;//攝像機y坐標
    static float cz;//攝像機z坐標
    
    static float tx;//觀察目標點x坐標  
    static float ty;//觀察目標點y坐標
    static float tz;//觀察目標點z坐標
 
    static float yAngle=0;//方位角
    static float xAngle=30;//仰角
    static boolean OKMove=false;//是否需要移動棋子標誌
    
    static int herosquareZ=7;//主正方形格子的初始行數
    static int herosquareX=0;//主正方形格子的初始列數
    
    static int herosquarez;//當確定了要走某個英雄時，記錄這個英雄的原來所在坐標。
    static int herosquarex;
    
    static LoadedObjectVertexNormalTexture[] qizi;//模型數組
    ChessboardForDraw cb;//棋盤
    
    ChessForControl[][] currBoard;//棋子數組
    static int[][] road;
    GJXQActivity father;//activity引用
  
	public MySurfaceView(Context context)
	{ 
        super(context);
        father=(GJXQActivity)context;//Activity對像
        mRenderer = new SceneRenderer();	
        setRenderer(mRenderer);				 	
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//設置為連續渲染模式
        tx=0;//觀察目標點x坐標  
	    ty=0;//平視觀察目標點y坐標
	    tz=0;//觀察目標點z坐標
	    road=new int[8][8];

	    //初始化走棋路徑
	    if(father.ca.num==1)
	    {
	    	herosquareZ=1;//如果是黑方,把紅格子定位在1,0格子裡面
	    	herosquareX=0;
	        //如果當前為黑方,則光標處於黑方位置
	    }
	    else
	    {
	    	herosquareZ=6;//如果是白方,就把紅格子放入6,0格子裡面
	    	herosquareX=0;
	    }
	    road[herosquareZ][herosquareX]=1;//把初始位置的紅方格定位於此  
	    yAngle=((father.ca.num%2)==1)?180:0;//如果當前為黑方,則攝像機方向角旋轉180.
		cx=(float)(tx+Math.cos(Math.toRadians(xAngle))*Math.sin(Math.toRadians(yAngle))*DISTANCE);//觀察者x坐標 
        cz=(float)(tz+Math.cos(Math.toRadians(xAngle))*Math.cos(Math.toRadians(yAngle))*DISTANCE);//觀察者z坐標 
        cy=(float)(ty+Math.sin(Math.toRadians(xAngle))*DISTANCE);  
       //初始化棋盤
        cb=new ChessboardForDraw();
        initBoard();//初始化棋子
    }
	
	public void initBoard()//初始化棋子數組的函數
	{
		currBoard=new ChessForControl[8][8];
		
		currBoard[0][0]=new ChessForControl(qizi[0] ,0,0,0);//黑車
		currBoard[0][1]=new ChessForControl(qizi[1] ,1,0,1);//黑馬
		currBoard[0][2]=new ChessForControl(qizi[2] ,2,0,2);//黑象
		currBoard[0][3]=new ChessForControl(qizi[3] ,3,0,3);//黑後
		currBoard[0][4]=new ChessForControl(qizi[4] ,4,0,4);//黑王
		currBoard[0][5]=new ChessForControl(qizi[2] ,2,0,5);//黑象
		currBoard[0][6]=new ChessForControl(qizi[1] ,1,0,6);//黑馬
		currBoard[0][7]=new ChessForControl(qizi[0] ,0,0,7);//黑車
		
		currBoard[1][0]=new ChessForControl(qizi[5] ,5,1,0);//黑兵
		currBoard[1][1]=new ChessForControl(qizi[5] ,5,1,1);//黑兵
		currBoard[1][2]=new ChessForControl(qizi[5] ,5,1,2);//黑兵
		currBoard[1][3]=new ChessForControl(qizi[5] ,5,1,3);//黑兵
		currBoard[1][4]=new ChessForControl(qizi[5] ,5,1,4);//黑兵
		currBoard[1][5]=new ChessForControl(qizi[5] ,5,1,5);//黑兵
		currBoard[1][6]=new ChessForControl(qizi[5] ,5,1,6);//黑兵
		currBoard[1][7]=new ChessForControl(qizi[5] ,5,1,7);//黑兵
		
		currBoard[6][0]=new ChessForControl(qizi[5] ,11,6,0);//白兵
		currBoard[6][1]=new ChessForControl(qizi[5] ,11,6,1);//白兵
		currBoard[6][2]=new ChessForControl(qizi[5] ,11,6,2);//白兵
		currBoard[6][3]=new ChessForControl(qizi[5] ,11,6,3);//白兵
		currBoard[6][4]=new ChessForControl(qizi[5] ,11,6,4);//白兵
		currBoard[6][5]=new ChessForControl(qizi[5] ,11,6,5);//白兵
		currBoard[6][6]=new ChessForControl(qizi[5] ,11,6,6);//白兵
		currBoard[6][7]=new ChessForControl(qizi[5] ,11,6,7);//白兵
		
		currBoard[7][0]=new ChessForControl(qizi[0] ,6,7,0);//白車
		currBoard[7][1]=new ChessForControl(qizi[1] ,7,7,1);//白馬
		currBoard[7][2]=new ChessForControl(qizi[2] ,8,7,2);//白象
		currBoard[7][3]=new ChessForControl(qizi[3] ,9,7,3);//白後
		currBoard[7][4]=new ChessForControl(qizi[4] ,10,7,4);//白王
		currBoard[7][5]=new ChessForControl(qizi[2] ,8,7,5);//白象
		currBoard[7][6]=new ChessForControl(qizi[1] ,7,7,6);//白馬
		currBoard[7][7]=new ChessForControl(qizi[0] ,6,7,7);//白車	
	}

	@Override 
    public boolean onTouchEvent(MotionEvent e) 
	{
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction())
        {
        case MotionEvent.ACTION_MOVE:
            float dy = y - mPreviousY;//計算觸控筆Y位移
            float dx = x - mPreviousX;//計算觸控筆X位移
            yAngle += dx * TOUCH_SCALE_FACTOR;//仰角改變
            xAngle += dy * TOUCH_SCALE_FACTOR;//方位角改變    
            if(xAngle+dy * TOUCH_SCALE_FACTOR<10)//如果當前攝像機仰角小於15,將其仰角強制為15
            {
            	xAngle=10;
            }
            if(xAngle+dy * TOUCH_SCALE_FACTOR>85)//當攝像機仰角大於85,將其強制為85;
            {
            	xAngle=85;
            }
            requestRender();//重繪畫面
        }
        mPreviousY = y;//記錄觸控筆位置
        mPreviousX = x;//記錄觸控筆位置    
        cx=(float)(tx+Math.cos(Math.toRadians(xAngle))*Math.sin(Math.toRadians(yAngle))*DISTANCE);//攝像機x坐標 
        cz=(float)(tz+Math.cos(Math.toRadians(xAngle))*Math.cos(Math.toRadians(yAngle))*DISTANCE);//攝像機z坐標 
        cy=(float)(ty+Math.sin(Math.toRadians(xAngle))*DISTANCE);//攝像機y坐標 
        return true;
    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent e)
    { 
		if(!father.ca.perFlag)//當前不是該玩家下棋,那麼直接返回,玩家無任何操控權利
		{
			return false;
		}
		if(keyCode==19||keyCode==20||keyCode==21||keyCode==22)//如果是按下前後左右建進行移動的情況.
		{
			road[herosquareZ][herosquareX]=0;//將此處的格子去掉
			if(keyCode==19)//分別代表前移，後移，左移，右移
    		{
				herosquareZ++;
    		}
    		else if(keyCode==20)//向後移動
    		{
    			herosquareZ--;
    		}
    		else if(keyCode==21)//向左移動
    		{
    			herosquareX++;
    		}
    		else if(keyCode==22)//向右移動
    		{
    			herosquareX--;
    		}
    	
    		if(herosquareX<0)//如果移動在左邊界了,那麼不可移動,
    		{
    			herosquareX=0;
			}
			if(herosquareX>7)//移動到了右邊界了
			{
				herosquareX=7;
			}
			if(herosquareZ<0)//移動到了上邊界了
			{
				herosquareZ=0;
			}
			if(herosquareZ>7)//移動到了下邊界了
			{
				herosquareZ=7;
			}
			
			road[herosquareZ][herosquareX]=1;//把格子移動到當前位置
			
		}
		else if(keyCode==62)//如果是空格鍵,這裡代表玩家操控棋子的建.
		{
			if(!OKMove&&currBoard[herosquareZ][herosquareX]!=null)//第一次按下空格
			{
				if(currBoard[herosquareZ][herosquareX].chessType>=(father.ca.num%2)*6
				   &&currBoard[herosquareZ][herosquareX].chessType<(father.ca.num%2+1)*6)
					//第一次按下空格,此格子不為空,並且該棋子是對方的,那麼不可操控
						{
							Toast.makeText
							(
									father, 
									"不能動別人的棋子,請重新操作!", 
									Toast.LENGTH_SHORT
							).show();
						
						}
				else//如果是自己的棋子,而且是第一次標誌,那麼記錄當前位置
					{OKMove=true;//至此標誌位為true
					herosquarez=herosquareZ;//並且記錄標記的英雄
					herosquarex=herosquareX;
					currBoard[herosquarez][herosquarex].y=0.4f;
					}
			}
			else if(OKMove&&currBoard[herosquarez][herosquarex]!=null
					&&GuiZe.canMove(currBoard[herosquarez][herosquarex], currBoard, herosquareZ, herosquareX)
					)//加可走接口，庫
    			//第二次按下空格，而且可走.
    		{				
      			OKMove=false;//同時恢復標記，進行下一輪      			
      			int srcRow=herosquarez;
      			int srcCol=herosquarex;
      			int dstRow=herosquareZ;
      			int dstCol=herosquareX;     
      			String msg="<#MOVE#>"+srcRow+","+srcCol+","+dstRow+","+dstCol;//向服務器發送下棋操作,並且攜帶下棋信息
      			father.ca.sendMessage(msg);
      			father.ca.perFlag=false;
    		}
    		else if(OKMove&&currBoard[herosquarez][herosquarex]!=null&&!GuiZe.canMove(currBoard[herosquarez][herosquarex], currBoard, herosquareZ, herosquareX))//第二次按下空格，該地方不可走，表示玩家不下這顆棋
    		{//第二次按下棋子,而且不可下,那麼恢復標記,將第一次抬起的棋子恢復原狀
    			currBoard[herosquarez][herosquarex].y=0f;
    			OKMove=false;
    			Toast.makeText//同時發Toast.提示玩家
    			(
    				father, 
    				"不符合規則,請重新操作!", 
    				Toast.LENGTH_SHORT
    			).show();
    		}
		}
    	return true;
    }
	private class SceneRenderer implements GLSurfaceView.Renderer 
    { 		
		ChessFoundation foundation;//底座
		TriangleS sanjiao1;//上面的三角形
		TriangleX sanjiao2;//下面的三角形
		RectWall heifang;//黑方條形板
		RectWall baifang;//白方條形板
		RectWall wall;//牆的正方形
		
		public int foundationTexId;//底座紋理ID
		public int qipantexId;
		public int walltexId;//牆面
		public int floortexId;//地面
		public int rooftexId;//屋頂紋理
		public int heitexId;//黑方紋理
		public int baitexId;//白方紋理
		public int triangletexIds;//三角形上面紋理
		public int triangletexIdx;//三角形下面紋理
		
		public int whitechesstexId;//棋子紋理
		public int blackchesstexId;

		public SceneRenderer(){}
      
        public void onDrawFrame(GL10 gl) 
        {                  	
        	//清除顏色緩存於深度緩存
        	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        	//設置當前矩陣為模式矩陣
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            //設置當前矩陣為單位矩陣
            gl.glLoadIdentity(); 
            //設置攝像機位置
            GLU.gluLookAt(gl, cx, cy, cz, tx, ty, tz, 0, 1, 0);
            
            gl.glDisable(GL10.GL_LIGHTING);//不允許光照
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//啟用頂點數組
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);//啟用顏色數組
			cb.drawself(gl);//畫棋盤
			gl.glDisableClientState(GL10.GL_COLOR_ARRAY);//關閉顏色數組緩衝		
			//開啟紋理
	        gl.glEnable(GL10.GL_TEXTURE_2D);   
	        //允許使用紋理ST坐標緩衝
	        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			foundation.drawSelf(gl, foundationTexId,qipantexId);//畫底座    
            drawWall( gl);//畫房間 
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);//關閉顏色數組緩衝
	        gl.glEnable(GL10.GL_LIGHTING);//允許光照
	        gl.glEnable(GL10.GL_LIGHT0);//開0號燈        
	        gl.glEnable(GL10.GL_LIGHT1);//開一號燈
	        gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);//啟用頂點法向量數組     
	        for(int i=0;i<currBoard.length;i++)//畫黑色棋子
	        {
	        	for(int j=0;j<currBoard[0].length;j++)
	        	{
	        		if(currBoard[i][j]!=null&&currBoard[i][j].chessType<=5&&currBoard[i][j].chessType>=0)
	        		{
	        			currBoard[i][j].drawSelf(gl,blackchesstexId);
	        		}
	        	}
	        }
	        for(int i=0;i<currBoard.length;i++)//循環棋子數組畫白色棋子,傳不同的紋理ID
	        {
	        	for(int j=0;j<currBoard[0].length;j++)
	        	{
	        		if(currBoard[i][j]!=null&&currBoard[i][j].chessType<=11&&currBoard[i][j].chessType>=6)
	        		{
	        			currBoard[i][j].drawSelf(gl,whitechesstexId);
	        		}
	        	}
	        }        
          gl.glDisable(GL10.GL_LIGHTING);//關閉光照
          gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);//關閉發向量數組
          gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);//禁用頂點法向量數組
          gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//禁用使用紋理ST坐標緩衝
          gl.glDisable(GL10.GL_TEXTURE_2D);  
          drawPlayerNum(gl);//畫條幅版    
        }

        public void onSurfaceChanged(GL10 gl, int width, int height)
        {
            //設置視窗大小及位置 
        	gl.glViewport(0, 0, width, height);
        	//設置當前矩陣為投影矩陣
            gl.glMatrixMode(GL10.GL_PROJECTION);
            //設置當前矩陣為單位矩陣
            gl.glLoadIdentity();
            //計算透視投影的比例
            float ratio = (float) width / height;
            //調用此方法計算產生透視投影矩陣
            gl.glFrustumf(-ratio, ratio, -1f, 1f, 2.0f, 400);    
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {
            //關閉抗抖動 
        	gl.glDisable(GL10.GL_DITHER);
        	//設置特定Hint項目的模式，這裡為設置為使用快速模式
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,GL10.GL_FASTEST);
            //設置屏幕背景色黑色RGBA
            gl.glClearColor(0,0,0,0);     
            //啟用深度測試
            gl.glEnable(GL10.GL_DEPTH_TEST);
            //打開背面剪裁
            gl.glEnable(GL10.GL_CULL_FACE);
            //打開平滑著色
            gl.glShadeModel(GL10.GL_SMOOTH);            
            
            foundation=new ChessFoundation();//建立底盤
            wall=new RectWall((HOUSE_SIZE+0.5f)*UNIT_SIZE,(HOUSE_SIZE+0.5f)*UNIT_SIZE);
            walltexId=initTexture(gl,R.drawable.qiangbi);//牆壁紋理
            floortexId=initTexture(gl,R.drawable.diban);//地板紋理
            rooftexId=initTexture(gl,R.drawable.wuding);//屋頂的紋理
            
            foundationTexId=initTexture(gl,R.drawable.dizuo);//加載底盤紋理
            qipantexId=initTexture(gl,R.drawable.qipan);
            triangletexIds=initTexture(gl,R.drawable.sjx);//上面的朝向下的三角形紋理
            triangletexIdx=initTexture(gl,R.drawable.sjxs);//下方的朝向上的三角形
            
            heitexId=initTexture(gl,R.drawable.heifang);//黑方紋理
            baitexId=initTexture(gl,R.drawable.baifang);//白方紋理
            
            whitechesstexId=initTexture(gl,R.drawable.whitechess);
            blackchesstexId=initTexture(gl,R.drawable.blackchess);
            
            heifang=new RectWall(0.7f,0.35f);//造黑方圖片
            heifang.x=BLACK_FLAG_X;
            heifang.y=BLACK_FLAG_Y;
            heifang.z=-4;
            
            
            baifang=new RectWall(0.7f,0.35f);//造白方圖片
            baifang.x=WHITE_FLAG_X;
            baifang.y=WHITE_FLAG_Y;
            baifang.z=-4;

            sanjiao1=new TriangleS(0.25f);//造兩個指示黑白方三角形
            sanjiao1.y=PLAYER_TYPE_Y;
            sanjiao1.z=-4;
            
            sanjiao2=new TriangleX(0.255f);//指示誰下棋的三角形
            sanjiao2.y=CURR_MOVE_PLAYER_Y;
            sanjiao2.z=-4;
            
            initLight(gl);//初始化燈光
            float[] positionParamsGreen={0,16*UNIT_SIZE,16.5f*UNIT_SIZE,1};//最後的1表示是定位光
            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, positionParamsGreen,0); 
            
            initLight2(gl);
            float[] positionParamsGreen2={0,16*UNIT_SIZE,-16.5f*UNIT_SIZE,1};//最後的1表示是定位光
            gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, positionParamsGreen2,0);
            
        }
        //繪製三角標誌板
        public void drawPlayerNum(GL10 gl)
        {
        	if(father.ca.num==1)//如果是黑方
        	{
        		sanjiao1.x=PLAYER_TYPE_X1;
        	}
        	else
        	{
        		sanjiao1.x=PLAYER_TYPE_X2;
        	}
        	
        	
        	if((father.ca.perFlag&&father.ca.num==1)||((!father.ca.perFlag)&&father.ca.num==2))
        	{
        		sanjiao2.x=CURR_MOVE_PLAYER_X1;
        	}
        	else
        	{
        		sanjiao2.x=CURR_MOVE_PLAYER_X2;
        	}        	
            //設置當前矩陣為模式矩陣
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            //設置當前矩陣為單位矩陣
            gl.glLoadIdentity(); 
            gl.glDisable(GL10.GL_LIGHTING);//關燈
            
            //開啟紋理
	        gl.glEnable(GL10.GL_TEXTURE_2D);   
	        //允許使用紋理ST坐標緩衝
	        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	        
            sanjiao1.drawSelf(gl, triangletexIds);
            sanjiao2.drawSelf(gl, triangletexIdx);
            gl.glEnable(GL10.GL_BLEND);//開啟混合
            gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_COLOR);
            heifang.drawSelf(gl, heitexId);
            baifang.drawSelf(gl, baitexId);
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);//關閉紋理
            gl.glDisable(GL10.GL_TEXTURE_2D);
            gl.glDisable(GL10.GL_BLEND);//關閉混合
        	
        }
         public void drawWall(GL10 gl)
        {
        	gl.glPushMatrix();
	        gl.glTranslatef(0,(HOUSE_SIZE/2-1)*UNIT_SIZE,-HOUSE_SIZE/2*UNIT_SIZE);
        	wall.drawSelf(gl, walltexId);
        	gl.glTranslatef(0, 0, HOUSE_SIZE*UNIT_SIZE);
        	gl.glRotatef(180, 0, 1, 0);
        	wall.drawSelf(gl,walltexId );
        	gl.glPopMatrix();
        	
        	gl.glPushMatrix();
        	gl.glTranslatef(-HOUSE_SIZE/2*UNIT_SIZE, (HOUSE_SIZE/2-1)*UNIT_SIZE, 0);
        	gl.glRotatef(90, 0, 1, 0);
        	wall.drawSelf(gl, walltexId);
        	gl.glTranslatef(0, 0, HOUSE_SIZE*UNIT_SIZE);
        	gl.glRotatef(180, 0, 1, 0);
        	wall.drawSelf(gl, walltexId);
        	gl.glPopMatrix();
        	
        	gl.glPushMatrix();
        	gl.glRotatef(-90, 1, 0, 0);
        	gl.glTranslatef(0,0,-UNIT_SIZE);
        	wall.drawSelf(gl,floortexId);
        	gl.glTranslatef(0, 0,HOUSE_SIZE*UNIT_SIZE);
        	gl.glRotatef(180, 0, 1, 0);
        	wall.drawSelf(gl,rooftexId );
        	gl.glPopMatrix();
        }
    }
	
	private void initLight(GL10 gl)
	{
		//白色燈光
        gl.glEnable(GL10.GL_LIGHT0);//打開0號燈  
        
        //環境光設置
        float[] ambientParams={1f,1f,1f,1.0f};//光參數 RGBA
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams,0);            

        //散射光設置
        float[] diffuseParams={1f,1f,1f,1.0f};//光參數 RGBA
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams,0); 
        
        //反射光設置
        float[] specularParams={1f,1f,1f,1.0f};//光參數 RGBA
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams,0);     
	}
	
	private void initLight2(GL10 gl)
	{
		//白色燈光
        gl.glEnable(GL10.GL_LIGHT1);//打開0號燈  
        
        //環境光設置
        float[] ambientParams={1f,1f,1f,1.0f};//光參數 RGBA
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, ambientParams,0);            

        //散射光設置
        float[] diffuseParams={1f,1f,1f,1.0f};//光參數 RGBA
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, diffuseParams,0); 
        
        //反射光設置
        float[] specularParams={1f,1f,1f,1.0f};//光參數 RGBA
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPECULAR, specularParams,0);     
	}
	//加載棋子模型
	public static void initChessForDraw(Resources r)
	{
		qizi=new LoadedObjectVertexNormalTexture[]
		    {
		        LoadUtil.loadFromFileVertexOnly("che.obj", r),
		        LoadUtil.loadFromFileVertexOnly("ma.obj", r),
		        LoadUtil.loadFromFileVertexOnly("xiang.obj", r),
	            LoadUtil.loadFromFileVertexOnly("hou.obj", r),
		        LoadUtil.loadFromFileVertexOnly("wang.obj", r),
		        LoadUtil.loadFromFileVertexOnly("bing.obj", r)
		     };
	}
	//初始化紋理
	public int initTexture(GL10 gl,int drawableId)//textureId
	{
		//生成紋理ID
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);    
		int currTextureId=textures[0];    
		gl.glBindTexture(GL10.GL_TEXTURE_2D, currTextureId);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,GL10.GL_CLAMP_TO_EDGE);
        
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp; 
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapTmp, 0);
        bitmapTmp.recycle();  
        return currTextureId;
	}
}

