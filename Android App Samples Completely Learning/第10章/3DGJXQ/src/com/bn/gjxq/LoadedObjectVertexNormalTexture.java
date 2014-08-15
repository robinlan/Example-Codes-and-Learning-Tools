package com.bn.gjxq;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
//加載後的物體——攜帶頂點、法向量、紋理信息
public class LoadedObjectVertexNormalTexture 
{
	private FloatBuffer   mVertexBuffer;//頂點坐標數據緩衝
	private FloatBuffer   mNormalBuffer;//頂點法向量數據緩衝
	private FloatBuffer   mTexBuffer;//頂點紋理數據緩衝
    int vCount=0;  
    public LoadedObjectVertexNormalTexture(float[] vertices,float[] normals,float texCoors[]) 
    {	
    	//頂點坐標數據的初始化================begin============================
        vCount=vertices.length/3;    
        //創建頂點坐標數據緩衝
        //vertices.length*4是因為一個整數四個字節
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設置字節順序
        mVertexBuffer = vbb.asFloatBuffer();//轉換為Float型緩衝
        mVertexBuffer.put(vertices);//向緩衝區中放入頂點坐標數據
        mVertexBuffer.position(0);//設置緩衝區起始位置
        //特別提示：由於不同平台字節順序不同數據單元不是字節的一定要經過ByteBuffer
        //轉換，關鍵是要通過ByteOrder設置nativeOrder()，否則有可能會出問題
        //頂點坐標數據的初始化================end============================
        
        //法向量信息初始化
        ByteBuffer vbn = ByteBuffer.allocateDirect(normals.length*4);
        vbn.order(ByteOrder.nativeOrder());//設置字節順序
        mNormalBuffer = vbn.asFloatBuffer();//轉換為Float型緩衝
        mNormalBuffer.put(normals);//向緩衝區中放入頂點坐標數據
        mNormalBuffer.position(0);//設置緩衝區起始位置 
        
        //紋理坐標緩衝初始化
        ByteBuffer vbt = ByteBuffer.allocateDirect(texCoors.length*4);
        vbt.order(ByteOrder.nativeOrder());//設置字節順序
        mTexBuffer = vbt.asFloatBuffer();//轉換為Float型緩衝
        mTexBuffer.put(texCoors);//向緩衝區中放入頂點坐標數據
        mTexBuffer.position(0);//設置緩衝區起始位置 
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
        
        //為畫筆指定頂點法向量數據
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
        
        //為畫筆指定紋理ST坐標緩衝
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
        //綁定當前紋理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
		
        //繪製圖形
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		//以三角形方式填充
        		0, 			 			//開始點編號
        		vCount					//頂點的數量
        );        
        
       
    }
}

