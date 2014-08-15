package com.bn.gjxq;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import static com.bn.gjxq.Constant.*;
//這個為繪製棋盤中的每一個顏色矩形
public class ColorRect 
{
	private FloatBuffer mVertexBuffer;//聲明頂點數據緩衝
	private FloatBuffer mColorBuffer;//聲明頂點顏色數據緩衝
	int vCount;	//聲明頂點的數量	
	public ColorRect(float[] colorArr)
	{
		 vCount=6;//設置頂點總數為6
		 //頂點數據
		 float vertices[]=new float[]//用於存儲頂點的數組
		 {
				 0,0,0,
				 0,0,UNIT_SIZE,
				 UNIT_SIZE,0,0,         //第一個逆時針捲繞的三角形
				 
				 UNIT_SIZE,0,0,
				 0,0,UNIT_SIZE,
				 UNIT_SIZE,0,UNIT_SIZE   //第二個逆時針捲繞的三角形
		 };
		 	ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);//字節緩衝
	        vbb.order(ByteOrder.nativeOrder());//設置字節順序
	        mVertexBuffer = vbb.asFloatBuffer();//轉換為float型緩衝
	        mVertexBuffer.put(vertices);//向緩衝區中放入頂點坐標數據
	        mVertexBuffer.position(0);//設置緩衝區起始位置
	        
	        //頂點顏色 數組
	        float colors[]=new float[vCount*4];
	        int c=0;//聲明一個標誌
	        for(int i=0;i<vCount;i++)
	        {
	        	colors[c++]=colorArr[0];//顏色份量 R
	        	colors[c++]=colorArr[1];//顏色份量 G
	        	colors[c++]=colorArr[2];//顏色份量B
	        	colors[c++]=colorArr[3];//顏色份量A
	        }
	        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
	        cbb.order(ByteOrder.nativeOrder());//設置字節順序
	        mColorBuffer= cbb.asFloatBuffer();//轉換為Float型緩衝
	        mColorBuffer.put(colors);//向緩衝區中放入頂點著色數據
	        mColorBuffer.position(0);//設置緩衝區起始位置	        
	}
	public void drawSelf(GL10 gl)
	{
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);//加載頂點數組指針
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);//加載顏色數組指針
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vCount);//以頂點三角形方式進行繪製
	}
}

