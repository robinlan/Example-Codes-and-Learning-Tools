package com.bn.lc;
import java.util.*;
public class DateUtil
{
	static Date dt;
	static int ERROR_MSG_INT;
	public static String getdate(String years,String monthes,String dates,int yearInterval)
	{
		String result=null; //返回值
		dt=new Date();      //日期對像
		
		int systemyears=dt.getYear()+1900; //得到系統日期
		
		//正則式
		String str1="\\d{4}";
		String str2="[1-9]|[0][1-9]|[1][0-2]";
		String str3="[1-9]|[0][1-9]|[1-2][0-9]|[3][0-1]";
		
	    if((years.matches(str1))&&(monthes.matches(str2))&&(dates.matches(str3))&&(yearInterval>0))
	    {//判斷是否符合格式
	    	int insertyear=Integer.parseInt(years);
	    	int insertmonth=Integer.parseInt(monthes);
	    	int date=Integer.parseInt(dates);
	    		
	    	if(((Math.abs(insertyear-systemyears))<=yearInterval))
	    	{
	    		if(((insertmonth==2)&&(((insertyear%4!=0)&&
	    	      (date<29))||((insertyear%4==0)&&(date<=29)))))
	    		{
	    			String tempMonth=(insertmonth<10)?("0"+insertmonth):""+insertmonth;
					String tempDate=(date<10)?("0"+date):""+date;
					result=insertyear+"-"+tempMonth+"-"+tempDate;
	    		}
	    		else
	    		{
	    			ERROR_MSG_INT=1;
	    		}
	    		if(((insertmonth==4)||(insertmonth==6)||(insertmonth==8)||
	    			(insertmonth==9)||(insertmonth==11))&&(date<31))
	    		{
	    			String tempMonth=(insertmonth<10)?("0"+insertmonth):""+insertmonth;
					String tempDate=(date<10)?("0"+date):""+date;
					result=insertyear+"-"+tempMonth+"-"+tempDate;
	    		}
		    	else
		    	{//二月天數不對
		    		ERROR_MSG_INT=1;
		    	}
	    		if(((insertmonth==1)||(insertmonth==3)||(insertmonth==5)||
	    			(insertmonth==7)||(insertmonth==8)||(insertmonth==10)||
	    			(insertmonth==12))&&(date<32))
	    		{
	    			String tempMonth=(insertmonth<10)?("0"+insertmonth):""+insertmonth;
					String tempDate=(date<10)?("0"+date):""+date;
					result=insertyear+"-"+tempMonth+"-"+tempDate;
	    		}
		    	else
		    	{//天數不對
		    		ERROR_MSG_INT=1;
		    	}
	    	} 
	    	else if(!((Math.abs(insertyear-systemyears))<=yearInterval))
	    	{//年份太久遠
	    		ERROR_MSG_INT=0;
	    	}
	    }
	    else if(yearInterval<=0)
	    {//出錯
	    	ERROR_MSG_INT=2;
	    }
	    else 
	    {//不符合格式
	    	ERROR_MSG_INT=3;
	    }
		return result;
	}
	
	public static String getSystemDateTime() //得到系統日期
	{
		dt=new Date();//日期對像
		//得到系統日期
		int tempyears=dt.getYear()+1900;
		int tempmonths=dt.getMonth()+1;
		int tempdate=dt.getDate();
		
		String tempMonth=(tempmonths<10)?("0"+tempmonths):""+tempmonths;
		String tempDate=(tempdate<10)?("0"+tempdate):""+tempdate;
		String result=tempyears+"-"+tempMonth+"-"+tempDate;
		return result;
	}
}
