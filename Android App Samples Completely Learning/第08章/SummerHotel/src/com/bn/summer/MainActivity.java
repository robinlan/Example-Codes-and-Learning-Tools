package com.bn.summer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import static com.bn.summer.Constant.*;

enum WhichView {MAIN_VIEW,//主界面
	           LOGIN_VIEW,//登錄界面
	        REGISTER_VIEW,//註冊斤面
	        USERINFO_VIEW,//顯示用戶信息的界面，也可以
	     ALLRESOURCE_VIEW,//顯示所有資源的界面
      RESOURCEDETAIL_VIEW,//顯示所選資源詳細信息的界面
       ORDERLIST_VIEW,//顯示當前用戶所有訂單的界面
      ORDERDETAIL_VIEW,//訂單詳情的界面
           KEFANG_VIEW,//顯示客房資源的界面
      MEETINGROOM_VIEW,//顯示會議室資源的界面
          WELCOME_VIEW,
          GY_VIEW,
          HELP_VIEW
                 }


public class MainActivity extends Activity {      
    
	Handler hd;
	WhichView curr;
	WelcomeView wv;
	String[] gNameAndgDetail;//分組資源名數組
	String[] userInfoArray;//用戶信息數組
	String[] listArray;//訂單列表數組
	String[] orderDetail;//訂單詳情數組
	String[] msgOrder;
	String[] resourceNameArray;
	String rgid;//資源編號
	String msgscale="";
	String msgscalemeeting="";
	String sex="女";
	Dialog dateInputDialog;//時間對話框1
	Dialog dateInputDialog1;//時間對話框2  
	Dialog exitDialog;
	int[] drawableIds={R.drawable.room,R.drawable.meeting};//客房和會議室的數組
	int[] kfmsg2={R.string.drbj,R.string.srbj,R.string.drgbj,R.string.srgbj};//客房規格
	int[] hysmsg={R.string.sr,R.string.swr,R.string.eswr,R.string.ss,R.string.bs,R.string.yb,R.string.ybes};//會議室規格
	
	
    final int MENU_OK=5;     
    final int GENDER_GROUP=0;      //性別子菜單項組的編號
    
