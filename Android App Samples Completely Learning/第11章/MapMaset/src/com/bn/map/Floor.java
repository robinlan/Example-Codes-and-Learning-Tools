package com.bn.map;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import static com.bn.map.Constant.*;
//表示地板的類
public class Floor {
	private FloatBuffer   mVertexBuffer;//頂點坐標數據緩衝
    private FloatBuffer mTextureBuffer;//頂點紋理數據緩衝
    private FloatBuffer mNormalBuffer;
    int vCount=0;//頂點數量
    int width;//地板橫向width個單位
    int height;//地板縱向height個單位
    public Floor(int width,int height)
    {
    	this.width=width;
    	this.height=height;
    	//頂點坐標數據的初始化================begin============================
        vCount=6;//每個地板塊6個頂點
    	float []vertices=new float[]
    	{
    			-width*UNIT_SIZE/2,0,-height*UNIT_SIZE/2,
    			-width*UNIT_SIZE/2,0,height*UNIT_SIZE/2,
    			width*UNIT_SIZE/2,0,height*UNIT_SIZE/2,
    		
    			width*UNIT_SIZE/2,0,height*UNIT_SIZE/2,
    			width*UNIT_SIZE/2,0,-height*UNIT_SIZE/2,
    			-width*UNIT_SIZE/2,0,-height*UNIT_SIZE/2
    	};    	
    	ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//設置字節順序
        mVertexBuffer = vbb.asFloatBuffer();//轉換為int型緩衝
        mVertexBuffer.put(vertices);//向緩衝區中放入頂點坐標數據
        mVertexBuffer.position(0);//設置緩衝區起始位置       
        float textures[]=new float[]
        {
        		0,0,
        		0,2,
        		2,2,
        		
        		2,2,
        		2,0,
        		0,0
        };
        
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());//設置字節順序
        mTextureBuffer= tbb.asFloatBuffer();//轉換為Float型緩衝
        mTextureBuffer.put(textures);//向緩衝區中放入頂點著色數據
        mTextureBuffer.position(0);//設置緩衝區起始位置
        //特別提示：由於不同平台字節順序不同數據單元不是字節的一定要經過ByteBuffer
        //轉換，關鍵是要通過ByteOrder設置nativeOrder()，否則有可能會出問題
        //頂點坐標數據的初始化================end============================
        
//        頂點法向量數據的初始化================begin============================
        float normals[]=new float[vCount*3];
        for(int i=0;i<vCount;i++)
        {
        	normals[i*3]=0;
        	normals[i*3+1]=1;
        	normals[i*3+2]=0;
        }

        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());//設置字節順序
        mNormalBuffer = nbb.asFloatBuffer();//轉換為int型緩衝
        mNormalBuffer.put(normals);//向緩衝區中放入頂點著色數據
        mNormalBuffer.position(0);//設置緩衝區起始位置
        //特別提示：由於不同平台字節順序不同數據單元不是字節的一定要經過ByteBuffer
        //轉換，關鍵是要通過ByteOrder設置nativeOrder()，否則有可能會出問題
        //頂點著色數據的初始化================end============================
        
        
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
        
        //為畫筆指定紋理ST坐標緩衝
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTextureBuffer);
        //綁定當前紋理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, texId);
		
        gl.glNormalPointer(GL10.GL_FLOAT, 0, mNormalBuffer);
        //繪製圖形
        gl.glDrawArrays
        (
        		GL10.GL_TRIANGLES, 		//以三角形方式填充
        		0, 			 			//開始點編號
        		vCount					//頂點的數量
        );
    }
}

