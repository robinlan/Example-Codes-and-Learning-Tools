package com.bn.lc;
import java.text.*;
public class FDSDUtil
{
	//保留两位小数
	public static String formatData(double d)
	{
		DecimalFormat myformat = new  DecimalFormat("0.00");
		return myformat.format(d);
	}
	//保留整数
	public static String formatDataInt(double d)
	{
		DecimalFormat myformat = new  DecimalFormat("0");
		return myformat.format(d);
	}
}