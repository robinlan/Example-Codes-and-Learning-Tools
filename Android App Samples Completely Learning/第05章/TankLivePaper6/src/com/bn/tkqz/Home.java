package com.bn.tkqz;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Home
{
	int x;
	int y;
	Bitmap bitmap;
	Home(Bitmap bitmap,int x,int y)
	{
		this.x=x;
		this.y=y;
		this.bitmap=bitmap;
	}
	void drawSelf(Canvas canvas,Paint paint)
	{
		canvas.drawBitmap(bitmap, x,y, paint);
	}
	void explode()
	{
		this.bitmap=AliveWallPaperTank.homediedBitmap;
		AliveWallPaperTank.overGame();
	}
}
