package com.bn.rcgl;

import java.util.ArrayList;
import java.util.Calendar;
import com.bn.rcgl.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import static com.bn.rcgl.Constant.*;
import static com.bn.rcgl.DBUtil.*;

public class RcActivity extends Activity 
{
	String[] defultType=new String[]{"會議","備忘","待辦"};//軟件的三個不能刪除的默認類型
	Dialog dialogSetRange;//日程查找時設置日期起始範圍的對話框
	Dialog dialogSetDatetime;//新建或修改日程時設置日期和時間的對話框
	Dialog dialogSchDelConfirm;//刪除日程時的確認對話框
	Dialog dialogCheck;//主界面中查看日程詳細內容的對話框
	Dialog dialogAllDelConfirm;//刪除全部過期日程時的確認對話框
	Dialog dialogAbout;//關於對話框
	static ArrayList<String> alType=new ArrayList<String>();//存儲所有日程類型的arraylist
	static ArrayList<Schedule> alSch=new ArrayList<Schedule>();//存儲所有schedule對象的ArrayList
	Schedule schTemp;//臨時的schedule
	ArrayList<Boolean> alSelectedType=new ArrayList<Boolean>();//記錄查找界面中類型前面checkbox狀態的
	String rangeFrom=getNowDateString();//查找日程時設置的起始日期，默認當前日期
	String rangeTo=rangeFrom;//查找日程時設置的終止日期，默認當前日期
	Layout curr=null;//記錄當前界面的枚舉類型
	WhoCall wcSetTimeOrAlarm;//用來判斷調用時間日期對話框的按鈕是設置時間還是設置鬧鐘,以便更改對話框中的一些控件該設置為visible還是gone
	WhoCall wcNewOrEdit;//用來判斷調用日程編輯界面的是新建日程按鈕還是在修改日程按鈕，以便設置對應的界面標題
	int sel=0;
	/*臨時記錄新建日程界面裡的類型spinner的position，因為設置時間的對話框cancel後
	     回到新建日程界面時會刷新所有控件，spinner中以選中的項目也會回到默認*/ 
	Handler hd=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case 0:
					gotoMain();
				break;
			}
		}
	};
	@Override    
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//無標題
        goToWelcomeView();
    }
    //歡迎界面
    public void goToWelcomeView()
    {
    	MySurfaceView mview=new MySurfaceView(this);
    	getWindow().setFlags//全屏
    	(
    			WindowManager.LayoutParams.FLAG_FULLSCREEN, 
    			WindowManager.LayoutParams.FLAG_FULLSCREEN
    	);
    	setContentView(mview);
    	curr=Layout.WELCOME_VIEW;
    }
    //===================================主界面start===========================================
    public void gotoMain()//初始化主界面
    {
    	getWindow().setFlags
    	(//非全屏
    			WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, 
    			WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
    	);	
    	setContentView(R.layout.main);
    	curr=Layout.MAIN;
    	sel=0;
    	
    	final ArrayList<Boolean> alIsSelected=new ArrayList<Boolean>();//記錄ListView中哪項選中了的標誌位
    	
    	final ImageButton bEdit=(ImageButton)findViewById(R.id.ibmainEdit);//修改日程按鈕
    	final ImageButton bCheck=(ImageButton)findViewById(R.id.ibmainCheck);//查看日程詳細內容的按鈕
    	final ImageButton bDel=(ImageButton)findViewById(R.id.ibmainDel);//刪除當前選中日程的按鈕
    	ImageButton bNew=(ImageButton)findViewById(R.id.ibmainNew);//新建日程按鈕
    	ImageButton bDelAll=(ImageButton)findViewById(R.id.ibmainDelAll);//刪除所有過期日程按鈕
    	ImageButton bSearch=(ImageButton)findViewById(R.id.ibmainSearch);//查找日程按鈕
    	final ListView lv=(ListView)findViewById(R.id.lvmainSchedule);//日程列表
        
        bCheck.setEnabled(false);//這三個按鈕分別為主界面的日程查看、日程修改、日程刪除,
    	bEdit.setEnabled(false);//默認設為不可用狀態
    	bDel.setEnabled(false);
        
    	alSch.clear();//從數據庫讀取之前清空存儲日程的arraylist
		loadSchedule(this);//從數據庫中讀取日程
		loadType(this);//從數據庫中讀取類型
		
        if(alSch.size()==0)//如果沒有任何日程，則刪除全部過期日程按鈕設為禁用
        {
        	bDelAll.setEnabled(false);
        }
        else
        {
        	bDelAll.setEnabled(true);
        }
        
        alIsSelected.clear();
    	
        for(int i=0;i<alSch.size();i++)//全部設置為false，即沒有一項選中
    	{
    		alIsSelected.add(false);
    	}
        
        //以下是ListView設置
        lv.setAdapter
        (
        		new BaseAdapter()
        		{
					@Override
					public int getCount() 
					{
						return alSch.size();
					}
					@Override
					public Object getItem(int position) 
					{
						return alSch.get(position);
					}
					@Override
					public long getItemId(int position) 
					{
						return 0;
					}
					@Override
					public View getView(int position, View convertView, ViewGroup parent) 
					{
						LinearLayout ll=new LinearLayout(RcActivity.this);
						ll.setOrientation(LinearLayout.VERTICAL);
						ll.setPadding(5, 5, 5, 5);
						
						LinearLayout llUp=new LinearLayout(RcActivity.this);
						llUp.setOrientation(LinearLayout.HORIZONTAL);
						LinearLayout llDown=new LinearLayout(RcActivity.this);
						llDown.setOrientation(LinearLayout.HORIZONTAL);
						
						//ListView中日期TextView
						TextView tvDate=new TextView(RcActivity.this);
						tvDate.setText(alSch.get(position).getDate1()+"   ");
						tvDate.setTextSize(17);
						tvDate.setTextColor(Color.parseColor("#129666"));
						llUp.addView(tvDate);
						
						//ListView時間TextView
						TextView tvTime=new TextView(RcActivity.this);
						tvTime.setText(alSch.get(position).timeForListView());
						tvTime.setTextSize(17);
						tvTime.setTextColor(Color.parseColor("#925301"));
						llUp.addView(tvTime);
						
						//若日程已過期，則日期和時間顏色、背景色設置為過期的顏色
						if(alSch.get(position).isPassed())
						{
							tvDate.setTextColor(getResources().getColor(R.color.passedschtext));
							tvTime.setTextColor(getResources().getColor(R.color.passedschtext));
							ll.setBackgroundColor(getResources().getColor(R.color.passedschbg));
						}
						//如果該項被選中了，背景色設置為選中的背景色
						if(alIsSelected.get(position))
						{
							ll.setBackgroundColor(getResources().getColor(R.color.selectedsch));
						}
						//如果有鬧鐘，則加上鬧鐘的圖標
						if(alSch.get(position).getAlarmSet())
						{
							ImageView iv=new ImageView(RcActivity.this);
							iv.setImageDrawable(getResources().getDrawable(R.drawable.alarm));
							iv.setLayoutParams(new LayoutParams(20, 20));
							llUp.addView(iv);
						}
						//日程類型TextView
						TextView tvType=new TextView(RcActivity.this);
						tvType.setText(alSch.get(position).typeForListView());
						tvType.setTextSize(17);
						tvType.setTextColor(Color.parseColor("#b20000"));
						llDown.addView(tvType);
						//日程標題TextView
						TextView tvTitle=new TextView(RcActivity.this);
						tvTitle.setText(alSch.get(position).getTitle());
						tvTitle.setTextSize(17);
						tvTitle.setTextColor(Color.parseColor("#000000"));
						llDown.addView(tvTitle);
						ll.addView(llUp);
						ll.addView(llDown);
						return ll;
					}
		        }
        );
        lv.setOnItemClickListener
        (
        		new OnItemClickListener()
        		{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
					{
						bCheck.setEnabled(true);//這三個按鈕分別為主界面的日程查看、日程修改、日程刪除,
				    	bEdit.setEnabled(true);//如果用戶在日程列表中選中了某個日程時，設為可用狀態
				    	bDel.setEnabled(true);
				    	
						schTemp=alSch.get(arg2);//選中該項目時，把該項目對像賦給schTemp
						
						//把標誌位全部設為false後再把當前選中項的對應的標誌位設為true
						for(int i=0;i<alIsSelected.size();i++)
						{
							alIsSelected.set(i,false);
						}
						alIsSelected.set(arg2,true);
					}
		        }
        );
        
        //bNew設置
        bNew.setOnClickListener
        (
        		new OnClickListener()
		        {
		
					@Override
					public void onClick(View v) {
						Calendar c=Calendar.getInstance();
						int t1=c.get(Calendar.YEAR);
						int t2=c.get(Calendar.MONTH)+1;
						int t3=c.get(Calendar.DAY_OF_MONTH);
						schTemp=new Schedule(t1,t2,t3);//臨時新建一個日程對象，年月日設為當前日期
						wcNewOrEdit=WhoCall.NEW;//調用日程編輯界面的是新建按鈕
						gotoSetting();//去日程編輯界面
					}        	
		        }
        );       
        //bEdit設置
        bEdit.setOnClickListener
        (
        		new OnClickListener()
		        {
					@Override
					public void onClick(View v) {
						wcNewOrEdit=WhoCall.EDIT;//調用日程編輯界面的是修改按鈕
						gotoSetting();//去日程編輯界面
					}        	
		        }
        ); 
        
        //刪除選中的日程按鈕
        bDel.setOnClickListener
        (
        		new OnClickListener()
		        {
					@Override
					public void onClick(View v) {
						showDialog(DIALOG_SCH_DEL_CONFIRM);
					}
		        }
        );
        
        //刪除所有過期日程按鈕
        bDelAll.setOnClickListener
        (
        		new OnClickListener()
		        {
					@Override
					public void onClick(View v) {
						showDialog(DIALOG_ALL_DEL_CONFIRM);
					}
		        }
        );
        
        //日程查找按鈕
        bSearch.setOnClickListener
        (
        		new OnClickListener()
		        {
					@Override
					public void onClick(View v) {
						gotoSearch();
					}
		        }
        );
        
      //日程查看按鈕
        bCheck.setOnClickListener
        (
        		new OnClickListener()
		        {
					@Override
					public void onClick(View v) {
						showDialog(DIALOG_CHECK);
					}
		        }
        ); 
    }
    //===================================日程編輯界面start=====================================
    public void gotoSetting()//初始化新建日程界面
    {
    	setContentView(R.layout.newschedule);
    	curr=Layout.SETTING;
    	
    	TextView tvTitle=(TextView)findViewById(R.id.tvnewscheduleTitle);
    	if(wcNewOrEdit==WhoCall.NEW)
    	{
    		tvTitle.setText("新建日程");
    	}
    	else if(wcNewOrEdit==WhoCall.EDIT)
    	{
    		tvTitle.setText("修改日程");
    	}
    	final Spinner spType=(Spinner)findViewById(R.id.spxjrcType);
    	Button bNewType=(Button)findViewById(R.id.bxjrcNewType);
    	final EditText etTitle=(EditText)findViewById(R.id.etxjrcTitle);
    	final EditText etNote=(EditText)findViewById(R.id.etxjrcNote);
    	TextView tvDate=(TextView)findViewById(R.id.tvnewscheduleDate);
    	Button bSetDate=(Button)findViewById(R.id.bxjrcSetDate);
    	TextView tvTime=(TextView)findViewById(R.id.tvnewscheduleTime);
    	TextView tvAlarm=(TextView)findViewById(R.id.tvnewscheduleAlarm);
    	final Button bSetAlarm=(Button)findViewById(R.id.bxjrcSetAlarm);
    	Button bDone=(Button)findViewById(R.id.bxjrcDone);
    	Button bCancel=(Button)findViewById(R.id.bxjrcCancel);
		
		etTitle.setText(schTemp.getTitle());
		etNote.setText(schTemp.getNote());
		tvDate.setText(schTemp.getDate1());
		tvTime.setText(schTemp.getTimeSet()?schTemp.getTime1():"無具體時間");
		tvAlarm.setText(schTemp.getAlarmSet()?schTemp.getDate2()+"  "+schTemp.getTime2():"無鬧鐘");
		
		//類型spinner設置
		spType.setAdapter
		(
				new BaseAdapter()
				{
					@Override
					public int getCount() 
					{
						return alType.size();
					}
		
					@Override
					public Object getItem(int position) 
					{
						return alType.get(position);
					}
					@Override
					public long getItemId(int position) {return 0;}
		
					@Override
					public View getView(int position, View convertView, ViewGroup parent) 
					{
						LinearLayout ll=new LinearLayout(RcActivity.this);
						ll.setOrientation(LinearLayout.HORIZONTAL);	
						TextView tv=new TextView(RcActivity.this);
						tv.setText(alType.get(position));
						tv.setTextSize(17);
						tv.setTextColor(R.color.black);
						return tv;
					}			
				}
		);
		spType.setSelection(sel);

		//新建日程類型按鈕
		bNewType.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						schTemp.setTitle(etTitle.getText().toString());//將已經輸入的title和note存入schTemp，以防返回時被清空
						schTemp.setNote(etNote.getText().toString());
						sel=spType.getSelectedItemPosition();//存儲spType的當前選擇
						gotoTypeManager();//進入日程類型管理界面
					}
				}
		);
		
		//
		bSetDate.setOnClickListener
		(
				new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						schTemp.setTitle(etTitle.getText().toString());//將已經輸入的主題和備註存入schTemp，以防設置完時間或鬧鐘返回時被清空
						schTemp.setNote(etNote.getText().toString());
						sel=spType.getSelectedItemPosition();
						wcSetTimeOrAlarm=WhoCall.SETTING_DATE;//調用設置日期時間對話框的是設置日程日期按鈕
						showDialog(DIALOG_SET_DATETIME);
					}
				}
		);
		bSetAlarm.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						schTemp.setTitle(etTitle.getText().toString());//將已經輸入的主題和備註存入schTemp，以防設置完時間或鬧鐘返回時被清空
						schTemp.setNote(etNote.getText().toString());
						sel=spType.getSelectedItemPosition();
						wcSetTimeOrAlarm=WhoCall.SETTING_ALARM;//調用設置日期時間對話框的是設置鬧鐘按鈕
						showDialog(DIALOG_SET_DATETIME);
					}
				}
		);
		
		//完成按鈕設置
		bDone.setOnClickListener(
			new OnClickListener()
			{
				@Override
				public void onClick(View v) {
					//讓新建的日程時間和當前時間比較看是否過期
					if(schTemp.isPassed())
					{
						Toast.makeText(RcActivity.this, "不能創建過期日程", Toast.LENGTH_SHORT).show();
						return;
					}
					
					if(schTemp.getAlarmSet())//如果設置了鬧鐘，則檢查鬧鐘時間是否合理
					{
						//如果日程日期在鬧鐘日期之前,
						//或者在日程時間已設置的前提下，日程日期和鬧鐘日期相同，但是日程時間在鬧鐘時間之前，
						//彈出提示
						if(schTemp.getDate1().compareTo(schTemp.getDate2())<0||
								schTemp.getTimeSet()&&
								schTemp.getDate1().compareTo(schTemp.getDate2())==0&&
								schTemp.getTime1().compareTo(schTemp.getTime2())<0)
						{
							Toast.makeText(RcActivity.this,"鬧鐘時間不能在日程時間之後", Toast.LENGTH_SHORT).show();
							return;
						}
					}
					
					String title=etTitle.getText().toString().trim();
					if(title.equals(""))//如果日程標題沒有輸入，默認為未命名
					{
						title="未命名";
					}
					schTemp.setTitle(title);
					String note=etNote.getText().toString();
					schTemp.setNote(note);
					String type=(String) spType.getSelectedItem();
					schTemp.setType(type);
					
			    	if(wcNewOrEdit==WhoCall.NEW)//如果當前界面是新建日程，調用插入日程方法
			    	{
			    		insertSchedule(RcActivity.this);
			    	}
			    	else if(wcNewOrEdit==WhoCall.EDIT)//如果當前界面是修改日程，調用更新日程方法
			    	{
			    		updateSchedule(RcActivity.this);
			    	}
					
					gotoMain();
				}
				
			}
		);
		//取消按鈕設置
		bCancel.setOnClickListener
		(
			new OnClickListener()
			{
				@Override
				public void onClick(View v) {					
					gotoMain();
				}
				
			}
		);
    }
    //===================================類型管理界面start=====================================
	public void gotoTypeManager()
	{
		setContentView(R.layout.typemanager);
		curr=Layout.TYPE_MANAGER;
		final ListView lvType=(ListView)findViewById(R.id.lvtypemanagerType);//列表列出所有已有類型
		final EditText etNew=(EditText)findViewById(R.id.ettypemanagerNewType);//輸入新類型名稱的TextView
		final Button bNew=(Button)findViewById(R.id.btypemanagerNewType);//新建類型按鈕
		final Button bBack=(Button)findViewById(R.id.btypemanagerBack);//返回上一頁按鈕
		
		bBack.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						gotoSetting();
					}
				}
		);
		
		lvType.setAdapter
		(
				new BaseAdapter()
				{
					@Override
					public int getCount() 
					{
						return alType.size();
					}
					@Override
					public Object getItem(int position) 
					{
						return alType.get(position);
					}
					@Override
					public long getItemId(int position) 
					{
						return 0;
					}
					@Override
					public View getView(final int position, View convertView, ViewGroup parent) 
					{
						LinearLayout ll=new LinearLayout(RcActivity.this);
						ll.setOrientation(LinearLayout.HORIZONTAL);
						ll.setGravity(Gravity.CENTER_VERTICAL);
						TextView tv=new TextView(RcActivity.this);
						tv.setText(alType.get(position));
						tv.setTextSize(17);
						tv.setTextColor(Color.BLACK);
						tv.setPadding(20, 0, 0, 0);
						ll.addView(tv);
						
						//軟件自帶的類型不能刪除，其他自建類型後面添加一個紅叉用來刪除自建類型
						if(position>=defultType.length)
						{
							ImageButton ib=new ImageButton(RcActivity.this);
							ib.setBackgroundDrawable(RcActivity.this.getResources().getDrawable(R.drawable.cross));
							ib.setLayoutParams(new LayoutParams(24, 24));
							ib.setPadding(20, 0, 0, 0);
							
							ib.setOnClickListener
							(
									new OnClickListener()
									{
										@Override
										public void onClick(View v) 
										{
											deleteType(RcActivity.this,lvType.getItemAtPosition(position).toString());
											loadType(RcActivity.this);
											gotoTypeManager();
										}
									}
							);
							ll.addView(ib);
						}
						return ll;
					}
				}
		);

		bNew.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						String newType=etNew.getText().toString().trim();
						if(newType.equals(""))
						{
							Toast.makeText(RcActivity.this, "類型名稱不能為空。", Toast.LENGTH_SHORT).show();
							return;
						}
						insertType(RcActivity.this,newType);
						gotoTypeManager();
					}
				}
		);
	}
    //===================================查找界面start=========================================
    public void gotoSearch()
    {
		setContentView(R.layout.search);
    	curr=Layout.SEARCH;
    	final Button bChange=(Button)findViewById(R.id.bsearchChange);//改變查找範圍按鈕
		final Button bSearch=(Button)findViewById(R.id.bsearchGo);//開始查找
		final Button bCancel=(Button)findViewById(R.id.bsearchCancel);//取消
		final CheckBox cbDateRange=(CheckBox)findViewById(R.id.cbsearchDateRange);//查找是否限制範圍的CheckBox
		final CheckBox cbAllType=(CheckBox)findViewById(R.id.cbsearchType);//是否在在所有類型中查找的CheckBox
		final ListView lv=(ListView)findViewById(R.id.lvSearchType);//所有類型列在lv中
		final TextView tvFrom=(TextView)findViewById(R.id.tvsearchFrom);//查找起始時期的tv
		final TextView tvTo=(TextView)findViewById(R.id.tvsearchTo);////查找終止時期的tv
		tvFrom.setText(rangeFrom);
		tvTo.setText(rangeTo);
		
		final ArrayList<String> type=getAllType(RcActivity.this);//獲取已存日程中的所有類型和用戶自建的所有類型
		
		alSelectedType.clear();
		for(int i=0;i<type.size();i++)//默認為所有類型設置狀態位false
		{
			alSelectedType.add(false);
		}
		
		cbDateRange.setOnCheckedChangeListener
		(
				new OnCheckedChangeListener()
				{//根據是否限制日期範圍的CheckBox決定更改日期範圍的按鈕是否可用
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						bChange.setEnabled(isChecked);
					}
				}
		);
		
		//設置「在全部類型中搜索」的CheckBox改變狀態時的行為
		cbAllType.setOnCheckedChangeListener
		(
				new OnCheckedChangeListener()
				{
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						for(int i=0;i<type.size();i++)//選中「全部選中」後把listview裡的所有類型後面的checkbox設成選中狀態
						{
							alSelectedType.set(i, isChecked);
						}
						lv.invalidateViews();//刷新ListView??
					}
				}
		);
		
		bChange.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) {
						showDialog(DIALOG_SET_SEARCH_RANGE);
					}
				}
		);
		
		lv.setAdapter
		(
				new BaseAdapter()
				{
					@Override
					public int getCount() 
					{
						return type.size();
					}
		
					@Override
					public Object getItem(int position) 
					{
						return type.get(position);
					}
					@Override
					public long getItemId(int position) 
					{
						return 0;
					}
		
					@Override
					public View getView(final int position, View convertView, ViewGroup parent) {
						LinearLayout ll=new LinearLayout(RcActivity.this);
						ll.setOrientation(LinearLayout.HORIZONTAL);	
						ll.setGravity(Gravity.CENTER_VERTICAL);
						LinearLayout llin=new LinearLayout(RcActivity.this);
						llin.setPadding(20, 0, 0, 0);
						ll.addView(llin);
						CheckBox cb=new CheckBox(RcActivity.this);
						cb.setButtonDrawable(R.drawable.checkbox);
						llin.addView(cb);
						cb.setChecked(alSelectedType.get(position));//按ArrayList裡面存儲的狀態設置CheckBox狀態
						
						cb.setOnCheckedChangeListener
						(
								new OnCheckedChangeListener()
								{
									@Override
									public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) 
									{
										alSelectedType.set(position, isChecked);//改變ArrayList裡面對應位置boolean值
									}
								}
						);
						
						TextView tv=new TextView(RcActivity.this);
						tv.setText(type.get(position));
						tv.setTextSize(18);
						tv.setTextColor(R.color.black);
						ll.addView(tv);
						return ll;
					}			
				}	
		);
		
		bSearch.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{ 
						//如果沒有一個類型被選中則提示
						boolean tmp=false;
						for(boolean b:alSelectedType)
						{
							tmp=tmp|b;
						}
						if(tmp==false)
						{
							Toast.makeText(RcActivity.this, "請至少選中一個類型", Toast.LENGTH_SHORT).show();
							return;
						}
						
						searchSchedule(RcActivity.this,type);
						gotoSearchResult();
					}
				}
		);
		
		bCancel.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						gotoMain();
					}
				}
		);
    }
    //===================================查找結果界面start=====================================
    public void gotoSearchResult()//該界面和主界面除了少了幾個按鈕其他完全一樣
    {
    	setContentView(R.layout.searchresult);
    	curr=Layout.SEARCH_RESULT;

    	sel=0;
    	
    	final ImageButton bCheck=(ImageButton)findViewById(R.id.ibsearchresultCheck);
    	final ImageButton bEdit=(ImageButton)findViewById(R.id.ibsearchresultEdit);
    	final ImageButton bDel=(ImageButton)findViewById(R.id.ibsearchresultDel);
    	ImageButton bBack=(ImageButton)findViewById(R.id.ibsearchresultBack);
    	ListView lv=(ListView)findViewById(R.id.lvsearchresultSchedule);
        
        bCheck.setEnabled(false);
    	bEdit.setEnabled(false);
    	bDel.setEnabled(false);
        
        
        //以下是查找結果的ListView設置
        lv.setAdapter
        (
        		new BaseAdapter()
		        {
					@Override
					public int getCount() 
					{
						return alSch.size();
					}
		
					@Override
					public Object getItem(int position) 
					{
						return alSch.get(position);
					}
		
					@Override
					public long getItemId(int position) 
					{
						return 0;
					}
		
					@Override
					public View getView(int position, View convertView, ViewGroup parent) 
					{
						LinearLayout ll=new LinearLayout(RcActivity.this);
						ll.setOrientation(LinearLayout.VERTICAL);
						ll.setPadding(5, 5, 5, 5);
						
						LinearLayout llUp=new LinearLayout(RcActivity.this);
						llUp.setOrientation(LinearLayout.HORIZONTAL);
						LinearLayout llDown=new LinearLayout(RcActivity.this);
						llDown.setOrientation(LinearLayout.HORIZONTAL);
								
						TextView tvDate=new TextView(RcActivity.this);
						tvDate.setText(alSch.get(position).getDate1()+"   ");
						tvDate.setTextSize(17);
						tvDate.setTextColor(Color.parseColor("#129666"));
						llUp.addView(tvDate);
						
						TextView tvTime=new TextView(RcActivity.this);
						tvTime.setText(alSch.get(position).timeForListView());
						tvTime.setTextSize(17);
						tvTime.setTextColor(Color.parseColor("#925301"));
						llUp.addView(tvTime);
						
						if(alSch.get(position).isPassed())//若日程已過期，則日期和時間顏色、背景色變灰
						{
							tvDate.setTextColor(Color.parseColor("#292929"));
							tvTime.setTextColor(Color.parseColor("#292929"));
							ll.setBackgroundColor(Color.parseColor("#818175"));
						}
						
						if(alSch.get(position).getAlarmSet())
						{
							ImageView iv=new ImageView(RcActivity.this);
							iv.setImageDrawable(getResources().getDrawable(R.drawable.alarm));
							iv.setLayoutParams(new LayoutParams(20, 20));
							llUp.addView(iv);
						}
						
						TextView tvType=new TextView(RcActivity.this);
						tvType.setText(alSch.get(position).typeForListView());
						tvType.setTextSize(17);
						tvType.setTextColor(Color.parseColor("#b20000"));
						llDown.addView(tvType);
						
						TextView tvTitle=new TextView(RcActivity.this);
						tvTitle.setText(alSch.get(position).getTitle());
						tvTitle.setTextSize(17);
						tvTitle.setTextColor(Color.parseColor("#000000"));
						llDown.addView(tvTitle);
		
						
						ll.addView(llUp);
						ll.addView(llDown);
						return ll;
					}
		        }
        );
        
        lv.setOnItemClickListener
        (
        		new OnItemClickListener()
        		{
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
					{
				        bCheck.setEnabled(true);
				    	bEdit.setEnabled(true);
				    	bDel.setEnabled(true);
						schTemp=alSch.get(arg2);//選中某個項目時，把該項目對像賦給schTemp
					}
		        }
        );
        
        //修改日程按鈕設置
        bEdit.setOnClickListener
        (
        		new OnClickListener()
		        {
		
					@Override
					public void onClick(View v) {
						wcSetTimeOrAlarm=WhoCall.EDIT;
						gotoSetting();
					}        	
		        }
        ); 
        //刪除選中日程按鈕設置
        bDel.setOnClickListener
        (
        		new OnClickListener()
		        {
					@Override
					public void onClick(View v) {
						showDialog(DIALOG_SCH_DEL_CONFIRM);
					}
		        }
        );
        
        //查找日程按鈕設置
        bBack.setOnClickListener
        (
        		new OnClickListener()
		        {
					@Override
					public void onClick(View v) 
					{
						gotoSearch();
					}
		        	
		        }
        );
        
      //查看日程按鈕設置
        bCheck.setOnClickListener
        (
        		new OnClickListener()
		        {
					@Override
					public void onClick(View v) {
						showDialog(DIALOG_CHECK);
					}
		        }
        );
    }
	//=========================幫助界面start==================================
	public void gotoHelp()
	{ 
		getWindow().setFlags//全屏
    	(
    			WindowManager.LayoutParams.FLAG_FULLSCREEN, 
    			WindowManager.LayoutParams.FLAG_FULLSCREEN
    	);
		setContentView(R.layout.help);
		curr=Layout.HELP;
		Button bBack=(Button)this.findViewById(R.id.bhelpback);
		bBack.setOnClickListener
		(
				new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						gotoMain();
					}
					
				}
		);
	}
	//創建對話框
    @Override
	public Dialog onCreateDialog(int id) 
    {
    	Dialog dialog=null;
    	switch(id)
    	{
    		case DIALOG_SET_SEARCH_RANGE:
    			AlertDialog.Builder b=new AlertDialog.Builder(this); 
  			  	b.setItems(null,null);
  			  	dialogSetRange=b.create();
  			  	dialog=dialogSetRange;	
    		break;
    		
    		case DIALOG_SET_DATETIME:
    			AlertDialog.Builder abSetDatetime=new AlertDialog.Builder(this); 
    			abSetDatetime.setItems(null,null);
    			dialogSetDatetime=abSetDatetime.create();
  			  	dialog=dialogSetDatetime;	
    		break;
    		
    		case DIALOG_SCH_DEL_CONFIRM:
    			AlertDialog.Builder abSchDelConfirm=new AlertDialog.Builder(this); 
    			abSchDelConfirm.setItems(null,null);
    			dialogSchDelConfirm=abSchDelConfirm.create();
  			  	dialog=dialogSchDelConfirm;	
    		break;
    		
    		case DIALOG_CHECK:
    			AlertDialog.Builder abCheck=new AlertDialog.Builder(this); 
    			abCheck.setItems(null,null);
    			dialogCheck=abCheck.create();
  			  	dialog=dialogCheck;	
    		break;
    		
    		case DIALOG_ALL_DEL_CONFIRM:
    			AlertDialog.Builder abAllDelConfirm=new AlertDialog.Builder(this); 
    			abAllDelConfirm.setItems(null,null);
    			dialogAllDelConfirm=abAllDelConfirm.create();
  			  	dialog=dialogAllDelConfirm;	
    		break;
    		
    		case DIALOG_ABOUT:
    			AlertDialog.Builder abAbout=new AlertDialog.Builder(this); 
    			abAbout.setItems(null,null);
    			dialogAbout=abAbout.create();
  			  	dialog=dialogAbout;	
    		break;
    	}
		return dialog;
	}
    //每次彈出Dialog對話框時更新對話框的內容
	@Override
	public void onPrepareDialog(int id,Dialog dialog) 
	{
		super.onPrepareDialog(id, dialog);
    	switch(id)
    	{
			case DIALOG_SET_SEARCH_RANGE://設置搜索範圍對話框		
				dialog.setContentView(R.layout.dialogsetrange);
				//year month day後面是1的表示關於起始時間設置，2表示關於終止時間設置，P表示plus加號，M表示minus建號
				final ImageButton bYear1P=(ImageButton)dialog.findViewById(R.id.bdialogsetrangeYear1P);
				final ImageButton bYear1M=(ImageButton)dialog.findViewById(R.id.bdialogsetrangeYear1M);
				final ImageButton bMonth1P=(ImageButton)dialog.findViewById(R.id.bdialogsetrangeMonth1P);
				final ImageButton bMonth1M=(ImageButton)dialog.findViewById(R.id.bdialogsetrangeMonth1M);
				final ImageButton bDay1P=(ImageButton)dialog.findViewById(R.id.bdialogsetrangeDay1P);
				final ImageButton bDay1M=(ImageButton)dialog.findViewById(R.id.bdialogsetrangeDay1M);
				final EditText etYear1=(EditText)dialog.findViewById(R.id.etdialogsetrangeYear1);
				final EditText etMonth1=(EditText)dialog.findViewById(R.id.etdialogsetrangeMonth1);
				final EditText etDay1=(EditText)dialog.findViewById(R.id.etdialogsetrangeDay1);
				
				final ImageButton bYear2P=(ImageButton)dialog.findViewById(R.id.bdialogsetrangeYear2P);
				final ImageButton bYear2M=(ImageButton)dialog.findViewById(R.id.bdialogsetrangeYear2M);
				final ImageButton bMonth2P=(ImageButton)dialog.findViewById(R.id.bdialogsetrangeMonth2P);
				final ImageButton bMonth2M=(ImageButton)dialog.findViewById(R.id.bdialogsetrangeMonth2M);
				final ImageButton bDay2P=(ImageButton)dialog.findViewById(R.id.bdialogsetrangeDay2P);
				final ImageButton bDay2M=(ImageButton)dialog.findViewById(R.id.bdialogsetrangeDay2M);
				final EditText etYear2=(EditText)dialog.findViewById(R.id.etdialogsetrangeYear2);
				final EditText etMonth2=(EditText)dialog.findViewById(R.id.etdialogsetrangeMonth2);
				final EditText etDay2=(EditText)dialog.findViewById(R.id.etdialogsetrangeDay2);
				
				Button bSetRangeOk=(Button)dialog.findViewById(R.id.bdialogsetrangeOk);
				Button bSetRangeCancel=(Button)dialog.findViewById(R.id.bdialogsetrangeCancel);
				
				//把YYYY/MM/DD格式的年月日分離出來,並且填到顯示年月日的TextView中
				String[] from=splitYMD(rangeFrom);
				String[] to=splitYMD(rangeTo);
				
				etYear1.setText(from[0]);
				etMonth1.setText(from[1]);
				etDay1.setText(from[2]);
				etYear2.setText(to[0]);
				etMonth2.setText(to[1]);
				etDay2.setText(to[2]);
				
				
				bYear1P.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int year=Integer.parseInt(etYear1.getText().toString().trim());
								year++;
								etYear1.setText(""+year);
							}
						}
				);
				bYear1M.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int year=Integer.parseInt(etYear1.getText().toString().trim());
								year--;
								etYear1.setText(""+year);
							}
						}
				);
				bMonth1P.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int month=Integer.parseInt(etMonth1.getText().toString().trim());
								if(++month>12)
								{
									month=1;
								}
								etMonth1.setText(month<10?"0"+month:""+month);
							}
						}
				);
				bMonth1M.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int month=Integer.parseInt(etMonth1.getText().toString().trim());
								if(--month<1)
								{
									month=12;
								}
								etMonth1.setText(month<10?"0"+month:""+month);
							}
						}
				);
				
				bDay1P.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int year=Integer.parseInt(etYear1.getText().toString().trim());
								int month=Integer.parseInt(etMonth1.getText().toString().trim());
								int day=Integer.parseInt(etDay1.getText().toString().trim());
								if(++day>getMaxDayOfMonth(year,month))
								{
									day=1;
								}
								etDay1.setText(day<10?"0"+day:""+day);
							}
						}
				);
				bDay1M.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int year=Integer.parseInt(etYear1.getText().toString().trim());
								int month=Integer.parseInt(etMonth1.getText().toString().trim());
								int day=Integer.parseInt(etDay1.getText().toString().trim());
								if(--day<1)
								{
									day=getMaxDayOfMonth(year,month);
								}
								etDay1.setText(day<10?"0"+day:""+day);
							}
						}
				);
				//================分割線，以上為設置起始時間的按鈕監聽，一下為設置終止時間的按鈕監聽==================
				bYear2P.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int year=Integer.parseInt(etYear2.getText().toString().trim());
								year++;
								etYear2.setText(""+year);
							}
						}	
				);
				bYear2M.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int year=Integer.parseInt(etYear2.getText().toString().trim());
								year--;
								etYear2.setText(""+year);
							}
						}
				);
				bMonth2P.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int month=Integer.parseInt(etMonth2.getText().toString().trim());
								if(++month>12)
								{
									month=1;
								}
								etMonth2.setText(month<10?"0"+month:""+month);
							}
						}
				);
				bMonth2M.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int month=Integer.parseInt(etMonth2.getText().toString().trim());
								if(--month<1)
								{
									month=12;
								}
								etMonth2.setText(month<10?"0"+month:""+month);
							}
						}
				);
				
				bDay2P.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int year=Integer.parseInt(etYear2.getText().toString().trim());
								int month=Integer.parseInt(etMonth2.getText().toString().trim());
								int day=Integer.parseInt(etDay2.getText().toString().trim());
								if(++day>getMaxDayOfMonth(year,month))
								{
									day=1;
								}
								etDay2.setText(day<10?"0"+day:""+day);
							}
						}
				);
				bDay2M.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int year=Integer.parseInt(etYear2.getText().toString().trim());
								int month=Integer.parseInt(etMonth2.getText().toString().trim());
								int day=Integer.parseInt(etDay2.getText().toString().trim());
								if(--day<1)
								{
									day=getMaxDayOfMonth(year,month);
								}
								etDay2.setText(day<10?"0"+day:""+day);
							}
						}
				);
				
				bSetRangeOk.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int year1=Integer.parseInt(etYear1.getText().toString().trim());
								int month1=Integer.parseInt(etMonth1.getText().toString().trim());
								int day1=Integer.parseInt(etDay1.getText().toString().trim());
								int year2=Integer.parseInt(etYear2.getText().toString().trim());
								int month2=Integer.parseInt(etMonth2.getText().toString().trim());
								int day2=Integer.parseInt(etDay2.getText().toString().trim());
								
								if(day1>getMaxDayOfMonth(year1,month1)||day2>getMaxDayOfMonth(year2,month2))
								{
									Toast.makeText(RcActivity.this, "日期設置錯誤", Toast.LENGTH_SHORT).show();
									return;
								}
								rangeFrom=Schedule.toDateString(year1, month1, day1);
								rangeTo=Schedule.toDateString(year2, month2, day2);
								if(rangeFrom.compareTo(rangeTo)>0)
								{
									Toast.makeText(RcActivity.this, "起始日期不能大於終止日期", Toast.LENGTH_SHORT).show();
									return;
								}
								dialogSetRange.cancel();
								gotoSearch();
							}
						}
				);
				
				//點取消則對話框關閉
				bSetRangeCancel.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								dialogSetRange.cancel();
							}
						}
				);

			break;
			
			case DIALOG_SET_DATETIME://設置時間日期對話框
				dialog.setContentView(R.layout.dialogdatetime);
				final ImageButton bYearP=(ImageButton)dialog.findViewById(R.id.bdialogdatetimeYearP);
				final ImageButton bYearM=(ImageButton)dialog.findViewById(R.id.bdialogdatetimeYearM);
				final ImageButton bMonthP=(ImageButton)dialog.findViewById(R.id.bdialogdatetimeMonthP);
				final ImageButton bMonthM=(ImageButton)dialog.findViewById(R.id.bdialogdatetimeMonthM);
				final ImageButton bDayP=(ImageButton)dialog.findViewById(R.id.bdialogdatetimeDayP);
				final ImageButton bDayM=(ImageButton)dialog.findViewById(R.id.bdialogdatetimeDayM);
				final ImageButton bHourP=(ImageButton)dialog.findViewById(R.id.bdialogdatetimeHourP);
				final ImageButton bHourM=(ImageButton)dialog.findViewById(R.id.bdialogdatetimeHourM);
				final ImageButton bMinP=(ImageButton)dialog.findViewById(R.id.bdialogdatetimeMinP);
				final ImageButton bMinM=(ImageButton)dialog.findViewById(R.id.bdialogdatetimeMinM);
				final EditText etYear=(EditText)dialog.findViewById(R.id.etdialogdatetimeYear);
				final EditText etMonth=(EditText)dialog.findViewById(R.id.etdialogdatetimeMonth);
				final EditText etDay=(EditText)dialog.findViewById(R.id.etdialogdatetimeDay);
				final EditText etHour=(EditText)dialog.findViewById(R.id.etdialogdatetimeHour);
				final EditText etMin=(EditText)dialog.findViewById(R.id.etdialogdatetimeMin);
				final CheckBox cbSetTime=(CheckBox)dialog.findViewById(R.id.cbdialogdatetimeSettime);
				final CheckBox cbSetAlarm=(CheckBox)dialog.findViewById(R.id.cbdialogdatetimeSetAlarm);
				Button bSetDateOk=(Button)dialog.findViewById(R.id.bdialogdatetimeOk);
				Button bSetDateCancel=(Button)dialog.findViewById(R.id.bdialogdatetimeCancel);
				
				LinearLayout llSetTime=(LinearLayout)dialog.findViewById(R.id.lldialogdatetimeSetTime);
				LinearLayout llCheckBox=(LinearLayout)dialog.findViewById(R.id.lldialogdatetimeCheckBox);
				LinearLayout llAlarmCheckBox=(LinearLayout)dialog.findViewById(R.id.lldialogdatetimeAlarmCheckBox);
				
				if(wcSetTimeOrAlarm==WhoCall.SETTING_DATE)//如果是設置日期按鈕調用的本對話框
				{
					llSetTime.setVisibility(LinearLayout.VISIBLE);//設置具體時間的LinearLayout顯示出來
					llCheckBox.setVisibility(LinearLayout.VISIBLE);//是否設置具體時間的CheckBox顯示出來
					llAlarmCheckBox.setVisibility(LinearLayout.GONE);//是否設置鬧鐘的CheckBox不顯示
					
					//把schTemp中的year month day顯示在EditText中
					etYear.setText(""+schTemp.getYear());
					etMonth.setText(schTemp.getMonth()<10?"0"+schTemp.getMonth():""+schTemp.getMonth());
					etDay.setText(schTemp.getDay()<10?"0"+schTemp.getDay():""+schTemp.getDay());
					
					//如果schTemp中表示是否設置具體時間的布爾值timeSet為true，即設置了具體時間，則把已設置的時分顯示在EditText中
					if(schTemp.getTimeSet())
					{
						etHour.setText(schTemp.getHour()<10?"0"+schTemp.getHour():""+schTemp.getHour());
						etMin.setText(schTemp.getMinute()<10?"0"+schTemp.getMinute():""+schTemp.getMinute());
					}
					else//否則默認顯示八點
					{
						etHour.setText("08");
						etMin.setText("00");
					}
					
					//是否設置具體時間的CheckBox決定有關設置時間的控件可不可用
					cbSetTime.setOnCheckedChangeListener
					(
							new OnCheckedChangeListener()
							{
								@Override
								public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) 
								{
									etHour.setEnabled(isChecked);
									etMin.setEnabled(isChecked);
									bHourP.setEnabled(isChecked);
									bHourM.setEnabled(isChecked);
									bMinP.setEnabled(isChecked);
									bMinM.setEnabled(isChecked);
								}
							}
					);
					
					//這三條語句確保觸發cbSetTime的OnCheckedChangeListener
					cbSetTime.setChecked(schTemp.getTimeSet());
					cbSetTime.setChecked(!schTemp.getTimeSet());
					cbSetTime.setChecked(schTemp.getTimeSet());
				}
				
				//如果調用該界面的是設置鬧鐘按鈕
				if(wcSetTimeOrAlarm==WhoCall.SETTING_ALARM)
				{
					llSetTime.setVisibility(LinearLayout.VISIBLE);//設置具體時間的LinearLayout顯示
					llCheckBox.setVisibility(LinearLayout.GONE);//是否設置具體時間的CheckBox不顯示
					llAlarmCheckBox.setVisibility(LinearLayout.VISIBLE);//是否設置鬧鐘的CheckBox顯示
					
					//是否設置鬧鐘CheckBox決定有關設置鬧鐘的控件可不可用
					cbSetAlarm.setOnCheckedChangeListener
					(
							new OnCheckedChangeListener()
							{
								@Override
								public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) 
								{
									bYearP.setEnabled(isChecked);
									bYearM.setEnabled(isChecked);
									bMonthP.setEnabled(isChecked);
									bMonthM.setEnabled(isChecked);
									bDayP.setEnabled(isChecked);
									bDayM.setEnabled(isChecked);
									bHourP.setEnabled(isChecked);
									bHourM.setEnabled(isChecked);
									bMinP.setEnabled(isChecked);
									bMinM.setEnabled(isChecked);
									etYear.setEnabled(isChecked);
									etMonth.setEnabled(isChecked);
									etDay.setEnabled(isChecked);
									etHour.setEnabled(isChecked);
									etMin.setEnabled(isChecked);
								}
							}
					);
					
					//確保OnCheckedChangeListener被觸發
					cbSetAlarm.setChecked(schTemp.getAlarmSet());
					cbSetAlarm.setChecked(!schTemp.getAlarmSet());
					cbSetAlarm.setChecked(schTemp.getAlarmSet());
					
					if(cbSetAlarm.isChecked())//如果表示是否設置鬧鐘的Checkbox是選中，說明有鬧鐘設置，則讀取鬧鐘數據填入EditText
					{
						etYear.setText(""+schTemp.getAYear());
						etMonth.setText(schTemp.getAMonth()<10?"0"+schTemp.getAMonth():""+schTemp.getAMonth());
						etDay.setText(schTemp.getADay()<10?"0"+schTemp.getADay():""+schTemp.getADay());
						etHour.setText(schTemp.getAHour()<10?"0"+schTemp.getAHour():""+schTemp.getAHour());
						etMin.setText(schTemp.getAMin()<10?"0"+schTemp.getAMin():""+schTemp.getAMin());
					}
					else//如果沒選中，說明沒有鬧鐘設置，默認讀取日程時間設置到鬧鐘的EditText
					{
						etYear.setText(""+schTemp.getYear());
						etMonth.setText(schTemp.getMonth()<10?"0"+schTemp.getMonth():""+schTemp.getMonth());
						etDay.setText(schTemp.getDay()<10?"0"+schTemp.getDay():""+schTemp.getDay());
						if(schTemp.getTimeSet())//如果日程設置了具體時間，鬧鐘的小時分鐘設置為具體時間的小時分鐘
						{
							etHour.setText(schTemp.getHour()<10?"0"+schTemp.getHour():""+schTemp.getHour());
							etMin.setText(schTemp.getMinute()<10?"0"+schTemp.getMinute():""+schTemp.getMinute());
						}
						else//如果日程沒設具體時間，則鬧鐘的小時分鐘默認設置8點
						{
							etHour.setText("08");
							etMin.setText("00");
						}
					}
				}				
				
				
				bYearP.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int year=Integer.parseInt(etYear.getText().toString().trim());
								year++;
								etYear.setText(""+year);
							}
						}
				);
				bYearM.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int year=Integer.parseInt(etYear.getText().toString().trim());
								year--;
								etYear.setText(""+year);
							}
						}
				);
				bMonthP.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int month=Integer.parseInt(etMonth.getText().toString().trim());
								if(++month>12)
								{
									month=1;
								}
								etMonth.setText(month<10?"0"+month:""+month);
							}
						}
				);
				bMonthM.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int month=Integer.parseInt(etMonth.getText().toString().trim());
								if(--month<1)
								{
									month=12;
								}
								etMonth.setText(month<10?"0"+month:""+month);
							}
						}
				);
				
				bDayP.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int year=Integer.parseInt(etYear.getText().toString().trim());
								int month=Integer.parseInt(etMonth.getText().toString().trim());
								int day=Integer.parseInt(etDay.getText().toString().trim());
								if(++day>getMaxDayOfMonth(year,month))
								{
									day=1;
								}
								etDay.setText(day<10?"0"+day:""+day);
							}
						}
				);
				bDayM.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int year=Integer.parseInt(etYear.getText().toString().trim());
								int month=Integer.parseInt(etMonth.getText().toString().trim());
								int day=Integer.parseInt(etDay.getText().toString().trim());
								if(--day<1)
								{
									day=getMaxDayOfMonth(year,month);
								}
								etDay.setText(day<10?"0"+day:""+day);
							}
						}
				);
				
				bHourP.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int hour=Integer.parseInt(etHour.getText().toString().trim());
								if(++hour>23)
								{
									hour=0;
								}
								etHour.setText(hour<10?"0"+hour:""+hour);
							}
						}
				);
				bHourM.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int hour=Integer.parseInt(etHour.getText().toString().trim());
								if(--hour<0)
								{
									hour=23;
								}
								etHour.setText(hour<10?"0"+hour:""+hour);
							}
						}
				);
				bMinP.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int min=Integer.parseInt(etMin.getText().toString().trim());
								if(++min>59)
								{
									min=0;
								}
								etMin.setText(min<10?"0"+min:""+min);
							}
						}
				);
				bMinM.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								int min=Integer.parseInt(etMin.getText().toString().trim());
								if(--min<0)
								{
									min=59;
								}
								etMin.setText(min<10?"0"+min:""+min);
							}
						}
				);
				
				bSetDateOk.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								String year=etYear.getText().toString().trim();
								String month=etMonth.getText().toString().trim();
								String day=etDay.getText().toString().trim();
								//最後再檢查一下是否有年月日設置錯誤，比如2月30號等等
								if(Integer.parseInt(day)>getMaxDayOfMonth(Integer.parseInt(year),Integer.parseInt(month)))
								{
									Toast.makeText(RcActivity.this, "日期設置錯誤", Toast.LENGTH_SHORT).show();
									return;
								}
								
								//如果此對話框是被設置日期按鈕調用的，把年月日賦給Schedule中的Date1，即日程日期
								if(wcSetTimeOrAlarm==WhoCall.SETTING_DATE)
								{
									schTemp.setDate1(year, month, day);
									schTemp.setTimeSet(cbSetTime.isChecked());
									if(cbSetTime.isChecked())//如果設置了具體時間，把時分賦給Schedule中的Time1，即日程時間
									{							
										String hour=etHour.getText().toString().trim();
										String min=etMin.getText().toString().trim();
										schTemp.setTime1(hour, min);
									}
									
								}
								//如果此對話框是被設置鬧鐘按鈕調用的，把年月日賦給Schedule中的Date2，即鬧鐘日期，時分賦給Time2，即鬧鐘時間
								else if(wcSetTimeOrAlarm==WhoCall.SETTING_ALARM)
								{
									schTemp.setAlarmSet(cbSetAlarm.isChecked());
									if(cbSetAlarm.isChecked())
									{
										schTemp.setDate2(year, month, day);
										String hour=etHour.getText().toString().trim();
										String min=etMin.getText().toString().trim();
										schTemp.setTime2(hour, min);
									}
								}
								dialogSetDatetime.cancel();
								gotoSetting();		
							}
						}
				);
				bSetDateCancel.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								dialogSetDatetime.cancel();
							}
						}
				);
				break;
			case DIALOG_SCH_DEL_CONFIRM://刪除日程對話框
				dialog.setContentView(R.layout.dialogschdelconfirm);
				Button bDelOk=(Button)dialog.findViewById(R.id.bdialogschdelconfirmOk);
				Button bDelCancel=(Button)dialog.findViewById(R.id.bdialogschdelconfirmCancel);
				
				bDelOk.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								deleteSchedule(RcActivity.this);
								gotoMain();
								dialogSchDelConfirm.cancel();
							}
						}
				);
				
				bDelCancel.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								dialogSchDelConfirm.cancel();
							}
						}
				);
				break;
				
			case DIALOG_CHECK://查看日程對話框
				dialog.setContentView(R.layout.dialogcheck);
				TextView tvType=(TextView)dialog.findViewById(R.id.tvdialogcheckType);//顯示類型的TextView
				TextView tvTitle=(TextView)dialog.findViewById(R.id.tvdialogcheckTitle);//顯示標題的TextView
				TextView tvNote=(TextView)dialog.findViewById(R.id.tvdialogcheckNote);//顯示備註的TextView
				TextView tvDatetime1=(TextView)dialog.findViewById(R.id.tvdialogcheckDate1);//顯示日程日期和時間的TextView
				TextView tvDatetime2=(TextView)dialog.findViewById(R.id.tvdialogcheckDate2);//顯示鬧鐘日期和時間的TextView
				Button bEdit=(Button)dialog.findViewById(R.id.bdialogcheckEdit);//編輯按鈕
				Button bDel=(Button)dialog.findViewById(R.id.bdialogcheckDel);//刪除按鈕
				Button bBack=(Button)dialog.findViewById(R.id.bdialogcheckBack);//返回按鈕
				
				tvType.setText(schTemp.typeForListView());
				tvTitle.setText(schTemp.getTitle());
				tvNote.setText(schTemp.getNote());
				
				//如果備註為空，顯示無備註
				if(schTemp.getNote().equals(""))
				{
					tvNote.setText("(無備註)");
				}
				String time1=schTemp.getTime1();
				
				//如果具體時間為空，時間顯示成--:--
				if(time1.equals("null"))
				{
					time1="- -:- -";
				}
				tvDatetime1.setText(schTemp.getDate1()+"  "+time1);
				
				String date2=schTemp.getDate2();
				String time2=schTemp.getTime2();
				
				//鬧鐘日期為空的話說明沒有鬧鐘
				if(date2.equals("null"))
				{
					date2="(無鬧鐘)";
					time2="";
				}
				tvDatetime2.setText(date2+"  "+time2);
				
		        bEdit.setOnClickListener
		        (
		        		new OnClickListener()
				        {
							@Override
							public void onClick(View v) {
								dialogCheck.cancel();
								gotoSetting();
							}        	
				        }
		        ); 
		        
		        bDel.setOnClickListener
		        (
		        		new OnClickListener()
				        {
							@Override
							public void onClick(View v) {
								dialogCheck.cancel();
								showDialog(DIALOG_SCH_DEL_CONFIRM);
							}
				        }
		        );
		        
		        bBack.setOnClickListener
		        (
		        		new OnClickListener()
				        {
							@Override
							public void onClick(View v) {
								dialogCheck.cancel();
							}
				        }
		        );
				break;
			case DIALOG_ALL_DEL_CONFIRM://刪除所有過期日程對話框
				dialog.setContentView(R.layout.dialogdelpassedconfirm);
				Button bAllDelOk=(Button)dialog.findViewById(R.id.bdialogdelpassedconfirmOk);
				Button bAllDelCancel=(Button)dialog.findViewById(R.id.bdialogdelpassedconfirmCancel);
				bAllDelOk.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								deletePassedSchedule(RcActivity.this);
								gotoMain();
								dialogAllDelConfirm.cancel();
							}
						}
				);
				
				bAllDelCancel.setOnClickListener
				(
						new OnClickListener()
						{
							@Override
							public void onClick(View v) 
							{
								dialogAllDelConfirm.cancel();
							}
						}
			    );
				break;
    	}
	}
	//onKeyDown方法
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	//按下手機返回按鈕時
    	if(keyCode==4){
        	switch(curr)
        	{
        		case MAIN://在主界面的話退出程序
        			System.exit(0);
        		break;
        		case SETTING://在日程編輯界面的話返回主界面
        			gotoMain();
        		break;
        		case TYPE_MANAGER:////在類型管理界面的話返回日程編輯界面
        			gotoSetting();
        		break;
        		case SEARCH://在日程查找界面的話返回主界面
        			gotoMain();
        		break;
        		case SEARCH_RESULT://在日程查找結果界面的話返回日程查找界面
        			gotoSearch();
        		break;
        		case HELP://在幫助界面的話返回主界面
        			gotoMain();
        		break;
        		case ABOUT:
        			gotoMain();
        		break;
        	}
        	return true;
    	}
    	return false;
	}
    //創建Menu
    @Override
	public boolean onCreateOptionsMenu(Menu menu) 
    {
    	if(curr!=Layout.MAIN)//只允許在主界面調用菜單???????????????????????
    	{
    		return false;  
    	}
    	MenuItem miHelp=menu.add(1, MENU_HELP, 0, "說明");
    	miHelp.setIcon(R.drawable.help);
		MenuItem miAbout=menu.add(1, MENU_ABOUT, 0, "關於");
		miAbout.setIcon(R.drawable.about);
		
		miAbout.setOnMenuItemClickListener
		(
				new OnMenuItemClickListener()
				{
					@Override
					public boolean onMenuItemClick(MenuItem item) 
					{	
						setContentView(R.layout.rcabout);
						curr=Layout.HELP;
						return true;
					}
					
				}
		);
		
		miHelp.setOnMenuItemClickListener
		(
				new OnMenuItemClickListener()
				{
					@Override
					public boolean onMenuItemClick(MenuItem item) 
					{		
						setContentView(R.layout.rchelp);
						curr=Layout.ABOUT;
						return true;
					}
				}
		);
		return true;
	}
    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) 
	{
		return super.onMenuItemSelected(featureId, item);
	}
    //用來得到year年month月的最大天數
	public int getMaxDayOfMonth(int year,int month)
    {
    	int day=0;
    	boolean run=false;
    	if(year%400==0||year%4==0&&year%100!=0)
    	{
    		run=true;
    	}
    	if(month==4||month==6||month==9||month==11)
    	{
    		day=30;
    	}
    	else if(month==2)
    	{
    		if(run)
    		{
    			day=29;
    		}
    		else
    		{
    			day=28;
    		}
    	}
    	else
    	{
    		day=31;
    	}
    	return day;
    }
    //返回把YYYY/MM/DD分隔後的年月日字符串數組
	public String[] splitYMD(String ss)
    {
    	String[] s=ss.split("/");
    	return s;
    }
}
