package com.bn.reader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLDBUtil 
{
	static SQLiteDatabase sld;
	public static void createOrOpenDatabase()//創建或打開數據庫
	{
		try
    	{
	    	sld=SQLiteDatabase.openDatabase
	    	(
	    			"/data/data/com.bn.reader/recordself", //數據庫所在路徑
	    			null, 							//游標工廠
	    			SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY //讀寫、若不存在則創建
	    	);	
	    	
	    	String sql1="create table if not exists BookRecord"+
					"("+
					"rid INTEGER PRIMARY KEY AUTOINCREMENT,"+
					"path varchar(50),"+
					"data blob"+
					");";
	    	String sql2="create table if not exists BookMark"+
					"("+
					"mid INTEGER PRIMARY KEY AUTOINCREMENT,"+
					"ridfk INTEGER,"+
					"bmname varchar(50),"+
					"page INTEGER"+
					");";
	    	String sql3="create table if not exists LastTimePage"+
	    			"("+
	    			"lid INTEGER PRIMARY KEY AUTOINCREMENT,"+
	    			"path varchar(50),"+
	    			"page INTEGER,"+
	    			"fontsize INTEGER"+
	    			");";
	    	sld.execSQL(sql1);//創建表
	    	sld.execSQL(sql2);
	    	sld.execSQL(sql3);
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

	//返回指定書所有的書籤
	public static List<BookMark> getBookmarkList(String path)
	{
		List<BookMark> al=new ArrayList<BookMark>();
		Cursor cur=null;
		try
		{
			createOrOpenDatabase();//打開數據庫			
			String sql="select page,bmname from BookRecord,BookMark"
						+" where path='"+path+"' and rid=ridfk order by page";
			cur=sld.rawQuery(sql, new String[]{});
			while(cur.moveToNext())
		    {		
				int page=cur.getInt(0);
				String bmname=cur.getString(1);
		    	al.add(new BookMark(bmname,page));//將頁數和書籤名字存入書籤類中
		    }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cur.close();
			closeDatabase(); //關閉數據庫
		}
		return al;
	}
	//返回指定書的最後一個書籤的頁數
	public static int getLastBookmarkPage(String path)
	{
		int page=0;
		Cursor cur=null;
		try
		{
			createOrOpenDatabase();//打開數據庫			
			String sql="select page from BookRecord,BookMark"
						+" where path='"+path+"' and rid=ridfk order by page";
			cur=sld.rawQuery(sql, new String[]{});
			
			cur.moveToLast();
			page=cur.getInt(0);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cur.close();
			closeDatabase(); //關閉數據庫
		}
		return page;
	}
	
	
	
	
	
	//返回每一本書所讀到的書的頁數
	public static int getLastTimePage(String path)
	{
		int page=0;
		Cursor cur=null;
		try
		{
			createOrOpenDatabase();//打開數據庫			
			String sql="select page from LastTimePage"
						+" where path='"+path+"'";
			cur=sld.rawQuery(sql, new String[]{});
			cur.moveToNext();	
			page=cur.getInt(0);

			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cur.close();
			closeDatabase(); //關閉數據庫
		}
		return page;
	}
	
	//返回每一本書所讀到的書的字體大小
	public static int getLastTimeFontSize(String path)
	{
		int fontsize=0;
		Cursor cur=null;
		try
		{
			createOrOpenDatabase();//打開數據庫			
			String sql="select fontsize from LastTimePage"
						+" where path='"+path+"'";
			cur=sld.rawQuery(sql, new String[]{});
			cur.moveToNext();	
			fontsize=cur.getInt(0);

			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cur.close();
			closeDatabase(); //關閉數據庫
		}
		return fontsize;
	}
	
	
	
	
	//向lastTime表中插入數據
	public static void lastTimeInsert(String path,int page,int fontsize)
	{
		Cursor cur=null;
		try
		{
			createOrOpenDatabase();//打開數據庫
			//先從數據庫中查詢指定的名稱是否存在
			String sql="select path from LastTimePage where path='"+path+"'";//查找是否存在指定路徑的數據
			cur=sld.rawQuery(sql, new String[]{});
			if(cur.moveToNext())
			{//若已經存在則更新
				sql="update LastTimePage set page=?,fontsize=? where path='"+path+"'";
				sld.execSQL(sql,new Object[]{page,fontsize});
			}
			else
			{//若不存在則插入
	            sql="insert into LastTimePage(path,page,fontsize)values(?,?,?)";    		
	    		sld.execSQL(sql,new Object[]{path,page,fontsize});   
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			cur.close();
        	closeDatabase(); //關閉數據庫
		}
	}
	
	//向BookRecord表中插入數據
	public static void recordInsert(String path,byte[] rdata)
	{
		Cursor cur=null;
		try
		{
			createOrOpenDatabase();//打開數據庫
			//先從數據庫中查詢指定的名稱是否存在
			String sql="select path from BookRecord where path='"+path+"'";//查找是否存在指定路徑的數據
			cur=sld.rawQuery(sql, new String[]{});
			if(cur.moveToNext())
			{//若已經存在則更新
				sql="update BookRecord set data=? where path='"+path+"'";
				sld.execSQL(sql,new Object[]{rdata});
			}
			else
			{//若不存在則插入
				//插入計算計劃ID,計算計劃名稱,計算計劃總量,計算計劃人數，計算計劃blod
	            sql="insert into BookRecord(path,data)values(?,?)";    		
	    		sld.execSQL(sql,new Object[]{path,rdata});   
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cur.close();
        	closeDatabase(); //關閉數據庫
		}
	}
	
	//獲取指定名稱的記錄數據（得到的是HashMap的byte數據）
	public static byte[] selectRecordData(String path)
	{
		byte[] data=null;
		Cursor cur=null;

		try
		{
			createOrOpenDatabase();//打開數據庫			
			String sql="select path,data from BookRecord where path='"+path+"'";
			cur=sld.rawQuery(sql, new String[]{});
			if(cur.moveToNext())
		    {
				data=cur.getBlob(1);	
		    }		
		}
		catch(Exception e)
		{
			e.printStackTrace();  
		}
		finally
		{
			cur.close();
        	closeDatabase(); //關閉數據庫
		}
		
		return data;
	}
	
	//判斷是第一次打開這本書還是第N次打開本書
	public static boolean judgeIsWhichTime(String path)
	{
		Cursor cur=null;
		try
		{
			createOrOpenDatabase();//打開數據庫			
			String sql="select path from BookRecord where path='"+path+"'";
			cur=sld.rawQuery(sql, new String[]{});
			if(cur.moveToNext())//如果存在路徑，則是第N次打開這本書
		    {
				return false;
		    }
			else
			{
				return true;//否則是第1次打開這本書	
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();  
		}
		finally
		{
			cur.close();
        	closeDatabase(); //關閉數據庫
		}
		return false;	
	}
	
	//刪除BookMark表中對應「書名」的記錄
	public static void deleteOneBookMark(String name)
	{
		try
		{
			createOrOpenDatabase();//打開數據庫			
			String sql="delete from BookMark where bmname='"+name+"'";
			sld.execSQL(sql);
		}
		catch(Exception e)
		{
			e.printStackTrace();  
		}
		finally
		{
        	closeDatabase(); //關閉數據庫
		}
	}
	//刪除當前這本書的全部書籤
	//返回指定書所有的書籤
	public static void deleteAllBookMark(String path)
	{	
		Cursor cur=null;
		try
		{
			createOrOpenDatabase();//打開數據庫			
			String sql="select rid from BookRecord,BookMark"
						+" where path='"+path+"' and rid=ridfk";
			cur=sld.rawQuery(sql, new String[]{});
		    cur.moveToNext();
			int rid=cur.getInt(0);//得到書籤中ridfk的值
			
			String sql2="delete from BookMark where ridfk='"+rid+"'";
			sld.execSQL(sql2);//清空當前這本書的全部記錄
		    
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cur.close();
        	closeDatabase(); //關閉數據庫
		}
	}
	//判斷當前這本書是否存在書籤
	public static boolean judgeHaveBookMark(String path)
	{	
		Cursor cur=null;
		try
		{
			createOrOpenDatabase();//打開數?菘?			
			String sql="select page from BookRecord,BookMark"
						+" where path='"+path+"' and rid=ridfk";
			cur=sld.rawQuery(sql, new String[]{});
			if(cur.moveToNext())//如果書中存在書籤，返回true
		    {
				return true;
		    }
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cur.close();
			closeDatabase(); //關閉數據庫
		}
		return false;		
	}
	//向BookMark表中插入數據
	public static void bookMarkInsert(String name,int page)
	{
		Cursor cur=null;
		try
		{
			createOrOpenDatabase();//打開數據庫
			//查詢當前path對應的rid
			String sql="select rid from BookRecord where path='"+Constant.FILE_PATH+"'";
			cur=sld.rawQuery(sql, new String[]{});
			cur.moveToNext();
			int rid=cur.getInt(0);
			
			//先從數據庫中查詢指定的名稱是否存在
			sql="select bmname from BookMark where bmname='"+name+"'";
			cur=sld.rawQuery(sql, new String[]{});
			if(cur.moveToNext())
			{//若已經存在則更新
				sql="update BookMark set page=?,ridfk=? where bmname='"+name+"'";
				sld.execSQL(sql,new Object[]{page,rid});
			}
			else
			{//若不存在則插入
	            sql="insert into BookMark(bmname,page,ridfk)values(?,?,?)";    		
	    		sld.execSQL(sql,new Object[]{name,page,rid});  		
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			cur.close();
        	closeDatabase(); //關閉數據庫
		}
	}
	//從hashMap轉化為byte
	public static byte[] fromListRowNodeListToBytes(HashMap<Integer,ReadRecord> map)
	{
		byte[] result=null;		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try 
		{
			ObjectOutputStream oout=new ObjectOutputStream(baos);
			oout.writeObject(map);
			result=baos.toByteArray();
			oout.close();
			baos.close();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		return result;
	}
	
	//從byte轉化為hashMap
	@SuppressWarnings("unchecked")
	public static HashMap<Integer,ReadRecord> fromBytesToListRowNodeList(byte[] data)
	{
		HashMap<Integer,ReadRecord> result=null;
		try
		{			
			ByteArrayInputStream bais=new ByteArrayInputStream(data);			
			ObjectInputStream oin=new ObjectInputStream(bais);			
			result=(HashMap<Integer,ReadRecord>)oin.readObject();
			oin.close();
			bais.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
}

