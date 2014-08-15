package com.bn.lb.admin;
import java.util.*;
import static com.bn.lb.admin.Constant.*;
public class DateUtil
{
	static RootActivity activity;
	static Date dt;
	static int ERROR_MSG_INT;
	public DateUtil(RootActivity activity)
	{
		this.activity=activity;
	}
	public static String getdate(String years,String monthes,String dates,int yearInterval)
	{
		String result=null;//返回值
		dt=new Date();//日期對像
		//得到系統日期
		int systemyears=dt.getYear()+1900;
		
		//正則式
		String str1="\\d{4}";
		String str2="[1-9]|[0][1-9]|[1][0-2]";
		String str3="[1-9]|[0][1-9]|[1-2][0-9]|[3][0-1]";
	    if((years.matches(str1))&&(monthes.matches(str2))&&(dates.matches(str3))&&(yearInterval>0))
	    {//判斷是否符合格式
	    	int insertyear=Integer.parseInt(years);
	    	int insertmonth=Integer.parseInt(monthes);
	    	int date=Integer.parseInt(dates);
	    		
	    	if(((Math.abs(insertyear-systemyears))<=yearInterval)&&
	    	   ((insertmonth!=2)||((insertmonth==2)&&(((insertyear%4!=0)&&(date<29))||((insertyear%4==0)&&(date<=29))))))
	    	{
	    		String tempMonth=(insertmonth<10)?("0"+insertmonth):""+insertmonth;
				String tempDate=(date<10)?("0"+date):""+date;
				result=insertyear+"-"+tempMonth+"-"+tempDate;
	    	}
	    	else if(!((Math.abs(insertyear-systemyears))<=yearInterval))
	    	{
	    		ERROR_MSG_INT=0;
	    		System.out.println("年份太久遠");
	    	}
	    	else
	    	{
	    		ERROR_MSG_INT=1;
	    		System.out.println("二月的天數不對");
	    	}
	    }
	    else if(yearInterval<=0)
	    {//
	    	ERROR_MSG_INT=2;
	    	System.out.println("出錯");
	    }
	    else 
	    {//不符合格式
	    	System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
	    	ERROR_MSG_INT=3;
	    	System.out.println("年份或月份或日期格式不對！請您檢查清楚！");
	    }
		return result;
	}
	public static String getSystemDateTime()
	{
		dt=new Date();//日期對像
		//得到系統日期
		int tempyears=dt.getYear()+1900;
		int tempmonths=dt.getMonth()+1;
		int tempdate=dt.getDate();
		
		String result=tempyears+"-"+tempmonths+"-"+tempdate;
		System.out.println(result);
		return result;
	}
}