    MenuItem[] miaHobby=new MenuItem[3];//愛好菜單項組  
    MenuItem male=null;//男性性別菜單
	
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //設置全屏顯示
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags
        (
        		WindowManager.LayoutParams.FLAG_FULLSCREEN , 
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        //強制為橫屏
        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //強制為豎屏
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);  
        //gotoLoginView();
        gotoWelcomeView();
		//創建消息處理器對像
		hd=new Handler()
        {
        	@Override
        	public void handleMessage(Message msg)
        	{
        	   //調用父類處理  
        	   super.handleMessage(msg);
        	   //獲取消息中的數據
        	   Bundle b;
			                    
			   b=msg.getData();
 			   //獲取內容字符串
 			   String msgStr=b.getString("msg");
        	   //根據消息what編號的不同，執行不同的業務邏輯
        		switch(msg.what)
        		{
        		 case Constant.GOTOLOGIN:
      			   gotoLoginView();
      			   break;
        		   //將消息中的內容提取出來顯示在EditText中
        		   case Constant.LOGINVIEW:

        			  if(msgStr.equals("登錄成功"))
        			  {
        				  Toast.makeText(MainActivity.this, "登錄成功！", Toast.LENGTH_SHORT).show();
   					      gotoMainView();
        			  }
        			  else
        			  {
        				  Toast.makeText(MainActivity.this, "登錄失敗", Toast.LENGTH_SHORT).show();
        			  }
				          
						      break;
        		   /*case Constant.CHANGE_EDIT2:
        			   //獲取消息中的數據
        			   b=msg.getData();
        			   //獲取內容字符串
        			   msgStr=b.getString("msg");
					   Toast.makeText(MainActivity.this, msgStr, Toast.LENGTH_SHORT).show();
					   if(msgStr.equals("註冊成功,請輸入用戶名和密碼以登錄！"))//如果從jsp得到的字符串為登錄成功，則轉到首頁
					   {
						   gotoLoginView();
					   }
        		   break;*/
        		   case Constant.MAINVIEW:
            			 //獲取消息中的數據
            			 b=msg.getData();
            			 //獲取內容字符串
            			 msgStr=b.getString("msg");
            			gNameAndgDetail=msgStr.split("\\|");
            			
            			TextView maintv1=(TextView)findViewById(R.id.mainTextView01);
            			maintv1.setText(gNameAndgDetail[0]);
            			TextView maintv2=(TextView)findViewById(R.id.mainTextView02);
            			maintv2.setText(gNameAndgDetail[1]);
            			TextView maintv3=(TextView)findViewById(R.id.mainTextView03);
            			maintv3.setText(gNameAndgDetail[2]);
            			TextView maintv4=(TextView)findViewById(R.id.mainTextView04);
            			maintv4.setText(gNameAndgDetail[3]);
            			 break;
        		   case Constant.KFVIEW:
        			  b=msg.getData();
        			  msgStr=b.getString("msg");
        			  //initListView1(msgStr);
        			  initListViewf(msgStr,Constant.RESOURCEDIVIDEDBYGROUPLISTVIEW01);
        			  break;
        		   case Constant.MEETINTVIEW:
        			   b=msg.getData();
        			   msgStr=b.getString("msg");
        			   Toast.makeText(MainActivity.this, msgStr,Toast.LENGTH_SHORT);
        			   //initListView11(msgStr);
        			   initListViewf(msgStr,Constant.RESOURCEDIVIDEDBYGROUP1LISTVIEW01);
        			   break;
        		   case Constant.RESOURCEDETAIL:
        			   b=msg.getData();
        			   msgStr=b.getString("msg");
        			   msgOrder=msgStr.split("\\|");
        			   TextView tv2=(TextView)findViewById(R.id.resourcedetailTextView02);
        			   tv2.setText(msgOrder[0]);
        			   TextView tv3=(TextView)findViewById(R.id.resourcedetailTextView03);
        			   tv3.setText(msgOrder[1]);
        			   break;
        		   case Constant.ALLRESOURCE:
        			   b=msg.getData();
        			   msgStr=b.getString("msg");
        			   //initListView3(msgStr);
        			   initListViewf(msgStr,Constant.ALLRESOURCELISTVIEW01);
        			   break;
        		   /*case Constant.CHANGE_EDIT6:
        			   b=msg.getData();
        			   msgStr=b.getString("msg");
        			   Toast.makeText(MainActivity.this, msgStr, Toast.LENGTH_SHORT).show();
        			   break;*/
        		   case Constant.MODIFYINFO:
        			   b=msg.getData();
	                   //獲取內容字符串
	                   msgStr=b.getString("msg");
	                   System.out.println(msgStr);
	                   Toast.makeText(MainActivity.this, msgStr, Toast.LENGTH_SHORT).show();
	                   gotoLoginView();
        		   break;
        		   case Constant.REGISTER:
        			   //獲取消息中的數據
        			   b=msg.getData();
        			   //獲取內容字符串
        			   msgStr=b.getString("msg");
					   Toast.makeText(MainActivity.this, msgStr, Toast.LENGTH_SHORT).show();
					   System.out.println(msgStr);
					   if(msgStr.equals("註冊成功,請輸入用戶名和密碼以登錄！"))//如果從jsp得到的字符串為登錄成功，則轉到首頁
					   {
						   gotoLoginView();
					   }
        		   break;
        		   case Constant.USERINFO://用於接收用戶信息 
        			   b=msg.getData();
        			   msgStr=b.getString("msg");
        			   System.out.println(msgStr);
        			   userInfoArray=msgStr.split("\\|");
        			   
        			   EditText uiet2=(EditText)findViewById(R.id.userinfoEditText02);//密碼
        			   EditText uiet4=(EditText)findViewById(R.id.userinfoEditText04);//手機號
        			   EditText uiet5=(EditText)findViewById(R.id.userinfoEditText05);//真實姓名
        			   RadioButton rbman=(RadioButton)findViewById(R.id.userinfoRadioButton01);//性別
        			   RadioButton rbwoman=(RadioButton)findViewById(R.id.userinfoRadioButton02);
        			   EditText uiet7=(EditText)findViewById(R.id.userinfoEditText07);//郵箱
        			   
        			   uiet2.setText(userInfoArray[0]);
        			   uiet4.setText(userInfoArray[1]);
        			   uiet5.setText(userInfoArray[2]);
        			   uiet7.setText(userInfoArray[4]);
        			   String sex=userInfoArray[3];
        			   if(sex.equals("男"))
        			   {
        				   rbman.setChecked(true);
        			   }
        			   else
        			   {
        				   rbwoman.setChecked(true);
        			   }        			   
        		   break;
        		   case Constant.ADDLIST:
        			   b=msg.getData();
        			   msgStr=b.getString("msg");
        			   System.out.println(msgStr);
        			   Toast.makeText(MainActivity.this, msgStr, Toast.LENGTH_SHORT).show();
        			   if(msgStr.equals("預訂成功！"))
        			   {
        				   gotoOrderList();
        			   }
        			   break;
        		   case Constant.ORDER:
        			   b=msg.getData();
        			   msgStr=b.getString("msg");
        			   System.out.println(msgStr);
        			   //ininOrderListView(msgStr);
        			   initListViewt(msgStr,Constant.ORDERLISTVIEW01);
        		   break;
        		   case Constant.ORDERDETAIL:
        			   b=msg.getData();
        			   msgStr=b.getString("msg");
        			   orderDetail=msgStr.split("\\|");
        			   
        			   TextView odtv1=(TextView)findViewById(R.id.orderdetailTextView01);
        			   TextView odtv2=(TextView)findViewById(R.id.orderdetailTextView02);
        			   TextView odtv3=(TextView)findViewById(R.id.orderdetailTextView03);
        			   TextView odtv4=(TextView)findViewById(R.id.orderdetailTextView04);
        			   TextView odtv5=(TextView)findViewById(R.id.orderdetailTextView05);
        			   
        			   odtv1.setText(orderDetail[0]);
        			   odtv2.setText(orderDetail[1]);
        			   odtv3.setText(orderDetail[2]);
        			   odtv4.setText(orderDetail[3]);
        			   odtv5.setText(orderDetail[4]);
        			break; 
        		   case Constant.DELETE:
        			   b=msg.getData();
        			   msgStr=b.getString("msg");
        			   if(msgStr.equals("訂單刪除成功！"))
        			   {
        				   Toast.makeText(MainActivity.this, "刪除成功！", Toast.LENGTH_SHORT).show();
        				   gotoOrderList();
        			   }
        			   else
        			   {
        				   Toast.makeText(MainActivity.this, "刪除失敗！", Toast.LENGTH_SHORT).show();
        			   }
        			   break;
        		}
        	}
        };        		        
        
    } 

    
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	
    	//確定菜單項
    	MenuItem help=menu.add(GENDER_GROUP+2,MENU_OK,0,R.string.help);
    	help.setIcon(R.drawable.help);
    	OnMenuItemClickListener lsn=new OnMenuItemClickListener()
    	{//實現菜單項點擊事件監聽接口
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				//appendStateStr();
				gotoHelpView();  
				return true;
			}    		
    	};
    	help.setOnMenuItemClickListener(lsn);//給確定菜單項添加監聽器    	
    	//給確定菜單項添加快捷鍵
    	MenuItem gy=menu.add(GENDER_GROUP+3,MENU_OK+1,0,R.string.gy);
    	gy.setIcon(R.drawable.gy1);  
    	OnMenuItemClickListener lsn1=new OnMenuItemClickListener()
    	{//實現菜單項點擊事件監聽接口
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				//appendStateStr();
				gotoGYView();  
				return true;
			}    		
    	};
    	gy.setOnMenuItemClickListener(lsn1);
    	return true;
    }
    
    protected void gotoGYView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.gy);
		curr=WhichView.GY_VIEW;
	}
	protected void gotoHelpView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.help);
		curr=WhichView.HELP_VIEW;
	}
	public boolean onKeyDown(int keyCode, KeyEvent e)
    {
    	if(keyCode!=4)
        {
        return false;
        }
    	/*if(curr==WhichView.MAIN_VIEW||curr==WhichView.ALLRESOURCE_VIEW||curr==WhichView.KEFANG_VIEW
    	  ||curr==WhichView.LOGIN_VIEW||curr==WhichView.MEETINGROOM_VIEW||curr==WhichView.ORDERDETAIL_VIEW||
    	  curr==WhichView.ORDERLIST_VIEW||curr==WhichView.REGISTER_VIEW||curr==WhichView.RESOURCEDETAIL_VIEW||
    	  curr==WhichView.USERINFO_VIEW)
    	{
    		showDialog(EXIT_DIALOG);
    		return true;
    	}*/
    	if(curr==WhichView.MAIN_VIEW||curr==WhichView.LOGIN_VIEW)
    	{
    		showDialog(EXIT_DIALOG);
    		return true;
    	}
    	else if(curr==WhichView.REGISTER_VIEW)
    	{
    		gotoLoginView();
    		return true;
    	}
    	else if(curr==WhichView.USERINFO_VIEW||curr==WhichView.KEFANG_VIEW||curr==WhichView.MEETINGROOM_VIEW)
    	{
    		gotoMainView();
    		return true;
    	}
    	else if(curr==WhichView.ALLRESOURCE_VIEW)
    	{
    		gotoMainView();
    		return true;
    	}
    	else if(curr==WhichView.RESOURCEDETAIL_VIEW||curr==WhichView.ORDERLIST_VIEW)
    	{
    		gotoAllResource();
    		return true;
    	}
    	else if(curr==WhichView.ORDERDETAIL_VIEW)
    	{
    		gotoOrderList();
    		return true;
    	}
    	else if(curr==WhichView.GY_VIEW||curr==WhichView.HELP_VIEW)
    	{
    		gotoMainView();
    		return true;
    	}
		return false;
    }
    
    public void initListViewf(String msgStr,int list)
    {
    	resourceNameArray=msgStr.split("\\|");
    	final int count=resourceNameArray.length/4;
    	BaseAdapter ba=new BaseAdapter()
        {
			@Override
			public int getCount() {
				return count;//總共5個選項
			}

			@Override
			public Object getItem(int arg0) { return null; }  

			@Override
			public long getItemId(int arg0) { return 0; }

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {	  						
				//初始化TextView
				LinearLayout ll=new LinearLayout(MainActivity.this);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				ll.setPadding(5, 5, 5, 5);
				
				TextView tv=new TextView(MainActivity.this);
				tv.setWidth(80);
				tv.setText(resourceNameArray[arg0*4]);
				tv.setTextColor(getResources().getColor(R.color.black));
				ll.addView(tv);
				TextView tv1=new TextView(MainActivity.this);
				tv1.setWidth(120);
				tv1.setText(resourceNameArray[arg0*4+1]);
				tv1.setTextColor(getResources().getColor(R.color.black));
				ll.addView(tv1);
				TextView tv2=new TextView(MainActivity.this);
				tv2.setWidth(60);
				tv2.setText(resourceNameArray[arg0*4+2]);
				tv2.setTextColor(getResources().getColor(R.color.black));
				ll.addView(tv2);
				TextView tv3=new TextView(MainActivity.this);
				tv3.setWidth(60);
				tv3.setText(resourceNameArray[arg0*4+3]);
				tv3.setTextColor(getResources().getColor(R.color.black));
				ll.addView(tv3);
				
				return ll;
			}        	
        };
        if(list==Constant.RESOURCEDIVIDEDBYGROUPLISTVIEW01)
        {
        	ListView lv=(ListView)findViewById(R.id.resourcedividedbygroupListView01);
            lv.setAdapter(ba);
            
            lv.setOnItemClickListener(
            		new OnItemClickListener()
            		{

    					@Override
    					public void onItemClick(AdapterView<?> arg0, View arg1,
    							int arg2, long arg3) {
    						
    						rgid=resourceNameArray[arg2*4];
    						gotoResourceDetail(rgid);
    					}
            			
            		}
            		);
        }
        if(list==Constant.RESOURCEDIVIDEDBYGROUP1LISTVIEW01)
        {
        	 ListView lv=(ListView)findViewById(R.id.resourcedividedbygroup1ListView01);
        	    lv.setAdapter(ba);
        	    
        	    lv.setOnItemClickListener(
        	    		new OnItemClickListener()
        	    		{

        					@Override
        					public void onItemClick(AdapterView<?> arg0, View arg1,
        							int arg2, long arg3) {
        						
        						rgid=resourceNameArray[arg2*4];
        						gotoResourceDetail(rgid);
        					}
        	    			
        	    		}
        	    		);
        }
        if(list==Constant.ALLRESOURCELISTVIEW01)
        {
        	ListView lv=(ListView)findViewById(R.id.allresourceListView01);
            lv.setAdapter(ba);
            
            lv.setOnItemClickListener(
            		new OnItemClickListener()
            		{

    					@Override
    					public void onItemClick(AdapterView<?> arg0, View arg1,
    							int arg2, long arg3) {
    						
    						rgid=resourceNameArray[arg2*4];
    						gotoResourceDetail(rgid);
    					}
            			
            		}
            		);
        }
    }
    public void initListViewt(String msgStr,int list)
    {
    	 listArray=msgStr.split("\\|");
     	final int count=listArray.length/3;//分組資源的數量
     	
     	//為ListView準備內容適配器
         BaseAdapter baa=new BaseAdapter()
         {
 			@Override
 			public int getCount() {
 				return count;
 			}

 			@Override
 			public Object getItem(int arg0) { return null; }

 			@Override
 			public long getItemId(int arg0) { return 0; }

 			@Override
 			public View getView(int arg0, View arg1, ViewGroup arg2) {							
 				LinearLayout ll=new LinearLayout(MainActivity.this);
 				ll.setOrientation(LinearLayout.HORIZONTAL);
 				
 				//ll.setLayoutParams(null);
 				ll.setBackgroundColor(getResources().getColor(R.color.yellow));
 				
 				TextView tv=new TextView(MainActivity.this);
 				tv.setWidth(50);//訂單編號
 				tv.setGravity(1);
 				tv.setText(listArray[arg0*3]);
 				tv.setTextSize(15);
 				tv.setTextColor(getResources().getColor(R.color.black));
 				ll.addView(tv);
 				TextView tv1=new TextView(MainActivity.this);
 				tv1.setWidth(170);//下單時間
 				tv1.setText(listArray[arg0*3+1]);
 				tv1.setTextSize(15);
 				tv1.setGravity(1);
 				tv1.setTextColor(getResources().getColor(R.color.black));
 				ll.addView(tv1);	
 				TextView tv2=new TextView(MainActivity.this);
 				tv2.setWidth(100);//訂單狀態
 				tv2.setText(listArray[arg0*3+2]);
 				tv2.setTextSize(15);
 				tv2.setGravity(1);
 				tv2.setTextColor(getResources().getColor(R.color.black));
 				ll.addView(tv2);	
 				return ll; 
 			}        		
         };
         if(list==Constant.ORDERLISTVIEW01)
         {
        	 ListView lvv=(ListView)findViewById(R.id.orderListView01);
             lvv.setAdapter(baa);//為ListView設置內容適配器
             
             //設置選項被單擊的監聽器
             lvv.setOnItemClickListener(
                new OnItemClickListener()
                {
     			@Override
     			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
     					long arg3) {//重寫選項被單擊事件的處理方法
     				
     				String oid=listArray[arg2*3].toString().trim();

     				gotoOrderDetail(oid);
     			}        	   
                }
             );
         }
    }
    
    
    protected void gotoWelcomeView()
    {
    	if(wv==null)
    	{
    		wv=new WelcomeView(this);
    	}
    	setContentView(wv);
    	curr=WhichView.WELCOME_VIEW;
    }


	protected void gotoResourceDetail(String rgid) {
		
		setContentView(R.layout.resourcedetail); 
		
		final TextView tv55=(TextView)findViewById(R.id.resourcedetailTextView01);
		tv55.setText(rgid);
		
		
		final String url="http://"+IP_ADDRESS+":8080/jiudian/resourceDetail.jsp";
		final Map<String,String> params=new HashMap<String,String>();
		params.put("params1", rgid);
		
		new Thread()
		{
			public void run()
			{
				String msgStr=HttpUploadUtil.postWithoutFile(url, params);//將url和參數傳給jsp
				Bundle b=new Bundle();
    			//將內容字符串放進數據Bundle中
    			b.putString("msg", msgStr);
    			//創建消息對像
    			Message msg=new Message();
    			//設置數據Bundle到消息中
    			msg.setData(b);
    			//設置消息的what值
    			msg.what=Constant.RESOURCEDETAIL;
    			//發送消息
    			hd.sendMessage(msg);
			}
		}.start();
		
		TextView et1=(TextView)findViewById(R.id.resouredetailEditText01);
		et1.setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View v) {

						showDialog(DATE_INPUT_DIALOG);
						String msg=getSystemDate();
						String msgArray[]=msg.split("\\-");
						EditText et1=(EditText)dateInputDialog.findViewById(R.id.EditText01);
						EditText et2=(EditText)dateInputDialog.findViewById(R.id.EditText02);
						EditText et3=(EditText)dateInputDialog.findViewById(R.id.EditText03);
						et1.setText(msgArray[0]);
						et2.setText(msgArray[1]);
						et3.setText(msgArray[2]);
					}
					
				}
				);
		TextView et2=(TextView)findViewById(R.id.resouredetailEditText02);
		et2.setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View v) {

						showDialog(DATE_INPUT_DIALOG_1);
						String msg=getSystemDate();
						String msgArray[]=msg.split("\\-");
						EditText et1=(EditText)dateInputDialog1.findViewById(R.id.EditText01);
						EditText et2=(EditText)dateInputDialog1.findViewById(R.id.EditText02);
						EditText et3=(EditText)dateInputDialog1.findViewById(R.id.EditText03);
						et1.setText(msgArray[0]);
						et2.setText(msgArray[1]);
						et3.setText(msgArray[2]);
					}
					
				}
				);
		
		Button b1=(Button)findViewById(R.id.resouredetailButton01);
		b1.setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View arg0) {
	
						final String url="http://"+IP_ADDRESS+":8080/jiudian/setOlist.jsp";

				    	SharedPreferences sp=MainActivity.this.getSharedPreferences("actm", Context.MODE_PRIVATE);
						   //從SharedPreferences中讀取上次訪問的時間
						        String uname=sp.getString
						        (
						        		"uname",   //鍵值
						        		null    //默認值
						        );
						        if(uname==null)
						        {
						        	Toast.makeText(MainActivity.this, "請登錄", Toast.LENGTH_SHORT).show();
						        	gotoLoginView();
						        }
						        else
						        {
						        	SharedPreferences.Editor editor=sp.edit();
							        editor.putString("uname",uname);
							        editor.commit();
						        }    
						
						TextView ettet1=(TextView)findViewById(R.id.resouredetailEditText01);
						TextView ettet2=(TextView)findViewById(R.id.resouredetailEditText02);
						TextView tv=(TextView)findViewById(R.id.resourcedetailTextView01);
//						final Map<String,String> params=new HashMap<String,String>();
//						params.put("param1", uname);
//						params.put("param2", tv.getText().toString().trim());
//						params.put("param3", ettet1.getText().toString().trim());
//						params.put("param4", ettet2.getText().toString().trim());
//						
//						new Thread()
//						{
//							public void run()
//							{
//								String msgStr=HttpUploadUtil.postWithoutFile(url, params);//將url和參數傳給jsp
//								Bundle b=new Bundle();
//				    			//將內容字符串放進數據Bundle中
//				    			b.putString("msg", msgStr);
//				    			//創建消息對像
//				    			Message msg=new Message();
//				    			//設置數據Bundle到消息中
//				    			msg.setData(b);
//				    			//設置消息的what值
//				    			msg.what=Constant.ADDLIST;
//				    			//發送消息
//				    			hd.sendMessage(msg);
//							}
//						}.start();
//					}
						if(ettet1.getText().toString().equals("點擊設置時間")||ettet2.getText().toString().equals("點擊設置時間"))
						{
							Toast.makeText(MainActivity.this, "請填寫日期！！！", Toast.LENGTH_SHORT).show();
						}
						else
						{
							final Map<String,String> params=new HashMap<String,String>();
							params.put("param1", uname);
							params.put("param2", tv.getText().toString().trim());
							params.put("param3", ettet1.getText().toString().trim());
							params.put("param4", ettet2.getText().toString().trim());
							
							new Thread()
							{
								public void run()
								{
									String msgStr=HttpUploadUtil.postWithoutFile(url, params);//將url和參數傳給jsp
									Bundle b=new Bundle();
					    			//將內容字符串放進數據Bundle中
					    			b.putString("msg", msgStr);
					    			//創建消息對像
					    			Message msg=new Message();
					    			//設置數據Bundle到消息中
					    			msg.setData(b);
					    			//設置消息的what值
					    			msg.what=Constant.ADDLIST;
					    			//發送消息
					    			hd.sendMessage(msg);
								}
							}.start();
						}
					}
				}
				);
