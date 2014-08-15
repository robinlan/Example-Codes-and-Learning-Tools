package com.bn.reader;

import static com.bn.reader.Constant.BITMAP;
import static com.bn.reader.Constant.CURRENT_LEFT_START;
import static com.bn.reader.Constant.CURRENT_PAGE;
import static com.bn.reader.Constant.PAGE_HEIGHT;
import static com.bn.reader.Constant.PAGE_WIDTH;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

//¼ÐÃÑ©Ò¦³SurfaceViewªº±`¶qÃþ
class WhatMessage{
	public static final int GOTO_WELLCOME_VIEW=0;
	public static final int GOTO_MAIN_MENU_VIEW=1;
	public static final int GOTO_SEARCHBOOK_LIST=2;
	public static final int GOTO_BACHGROUND_LIST=3;
}
//ªí¥Ü¦U­Ó¬É­±ªº¦w¥þÃþ«¬ªTÁ|
enum WhichView {WELLCOM_VIEW,MAIN_VIEW,SEARCHBOOK_LIST,BACKGROUND_LIST }

public class ReaderActivity extends Activity {
	//­I´º¹Ï
	int[] drawableIds=
	{R.drawable.bg_fhzl,R.drawable.bg_lsct,R.drawable.bg_sjzx,R.drawable.bg_ssnh,R.drawable.bg_wffm,R.drawable.bg_ygmm};
	//­I´º¹Ï´y­z
	int[] msgIds={R.string.fhzl,R.string.lsct,R.string.sjzx,R.string.ssnh,R.string.wffm,R.string.ygmm};
	//¤å¦rÃC¦â
	int[] drawColorIds={R.drawable.tc_black,R.drawable.tc_blue,R.drawable.tc_gray,R.drawable.tc_green,
			R.drawable.tc_purper,R.drawable.tc_red,R.drawable.tc_yellow,R.drawable.tc_white};
	//¤å¦rÃC¦â´y­z
	int[] msgIds2={R.string.tc_black,R.string.tc_blue,R.string.tc_gray,R.string.tc_green,
			R.string.tc_purper,R.string.tc_red,R.string.tc_yellow,R.string.tc_white};
	//¦rÅé¤j¤p´y­z
	int[] fontSizeIds={R.string.font_size16,R.string.font_size24,R.string.font_size32};
	//¦rÅé¹Ï¤ù
	int[] fontSizeDrawable={R.drawable.font_size3,R.drawable.font_size2,R.drawable.font_size1};
	
	//­µ¼Ö¦WºÙ
	int[] musicname={R.string.music_gs,R.string.music_mh};
	
	String sdcardPath="/sdcard";//®Ú¥Ø¿ý
	String leavePath;//¤l¤å¥ó¸ô®|
	ListView lv;//¦Cªíªº¤Þ¥Î
	Button root_b;//ªð¦^®Ú¥Ø¿ýªº«ö¶s
	Button back_b;//ªð¦^¨ì¤W¼h¥Ø¿ý
	
	//dialog½s¸¹
	final int LIST_SEARCH=1;//§ä®Ñ«ö¶s
	final int LIST_TURNPAGE=2;//¦Û°ÊÂ½®Ñ«ö¶s
	final int LIST_SET=3;//³]¸m«ö¶s
	final int LIST_BOOKMARK=4;//®ÑÅÒ«ö¶s
	
	final int NAME_INPUT_DIALOG_ID=5;//¿é¤J®ÑÅÒ¦W¦rªº¹ï¸Ü®Ø
	final int SELECT_BOOKMARK=6;//¿ï¾Ü®ÑÅÒªº¹ï¸Ü®Ø	
	final int EXIT_READER=7;//°h¥X³n¥ó¹ï¸Ü®Ø
	final int DELETE_ONE_BOOKMARK=8;//§R°£¤@±ø°O¿ýªº¹ï¸Ü®Ø
	final int DELETE_ALL_BOOKMARK=9;//²MªÅ·í«e®Ñªº©Ò¦³®ÑÅÒ
	
	final int SET_FONT_SIZE	=10;//³]¸m¦rÅé¤j¤p¹ï¸Ü®Ø
	final int SET_FONT_COLOR=11;//³]¸m¦rÅéÃC¦â¹ï¸Ü®Ø
	
	final int BACKGROUND_PIC=12;//­I´º¹Ï¤ù
	final int BACKGROUND_MUSIC=13;//­I´º­µ¼Ö¹ï¸Ü®Ø

	DownLoad dl;
	WhichView curr;
	ReaderView readerView;//ReaderViewªº¤Þ¥Î
	WellcomeSurfaceView wellcomView;
	ListViewUtills lvutills;
	TurnPageThread turnpage;
	
	MediaPlayer mp;//´CÅé¼½©ñ¾¹¤Þ¥Î
	
	SharedPreferences sp;//§PÂ_¬O²Ä´X¦¸¥´¶}¦P¤@¥»®Ñ
	
	List<BookMark> dataBaseBookMark=new ArrayList<BookMark>();//¦s©ñ©Ò¦³±N­n©ñ¤J¡u®ÑÅÒ¬É­±¦Cªí¡v¤¤ªº®ÑÅÒ
    
	String[] tempname=null;//¦s©ñ±q¼Æ¾Ú®w¤¤¨ú¥Xªº©Ò¦³¡u?âJ–E¾X®³ŽÕ?
	
	int[] temppage;//¦s©ñ±q¼Æ¾Ú®w¤¤¨ú¥Xªº©Ò¦³·í«e®Ñ®ÑÅÒªº­¶¼Æ
	
	String deleteOneBookMarkName=null;//§R°£¤@±ø°O¿ýªº®ÑÅÒ¦WºÙ
	boolean haveBookMark=false;//§PÂ_¼Æ¾Ú®w¤¤¬O§_¦s¦b®ÑÅÒ
	
	Handler myHandler = new Handler(){//³B²z¦U­ÓSurfaceViewµo°eªº®ø®§
        public void handleMessage(Message msg) {
        	switch(msg.what)
        	{
        	case WhatMessage.GOTO_WELLCOME_VIEW:
        		goToWellcomView();
        		break;
        	case WhatMessage.GOTO_MAIN_MENU_VIEW:
        		goToReaderView();
        		break;
        	case WhatMessage.GOTO_SEARCHBOOK_LIST:
        		goToSearchBooklist();
        		break;
        	case WhatMessage.GOTO_BACHGROUND_LIST:
        		goToBackground();
        		break;
        	}
        }
	};
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
         
