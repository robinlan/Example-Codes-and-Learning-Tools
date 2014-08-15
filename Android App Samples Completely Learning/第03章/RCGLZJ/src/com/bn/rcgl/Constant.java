package com.bn.rcgl; 

import java.util.Calendar;  
public class Constant 
{
	final static int DIALOG_SET_SEARCH_RANGE=1;//設置搜索日期範圍對話框
	final static int DIALOG_SET_DATETIME=2;//設置日期時間對話框
	final static int DIALOG_SCH_DEL_CONFIRM=3;//日程刪除確認
	final static int DIALOG_CHECK=4;//查看日程
	final static int DIALOG_ALL_DEL_CONFIRM=5;//刪除全部過期日程
	final static int DIALOG_ABOUT=6;//關於對話框
	 
	final static int MENU_HELP=1;//菜單幫助  
	final static int MENU_ABOUT=2;//菜單關於
	
	public static enum WhoCall
	{//判斷誰調用了dialogSetRange，以決定哪個控件該gone或者visible 
		SETTING_ALARM,//表示設置鬧鐘 按鈕
		SETTING_DATE,//表示設置日期按鈕
		SETTING_RANGE,//表示設置日程查找範圍按鈕
		NEW,//表示新建日程按鈕
		EDIT,//表示修改日程按鈕
		SEARCH_RESULT//表示查找按鈕
	}
	
	public static enum Layout
	{
		WELCOME_VIEW,
		MAIN,//主界面
		SETTING,//日程設置
		TYPE_MANAGER,//類型管理
		SEARCH,//查找
		SEARCH_RESULT,//查找結果界面
		HELP,//幫助界面
		ABOUT
	}
	
	public static String getNowDateString()//獲得當前日期方法並轉換格式YYYY/MM/DD
	{
		Calendar c=Calendar.getInstance();
		String nowDate=Schedule.toDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH));
		return nowDate;
		
	}
	public static String getNowTimeString()//獲得當前時間，並轉換成格式HH:MM
	{
		Calendar c=Calendar.getInstance();
		int nowh=c.get(Calendar.HOUR_OF_DAY);
		int nowm=c.get(Calendar.MINUTE);
		String nowTime=(nowh<10?"0"+nowh:""+nowh)+":"+(nowm<10?"0"+nowm:""+nowm);
		return nowTime;
	}
}
