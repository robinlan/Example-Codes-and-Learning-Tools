package com.bn.gjxq;

import static com.bn.gjxq.Constant.UNIT_SIZE;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class RectWall
{
	private FloatBuffer   mVertexBuffer;//頂點坐標數據緩衝
    private FloatBuffer   mTextureBuffer;//頂點紋理數據緩衝
    int vCount;//頂點數
    float x;//向幾軸推動到的地方
	float y;
	float z;
	public RectWall(float width,float height)
	{
		vCount=6;   	
    	float []vertices=new float[]
    	{
    			-width*UNIT_SIZE/2,height*UNIT_SIZE/2,0,
    			-width*UNIT_SIZE/2,-height*UNIT_SIZE/2,0,
    			width*UNIT_SIZE/2,-height*UNIT_SIZE/2,0,
    		
    			width*UNIT_SIZE/2,-height*UNIT_SIZE/2,0,
    			width*UNIT_SIZE/2,height*UNIT_SIZE/2,0,
    			-width*UNIT_SIZE/2,height*UNIT_SIZE/2,0
    	};    	
    	ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設置字節順序
        mVertexBuffer = vbb.asFloatBuffer();//轉換為int型緩衝
        mVertexBuffer.put(vertices);//向緩衝區中放入頂點坐標數據
        mVertexBuffer.position(0);//設置緩衝區起始位置       
        float textures[]=new float[]
        {
        		0,0,
        		0,1f,
        		1f,1f,
        		
        		1f,1f,
        		1,0,
        		0,0
        };
        
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());//設置字節順序
        mTextureBuffer= tbb.asFloatBuffer();//轉換為Float型緩衝
        mTextureBuffer.put(textures);//向緩衝區中放入頂點著色數據
        mTextureBuffer.position(0);//設置緩衝區起始位置
	}
	
	 public void drawSelf(GL10 gl,int texId)
	    {
	    	
	        gl.glPushMatrix();
	        gl.glTranslatef(x, y, z);
	      //為畫筆指定頂點坐標數據
	        gl.glVertexPointer
	        (
	        		3,				//每個頂點的坐標數量為3  xyz 
	        		GL10.GL_FLOAT,	//頂點坐標值的類型為 GL_FIXED
	        		0, 				//連續頂點坐標數據之間的間隔
	        		mVertexBuffer	//頂點坐標數據
	        );

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
	        gl.glPopMatrix();
	    }
}

