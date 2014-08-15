package com.bn.tkqz;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Barrier 
{
	public static final int RODE=0;
	public static final int BRICK=1;
	public static final int STONE=2;
	public static final int SEA=3;
	public static final int ICE=4;
	public static final int GRASS=5;
	int x;
	int y;
	Bitmap bitmap;
	abstract void drawSelf(Canvas canvas,Paint paint,int x,int y);
}
