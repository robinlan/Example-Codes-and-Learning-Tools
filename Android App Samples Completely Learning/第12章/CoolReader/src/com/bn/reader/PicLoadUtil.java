package com.bn.reader;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class PicLoadUtil 
{

   //從資源中加載一幅圖片
   public static Bitmap LoadBitmap(Resources res,int picId)
   {
	   Bitmap result=BitmapFactory.decodeResource(res, picId);
	   return result;
   }
   
   //縮放旋轉圖片的方法
   public static Bitmap scaleToFit(Bitmap bm,float targetWidth,float targetHeight)//縮放圖片的方法
   {
   	float width = bm.getWidth(); //圖片寬度
   	float height = bm.getHeight();//圖片高度	
   	
   	Matrix m1 = new Matrix(); 
   	m1.postScale(targetWidth/width, targetHeight/height);//按照	targetWidth/targetHeight 縮放圖片
   	Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, (int)width, (int)height, m1, true);//聲明位圖        	
   	return bmResult;
   }
}

