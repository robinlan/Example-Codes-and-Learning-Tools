package com.bn.reader;


public class CalUtil 
{
	//求折頁時邊上兩個點的坐標的方法
	public static int[] calCD(float ax,float ay,float bx,float by)
	{
		//直線cd的斜率
		float kq=(bx-ax)/(ay-by);
		//p點的坐標
		float px=(ax+bx)/2;
		float py=(ay+by)/2;		
		//直線cd的b值
		float bq=py-kq*px;
		//c點的坐標
		float cx=(by-bq)/kq;
		float cy=by;
		//d點的坐標
		float dx=bx;
		float dy=kq*bx+bq;		
		return new int[]{(int)cx,(int)cy,(int)dx,(int)dy};
	}
}

