package com.bn.map;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

// 表示單個紋理矩形的類
public class TextureRect
{
	private FloatBuffer   mVertexBuffer;//頂點坐標數據緩衝
    private FloatBuffer   mTextureBuffer;//頂點著色數據緩衝
    int vCount;    
    public TextureRect(float X_UNIT_SIZE,float Y_UNIT_SIZE,float[] textures)
    {
    	//頂點坐標數據的初始化================begin============================
        vCount=6;
        float vertices[]=new float[]
        {
        	-1*X_UNIT_SIZE,1*Y_UNIT_SIZE,0,
        	-1*X_UNIT_SIZE,-1*Y_UNIT_SIZE,0,
        	1*X_UNIT_SIZE,1*Y_UNIT_SIZE,0,
        	
        	-1*X_UNIT_SIZE,-1*Y_UNIT_SIZE,0,
        	1*X_UNIT_SIZE,-1*Y_UNIT_SIZE,0,
        	1*X_UNIT_SIZE,1*Y_UNIT_SIZE,0
        };
		
        //創建頂點坐標數據緩衝
        //vertices.length*4是因為一個整數四個字節
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設置字節順序
        mVertexBuffer = vbb.asFloatBuffer();//轉換為int型緩衝
        mVertexBuffer.put(vertices);//向緩衝區中放入頂點坐標數據
        mVertexBuffer.position(0);//設置緩衝區起始位置
        //特別提示：由於不同平台字節順序不同數據單元不是字節的一定要經過ByteBuffer
        //轉換，關鍵是要通過ByteOrder設置nativeOrder()，否則有可能會出問題
        //頂點坐標數據的初始化================end============================
        
        //頂點紋理數據的初始化================begin============================
        

        
        //創建頂點紋理數據緩衝
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());//設置字節順序
        mTextureBuffer= tbb.asFloatBuffer();//轉換為Float型緩衝
        mTextureBuffer.put(textures);//向緩衝區中放入頂點著色數據
        mTextureBuffer.position(0);//設置緩衝區起始位置
        //特別提示：由於不同平台字節順序不同數據單元不是字節的一定要經過ByteBuffer
        //轉換，關鍵是要通過ByteOrder設置nativeOrder()，否則有可能會出問題
        //頂點紋理數據的初始化================end============================
    }

    public void drawSelf(GL10 gl,int texId)
    {        
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);//啟用頂點坐標數組
        
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
    }
}

