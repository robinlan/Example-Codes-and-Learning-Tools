package com.bn.gjxq;
import javax.microedition.khronos.opengles.GL10;
import static com.bn.gjxq.Constant.*;

public class ChessForControl
{
	boolean isMoved=false;//是否移動過
	LoadedObjectVertexNormalTexture object;//加載的3D物體
	int chessType;  //0-11  0-5 表示黑棋,6-11 表示白棋
	int col;//當前棋子所在的列
	int row;//當前棋子所在的行
	float y=0;//當前棋子的高度值
	//構造器,傳入參數為棋子模型引用,棋子角色,棋子的當前位置.
	public ChessForControl(LoadedObjectVertexNormalTexture object,int chessType,int row,int col)
	{
		this.object=object;//棋子模型的引用
		this.chessType=chessType;	//棋子的角色定位
		this.col=col;//棋子所在行
		this.row=row;//棋子所在列
	}
	public void drawSelf(GL10 gl,int texId)//畫英雄的函數
	{
		gl.glPushMatrix();//保護現場	
		gl.glTranslatef((0.5f+col-4)*UNIT_SIZE,0,(0.5f+row-4)*UNIT_SIZE);//將棋子移動到對應的位置
		gl.glTranslatef(0, 0.05f, 0);//由於模型的原因,需要向上移動一點
    	gl.glRotatef(((chessType>=0&&chessType<=5)?180:0), 0, 1, 0);//如果是黑方需要把模型旋轉180度
    	gl.glTranslatef(0, y, 0);//當棋子為選定時,把其畫高一點.
		object.drawSelf(gl,texId);//畫英雄
		gl.glPopMatrix();//恢復現場
	}
}