//		b1=(Button)findViewById(R.id.resourcedetailButton02);
//		b1.setOnClickListener(
//				new OnClickListener()
//				{
//
//					@Override
//					public void onClick(View v) {
//						
//						gotoMainView();
//					}
//					
//				}
//				);
		ImageButton resourceDetailib2=(ImageButton)findViewById(R.id.resourcedetailImageButton02);
		resourceDetailib2.setOnClickListener(
			new OnClickListener()
			{

				@Override
				public void onClick(View v) {
					
					gotoUserInfo();
				}
				
			}
		);
		ImageButton resourceDetailib1=(ImageButton)findViewById(R.id.resourcedetailImageButton01);
		resourceDetailib1.setOnClickListener(
			new OnClickListener()
			{

				@Override
				public void onClick(View v) {
					
					gotoOrderList();
				}
				
			}
		);
		
		curr=WhichView.RESOURCEDETAIL_VIEW;
	}
	
	public Dialog onCreateDialog(int id)
    {
    	Dialog result=null;
    	switch(id)
    	{
    	case DATE_INPUT_DIALOG:
    		dateInputDialog=new MyDialog(this);
    		result=dateInputDialog;
    		break;
    	case DATE_INPUT_DIALOG_1:
    		dateInputDialog1=new MyDialog1(this);
    		result=dateInputDialog1;
    		break;
    	case EXIT_DIALOG:
    		exitDialog=new EXDialog(this);
    		result=exitDialog;
    	}
		return result;
    }
    
    public void onPrepareDialog(int id,final Dialog dialog)
    {
    	switch(id)
    	{
    	case DATE_INPUT_DIALOG:
    		Button b1=(Button)dateInputDialog.findViewById(R.id.Button01);
    		b1.setOnClickListener(
    				new OnClickListener()
    				{

						@Override
						public void onClick(View v) {
							
							EditText et1=(EditText)dateInputDialog.findViewById(R.id.EditText01);
							String year=et1.getText().toString();
							EditText et2=(EditText)dateInputDialog.findViewById(R.id.EditText02);
							String month=et2.getText().toString();
							EditText et3=(EditText)dateInputDialog.findViewById(R.id.EditText03);
							String day=et3.getText().toString();
							String msg=showMsg(year,month,day);
							if(msg.equals("年份超過限制！！！")||msg.equals("日期超出！！！")||msg.equals("二月份日期超出！！！")||msg.equals("二月份超出！！！"))
							{
								Toast.makeText(MainActivity.this, msg, 5000).show();
							}
							else
							{
								//setContentView(R.layout.main);
								TextView et=(TextView)findViewById(R.id.resouredetailEditText01);
								et.setText(msg);
							}
							dateInputDialog.cancel();
						}
    					
    				}
    				);
    		b1=(Button)dateInputDialog.findViewById(R.id.Button02);
    		b1.setOnClickListener(
    				new OnClickListener()
    				{

						@Override
						public void onClick(View v) {
							
							dateInputDialog.cancel();
						}
    					
    				}
    				);
    		ImageButton ib1=(ImageButton)dateInputDialog.findViewById(R.id.ImageButton01);
    		EditText et1=(EditText)dateInputDialog.findViewById(R.id.EditText01);
    		upOrDown(ib1,et1,UP);
    		ImageButton ib4=(ImageButton)dateInputDialog.findViewById(R.id.ImageButton04);
    		upOrDown(ib4,et1,DOWN);
    		
    		ImageButton ib2=(ImageButton)dateInputDialog.findViewById(R.id.ImageButton02);
    		EditText et2=(EditText)dateInputDialog.findViewById(R.id.EditText02);
    		upOrDown(ib2,et2,UP);
    		ImageButton ib5=(ImageButton)dateInputDialog.findViewById(R.id.ImageButton05);
    		upOrDown(ib5,et2,DOWN);
    		
    		ImageButton ib3=(ImageButton)dateInputDialog.findViewById(R.id.ImageButton03);
    		EditText et3=(EditText)dateInputDialog.findViewById(R.id.EditText03);
    		upOrDown(ib3,et3,UP);
    		ImageButton ib6=(ImageButton)dateInputDialog.findViewById(R.id.ImageButton06);
    		upOrDown(ib6,et3,DOWN);
    		break;
    	case DATE_INPUT_DIALOG_1:
    		Button b2=(Button)dateInputDialog1.findViewById(R.id.Button01);
    		b2.setOnClickListener(
    				new OnClickListener()
    				{

						@Override
						public void onClick(View v) {
							
							EditText et1=(EditText)dateInputDialog1.findViewById(R.id.EditText01);
							String year=et1.getText().toString();
							EditText et2=(EditText)dateInputDialog1.findViewById(R.id.EditText02);
							String month=et2.getText().toString();
							EditText et3=(EditText)dateInputDialog1.findViewById(R.id.EditText03);
							String day=et3.getText().toString();
							String msg=showMsg(year,month,day);
							if(msg.equals("年份超過限制！！！")||msg.equals("日期超出！！！")||msg.equals("二月份日期超出！！！")||msg.equals("二月份超出！！！"))
							{
								Toast.makeText(MainActivity.this, msg, 5000).show();
							}
							else
							{
								//setContentView(R.layout.main);
								TextView et=(TextView)findViewById(R.id.resouredetailEditText02);
								et.setText(msg);
							}
							dateInputDialog1.cancel();
						}
    					
    				}
    				);
    		b2=(Button)dateInputDialog1.findViewById(R.id.Button02);
    		b2.setOnClickListener(
    				new OnClickListener()
    				{

						@Override
						public void onClick(View v) {
							
							dateInputDialog1.cancel();
						}
    					
    				}
    				);
    		ImageButton ib11=(ImageButton)dateInputDialog1.findViewById(R.id.ImageButton01);
    		EditText et11=(EditText)dateInputDialog1.findViewById(R.id.EditText01);
    		upOrDown(ib11,et11,UP);
    		ImageButton ib41=(ImageButton)dateInputDialog1.findViewById(R.id.ImageButton04);
    		upOrDown(ib41,et11,DOWN);
    		
    		ImageButton ib21=(ImageButton)dateInputDialog1.findViewById(R.id.ImageButton02);
    		EditText et21=(EditText)dateInputDialog1.findViewById(R.id.EditText02);
    		upOrDown(ib21,et21,UP);
    		ImageButton ib51=(ImageButton)dateInputDialog1.findViewById(R.id.ImageButton05);
    		upOrDown(ib51,et21,DOWN);
    		
    		ImageButton ib31=(ImageButton)dateInputDialog1.findViewById(R.id.ImageButton03);
    		EditText et31=(EditText)dateInputDialog1.findViewById(R.id.EditText03);
    		upOrDown(ib31,et31,UP);
    		ImageButton ib61=(ImageButton)dateInputDialog1.findViewById(R.id.ImageButton06);
    		upOrDown(ib61,et31,DOWN);
    		break;
    	case EXIT_DIALOG:
    		Button b3=(Button)exitDialog.findViewById(R.id.Button01);
    		b3.setOnClickListener(
    				new OnClickListener()
    				{

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							System.exit(0);
						}
    					
    				}
    				);
    		b3=(Button)exitDialog.findViewById(R.id.Button02);
    		b3.setOnClickListener(
    				new OnClickListener()
    				{

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							exitDialog.cancel();
						}
    					
    				}
    				);
    	}
    }
  
    protected String showMsg(String year, String month, String day) {
		
    	String msg="";
    	String moStr="";
    	String daStr="";
		int ye=Integer.parseInt(year);
		int mo=Integer.parseInt(month);
		int da=Integer.parseInt(day);
		if(ye<2011||ye>2100)
		{
			msg="年份超過限制！！！";
		}
		else if((mo==4||mo==6||mo==9||mo==11)&&da>=31)
		{
			msg="日期超出！！！";
		}
		else if((((ye%4==0)&&(ye%100!=0))||((ye%100==0)&&(ye%400==0)))&&mo==2&&da>29)
		{
			msg="二月份日期超出！！！";
		}
		else if(!(((ye%4==0)&&(ye%100!=0))||((ye%100==0)&&(ye%400==0)))&&mo==2&&da>28)
		{
			msg="二月份超出！！！";
		}
		else if((mo==1||mo==3||mo==5||mo==7||mo==8||mo==10||mo==12)&&da>=32)
		{
			msg="日期超出！！！";
		}
		else if(mo>=13)
		{
			msg="日期超出！！！";
		}
		else
		{
			if(mo<=9)
			{
				moStr="0"+mo;
			}
			else
			{
				moStr=""+mo;
			}
			if(da<=9)
			{
				daStr="0"+da;
			}
			else
			{
				daStr=""+da;
			}
			msg=ye+"-"+moStr+"-"+daStr;
		}
		return msg;
	}
	
    public void upOrDown(ImageButton ib,final EditText et,final int upordown)
	{
		ib.setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						
						String msg=et.getText().toString();
						int msg1=Integer.parseInt(msg);
						msg1+=upordown;
						String msg2=""+msg1;
						et.setText(msg2);
					}
					
				}
				);
	}
	
    protected String getSystemDate() {
		
		String result="";
		Date dt=new Date();
		int nowyear=dt.getYear()+1900;
		int nowmonth=dt.getMonth()+1;
		int nowday=dt.getDate();
		result=nowyear+"-"+nowmonth+"-"+nowday;
		return result;
	}
    
    public void gotoMainView()
    {   	
    	setContentView(R.layout.main);
    	//準備URL
		final String url="http://"+IP_ADDRESS+":8080/jiudian/main.jsp"; 
		final Map<String,String> params=new HashMap<String,String>();
		params.put("params1","getGroupName");				
		//發送數據
		new Thread()
		{
			public void run()
			{
				String msgStr=HttpUploadUtil.postWithoutFile(url, params);//將url和參數傳給jsp
				Bundle b=new Bundle();
    			//將內容字符串放進數據Bundle中
    			b.putString("msg", msgStr);
    			//創建消息對像
    			Message msg=new Message();
    			//設置數據Bundle到消息中
    			msg.setData(b);
    			//設置消息的what值
    			msg.what=Constant.MAINVIEW; 
    			//發送消息
    			hd.sendMessage(msg);
			}
		}.start();
		
    	TextView matv1=(TextView)findViewById(R.id.mainTextView01);
    	matv1.setOnClickListener(
    			new OnClickListener()
    			{

					@Override
					public void onClick(View v) {

						gotoKefang();
					}
    				
    			}
    	);
    	
    	TextView matv2=(TextView)findViewById(R.id.mainTextView02);
    	matv2.setOnClickListener(
    			new OnClickListener()
    			{

					@Override
					public void onClick(View v) {

						gotoKefang();
					}
    				
    			}
    	);
    	TextView matv3=(TextView)findViewById(R.id.mainTextView03);
    	matv3.setOnClickListener(
    			new OnClickListener()
    			{

					@Override
					public void onClick(View v) {

						gotoMeetingroom();
					}
    				
    			}
    	);
    	TextView matv4=(TextView)findViewById(R.id.mainTextView04);
    	matv4.setOnClickListener(
    			new OnClickListener()
    			{

					@Override
					public void onClick(View v) {

						gotoMeetingroom();
					}
    				
    			}
    	);
    	
		ImageButton next=(ImageButton)findViewById(R.id.mainImageButton01);
        next.setOnClickListener(
        		new OnClickListener()
        		{

					@Override
					public void onClick(View v) {
						
						gotoUserInfo();
					}
        			
        		}
        		);
    	
    	curr=WhichView.MAIN_VIEW;
    }
    
	
    public void gotoLoginView()
    {
    	setContentView(R.layout.login);
        
        Button b=(Button)this.findViewById(R.id.loginButton01);//登錄鍵監聽
        b.setOnClickListener
        (
        	new OnClickListener()
        	{
				@Override
				public void onClick(View v) 
				{					
					//準備URL
					final String url="http://"+IP_ADDRESS+":8080/jiudian/receive.jsp"; 
					
					//準備參數列表
					EditText et1=(EditText)findViewById(R.id.loginEditText01);
					EditText et2=(EditText)findViewById(R.id.loginEditText02);
					
					final Map<String,String> params=new HashMap<String,String>();
					params.put("params1",et1.getText().toString());				
					params.put("params2", et2.getText().toString());
					
					String username=et1.getText().toString();
					//獲取SharedPreferences
					   SharedPreferences sp=MainActivity.this.getSharedPreferences("actm", Context.MODE_PRIVATE);
					   //從SharedPreferences中讀取上次訪問的時間
					        String uname=sp.getString
					        (
					        		username,   //鍵值
					        		null    //默認值
					        );
//					        if(uname==null)
//					        {
//					        	Toast.makeText(MainActivity.this, "請登錄", Toast.LENGTH_SHORT).show();
//					        	gotoLoginView();
//					        }
					        //else
					        //{
					        	SharedPreferences.Editor editor=sp.edit();
						        editor.putString("uname",username);
						        editor.commit();
					        //}  
					//發送數據
					new Thread()
					{
						public void run()
						{
							String msgStr=HttpUploadUtil.postWithoutFile(url, params);//將url和參數傳給jsp
							Bundle b=new Bundle();
		        			//將內容字符串放進數據Bundle中
		        			b.putString("msg", msgStr);
		        			//創建消息對像
		        			Message msg=new Message();
		        			//設置數據Bundle到消息中
		        			msg.setData(b);
		        			//設置消息的what值
		        			msg.what=Constant.LOGINVIEW;
		        			//發送消息
		        			hd.sendMessage(msg);
						}
					}.start();	

				}        		
        	}
        );
        Button re=(Button)this.findViewById(R.id.loginButton02);//註冊鍵監聽
        re.setOnClickListener(
        	new OnClickListener()
        	{

				@Override
				public void onClick(View v) {
					gotoRegister();//去註冊界面
				}
        		 
        	}
        );
//        ImageButton next=(ImageButton)findViewById(R.id.loginImageButton01);
//        next.setOnClickListener(
//        		new OnClickListener()
//        		{
//
//					@Override
//					public void onClick(View v) {
//						
//						gotoUserInfo();
//					}
//        			
//        		}
//        		);
    	
    	curr=WhichView.LOGIN_VIEW;
    }
    
    protected void gotoRegister() {
		
        setContentView(R.layout.register); 
    	
    	Button re=(Button)this.findViewById(R.id.registerButton01);
    	re.setOnClickListener(
    			new OnClickListener()
    			{

					@Override
					public void onClick(View v) {
						
						
						//準備URL
						final String url="http://"+IP_ADDRESS+":8080/jiudian/register.jsp"; 
						
						//準備參數列表
						final EditText etyhm=(EditText)findViewById(R.id.registerEditText01);
						final EditText etmm=(EditText)findViewById(R.id.registerEditText02);
						final EditText etmmqr=(EditText)findViewById(R.id.registerEditText03);
						final EditText ettelnum=(EditText)findViewById(R.id.registerEditText04);
						final EditText etrealname=(EditText)findViewById(R.id.registerEditText05);
						    
						final EditText etemail=(EditText)findViewById(R.id.registerEditText07);
						if(etyhm.getText().toString().matches(""))
						{
							Toast.makeText(MainActivity.this,"請輸入用戶名", Toast.LENGTH_SHORT).show();
						}
						else if(!(etyhm.getText().toString().matches("^[a-zA-Z][a-zA-Z0-9_]{5,9}$")))
						{
							Toast.makeText(MainActivity.this,"用戶名長度為6到10字節，允許數字下劃線", Toast.LENGTH_SHORT).show();
						}
						else if(etmm.getText().toString().matches(""))
						{
							Toast.makeText(MainActivity.this,"請輸入密碼", Toast.LENGTH_SHORT).show();
						}
						else if(etmm.getText().toString().length()<6)
						{
							Toast.makeText(MainActivity.this,"密碼長度不能小於6", Toast.LENGTH_SHORT).show();
						}
						else if(etmmqr.getText().toString().matches(""))
						{
							Toast.makeText(MainActivity.this,"請再次輸入密碼", Toast.LENGTH_SHORT).show();
						}
						else if(!etmm.getText().toString().trim().equals(etmmqr.getText().toString().trim()))
						{
							Toast.makeText(MainActivity.this,"密碼輸入不一致，請重新輸入", Toast.LENGTH_SHORT).show();
						}
						else if(ettelnum.getText().toString().matches(""))
						{
							Toast.makeText(MainActivity.this, "請輸入手機號碼", Toast.LENGTH_SHORT).show();
						}
						else if(!ettelnum.getText().toString().matches("^1[3,5]{1}[0-9]{1}[0-9]{8}$"))
						{
							Toast.makeText(MainActivity.this, "手機號碼格式不正確", Toast.LENGTH_SHORT).show();
						}
						
						else if(etrealname.getText().toString().matches(""))
						{
							Toast.makeText(MainActivity.this, "真實姓名不能為空", Toast.LENGTH_SHORT).show();
						}
//						else if(!etrealname.getText().toString().matches("[\u4e00-\u9fa5]"))
//						{
//							Toast.makeText(MainActivity.this, "姓名必須包含漢字", Toast.LENGTH_SHORT).show();
//						}
						else if(etemail.getText().toString().matches(""))
						{
							Toast.makeText(MainActivity.this, "請輸入郵箱", Toast.LENGTH_SHORT).show();
						}
						else if(!etemail.getText().toString().matches("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$"))
						{
							Toast.makeText(MainActivity.this, "郵箱格式不正確", Toast.LENGTH_SHORT).show();
						}
						else
						{ 
							
						final Map<String,String> params=new HashMap<String,String>();
						params.put("yonghuming", etyhm.getText().toString());				
						params.put("mm", etmm.getText().toString());
						params.put("telnum", ettelnum.getText().toString());
						params.put("realname", etrealname.getText().toString());
						params.put("email", etemail.getText().toString());
						RadioButton rbman=(RadioButton)findViewById(R.id.registerRadioButton01);
						if(rbman.isChecked())
						{
							sex="男";
						}
						System.out.print(sex);
						params.put("sex", sex);
						
						//發送數據
						new Thread()
						{
							public void run()
							{
								String msgStr=HttpUploadUtil.postWithoutFile(url, params);//將url和參數傳給jsp
								Bundle b=new Bundle();
			        			//將內容字符串放進數據Bundle中
			        			b.putString("msg", msgStr);
			        			//創建消息對像
			        			Message msg=new Message();
			        			//設置數據Bundle到消息中
			        			msg.setData(b);
			        			//設置消息的what值
			        			msg.what=Constant.REGISTER;
			        			//發送消息
			        			hd.sendMessage(msg);
							}
						}.start();
						}
					}
    				
    			}
    			
    	);
    	re=(Button)findViewById(R.id.registerButton02);
    	re.setOnClickListener(
    			new OnClickListener()
    			{

					@Override
					public void onClick(View arg0) {
						gotoLoginView();
					}
    				
    			}
    			);
    	
    	curr=WhichView.REGISTER_VIEW;
	}
	
    protected void gotoUserInfo() {
	
    	setContentView(R.layout.userinfo);
    	
    	SharedPreferences sp=MainActivity.this.getSharedPreferences("actm", Context.MODE_PRIVATE);
		   //從SharedPreferences中讀取上次訪問的時間
		        String uname=sp.getString
		        (
		        		"uname",   //鍵值
		        		null    //默認值
		        );
		        if(uname==null)
		        {
		        	Toast.makeText(MainActivity.this, "請登錄", Toast.LENGTH_SHORT).show();
		        	gotoLoginView();
		        }
		        else
		        {
		        	SharedPreferences.Editor editor=sp.edit();
			        editor.putString("uname",uname);
			        editor.commit();
		        }     
   //準備URL
		final String url="http://"+IP_ADDRESS+":8080/jiudian/userInfo.jsp";
     TextView tv1=(TextView)this.findViewById(R.id.userinfoTextView01);
     tv1.setText(uname);
     final Map<String,String> params=new HashMap<String,String>();
		params.put("param1", tv1.getText().toString());	
		new Thread()
		{
			public void run()
			{
				String msgStr=HttpUploadUtil.postWithoutFile(url, params);//將url和參數傳給jsp
				Bundle b=new Bundle();
 			//將內容字符串放進數據Bundle中
 			b.putString("msg", msgStr);
 			//創建消息對像
 			Message msg=new Message();
 			//設置數據Bundle到消息中
 			msg.setData(b);
 			//設置消息的what??
 			msg.what=Constant.USERINFO;
 			//發送消息
 			hd.sendMessage(msg);
			}
		}.start();
    	ImageButton next=(ImageButton)findViewById(R.id.userinfoImageButton01);
    	next.setOnClickListener(
    			new OnClickListener()
    			{

					@Override
					public void onClick(View v) {
						
						gotoAllResource();
					}
    				
    			}
    			);
    	ImageButton before=(ImageButton)findViewById(R.id.userinfoImageButton02);
    	before.setOnClickListener(
    			new OnClickListener()
    			{

					@Override
					public void onClick(View v) {
						
						gotoMainView();
					}
    				
    			}
    			);
    	Button uib1=(Button)findViewById(R.id.userinfoButton01);
    	uib1.setOnClickListener(
    			new OnClickListener()
    			{

					@Override
					public void onClick(View v) {
						
						//gotoModifyInfo();
						final String url="http://"+IP_ADDRESS+":8080/jiudian/modifyInfo.jsp";
						SharedPreferences sp=MainActivity.this.getSharedPreferences("actm", Context.MODE_PRIVATE);
						   //從SharedPreferences中讀取上次訪問的時間
						        String uname=sp.getString
						        (
						        		"uname",   //鍵值
						        		null    //默認值
						        );
						if(uname==null)
				     {
				     	Toast.makeText(MainActivity.this, "請登錄", Toast.LENGTH_SHORT).show();
				     	gotoLoginView();
				     }
				     else
				     {
				     	SharedPreferences.Editor editor=sp.edit();
					        editor.putString("uname",uname);
					        editor.commit();
				     }
						final TextView uitv1=(TextView)findViewById(R.id.userinfoTextView01);
				        uitv1.setText(uname);
						
						   EditText uiet2=(EditText)findViewById(R.id.userinfoEditText02);//密碼
	        			   EditText uiet4=(EditText)findViewById(R.id.userinfoEditText04);//手機號
	        			   EditText uiet5=(EditText)findViewById(R.id.userinfoEditText05);//真實姓名
	        			   RadioButton rbman=(RadioButton)findViewById(R.id.userinfoRadioButton01);//性別
	        			   RadioButton rbwoman=(RadioButton)findViewById(R.id.userinfoRadioButton02);
	        			   EditText uiet7=(EditText)findViewById(R.id.userinfoEditText07);//郵箱
	        			   
	        			
					        final Map<String,String> params=new HashMap<String,String>();
							params.put("param1", uitv1.getText().toString());	
							params.put("param2", uiet2.getText().toString());
							params.put("param3", uiet4.getText().toString());
							params.put("param4", uiet5.getText().toString());
							//params.put("param5", uiet2.getText().toString());//性別沒有傳！！
							
							if(rbman.isChecked())
							{
								params.put("param5","男");
							}
							else
							{
								params.put("param5","女");
							}
							params.put("param6", uiet7.getText().toString());
							new Thread()
							{
								public void run()
								{
									String msgStr=HttpUploadUtil.postWithoutFile(url, params);//將url和參數傳給jsp
									Bundle b=new Bundle();
					    			//將內容字符串放進數據Bundle中
					    			b.putString("msg", msgStr);
					    			//創建消息對像
					    			Message msg=new Message();
					    			//設置數據Bundle到消息中
					    			msg.setData(b);
					    			//設置消息的what值
					    			msg.what=Constant.MODIFYINFO;
					    			//發送消息
					    			hd.sendMessage(msg);
								}
							}.start();
	        		       
					}
    				
    			}
    	);
    	Button blogout=(Button)findViewById(R.id.userinfoButton03);//註銷按鈕添加監聽
    	blogout.setOnClickListener(
    			new OnClickListener()
    			{

					@Override
					public void onClick(View v) {
			
						gotoLoginView();
					}
    				
    			}
    	);
		curr=WhichView.USERINFO_VIEW;
	}
	 
    public void ininOrderListView(String allOrder)
    {
    	 //初始化資源分組名
	    listArray=allOrder.split("\\|");
    	final int count=listArray.length/3;//分組資源的數量
    	
    	//為ListView準備內容適配器
        BaseAdapter baa=new BaseAdapter()
        {
			@Override
			public int getCount() {
				return count;
			}

			@Override
			public Object getItem(int arg0) { return null; }

			@Override
			public long getItemId(int arg0) { return 0; }

			@Override
			public View getView(int arg0, View arg1, ViewGroup arg2) {							
				LinearLayout ll=new LinearLayout(MainActivity.this);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				
				//ll.setLayoutParams(null);
				ll.setBackgroundColor(getResources().getColor(R.color.yellow));
				
				TextView tv=new TextView(MainActivity.this);
				tv.setWidth(50);//訂單編號
				tv.setGravity(1);
				tv.setText(listArray[arg0*3]);
				tv.setTextSize(15);
				tv.setTextColor(getResources().getColor(R.color.black));
				ll.addView(tv);
				TextView tv1=new TextView(MainActivity.this);
				tv1.setWidth(170);//下單時間
				tv1.setText(listArray[arg0*3+1]);
				tv1.setTextSize(15);
				tv1.setGravity(1);
				tv1.setTextColor(getResources().getColor(R.color.black));
				ll.addView(tv1);	
				TextView tv2=new TextView(MainActivity.this);
				tv2.setWidth(85);//訂單狀態
				tv2.setText(listArray[arg0*3+2]);
				tv2.setTextSize(15);
				tv2.setGravity(1);
				tv2.setTextColor(getResources().getColor(R.color.black));
				ll.addView(tv2);	
				return ll; 
			}        		
        };
        
        ListView lvv=(ListView)findViewById(R.id.orderListView01);
        lvv.setAdapter(baa);//為ListView設置內容適配器
        
        //設置選項被單擊的監聽器
        lvv.setOnItemClickListener(
           new OnItemClickListener()
           {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {//重寫選項被單擊事件的處理方法
				
				String oid=listArray[arg2*3].toString().trim();

				gotoOrderDetail(oid);
			}        	   
           }
        );
    }

	protected void gotoAllResource() {
		
		setContentView(R.layout.allresource);
		
		final String url="http://"+IP_ADDRESS+":8080/jiudian/getAllResources.jsp";   
		
		final Map<String,String> params=new HashMap<String,String>();
		params.put("params1","params1");
		
		new Thread()
		{
			public void run()
			{
				String msgStr=HttpUploadUtil.postWithoutFile(url, params);  
				Bundle b=new Bundle();
				b.putString("msg", msgStr);
				Message msg=new Message();
				msg.setData(b);
				msg.what=Constant.ALLRESOURCE;
				hd.sendMessage(msg);
			}
		}.start();
		
		ImageButton next=(ImageButton)findViewById(R.id.allresourceImageButton01);
		next.setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						
						gotoOrderList();//去顯示所有訂單的界面的方法
					}
					
				}
				);
		ImageButton before=(ImageButton)findViewById(R.id.allresourceImageButton02);
		before.setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						
						gotoUserInfo();//去顯示當前用戶所有訂單的方法
					}
					
				}
				);
		curr=WhichView.ALLRESOURCE_VIEW;
	}
   
    protected void gotoKefang()
    {
    	setContentView(R.layout.resourcedividedbygroup);
    	
    	//ImageButton設置監聽
    	ImageButton ib1=(ImageButton)findViewById(R.id.resourcedividedbygroupImageButton01);
    	ib1.setOnClickListener(
    			new OnClickListener()
    			{
					@Override
					public void onClick(View v) {
						
						gotoOrderList();//去顯示當前用戶所有訂單的方法
					}
    				
    			}
    	);
    	
    	//ImageButton設置監聽
    	ImageButton ib2=(ImageButton)findViewById(R.id.resourcedividedbygroupImageButton02);
    	ib2.setOnClickListener(
    			new OnClickListener()
    			{
					@Override
					public void onClick(View v) {
						
						gotoUserInfo();//去顯示當前用戶所有信息的方法
					}
    				
    			}
    	);
		
		Spinner sp=(Spinner)findViewById(R.id.resourcedividedbygroupSpinner01);
		BaseAdapter ba=new BaseAdapter()
		{

			@Override
			public int getCount() {
	
				return 4;
			}

			@Override
			public Object getItem(int arg0) {

				return null;
			}

			@Override
			public long getItemId(int arg0) {
	
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				TextView tv=new TextView(MainActivity.this);
				tv.setText(getResources().getText(kfmsg2[position]).toString());
				tv.setTextSize(15);//設置字體大小
				tv.setTextColor(R.color.black);//設置字體顏色
				return tv;
			}
			
		};
		sp.setAdapter(ba);//設置適配器
		//設置監聽
		sp.setOnItemSelectedListener(
				new OnItemSelectedListener()
				{

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,  
							int arg2, long arg3) {

					    TextView tv=(TextView)arg1;
					    msgscale=tv.getText().toString();
					    
					    final String url="http://"+IP_ADDRESS+":8080/jiudian/resourceDividedByGroup.jsp";  
					    final Map<String,String> params=new HashMap<String,String>();      
			
						params.put("params1",msgscale);
						
						new Thread()
						{
							public void run()
							{
								String msgStr=HttpUploadUtil.postWithoutFile(url, params);//將url和參數傳給jsp
								Bundle b=new Bundle();
				    			//將內容字符串放進數據Bundle中
				    			b.putString("msg", msgStr); 
				    			//創建消息對像
				    			Message msg=new Message();
				    			//設置數據Bundle到消息中
				    			msg.setData(b);
				    			//設置消息的what值
				    			msg.what=Constant.KFVIEW;  
				    			//發送消息
				    			hd.sendMessage(msg);
							}
						}.start();	
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						
						
					}
					
				}
				);
		curr=WhichView.KEFANG_VIEW;
    }
    
    
    protected void gotoMeetingroom()//顯示會議室所有資源的方法
    {
    	setContentView(R.layout.resourcedividedbygroup1);
		Log.d("11111", "11111");
		Spinner sp=(Spinner)findViewById(R.id.resourcedividedbygroup1Spinner01);
		BaseAdapter ba=new BaseAdapter()
		{

			@Override
			public int getCount() {
				
				return 7;
			}

			@Override
			public Object getItem(int arg0) {
				
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
			
				TextView tv=new TextView(MainActivity.this);
				tv.setText(getResources().getText(hysmsg[position]).toString());
				tv.setTextSize(15);//設置字體大小
				tv.setTextColor(R.color.black);//設置字體顏色
				return tv;
			}
			
		};
		sp.setAdapter(ba);
		sp.setOnItemSelectedListener(
				new OnItemSelectedListener()
				{

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
				
					    TextView tv=(TextView)arg1;
					    String msgscalemeeting=tv.getText().toString();
					    final String url="http://"+IP_ADDRESS+":8080/jiudian/resourceDividedByGroup.jsp";
					    final Map<String,String> params=new HashMap<String,String>();				
						params.put("params1",msgscalemeeting);
						
						new Thread()
						{
							public void run()
							{
								String msgStr=HttpUploadUtil.postWithoutFile(url, params);//將url和參數傳給jsp
								Bundle b=new Bundle();
				    			//將內容字符串放進數據Bundle中
				    			b.putString("msg", msgStr);
				    			//創建消息對像
				    			Message msg=new Message();
				    			//設置數據Bundle到消息中
				    			msg.setData(b);
				    			//設置消息的what值
				    			msg.what=Constant.MEETINTVIEW;
				    			//發送消息
				    			hd.sendMessage(msg);
							}
						}.start();	
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						
						
					}
					
				}
				);
		ImageButton ib11=(ImageButton)findViewById(R.id.resourcedividedbygroup1ImageButton01);
		ib11.setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						gotoOrderList();
						
					}
					
				}
		);
		ImageButton ib12=(ImageButton)findViewById(R.id.resourcedividedbygroup1ImageButton02);
		ib12.setOnClickListener(
				new OnClickListener()
				{

					@Override
					public void onClick(View v) {
						gotoUserInfo();
						
					}
					
				}
		);
		curr=WhichView.MEETINGROOM_VIEW;
    }
	
    protected void gotoOrderList()
    {    	
    	setContentView(R.layout.order);

    	SharedPreferences sp=MainActivity.this.getSharedPreferences("actm", Context.MODE_PRIVATE);
		   //從SharedPreferences中讀取上次訪問的時間
		        String uname=sp.getString
		        (
		        		"uname",   //鍵值
		        		null    //默認值
		        );
		        if(uname==null)
		        {
		        	Toast.makeText(MainActivity.this, "請登錄", Toast.LENGTH_SHORT).show();
		        	gotoLoginView();
		        }
		        else
		        {
		        	SharedPreferences.Editor editor=sp.edit();
			        editor.putString("uname",uname);
			        editor.commit();
		        }       
		final String url="http://"+IP_ADDRESS+":8080/jiudian/orderclient.jsp";//準備URL
        final Map<String,String> params=new HashMap<String,String>();//準備參數
        params.put("param1", uname);
        System.out.println(uname);
        
        //開啟線程發送消息
        new Thread()
        {
        	public void run()
        	{
        		String msgStr=HttpUploadUtil.postWithoutFile(url, params);
        		Bundle b=new Bundle();
        		b.putString("msg", msgStr);
        		Message msg=new Message();
        		msg.setData(b);
        		msg.what=Constant.ORDER; 
        		hd.sendMessage(msg); 
        	}
        }.start();
        
        //ImageButton設置監聽
        ImageButton od=(ImageButton)findViewById(R.id.orderImageButton02);
    	od.setOnClickListener(
    			new OnClickListener()
    			{

					@Override
					public void onClick(View v) {
						gotoAllResource();//去顯示所有資源的界面
					}
    				
    			}
    	);
        curr=WhichView.ORDERLIST_VIEW;
    }    
    
    protected void gotoOrderDetail(String oid)
    {
    	final String oid1=oid;
    	//設置當前頁面為所選對應訂單編號的詳細信息
    	setContentView(R.layout.orderdetail);
    	//準備url
    	final String url="http://"+IP_ADDRESS+":8080/jiudian/orderdetail.jsp";
    	//準備參數
    	final Map<String,String> params=new HashMap<String,String>();
    	params.put("param1",oid);
    	System.out.println(oid);
    	//創建發送消息的線程
    	new Thread()
    	{
    		public void run()
    		{
    			String msgStr=HttpUploadUtil.postWithoutFile(url, params);
    			Bundle b=new Bundle();
    			b.putString("msg",msgStr);
    			Message msg=new Message();
    			msg.setData(b);
    			msg.what=Constant.ORDERDETAIL;
    			hd.sendMessage(msg);
    		}
    	}.start();
    	
    	//ImageButton設置監聽
    	ImageButton odib=(ImageButton)findViewById(R.id.orderdetailImageButton02);
    	odib.setOnClickListener(
    			new OnClickListener()
    			{

					@Override
					public void onClick(View v) {
						  
						gotoOrderList();//去顯示當前用戶所有訂單的界面
					}
    				
    			}
    	);
    	Button b=(Button)findViewById(R.id.orderdetailButton01);
    	b.setOnClickListener(
    			new OnClickListener()
    			{

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						final String url="http://"+IP_ADDRESS+":8080/jiudian/DeleteOlist.jsp"; 
						final Map<String,String> params=new HashMap<String,String>();
						params.put("param1",oid1);
						
						new Thread()
						{
							public void run()
							{
								String msgStr=HttpUploadUtil.postWithoutFile(url, params);
				    			Bundle b=new Bundle();
				    			b.putString("msg",msgStr);
				    			Message msg=new Message();
				    			msg.setData(b);
				    			msg.what=Constant.DELETE;
				    			hd.sendMessage(msg);
							}
						}.start();
					}
    				
    			}
    			);
    	
    	curr=WhichView.ORDERDETAIL_VIEW;
    }
}

