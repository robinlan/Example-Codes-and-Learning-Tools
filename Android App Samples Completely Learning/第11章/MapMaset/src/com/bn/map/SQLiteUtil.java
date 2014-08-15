package com.bn.map;

import java.util.Vector;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteUtil 
{
	static SQLiteDatabase sld;
	//創建或打開數據庫的方法
    public static void createOrOpenDatabase()
    {
    	try
    	{
	    	sld=SQLiteDatabase.openDatabase
	    	(
	    			"/data/data/com.bn.map/mydb", //當前應用程序只能在自己的包下創建數據庫
	    			null, 								//CursorFactory
	    			SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY //讀寫、若不存在則創建
	    	);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    }
  //關閉數據庫的方法
    public static void closeDatabase()
    {
    	try
    	{
	    	sld.close();    
    	}
		catch(Exception e)
		{
            e.printStackTrace();
		}
    }
    //建表
    public static void createTable(String sql)
    {
    	createOrOpenDatabase();//打開數據庫
    	try
    	{
        	sld.execSQL(sql);//建表
    	}
		catch(Exception e)
		{
            e.printStackTrace();
		}
    	closeDatabase();//關閉數據庫
    }
  //插入記錄的方法
    public static void insert(String sql)
    {
    	createOrOpenDatabase();//打開數據庫
    	try
    	{
        	sld.execSQL(sql);
    	}
		catch(Exception e)
		{
            e.printStackTrace();
		}
		closeDatabase();//關閉數據庫
    }
    //刪除記錄的方法
    public static  void delete(String sql)
    {
    	createOrOpenDatabase();//打開數據庫
    	try
    	{
        	sld.execSQL(sql);
      	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		closeDatabase();//關閉數據庫
    }
    //修改記錄的方法
    public static void update(String sql)
    {   
    	createOrOpenDatabase();//打開數據庫
    	try
    	{
        	sld.execSQL(sql);    	
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		closeDatabase();//關閉數據庫
    }
    //查詢的方法
    public static Vector<Vector<String>> query(String sql)
    {
    	createOrOpenDatabase();//打開數據庫
    	Vector<Vector<String>> vector=new Vector<Vector<String>>();//新建存放查詢結果的向量
    	try
    	{
           Cursor cur=sld.rawQuery(sql, new String[]{});
        	while(cur.moveToNext())
        	{
        		Vector<String> v=new Vector<String>();
        		int col=cur.getColumnCount();		//返回每一行都多少字段
        		for( int i=0;i<col;i++)
				{
					v.add(cur.getString(i));					
				}				
				vector.add(v);
        	}
        	cur.close();		
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		closeDatabase();//關閉數據庫
		return vector;
    }  
}

