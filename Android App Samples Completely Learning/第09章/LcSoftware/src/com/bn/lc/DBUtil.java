package com.bn.lc;
import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
public class DBUtil 
{
	static SQLiteDatabase sld;
	static Lc_Activity activity;
			
	//創建或打開數據庫的方法
    public static void createOrOpenDatabase()
    {
    	try
    	{
	    	sld=SQLiteDatabase.openDatabase
	    	(
	    			"/data/data/com.bn.lc/mydb", //數據庫所在路徑
	    			null, 								//CursorFactory
	    			SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY //讀寫、若不存在則創建
	    	);
	    	
	    	String sql01="create table if not exists Income" +
	    				 "(" +
		    				 "id INTEGER PRIMARY KEY AUTOINCREMENT," +
			    			 "idate char(10)," +
			    			 "isource Varchar(20)," +
			    			 "imoney Integer," +
			    			 "imemo Varchar(50)" +
		    			 ")";
	    	String sql02="create table if not exists Scy" +     //收入類別
		    			 "("+
			    			 "icategory Varchar(10) PRIMARY KEY ," +
			    			 "says varchar(50)" +
		    			 ")";
	    	String sql03="create table if not exists Spend" +
		    			 "(" +
			    			 "id INTEGER PRIMARY KEY AUTOINCREMENT," +
							 "idate char(10)," +
							 "isource Varchar(20)," +
							 "imoney Integer," +
							 "imemo Varchar(50)" +
						 ")";
	    	String sql04="create table if not exists Zcy" +        //支出類別
		    			 "(" +
			    			 "icategory Varchar(10) PRIMARY KEY," +
			    			 "says Varchar(50)" +
		    			 ")";
	    	String sql05="create table if not exists PersonDate" +
		    			 "(" +
			    			 "id INTEGER PRIMARY KEY AUTOINCREMENT," +
			    			 "isex char(1)," +
			    			 "idate varchar(10)," +
			    			 "iage varchar(2)," +
			    			 "iblood varchar(5)," +
			    			 "iprovince varchar(5)," +
			    			 "icity varchar(5)," +
			    			 "iemail varchar(15)," +
			    			 "ioldpwd varchar(6)" +
		    			 ")";
	    	sld.execSQL(sql01);
	    	sld.execSQL(sql02);
	    	sld.execSQL(sql03);
	    	sld.execSQL(sql04);
	    	sld.execSQL(sql05);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    } 
    
    //類別維護插入方法
	public static void insertCategory(String str,String str0,String str1)  
    {
    	try
    	{
        	String sql="insert into "+str0+" values('"+str+"','"+str1+"');";
        	sld.execSQL(sql);
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    } 
    
	//類別維護查詢的方法
    public static  List<String> queryCategory(String str)
    {
    	List<String> addcategory=new ArrayList<String>();
    	try
    	{
        	String sql="select * from "+str+";";
        	Cursor cur=sld.rawQuery(sql, new String[]{});
        	while(cur.moveToNext())
        	{
        		addcategory.add(cur.getString(0));
        		addcategory.add(cur.getString(1));
        	}
        	cur.close();
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return addcategory;
    }
    
    //類別的刪除信息
    public static void deleteValuesFromTable(String tablename,String colname,String getstr)
    {
    	try
    	{
        	String sql="delete from "+tablename+" where "+colname+"='"+getstr+"';";
        	sld.execSQL(sql);
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
    
  //日常收入插入記錄的方法
    public static void insert(String tableName)
    {  	
    	int money=Integer.parseInt(Lc_Activity.Imoney01);
    	try
    	{
        	String sql="insert into '"+tableName+"' values(null,'"+Lc_Activity.Idate01+"'," +
        			   "'"+Lc_Activity.Isource+"','"+money+"','"+Lc_Activity.Imemo+"')";
        	
        	sld.execSQL(sql);
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
    
  //收入 支出查詢
    public static  List<String> queryIncome(String tableName, int state)
    {
    	List<String> IncomeSpeedSelect=new ArrayList<String>();
        try 
    	{ 
        	if(state==1)
        	{  //checkBox1 checkBox2 checkBox3都被選中的情況 
        		String sql1="select * from '"+tableName+"' where " +
        			"idate between '"+Lc_Activity.Idate01+"' AND '"+Lc_Activity.Idate02+"' " +
        				"and imoney between '"+Integer.parseInt(Lc_Activity.Imoney01)+"'  " +
        					"and '"+Integer.parseInt(Lc_Activity.Imoney02)+"' " +
        						"and isource='"+Lc_Activity.Isource+"';";
        		Cursor cur=sld.rawQuery(sql1, new String[]{});       
            	while(cur.moveToNext())                               
            	{
            		IncomeSpeedSelect.add(cur.getString(1));
            		IncomeSpeedSelect.add(cur.getString(2));
            		IncomeSpeedSelect.add(cur.getString(3));
            		IncomeSpeedSelect.add(cur.getString(4));
            	}
            	cur.close();
        	}
        	if(state==2)
        	{//checkBox1 checkBox2 都被選中的情況 
        		String sql1="select * from '"+tableName+"' where " +
        			"idate between '"+Lc_Activity.Idate01+"' AND '"+Lc_Activity.Idate02+"' " +
        				"and imoney between '"+Integer.parseInt(Lc_Activity.Imoney01)+"'  " +
        					"and '"+Integer.parseInt(Lc_Activity.Imoney02)+"' ;";
        		Cursor cur=sld.rawQuery(sql1, new String[]{});       
            	while(cur.moveToNext())                               
            	{
            		IncomeSpeedSelect.add(cur.getString(1));
            		IncomeSpeedSelect.add(cur.getString(2));
            		IncomeSpeedSelect.add(cur.getString(3));
            		IncomeSpeedSelect.add(cur.getString(4));
            	}
            	cur.close();
        	}
        	if(state==3)
        	{//checkBox1  checkBox3都被選中的情況 
        		String sql1="select * from '"+tableName+"' where " +
    			"idate between '"+Lc_Activity.Idate01+"' AND '"+Lc_Activity.Idate02+"' " +
    				"and isource='"+Lc_Activity.Isource+"';";
        		Cursor cur=sld.rawQuery(sql1, new String[]{});       
        		while(cur.moveToNext())                               
        		{
        			IncomeSpeedSelect.add(cur.getString(1));
        			IncomeSpeedSelect.add(cur.getString(2));
        			IncomeSpeedSelect.add(cur.getString(3));
        			IncomeSpeedSelect.add(cur.getString(4));
        		}
        		cur.close();
        	}
        	if(state==4)
        	{// checkBox2 checkBox3都被選中的情況 
        		String sql1="select * from '"+tableName+"' " +
        		   "where imoney between '"+Integer.parseInt(Lc_Activity.Imoney01)+"' " +
        		   	  "and '"+Integer.parseInt(Lc_Activity.Imoney02)+"' " +
        		   	  		"and isource='"+Lc_Activity.Isource+"';";
        		Cursor cur=sld.rawQuery(sql1, new String[]{});       
        		while(cur.moveToNext())                               
        		{
        			IncomeSpeedSelect.add(cur.getString(1));
        			IncomeSpeedSelect.add(cur.getString(2));
        			IncomeSpeedSelect.add(cur.getString(3));
        			IncomeSpeedSelect.add(cur.getString(4));
        		}
        	cur.close();
        	}
        	if(state==5)
        	{// checkBox1 都被選中的情況 
        		String sql1="select * from '"+tableName+"' where " +
        			"idate between '"+Lc_Activity.Idate01+"' AND '"+Lc_Activity.Idate02+"' ;";
        		Cursor cur=sld.rawQuery(sql1, new String[]{});       
            	while(cur.moveToNext())                               
            	{
            		IncomeSpeedSelect.add(cur.getString(1));
            		IncomeSpeedSelect.add(cur.getString(2));
            		IncomeSpeedSelect.add(cur.getString(3));
            		IncomeSpeedSelect.add(cur.getString(4));
            	}
            	cur.close();
        	}
        	if(state==6)
        	{// checkBox2 都被選中的情況 
        		String sql1="select * from '"+tableName+"' where " +
         	   "imoney between '"+Integer.parseInt(Lc_Activity.Imoney01)+"'" +
         	   	 " and '"+Integer.parseInt(Lc_Activity.Imoney02)+"' ;";
        		Cursor cur=sld.rawQuery(sql1, new String[]{});       
            	while(cur.moveToNext())                               
            	{
            		IncomeSpeedSelect.add(cur.getString(1));
            		IncomeSpeedSelect.add(cur.getString(2));
            		IncomeSpeedSelect.add(cur.getString(3));
            		IncomeSpeedSelect.add(cur.getString(4));
            	}
            	cur.close();
        		
        	}
        	if(state==7)
        	{// checkBox3 都被選中的情況 
        		String sql1="select * from '"+tableName+"' where isource='"+Lc_Activity.Isource+"' ;";
            	Cursor cur=sld.rawQuery(sql1, new String[]{});       
            	while(cur.moveToNext())                               
            	{
            		IncomeSpeedSelect.add(cur.getString(1));
            		IncomeSpeedSelect.add(cur.getString(2));
            		IncomeSpeedSelect.add(cur.getString(3));
            		IncomeSpeedSelect.add(cur.getString(4));
            	}
            	cur.close();	
        	}
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return IncomeSpeedSelect;
    }

    //收入支出查詢 只有表名
    public static List<String> queryTable(String tableName)
    {
    	List<String> slist=new ArrayList<String>();
    	try
    	{
    		String sql="select * from "+tableName+";";
    		Cursor cur=sld.rawQuery(sql, new String[]{});       
        	while(cur.moveToNext())                               
        	{
        		slist.add(cur.getString(1));
        		slist.add(cur.getString(2));
        		slist.add(cur.getString(3));
        		slist.add(cur.getString(4));
        	}
        	cur.close(); 
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return slist;
    }
    
    //收入或支出的類別的查詢
    public static List<String> getIsource(String tableName)
    {
    	List<String> alist=new ArrayList<String>();
    	try
    	{
    		String sql="select isource from "+tableName;
    		Cursor cur=sld.rawQuery(sql, new String[]{});
        	while(cur.moveToNext())                               
        	{
        		alist.add(cur.getString(0));
        	}
        	cur.close();
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return alist;
    }
    
    //獲得對應表的同類的總和
    public static List<String> getSum(String tableName,int state)  
    {
    	List<String> sumSelect=new ArrayList<String>();
    	if(state==1)
    	{
    		try
        	{
            	String sql="select sum(imoney) from "+tableName+" " +
            		"where idate between '"+Lc_Activity.Idate01+"' AND '"+Lc_Activity.Idate02+"' ;";
            	Cursor cur=sld.rawQuery(sql, new String[]{});
            	while(cur.moveToNext())
            	{
            	     sumSelect.add(cur.getString(0));
            	}
        	}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    		return sumSelect;
    	}
    	if(state==2)
    	{
    		try
    		{
    			String sql="select sum(imoney) from "+tableName+" where isource= '"+Lc_Activity.Isource+"';";
        		Cursor cur=sld.rawQuery(sql, new String[]{});
            	while(cur.moveToNext())
            	{
            	     sumSelect.add(cur.getString(0));
            	}
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    		return sumSelect;
    	}
    	if(state==3)
    	{
    		try
    		{
    			String sql="select sum(imoney) from '"+tableName+"' " +
    				"where (isource= '"+Lc_Activity.Isource+"') " +
    					"and idate between '"+Lc_Activity.Idate01+"' and '"+Lc_Activity.Idate02+"' ;";
        		Cursor cur=sld.rawQuery(sql, new String[]{});
            	while(cur.moveToNext())
            	{
            	     sumSelect.add(cur.getString(0));
            	}
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    		return sumSelect;
    	}
    	
		return sumSelect;
    }

    //查詢個人信息
    public static String getPassword()
    {
    	String result=null;
    	try
    	{
    		String sql="select Ioldpwd from PersonDate;";
    		Cursor cur=sld.rawQuery(sql, new String[]{});  
	         
        	while(cur.moveToNext())                               
        	{
        		result=cur.getString(0);
        		System.out.println(result);
        	}
        	cur.close();		
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return result;
    }
    
	//插入個人信息
	public static void InsertPersonDate()  
    {  	
    	try
    	{
    		String sql="insert into PersonDate values(1,'"+Lc_Activity.sexDate+"'," +
	    			   "'"+Lc_Activity.Idate01+"','"+Lc_Activity.Iage+"','"+Lc_Activity.bloodDate+"'," +
	    			   "'"+Lc_Activity.priovinceDate+"','"+Lc_Activity.cityDate+"','"+Lc_Activity.Iemail+"'," +
	    			   "'"+Lc_Activity.Ioldpwd+"');";
        	sld.execSQL(sql);
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
	
	//更改個人信息
	public static void UpdatePersonDate()  
    {  	
    	try
    	{
    		String sql="update PersonDate set isex='"+Lc_Activity.sexDate+"'," +
    		           "idate='"+Lc_Activity.Idate01+"',iage='"+Lc_Activity.Iage+"'," +
    			       "iblood='"+Lc_Activity.bloodDate+"'," +
    				   "iprovince='"+Lc_Activity.priovinceDate+"',icity='"+Lc_Activity.cityDate+"'," +
    			       "iemail='"+Lc_Activity.Iemail+"',ioldpwd='"+Lc_Activity.Inewpwd+"' where id=1;";
        	sld.execSQL(sql);
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }

	//查詢表中的數據
	public static List<String> getAllInformation(String time,String source,String money,String tableName)
	{
		List<String> slist=new ArrayList<String>();
		try
		{
			String sql="select * from "+tableName+" where idate='"+time+
			           "' and isource='"+source+"' and imoney='"+money+"';";
    		Cursor cur=sld.rawQuery(sql, new String[]{});
        	while(cur.moveToNext())                               
        	{
        		slist.add(cur.getString(0));
        		slist.add(cur.getString(1));
        		slist.add(cur.getString(2));
        		slist.add(cur.getString(3));
        		slist.add(cur.getString(4));
        	}
        	cur.close();		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return slist;
	}
	
	//刪除表中的某一條記錄   (收入 支出查詢詳細信息的刪除功能)
	public static void deleteFromTable(int id,String str)
	{
		try
		{
			String sql="delete from "+str+" where id="+id+";";
			sld.execSQL(sql);
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
}
