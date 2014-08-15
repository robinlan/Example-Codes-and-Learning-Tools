package com.bn.wlqp;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class PicLoadUtil 
{

   //从资源中加载一幅图片
   public static Bitmap LoadBitmap(Resources res,int picId)
   {
	   Bitmap result=BitmapFactory.decodeResource(res, picId);
	   return result;
   }
   
   //缩放旋转图片的方法
   public static Bitmap scaleToFit(Bitmap bm,int dstWidth,int dstHeight)//缩放图片的方法
   {
   	float width = bm.getWidth(); //图片宽度
   	float height = bm.getHeight();//图片高度
   	float wRatio=dstWidth/height;
   	float hRatio=dstHeight/width;
   	
   	Matrix m1 = new Matrix(); 
   	m1.postScale(wRatio, hRatio);
   	Matrix m2= new Matrix();
   	m2.setRotate(90, dstWidth/2, dstHeight/2);
   	Matrix mz=new Matrix();
	mz.setConcat(m1, m2);
   	
   	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, mz, true);//声明位图        	
   	return bmResult;
   }
   
   public static Bitmap[][] splitPic
   (
		   int cols,//切割的行数 
		   int rows,//切割的列数    
		   Bitmap srcPic,//被切割的图片  
		   int dstWitdh,//切割后调整的目标宽度
		   int dstHeight//切割后调整的目标高度  
   ) 
   {   
	   final float width=srcPic.getWidth();
	   final float height=srcPic.getHeight();
	   
	   final int tempWidth=(int)(width/cols);
	   final int tempHeight=(int)(height/rows);
	   
	   Bitmap[][] result=new Bitmap[rows][cols];
	   
	   for(int i=0;i<rows;i++)
	   {
		   for(int j=0;j<cols;j++)
		   {
			   Bitmap tempBm=Bitmap.createBitmap(srcPic, j*tempWidth, i*tempHeight,tempWidth, tempHeight);		
			   result[i][j]=scaleToFit(tempBm,dstWitdh,dstHeight);
		   }
	   }
	   
	   return result;
   }
}