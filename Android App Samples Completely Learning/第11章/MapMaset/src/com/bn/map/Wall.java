package com.bn.map;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;
import static com.bn.map.GameSurfaceView.*;
import static com.bn.map.Constant.*;

//表示牆的類
public class Wall {
	private FloatBuffer   mVertexBuffer;//頂點坐標數據緩衝
    private FloatBuffer   mTextureBuffer;//頂點紋理數據緩衝
    private FloatBuffer   mNormalBuffer;//頂點法向量數據緩衝
    int vCount;//頂點數量
    private int[][] indexFlag;//用於記錄當前點是否掃瞄過   1 表示此點不需要在掃瞄   0   表示此點需要掃瞄
    
    public Wall()
    {
    	//頂點坐標數據的初始化================begin============================
        int rows=MAP.length;
        int cols=MAP[0].length;
        indexFlag=new int[rows][cols];
        
        ArrayList<Float> alVertex=new ArrayList<Float>();
        ArrayList<Float> alNormal=new ArrayList<Float>();
        ArrayList<Float> alTexture=new ArrayList<Float>();
        
        for(int i=0;i<rows;i++)//行掃瞄
        {
        	for(int j=0;j<cols;j++)//列掃瞄
        	{//對地圖中的每一塊進行處理
        		if(MAP[i][j]==1)//當前點為牆
        		{
        			int [][] area=returnMaxBlock(i,j);// area[0]表示起始點    行 列  area[1]表示寬度和高度
        			for(int k=area[0][0];k<area[0][0]+area[1][1];k++)//對區域內的每個 點    建造圍牆
        			{
        				for(int t=area[0][1];t<area[0][1]+area[1][0];t++)
        				{
        					//建造頂層牆
        					float xx1=t*UNIT_SIZE;       //    1
                			float y=FLOOR_Y+WALL_HEIGHT;
                			float zz1=k*UNIT_SIZE;
                			
                			float xx2=t*UNIT_SIZE;       //     2
                			float zz2=(k+1)*UNIT_SIZE;
                			
                			float xx3=(t+1)* UNIT_SIZE;    //   3
                			float zz3=(k+1)*UNIT_SIZE;
                			
                			float xx4=(t+1)*UNIT_SIZE;       //    4
                			float zz4=k*UNIT_SIZE;
                			//構造三角形
                			alVertex.add(xx1);alVertex.add(y);alVertex.add(zz1);
            				alVertex.add(xx2);alVertex.add(y);alVertex.add(zz2);
            				alVertex.add(xx3);alVertex.add(y);alVertex.add(zz3);

            				alVertex.add(xx3);alVertex.add(y);alVertex.add(zz3);
            				alVertex.add(xx4);alVertex.add(y);alVertex.add(zz4);
            				alVertex.add(xx1);alVertex.add(y);alVertex.add(zz1);
            				
            				//添加紋理   整塊平鋪
            				alTexture.add((float)((float)t/cols));alTexture.add((float)k/rows);
            				alTexture.add((float)((float)t/cols));alTexture.add((float)((float)(k+1)/rows));        				
            				alTexture.add((float)((float)(t+1)/cols));alTexture.add((float)((float)(k+1)/rows));
            				
            				alTexture.add((float)((float)(t+1)/cols));alTexture.add((float)((float)(k+1)/rows));
            				alTexture.add((float)((float)(t+1)/cols));alTexture.add((float)k/rows);
            				alTexture.add((float)((float)t/cols));alTexture.add((float)k/rows);
            				
            			

            				//建造向量
            				alNormal.add(0f);alNormal.add(1f);alNormal.add(0f);
            				alNormal.add(0f);alNormal.add(1f);alNormal.add(0f);
            				alNormal.add(0f);alNormal.add(1f);alNormal.add(0f);
            				
            				alNormal.add(0f);alNormal.add(1f);alNormal.add(0f);
            				alNormal.add(0f);alNormal.add(1f);alNormal.add(0f);
            				alNormal.add(0f);alNormal.add(1f);alNormal.add(0f);

            				//建造牆的上面
            				if(k==0||MAP[k-1][t]==0)
            				{
            					float x1=t*UNIT_SIZE;
                				float y1=FLOOR_Y;
                				float z1=k*UNIT_SIZE;   //  1
                				
                				float x2=t*UNIT_SIZE;
                				float y2=FLOOR_Y+WALL_HEIGHT;
                				float z2=k*UNIT_SIZE;    // 2
                				
                				float x3=(t+1)*UNIT_SIZE;
                				float y3=FLOOR_Y+WALL_HEIGHT;
                				float z3=k*UNIT_SIZE;    //  3
                				
                				float x4=(t+1)*UNIT_SIZE;
                				float y4=FLOOR_Y;
                				float z4=k*UNIT_SIZE;    //  4
                				//建造三角形
                				alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
                				alVertex.add(x2);alVertex.add(y2);alVertex.add(z2);
                				alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
                				
                				alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
                				alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
                				alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
                				//建造紋理
                				alTexture.add((float)((float)(t-area[0][1])/cols));alTexture.add(0f);
                				alTexture.add((float)((float)(t-area[0][1])/cols));alTexture.add((float)((float)1/rows));        				
                				alTexture.add((float)((float)(t+1-area[0][1])/cols));alTexture.add((float)((float)1/rows));
                				
                				alTexture.add((float)((float)(t+1-area[0][1])/cols));alTexture.add((float)((float)1/rows));
                				alTexture.add((float)((float)(t+1-area[0][1])/cols));alTexture.add(0f);
                				alTexture.add((float)((float)(t-area[0][1])/cols));alTexture.add((float)1/rows);
                                //建造向量
                				alNormal.add(0f);alNormal.add(0f);alNormal.add(-1f);
                				alNormal.add(0f);alNormal.add(0f);alNormal.add(-1f);
                				alNormal.add(0f);alNormal.add(0f);alNormal.add(-1f);
                				
                				alNormal.add(0f);alNormal.add(0f);alNormal.add(-1f);
                				alNormal.add(0f);alNormal.add(0f);alNormal.add(-1f);
                				alNormal.add(0f);alNormal.add(0f);alNormal.add(-1f);
            				}
            				//建造牆的下面
            				if(k==rows-1||MAP[k+1][t]==0)
            				{
            					float x2=t*UNIT_SIZE;
                				float y2=FLOOR_Y;
                				float z2=(k+1)*UNIT_SIZE;
                				
                				float x1=t*UNIT_SIZE;
                				float y1=FLOOR_Y+WALL_HEIGHT;
                				float z1=(k+1)*UNIT_SIZE;
                				
                				float x4=(t+1)*UNIT_SIZE;
                				float y4=FLOOR_Y+WALL_HEIGHT;
                				float z4=(k+1)*UNIT_SIZE;
                				
                				float x3=(t+1)*UNIT_SIZE;
                				float y3=FLOOR_Y;
                				float z3=(k+1)*UNIT_SIZE;
                				//建造三角形
                				alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
                				alVertex.add(x2);alVertex.add(y2);alVertex.add(z2);
                				alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
                				
                				alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
                				alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
                				alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
                				//建造紋理
                				alTexture.add((float)((float)(t-area[0][1])/cols));alTexture.add(0f);
                				alTexture.add((float)((float)(t-area[0][1])/cols));alTexture.add((float)((float)1/rows));        				
                				alTexture.add((float)((float)(t+1-area[0][1])/cols));alTexture.add((float)((float)1/rows));
                				
                				alTexture.add((float)((float)(t+1-area[0][1])/cols));alTexture.add((float)((float)1/rows));
                				alTexture.add((float)((float)(t+1-area[0][1])/cols));alTexture.add(0f);
                				alTexture.add((float)((float)(t-area[0][1])/cols));alTexture.add((float)1/rows);
                				//建造向量
                				alNormal.add(0f);alNormal.add(0f);alNormal.add(1f);
                				alNormal.add(0f);alNormal.add(0f);alNormal.add(1f);
                				alNormal.add(0f);alNormal.add(0f);alNormal.add(1f);
                				
                				alNormal.add(0f);alNormal.add(0f);alNormal.add(1f);
                				alNormal.add(0f);alNormal.add(0f);alNormal.add(1f);
                				alNormal.add(0f);alNormal.add(0f);alNormal.add(1f);
            				}
            				//建造牆的左面
            				if(t==0||MAP[k][t-1]==0)
            				{
            					float x2=t*UNIT_SIZE;
                				float y2=FLOOR_Y;
                				float z2=(k+1)*UNIT_SIZE;
                				
                				float x3=t*UNIT_SIZE;
                				float y3=FLOOR_Y+WALL_HEIGHT;
                				float z3=(k+1)*UNIT_SIZE;
                				
                				float x4=t*UNIT_SIZE;
                				float y4=FLOOR_Y+WALL_HEIGHT;
                				float z4=k*UNIT_SIZE;
                				
                				float x1=t*UNIT_SIZE;
                				float y1=FLOOR_Y;
                				float z1=k*UNIT_SIZE;
                				//建造三角形
                				alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
                				alVertex.add(x2);alVertex.add(y2);alVertex.add(z2);
                				alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
                				
                				alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
                				alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
                				alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
                				//建造紋理
                				alTexture.add(0f);alTexture.add((float)(k-area[0][0])/rows);
                				alTexture.add(0f);alTexture.add((float)((float)(k+1-area[0][0])/rows));        				
                				alTexture.add((float)((float)1/cols));alTexture.add((float)((float)(k+1-area[0][0])/rows));
                				
                				alTexture.add((float)((float)1/cols));alTexture.add((float)((float)(k+1-area[0][0])/rows));
                				alTexture.add((float)((float)1/cols));alTexture.add((float)(k-area[0][0])/rows);
                				alTexture.add(0f);alTexture.add((float)(k-area[0][0])/rows);
                				//建造向量
                				alNormal.add(-1f);alNormal.add(0f);alNormal.add(0f);
                				alNormal.add(-1f);alNormal.add(0f);alNormal.add(0f);
                				alNormal.add(-1f);alNormal.add(0f);alNormal.add(0f);
                				
                				alNormal.add(-1f);alNormal.add(0f);alNormal.add(0f);
                				alNormal.add(-1f);alNormal.add(0f);alNormal.add(0f);
                				alNormal.add(-1f);alNormal.add(0f);alNormal.add(0f);
            				}
            				//建造牆的右面
            				if(t==cols-1||MAP[k][t+1]==0)
            				{
            					float x3=(t+1)*UNIT_SIZE;
                				float y3=FLOOR_Y;
                				float z3=(k+1)*UNIT_SIZE;
                				
                				float x2=(t+1)*UNIT_SIZE;
                				float y2=FLOOR_Y+WALL_HEIGHT;
                				float z2=(k+1)*UNIT_SIZE;
                				
                				float x1=(t+1)*UNIT_SIZE;
                				float y1=FLOOR_Y+WALL_HEIGHT;
                				float z1=k*UNIT_SIZE;
                				
                				float x4=(t+1)*UNIT_SIZE;
                				float y4=FLOOR_Y;
                				float z4=k*UNIT_SIZE;
                				//建造三角形
                				alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
                				alVertex.add(x2);alVertex.add(y2);alVertex.add(z2);
                				alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
                				
                				alVertex.add(x3);alVertex.add(y3);alVertex.add(z3);
                				alVertex.add(x4);alVertex.add(y4);alVertex.add(z4);
                				alVertex.add(x1);alVertex.add(y1);alVertex.add(z1);
                				//建造紋理
                				alTexture.add(0f);alTexture.add((float)(k-area[0][0])/rows);
                				alTexture.add(0f);alTexture.add((float)((float)(k+1-area[0][0])/rows));        				
                				alTexture.add((float)((float)1/cols));alTexture.add((float)((float)(k+1-area[0][0])/rows));
                				
                				alTexture.add((float)((float)1/cols));alTexture.add((float)((float)(k+1-area[0][0])/rows));
                				alTexture.add((float)((float)1/cols));alTexture.add((float)(k-area[0][0])/rows);
                				alTexture.add(0f);alTexture.add((float)(k-area[0][0])/rows);
                				//建造向量
                				alNormal.add(1f);alNormal.add(0f);alNormal.add(0f);
                				alNormal.add(1f);alNormal.add(0f);alNormal.add(0f);
                				alNormal.add(1f);alNormal.add(0f);alNormal.add(0f);
                				
                				alNormal.add(1f);alNormal.add(0f);alNormal.add(0f);
                				alNormal.add(1f);alNormal.add(0f);alNormal.add(0f);
                				alNormal.add(1f);alNormal.add(0f);alNormal.add(0f);
            				}
        				}
        			}
        		}
        	}
        }
    	vCount=alVertex.size()/3;        
        float vertices[]=new float[alVertex.size()];
        for(int i=0;i<alVertex.size();i++)
        {
        	vertices[i]=alVertex.get(i);
        }
		
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
        
        //頂點法向量數據初始化================begin============================
        float normals[]=new float[vCount*3];
        for(int i=0;i<vCount*3;i++)
        {
        	normals[i]=alNormal.get(i);
        }
        
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());//設置字節順序
        mNormalBuffer = nbb.asFloatBuffer();//轉換為int型緩衝
        mNormalBuffer.put(normals);//向緩衝區中放入頂點著色數據
        mNormalBuffer.position(0);//設置緩衝區起始位置
        //頂點法向量數據初始化================end============================ 
        
