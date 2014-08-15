package com.bn.lccx;

import java.util.Vector;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import static com.bn.lccx.LoadUtil.*;
enum WhichView {MAIN_MENU,ZZCX_VIEW,CCCX_VIEW,CZCCCX_VIEW,LIST_VIEW,PASSSTATION_VIEW,
	             CCTJ_VIEW,CZTJ_VIEW,GXTJ_VIEW,FJGN_VIEW,WELCOME_VIEW,ABOUT_VIEW,HELP_VIEW}
public class LCCXActivity extends Activity 
{
	WelcomeView wv;//進入歡迎界面
	WhichView curr;//當前枚舉值	
	static int flag;//設置頁面的標誌位　　　０　站站查詢　１　車次查詢　２　車站查詢	
	
	
	String[][]msgg=new String[][]{{""}};//聲明引用
	
	
	String s1[];
	String s2[];
	
	
	Handler hd=new Handler()//聲明消息處理器
	{
			@Override
			public void handleMessage(Message msg)//重寫方法
        	{
        		switch(msg.what)
        		{
	        		case 0://進入歡迎界面
	        			goToWelcomeView();
	        			
	        		break;
	        		case 1://進入菜單界面
	        			goToMainMenu();       			
	        		break;
	        		case 2://進入關於界面
	        			setContentView(R.layout.about);
	        	    	curr=WhichView.ABOUT_VIEW;
	        			break;
	        		case 3://進入幫助界面
	        			setContentView(R.layout.help);
	        	    	curr=WhichView.HELP_VIEW;
	        			break;
	        		
        		}
        	}
	};
	
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        //設置為全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//設置橫屏模式
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);		
		CreatTable.creattable();//建表
		iniTLisit();//初始化數組
		this.hd.sendEmptyMessage(0);						//發送消息進入歡迎界面
    }
	
	 public void goToWelcomeView()
	    {
	    	if(wv==null)//如果該對像沒創建則創建
	    	{
	    		wv=new WelcomeView(this);
	    	}
	    	setContentView(wv);
	    	curr=WhichView.WELCOME_VIEW;//標識當前所在界面
	    }
	public void goToMainMenu()//去主菜單
	 {	
	      	setContentView(R.layout.main);	
	      	curr=WhichView.MAIN_MENU;
			//拿到主菜單中個按鈕的引用
			ImageButton ibzzcx=(ImageButton)findViewById(R.id.ibzzcx);
			ImageButton ibcccx=(ImageButton)findViewById(R.id.ibcccx);
			ImageButton ibczcccx=(ImageButton)findViewById(R.id.ibczcccx);
			ImageButton ibfjgn=(ImageButton)findViewById(R.id.ibfjgn);
			ImageButton ibabout=(ImageButton)findViewById(R.id.about_button);
			ImageButton ibhelp=(ImageButton)findViewById(R.id.help_button);
			ibabout.setOnClickListener//關於按鈕的監聽
			(
			   new OnClickListener()
			   {
				public void onClick(View v) 
				{
					
					hd.sendEmptyMessage(3);//發消息進入關於界面					
				}
			   }
			);
			ibhelp.setOnClickListener//幫助查詢的監聽
			(
			   new OnClickListener()
			   {
				public void onClick(View v) 
				{

					hd.sendEmptyMessage(2);	//發消息進入幫助界面
				}
			   }
			);
			ibzzcx.setOnClickListener//站站查詢按鈕的監聽
			(
			   new OnClickListener()
			   {
				public void onClick(View v) 
				{
					goTozzcxView();//進入站站查詢模塊
				}
			   }
			);
			ibcccx.setOnClickListener//車次查詢按鈕的監聽
			(
			   new OnClickListener()
			   {
				public void onClick(View v) 
				{
					goTocccxView();//進入車次查詢模塊
				}   
			   }
			);
			ibczcccx.setOnClickListener//?嫡舅�窒蚇漣擬�?
			(
			   new OnClickListener()
			   {
				public void onClick(View v) 
				{
					goToczcccxView();//進入車站查詢模塊
				}   
			   }
			); 
			ibfjgn.setOnClickListener//附加功能按鈕的監聽
			(
			   new OnClickListener()
			   {
				public void onClick(View v) 
				{
                    goTofjgnView();//進入附加功能模塊
				}
			   }
			);
	 }
	 public void goTozzcxView()//去站站查詢
	 {
		 setContentView(R.layout.zzcx);
		 curr=WhichView.ZZCX_VIEW;
		 flag=0;//標誌位
		
		 Button bcx=(Button) findViewById(R.id.zzcxbt);//查詢按鈕
		 Button bfh=(Button) findViewById(R.id.zzcxfhbt);//返回按鈕
		
		 iniTLisitarray(R.id.EditText01);//為各個車站輸入文本框添加適配器
		 iniTLisitarray(R.id.zzcxzzz);
		 iniTLisitarray(R.id.zzcxzdz); 

		 final CheckBox zzzcx=(CheckBox)findViewById(R.id.zzcxzzzbt);//中轉站復選框的引用
		 		 
		 bcx.setOnClickListener//為查詢按鈕添加監聽
		 (
			new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					if(!isLegal())
					{
						return;
					}

					AutoCompleteTextView zzcx_cfz = (AutoCompleteTextView) findViewById(R.id.EditText01);//出發站
					AutoCompleteTextView zzcx_zzz = (AutoCompleteTextView) findViewById(R.id.zzcxzzz);//中轉站
					AutoCompleteTextView zzcx_zdz= (AutoCompleteTextView) findViewById(R.id.zzcxzdz);//終點站
					
					String start=zzcx_cfz.getText().toString().trim();//得到相應的文本
					String end =zzcx_zdz.getText().toString().trim();
					String between=zzcx_zzz.getText().toString().trim();
					
					
					Vector<Vector<String>> temp;
					if(zzzcx.isChecked()==true)//如果中轉站查詢按鈕被選中，則進行中轉站查詢
					{
						 temp= LoadUtil.Zjzquery(start, between, end);//進行中轉站查詢
						 if(temp.size()==0)//如果查詢結果向量長度為0，則無查詢結果
							{
								Toast.makeText(LCCXActivity.this, "沒有你所查找的中轉站路線!!!", Toast.LENGTH_SHORT).show();
								zzcx_cfz.setText("");zzcx_zzz.setText("");zzcx_zdz.setText("");
								return;
							}
						 
					}else //否則進行站站查詢
					{
						temp= LoadUtil.getSameVector(start, end);
						if(temp.size()==0)
						{
							Toast.makeText(LCCXActivity.this, "對不起，沒有相關的列車信息!!!", Toast.LENGTH_SHORT).show();
							zzcx_cfz.setText("");zzcx_zzz.setText("");zzcx_zdz.setText("");
							return;
						}
					}

					zzcx_cfz=null;//將個輸入框的引用置為空
					zzcx_zdz=null;
					zzcx_zzz=null;
					
					String[][] msgInfo=new String[temp.elementAt(0).size()][temp.size()];//新建和結果向量對應的數組
					for(int i=0;i<temp.size();i++)
					{//for循環將結果向量中的數據導入數組
						for(int j=0;j<temp.elementAt(0).size();j++)
						{
							msgInfo[j][i]=(String)temp.get(i).get(j);
						}
					}
					goToListView(msgInfo);//切換到查詢結果顯示界面ListView界面
				}	
			}
		 );
		 bfh.setOnClickListener//為返回按鈕添加監聽
		 (
			new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					goToMainMenu();	//返回到主菜單界面
				}	
			}
		 );		
		 //建立適配器
			
	 }
	 public void goTocccxView()//去車次查詢界面
	 {
		 setContentView(R.layout.cccx);//切換到車次查詢界面
		 curr=WhichView.CCCX_VIEW;//標識界面
		 flag=1;
		 Button bcx=(Button) findViewById(R.id.cccx_cx);
		 Button bfh=(Button) findViewById(R.id.cccx_fh);
		 bcx.setOnClickListener
		 (
			new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					if(!isLegal())//如果各個輸入框不滿足規則則返回
					{						
						return;
					}
					AutoCompleteTextView cccx_cc= (AutoCompleteTextView) findViewById(R.id.cccxcc);//得到車次輸入框的引用
					 String cccxcc=cccx_cc.getText().toString().trim();//得到其中的文本
					 Vector<Vector<String>> temp= LoadUtil.trainSearch(cccxcc);//調用工具函數查詢得到結果集
					 cccx_cc=null;
					 if(temp.size()==0)//如果結果向量長度為0，說明沒有查詢結果，即無此車次相關信息
						{
							Toast.makeText(LCCXActivity.this, "沒有相關信息!!!", Toast.LENGTH_SHORT).show();
							return;
						}
					 String[][] msgInfo=new String[temp.elementAt(0).size()][temp.size()];//新建對應於向量的數組					 
						for(int i=0;i<temp.size();i++)//否則將向量中的數據導入對應的數組
						{
							for(int j=0;j<temp.elementAt(i).size();j++)
							{
								msgInfo[j][i]=(String)temp.get(i).get(j);
							}
						}						
						goToListView(msgInfo);//切換到結果顯示界面ListView界面
				}	
			}
		 );
		 bfh.setOnClickListener//為返回按鈕添加監聽
		 (
			new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					goToMainMenu();	//返回到菜單界面
				}	
			}
		 );
		 
	 }
	 public void goToczcccxView()//去車站所有車次查詢
	 {
		 setContentView(R.layout.czcx);//切換到車站查詢界面
		 curr=WhichView.CZCCCX_VIEW;//標識界面
		 flag=2;//標識所在界面為車次查詢界面
		 Button bcx=(Button) findViewById(R.id.czcx_cx);//拿到查詢按鈕的引用
		 Button bfh=(Button) findViewById(R.id.czcx_fh);//拿到返回按鈕的引用
		 
		 iniTLisitarray(R.id.czcxwb);//為車站文本框添加適配器來完成文本輸入的提示功能
		 bcx.setOnClickListener//為查詢按鈕添加監聽
		 (
			new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					if(!isLegal())//如果某個文本框不合規則，則返回
					{
						return;
					}
					AutoCompleteTextView czcx_czzm= (AutoCompleteTextView) findViewById(R.id.czcxwb);//拿到車站輸入框的引用
					String czcxczzm=czcx_czzm.getText().toString().trim();//得到對應文本框中的文本
					 Vector<Vector<String>> temp= stationSearch(czcxczzm);//調用工具函數查詢得到結果向量
					 czcx_czzm=null;
					 if(temp.size()==0)//如果結果向量的長度為0，說明沒有相關信息
						{
							Toast.makeText(LCCXActivity.this, "沒有相關信息!!!", Toast.LENGTH_SHORT).show();
							return;
						}
					 String[][] msgInfo=new String[temp.elementAt(0).size()][temp.size()];//否則創建對應於結果向量的數組
						for(int i=0;i<temp.size();i++)//將結果向量中的數據導入數組
						{
							for(int j=0;j<temp.elementAt(0).size();j++)
							{
								msgInfo[j][i]=(String)temp.get(i).get(j);
								
							}
						}
						
					goToListView(msgInfo);
				}	
			}
		 );
		 bfh.setOnClickListener//為返回按鈕添加監聽
		 (
			new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					goToMainMenu();	//切換到主菜單界面
				}	
			}
		 );
		 
	 }
	 public void goTofjgnView()//去附加功能界面
	 {
		 setContentView(R.layout.fjgnmenu);//切換到附加功能界面
		 curr=WhichView.FJGN_VIEW;//標識當前所在界面為附加功能界面
		 ImageButton ibcctj=(ImageButton)findViewById(R.id.ibcctj);//拿到車次添加按鈕引用
	     ImageButton ibcztj=(ImageButton)findViewById(R.id.ibcztj);//拿到車站添加按鈕引用
		 ImageButton ibgxtj=(ImageButton)findViewById(R.id.ibgxtj);//拿到關係添加按鈕的引用
		 ibcctj.setOnClickListener//車次添加按鈕的監聽
		 (
			   new OnClickListener()
			   {
				public void onClick(View v) 
				{
					goTocctjView();//去車次添加界面
				}
			   }
		 );
		 ibcztj.setOnClickListener//車站添加按鈕的監聽
		 (
			   new OnClickListener()
			   {
				public void onClick(View v) 
				{
					goTocztjView();//切換到車站添加界面
				}
			   }
		 );
		 ibgxtj.setOnClickListener//關係添加按鈕的監聽
		 (
			   new OnClickListener()
			   {
				public void onClick(View v) 
				{
					goTogxtjView();
				}
			   }
		 );
	 }
	 public void goTocctjView()//去車次添加界面
	 {
		 setContentView(R.layout.cctj);//切換界面
		 curr=WhichView.CCTJ_VIEW;//標識界面
		 Button bcctjtj=(Button)findViewById(R.id.cctj_tj);//拿到添加按鈕的一引用
		 Button bcctjfh=(Button)findViewById(R.id.cctj_fh);//拿到返回按鈕的引用
		 iniTLisitarray(R.id.cctj_sfz);//為始發站和終點站文本框添加適配器
		 iniTLisitarray(R.id.cctj_zdz);		 
		 final int tid=LoadUtil.getInsertId("train","Tid")+1;//拿到此時車站表中TID列的最大ID，然後加1得出要插入此車次的ID。
		 bcctjtj.setOnClickListener//為添加按鈕添加監聽
		 (
			new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					if(!isLegal())//判斷輸入框是否符合規則
					{
						return;
					}
					AutoCompleteTextView cctjcnm=(AutoCompleteTextView)findViewById(R.id.cctj_cm);//拿到個輸入框的引用
					 AutoCompleteTextView cctjclx=(AutoCompleteTextView)findViewById(R.id.cctj_lclx);
					 AutoCompleteTextView cctjcsf=(AutoCompleteTextView)findViewById(R.id.cctj_sfz);
					 AutoCompleteTextView cctjczd=(AutoCompleteTextView)findViewById(R.id.cctj_zdz);
					 String cnm=cctjcnm.getText().toString().trim();
					 String clx=cctjclx.getText().toString().trim();
					 String csf=cctjcsf.getText().toString().trim();
					 String czd=cctjczd.getText().toString().trim();
					 String sql="select * from train where Tname='" +cnm+//查看是否有該車次
					"'";
					Vector<Vector<String>> ss=query(sql);					
					if(ss.size()>0)
					{
						Toast.makeText(LCCXActivity.this, "對不起，已經有了此車次!!!", Toast.LENGTH_SHORT).show();
						return;
					}
					 sql ="select Sid from station where Sname='"+csf+"'";
					if(query(sql).size()==0)//查看是否有該車站
					{
						Toast.makeText(LCCXActivity.this, "對不起，該始發站不存在!!!", Toast.LENGTH_SHORT).show();
						return;
					}
					sql="select Sid from station where Sname='"+czd+"'";//查看是否有該車站
					if(query(sql).size()==0)
					{
						Toast.makeText(LCCXActivity.this, "對不起，該終點站不存在!!!", Toast.LENGTH_SHORT).show();
						return;
					}
					
					
					
					sql="insert into train values(" +
					tid +",'" +cnm+"','" +csf +"'" +",'" +czd +"','" +clx +"')";//添加關係
					if(!insert(sql))//如果失敗
					{
					Toast.makeText(LCCXActivity.this, "對不起，添加失敗!!!", Toast.LENGTH_SHORT).show();
						
					}else{//否則為添加成功
						Toast.makeText(LCCXActivity.this, "恭喜你，添加成功!!!", Toast.LENGTH_SHORT).show();
					}
					
				}	
			}
		 );
		 bcctjfh.setOnClickListener//為返回按鈕添加監聽
		 (
			new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
				  goTofjgnView();//返回到附加功能界面
				}	
			}
		 );
		 
	 }
	 public void goTocztjView()//去車站添加界面
	 {
		 setContentView(R.layout.cztj);//切換界面
		 curr=WhichView.CZTJ_VIEW;//標識界面
		 Button bcztjtj=(Button)findViewById(R.id.cztj_tj);//拿到添加按鈕的引用
		 Button bcztjfh=(Button)findViewById(R.id.cztj_fh);//拿到返回按鈕的引用
		
		 
		 
		 final int sid=LoadUtil.getInsertId("station","Sid")+1;//查出SId列中最大的ID，加1得到此時需要插入的車站的ID
		 bcztjtj.setOnClickListener//為添加按鈕添加監聽
		 (
			new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					if(!isLegal())
					{
						return;
					}
					 EditText cztjmc=(EditText)findViewById(R.id.et_cztj_czmc);//得到個輸入框中的引用
					 EditText cztjjc=(EditText)findViewById(R.id.et_cztj_czjc);
					String cnm=cztjmc.getText().toString().trim();//得到對應的文本
					String clx=cztjjc.getText().toString().trim();
					 if(!clx.matches("[a-zA-Z]+"))//正則式匹配，查看簡稱輸入框中的文本是否符合都是字母的規則
					{
						//發不匹配消息
						 Toast.makeText(LCCXActivity.this, "對不起，簡稱只能為字母!!!", Toast.LENGTH_SHORT).show();
							return;
					}
					
					String sql="select * from station where Sname='" +cnm+
					"'";
					Vector<Vector<String>> ss=query(sql);//查看該車站是否已經存在					
					if(ss.size()>0)//如果結果向量的長度大於0，說明已經有了該車了
					{
						Toast.makeText(LCCXActivity.this, "對不起，已經有了此車站!!!", Toast.LENGTH_SHORT).show();
						return;
					}
					sql="insert into station values(" +sid +	",'" +cnm +	"','" +	clx +"')";
					if(!insert(sql))//進行插入操作，如果是添加失敗
					{
					Toast.makeText(LCCXActivity.this, "對不起，添加失敗!!!", Toast.LENGTH_SHORT).show();
						return;
					}else{//否則為添加成功
						iniTLisit();
						Toast.makeText(LCCXActivity.this, "恭喜你，添加成功!!!", Toast.LENGTH_SHORT).show();
					}
				}	
			}
		 );
		 bcztjfh.setOnClickListener//為返回按鈕添加監聽
		 (
			new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
				  goTofjgnView();//返回到附加功能界面
				}	
			}
		 );
	 }
	 public void goTogxtjView()//去關係添加界面
	 {
		 setContentView(R.layout.gxtj);//切換界面
		 curr=WhichView.GXTJ_VIEW;//標識界面
		 Button bgxtjtj=(Button)findViewById(R.id.gxtj_tj);//拿到添加按鈕的引用
		 Button bgxtjfh=(Button)findViewById(R.id.gxtj_fh);//拿到返回按鈕的引用
		
		 iniTLisitarray(R.id.et_gxtj_zm);//為車站名字添加適配器
		 
		 bgxtjtj.setOnClickListener//為添加按鈕添加監聽
		 (
			new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
					EditText gxtjcnm=(EditText)findViewById(R.id.et_gxtj_cm);//拿到車名輸入框的引用
					AutoCompleteTextView gxtjclx=(AutoCompleteTextView)findViewById(R.id.et_gxtj_zm);//拿到站名輸入框的引用
					EditText gxtjcsf=(EditText)findViewById(R.id.et_gxtj_dzsj);//拿到到站時間輸入框的引用
					EditText gxtjczd=(EditText)findViewById(R.id.et_gxtj_kcsj);//拿到發車時間輸入框的引用
					
					String cnm=gxtjcnm.getText().toString().trim();//得到對應的文本信息
					String znm=gxtjclx.getText().toString().trim();
					String dct=gxtjcsf.getText().toString().trim();
					String fct=gxtjczd.getText().toString().trim();
					 
					int Rid=LoadUtil.getInsertId("relation","Rid")+1;//查出relation表中最大的ID加1得到當前插入的關係的ID
					 
					int cnmm=0;//車次對應的ID
					int cznm=0;//車站對應的ID
					
					if(!isLegal())
					{
						return;
					}					
					
					String sql = "select Tid "+
					"from train where Tname='"+cnm+"'";
					Vector<Vector<String>> ss=query(sql);					
					if(ss.size()>0)//得到車次對應的ID
					{
						cnmm=Integer.parseInt((String)ss.get(0).get(0));						
					}else if(ss.size()==0){
						Toast.makeText(LCCXActivity.this, "對不起，沒有該車!!!", Toast.LENGTH_SHORT).show();
					return;
					}
					sql="select Sid from station where Sname='"+znm+"'";				
					ss=query(sql);
					if(ss.size()>0)//得到車站對應的ID
					{
						cznm=Integer.parseInt((String)ss.get(0).get(0));						
					}
					else if(ss.size()==0){
						Toast.makeText(LCCXActivity.this, "對不起，沒有該站!!!", Toast.LENGTH_SHORT).show();
						return;
					}
					
					sql="select Rid from relation where Sid="+cznm+" and Tid="+cnmm;//進行查看該關係是否已經存在
				
					if(query(sql).size()>0)//如果已經存在
					{
					Toast.makeText(LCCXActivity.this, "對不起，該關係已經有了!!!", Toast.LENGTH_SHORT).show();
					return;
					}//否則進行插入操作
					sql="insert into relation values(" +
					Rid +
							"," +
							cnmm +
							"," +
							cznm +
							",'" +
							dct +
							"','" +
							fct +
							"')";
					
					if(!insert(sql))//如果插入失敗
					{
					Toast.makeText(LCCXActivity.this, "對不起，添加失敗!!!", Toast.LENGTH_SHORT).show();
					return;
					}else{
						Toast.makeText(LCCXActivity.this, "恭喜你，添加成功!!!", Toast.LENGTH_SHORT).show();
					}
				}	
			}
		 );
		 bgxtjfh.setOnClickListener//為返回按鈕添加監聽
		 (
			new OnClickListener()
			{
				@Override
				public void onClick(View v) 
				{
				  goTofjgnView();//返回到附加功能界面
				}	
			}
		 );
	 }
	 public void goToListView(String[][]mssg)//去ListView界面
	 {
		 	msgg=mssg;//賦值引用給全局數組，用來實現返回按鈕功能
	        setContentView(R.layout.listview);//切換界面
	        curr=WhichView.LIST_VIEW;//標識界面
	        final String[][]msg=mssg;//新建數組，並賦值
	        ListView lv_detail=(ListView)this.findViewById(R.id.ListView_detail);//拿到ListView的引用
	        BaseAdapter ba_detail=new BaseAdapter()//新建適配器
	        {
				@Override
				public int getCount() 
				{
					return msg[0].length;//得到列表的長度
				}
				@Override
				public Object getItem(int arg0){return null;}
				@Override
				public long getItemId(int arg0){return 0;}
				@Override
				public View getView(int arg0, View arg1, ViewGroup arg2)//為每一項添加內容
				{
					LinearLayout ll_detail=new LinearLayout(LCCXActivity.this);
					ll_detail.setOrientation(LinearLayout.HORIZONTAL);		//設置朝向	
					ll_detail.setPadding(5,5,5,5);//四周留白

					for(int i=0;i<msg.length;i++)//為每一行設置顯示的數據
					{					    
						TextView s= new TextView(LCCXActivity.this);
						s.setText(msg[i][arg0]);//TextView中顯示的文字
						s.setTextSize(14);//字體大小
						s.setTextColor(getResources().getColor(R.color.black));//字體顏色
						s.setPadding(1,2,2,1);//四周留白
						s.setWidth(60);//寬度
					    s.setGravity(Gravity.CENTER);
					    ll_detail.addView(s);//放入LinearLayout
					}
					return ll_detail;//將此LinearLayout返回
				}        	
	        };    
	        lv_detail.setAdapter(ba_detail);//將適配器添加進ListView
	        
	        lv_detail.setOnItemClickListener//為列表添加監聽
	        (
	           new OnItemClickListener()
	           {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,//當點擊列表中的某一項時調用此函數
						long arg3) //arg2為點擊的第幾項
				{
					String cccx=msg[0][arg2];//取出對應項中對應的車次信息
					
					 Vector<Vector<String>> temp= LoadUtil.getInfo(cccx);//查詢該車次經過的所有車站
					 if(temp.size()==0)//判斷是否有查詢結果
						{
							Toast.makeText(LCCXActivity.this, "沒有相關信息!!!", Toast.LENGTH_SHORT).show();
							return;
						}
					 String[][] msgInfo=new String[temp.elementAt(0).size()][temp.size()];//如果有則將結果放入對應的數組
						for(int i=0;i<temp.size();i++)
						{
							for(int j=0;j<temp.elementAt(0).size();j++)
							{
								msgInfo[j][i]=(String)temp.get(i).get(j);								
							}
						}
						msgg=msg;
						goToPassStationView(msgInfo);//切換到車次具體情況顯示界面

				}        	   
	           }
	        );
	 }
	//某列車經過的所有車站。去經過車站界面
	 public void goToPassStationView(String[][]mssg)
	 {
		 setContentView(R.layout.passstation);//切換界面
		 curr=WhichView.PASSSTATION_VIEW;//標識界面
	        ListView lv_detail=(ListView)this.findViewById(R.id.ListView_passstation);//得到ListView的引用
	        final String[][]msg=mssg;
	       
	        BaseAdapter ba_detail=new BaseAdapter()//新建適配器
	        {
				@Override
				public int getCount() 
				{
					return msg[0].length;//得到列表的長度
				}
				@Override
				public Object getItem(int arg0){return null;}
				@Override
				public long getItemId(int arg0){return 0;}
				@Override
				public View getView(int arg0, View arg1, ViewGroup arg2)
				{
					LinearLayout ll_detail=new LinearLayout(LCCXActivity.this);
					ll_detail.setOrientation(LinearLayout.HORIZONTAL);		//設置朝向	
					ll_detail.setPadding(5,2,2,4);//設置四周留白
					TextView []tv=
					{
					   new TextView(LCCXActivity.this),new TextView(LCCXActivity.this),new TextView(LCCXActivity.this)
					};
					for(int i=0;i<msg.length;i++)//設置每一行中顯示的數據
					{
						tv[i].setText(msg[i][arg0]);//每個TextView中的文本
						tv[i].setTextSize(13);//字體大小
						tv[i].setTextColor(getResources().getColor(R.color.black));//字體顏色
						tv[i].setPadding(5,2,3,2);//四周留白
						tv[i].setWidth(150);//寬度
					    tv[i].setGravity(Gravity.CENTER);
					    ll_detail.addView(tv[i]);//添加進LinearLayout

					}
					return ll_detail;//將此LinearLayout返回
				}        	
	        };    
	        lv_detail.setAdapter(ba_detail);//將適配器添加進列表
	        
	 }
	 //查看在某個界面中點擊查詢按鈕時，判斷輸入框是否為空
	 public boolean isLegal()
	 {
		 if(curr==WhichView.ZZCX_VIEW)//如果當前為站站查詢界面，對相應的文本框進行合法驗證
		 {
			EditText etcfz=(EditText)findViewById(R.id.EditText01);//出發站
			EditText etzzz=(EditText)findViewById(R.id.zzcxzzz);//中轉站
			EditText etzdz=(EditText)findViewById(R.id.zzcxzdz);//終點站
			CheckBox cbzzz=(CheckBox)findViewById(R.id.zzcxzzzbt);//中轉站復選框
			if(etcfz.getText().toString().trim().equals(""))//出發站為空
			{
				Toast.makeText(this, "出發站不能為空！！！",Toast.LENGTH_LONG).show();
				return false;
			}
			if(etzzz.getText().toString().trim().equals("")&&cbzzz.isChecked())//中轉站為空
			{
				Toast.makeText(this, "中轉站不能為空！！！",Toast.LENGTH_LONG).show();
				return false;
			}
			if(etzdz.getText().toString().trim().equals(""))//終點站為空
			{
				Toast.makeText(this, "終點站不能為空！！！",Toast.LENGTH_LONG).show();
				return false;
			}
			if(etcfz.getText().toString().trim().contentEquals(etzdz.getText().toString().trim()))//出發站和終點站相同
			{
				Toast.makeText(this, "出發站和終點站不能相同！！！",Toast.LENGTH_LONG).show();
				return false;
			}
			if(cbzzz.isChecked()&&etcfz.getText().toString().trim().contentEquals(etzzz.getText().toString().trim()))//出發站和中轉站相同
			{
				Toast.makeText(this, "出發站和中轉站不能相同！！！",Toast.LENGTH_LONG).show();
				return false;
		    }
			if(cbzzz.isChecked()&&etzdz.getText().toString().trim().contentEquals(etzzz.getText().toString().trim()))//終點站和中轉站
			{
				Toast.makeText(this, "終點站和中轉站不能相同！！！",Toast.LENGTH_LONG).show();
				return false;
			}
		 }
		 if(curr==WhichView.CCCX_VIEW)//如果當前為車次查詢界面
		 {
			 EditText etcccx=(EditText)findViewById(R.id.cccxcc);
			 if(etcccx.getText().toString().trim().contentEquals(""))
			 {
				 Toast.makeText(this, "車次不能為空！！！",Toast.LENGTH_LONG).show();
					return false;
			 }
		 }
		 if(curr==WhichView.CZCCCX_VIEW)//如果當前為車站車次查詢界面
		 {
			 EditText etczcccx=(EditText)findViewById(R.id.czcxwb);
			 if(etczcccx.getText().toString().trim().contentEquals(""))
			 {
				 Toast.makeText(this, "車站不能為空！！！",Toast.LENGTH_LONG).show();
					return false;
			 }
		 }
		 if(curr==WhichView.CCTJ_VIEW)//如果當前在車次添加
		 {
			 EditText et_cm=(EditText)findViewById(R.id.cctj_cm);//車名
			 EditText et_lclx=(EditText)findViewById(R.id.cctj_lclx);//列車類型
			 EditText et_sfz=(EditText)findViewById(R.id.cctj_sfz);//始發站
			 EditText et_zdz=(EditText)findViewById(R.id.cctj_zdz);//終點站
			 if(et_cm.getText().toString().trim().contentEquals(""))
			 {
				 Toast.makeText(this, "車名不能為空！！！",Toast.LENGTH_SHORT).show();
					return false;
			 }
			 if(et_lclx.getText().toString().trim().contentEquals(""))
			 {
				 Toast.makeText(this, "列車類型不能為空！！！",Toast.LENGTH_SHORT).show();
					return false;
			 }
			 if(et_sfz.getText().toString().trim().contentEquals(""))
			 {
				 Toast.makeText(this, "始發站不能為空！！！",Toast.LENGTH_SHORT).show();
					return false;
			 }
			 if(et_zdz.getText().toString().trim().contentEquals(""))
			 {
				 Toast.makeText(this, "終點站不能為空！！！",Toast.LENGTH_SHORT).show();
					return false;
			 }	 
		 }
		 if(curr==WhichView.CZTJ_VIEW)//如果當前在車站添加界面
		 {
			 EditText et_czmc=(EditText)findViewById(R.id.et_cztj_czmc);//車站名稱
			 EditText et_czjc=(EditText)findViewById(R.id.et_cztj_czjc);//車站簡稱
			 if(et_czmc.getText().toString().trim().contentEquals(""))
			 {
				 Toast.makeText(this, "車站名稱不能為空！！！",Toast.LENGTH_SHORT).show();
					return false;
			 }
			 if(et_czjc.getText().toString().trim().contentEquals(""))
			 {
				 Toast.makeText(this, "車站簡稱不能為空！！！",Toast.LENGTH_SHORT).show();
					return false;
			 }
		 }
		 if(curr==WhichView.GXTJ_VIEW)//如果當前在關係添加界面
		 {
			 EditText et_cm=(EditText)findViewById(R.id.et_gxtj_cm);//車名
			 EditText et_zm=(EditText)findViewById(R.id.et_gxtj_zm);//站名
			 
			 if(et_cm.getText().toString().trim().contentEquals(""))
			 {
				 Toast.makeText(this, "車名不能為空！！！",Toast.LENGTH_SHORT).show();
					return false;
			 }
			 if(et_zm.getText().toString().trim().contentEquals(""))
			 {
				 Toast.makeText(this, "站名不能為空！！！",Toast.LENGTH_SHORT).show();
					return false;
			 }
			 
		 }
		 return true;
	 } 
	 @Override
	  public boolean onKeyDown(int keyCode, KeyEvent e)//鍵盤監聽
	  { 
	    	if(keyCode!=4)//如果不是按下的返回按鈕時不做任何處理，直接返回
	    	{
	    		return false;
	    	}
	    	if(curr==WhichView.ZZCX_VIEW|| curr==WhichView.CCCX_VIEW||curr==WhichView.CZCCCX_VIEW||
	    			curr==WhichView.FJGN_VIEW)//站站查詢//車次查詢//車站查詢//附加功能
	    	{
	    		goToMainMenu();//返回到主菜單界面
	    		return true;
	    	}
	    	if(curr==WhichView.CCTJ_VIEW|| curr==WhichView.CZTJ_VIEW||curr==WhichView.GXTJ_VIEW)
	    	{//如果是車次添加、車站添加和關係添加界面
	    		goTofjgnView();//返回到附加界面
	    		return true;
	    	}	    
	    	
	    	if(curr==WhichView.MAIN_MENU)
	    	{
	    		System.exit(0);//如果是在主菜單中按下返回按鈕，則直接退出
	    		return true;
	    	}
	    	if(curr==WhichView.PASSSTATION_VIEW)//如果是在車次詳細情況顯示界面
	    	{
	    		goToListView(msgg);//返回到ListView界面
	    		return true;
	    	}
	    	
	    	if(curr==WhichView.LIST_VIEW)//如果是在ListView界面，則根據當前情況返回
	    	{
	    		if(flag==0)//如果為站站查詢界面
	    		{
	    			goTozzcxView();
		    		return true;
	    		}
	    		else if(flag==1)//如果是車次查詢界面
	    		{
	    			goTocccxView();
		    		return true;
	    		}
	    		else//否則為車站查詢界面
	    		{
	    			goToczcccxView();
		    		return true;
	    		}
	    	}
	    	if(curr==WhichView.ABOUT_VIEW||curr==WhichView.HELP_VIEW)//如果是關於和幫助界面
	    	{
	    		
	    		goToMainMenu();//返回到主菜單界面
	    		return true;
	    	}
	    	return false;
	 }
	 
	 public void iniTLisit()//初始化適配器中需要的數據的函數
	    {
		 
		 String sql = "select Sname from station";//查出所有車站名字
		 
		 Vector<Vector<String>> temp= LoadUtil.query(sql);
			String[][] msgInfo=new String[temp.get(0).size()][temp.size()];
			for(int i=0;i<temp.size();i++)
			{
				for(int j=0;j<temp.elementAt(0).size();j++)
				{
					msgInfo[j][i]=(String)temp.get(i).get(j);
				}
			}
			this.s1=msgInfo[0];//得到該數組
		 sql="select Spy from station";//查出所有車站名字的簡稱
		 
		 temp= LoadUtil.query(sql);

			msgInfo=new String[temp.elementAt(0).size()][temp.size()];
			for(int i=0;i<temp.size();i++)
			{
				for(int j=0;j<temp.elementAt(0).size();j++)
				{
					msgInfo[j][i]=(String)temp.get(i).get(j);
				}
			}
			this.s2=msgInfo[0];//得到該數組

	    }
	 
	 public void iniTLisitarray(int id)//為對應ID的輸入框添加適配器
	 {
		 CityAdapter<String> cAdapter = new CityAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,this.s1,this.s2); //設置提示框中的所有內容  
	        AutoCompleteTextView autoView=(AutoCompleteTextView)findViewById(id);//設置要添加提示信息的輸入框
	        autoView.setAdapter(cAdapter);   //添加適配器
	        autoView.setThreshold(1);
	        autoView.setDropDownHeight(100) ;
	        autoView.setDropDownBackgroundResource(R.color.gray);
	 }

	
}
