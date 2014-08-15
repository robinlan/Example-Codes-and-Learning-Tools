package com.bn.reader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DownLoad
{
	String []txtName;//存放目錄列表中的名字
	String listName;//所讀取目錄的內容字符串
	ListView lv;//聲明ListView的引用
	ReaderActivity ra;//Activity的引用
	
	public DownLoad(String path,ReaderActivity ra)//用來設置下載列表
	{
		this.ra=ra;//拿到Activity 的引用
		ra.goToBackground();
		lv=(ListView)ra.findViewById(R.id.background);
		listName=getTxtInfo(path);
		initListView(listName);//初始化文本目錄列表
		
	}
	public String getTxtInfo(String name)
	{
		String result=null;
		try
		{
			URL url=new URL(Constant.ADD_PRE+name);//獲取連接URL
			URLConnection uc=url.openConnection();//開啟連接
			InputStream is=uc.getInputStream();//通過連接打開輸入流
			int ch=0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();//開啟字節數組輸出流
			while((ch=is.read())!=-1)//在讀取結束之前，將每次讀取的結果存入輸出流中
		    {
		      	baos.write(ch);
		    }      
		    byte[] bb=baos.toByteArray();//將輸出流的內容導入到bb字節數組中
		    baos.close();//關閉輸出流
		    is.close();//關閉輸入流 
		    result=new String(bb,"UTF-8");//將字節數組中的內容按照"UTF-8"編碼
		    result=result.replaceAll("\\r\\n","\n");//並用換行符替換回車符
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
	public void initListView(String s)
	{
		txtName=s.split("\\|");//給文件名字數組賦值
		final int i=txtName.length/2;
		
		BaseAdapter ba=new BaseAdapter()//創建適配器
		{
			@Override
			public int getCount() {
				return i;
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
				LinearLayout ll=new LinearLayout(DownLoad.this.ra);//創建LinearLayout
				ll.setOrientation(LinearLayout.HORIZONTAL);//設置朝向	
				ll.setPadding(5,5,5,5);//設置四周留白

				//初始化ImageView
				ImageView  ii=new ImageView(DownLoad.this.ra);//創建ImageView
				ii.setImageDrawable(ra.getResources().getDrawable(R.drawable.sl_txt));//設置圖片
				ii.setScaleType(ImageView.ScaleType.FIT_XY);//按照原圖比例
				ii.setLayoutParams(new Gallery.LayoutParams(60,60));//Image的大小設置
				ll.addView(ii);//添加到LinearLayout中
						
				//初始化TextView
				TextView tv=new TextView(DownLoad.this.ra);//創建TextView
				tv.setText(txtName[arg0*2]);//添加文件名稱
				tv.setTextSize(18);//設置字體大小
				tv.setTextColor(DownLoad.this.ra.getResources().getColor(R.color.white));//設置字體顏色
				tv.setPadding(5,5,5,5);//設置四周留白
				tv.setGravity(Gravity.RIGHT);
				ll.addView(tv);//添加到LinearLayout中
				
				return ll;
			}        	
		};
		lv.setAdapter(ba);//設置適配器
	
	}
	public void downFile(String urlStr,String path,String fileName){
		InputStream inputStream=null;//輸入流引用
		FileUtils fileUtils=new FileUtils(ra);//創建文件下載工具類  
		try {
			if(fileUtils.isFileExist(path+fileName)){//如果存在就不再下載
				Toast.makeText
				(
						ra, 
						"已經存在該文件無需下載"
						,Toast.LENGTH_SHORT 
				).show();
			}else{
				inputStream=fileUtils.getInputStreamFromUrl(urlStr);//通過連接獲取輸入流
				File resultFile=fileUtils.writeToSDFromInput(path, fileName, inputStream);//把輸入流中的文件寫入SD卡
				try{
				inputStream.close();//完成後關閉輸入流
				}catch(IOException e){
					e.printStackTrace();
				}
				if(resultFile==null){
					Toast.makeText//如果文本為?仗腰拘畔?
					(
							ra, 
							"該文件為空"
							,Toast.LENGTH_SHORT 
					).show();
				}
			}
		}catch (IOException e){
			e.printStackTrace();
		}	
	}
}
