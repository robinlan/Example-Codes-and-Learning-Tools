package com.bn.reader;

import java.io.File;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ListViewUtills
{
	String currentPath;//當前路徑
	ReaderActivity reader;//Activity的引用
	
	Boolean judgeTimes=false;//判讀是否是第一次打開軟件
	
	public ListViewUtills(ReaderActivity reader)
	{
		this.reader=reader;//拿到引用
	}
	//用來獲取路徑下的文件數組
	public File[] getFiles(String filePath)
	{
		File[] files=new File(filePath).listFiles();//獲取當前目錄下的文件    	
		return files;
	}
	//將查找 文件列表添加到ListView中
	public void intoListView(final File[] files,final ListView lv)
	{
		if(files!=null)//當文件列表不為空時
		{
			if(files.length==0)//當前目錄為空
			{				
				File cf=new File(currentPath);//獲取當前文件列表的路徑對應的文件
				cf=cf.getParentFile();//獲取父目錄文件
				currentPath=cf.getPath();//記錄當前文件列表路徑
				Toast.makeText
				(
						reader, 
						"該文件夾為空！！", 
						Toast.LENGTH_SHORT
				).show();  
			}
			else
			{
				BaseAdapter ba=new BaseAdapter()//創建適配器
				{
					@Override
					public int getCount() {
						return files.length;
					}
					@Override
					public Object getItem(int position) {
						return null;
					}
	
					@Override
					public long getItemId(int position) {				
						return 0;
					}
		        			
					@Override
					public View getView(int arg0, View arg1, ViewGroup arg2) {
		        				
						//初始化LinearLayout
						LinearLayout ll=new LinearLayout(reader);
						ll.setOrientation(LinearLayout.HORIZONTAL);//設置朝向	
						ll.setPadding(5,5,5,5);//設置四周留白
	
						//初始化ImageView
						ImageView  ii=new ImageView(reader);
						String s=files[arg0].getPath();
						File f=new File(s);//獲得文件對像
						char c[]=s.toCharArray();
						int i=s.length();
						if(f.isDirectory())//存在分支
						{
							ii.setImageDrawable(reader.getResources().getDrawable(R.drawable.sl_file));//設置圖片
						}else if(c[i-1]=='t'&&c[i-2]=='x'&&c[i-3]=='t')
						{
							ii.setImageDrawable(reader.getResources().getDrawable(R.drawable.sl_txt));
						}
						else
						{
							ii.setImageDrawable(reader.getResources().getDrawable(R.drawable.sl_else));
						}
						ii.setScaleType(ImageView.ScaleType.FIT_XY);//按照原圖比例
						ii.setLayoutParams(new Gallery.LayoutParams(60,60));//圖片的大小設置
						ll.addView(ii);//添加到LinearLayout中
								
						//初始化TextView
						TextView tv=new TextView(reader);
						tv.setText(files[arg0].getName());//添加文件名稱
						tv.setTextSize(18);//設置字體大小
						tv.setTextColor(reader.getResources().getColor(R.color.white));//設置字體顏色
						tv.setPadding(5,5,5,5);//設置四周留白
						tv.setGravity(Gravity.RIGHT);
						ll.addView(tv);//添加到LinearLayout中				
						return ll;
					}        	
				};
				lv.setAdapter(ba);//設置適配器
		            	
				lv.setOnItemClickListener//設置選中菜單的監聽器
				(
					new OnItemClickListener()
					{
						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
					
							File f=new File(files[arg2].getPath());//獲得當前點擊的文件對像
							if(f.isDirectory())//存在分支
							{
								currentPath=files[arg2].getPath();//獲取路徑
								File[] fs=getFiles(currentPath);//獲取當前路徑下所有子文件
								intoListView(fs,lv);//將子文件列表填入ListView中
							}
							else 
							{
								reader.saveCurrentData();//換書之前（即：Constant.FILE_PATH變化之前），將當前書的全部信息存入數據庫
								
								String s=files[arg2].getPath();//獲取路徑
								char c[]=s.toCharArray();
		        				int i=s.length();
		        				if(c[i-1]=='t'&&c[i-2]=='x'&&c[i-3]=='t')
		        				{
		        					
		        					Constant.FILE_PATH=files[arg2].getPath();//獲取路徑
									Constant.TEXTNAME=files[arg2].getName();//獲取文件的名字
		        					Constant.CONTENTCOUNT=TextLoadUtil.getCharacterCount(Constant.FILE_PATH);//調用getCharacterCount方法   					
		        					//當選擇另一本書時，數據庫中查找，是否已經讀過這本書的記錄
		        					try
		        					{
		        						judgeTimes=SQLDBUtil.judgeIsWhichTime(Constant.FILE_PATH);
		        						System.out.println("judgeTimes="+judgeTimes+"####");
		        					}catch(Exception e)
		        					{
		        						e.printStackTrace();
		        					}
		        					
		        					if(judgeTimes)//如果是第一次打開這本書，
		        					{
		        						reader.saveCurrentData();//換書之後，當前信息存入數據庫BookRecord，否則無法存書籤(沒有班級，哪裡來的學生)
		        						Constant.CURRENT_LEFT_START=0;
			        					Constant.CURRENT_PAGE=0;
			        					reader.goToReaderView();
		        					}else//否則，在數據庫中取出數據
		        					{
		        						try
		        						{
		        							//在數據庫中取出hashMap
		        							byte[] data=SQLDBUtil.selectRecordData(Constant.FILE_PATH);
		        							//為readerView中的hashMap
		        							reader.readerView.currBook=SQLDBUtil.fromBytesToListRowNodeList(data);//將byte型數據轉化為hashMap型的數據
		        							int page=SQLDBUtil.getLastTimePage(Constant.FILE_PATH);//得到這本書上一次讀到的位置
		        							int fontsize=SQLDBUtil.getLastTimeFontSize(Constant.FILE_PATH);//得到上一次的字體大小
		        							if(fontsize!=Constant.TEXT_SIZE)//如果此次打開與上一次打開時，字體大小不同
		        							{
		        								reader.updataBookMarkAndHashMap();//更新書籤或者是hashMap
		        								
		        							}

		        							reader.readerView.currRR=reader.readerView.currBook.get(page);//根據hashMap的索引取出ReadRecord的對象（記錄當前書頁的左上點索引）
		        			        		Constant.CURRENT_LEFT_START=reader.readerView.currRR.leftStart;//為當前書頁左上索引賦值
		        			        		Constant.CURRENT_PAGE=reader.readerView.currRR.pageNo;//為當前書頁的page賦值      		
		        						}catch(Exception e)
		        						{
		        							e.printStackTrace();
		        						}
		        						
			        					reader.goToReaderView();
		        					}
		        				}else
		        				{
		        					Toast.makeText
		        					(
		        						reader, 
		        						"請打開.txt文件", 
		        						Toast.LENGTH_SHORT
		        					).show();
		        				}
		        			}        						
						}
					}
				);
			}    
		}
		else
		{
			File cf=new File(currentPath);//獲取當前文件列表的路徑對應的文件
			cf=cf.getParentFile();//獲取父目錄文件
			currentPath=cf.getPath();//記錄當前文件列表路徑
			Toast.makeText
			(
				reader, 
				"已經到根目錄了", 
				Toast.LENGTH_SHORT
			).show();
		}
	}
}

