package com.bn.map;
import static com.bn.map.GameSurfaceView.*;
import static  com.bn.map.Constant.*;
public class BallGDThread extends Thread
{
	GameSurfaceView gameSurface;//surfaceView的引用
	public Boolean flag=true;//線程標誌位
	public static float t=0.1f;//每次走的時間
	public static int ZJS_Time;//總計時間，每一關的，從開始到完成開始計時
	public static float ballRD;
	public float ballGX;
	public float ballGZ;//每次拷貝加速度
	
	
	int ballXx=0;//此格所在的行列
	int ballZz=0;
	public static Boolean flagSY=true;//判斷是否掉進洞標誌      
	public BallGDThread(GameSurfaceView gameSurface)
	{
		this.gameSurface=gameSurface;
		ballRD=ballR/2;
	}
	@Override   
	public void run()
	{
		while(flag)
		{
			ballGX=GameSurfaceView.ballGX;//拷貝加速度
			ballGZ=GameSurfaceView.ballGZ;
			try{
			PZJC(ballX,ballZ,ballVX*t+ballGX*t*t/2,ballVZ*t+ballGZ*t*t/2);//判斷碰撞情況方法
			}catch (Exception tt) {
				tt.printStackTrace();
			}
			ballVX+=ballGX*t;
			ballVZ+=ballGZ*t;//最終速度
			
			ballX=ballX+ballVX*t+ballGX*t*t/2;//VT+1/2A*T*T
			ballZ=ballZ+ballVZ*t+ballGZ*t*t/2;//最終位置		
			gameSurface.ball.mAngleX+=(float)Math.toDegrees(((ballVZ*t+ballGZ*t*t/2))/ballR);
			gameSurface.ball.mAngleZ-=(float)Math.toDegrees((ballVX*t+ballGX*t*t/2)/ballR);//旋轉的角度
			if(Math.abs((ballVZ*t+ballGZ*t*t/2))<0.005f)//如果當前前進值小於調整值，則相應的轉動方向角歸零
			{
				gameSurface.ball.mAngleX=0;
			}
			if(Math.abs(ballVX*t+ballGX*t*t/2)<0.005f)
			{
				gameSurface.ball.mAngleZ=0;
			}
			//*********************判斷是否撞角*****************//
			pdZJ();//判斷進洞函數，及相應的操作
			
			if(!flagSY)//如果掉進洞裡了
			{
				flagSY=true;//初始到開始
				if(ballXx==ballMbX&&ballZz==ballMbZ)//如果是贏了
				{
					try 
					{
						Thread.sleep(1000);
					} 
					catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
					flag=false;
					gameSurface.father.curr_grade=GD_TIME[guankaID]-STIME;
					gameSurface.father.hd.sendEmptyMessage(1);//進入贏的界面
				}
				else//否則是進洞了
				{
					try
			        {
						Thread.sleep(1000);//停頓1秒
					} catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				ballX=ballCsX*UNIT_SIZE-MAP[0].length*UNIT_SIZE/2;
				ballZ=ballCsZ*UNIT_SIZE-MAP.length*UNIT_SIZE/2;
				ballY=ballR;
//				ZJS_Time=0;
				}
				ballVX=0;
				ballVZ=0;//最終速度都減為零
//				ballGX=0;
//				ballGZ=0;
			}		
			ballVX*=V_TENUATION;
			ballVZ*=V_TENUATION;//衰減
			if(Math.abs(ballVX)<0.04)//當速度小於某個調整值時
			{
				ballVX=0;//速度歸零
				gameSurface.ball.mAngleZ=0;//將繞軸選擇的值置為零
			}
			if(Math.abs(ballVZ)<0.04)
			{
				ballVZ=0;
				gameSurface.ball.mAngleX=0;
			}
			
			
			ZJS_Time+=50;//每局的總時間家
			STIME=(ZJS_Time/1000);//走過的時間秒數
			if(GD_TIME[guankaID]-STIME<=0)//如果時間減到零，說明沒有通過
			{
				flag=false;
				gameSurface.father.hd.sendEmptyMessage(2);
			}
			try //休息50毫秒，進入下一次循環
			{
				Thread.sleep(50);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
	public static void initDiTu()//初始化地圖方法
	{
		guankaID%=MAPP.length;//防止數組越界
        ballY=0;
        MAP=MAPP[guankaID];//地圖數組
        
        Wall walll=new Wall();//新建地圖
        try
        {
			Thread.sleep(1000);//停頓1秒
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}		
		wall=walll;//將新建地圖付給當前場景繪製地圖
		ballCsX=CAMERA_COL_ROW[guankaID][0];//初始球所在的初始行列
        ballCsZ=CAMERA_COL_ROW[guankaID][1];
        
        ballMbX=CAMERA_COL_ROW[guankaID][2];//鍾離權目標行列
        ballMbZ=CAMERA_COL_ROW[guankaID][3];
		MAP_OBJECT=MAP_OBJECTT[guankaID];//該地圖的洞數組
        ballX=ballCsX*UNIT_SIZE-MAP[0].length*UNIT_SIZE/2;//將球畫到初始位置
		ballZ=ballCsZ*UNIT_SIZE-MAP.length*UNIT_SIZE/2;
		STIME=GD_TIME[guankaID];//限制時間，還原
		ZJS_Time=0;//總時間歸零
	}
	public Boolean PZJC(float ballX,float ballZ,float BX,float BZ)
	{
		Boolean flag=false;
		ballX=MAP[0].length*UNIT_SIZE/2+ballX;//將地圖移到XZ都大於零的象限
		ballZ=MAP.length*UNIT_SIZE/2+ballZ;
		if(BZ>0)//如果向Z軸正方向運動
		{
			for(int i=(int)((ballZ+ballR)/UNIT_SIZE);i<=(int)((ballZ+ballR+BZ)/UNIT_SIZE);i++)
				//循環，假如它一下穿過幾個格子，那麼從第一個格子開始判斷
			{
				if(MAP[i][(int)(ballX/UNIT_SIZE)]==BKTG&&MAP[i-1][(int)(ballX/UNIT_SIZE)]==KTG){//判斷是否碰牆壁了
					ballVZ=-ballVZ*VZ_TENUATION;//將速度置反，並調整
					if((GameSurfaceView.ballZ+ballVZ*t+ballGZ*t*t/2)>=(i*UNIT_SIZE-ballR-MAP.length*UNIT_SIZE/2))//如果速度調反後還是會穿牆，那麼將加速度歸零，並將球畫在和牆壁緊挨著的地方
					{
						GameSurfaceView.ballZ=(i*UNIT_SIZE-ballR-MAP.length*UNIT_SIZE/2);
						ballVZ=0;
						ballGZ=0;
					}
					else{					
					gameSurface.father.playSound(1, 0);//播放移動聲音
					gameSurface.father.shake();//震動
					}
					flag=true;//標誌位置為true
				}
				else if(BX<=0&&((int)((ballX-ballR)/UNIT_SIZE)>=0)&&(MAP[i][(int)((ballX-ballR)/UNIT_SIZE)]==BKTG)
						&&MAP[i-1][(int)((ballX-ballR)/UNIT_SIZE)]==KTG)//如果是球的Z負方向邊碰角，可能會碰到角
				{
					float sina=(ballX-((int)((ballX-ballR)/UNIT_SIZE)+1)*UNIT_SIZE)/ballR;//得到碰角時的角度相關值
					float cosa=(float)Math.sqrt(1-sina*sina);
					ballVX=jsSDX(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;//得到碰角後的速度
					ballVZ=-jsSDZ(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;
					ballGX=0;
					ballGZ=0;
					if(Math.abs(ballVX)>SD_TZZ||Math.abs(ballVZ)>SD_TZZ){//如果碰撞很大，則播放聲音
					gameSurface.father.playSound(1, 0);//播放移動聲音
					gameSurface.father.shake();
					}else if(Math.abs(ballVZ)<SD_TZZ)//如果速度小於調整值，則不彈起，而且速度值為零
					{
						GameSurfaceView.ballZ=i*UNIT_SIZE-ballR-MAP.length*UNIT_SIZE/2;
						ballVZ=0;
						ballGZ=0;
					}else if(Math.abs(ballVX)<SD_TZZ)//如果速度小於調整值，則不彈起，而且速度值為零
					{
						GameSurfaceView.ballX=(1+i)*UNIT_SIZE-ballR-MAP.length*UNIT_SIZE/2;
						ballVZ=0;
						ballGZ=0;
					}
					flag=true;
				}
				else if(BX>=0&&((int)((ballX+ballR)/UNIT_SIZE)>=0)&&MAP[i][(int)((ballX+ballR)/UNIT_SIZE)]==BKTG
						&&MAP[i-1][(int)((ballX+ballR)/UNIT_SIZE)]==KTG){//如果是Z正方向運動時碰角了
					float sina=(ballX-((int)((ballX+ballR)/UNIT_SIZE)*UNIT_SIZE))/ballR;
					float cosa=(float)Math.sqrt(1-sina*sina);
					ballVX=-jsSDX(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;
					ballVZ=-jsSDZ(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;
					if(Math.abs(ballVX)>SD_TZZ||Math.abs(ballVZ)>SD_TZZ){
						gameSurface.father.playSound(1, 0);//播放移動聲音
						gameSurface.father.shake();
						}else{
							ballGX=0;
							ballGZ=0;
						}
					flag=true;
				}
			}	
		}
		
		if(BX>0)//如果向X軸正方向走
		{
			for(int i=(int)((ballX+ballR)/UNIT_SIZE);i<=(int)((ballX+ballR+BX)/UNIT_SIZE);i++)
			{//循環，假如它一下穿過幾個格子，那麼從第一個格子開始判斷
				if(MAP[(int)(ballZ/UNIT_SIZE)][i]==BKTG&&MAP[(int)(ballZ/UNIT_SIZE)][i-1]==KTG){//如果碰壁了			
					
					ballVX=-ballVX*VZ_TENUATION;//速度置反，並調整
					if((GameSurfaceView.ballX+ballVX*t+ballGX*t*t/2)>
					((i)*UNIT_SIZE-ballR-MAP[0].length*UNIT_SIZE/2))//如果已經緊貼牆壁了，那麼速度為零
					{
						GameSurfaceView.ballX=(i)*UNIT_SIZE-ballR-MAP[0].length*UNIT_SIZE/2;
						ballGX=0;//加速度和速度設置為零
						ballVX=0;
					}
					else
					{						
					gameSurface.father.playSound(1, 0);//播放移動聲音
					gameSurface.father.shake();
					}
					if(ballGX>0)//速度小於調整值則歸零
					{
						ballGX=0;
					}
					 flag=true;
					 
				}
				else if(BZ<=0&&((int)((ballZ-ballR)/UNIT_SIZE)>=0)&&(MAP[(int)((ballZ-ballR)/UNIT_SIZE)][i]==BKTG)
						&&(MAP[(int)((ballZ-ballR)/UNIT_SIZE)][i-1]==KTG))//球的左邊撞角
				{
					float sina=(ballZ-((int)((ballZ-ballR)/UNIT_SIZE)+1)*UNIT_SIZE)/ballR;//得到相關角度的正弦值和餘弦值
					float cosa=(float)Math.sqrt(1-sina*sina);
					ballVX=-jsSDX(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;
					ballVZ=jsSDZ(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;//得到碰角後的速度
					if(Math.abs(ballVX)>SD_TZZ||Math.abs(ballVZ)>SD_TZZ){//速度達到一定大後才播放聲音和震動
						gameSurface.father.playSound(1, 0);//播放移動聲音
						gameSurface.father.shake();//震動
						}else{
							ballGX=0;//速度歸零
							ballGZ=0;
						}
					flag=true;
				}
				else if(BZ>=0&&((int)((ballZ+ballR)/UNIT_SIZE)>=0)&&MAP[(int)((ballZ+ballR)/UNIT_SIZE)][i]==BKTG
						&&(MAP[(int)((ballZ+ballR)/UNIT_SIZE)][i-1]==KTG)){//如果右邊碰角
					float sina=-(ballZ-((int)((ballZ+ballR)/UNIT_SIZE))*UNIT_SIZE)/ballR;//得到相關值
					float cosa=(float)Math.sqrt(1-sina*sina);
					ballVX=-jsSDX(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;//得到碰角後的速度
					ballVZ=-jsSDZ(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;
					
					if(Math.abs(ballVX)>SD_TZZ||Math.abs(ballVZ)>SD_TZZ)
					{
						gameSurface.father.playSound(1, 0);//播放移動聲音
						gameSurface.father.shake();//震動
						}else{
							ballGX=0;
							ballGZ=0;//否則速度歸零
						}
					flag=true;
				}
			}		
		}
		 if(BX<0)
		{
			for(int i=(int)((ballX-ballR)/UNIT_SIZE);i>=(int)((ballX-ballR+BX)/UNIT_SIZE);i--)
			{//循環判斷是否碰壁
				if(MAP[(int)(ballZ/UNIT_SIZE)][i]==BKTG&&MAP[(int)(ballZ/UNIT_SIZE)][i+1]==KTG)
				{//如果碰壁
					
						ballVX=-ballVX*VZ_TENUATION;//速度置反並調整，
						if((GameSurfaceView.ballX+ballVX*t+ballGX*t*t/2)<
						((1+i)*UNIT_SIZE+ballR-MAP[0].length*UNIT_SIZE/2))//如果已經緊貼牆壁了，那麼速度歸零
						{
							GameSurfaceView.ballX=(1+i)*UNIT_SIZE+ballR-MAP[0].length*UNIT_SIZE/2;
							ballGX=0;//加速度和速度設置為零
							ballVX=0;
						}
						else
						{						
						gameSurface.father.playSound(1, 0);//播放移動聲音
						gameSurface.father.shake();
						}
					
					if(ballVX<0)
					{
						ballVX=-ballVX;
					}
					flag=true;
//					return true;
				}
				else if(BZ<=0&&((int)((ballZ-ballR)/UNIT_SIZE)>=0)&&(MAP[(int)((ballZ-ballR)/UNIT_SIZE)][i]==BKTG)
						&&MAP[(int)((ballZ-ballR)/UNIT_SIZE)][i+1]==KTG)//球左邊撞角
				{
					float sina=(ballZ-((int)((ballZ-ballR)/UNIT_SIZE)+1)*UNIT_SIZE)/ballR;
					float cosa=(float)Math.sqrt(1-sina*sina);//得到相關的值
					ballVX=jsSDX(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;//得到碰角後的速度
					ballVZ=jsSDZ(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;
					if(Math.abs(ballVX)>SD_TZZ||Math.abs(ballVZ)>SD_TZZ)//看是否要播放聲音
					{
						gameSurface.father.playSound(1, 0);//播放移動聲音
						gameSurface.father.shake();//震動
						}else{
							ballGX=0;
							ballGZ=0;
						}
					flag=true;
				}
				else if(BZ>=0&&((int)((ballZ+ballR)/UNIT_SIZE)>=0)&&MAP[(int)((ballZ+ballR)/UNIT_SIZE)][i]==BKTG
						&&MAP[(int)((ballZ+ballR)/UNIT_SIZE)][i+1]==KTG){//如果右邊碰角
					float sina=-(ballZ-((int)((ballZ+ballR)/UNIT_SIZE))*UNIT_SIZE)/ballR;
					float cosa=(float)Math.sqrt(1-sina*sina);//得到相關值
					ballVX=jsSDX(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;//得到碰角後的速度
					ballVZ=-jsSDZ(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;
					if(Math.abs(ballVX)>SD_TZZ||Math.abs(ballVZ)>SD_TZZ)//判斷是否要播放聲音
					{
						gameSurface.father.playSound(1, 0);//播放移動聲音
						gameSurface.father.shake();//震動
						}else{
							ballGX=0;
							ballGZ=0;
						}
					flag=true;
				}
			}
		}
		
		if(BZ<0)//向Z軸負方向上運動時
		{
			for(int i=(int)((ballZ-ballR)/UNIT_SIZE);i>=(int)((ballZ-ballR+BZ)/UNIT_SIZE);i--)
			{//循環看是否碰壁了
				if(MAP[i][(int)(ballX/UNIT_SIZE)]==BKTG&&MAP[i+1][(int)(ballX/UNIT_SIZE)]==KTG){
					ballVZ=-ballVZ*VZ_TENUATION;//將速度置反，並調整
					if((GameSurfaceView.ballZ+ballVZ*t+ballGZ*t*t/2)<=((1+i)*UNIT_SIZE+ballR-MAP.length*UNIT_SIZE/2))
					{//看調整後的速度下，還是否會穿牆
						GameSurfaceView.ballZ=(1+i)*UNIT_SIZE+ballR-MAP.length*UNIT_SIZE/2;
						ballVZ=0;
						ballGZ=0;
					}
					else
					{					
					gameSurface.father.playSound(1, 0);//播放移動聲音
					gameSurface.father.shake();//震動
					}					
					if(ballVZ<0)//如果速度還小於零，則置反
					{
						ballVZ=-ballVZ;
					}					
					flag=true;
				}
				else if(BX<=0&&((int)((ballX-ballR)/UNIT_SIZE)>=0)&&(MAP[i][(int)((ballX-ballR)/UNIT_SIZE)]==BKTG)
						&&MAP[i+1][(int)((ballX-ballR)/UNIT_SIZE)]==KTG)//左邊碰角
				{
					float sina=(ballX-((int)((ballX-ballR)/UNIT_SIZE)+1)*UNIT_SIZE)/ballR;//得到角度相關值
					float cosa=(float)Math.sqrt(1-sina*sina);					
					ballVX=jsSDX(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;//得到碰角後的速度
					ballVZ=jsSDZ(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;
					if(Math.abs(ballVX)>SD_TZZ||Math.abs(ballVZ)>SD_TZZ)//判斷是否要播放聲音
					{
						gameSurface.father.playSound(1, 0);//播放移動聲音
						gameSurface.father.shake();//震動
					}
					else
					{
							ballGX=0;
							ballGZ=0;
					}
					flag=true;
				}
				else if(BX>=0&&((int)((ballX+ballR)/UNIT_SIZE)<MAP[0].length)&&MAP[i][(int)((ballX+ballR)/UNIT_SIZE)]==BKTG
						&&MAP[i+1][(int)((ballX+ballR)/UNIT_SIZE)]==KTG){//右邊碰角
					float sina=-(ballX-((int)((ballX+ballR)/UNIT_SIZE))*UNIT_SIZE)/ballR;
					float cosa=(float)Math.sqrt(1-sina*sina);//得到相關值
					ballVX=-jsSDX(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;//得到碰角後的速度
					ballVZ=jsSDZ(ballVX,ballVZ,cosa,sina)*VZ_TENUATION;
					if(Math.abs(ballVX)>SD_TZZ||Math.abs(ballVZ)>SD_TZZ)//看是否要播放聲音
					{
						gameSurface.father.playSound(1, 0);//播放移動聲音
						gameSurface.father.shake();//震動
					}
					else
					{
							ballGX=0;
							ballGZ=0;
					}
					flag=true;
				}
				
			}	
		}
		return flag;
	}
	public void pdZJ()//判斷是否進洞方法
	{
		ballXx=(int)((MAP[0].length*UNIT_SIZE/2+ballX)/UNIT_SIZE);//此格所在的對應的數組行列
		ballZz=(int)((MAP.length*UNIT_SIZE/2+ballZ)/UNIT_SIZE);
		if(MAP_OBJECT[ballZz][ballXx]==1){//左上的格子是洞
				if((float)Math.sqrt(
				(ballX-ballXx*UNIT_SIZE+MAP[0].length*UNIT_SIZE/2)*(ballX-ballXx*UNIT_SIZE+MAP[0].length*UNIT_SIZE/2)
				+(ballZ-ballZz*UNIT_SIZE+MAP.length*UNIT_SIZE/2)*(ballZ-ballZz*UNIT_SIZE+MAP.length*UNIT_SIZE/2))
				<ballR+ballRD//則判斷球心是否在洞內
				){//掉進洞裡了
					flagSY=false;//標誌為
					ballX=ballXx*UNIT_SIZE-MAP[0].length*UNIT_SIZE/2;//將球畫到洞裡
					ballZ=ballZz*UNIT_SIZE-MAP.length*UNIT_SIZE/2;
					ballY=0;
					
		}}
		else if(MAP_OBJECT[ballZz][ballXx+1]==1)//左下的格子是洞
		{
			if((float)Math.sqrt(
					(ballX-(1+ballXx)*UNIT_SIZE+MAP[0].length*UNIT_SIZE/2)*(ballX-(1+ballXx)*UNIT_SIZE+MAP[0].length*UNIT_SIZE/2)
					+(ballZ-ballZz*UNIT_SIZE+MAP.length*UNIT_SIZE/2)*(ballZ-ballZz*UNIT_SIZE+MAP.length*UNIT_SIZE/2))
					<ballR+ballRD//則判斷球心是否在洞內
					){
				flagSY=false;//掉進洞裡了
				ballX=(1+ballXx)*UNIT_SIZE-MAP[0].length*UNIT_SIZE/2;
				ballZ=ballZz*UNIT_SIZE-MAP.length*UNIT_SIZE/2;
				ballY=0;
				ballXx=ballXx+1;
			}
		}
		else if(MAP_OBJECT[ballZz+1][ballXx+1]==1){//右下的格子是洞
			if((float)Math.sqrt(
					(ballX-(1+ballXx)*UNIT_SIZE+MAP[0].length*UNIT_SIZE/2)*(ballX-(1+ballXx)*UNIT_SIZE+MAP[0].length*UNIT_SIZE/2)
					+(ballZ-(1+ballZz)*UNIT_SIZE+MAP.length*UNIT_SIZE/2)*(ballZ-(1+ballZz)*UNIT_SIZE+MAP.length*UNIT_SIZE/2))
					<ballR+ballRD//則判斷球心是否在洞內
					){
				flagSY=false;//掉進洞裡了
				ballX=(1+ballXx)*UNIT_SIZE-MAP[0].length*UNIT_SIZE/2;
				ballZ=(ballZz+1)*UNIT_SIZE-MAP.length*UNIT_SIZE/2;
				ballY=0;
				ballXx=ballXx+1;
				ballZz=ballZz+1;
			}
		}
		else if(MAP_OBJECT[ballZz+1][ballXx]==1){//右上的格子是洞
			if((float)Math.sqrt(
					(ballX-ballXx*UNIT_SIZE+MAP[0].length*UNIT_SIZE/2)*(ballX-ballXx*UNIT_SIZE+MAP[0].length*UNIT_SIZE/2)
					+(ballZ-(1+ballZz)*UNIT_SIZE+MAP.length*UNIT_SIZE/2)*(ballZ-(1+ballZz)*UNIT_SIZE+MAP.length*UNIT_SIZE/2))
					<ballR+ballRD//則判斷球心是否在洞內
					){
						flagSY=false;//掉進洞裡了
						ballX=ballXx*UNIT_SIZE-MAP[0].length*UNIT_SIZE/2;
						ballZ=(ballZz+1)*UNIT_SIZE-MAP.length*UNIT_SIZE/2;
						ballY=0;
						ballZz=ballZz+1;
			}
		}
		
	}
	public float jsSDX(float vx,float vz,float cosa,float sina)//遇到角時計算X方向的速度，此兩個方法為從軸負方向前進時
	{
	float vvx;
	vvx=-2*vz*sina*cosa+vx*cosa*cosa-vx*sina*sina;//計算此時X軸方向的速度
		return Math.abs(vvx);//返回值
	}
	public float jsSDZ(float vx,float vz,float cosa,float sina)//遇到角時計算Z方向的速度。此兩個方法為從軸負方向前進時
	{
	float vvz;	
	vvz=vz*sina*sina-vz*cosa*cosa+2*vx*cosa*sina;//計算此時的Z方向上的速度
		return Math.abs(vvz);
	}
}

