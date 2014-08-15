package com.bn.map;
import static com.bn.map.Constant.*;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.view.MotionEvent;
class GameSurfaceView extends GLSurfaceView 
{		
	MapMasetActivity father;//聲明Activity
	private float mPreviousY;//上次的觸控位置Y坐標
    private float mPreviousX;//上次的觸控位置Y坐標
    
    public static int guankaID;//管卡ID
    public static int[][]MAP;//對應關卡的地圖數組
    public static int[][] MAP_OBJECT;//對應關卡的洞數組
    public static int STIME;//每一關對應的時間限制
    
    public static float yAngle=0f;//方位角
    public static float xAngle=90f;//仰角 
    public static float cx;//攝像機x坐標
    public static float cy;//攝像機y坐標
    public static float cz;//攝像機z坐標
    public static float tx=0;//觀察目標點x坐標  
    public static float ty=0;//觀察目標點y坐標
    public static float tz=0f;//觀察目標點z坐標      
    public static float upX=0;
    public static float upY=1;
    public static float upZ=0;//up軸
    
    public static float ballX;//球的各個坐標
    public static float ballY;
    public static float ballZ;
    public static float ballGX=0f;//x方向上的加速度
    public static float ballGZ=0f;//y方向上的加速度
    
    public static int ballCsX;//初始格子
    public static int ballCsZ;
    public static int ballMbX;//目標格子
    public static int ballMbZ;
    
    public static float ballVX=0;//XZ方向上的速度
    public static float ballVZ=0;
   
    private SceneRenderer mRenderer;//場景渲染器	
    
    public static int floorId;//地板紋理ID
    public static int wallId;//牆紋理
    public static int yuankonId;//圓孔紋理Id
    public static int ballId;//球紋理ID
    public static int ballYZId;//球的影子紋理ID
    public static int numberId;//數字ID
    public static int time_DH_Id;//頓號ID
    public static int mbyuankonId;
    
	public RectWall yuankon;//圓孔矩形
	public Floor floor;//地板
	public static  Wall wall;//牆
	public BallTextureByVertex ball;//球
	public RectWall ballYZ;//球的影子矩形
	public Number number;//數字
	public TextureRect time_DH;//頓號，用於時間
	
	BallGDThread ballgdT;//球運動線程
	
	public GameSurfaceView(Context context)
	{
        super(context);
        this.father=(MapMasetActivity)context;
        ballCsX=CAMERA_COL_ROW[guankaID][0];//初始行列
        ballCsZ=CAMERA_COL_ROW[guankaID][1];
        
        ballMbX=CAMERA_COL_ROW[guankaID][2];//目標行列
        ballMbZ=CAMERA_COL_ROW[guankaID][3];
        
        MAP=MAPP[guankaID];//地圖數組
        MAP_OBJECT=MAP_OBJECTT[guankaID];//洞數組
        STIME=GD_TIME[guankaID];//限制時間
        
        ballX=ballCsX*UNIT_SIZE-MAP[0].length*UNIT_SIZE/2;//初始化球位置
		ballZ=ballCsZ*UNIT_SIZE-MAP.length*UNIT_SIZE/2;
		ballY=ballR;
       
        tx=0;//攝像機目標位置
        ty=0;
        tz=0;
        ballgdT=new BallGDThread(this);
        //設置攝像機的位置
        cx=(float)(tx+Math.cos(Math.toRadians(xAngle))*Math.sin(Math.toRadians(yAngle))*DISTANCE);//攝像機x坐標 
        cz=(float)(tz+Math.cos(Math.toRadians(xAngle))*Math.cos(Math.toRadians(yAngle))*DISTANCE);//攝像機z坐標 
        cy=(float)(ty+Math.sin(Math.toRadians(xAngle))*DISTANCE);//攝像機y坐標 
        mRenderer = new SceneRenderer();	//創建場景渲染器
        setRenderer(mRenderer);				//設置渲染器		
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//設置渲染模式為主動渲染      
       
    }	
	@Override 
    public boolean onTouchEvent(MotionEvent e) 
	{
        float y = e.getY();//得到按下的XY坐標
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
            if(xAngle+dy * TOUCH_SCALE_FACTOR>90)//當攝像機仰角大於85,將其強制為85;
            {
            	xAngle=90;
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
	
	private class SceneRenderer implements GLSurfaceView.Renderer 
	{
		
        public void onDrawFrame(GL10 gl) 
        {  
        	//採用平滑著色
            gl.glShadeModel(GL10.GL_SMOOTH);            
        	//清除顏色緩存於深度緩存
        	gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);        	
        	//設置當前矩陣為模式矩陣
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            //設置當前矩陣為單位矩陣
            gl.glLoadIdentity();    
            //設置camera位置
            GLU.gluLookAt
            (gl, cx,cy,cz, tx,ty, tz,0,1, 0);   //設置攝像機
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//啟用頂點數組
            gl.glEnable(GL10.GL_TEXTURE_2D);//啟用紋理
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY); 
            
            gl.glEnable(GL10.GL_LIGHTING);//允許光照
	        gl.glEnable(GL10.GL_LIGHT0);//開0號燈  	        
	        //允許使用法向量數組
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY); 
            
            floor.drawSelf(gl, floorId);//繪製地板
            
            gl.glPushMatrix();//保護矩陣
            gl.glTranslatef(-MAP[0].length/2*UNIT_SIZE, 0, (-MAP.length/2)*UNIT_SIZE);
            wall.drawSelf(gl, wallId);//繪製牆           
            gl.glPopMatrix();//恢復矩陣
            
            gl.glDisable(GL10.GL_LIGHTING);//關閉光照
            gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);//關閉法向量數組
            
