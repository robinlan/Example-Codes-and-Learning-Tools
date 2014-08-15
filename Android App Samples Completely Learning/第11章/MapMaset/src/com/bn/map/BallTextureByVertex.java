package com.bn.map;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import static com.bn.map.Constant.*;

import javax.microedition.khronos.opengles.GL10;

public class BallTextureByVertex {   
	private FloatBuffer   mVertexBuffer;//頂點坐標數據緩衝
	private FloatBuffer   mNormalBuffer;//頂點法向量數據緩衝
    private FloatBuffer mTextureBuffer;//頂點紋理數據緩衝
    public float mAngleX;//沿x軸旋轉角度
    public float mAngleY;//沿y軸旋轉角度 
    public float mAngleZ;//沿z軸旋轉角度 
    int vCount=0;//頂點數量
    public BallTextureByVertex(float scale,float angleSpan) 
    {    	    	
    	//獲取切分整圖的紋理數組
    	float[] texCoorArray= 
         generateTexCoor
    	 (
    			 (int)(360/angleSpan), //紋理圖切分的列數
    			 (int)(180/angleSpan)  //紋理圖切分的行數
    	);
        int tc=0;//紋理數組計數器
        int ts=texCoorArray.length;//紋理數組長度
    	
    	ArrayList<Float> alVertix=new ArrayList<Float>();//存放頂點坐標的ArrayList
    	ArrayList<Float> alTexture=new ArrayList<Float>();//存放紋理坐標的ArrayList
    	
        for(float vAngle=90;vAngle>-90;vAngle=vAngle-angleSpan)//垂直方向angleSpan度一份
        {
        	for(float hAngle=360;hAngle>0;hAngle=hAngle-angleSpan)//水平方向angleSpan度一份
        	{
        		//縱向橫向各到一個角度後計算對應的此點在球面上的四邊形頂點坐標
        		//並構建兩個組成四邊形的三角形
        		double xozLength=scale*UNIT_SIZE*Math.cos(Math.toRadians(vAngle));
        		float x1=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
        		float z1=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
        		float y1=(float)(scale*UNIT_SIZE*Math.sin(Math.toRadians(vAngle)));
        		
        		xozLength=scale*UNIT_SIZE*Math.cos(Math.toRadians(vAngle-angleSpan));
        		float x2=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
        		float z2=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
        		float y2=(float)(scale*UNIT_SIZE*Math.sin(Math.toRadians(vAngle-angleSpan)));
        		
        		xozLength=scale*UNIT_SIZE*Math.cos(Math.toRadians(vAngle-angleSpan));
        		float x3=(float)(xozLength*Math.cos(Math.toRadians(hAngle-angleSpan)));
        		float z3=(float)(xozLength*Math.sin(Math.toRadians(hAngle-angleSpan)));
        		float y3=(float)(scale*UNIT_SIZE*Math.sin(Math.toRadians(vAngle-angleSpan)));
        		
        		xozLength=scale*UNIT_SIZE*Math.cos(Math.toRadians(vAngle));
        		float x4=(float)(xozLength*Math.cos(Math.toRadians(hAngle-angleSpan)));
        		float z4=(float)(xozLength*Math.sin(Math.toRadians(hAngle-angleSpan)));
        		float y4=(float)(scale*UNIT_SIZE*Math.sin(Math.toRadians(vAngle)));   
        		
        		//構建第一三角形
        		alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);        		
        		//構建第二三角形
        		alVertix.add(x4);alVertix.add(y4);alVertix.add(z4);
        		alVertix.add(x2);alVertix.add(y2);alVertix.add(z2);
        		alVertix.add(x3);alVertix.add(y3);alVertix.add(z3); 
        		
        		//第一三角形3個頂點的6個紋理坐標
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);        		
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		//第二三角形3個頂點的6個紋理坐標
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);        		
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);
        		alTexture.add(texCoorArray[tc++%ts]);       		
        	}
        } 	
        
        
        
        vCount=alVertix.size()/3;//頂點的數量為坐標值數量的1/3，因為一個頂點有3個坐標
    	
        //將alVertix中的坐標值轉存到一個int數組中
        float vertices[]=new float[vCount*3];
    	for(int i=0;i<alVertix.size();i++)
    	{
    		vertices[i]=alVertix.get(i);
    	}
        
        //創建繪製頂點數據緩衝
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設置字節順序
        mVertexBuffer = vbb.asFloatBuffer();//轉換為int型緩衝
        mVertexBuffer.put(vertices);//向緩衝區中放入頂點坐標數據
        mVertexBuffer.position(0);//設置緩衝區起始位置     
        
        //創建頂點法向量數據緩衝
        ByteBuffer nbb = ByteBuffer.allocateDirect(vertices.length*4);
        nbb.order(ByteOrder.nativeOrder());//設置字節順序
        mNormalBuffer = vbb.asFloatBuffer();//轉換為int型緩衝
        mNormalBuffer.put(vertices);//向緩衝區中放入頂點坐標數據
        mNormalBuffer.position(0);//設置緩衝區起始位置
        
        //創建紋理坐標緩衝
        float textureCoors[]=new float[alTexture.size()];//頂點紋理值數組
        for(int i=0;i<alTexture.size();i++) 
        {
        	textureCoors[i]=alTexture.get(i);
        }
        
        ByteBuffer tbb = ByteBuffer.allocateDirect(textureCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());//設置字節順序
        mTextureBuffer = tbb.asFloatBuffer();//轉換為int型緩衝
        mTextureBuffer.put(textureCoors);//向緩衝區中放入頂點著色數據
        mTextureBuffer.position(0);//設置緩衝區起始位置
    }

    public void drawSelf(GL10 gl,int texId)
    {
    	gl.glRotatef(mAngleZ, 0, 0, 1);//沿Z軸旋轉
    	gl.glRotatef(mAngleX, 1, 0, 0);//沿X軸旋轉
        gl.glRotatef(mAngleY, 0, 1, 0);//沿Y軸旋轉
        
        //允許使用頂點數組
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		//為畫筆指定頂點坐標數據
        gl.glVertexPointer
        (
        		3,				//每個頂點的坐標數量為3  xyz 
        		GL10.GL_FLOAT,	//頂點坐標值的類型為 GL_FIXED
        		0, 				//連續頂點坐標數據之間的間隔
        		mVertexBuffer	//頂點坐標數據
        );
        
       
        //為畫筆指定頂點法向量數據
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer); 
		
        //開啟紋理
//        gl.glEnable(GL10.GL_TEXTURE_2D);   
//        //允許使用紋理ST坐標緩衝
//        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        //為畫筆指定紋理ST坐標緩衝
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
        //綁定當前紋理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
        
        //繪製圖形
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		//以三角形方式填充
        		0, 			 			//開始點編號
        		vCount					//頂點數量
        );
    }
    
    //自動切分紋理產生紋理數組的方法
    public float[] generateTexCoor(int bw,int bh)//傳入切分的列數  ， 行數
    {
    	float[] result=new float[bw*bh*6*2]; 
    	float sizew=1.0f/bw;//列寬
    	float sizeh=1.0f/bh;//行寬
    	int c=0;
    	for(int i=0;i<bh;i++)
    	{
    		for(int j=0;j<bw;j++)
    		{
    			//每行列一個矩形，由兩個三角形構成，共六個點，12個紋理坐標
    			float s=j*sizew;
    			float t=i*sizeh;
    			
    			result[c++]=s;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t+sizeh;    			
    		}
    	}
    	return result;
    }
    
}

