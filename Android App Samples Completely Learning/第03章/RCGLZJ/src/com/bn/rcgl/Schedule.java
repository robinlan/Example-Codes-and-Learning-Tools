package com.bn.rcgl;

import static com.bn.rcgl.Constant.getNowDateString;
import static com.bn.rcgl.Constant.getNowTimeString;
import static com.bn.rcgl.DBUtil.getSNFromPrefs;
import android.util.Log;

public class Schedule 
{
	private int sn;//每一個日程對應一個獨一無二的sn碼，在數據庫中為主鍵
	private String date1;//日程日期
	private String time1;//日程時間
	private String date2;//鬧鐘日期
	private String time2;//鬧鐘時間
	private String type;//日程類型
	private String title;//日程標題
	private String note;//日程備註
	private boolean timeSet;//日程是否設置具體時間
	private boolean alarmSet;//日程是否設置鬧鐘
	
	
	//創建新日程時的臨時數據，只需要年月日三個數據，用來在剛剛進入新建日程界面日把年月日默認設置成當前日期
	public Schedule(int y,int m,int d)
	{
		sn=0;
		date1=toDateString(y,m,d);
		time1=toTimeString(8,0);//時間默認8點
		
		date2=null;
		time2=null;
		
		title="";
		note="";
		type="";
		
		timeSet=true;
		alarmSet=false;
				
	}
	
	//此構造器為從數據庫讀取日程對像時用
	public Schedule(int sn,String date1,String time1,String date2,String time2,String title,String note,String type,String timeSet,String alarmSet)
	{
		this.sn=sn;
		this.date1=date1;
		this.time1=time1;
		this.date2=date2;
		this.time2=time2;
		this.title=title;
		this.note=note;
		this.type=type;
		this.timeSet=Boolean.parseBoolean(timeSet);
		this.alarmSet=Boolean.parseBoolean(alarmSet);
	}
	
	public int getYear()//獲得年
	{
		String[] date=date1.split("/");
		int tmp=Integer.valueOf(date[0]);
		return tmp;
	}
	
	public int getMonth()//獲得月
	{
		String[] date=date1.split("/");
		int tmp=Integer.valueOf(date[1]);
		return tmp;
	}
	
	public int getDay()//獲得日
	{
		String[] date=date1.split("/");
		int tmp=Integer.valueOf(date[2]);
		return tmp;
	}
	
	public int getHour()//獲得時
	{
		String[] time=time1.split(":");
		int tmp=Integer.valueOf(time[0]);
		return tmp;
	}
	
	public int getMinute()//獲得分
	{
		String[] time=time1.split(":");
		int tmp=Integer.valueOf(time[1]);
		return tmp;
	}
	
	public int getAYear()//獲得鬧鐘的年
	{
		String[] date=date2.split("/");
		int tmp=Integer.valueOf(date[0]);
		return tmp;
	}
	
	public int getAMonth()//獲得鬧鐘月
	{
		String[] date=date2.split("/");
		int tmp=Integer.valueOf(date[1]);
		return tmp;
	}
	
	public int getADay()//獲得鬧鐘日
	{
		String[] date=date2.split("/");
		int tmp=Integer.valueOf(date[2]);
		return tmp;
	}
	
	public int getAHour()//獲得鬧鐘時
	{
		String[] time=time2.split(":");
		int tmp=Integer.valueOf(time[0]);
		return tmp;
	}
	
	public int getAMin()//獲得鬧鐘分
	{
		String[] time=time2.split(":");
		int tmp=Integer.valueOf(time[1]);
		return tmp;
	}
	
	public void setType(String s)//設置類型
	{
		this.type=s;
	}
	
	public String getType()//獲得類型
	{
		return type;
	}
	
	public void setTitle(String s)//設置標題
	{
		this.title=s;
	}
	
	public String getTitle()//獲得標題
	{
		return title;
	}
	
	public void setNote(String s)//設置備註
	{
		this.note=s;
	}
	
	public String getNote()//獲得備註
	{
		return note;
	}
	
	public void setTimeSet(boolean b)//設置是否設置具體時間的布爾值
	{
		this.timeSet=b;
		if(!timeSet)//如果為false說明沒有設置具體時間，則具體時間默認為當天最後一分鐘
		{
			time1="23:59";
		}
	}
	
	public boolean getTimeSet()//得到是否設了時間
	{
		return timeSet;
	}
	
	public void setAlarmSet(boolean b)//設置是否設置鬧鐘的布爾值
	{
		this.alarmSet=b;
		if(!timeSet)//如果為false說明沒有設置鬧鐘，則鬧鐘置null
		{
			date2=null;
			time2=null;
		}
	}
	
	public boolean getAlarmSet()//得到是否設置了鬧鐘
	{
		return alarmSet;
	}
	
	public void setDate1(String y,String m,String d)//設置日程日期，轉換成YYYY/MM/DD
	{
		StringBuffer sb=new StringBuffer();
		sb.append(y);
		sb.append("/");
		sb.append(m);
		sb.append("/");
		sb.append(d);
		date1=sb.toString();
	}
	
