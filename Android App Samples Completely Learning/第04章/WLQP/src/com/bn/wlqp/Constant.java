package com.bn.wlqp;
public class Constant
{
	static final int SCREEN_WIDTH=480;  //屏幕的宽度
	static final int SCREEN_HEIGHT=	320; //屏幕的高度
	static final int BACK_SIZE=16;  //背景大小
	public static int MOVE_YOFFSET=30;//选中扑克牌上移的偏移量
	//背景图的起始位置
	public static int BACK_XOFFSET=0;
	public static int BACK_YOFFSET=0;
	
	//START按钮
	public static int BUTTON_START_XOFFSET=140;
	public static int BUTTON_START_YOFFSET=100;
	//HELP按钮
	public static int BUTTON_HELP_XOFFSET=140;
	public static int BUTTON_HELP_YOFFSET=150;
	//ABOUT按钮
	public static int BUTTON_ABOUT_XOFFSET=140;
	public static int BUTTON_ABOUT_YOFFSET=200;
	//out 按钮
	public static int BUTTON_OUT_XOFFSET=140;
	public static int BUTTON_OUT_YOFFSET=250;
	
	//START按钮的尺寸
	public static int BUTTON_START_WIDTH=200;
	public static int BUTTON_START_HEIGHT=40;
	//START按钮的尺寸
	public static int BUTTON_HELP_WIDTH=200;
	public static int BUTTON_HELP_HEIGHT=40;
	//ABOUT按钮的尺寸
	public static int BUTTON_ABOUT_WIDTH=200;
	public static int BUTTON_ABOUT_HEIGHT=40;
	//退出按钮的尺寸
	public static int BUTTON_OUT_WIDTH=200;
	public static int BUTTON_OUT_HEIGHT=40;
	
	//得分
	public static int SCORE=0;
	
	//提示文字的坐标
	//自己出牌时的提示
	public static int TIP_OWN_XOFFSET=302;
	public static int TIP_OWN_YOFFSET=10;
	//别人出牌时的提示
	public static int TIP_OTHER_XOFFSET=302;
	public static int TIP_OTHER_YOFFSET=10;
	
	//扑克牌的本身的宽度和高度
    public static int CARD_WIDTH=49;
    public static int CARD_HEIGHT=72;
    
    //扑克牌左下角的坐标
    public static int CARD_LEFT_XOFFSET=100;
    public static int CARD_LEFT_YOFFSET=317;
	//扑克牌的起始和截止的坐标
	public static int CARD_SMALL_XOFFSET=100;
	public static int CARD_BIG_XOFFSET=389;
	
	//下面扑克牌的坐标
	public static int DOWN_X=100;
	public static int DOWN_Y=245;
	
	static final int MOVE_SIZE=15;   //显示的牌的间距
	static final int sleeptime=100;  //刷帧的间隔的时间
	public static int LEFT_UP_X=20;   //左上角图坐标X
	public static int LEFT_UP_Y=7;    //左上角图坐标Y
	
	//右侧玩家的名称的图的坐标
	public static int RIGHT_UP_PE1_X=395; 
	public static int RIGHT_UP_PE1_Y=10;  
	public static int RIGHT_UP_PE2_Y=30; 
	public static int RIGHT_UP_PE3_Y=50;
	//右侧玩家名称对应的头像图片的坐标
	//玩家1对应的
	public static int RIGHT_UP_HEAD1_XOFFSET=378;
	public static int RIGHT_UP_HEAD1_YOFFSET=10;
	//玩家2对应的
	public static int RIGHT_UP_HEAD2_XOFFSET=378;
	public static int RIGHT_UP_HEAD2_YOFFSET=30;
	//玩家3对应的
	public static int RIGHT_UP_HEAD3_XOFFSET=383;
	public static int RIGHT_UP_HEAD3_YOFFSET=50;
	
	//右侧分数100的图的坐标
	public static int RIGHT_UP_PEJ_Y=12; 
	public static int RIGHT_UP_PEJ_Y_SPAN=20;
	public static int RIGHT_UP_PEJ_X=453;
	public static int RIGHT_UP_PEJ_X_SPAN=10; 
	
	//上面扑克坐标
	public static int UP_X=150; 
	public static int UP_Y=0;
		
	//左上角的返回的坐标
	public static int LEFT_RETURN_XOFFSET=5;
	public static int LEFT_RETURN_YOFFSET=5; 
	//左上角的按钮的宽度和高度
	public static int BUTTON_RETURN_WIDTH=70;
	public static int BUTTON_RETURN_HEIGHT=30; 
	//右下角出牌按钮的坐标
	public static int RIGHT_FCARD_XOFFSET=400;
	public static int RIGHT_FCARD_YOFFSET=257;
	//出牌按钮的宽度和高度
	public static int BUTTON_FCARD_WIDTH=70;
	public static int BUTTON_FCARD_HEIGHT=30;
	//右下角放弃按钮的坐标
	public static int RIGHT_GIVEUP_XOFFSET=400;
	public static int RIGHT_GIVEUP_YOFFSET=289;
	//放弃按钮的宽度和高度 
	public static int BUTTON_GIVEUP_WIDTH=70;
	public static int BUTTON_GIVEUP_HEIGHT=30;

	//左下角客户端可以看见的头像的图坐标
	public static int LEFT_DOWN_X=20; 
	public static int LEFT_DOWN_Y=255;
	//左边上家人物头像的图片的坐标
	public static int LEFT_X=10;
	public static int LEFT_Y=70; 
	//右侧下家的任务头像的图片的坐标  
	public static int RIGHT_PERSON_XOFFSET=420; 
	public static int RIGHT_PERSON_YOFFSET=70; 
	//左侧上家的扑克牌的坐标
	public static int LEFT_CARD_XOFFSET=0;
	public static int LEFT_CARD_YOFFSET=100; 
	//右边图坐标
	public static int RIGHT_CARD_XOFFSET=422; 
	public static int RIGHT_CARD_YOFFSET=100;  
	
	//中间4张扑克牌
	public static int MIDDLE_CARD1_XOFFSET=130; 
	public static int MIDDLE_CARD1_YOFFSET=124;
	public static int MIDDLE_CARD_SPAN=55;
	
	public static int MIDDLE_CARD2_XOFFSET=185; 
	public static int MIDDLE_CARD2_YOFFSET=124;
	
	public static int MIDDLE_CARD3_XOFFSET=240; 
	public static int MIDDLE_CARD3_YOFFSET=124;
	
	public static int MIDDLE_CARD4_XOFFSET=295; 
	public static int MIDDLE_CARD4_YOFFSET=124;
    
    public static int[][] MAP_CARDS=
	{
		{53,10,8,52,9,7},
		{6,4,2,5,3,1},
		{0,11,35,12,36,34}, 
		{33,31,29,32,30,28}, 
		{27,38,23,26,37,22},
		{21,19,17,20,18,16},
		{15,13,24,14,25,49},
		{48,46,44,47,45,43},
		{42,40,51,41,39,50}
	};

    
    //生成对应图标的X Y坐标
    public static int[] fromNumToAB(int num)
    {
    	int[] result=new int[2];
    	
    	outer:for(int i=0;i<MAP_CARDS.length;i++)
    	{
    		for(int j=0;j<MAP_CARDS[0].length;j++)
    		{
    			if(MAP_CARDS[i][j]==num)
    			{
    				result[0]=i;
    				result[1]=j;
    				break outer;
    			}
    		}
    	}    	
    	return result;
    }
}