        //¥þ«ÌÅã¥ÜActivityªº³]¸m
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//¥h±¼¼ÐÃD
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,
        	   WindowManager.LayoutParams.FLAG_FULLSCREEN);//¥h±¼¼ÐÀY
        //¥»³n¥ó¬O¾î«ÌÀ³¥Î
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//±j¨î¾î«Ì        
        //Àò¨ú¤À¿ë²v
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);       
        //µ¹±`¶qÃþ¤¤ªº«Ì¹õ°ª©M¼e½á­È
        if(dm.widthPixels>dm.heightPixels)
        {
        	 Constant.SCREEN_WIDTH=dm.widthPixels;
             Constant.SCREEN_HEIGHT=dm.heightPixels;   
        }
        else
        {
        	Constant.SCREEN_WIDTH=dm.heightPixels;
            Constant.SCREEN_HEIGHT=dm.widthPixels;  
        }
        //µ¹±`¶qÃþ¤¤ªº¦Û¾AÀ³«Ì¹õªºÅÜ¶q³]ªì­È
        Constant.changeRatio();//½Õ¥Î¦Û¾AÀ³«Ì¹õªº¤èªk
        
        readerView=new ReaderView(this);//®³¨ìReaderViewªº¤Þ¥Î

        isWhichTime();//§PÂ_¬O²Ä´X¦¸¥´¶}³n¥ó¡A®Ú¾Ú²Ä´X¦¸¥´¶}³n¥ó¡A¥´¶}ªº®Ñ­¶¦ì¸m¤£¦P
        
        lvutills=new ListViewUtills(this);
        turnpage=new TurnPageThread(readerView);
        sendMessage(WhatMessage.GOTO_WELLCOME_VIEW);//¸õÂà¨ì¥D¬É­±
    }
	@Override
	public boolean onKeyDown(int keyCode,KeyEvent e)
	{
		 switch(keyCode)
		 {
		 	case 4:
		 		//¸õÂà¨ì¥D¬É­±
		 		sendMessage(WhatMessage.GOTO_MAIN_MENU_VIEW);//¸õÂà¨ì¥D¬É­±
		 		break;
		   case 82:
			   super.openOptionsMenu();
			   break;   
		   }
		   return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
	   //menuµæ³æ²Õ
	   //§ä®Ñ«ö¶s
	   MenuItem search=menu.add(0, 0, 0, R.string.search);
	   search.setIcon(R.drawable.m_search);//§ä®Ñ¹Ï¤ù
	   OnMenuItemClickListener searchbook=new OnMenuItemClickListener()
	   {//¹ê²{µæ³æ¶µÂIÀ»¨Æ¥óºÊÅ¥±µ¤f
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				showDialog(LIST_SEARCH);
				return true;
			}    		
		};
		search.setOnMenuItemClickListener(searchbook);//µ¹¡u§ä®Ñ¡v²K¥[ºÊÅ¥¾¹    	

	   //®ÑÅÒ«ö¶s
	   MenuItem bookMark=menu.add(0,0,0,R.string.bookmark);
	   bookMark.setIcon(R.drawable.m_bookmark);
	   OnMenuItemClickListener bookmark=new OnMenuItemClickListener()
	   {
		   @Override
		   public boolean onMenuItemClick(MenuItem item) {
			   showDialog(LIST_BOOKMARK);
			   
			   return true;
		   }	   
	   };
	   bookMark.setOnMenuItemClickListener(bookmark);

	   //Â½­¶«ö¶s
	   MenuItem turnPage=menu.add(0,0,0,R.string.turnpage);
	   turnPage.setIcon(R.drawable.m_turnpage);
	   OnMenuItemClickListener turn=new OnMenuItemClickListener()
	   {//¹ê²{µæ³æ¶µÂIÀ»¨Æ¥óºÊÅ¥±µ¤f
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				showDialog(LIST_TURNPAGE);
				return true;
			}    		
		};
		turnPage.setOnMenuItemClickListener(turn);//µ¹¡uÂ½­¶¡v²K¥[ºÊÅ¥¾¹  
	    
	   //³]¸m«ö¶s
	   MenuItem setUp=menu.add(0,0,0,R.string.setup);
	   setUp.setIcon(R.drawable.m_set);
	   OnMenuItemClickListener set=new OnMenuItemClickListener()
	   {//¹ê²{µæ³æ¶µÂIÀ»¨Æ¥óºÊÅ¥±µ¤f
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				showDialog(LIST_SET);
				return true;
			}    		
		};
		setUp.setOnMenuItemClickListener(set);//µ¹¡u³]¸m¡v²K¥[ºÊÅ¥¾¹    
	     return true;
	}
   
	@Override
	public Dialog onCreateDialog(int id)
	{
		Dialog dialog=null;
		switch(id)
		{
		case LIST_SEARCH://§ä®Ñ
			Builder b=new AlertDialog.Builder(this); 
 		  	b.setIcon(R.drawable.m_search);//³]¸m¹Ï¼Ð
 		  	b.setTitle(R.string.search);//³]¸m¼ÐÃD
 		  	b.setItems(
 			 R.array.searchbook,
 			 new DialogInterface.OnClickListener()
     		 {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//³o¸Ì¥[ÂIÀ»¦Cªí¤¤ªº¤º®e²£¥Íµ²ªGªº¥N½X
						switch(which)
						{
						case 0:
							searchBook();
							break;
						case 1:
							downTxt();
							break;
						}
					}      			
     		 }
 		   );
 		  	dialog=b.create();
 		  	break;
		case LIST_BOOKMARK://®ÑÅÒ¤G¯Åµæ³æ
			b=new AlertDialog.Builder(this);
			b.setIcon(R.drawable.m_bookmark);//³]¸m¹Ï¼Ð
			b.setTitle(R.string.bookmark);//³]¸m¡u®ÑÅÒ¡v¼ÐÃD
			b.setItems(
				R.array.bookmark,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					//³o¸Ì²K¥[ÂIÀ»¦Cªí¤¤ªº¤º®e²£¥Íµ²ªGªº¥N½X
						switch(which)
						{
						case 0://²K¥[®ÑÅÒ
							showDialog(NAME_INPUT_DIALOG_ID);
							break;
						case 1://¿ï¾Ü®ÑÅÒ
							try
							{//§PÂ_¼Æ¾Ú®w¤¤¬O§_¦s¦b·í«e³o¥»®Ñªº®ÑÅÒ
								haveBookMark=SQLDBUtil.judgeHaveBookMark(Constant.FILE_PATH);
							}catch(Exception e)
							{
								e.printStackTrace();
							}
							if(haveBookMark)
							{
								//¦pªG¦s¦b®ÑÅÒ
								showDialog(SELECT_BOOKMARK);
							}else
							{//¦pªG¤£¦s¦b®ÑÅÒ¡A¥XToast
								Toast.makeText
								(
									ReaderActivity.this, 
									"½Ð¥ý²K¥[®ÑÅÒ", 
									Toast.LENGTH_SHORT
								).show();
							}
							
							break;
						case 2://²MªÅ®ÑÅÒ
							showDialog(DELETE_ALL_BOOKMARK);							
							break;
						}
					}
				}	
			);
			dialog=b.create();
			break;
		case NAME_INPUT_DIALOG_ID://¼u¥X²K¥[®ÑÅÒ¹ï¸Ü®Ø
			dialog=new MyDialog(this);
			break;
		case SELECT_BOOKMARK://¼u¥X¿ï¾Ü®ÑÅÒªº¹ï¸Ü®Ø
			b=new AlertDialog.Builder(this);
			b.setItems(null, null);
			dialog=b.create();
			break;
		case EXIT_READER://°h¥X³n¥ó¹ï¸Ü®Ø
			b=new AlertDialog.Builder(this);
			b.setItems(null, null);
			dialog=b.create();
			break;
		case DELETE_ONE_BOOKMARK://§R°£¤@±ø®ÑÅÒ°O¿ý
			b=new AlertDialog.Builder(this);
			b.setItems(null, null);
			dialog=b.create();
			break;
		case DELETE_ALL_BOOKMARK://²MªÅ·í«e³o¥»®Ñªº¥þ³¡°O¿ý
			b=new AlertDialog.Builder(this);
			b.setItems(null, null);
			dialog=b.create();
			break;
			
		case SET_FONT_SIZE://¦rÅé¤j¤p
			b=new AlertDialog.Builder(this);
			b.setItems(null, null);
			dialog=b.create();
			break;
		case BACKGROUND_MUSIC://¦rÅéÃC¦â
			b=new AlertDialog.Builder(this);
			b.setItems(null, null);
			dialog=b.create();
			break;
		case SET_FONT_COLOR://³]¸m¦rÅéÃC¦â
			dialog=new MyDialogFontColor(this);//¥Îxml¤å¥ó¦Û¤v§G§½
			break;
		case BACKGROUND_PIC://­I´º¹Ï¤ù¹ï¸Ü®Ø
			dialog=new MyDialogBackgroundPic(this);//¥Î¦Û¤v§G§½ªºxml°Ý¤å¥ó
			break;
		case LIST_TURNPAGE://¦Û°ÊÂ½®Ñ
			b=new AlertDialog.Builder(this); 
 		  	b.setIcon(R.drawable.m_turnpage);//³]¸m¹Ï¼Ð
 		  	b.setTitle(R.string.turnpage);//³]¸m¼ÐÃD
 		  	b.setItems(
 			 R.array.turnpage, 
 			 new DialogInterface.OnClickListener()
     		 {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//³o¸Ì¥[ÂIÀ»¦Cªí¤¤ªº¤º®e²£¥Íµ²ªGªº¥N½X
						switch(which)
						{
						case 0://30¬í¦Û°ÊÂ½®Ñ
							if(Constant.FILE_PATH==null)//¦pªG¨S¦³¿ï®Ñ
				    		    {
				    		    	Toast.makeText
									(
										ReaderActivity.this, 
										"½Ð¿ï¾Ü±z­n¾\Åªªº¤å¥»", 
										Toast.LENGTH_SHORT
									).show();
				    		    	
				    		    }else//¦pªG¤w¸g¿ï®Ñ
				    		    {
					    		    if(turnpage.isPageflag()==false)//¦pªG½uµ{¨S¦³¶}©l¡A«h¶}©l½uµ{
					    		    {
					    		    	turnpage.setPageflag(true);
					    		    	turnpage.start();
					    		    }
							    	turnpage.setFiftySecond(false);
							    	turnpage.setFortySecond(false);
							    	turnpage.setThirtySecond(true);//±N30¬í³]¬°true¨ä¥L¬°false
							    	sendMessage(WhatMessage.GOTO_MAIN_MENU_VIEW);//¸õÂà¨ì¥D¬É­±
				    		    }
							break;
						case 1://40¬í¦Û°ÊÂ½®Ñ
			    		    if(Constant.FILE_PATH==null)//¦pªG¨S¦³¿ï®Ñ
			    		    {
			    		    	Toast.makeText
								(
									ReaderActivity.this, 
									"½Ð¿ï¾Ü±z­n¾\Åªªº¤å¥»", 
									Toast.LENGTH_SHORT
								).show();
			    		    	
			    		    }else//¦pªG¤w¸g¿ï®Ñ
			    		    {
			    		    	if(turnpage.isPageflag()==false)//¦pªG½uµ{¨S¦³¶}©l¡A«h¶}©l½uµ{
				    		    {
				    		    	turnpage.setPageflag(true);
				    		    	turnpage.start();
				    		    }
						    	turnpage.setFiftySecond(false);
						    	turnpage.setThirtySecond(false);//±N40¬í³]¬°true¨ä¥L¬°false
						    	turnpage.setFortySecond(true);
						    	sendMessage(WhatMessage.GOTO_MAIN_MENU_VIEW);//¸õÂà¨ì¥D¬É­±
						    }
			    		    break;
						case 2://50¬í¦Û°ÊÂ½®Ñ
							if(Constant.FILE_PATH==null)//¦pªG¨S¦³¿ï®Ñ
				    		    {
				    		    	Toast.makeText
									(
										ReaderActivity.this, 
										"½Ð¿ï¾Ü±z­n¾\Åªªº¤å¥»", 
										Toast.LENGTH_SHORT
									).show();
				    		    	
				    		    }else//¦pªG¤w¸g¿ï®Ñ
				    		    {
				    		    	if(turnpage.isPageflag()==false)//¦pªG½uµ{¨S¦³¶}©l¡A«h¶}©l½uµ{
					    		    {
					    		    	turnpage.setPageflag(true);
					    		    	turnpage.start();
					    		    }
							    	turnpage.setThirtySecond(false);//±N50¬í³]¬°true¨ä¥L¬°false
							    	turnpage.setFortySecond(false);
							    	turnpage.setFiftySecond(true);
							    	sendMessage(WhatMessage.GOTO_MAIN_MENU_VIEW);//¸õÂà¨ì¥D¬É­±
				    		    }
							break;
						case 3://°±¤î¦Û°ÊÂ½­¶
							turnpage.setPageflag(false);//°±¤î½uµ{
							sendMessage(WhatMessage.GOTO_MAIN_MENU_VIEW);//¸õÂà¨ì¥D¬É­±
							break;
						}
					}      			
     		 }
 		   );
 		  dialog=b.create();
 		  break;  
   	case LIST_SET://³]¸m
   			b=new AlertDialog.Builder(this); 
 		  	b.setIcon(R.drawable.m_set);//³]¸m¹Ï¼Ð
 		  	b.setTitle(R.string.setup);//³]¸m¼ÐÃD
 		  	b.setItems(
 			 R.array.setup, 
 			 new DialogInterface.OnClickListener()
     		 {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//³o¸Ì¥[ÂIÀ»¦Cªí¤¤ªº¤º®e²£¥Íµ²ªGªº¥N½X
						switch(which)
						{
						case 0://­I´º­µ¼Ö
							showDialog(BACKGROUND_MUSIC);
							break;
						case 1://­I´º¹Ï¤ù
							showDialog(BACKGROUND_PIC);
							break;
						case 2://¦rÅéÃC¦â
							showDialog(SET_FONT_COLOR);//³]¸m¦rÅéÃC¦â
							break;
						case 3://¦rÅé¤j¤p
							showDialog(SET_FONT_SIZE);//Åã¥Ü¦rÅé¤j¤p¹ï¸Ü®Ø
							break;
						
						}
					}    
     		 }
 		   );
 		  	dialog=b.create();
 		  	break;    
		}
		return dialog;
   }
    //¨C¦¸¼u¥X¹ï¸Ü®Ø®É³Q¦^½Õ¥H°ÊºA§ó·s¹ï¸Ü®Ø¤º®eªº¤èªk
    @Override
    public void onPrepareDialog(int id, final Dialog dialog)
    {
    	//­Y¤£¬Oµ¥«Ý¹ï¸Ü®Ø«hªð¦^
    	switch(id)
    	{
    	   case NAME_INPUT_DIALOG_ID://©m¦W¿é¤J¹ï¸Ü®Ø
    		   //½T©w«ö¶s
    		   Button bjhmcok=(Button)dialog.findViewById(R.id.bjhmcOk);
    		   //¨ú®ø«ö¶s
       		   Button bjhmccancel=(Button)dialog.findViewById(R.id.bjhmcCancle);
       		   //µ¹½T©w«ö¶s²K¥[ºÊÅ¥¾¹
       		   bjhmcok.setOnClickListener
               (
    	          new OnClickListener()
    	          {
    				@Override
    				public void onClick(View v) 
    				{
						if(Constant.FILE_PATH==null)//¦pªG¨S¦³¿ï¾Ü®Ñ¡A¤£¥i¥H¥[®ÑÅÒ
						{
							Toast.makeText(ReaderActivity.this, "½Ð¥ý¿ï¾Ü±z·Q­n¾\Åªªº®Ñ", Toast.LENGTH_SHORT).show();
						}else
						{
							//Àò¨ú¹ï¸Ü®Ø¸Ìªº¤º®e¨Ã¥ÎToastÅã¥Ü
	    					EditText et=(EditText)dialog.findViewById(R.id.etname);
	    					String name=et.getText().toString();
	    					if(name.equals(""))//¦pªG®ÑÅÒ¬°ªÅ
	    					{
	    						Toast.makeText(ReaderActivity.this, "®ÑÅÒ¦W¦r¤£¯à¬°ªÅ", Toast.LENGTH_SHORT).show();
	    					}else//®ÑÅÒ¤£¬°ªÅ
	    					{
	    						try
								{
									//·í«e®ÑÅÒªº¡u¦W¦r¡v©M·í«e®ÑÅÒªº¡u­¶¼Æ¡v¦s¤J¼Æ¾Ú®w
									SQLDBUtil.bookMarkInsert(name,Constant.CURRENT_PAGE);
								}catch(Exception e)
								{
									e.printStackTrace();
								}
	    						//Ãö³¬¹ï¸Ü®Ø
	    						dialog.cancel();
	    					}
						}	
    				}        	  
    	          }
    	        );   
       		    //µ¹¨ú®ø«ö¶s²K¥[ºÊÅ¥¾¹
       		    bjhmccancel.setOnClickListener
	            (
	 	          new OnClickListener()
	 	          {
	 				@Override
	 				public void onClick(View v) 
	 				{	//Ãö³¬¹ï¸Ü®Ø
	 					dialog.cancel();					
	 				}        	  
	 	          }
	 	        );   
    	   break;
    	   case SELECT_BOOKMARK://¿ï¾Ü®ÑÅÒªº¹ï¸Ü®Ø
    		   try
    		   {
    			   //¦b¼Æ¾Ú®w¤¤¨ú¥X©Ò¦³ªº®ÑÅÒ°O¿ý
    			   dataBaseBookMark=SQLDBUtil.getBookmarkList(Constant.FILE_PATH);				   
    			   int i=0;
    			   tempname=new String[dataBaseBookMark.size()];//¼Æ²Õ¤j¤p
    			   temppage=new int[dataBaseBookMark.size()];//®Ñ­¶

    			   for(BookMark dataBookMark:dataBaseBookMark)
    			   {
    				   tempname[i]=dataBookMark.bmname;//Àò¨ú©Ò¦³®ÑÅÒªº¦W¦r
    				   temppage[i]=dataBookMark.page;
    				   i++;					   
    			   }
    		   }catch(Exception e)
    		   {
    			   e.printStackTrace();
    		   }
    		   
			   //¹ï¸Ü®Ø¹ïÀ³ªºÁ`««ª½¤è¦VLinearLayout
       		   	LinearLayout ll=new LinearLayout(ReaderActivity.this);
       			ll.setOrientation(LinearLayout.VERTICAL);		//³]¸m´Â¦V	
       			ll.setGravity(Gravity.CENTER_HORIZONTAL);
       			ll.setBackgroundResource(R.drawable.dialog);
       			
       			//¼ÐÃD¦æªº¤ô¥­LinearLayout
       			LinearLayout lln=new LinearLayout(ReaderActivity.this);
       			lln.setOrientation(LinearLayout.HORIZONTAL);		//³]¸m´Â¦V	
       			lln.setGravity(Gravity.CENTER);//©~¤¤
       			lln.setLayoutParams(new ViewGroup.LayoutParams(200, LayoutParams.WRAP_CONTENT));
       			
       			//¼ÐÃD¦æªº¤å¦r
       			TextView tvTitle=new TextView(ReaderActivity.this);
       			tvTitle.setText(R.string.bookmark_dialog);
       			tvTitle.setTextSize(20);//³]¸m¦rÅé¤j¤p
       			tvTitle.setTextColor(ReaderActivity .this.getResources().getColor(R.color.white));//³]¸m¦rÅéÃC¦â
       			lln.addView(tvTitle);
       			
       			//±N¼ÐÃD¦æ²K¥[¨ìÁ`LinearLayout
       			ll.addView(lln);		
       		   	
       		   	//¬°¹ï¸Ü®Ø¤¤ªº¾ú¥v°O¿ý±ø¥Ø³Ð«ØListView
       		    //ªì©l¤ÆListView
       	        ListView lv=new ListView(this);
       	        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE); 
       	        
       	        //¬°ListView·Ç³Æ¤º®e¾A°t¾¹
       	        BaseAdapter ba=new BaseAdapter()
       	        {
       				@Override
       				public int getCount() {
       					return tempname.length;//Á`¦@´X­Ó¿ï¶µ
       				}

       				@Override
       				public Object getItem(int arg0) { return null; }

       				@Override
       				public long getItemId(int arg0) { return 0; }

       				@Override
       				public View getView(int arg0, View arg1, ViewGroup arg2) {
       					
       					LinearLayout llb=new LinearLayout(ReaderActivity.this);
						llb.setOrientation(LinearLayout.HORIZONTAL);//³]¸m´Â¦V	
						llb.setPadding(5,5,5,5);//³]¸m¥|©P¯d¥Õ
       					
       					//¬°®ÑÅÒ²K¥[¹Ï¤ù
       					ImageView image=new ImageView(ReaderActivity.this);
       					image.setImageDrawable(ReaderActivity.this.getResources().getDrawable(R.drawable.sl_bookmark));
       					image.setScaleType(ImageView.ScaleType.FIT_XY);//«ö·Ó­ì¹Ï¤ñ¨Ò
       					image.setLayoutParams(new Gallery.LayoutParams(30, 30));
       					llb.addView(image);
       					
       					//°ÊºA¥Í¦¨¨C±ø®ÑÅÒ¹ïÀ³ªºTextView
       					TextView tv=new TextView(ReaderActivity.this);
       					tv.setGravity(Gravity.LEFT);
       					tv.setText(tempname[arg0]+"     "+"²Ä"+String.valueOf(temppage[arg0]+1)+"­¶");//³]¸m¤º®e
       					tv.setTextSize(20);//³]¸m¦rÅé¤j¤p
       					tv.setTextColor(ReaderActivity.this.getResources().getColor(R.color.white));//³]¸m¦rÅéÃC¦â
       					tv.setPadding(0,0,0,0);//³]¸m¥|©P¯d¥Õ
       					llb.addView(tv);
       					return llb;
       				}        	
       	        };       
       	        lv.setAdapter(ba);//¬°ListView³]¸m¤º®e¾A°t¾¹
       	        
       	        //³]¸m¿ï¶µ³Q³æÀ»ªººÊÅ¥¾¹
       	        lv.setOnItemClickListener(
       	           new OnItemClickListener()
       	           {
       				@Override
       				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
       						long arg3) {//­«¼g¿ï¶µ³Q³æÀ»¨Æ¥óªº³B²z¤èªk
   					
   						int page=temppage[arg2];//±o¨ì³o±ø°O¿ý¹ïÀ³ªº­¶¸¹
   						readerView.currRR=readerView.currBook.get(page);//¦bhashMap¤¤§ä¨ì¹ïÀ³ªºreaderView.currRR¹ï¹³
   						Constant.CURRENT_LEFT_START=readerView.currRR.leftStart;//°O¿ý·í«eÅª¨ì³Bleftstartªº­È
   						Constant.CURRENT_PAGE=readerView.currRR.pageNo;//°O¿ý·í«eÅª¨ì³Bªºpageªº­È
   						
   						//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
   						readerView.currRR.isLeft=true;
   						readerView.bmLeft=readerView.drawPage(readerView.currRR);
   						readerView.bmRight=readerView.drawPage(readerView.currRR);
   						readerView.repaint();
       					
       					dialog.cancel();//Ãö³¬¹ï¸Ü®Ø
       				}        	   
       	           }
       	        );
       	        //±N¾ú¥v°O¿ý±øªºListView¥[¤JÁ`LinearLayout
	       	    ll.addView(lv);

	       	    lv.setOnItemLongClickListener(
	       	    		new OnItemLongClickListener()
	       	    		{
							@Override
							public boolean onItemLongClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								//®Ú¾Ú·í«eªº®ÑÅÒªº¦W¦r¡A§ä¨ì¹ïÀ³ªº®ÑÅÒªº­¶¸¹,§R°£³o±ø°O¿ý
								deleteOneBookMarkName=tempname[arg2];
								try
								{
									showDialog(DELETE_ONE_BOOKMARK);
									
								}catch(Exception e)
								{
									e.printStackTrace();
								}
								dialog.cancel();//Ãö³¬¹ï¸Ü®Ø
					
								return true;
							}
	       	    		}
	       	    );
	       	     dialog.setContentView(ll); 
    		   break;
    	   case EXIT_READER://°h¥X¹ï¸Ü®Ø
    		   
    		   //¹ï¸Ü®Ø¹ïÀ³ªºÁ`««ª½¤è¦VLinearLayout
      		   	LinearLayout lle=new LinearLayout(ReaderActivity.this);
      			lle.setOrientation(LinearLayout.VERTICAL);		//³]¸m´Â¦V	
      			lle.setGravity(Gravity.CENTER_HORIZONTAL);
      			lle.setBackgroundResource(R.drawable.dialog);
      			
      			//¼ÐÃD¦æªº¤ô¥­LinearLayout
      			LinearLayout llt=new LinearLayout(ReaderActivity.this);
      			llt.setOrientation(LinearLayout.HORIZONTAL);		//³]¸m´Â¦V	
      			llt.setGravity(Gravity.CENTER);//©~¤¤
      			llt.setLayoutParams(new ViewGroup.LayoutParams(200, LayoutParams.WRAP_CONTENT));
      			
      			//¼ÐÃD¦æªº¤å¦r
      			TextView eTitle=new TextView(ReaderActivity.this);
      			eTitle.setText(R.string.exit_bookmark);
      			eTitle.setTextSize(20);//³]¸m¦rÅé¤j¤p
      			eTitle.setTextColor(ReaderActivity .this.getResources().getColor(R.color.white));//³]¸m¦rÅéÃC¦â
      			llt.addView(eTitle);
      			
      			//±N¼ÐÃD¦æ²K¥[¨ìÁ`LinearLayout
      			lle.addView(llt);
      			
      			LinearLayout lleb=new LinearLayout(ReaderActivity.this);
      			lleb.setOrientation(LinearLayout.HORIZONTAL);//¤ô¥­¤è¦V
      			lleb.setLayoutParams(new ViewGroup.LayoutParams(200, LayoutParams.WRAP_CONTENT));
      			lleb.setGravity(Gravity.CENTER);

	       	    //½T©w«ö¶s
	       	    Button eok=new Button(ReaderActivity.this);//²MªÅ®ÑÅÒ«ö¶s
	       	    eok.setText(R.string.ok);//³]¸m¡u«ö¶s¡vªº¦W¦r
	       	    eok.setTextSize(18);//³]¸m¦rÅé¤j¤p
	       	    eok.setWidth(100);
	       	    eok.setHeight(20);
	       	    eok.setGravity(Gravity.CENTER);
	       	    eok.setOnClickListener(
	       	    		 new OnClickListener()
	       	    		 {
							public void onClick(View v) {
								dialog.cancel();//¨ú®ø¹ï¸Ü®Ø
								savePreference();//·í«e®Ñ­¶°h¥X®É,«O¦s²{³õ
								saveCurrentData();//·í«ehashMapªº«H®§¦s¤J¼Æ¾Ú®w
								System.exit(0);	
							} 
	       	    		 } 
	       	     );
	       	    lleb.addView(eok);//¥[¤J¨ìlinearLayout¤¤
	       	     //¨ú®ø«ö¶s
	       	    Button eCancel=new Button(ReaderActivity.this);
	       	    eCancel.setText(R.string.cancel);//³]¸m«ö¶sªº¦W¦r
	       	    eCancel.setTextSize(18);
	       	    eCancel.setWidth(100);
	       	    eCancel.setHeight(20);
	       	    eCancel.setGravity(Gravity.CENTER);
	            eCancel.setOnClickListener(
	      	    		 new OnClickListener()
	       	    		 {
							public void onClick(View v) {
								dialog.cancel();//¨ú®ø¹ï¸Ü®Ø
							}
	       	    		 }
	       	     );
	       	    lleb.addView(eCancel);
	       	    lle.addView(lleb);
	       	    dialog.setContentView(lle);
    		   break;
    	   case DELETE_ONE_BOOKMARK://§R°£¤@±ø®ÑÅÒ°O¿ý¹ï¸Ü®Ø
    		   
    		   //¹ï¸Ü®Ø¹ïÀ³ªºÁ`««ª½¤è¦VLinearLayout
     		   	LinearLayout lld=new LinearLayout(ReaderActivity.this);
     			lld.setOrientation(LinearLayout.VERTICAL);		//³]¸m´Â¦V	
     			lld.setGravity(Gravity.CENTER_HORIZONTAL);
     			lld.setBackgroundResource(R.drawable.dialog);
     			
     			//¼ÐÃD¦æªº¤ô¥­LinearLayout
     			LinearLayout lldt=new LinearLayout(ReaderActivity.this);
     			lldt.setOrientation(LinearLayout.HORIZONTAL);		//³]¸m´Â¦V	
     			lldt.setGravity(Gravity.CENTER);//©~¤¤
     			lldt.setLayoutParams(new ViewGroup.LayoutParams(240, LayoutParams.WRAP_CONTENT));
     			
     			//¼ÐÃD¦æªº¤å¦r
     			TextView deTitle=new TextView(ReaderActivity.this);
     			deTitle.setText(R.string.delete_onebookmark);
     			deTitle.setTextSize(20);//³]¸m¦rÅé¤j¤p
     			deTitle.setTextColor(ReaderActivity .this.getResources().getColor(R.color.white));//³]¸m¦rÅéÃC¦â
     			lldt.addView(deTitle);
     			
     			//±N¼ÐÃD¦æ²K¥[¨ìÁ`LinearLayout
     			lld.addView(lldt);
     			
     			LinearLayout lldeb=new LinearLayout(ReaderActivity.this);
     			lldeb.setOrientation(LinearLayout.HORIZONTAL);//¤ô¥­¤è¦V
     			lldeb.setLayoutParams(new ViewGroup.LayoutParams(200, LayoutParams.WRAP_CONTENT));
     			lldeb.setGravity(Gravity.CENTER);

	       	    //½T©w«ö¶s
	       	    Button deok=new Button(ReaderActivity.this);//²MªÅ®ÑÅÒ«ö¶s
	       	    deok.setText(R.string.ok);//³]¸m¡u«ö¶s¡vªº¦W¦r
	       	    deok.setTextSize(18);//³]¸m¦rÅé¤j¤p
	       	    deok.setWidth(100);
	       	    deok.setHeight(20);
	       	    deok.setGravity(Gravity.CENTER);
	       	    deok.setOnClickListener(
	       	    		 new OnClickListener()
	       	    		 {
							public void onClick(View v) {	
								try
								{//¼Æ¾Ú®w¤¤§R°£¤@±ø®ÑÅÒ°O¿ý
									SQLDBUtil.deleteOneBookMark(deleteOneBookMarkName);
								}catch(Exception e)
								{
									e.printStackTrace();
								}
								dialog.cancel();//¨ú®ø¹ï¸Ü®Ø
							} 
	       	    		 } 
	       	     );
	       	    lldeb.addView(deok);//¥[¤J¨ìlinearLayout¤¤
	       	     //¨ú®ø«ö¶s
	       	    Button deCancel=new Button(ReaderActivity.this);
	       	    deCancel.setText(R.string.cancel);//³]¸m«ö¶sªº¦W¦r
	       	    deCancel.setTextSize(18);
	       	    deCancel.setWidth(100);
	       	    deCancel.setHeight(20);
	       	    deCancel.setGravity(Gravity.CENTER);
	            deCancel.setOnClickListener(
	      	    		 new OnClickListener()
	       	    		 {
							public void onClick(View v) {
								dialog.cancel();//¨ú®ø¹ï¸Ü®Ø
								
								showDialog(SELECT_BOOKMARK);//Åã¥Ü¿ï¾Ü®ÑÅÒªº¹ï¸Ü®Ø
									
							}
	       	    		 }
	       	     );
	            lldeb.addView(deCancel);
	       	    lld.addView(lldeb);
	       	    dialog.setContentView(lld);
   		   break;
    	   case DELETE_ALL_BOOKMARK:
    		   
    		 //¹ï¸Ü®Ø¹ïÀ³ªºÁ`««ª½¤è¦VLinearLayout
    		   	LinearLayout lla=new LinearLayout(ReaderActivity.this);
    			lla.setOrientation(LinearLayout.VERTICAL);		//³]¸m´Â¦V	
    			lla.setGravity(Gravity.CENTER_HORIZONTAL);
    			lla.setBackgroundResource(R.drawable.dialog);
    			
    			//¼ÐÃD¦æªº¤ô¥­LinearLayout
    			LinearLayout llat=new LinearLayout(ReaderActivity.this);
    			llat.setOrientation(LinearLayout.HORIZONTAL);		//³]¸m´Â¦V	
    			llat.setGravity(Gravity.CENTER);//©~¤¤
    			llat.setLayoutParams(new ViewGroup.LayoutParams(200, LayoutParams.WRAP_CONTENT));
    			
    			//¼ÐÃD¦æªº¤å¦r
    			TextView deaTitle=new TextView(ReaderActivity.this);
    			deaTitle.setText(R.string.delete_allbookmark);
    			deaTitle.setTextSize(20);//³]¸m¦rÅé¤j¤p
    			deaTitle.setTextColor(ReaderActivity .this.getResources().getColor(R.color.white));//³]¸m¦rÅéÃC¦â
    			llat.addView(deaTitle);
    			
    			//±N¼ÐÃD¦æ²K¥[¨ìÁ`LinearLayout
    			lla.addView(llat);
    			
    			LinearLayout lldeab=new LinearLayout(ReaderActivity.this);
    			lldeab.setOrientation(LinearLayout.HORIZONTAL);//¤ô¥­¤è¦V
    			lldeab.setLayoutParams(new ViewGroup.LayoutParams(200, LayoutParams.WRAP_CONTENT));
    			lldeab.setGravity(Gravity.CENTER);

	       	    //½T©w«ö¶s
	       	    Button deaok=new Button(ReaderActivity.this);//²MªÅ®ÑÅÒ«ö¶s
	       	    deaok.setText(R.string.ok);//³]¸m¡u«ö¶s¡vªº¦W¦r
	       	    deaok.setTextSize(18);//³]¸m¦rÅé¤j¤p
	       	    deaok.setWidth(100);
	       	    deaok.setHeight(20);
	       	    deaok.setGravity(Gravity.CENTER);
	       	    deaok.setOnClickListener(
	       	    		 new OnClickListener()
	       	    		 {
							public void onClick(View v) {	
						
								try
								{//²MªÅ·í«e³o¥»®Ñªº¥þ³¡®ÑÅÒ
									SQLDBUtil.deleteAllBookMark(Constant.FILE_PATH);
								}catch(Exception e)
								{
									e.printStackTrace();
								}

								dialog.cancel();//¨ú®ø¹ï¸Ü®Ø
							} 
	       	    		 } 
	       	     );
	       	    lldeab.addView(deaok);//¥[¤J¨ìlinearLayout¤¤
	       	     //¨ú®ø«ö¶s
	       	    Button deaCancel=new Button(ReaderActivity.this);
	       	    deaCancel.setText(R.string.cancel);//³]¸m«ö¶sªº¦W¦r
	       	    deaCancel.setTextSize(18);
	       	    deaCancel.setWidth(100);
	       	    deaCancel.setHeight(20);
	       	    deaCancel.setGravity(Gravity.CENTER);
	            deaCancel.setOnClickListener(
	      	    		 new OnClickListener()
	       	    		 {
							public void onClick(View v) {
								dialog.cancel();//¨ú®ø¹ï¸Ü®Ø
							}
	       	    		 }
	       	     );
	            lldeab.addView(deaCancel);
	       	    lla.addView(lldeab);
	       	    dialog.setContentView(lla);
    		   break;
    	   case BACKGROUND_MUSIC://§G§½­I´º­µ¼Ö¹ï¸Ü®Ø
    		   setBackgroundMusicDialog(dialog);
    		   break;
    	   case BACKGROUND_PIC://­I´º¹Ï¤ù
    		   setBackgroundPic(dialog);
    		   break;
    	   case SET_FONT_SIZE://¦Û¤v§G§½¦rÅé¤j¤p¹ï¸Ü®Ø
    		   setFontSize(dialog);//½Õ¥ÎsetFontSize¤èªkÅã¥Ü§G§½ªºdialog
    		   break;
    	   case SET_FONT_COLOR://§G§½¦rÅéªºÃC¦â
    		   setFontColor(dialog);//§G§½¦rÅéªºÃC¦â
    		   break;
    		   
    	}
    	
    }

	//¬d§ä®Ñ¥Ø
	public void searchBook()
	{

		goToSearchBooklist();
		
		lv=(ListView)ReaderActivity.this.findViewById(R.id.flist);//Àò¨úListView±±¥ó¹ï¹³
		root_b=(Button)findViewById(R.id.Button01);
	    back_b=(Button)findViewById(R.id.Button02);
		final File[] files=lvutills.getFiles(sdcardPath);//Àò¨ú®Ú¸`ÂI¤å¥ó¦Cªí 
		lvutills.intoListView(files,lv);//±N¦U­Ó¤å¥ó²K¥[¨ìListView¦Cªí¤¤
		root_b.setOnClickListener(
				//OnClickListener¬°Viewªº¤º³¡±µ¤f¡A¨ä¹ê²{ªÌ­t³dºÊÅ¥¹«¼ÐÂIÀ»¨Æ¥ó
	           new View.OnClickListener(){ 
	        	   public void onClick(View v){
	        		   lvutills.intoListView(files,lv);
	        	   }}); 
		back_b.setOnClickListener(
				new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						if(lvutills.currentPath.equals("/sdcard"))
						{
							Toast.makeText
							(
								ReaderActivity.this,
								"¤w¸g¨ì®Ú¥Ø¿ý¤F", 
								Toast.LENGTH_SHORT
							).show();
						}else
						{
								File cf=new File(lvutills.currentPath);//Àò¨ú·í«e¤å¥ó¦Cªíªº¸ô®|¹ïÀ³ªº¤å¥ó
								cf=cf.getParentFile();//Àò¨ú¤÷¥Ø¿ý¤å¥ó
								lvutills.currentPath=cf.getPath();//°O¿ý·í«e¤å¥ó¦Cªí¸ô®|
								lvutills.intoListView(lvutills.getFiles(lvutills.currentPath),lv);	
						}
						
					}});
	}

	public void setBackgroundMusicDialog(final Dialog dialog)
	{
		//¹ï¸Ü®Ø¹ïÀ³ªºÁ`««ª½¤è¦VLinearLayout
	   	LinearLayout ll=new LinearLayout(ReaderActivity.this);
		ll.setOrientation(LinearLayout.VERTICAL);		//³]¸m´Â¦V	
		ll.setGravity(Gravity.CENTER_HORIZONTAL);
		ll.setBackgroundResource(R.drawable.dialog);
		
		//¼ÐÃD¦æªº¤ô¥­LinearLayout
		LinearLayout lln=new LinearLayout(ReaderActivity.this);
		lln.setOrientation(LinearLayout.HORIZONTAL);		//³]¸m´Â¦V	
		lln.setGravity(Gravity.CENTER);//©~¤¤
		lln.setLayoutParams(new ViewGroup.LayoutParams(200, LayoutParams.WRAP_CONTENT));
		
		//¼ÐÃD¦æªº¤å¦r
		TextView tvTitle=new TextView(ReaderActivity.this);
		tvTitle.setText(R.string.music_name);
		tvTitle.setTextSize(20);//³]¸m¦rÅé¤j¤p
		tvTitle.setTextColor(ReaderActivity .this.getResources().getColor(R.color.white));//³]¸m¦rÅéÃC¦â
		lln.addView(tvTitle);
		
		//±N¼ÐÃD¦æ²K¥[¨ìÁ`LinearLayout
		ll.addView(lln);		
	   	
	   	//¬°¹ï¸Ü®Ø¤¤ªº¾ú¥v°O¿ý±ø¥Ø³Ð«ØListView
	    //ªì©l¤ÆListView
        ListView lv=new ListView(this);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE); 
        
        //¬°ListView·Ç³Æ¤º®e¾A°t¾¹
        BaseAdapter ba=new BaseAdapter()
        {
			@Override
			public int getCount() {
				return musicname.length;//Á`¦@´X­Ó¿ï¶µ
			}

			@Override
			public Object getItem(int arg0) { return null; }

			@Override
			public long getItemId(int arg0) { return 0; }

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				
			LinearLayout llb=new LinearLayout(ReaderActivity.this);
			llb.setOrientation(LinearLayout.HORIZONTAL);//³]¸m´Â¦V	
			llb.setPadding(5,5,5,5);//³]¸m¥|©P¯d¥Õ
				
				//¬°®ÑÅÒ²K¥[¹Ï¤ù
				ImageView image=new ImageView(ReaderActivity.this);
				image.setImageDrawable(ReaderActivity.this.getResources().getDrawable(R.drawable.sl_music));//³]©w¹Ï¤ù
				image.setScaleType(ImageView.ScaleType.FIT_XY);//«ö·Ó­ì¹Ï¤ñ¨Ò
				image.setLayoutParams(new Gallery.LayoutParams(30, 30));
				llb.addView(image);
				
				//°ÊºA¥Í¦¨¨C±ø®ÑÅÒ¹ïÀ³ªºTextView
				TextView tv=new TextView(ReaderActivity.this);
				tv.setGravity(Gravity.LEFT);
				tv.setText(ReaderActivity.this.getResources().getString(musicname[arg0]));//³]¸m¤º®e
				tv.setTextSize(20);//³]¸m¦rÅé¤j¤p
				tv.setTextColor(ReaderActivity.this.getResources().getColor(R.color.white));//³]¸m¦rÅéÃC¦â
				tv.setPadding(12,0,0,0);//³]¸m¥|©P¯d¥Õ
				llb.addView(tv);
				return llb;
			}        	
        };       
        lv.setAdapter(ba);//¬°ListView³]¸m¤º®e¾A°t¾¹
        
        //³]¸m¿ï¶µ³Q³æÀ»ªººÊÅ¥¾¹
        lv.setOnItemClickListener(
           new OnItemClickListener()
           {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {//­«¼g¿ï¶µ³Q³æÀ»¨Æ¥óªº³B²z¤èªk
				//®Ú¾Ú·í«eªº®ÑÅÒªº¦W¦r¡A§ä¨ì¹ïÀ³ªº®ÑÅÒªº­¶¸¹¡A®Ú¾Ú­¶¸¹½T©w¤Á´«¨ì¨º¤@­¶

				switch(arg2)
				{
					case 0:
						if(ReaderActivity.this.mp==null||Constant.I==R.raw.mh)
						{
							Constant.I=R.raw.gsls;
							ReaderActivity.this.mp=MediaPlayer.create(ReaderActivity.this, R.raw.gsls);
							ReaderActivity.this.mp.setLooping(true);
							ReaderActivity.this.mp.start();
							
						}else{
							ReaderActivity.this.mp.release();
							ReaderActivity.this.mp=null;
						}
						
						ReaderActivity.this.goToReaderView();
						
						break;
					case 1:

						if(ReaderActivity.this.mp==null||Constant.I==R.raw.gsls)
						{  	
							Constant.I=R.raw.mh;
							ReaderActivity.this.mp=MediaPlayer.create(ReaderActivity.this, R.raw.mh);
							ReaderActivity.this.mp.setLooping(true);
							ReaderActivity.this.mp.start();
						}else{
							ReaderActivity.this.mp.release();
							ReaderActivity.this.mp=null;
							}
						
						ReaderActivity.this.goToReaderView();
						
						break;
				}
				dialog.cancel();//Ãö³¬¹ï¸Ü®Ø

				//ªì©l¤Æ¨ì·í«e¤å¥ó²ÄX­¶
				readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);

				//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
				readerView.bmLeft=readerView.drawPage(readerView.currRR);
				readerView.bmRight=readerView.drawPage(readerView.currRR);
				readerView.repaint();
			}        	   
           }
        );
        //±N¾ú¥v°O¿ý±øªºListView¥[¤JÁ`LinearLayout
        ll.addView(lv);
        dialog.setContentView(ll); 	
		
	}
	//³]¸m¦rÅéÃC¦â
	public void setFontSize(final Dialog dialog)
	{
		//¹ï¸Ü®Ø¹ïÀ³ªºÁ`««ª½¤è¦VLinearLayout
	   	LinearLayout ll=new LinearLayout(ReaderActivity.this);
		ll.setOrientation(LinearLayout.VERTICAL);		//³]¸m´Â¦V	
		ll.setGravity(Gravity.CENTER_HORIZONTAL);
		ll.setBackgroundResource(R.drawable.dialog);
		
		//¼ÐÃD¦æªº¤ô¥­LinearLayout
		LinearLayout lln=new LinearLayout(ReaderActivity.this);
		lln.setOrientation(LinearLayout.HORIZONTAL);		//³]¸m´Â¦V	
		lln.setGravity(Gravity.CENTER);//©~¤¤
		lln.setLayoutParams(new ViewGroup.LayoutParams(200, LayoutParams.WRAP_CONTENT));
		
		//¼ÐÃD¦æªº¤å¦r
		TextView tvTitle=new TextView(ReaderActivity.this);
		tvTitle.setText(R.string.font_size);
		tvTitle.setTextSize(20);//³]¸m¦rÅé¤j¤p
		tvTitle.setTextColor(ReaderActivity .this.getResources().getColor(R.color.white));//³]¸m¦rÅéÃC¦â
		lln.addView(tvTitle);
		
		//±N¼ÐÃD¦æ²K¥[¨ìÁ`LinearLayout
		ll.addView(lln);		
	   	
	   	//¬°¹ï¸Ü®Ø¤¤ªº¾ú¥v°O¿ý±ø¥Ø³Ð«ØListView
	    //ªì©l¤ÆListView
        ListView lv=new ListView(this);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE); 
        
        //¬°ListView·Ç³Æ¤º®e¾A°t¾¹
        BaseAdapter ba=new BaseAdapter()
        {
			@Override
			public int getCount() {
				return fontSizeIds.length;//Á`¦@´X­Ó¿ï¶µ
			}

			@Override
			public Object getItem(int arg0) { return null; }

			@Override
			public long getItemId(int arg0) { return 0; }

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {
				
			LinearLayout llb=new LinearLayout(ReaderActivity.this);
			llb.setOrientation(LinearLayout.HORIZONTAL);//³]¸m´Â¦V	
			llb.setPadding(5,5,5,5);//³]¸m¥|©P¯d¥Õ
				
				//¬°®ÑÅÒ²K¥[¹Ï¤ù
				ImageView image=new ImageView(ReaderActivity.this);
				image.setImageDrawable(ReaderActivity.this.getResources().getDrawable(fontSizeDrawable[arg0]));//³]©w¹Ï¤ù
				image.setScaleType(ImageView.ScaleType.FIT_XY);//«ö·Ó­ì¹Ï¤ñ¨Ò
				image.setLayoutParams(new Gallery.LayoutParams(30, 30));
				llb.addView(image);
				
				//°ÊºA¥Í¦¨¨C±ø®ÑÅÒ¹ïÀ³ªºTextView
				TextView tv=new TextView(ReaderActivity.this);
				tv.setGravity(Gravity.LEFT);
				tv.setText(ReaderActivity.this.getResources().getString(fontSizeIds[arg0]));//³]¸m¤º®e
				tv.setTextSize(20);//³]¸m¦rÅé¤j¤p
				tv.setTextColor(ReaderActivity.this.getResources().getColor(R.color.white));//³]¸m¦rÅéÃC¦â
				tv.setPadding(0,0,0,0);//³]¸m¥|©P¯d¥Õ
				llb.addView(tv);
				return llb;
			}        	
        };       
        lv.setAdapter(ba);//¬°ListView³]¸m¤º®e¾A°t¾¹
        
        //³]¸m¿ï¶µ³Q³æÀ»ªººÊÅ¥¾¹
        lv.setOnItemClickListener(
           new OnItemClickListener()
           {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {//­«¼g¿ï¶µ³Q³æÀ»¨Æ¥óªº³B²z¤èªk
				
				switch(arg2)
				{
					case 0:
						if(Constant.TEXT_SIZE!=16)//¦pªG·í«e¦rÅé¤j¤p¤£µ¥©ó­n´«¨ìªº¦rÅé¤j¤p
						{
							//ÅÜ´«¦rÅé¤j¤p
							Constant.TEXT_SIZE=16;
							Constant.TEXT_SPACE_BETWEEN_CN=16;
							Constant.TEXT_SPACE_BETWEEN_EN=8;							
							//µ¹±`¶qÃþ¤¤ªº¦Û¾AÀ³«Ì¹õªºÅÜ¶q­«·s½á­È
					        Constant.changeRatio();
							
					        updataBookMarkAndHashMap();//§ó·shashMap						
						}else//¦pªG¬Ûµ¥
						{
							//¤£°µÅÜ´«
						}
						
						break;
					case 1:
						if(Constant.TEXT_SIZE!=24)//¦pªG·í«e¦rÅé¤j¤p¤£µ¥©ó­n´«¨ìªº¦rÅé¤j¤p
						{
							//ÅÜ´«¦rÅé¤j¤p
							Constant.TEXT_SIZE=24;
							Constant.TEXT_SPACE_BETWEEN_CN=24;
							Constant.TEXT_SPACE_BETWEEN_EN=12;						
							//µ¹±`¶qÃþ¤¤ªº¦Û¾AÀ³«Ì¹õªºÅÜ¶q­«·s½á­È
					        Constant.changeRatio();
							
					        updataBookMarkAndHashMap();//§ó·shashMap						
						}else//¦pªG¬Ûµ¥
						{
							//¤£°µÅÜ´«
						}
						break;
					case 2:
						if(Constant.TEXT_SIZE!=32)//¦pªG·í«e¦rÅé¤j¤p¤£µ¥©ó­n´«¨ìªº¦rÅé¤j¤p
						{
							//ÅÜ´«¦rÅé¤j¤p
							Constant.TEXT_SIZE=32;
							Constant.TEXT_SPACE_BETWEEN_CN=32;
							Constant.TEXT_SPACE_BETWEEN_EN=16;
							
							//µ¹±`¶qÃþ¤¤ªº¦Û¾AÀ³«Ì¹õªºÅÜ¶q­«·s½á­È
					        Constant.changeRatio();
							
					        updataBookMarkAndHashMap();//§ó·shashMap
					
						}else//¦pªG¬Ûµ¥
						{
							//¤£°µÅÜ´«
						}
						break;
				}
				dialog.cancel();//Ãö³¬¹ï¸Ü®Ø


				//ªì©l¤Æ¨ì·í«e¤å¥ó²ÄX­¶
				readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);

				//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
				readerView.bmLeft=readerView.drawPage(readerView.currRR);
				readerView.bmRight=readerView.drawPage(readerView.currRR);
				readerView.repaint();
			}        	   
           }
        );
        //±N¾ú¥v°O¿ý±øªºListView¥[¤JÁ`LinearLayout
        ll.addView(lv);
        dialog.setContentView(ll); 
	
	}
	
	//­I´º¹Ï¤ù
	public void setBackgroundPic(final Dialog dialog)
	{
		Button bg_fhzl=(Button)dialog.findViewById(R.id.bg_fhzl);
		Button bg_lsct=(Button)dialog.findViewById(R.id.bg_lsct);
		
		Button bg_sjzx=(Button)dialog.findViewById(R.id.bg_sjzx);
		Button bg_ssnh=(Button)dialog.findViewById(R.id.bg_ssnh);
		
		Button bg_wffm=(Button)dialog.findViewById(R.id.bg_wffm);
		Button bg_ygmm=(Button)dialog.findViewById(R.id.bg_ygmm);
		
		
		
		bg_fhzl.setOnClickListener
        (
	          new OnClickListener()
	          {
				@Override
				public void onClick(View v) 
				{
					Constant.BITMAP=drawableIds[0];
					
					readerView.bmBack=PicLoadUtil.LoadBitmap(readerView.getResources(), BITMAP);//¦Û¾AÀ³«Ì¹õªº­I´º¹Ï¤ù
					readerView.bmBack=PicLoadUtil.scaleToFit(readerView.bmBack, PAGE_WIDTH, PAGE_HEIGHT);
					
					//ªì©l¤Æ¨ì·í«e¤å¥ó²ÄX­¶
					readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);
					//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
					readerView.bmLeft=readerView.drawPage(readerView.currRR);
					readerView.bmRight=readerView.drawPage(readerView.currRR);
					readerView.repaint();
 					dialog.cancel();
 						
				}        	  
	          }
	        );   
		
		bg_lsct.setOnClickListener
         (
	          new OnClickListener()
	          {
				@Override
				public void onClick(View v) 
				{	
					Constant.BITMAP=drawableIds[1];
					
					readerView.bmBack=PicLoadUtil.LoadBitmap(readerView.getResources(), BITMAP);//¦Û¾AÀ³«Ì¹õªº­I´º¹Ï¤ù
					readerView.bmBack=PicLoadUtil.scaleToFit(readerView.bmBack, PAGE_WIDTH, PAGE_HEIGHT);
					
					readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);

					//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
					readerView.bmLeft=readerView.drawPage(readerView.currRR);
					readerView.bmRight=readerView.drawPage(readerView.currRR);
					readerView.repaint();
					//Ãö³¬¹ï¸Ü®Ø
					dialog.cancel();					
				}        	  
	          }
	        );   
		
		bg_sjzx.setOnClickListener
        (
	          new OnClickListener()
	          {
				@Override
				public void onClick(View v) 
				{
					Constant.BITMAP=drawableIds[2];
					
					readerView.bmBack=PicLoadUtil.LoadBitmap(readerView.getResources(), BITMAP);//¦Û¾AÀ³«Ì¹õªº­I´º¹Ï¤ù
					readerView.bmBack=PicLoadUtil.scaleToFit(readerView.bmBack, PAGE_WIDTH, PAGE_HEIGHT);
					
					readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);

					//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
					readerView.bmLeft=readerView.drawPage(readerView.currRR);
					readerView.bmRight=readerView.drawPage(readerView.currRR);
					readerView.repaint();
 					dialog.cancel();
 						
				}        	  
	          }
	        );   
		
		bg_ssnh.setOnClickListener
         (
	          new OnClickListener()
	          {
				@Override
				public void onClick(View v) 
				{
					Constant.BITMAP=drawableIds[3];
					
					readerView.bmBack=PicLoadUtil.LoadBitmap(readerView.getResources(), BITMAP);//¦Û¾AÀ³«Ì¹õªº­I´º¹Ï¤ù
					readerView.bmBack=PicLoadUtil.scaleToFit(readerView.bmBack, PAGE_WIDTH, PAGE_HEIGHT);
					
					readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);

					//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
					readerView.bmLeft=readerView.drawPage(readerView.currRR);
					readerView.bmRight=readerView.drawPage(readerView.currRR);
					readerView.repaint();
					//Ãö³¬¹ï¸Ü®Ø
					dialog.cancel();					
				}        	  
	          }
	        );   
		
		bg_wffm.setOnClickListener
        (
	          new OnClickListener()
	          {
				@Override
				public void onClick(View v) 
				{
					Constant.BITMAP=drawableIds[4];
					
					readerView.bmBack=PicLoadUtil.LoadBitmap(readerView.getResources(), BITMAP);//¦Û¾AÀ³«Ì¹õªº­I´º¹Ï¤ù
					readerView.bmBack=PicLoadUtil.scaleToFit(readerView.bmBack, PAGE_WIDTH, PAGE_HEIGHT);
					
					readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);

					//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
					readerView.bmLeft=readerView.drawPage(readerView.currRR);
					readerView.bmRight=readerView.drawPage(readerView.currRR);
					readerView.repaint();
 					dialog.cancel();
 						
				}        	  
	          }
	        );   
		
		bg_ygmm.setOnClickListener
         (
	          new OnClickListener()
	          {
				@Override
				public void onClick(View v) 
				{

					Constant.BITMAP=drawableIds[5];

					readerView.bmBack=PicLoadUtil.LoadBitmap(readerView.getResources(), BITMAP);//¦Û¾AÀ³«Ì¹õªº­I´º¹Ï¤ù
					readerView.bmBack=PicLoadUtil.scaleToFit(readerView.bmBack, PAGE_WIDTH, PAGE_HEIGHT);

					readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);

					//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
					readerView.bmLeft=readerView.drawPage(readerView.currRR);
					readerView.bmRight=readerView.drawPage(readerView.currRR);
					readerView.repaint();
					//Ãö³¬¹ï¸Ü®Ø
					dialog.cancel();					
				}        	  
	          }
	        );   	
	}
	
	
	
	//³]¸m¦rÅéÃC¦â
	public void setFontColor(final Dialog dialog)
	{
		Button black=(Button)dialog.findViewById(R.id.tc_black);
		Button blue=(Button)dialog.findViewById(R.id.tc_blue);
		
		Button gray=(Button)dialog.findViewById(R.id.tc_gray);
		Button green=(Button)dialog.findViewById(R.id.tc_green);
		
		Button purper=(Button)dialog.findViewById(R.id.tc_purper);
		Button red=(Button)dialog.findViewById(R.id.tc_red);
		
		Button white=(Button)dialog.findViewById(R.id.tc_white);
		Button yellow=(Button)dialog.findViewById(R.id.tc_yellow);
		black.setOnClickListener
        (
	          new OnClickListener()
	          {
				@Override
				public void onClick(View v) 
				{
					Constant.COLOR=0xff000000;
					//ªì©l¤Æ¨ì·í«e¤å¥ó²ÄX­¶
					readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);

					//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
					readerView.bmLeft=readerView.drawPage(readerView.currRR);
					readerView.bmRight=readerView.drawPage(readerView.currRR);
					readerView.repaint();
 					dialog.cancel();
 						
				}        	  
	          }
	        );   
		
		blue.setOnClickListener
         (
	          new OnClickListener()
	          {
				@Override
				public void onClick(View v) 
				{
					Constant.COLOR=0xff0000ff;
					readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);

					//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
					readerView.bmLeft=readerView.drawPage(readerView.currRR);
					readerView.bmRight=readerView.drawPage(readerView.currRR);
					readerView.repaint();
					
					//Ãö³¬¹ï¸Ü®Ø
					dialog.cancel();					
				}        	  
	          }
	        );   
		
		gray.setOnClickListener
        (
	          new OnClickListener()
	          {
				@Override
				public void onClick(View v) 
				{
					Constant.COLOR=0xff5b5b5b;
					
					readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);

					//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
					readerView.bmLeft=readerView.drawPage(readerView.currRR);
					readerView.bmRight=readerView.drawPage(readerView.currRR);
					readerView.repaint();

 						dialog.cancel();
 						
				}        	  
	          }
	        );   
		
		green.setOnClickListener
         (
	          new OnClickListener()
	          {
				@Override
				public void onClick(View v) 
				{
					Constant.COLOR=0xff00ff00;
					readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);

					//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
					readerView.bmLeft=readerView.drawPage(readerView.currRR);
					readerView.bmRight=readerView.drawPage(readerView.currRR);
					readerView.repaint();

					//Ãö³¬¹ï¸Ü®Ø
					dialog.cancel();					
				}        	  
	          }
	        );   
		
		purper.setOnClickListener
        (
	          new OnClickListener()
	          {
				@Override
				public void onClick(View v) 
				{
					Constant.COLOR=0xff930093;
					
					readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);

					//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
					readerView.bmLeft=readerView.drawPage(readerView.currRR);
					readerView.bmRight=readerView.drawPage(readerView.currRR);
					readerView.repaint();

 						dialog.cancel();
 						
				}        	  
	          }
	        );   
		
		red.setOnClickListener
         (
	          new OnClickListener()
	          {
				@Override
				public void onClick(View v) 
				{
					Constant.COLOR=0xffff0000;
					readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);

					//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
					readerView.bmLeft=readerView.drawPage(readerView.currRR);
					readerView.bmRight=readerView.drawPage(readerView.currRR);
					readerView.repaint();

					//Ãö³¬¹ï¸Ü®Ø
					dialog.cancel();					
				}        	  
	          }
	        );   
		
		yellow.setOnClickListener
        (
	          new OnClickListener()
	          {
				@Override
				public void onClick(View v) 
				{
					Constant.COLOR=0xffffff00;
					
					readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);

					//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
					readerView.bmLeft=readerView.drawPage(readerView.currRR);
					readerView.bmRight=readerView.drawPage(readerView.currRR);
					readerView.repaint();

 						dialog.cancel();
				}        	  
	          }
	        );   
		
		white.setOnClickListener
         (
	          new OnClickListener()
	          {
				@Override
				public void onClick(View v) 
				{
					Constant.COLOR=0xffffffff;
					readerView.currRR=new ReadRecord(CURRENT_LEFT_START,0,CURRENT_PAGE);

					//Ã¸»s¥ª¥k¨â´T¹Ï¤ù
					readerView.bmLeft=readerView.drawPage(readerView.currRR);
					readerView.bmRight=readerView.drawPage(readerView.currRR);
					readerView.repaint();

					//Ãö³¬¹ï¸Ü®Ø
					dialog.cancel();					
				}        	  
	          }
	        );   
	}
	
	
	public void downTxt()
	{
		dl=new DownLoad("txt_list.txt",ReaderActivity.this);
		dl.lv.setOnItemClickListener(
				new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
							long arg3) {//­«¼g¿ï¶µ³Q³æÀ»¨Æ¥óªº³B²z¤èªk
						String s=dl.txtName[arg2*2+1];
						dl.downFile(Constant.ADD_PRE+s,"/",s);
					} });
	}
	//¦VHandlerµo°e«H®§ªº¤èªk
    public void sendMessage(int what)
    {
    	Message msg = myHandler.obtainMessage(what); 
    	myHandler.sendMessage(msg);
    }  
    //¦V¦U­Ó¬É­±¸õÂàªº¤èªk
	public void goToSearchBooklist()
    {
    	setContentView(R.layout.searchbook_list);
    	curr=WhichView.SEARCHBOOK_LIST;
    }
	
	public void goToReaderView()
	{
		if(readerView==null)
    	{
			readerView=new ReaderView(this);
    	}
    	setContentView(readerView);
    	
    	readerView.requestFocus();
    	readerView.setFocusableInTouchMode(true);
    	
    	readerView.requestFocus();
    	readerView.setFocusableInTouchMode(true);
    	
    	curr=WhichView.MAIN_VIEW;
	}
	public void goToBackground()
	{
		setContentView(R.layout.background_list);
    	curr=WhichView.BACKGROUND_LIST;
	}
	public void goToWellcomView()//¶i¤J"¦Ê¯Ç¬ì§Þ"¬É­±
	{
		if(wellcomView==null)
    	{
			wellcomView=new WellcomeSurfaceView(this);
    	}
		setContentView(wellcomView);
		curr=WhichView.WELLCOM_VIEW;
	}
	public void isWhichTime()//§PÂ_¬O²Ä´X¦¸¥´¶}³n¥ó
	{
		sp=this.getSharedPreferences("read", Context.MODE_PRIVATE);//³]¬°¼Ò¦¡¬°¨p¦³¼Ò¦¡
		
		String isOneTime=sp.getString("isOneTime", null);//§PÂ_¬O¤£¬O²Ä¤@¦¸¥´¶}³n¥ó
        String lastTimePath=sp.getString("path", null);//±o¨ì¤W¤@¦¸³X°Ýªº®Ñªº¸ô®|
        String lastTimePage=sp.getString("page", null);//±o¨ì¤W¤@¦¸³X°Ý®Ñªº®Ñ­¶
        String lastTimeName=sp.getString("name", null);//±o¨ì¤W¤@¦¸³X°Ý®Ñªº¦W¦r
        String lastTimeFontSize=sp.getString("fontsize", null);//±o¨ì¤W¤@¦¸³X°Ý®Ñªº¦rÅé¤j¤p
        if(lastTimePath==null)//¦pªG¬O¨S¦³¿ï¹L®Ñ(¥]¬A¦b»¡©ú¬É­±°h¥Xµ{§Ç©M²Ä¤@¦¸¥´¶}³n¥ó)
        {
        	Constant.FILE_PATH=null;//·í«e¸ô®|¬°ªÅ
        	Constant.CURRENT_LEFT_START=0;//·í«e®Ñ­¶¥ª¤W¤èªº¯Á¤Þ­È¬°0
        	Constant.CURRENT_PAGE=0;//·í«e®Ñ­¶¬°0	
        	
        	if(isOneTime==null)//¦pªG¬O²Ä¤@¦¸¥´¶}³n¥ó
        	{//¨Ï¥ÎÀq»{¦rÅé¤j¤p¡AµL°Ê§@
        	}else//¦pªG´¿¸g¦b»¡©ú¬É­±°h¥X¹L³n¥ó
        	{
        		//½T©w¦rÅé¤j¤p
        		Constant.TEXT_SIZE=Integer.parseInt(lastTimeFontSize);//±o¨ì¤W¤@¦¸¦b»¡©ú¬É­±ªº¦rÅé¤j¤p
        		Constant.TEXT_SPACE_BETWEEN_CN=Constant.TEXT_SIZE;
        		Constant.TEXT_SPACE_BETWEEN_EN=Constant.TEXT_SIZE/2;
        		//µ¹±`¶qÃþ¤¤ªº¦U­Ó±`¶q­«·s½á­È
                Constant.changeRatio();//½Õ¥Î¦Û¾AÀ³«Ì¹õªº¤èªk
        	}        	
        	
        }else//§_«h¡AÀò¨ú¤W¤@¦¸¥´¶}®Ñªº¡u¸ô®|¡v»P¡u­¶¼Æ¡v
        {
        	//½T©w¦rÅéªº¤j¤p
        	Constant.TEXT_SIZE=Integer.parseInt(lastTimeFontSize);//±o¨ì¤W¤@¦¸¦rÅé¤j¤p
    		Constant.TEXT_SPACE_BETWEEN_CN=Constant.TEXT_SIZE;
    		Constant.TEXT_SPACE_BETWEEN_EN=Constant.TEXT_SIZE/2;
    		//µ¹±`¶qÃþ¤¤ªº¦U­Ó±`¶q­«·s½á­È
            Constant.changeRatio();//½Õ¥Î¦Û¾AÀ³«Ì¹õªº¤èªk
        	//*****************************½T©w¦rÅé¤j¤pµ²§ô******************************************
        	Constant.TEXTNAME=lastTimeName;//Àò¨ú¤W¤@¦¸¥´¶}¤å¥óªº®Ñ¦W
        	
        	Constant.FILE_PATH=lastTimePath;//Àò¨ú¤W¤@¦¸¥´¶}¤å¥óªº¸ô®|
        	
        	//½Õ¥ÎgetCharacterCount¤èªk­pºâ¤å³¹ªº¦r²Åªø«×¡]¨¾¤îÅª¨ì³Ì«á¤@­¶¥i¥HÄ~Äò¦V¤UÅª¡^
        	Constant.CONTENTCOUNT=TextLoadUtil.getCharacterCount(Constant.FILE_PATH);  
        	
        	
        	int page=Integer.parseInt(lastTimePage);//±o¨ì®Ñªº­¶¼Æ¡AÂà¤Æ¬°int«¬
        	try{
        		//®Ú¾Ú·í«e¸ô®|¬d§ä¼Æ¾Ú®w¤¤ªº¹ïÀ³hashMapªºbyte«¬¼Æ¾Ú
        		byte[] data=SQLDBUtil.selectRecordData(Constant.FILE_PATH);
        		//¬°readerView¤¤ªºhashMap
        		readerView.currBook=SQLDBUtil.fromBytesToListRowNodeList(data);//±Nbyte«¬¼Æ¾ÚÂà¤Æ¬°hashMap«¬ªº¼Æ¾Ú      		
        		readerView.currRR=readerView.currBook.get(page);//®Ú¾ÚhashMapªº¯Á¤Þ¨ú¥XReadRecordªº¹ï¶H¡]°O¿ý·í«e®Ñ­¶ªº¥ª¤WÂI¯Á¤Þ¡^
        		Constant.CURRENT_LEFT_START=readerView.currRR.leftStart;//¬°·í«e®Ñ­¶¥ª¤W¯Á¤Þ½á­È
        		Constant.CURRENT_PAGE=readerView.currRR.pageNo;//¬°·í«e®Ñ­¶ªºpage½á­È      		
        	}catch(Exception e)
        	{
        		e.printStackTrace();
        	}        	
        }	
	}
	//¦pªGµo¥Í´«®Ñ¨Æ¥ó
	public void saveCurrentData()//°h¥X³n¥ó©M´«®Ñ®É³£­n¦s¼Æ¾Ú®w
	{
		if(Constant.FILE_PATH==null)//¦pªG¬O²Ä¤@¦¸¥´¶}³n¥ó(¦b»¡©ú¬É­±°h¥X³n¥ó)
		{
			//¨S¦³°Ê§@
		}else//¦pªG¬O²Än¦¸¿ï®Ñ
		{
			try
			{
				byte[] data=SQLDBUtil.fromListRowNodeListToBytes(readerView.currBook);//hashMapÂà¤Æ¬°byte
				SQLDBUtil.recordInsert(Constant.FILE_PATH,data);//±N·í«eªº¸ô®|©MhashMapªºbyte§Î¦¡¦s¤J¼Æ¾Ú®w
				//·í«e®Ñªº®Ñ­¶«H®§¦s¤J¼Æ¾Ú®w¡A¤è«K¤U¦¸¥´¶}®ÉÁÙ¬O·í«e­¶
				SQLDBUtil.lastTimeInsert(Constant.FILE_PATH, Constant.CURRENT_PAGE,Constant.TEXT_SIZE);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	public void savePreference()//·í«e®Ñ­¶°h¥X®É¡]ÂIÀ»°h¥X«ö¶s¡^«O¦s²{³õPreference
	{
		SharedPreferences.Editor editor=sp.edit();//½s¿èSharedPreferences
		if(Constant.FILE_PATH==null)//¦pªG¬O¦b»¡©ú¬É­±°h¥X
		{
			//¨S¦³°Ê§@µo¥Í
		}else//¦pªG¬O¦bÅª®Ñ¬É­±°h¥X¡A«O¦s²{³õ
		{
			//·í«eªº¤å¥ó¸ô®|©M¤å¥óªºpage¦s¤Jpreference
			int page=readerView.currRR.pageNo;//·í«e­¶¼Æ
			editor.putString("path", Constant.FILE_PATH);//·í«e¸ô®|¦s¤JSharedPreferences
			editor.putString("page", String.valueOf(page));//±N·í«e­¶¼Æ©ñ¤Jpreference(Âà¤Æ¬°String«¬)
			editor.putString("name",Constant.TEXTNAME);//±N·í«e®Ñªº¦W¦r©ñ¤Jpreference
		}
		editor.putString("isOneTime", "is");
		editor.putString("fontsize", String.valueOf(Constant.TEXT_SIZE));//§â·í«e¦rÅé¦s¤Jpreference
		editor.commit();//´£¥æ
		
	}
	//·í¦rÅéÅÜ¤Æ«á,§ó·s®ÑÅÒ©MHashMap¤¤¦s©ñ¼Æ¾Úªº¤èªk
	public void updataBookMarkAndHashMap()
	{
		try
		{//§PÂ_¼Æ¾Ú®w¤¤¬O§_¦s¦b·í«e³o¥»®Ñªº®ÑÅÒ
			haveBookMark=SQLDBUtil.judgeHaveBookMark(Constant.FILE_PATH);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if(haveBookMark)//¦pªG¦s¦b®ÑÅÒ
		{
			/*
			 * ¦pªG¦s¦b®ÑÅÒ¡A¥ý§ó·s®ÑÅÒ¡A¦b¼Æ¾Ú®w¤¤¨ú¥X·j¦³®ÑÅÒªº­¶¼Æ©M¦W¦r¡A±q¦Ó±o¨ì©Ò¦³ªºleft_startªº­È©ñ¦b¤@­Ó¼Æ²Õ¤¤
			 * §ó·shashMap¨ì®ÑÅÒ¤¤ªºleft_start³Bªº­È¡CµM«á¤£²MªÅhashMap¡A­«·sÃ¸»s¨ì²{¦bÅª¨ìªº¦ì¸m
			 */
			
			String[] nameBookMark=null;//¼È®É¦s©ñ®ÑÅÒªº¦W¦r
			int[] pageBookMark = null;//¼È®É¦s©ñ®ÑÅÒªº­¶¼Æ
			int[] leftStart=null;//¼È®É¦s©ñ¨C±ø®ÑÅÒ¹ïÀ³ªºleftStart
		   try
		   {
			   //¦b¼Æ¾Ú®w¤¤¨ú¥X©Ò¦³ªº®ÑÅÒ°O¿ý
			   dataBaseBookMark=SQLDBUtil.getBookmarkList(Constant.FILE_PATH);				   
			   int i=0;
			   nameBookMark=new String[dataBaseBookMark.size()];//¼Æ²Õ¤j¤p
			   pageBookMark=new int[dataBaseBookMark.size()];//®Ñ­¶
			   leftStart=new int[dataBaseBookMark.size()];//¹ïÀ³ªºleftStart

			   for(BookMark bm:dataBaseBookMark)
			   {
				   nameBookMark[i]=bm.bmname;//Àò¨ú©Ò¦³®ÑÅÒªº¦W¦r
				   pageBookMark[i]=bm.page;
				   i++;					   
			   }
		   }catch(Exception e)
		   {
			   e.printStackTrace();
		   }
		   for(int i=0;i<dataBaseBookMark.size();i++)
		   {
			   int m=i;
			   readerView.currRR=readerView.currBook.get(pageBookMark[m]);//¦bhashMap¤¤§ä¨ì¹ïÀ³ªºreaderView.currRR¹ï¹³
			   leftStart[m]=readerView.currRR.leftStart;//Àò¨úleftstartªº­È  
		   }
		   int tempLeftStart=Constant.CURRENT_LEFT_START;//±N·í«eªºleft_start°O¿ý¤U¨Ó
		   //­«·sÃ¸»s¬É­±¡A¨Ã¥B¦s®ÑÅÒ
		   for(int i=0;i<dataBaseBookMark.size();i++)
		   {			   
			   	Constant.CURRENT_LEFT_START=0;//¦]¬°­n±q²Ä¤@­¶¶}©lµêÀÀÃ¸»s ©Ò¥H­È­nÂk¹s
		   		Constant.CURRENT_PAGE=0;
		   		Constant.nextPageStart=0;
		   		Constant.nextPageNo=0;
		   		
		   		readerView.currBook.clear();//²MªÅhashMap
		   		int m=i;
		   		while(Constant.CURRENT_LEFT_START<leftStart[m])
		   		{
		   			readerView.currRR=new ReadRecord(Constant.nextPageStart,0,Constant.nextPageNo);
		   			
		   			Constant.CURRENT_LEFT_START=readerView.currRR.leftStart;//°O¿ý·í«eÅª¨ì³Bleftstartªº­È
		   			Constant.CURRENT_PAGE=readerView.currRR.pageNo;//°O¿ý·í«eÅª¨ì³Bªºpageªº­È

		   			readerView.currBook.put(readerView.currRR.pageNo, readerView.currRR);//·í«e­¶ªº«H®§¥[¤JhashMap
		   			
		   			readerView.currRR.isLeft=true;
		   			readerView.drawVirtualPage(readerView.currRR);//Ã¸»s¥ªÃäµêÀÀ­¶
		   			readerView.drawVirtualPage(readerView.currRR);//Ã¸»s¥kÃäµêÀÀ­¶	
		   		}
		   		//¦VbookMark¤¤¦s¤J§ó·s«áªº¼Æ¾Ú
		   		SQLDBUtil.bookMarkInsert(nameBookMark[m],Constant.CURRENT_PAGE);
		   }
		   //¤£²MhashMap¡A­«·sÃ¸»s¨ì§Ú­Ì²{¦bÅªªº¬É­±
		   
		   Constant.CURRENT_LEFT_START=0;//¦]¬°­n±q²Ä¤@­¶¶}©lµêÀÀÃ¸»s ©Ò¥H­È­nÂk¹s
		   Constant.CURRENT_PAGE=0;
		   Constant.nextPageStart=0;
		   Constant.nextPageNo=0;
		   
		   readerView.currRR=new ReadRecord(0,0,0);
		   readerView.currBook.put(0, readerView.currRR);//±N²Ä¤@­¶©ñ¤JhashMap¤¤
		   
		   while(Constant.CURRENT_LEFT_START<tempLeftStart)
		   {
			   readerView.currRR=new ReadRecord(Constant.nextPageStart,0,Constant.nextPageNo);
	
			   Constant.CURRENT_LEFT_START=readerView.currRR.leftStart;//°O¿ý·í«eÅª¨ì³Bleftstartªº­È
			   Constant.CURRENT_PAGE=readerView.currRR.pageNo;//°O¿ý·í«eÅª¨ì³Bªºpageªº­È

			   readerView.currBook.put(readerView.currRR.pageNo, readerView.currRR);//·í«e­¶ªº«H®§¥[¤JhashMap
	
			   readerView.currRR.isLeft=true;
			   readerView.drawVirtualPage(readerView.currRR);//Ã¸»s¥ªÃäµêÀÀ­¶
			   readerView.drawVirtualPage(readerView.currRR);//Ã¸»s¥kÃäµêÀÀ­¶	
		   }	   	
		}else//¦pªG¤£¦s¦b®ÑÅÒ¡A¥u§ó·s·í«e­¶¼ÆªºhashMap
		//§_«h¡A®Ú¾Ú·í«e­¶ªºLeft_Start­pºâ
	   	{
	   		int tempLeftStart=Constant.CURRENT_LEFT_START;//±N·í«eªºleft_start°O¿ý¤U¨Ó

	   		Constant.CURRENT_LEFT_START=0;//¦]¬°­n±q²Ä¤@­¶¶}©lµêÀÀÃ¸»s ©Ò¥H­È­nÂk¹s
	   		Constant.CURRENT_PAGE=0;
	   		Constant.nextPageStart=0;
	   		Constant.nextPageNo=0;
	   		
	   		readerView.currBook.clear();//²MªÅhashMap
	   		
	   		readerView.currRR=new ReadRecord(0,0,0);
			readerView.currBook.put(0, readerView.currRR);//±N²Ä¤@­¶©ñ¤JhashMap¤¤
	   		
	   		while(Constant.CURRENT_LEFT_START<tempLeftStart)
	   		{
	   			readerView.currRR=new ReadRecord(Constant.nextPageStart,0,Constant.nextPageNo);
	   			
	   			Constant.CURRENT_LEFT_START=readerView.currRR.leftStart;//°O¿ý·í«eÅª¨ì³Bleftstartªº­È
	   			Constant.CURRENT_PAGE=readerView.currRR.pageNo;//°O¿ý·í«eÅª¨ì³Bªºpageªº­È

	   			readerView.currBook.put(readerView.currRR.pageNo, readerView.currRR);//·í«e­¶ªº«H®§¥[¤JhashMap
	   			
	   			readerView.currRR.isLeft=true;
	   			readerView.drawVirtualPage(readerView.currRR);//Ã¸»s¥ªÃäµêÀÀ­¶
	   			readerView.drawVirtualPage(readerView.currRR);//Ã¸»s¥kÃäµêÀÀ­¶	
	   		}
	   	}
	}
	
    @Override 
    public void onResume()
    {
    	super.onResume();
    }
    @Override 
    public void onPause()
    {
    	super.onPause();
    }
}