	public String getDate1()//得到日程日期
	{
		return date1;
	}
	
	public void setTime1(String h,String m)//設置日程時間，轉換成HH:MM
	{
		StringBuffer sb=new StringBuffer();
		sb.append(h);
		sb.append(":");
		sb.append(m);
		time1=sb.toString();
	}
	
	public String getTime1()//獲得日程時間
	{
		return time1;
	}
	
	public void setDate2(String y,String m,String d)//設置鬧鐘日期
	{
		StringBuffer sb=new StringBuffer();
		sb.append(y);
		sb.append("/");
		sb.append(m);
		sb.append("/");
		sb.append(d);
		date2=sb.toString();
	}
	
	public String getDate2()//得到鬧鐘日期
	{
		return date2;
	}
	
	public void setTime2(String h,String m)//設置鬧鐘時間
	{
		StringBuffer sb=new StringBuffer();
		sb.append(h);
		sb.append(":");
		sb.append(m);
		time2=sb.toString();
	}
	
	public String getTime2()//得到鬧鐘時間
	{
		return time2;
	}	
	
	public void setSn(int sn)//設置sn碼 
	{
		this.sn = sn;
	}

	public int getSn() //得到sn碼
	{
		return sn;
	}

	public static String toDateString(int y,int m,int d)//靜態方法，把int型的年月日轉換成YYYY/MM/DD
	{
		StringBuffer sb = new StringBuffer();
		sb.append(y);
		sb.append("/");
		sb.append(m<10?"0"+m:""+m);
		sb.append("/");
		sb.append(d<10?"0"+d:""+d);
		return sb.toString();
	}
	
	public String toTimeString(int h,int m)//把int型的時分轉換成HH:MM
	{
		StringBuffer sb = new StringBuffer();
		sb.append(h<10?"0"+h:""+h);
		sb.append(":");
		sb.append(m<10?"0"+m:""+m);
		return sb.toString();
	}
		
	public String typeForListView()//用來得到在主界面的ListView裡顯示的類型格式
	{
		StringBuffer sbTmp=new StringBuffer();
		sbTmp.append("[");
		sbTmp.append(type);
		sbTmp.append("]");
		return sbTmp.toString();
	}
	
	public String dateForListView()//用來得到在主界面的ListView裡顯示的日期格式
	{
		StringBuffer sbTmp=new StringBuffer();
		sbTmp.append(date1);
		sbTmp.append("   ");
		return sbTmp.toString();
	}
	
	public String timeForListView()//用來得到在主界面的ListView裡顯示的時間格式
	{
		if(!timeSet)
		{
			return "- -:- -   ";
		}
		StringBuffer sbTmp=new StringBuffer();
		sbTmp.append(time1);
		sbTmp.append("   ");
		return sbTmp.toString();
	}
	
	public boolean isPassed()//讓日程設置時間與當前時間相比，判斷日程是否已過期
	{
		String nowDate=getNowDateString();
		String nowTime=getNowTimeString();
		String schDate=date1;
		String schTime=timeSet?time1:"23:59";//如果日程沒有設置時間，則認為過了當天23:59，也就是到了第二天才過時
							
		if(nowDate.compareTo(schDate)>0||(nowDate.compareTo(schDate)==0&&nowTime.compareTo(schTime)>0))
		{
			return true;
		}
		return false;
	}
	
	public String toInsertSql(RcActivity father)//獲取schedule對像存入數據庫時的sql語句
	{
		StringBuffer sb = new StringBuffer();
		sb.append("insert into schedule values(");
		sn=getSNFromPrefs(father);
		sb.append(sn);
		sb.append(",'");
		sb.append(date1);
		sb.append("','");
		sb.append(time1);
		sb.append("','");
		sb.append(date2);
		sb.append("','");
		sb.append(time2);
		sb.append("','");
		sb.append(title);
		sb.append("','");
		sb.append(note);
		sb.append("','");
		sb.append(type);
		sb.append("','");
		sb.append(timeSet);
		sb.append("','");
		sb.append(alarmSet);
		sb.append("')");	
		Log.d("toInsertSql",sb.toString());
		return sb.toString();
	}
	
	public String toUpdateSql(RcActivity father)//獲取schedule對像更新時的sql語句
	{
		int preSn=sn;//記錄之前的sn
		StringBuffer sb = new StringBuffer();
		sb.append("update schedule set sn=");
		sn=getSNFromPrefs(father);//換成新的sn
		sb.append(sn);
		sb.append(",date1='");
		sb.append(date1);
		sb.append("',time1='");
		sb.append(time1);
		sb.append("',date2='");
		sb.append(date2);
		sb.append("',time2='");
		sb.append(time2);
		sb.append("',title='");
		sb.append(title);
		sb.append("',note='");
		sb.append(note);
		sb.append("',type='");
		sb.append(type);
		sb.append("',timeset='");
		sb.append(timeSet);
		sb.append("',alarmset='");
		sb.append(alarmSet);
		sb.append("' where sn=");
		sb.append(preSn);
		Log.d("toUpdateSql",sb.toString());
		return sb.toString();
	}
}