            gl.glEnable(GL10.GL_BLEND);//開啟混合

            gl.glBlendFunc(GL10.GL_SRC_ALPHA ,GL10.GL_ONE_MINUS_SRC_ALPHA);//設置混合因子
            gl.glPushMatrix();//保護當前矩陣，
            gl.glTranslatef(ballMbX*UNIT_SIZE-MAP[0].length*UNIT_SIZE/2,
            		0.015f,
            		ballMbZ*UNIT_SIZE- MAP.length*UNIT_SIZE/2);
            gl.glRotatef(-90, 1, 0, 0);
			yuankon.drawSelf(gl, mbyuankonId);//繪製目標圓孔
			gl.glPopMatrix();
            drawYuanKong(gl);//繪製圓孔           
            gl.glPushMatrix();
        	gl.glTranslatef(ballX+ballR-0.2f, 0.01f, ballZ-ballR+0.2f);
        	gl.glRotatef(-90, 1, 0, 0);
        	gl.glRotatef(45, 0, 0, 1);
        	ballYZ.drawSelf(gl, ballYZId);//繪製影子
        	gl.glPopMatrix();            
            gl.glDisable(GL10.GL_BLEND);//關閉混合                    
            drawBall(gl);//繪製球            
            drawNumber(gl);//繪製當前剩餘時間數字            
            
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);   //關閉頂點數組         
            gl.glDisable(GL10.GL_TEXTURE_2D);//關閉紋理
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);        
         }        
        
        public  void drawNumber(GL10 gl)//繪製剩餘時間方法
        {
        	gl.glMatrixMode(GL10.GL_MODELVIEW);//恢復矩陣   
	        gl.glLoadIdentity();   	     //設置當前矩陣為單位矩陣
	      
	        gl.glEnable(GL10.GL_BLEND);//開啟混合
	        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);//設置混合因子
	        //繪製數字儀表盤    高度
	        gl.glPushMatrix();
	        gl.glTranslatef(2.0f,1.6f,-6);//設置儀表板的位置.不能再調節
	        number.y=0;//數字的Y坐標
	        number.NumberStr=Math.abs(GD_TIME[guankaID]-STIME)/60+"";//剩下的分鐘數
	        number.drawSelf(gl,0,numberId);//繪製分鐘
	        gl.glTranslatef(ICON_WIDTH*0.7f,0f,0);
	        time_DH.drawSelf(gl, time_DH_Id);//畫頓號
	        gl.glTranslatef(ICON_WIDTH*0.7f,0f,0);
	        number.NumberStr=Math.abs(GD_TIME[guankaID]-STIME)%60+"";
	        number.drawSelf(gl,1,numberId);//畫秒數
	        gl.glPopMatrix();//恢復矩陣
	        gl.glDisableClientState(GL10.GL_BLEND);//關閉混合       
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
            gl.glFrustumf(-ratio, ratio, -1, 1, 3, 1000);   
        }
        public void onSurfaceCreated(GL10 gl, EGLConfig config) 
        {
            //關閉抗抖動 
        	gl.glDisable(GL10.GL_DITHER);
        	//設置特定Hint項目的模式，這裡為設置為使用快速模式
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,GL10.GL_FASTEST);
            //設置為打開背面剪裁
    		gl.glEnable(GL10.GL_CULL_FACE);
    		 //設置著色模型為平滑著色   
            gl.glShadeModel(GL10.GL_SMOOTH);
    		//開啟混合   
            //設置屏幕背景色黑色RGBA
            gl.glClearColor(0,0,0,0);           
            //啟用深度測試
            gl.glEnable(GL10.GL_DEPTH_TEST);             
            floorId=initTexture(gl,R.drawable.floor);//地面ID
            wallId=initTexture(gl,R.drawable.wall);    //牆ID
            
            yuankonId=initTexture2(gl,R.drawable.yuankon);//圓孔ID
            ballId=initTexture2(gl,R.drawable.ball);//球ID
            ballYZId=initTexture2(gl,R.drawable.ballyingzi);//球的影子ID
            numberId=initTexture2(gl,R.drawable.number);//數字ID
            time_DH_Id=initTexture2(gl,R.drawable.dunhao);//頓號紋理
            mbyuankonId=initTexture2(gl,R.drawable.mbyuankon);//目標圓孔Id
            
            
            floor=new Floor(MAP[0].length,MAP.length);//地板
            wall=new Wall();//牆     
            yuankon=new RectWall(2f*ballR,2f*ballR);//圓孔
            ball=new BallTextureByVertex(ballR,15);//球
            ballYZ=new RectWall(3.6f*ballR,2.6f*ballR);//影子
            number=new Number(GameSurfaceView.this);//數字對像
            time_DH=new TextureRect(ICON_WIDTH*0.5f/2,//數字
            	 ICON_HEIGHT*0.5f/2,
           		 new float[]
		             {
		           	  0,0, 0,1, 1,0,
		           	  0,1, 1,1,  1,0
		             });//頓號
            ballgdT.start();
            
            initLight(gl);//初始化燈光
            float[] positionParamsGreen={-4,4,4,0};//最後的0表示是定向光
            gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, positionParamsGreen,0); 
     
        }
        public void drawBall(GL10 gl)//畫重力球
        {	
        	gl.glPushMatrix();
        	gl.glTranslatef(ballX, ballY, ballZ);     //移動相應的位置 	
        	ball.drawSelf(gl, ballId);   //繪製
        	gl.glPopMatrix();       	
        }
        public void drawYuanKong(GL10 gl)//繪製圓孔
        {
        	gl.glPushMatrix();
        	gl.glTranslatef(-MAP[0].length*UNIT_SIZE/2, 0.01f,- MAP.length*UNIT_SIZE/2);
        	for(int i=0;i<MAP_OBJECT.length;i++)
        	{
        		for(int j=0;j<MAP_OBJECT[0].length;j++)
        		{
        			if(MAP_OBJECT[i][j]==1)
        			{
        				if(i==ballMbX&&j==ballMbZ)//如果不是目標洞則繪製
        				{
        				   continue;
        				}
        				gl.glPushMatrix();
        				gl.glTranslatef((j)*UNIT_SIZE, 0.001f, (i)*UNIT_SIZE);
        				gl.glRotatef(-90, 1, 0, 0);
        				yuankon.drawSelf(gl, yuankonId);//繪製
        				gl.glPopMatrix();
        			}
        		}
        	}
        	gl.glPopMatrix();
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
	 }
	//初始化紋理
	public int initTexture2(GL10 gl,int drawableId)//textureId
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

	//初始化紋理
	public int initTexture(GL10 gl,int drawableId)//textureId
	{
		//生成紋理ID
		int[] textures = new int[1];
		gl.glGenTextures(1, textures, 0);    
		int currTextureId=textures[0];    
		gl.glBindTexture(GL10.GL_TEXTURE_2D, currTextureId);
		
		//在MIN_FILTER MAG_FILTER中使用MIPMAP紋理
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR_MIPMAP_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR_MIPMAP_LINEAR);
		// 生成Mipmap紋理
		((GL11)gl).glTexParameterf(GL10.GL_TEXTURE_2D,GL11.GL_GENERATE_MIPMAP,GL10.GL_TRUE);
        //紋理拉伸方式
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_REPEAT);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,GL10.GL_REPEAT);
        
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

