package com.bn.lc;

public class Constant 
{
	static int textlength=15;
	//每個菜單項目的編號=======begin=========
	final static int MENU_GENDER_HELP=0;    
    final static int MENU_GENDER_ABOUT=1;
    final static int MAIN_GROUP=2;        //外層總菜單項組的編號
	//休息時間
	static int SLEEPTIME=100;
	//標題
	static int BNLC_XOFFSET=60; 
	static int BNLC_YOFFSET=10;
	//背景
	static int BACK_XOFFSET=0;     
	static int BACK_YOFFSET=0;
	//圖片的大小
	static int PIC_WIDTH=80;  
	static int PIC_HEIGHT=80;
	//收支類別
	static int LEI_XOFFSET=20;    
	static int LEI_YOFFSET=60;
	//日常收入
	static int SHOU_XOFFSET=120;  
	static int SHOU_YOFFSET=60;
	//日常支出
	static int ZHI_XOFFSET=220;  
	static int ZHI_YOFFSET=60;
	//收入統計
	static int STONG_XOFFSET=20;  
	static int STONG_YOFFSET=200;
	//支出統計
	static int ZTONG_XOFFSET=120; 
	static int ZTONG_YOFFSET=200;
	//收入查詢
	static int SCHA_XOFFSET=220;  
	static int SCHA_YOFFSET=200;
	//支出查詢
	static int ZCHA_XOFFSET=20;  
	static int ZCHA_YOFFSET=340;
	//系統設置
	static int XI_XOFFSET=120;  
	static int XI_YOFFSET=340;
	//退出系統
	static int OUT_XOFFSET=220; 
	static int OUT_YOFFSET=340;
	
	public static final int NAME_INPUT_DIALOG_ID=0; //姓名輸入對話框id
	public static final int DATE_INPUT_DIALOG_ID=1; //日期輸入對話框id
	
	public static final int UP_TIME=1;    //向上按鍵
	public static final int DOWN_TIME=-1;//向下按鍵
	
	public static final int YEAR_INTERVAL=100; //年限的上下浮動
	public static final int YEAR_LONG_INT=0; //年限久遠
	public static final int ERROR_FEBRUARY_INT=1; //二月天數不對
	public static final int ERROR_INT=2; //年限久遠
	public static final int ERROR_FORMAT_INT=3; //格式不對
	  
	 
	//下拉列表數組
	static String[] sexIds={"男","女"};  //性別下拉表
	static String[] bloodIds={"O型","A型","B型","AB型"};
	static String[] provinceIds={"北京","天津 ","上海  ","重慶省","河北省 ","河南省","雲南省 ","山東省"};
	static String[] cityIds={"北京","天津 ","上海市  ","重慶","河北 ","河南","雲南 ","山東"};
	static String[] years={"三個月","半年","一年","二年","三年","五年"};//時間
	static String[] rates={"2.60","2.80","3.00","3.90","4.50","5.00"};//利率
}

