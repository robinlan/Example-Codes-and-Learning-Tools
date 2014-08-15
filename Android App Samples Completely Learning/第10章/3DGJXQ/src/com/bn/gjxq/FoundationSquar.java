package com.bn.gjxq;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import static com.bn.gjxq.Constant.UNIT_SIZE;
public class FoundationSquar 
{
	private FloatBuffer   mVertexBuffer;//頂點坐標數據緩衝
    private FloatBuffer   mTextureBuffer;//頂點紋理數據緩衝 
    int vCount;//記錄定點數
    public FoundationSquar(float whith_1,float whith_2,float height)
    {
    	vCount=6;
    	float []vertices=new float[]
    	 {
    		-whith_1/2*UNIT_SIZE,0,0,
    		-whith_2/2*UNIT_SIZE,-height*UNIT_SIZE,(whith_2/2-whith_1/2)*UNIT_SIZE,
    		whith_2/2*UNIT_SIZE,-height*UNIT_SIZE,(whith_2/2-whith_1/2)*UNIT_SIZE,
    		
    		whith_2/2*UNIT_SIZE,-height*UNIT_SIZE,(whith_2/2-whith_1/2)*UNIT_SIZE,
    		whith_1/2*UNIT_SIZE,0,0,
    		-whith_1/2*UNIT_SIZE,0,0,
    	};
    	
    	ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設置字節順序
        mVertexBuffer = vbb.asFloatBuffer();//轉換為int型緩衝
        mVertexBuffer.put(vertices);//向緩衝區中放入頂點坐標數據
        mVertexBuffer.position(0);//設置緩衝區起始位置
        
        float textures[]=new float[]
        {
        		1.5f/11,0,
        		0,1f,
        		1f,1f,
        		
        		1f,1f,
        		9.5f/11,0,
        		1.5f/11
        };
        
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());//設置字節順序
        mTextureBuffer= tbb.asFloatBuffer();//轉換為Float型緩衝
        mTextureBuffer.put(textures);//向緩衝區中放入頂點著色數據
        mTextureBuffer.position(0);//設置緩衝區起始位置
    }
    
    public void drawSelf(GL10 gl,int texId)
    {
    	//為畫筆指定頂點坐標數據
        gl.glVertexPointer
        (
        		3,				//每個頂點的坐標數量為3  xyz 
        		GL10.GL_FLOAT,	//頂點坐標值的類型為 GL_FIXED
        		0, 				//連續頂點坐標數據之間的間隔
        		mVertexBuffer	//頂點坐標數據
        );
        
      //開啟紋理
        gl.glEnable(GL10.GL_TEXTURE_2D);   
        //允許使用紋理ST坐標緩衝
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        //為畫筆指定紋理ST坐標緩衝
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
        //綁定當前紋理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
		
        //繪製圖形
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		//以三角形方式填充
        		0,
        		vCount 
        );
    }
}