        //頂點紋理數據的初始化================begin============================
        float textures[]=new float[alTexture.size()];
        for(int i=0;i<alTexture.size();i++)
        {
        	textures[i]=alTexture.get(i);
        }
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
    //根據當前點  當前點必須為牆   判斷出此點周圍最大的面積塊數
    public int[][] returnMaxBlock(int rowIndex,int colIndex)
    {
    	int rowindex=rowIndex;
    	int colindex=colIndex;
    	int rowsize;//用於記錄行的大小
    	int colsize;//用於記錄列的大小
    
    	int [][] area=new int[2][2];//用於記錄位置 area[0]表示起始點索引  area[1]表示寬度和高度
    	area[0][0]=rowIndex;area[0][1]=colIndex;
    	//橫向 高度為1
    	int tempRowSize=1;//高度
    	int tempColSize=1;//寬度
    	while(colindex+1<MAP[0].length&&MAP[rowindex][colindex+1]==1&&indexFlag[rowindex][colindex+1]==0)
    	{
    		tempColSize++;//寬度加一
    		colindex++;//列索引加一
    	}
    	rowsize=tempRowSize;colsize=tempColSize;
    	area[1][0]=colsize;area[1][1]=rowsize;//返回當前得面積位置
    	
    	//橫向寬度為2
    	tempRowSize=0;tempColSize=0;rowindex=rowIndex;colindex=colIndex;//數據初始化
    	while(colindex+1<MAP[0].length&&rowindex+1<MAP.length&&MAP[rowindex][colindex]==1&&
    	   indexFlag[rowindex][colindex]==0&&MAP[rowindex+1][colindex]==1&&indexFlag[rowindex+1][colindex]==0)
    	{
    		colindex++;
    		tempRowSize=2;
    		tempColSize++;//列數加一
    	}
    	if(tempColSize*tempRowSize>colsize)
    	{
    		rowsize=tempRowSize;colsize=tempColSize;
    		area[1][0]=colsize;area[1][1]=rowsize;//返回當前得面積位置
    	}
//    	縱向  寬度為1
    	tempRowSize=1;tempColSize=1;rowindex=rowIndex;colindex=colIndex;//數據初始化
    	while(rowindex+1<MAP.length&&MAP[rowindex+1][colindex]==1&&indexFlag[rowindex+1][colindex]==0)
    	{
    		rowindex++;
    		tempRowSize++;
    	}
    	if(tempRowSize*tempColSize>rowsize*colsize)
    	{
    		rowsize=tempRowSize;colsize=tempColSize;
    		area[1][0]=1;area[1][1]=rowsize;//返回當前得面積位置
    	}
    	//縱向寬度為2
    	tempRowSize=0;tempColSize=0;rowindex=rowIndex;colindex=colIndex;//數據初始化
    	while(colindex+1<MAP[0].length&&rowindex+1<MAP.length&&MAP[rowindex][colindex]==1&&
    	    	   indexFlag[rowindex][colindex]==0&&MAP[rowindex][colindex+1]==1&&indexFlag[rowindex][colindex+1]==0)
    	{
    	    rowindex++;
    	    tempColSize=2;
    	    tempRowSize++;//高度加1
    	}
    	if(tempColSize*tempRowSize>colsize*rowsize)
    	{
    	    rowsize=tempRowSize;colsize=tempColSize;
    	    area[1][0]=colsize;area[1][1]=rowsize;//返回當前得面積位置
    	}
    	//將indexFlag掃瞄過的格子置為1
    	for(int i=area[0][0];i<area[0][0]+area[1][1];i++)
    	{
    		for(int j=area[0][1];j<area[0][1]+area[1][0];j++)
    		{
    			indexFlag[i][j]=1;//將其值設置為    1     表示不用在掃瞄
    		}
    	}
    	return area;
    }
}

