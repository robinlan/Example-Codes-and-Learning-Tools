package com.bn.gjxq;
import static com.bn.gjxq.Constant.UNIT_SIZE;

import javax.microedition.khronos.opengles.GL10;
//棋盤的底盤
public class ChessFoundation 
{
	FoundationSquar squar;//FoundationSquar的引用
	RectWall cc;//RectWall的引用
	float whith_1=9.5f;//寬度1
	float whith_2=10.5f;//寬度2
	float height=1;//高度
    public ChessFoundation()
    {
    	squar=new FoundationSquar(whith_1,whith_2,height);//創建底盤每一面的矩形
    	cc=new RectWall(whith_1+0.01f,whith_1+0.01f);
    	cc.z=0;
    }
    public void  drawSelf(GL10 gl,int texId,int texIdd)
    {
    	gl.glPushMatrix();//保護矩陣
    	gl.glTranslatef(0, -0.05f,0 );//向外軸移動
    	
    	gl.glPushMatrix();
    	gl.glRotatef(-90, 1, 0, 0);//繞X軸反向旋轉90
    	
    	cc.drawSelf(gl, texIdd);//進行繪製
    	gl.glPopMatrix();
    	
    	gl.glPushMatrix();//保護當前矩陣
		gl.glTranslatef(0, 0, whith_1/2*UNIT_SIZE);//將其移動到合適位置
		squar.drawSelf(gl, texId);//畫正前方的一個
		gl.glTranslatef(0, 0, -whith_1*UNIT_SIZE);
		gl.glRotatef(180f, 0, 1, 0);
		squar.drawSelf(gl, texId);//畫最後方的一個
		gl.glPopMatrix();//畫完前後兩面後恢復矩陣
		
		gl.glPushMatrix();	//保護當前矩陣		
		gl.glTranslatef(whith_1/2*UNIT_SIZE, 0, 0);//移動到合適位置
		gl.glRotatef(90f, 0, 1, 0);
		squar.drawSelf(gl, texId);
		gl.glTranslatef(0,0, -whith_1*UNIT_SIZE);
		gl.glRotatef(180f, 0, 1, 0);
		squar.drawSelf(gl, texId);
		gl.glPopMatrix();//恢復矩陣
		gl.glPopMatrix();
    }
}

