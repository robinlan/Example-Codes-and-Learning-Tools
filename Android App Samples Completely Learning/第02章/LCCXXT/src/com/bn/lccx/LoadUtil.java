package com.bn.lccx;


import java.util.Vector;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LoadUtil {
	
	public static SQLiteDatabase createOrOpenDatabase()//連接數據庫
	{		
		SQLiteDatabase sld=null;
		try{
			sld=SQLiteDatabase.openDatabase//連接並創建數據庫，如果不存在則創建
			(
					"/data/data/com.bn.lccx/mydb", 
					null, 
					SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY);

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return sld;//返回該連接
	}
	
	
	
	public static void createTable(String sql){//創建表
		SQLiteDatabase sld=createOrOpenDatabase();//連接數據庫
		try{
			sld.execSQL(sql);//執行SQL語句
			sld.close();//關閉連接
		}catch(Exception e){
			
			
		}		
	}
	
	public static boolean insert(String sql)//插入數據
	{
		SQLiteDatabase sld=createOrOpenDatabase();//連接數據庫
		try{
			sld.execSQL(sql);
			
			sld.close();
			return true;
		}catch(Exception e){
			return false;			
		}
		
	}
	
	public  static Vector<Vector<String>>  query(String sql)//查詢
	{
		Vector<Vector<String>> vector=new Vector<Vector<String>>();//新建存放查詢結果的向量
		SQLiteDatabase sld=createOrOpenDatabase();//得到連接數據庫的連接
		
		
		try{			
			Cursor cur=sld.rawQuery(sql, new String[]{});//得到結果集
			
			while(cur.moveToNext())//如果存在下一條
			{
				Vector<String> v=new Vector<String>();
				int col=cur.getColumnCount();		//將其放入向量		
				for( int i=0;i<col;i++)
				{
					v.add(cur.getString(i));					
				}				
				vector.add(v);         		
			}
			cur.close();//關閉結果集
			sld.close();//關閉連接
		}catch(Exception e)
		{
			e.printStackTrace();		
		}
		return vector;
	}
	
		
	
	
	//查找某車的經過的所有車站
	public static Vector<Vector<String>> getInfo(String tname)
	{
		//查找某列車經過的車站
		String sql = "select Sname,Rarrivetime,Rstarttime "+
							"from station,"+
							"(select Sid,Rid,Rarrivetime,Rstarttime "+
							"from relation where Tid="+
							"(select Tid from train "+
							"where Tname='"+tname+"')) a "+
							"where a.Sid=station.Sid order by Rid";	
		//得到符合要求的站
		Vector<Vector<String>> vtemp = query(sql);	
		
		return vtemp;
		
	}
	//站站查詢
	public static Vector<Vector<String>> getSameVector(String start,String end)
	{
		//查找車名,始發站,終點站和車類型
		String sql = "select Tname,Tstartstation,Tterminus,Ttype "+
						"from train where Tid in "+
						"(select Tid from relation where Sid in "+
						"(select Sid from station where Sname='"+start+"') "+
						"and Tid in "+
						"(select Tid from relation where Sid in "+
						"(select Sid from station where Sname='"+end+"')))";
		//得到有關火車信息的Vector
		Vector<Vector<String>> temp = query(sql);
		//查找出發站和火車開車的時間
		String sql1 = "select Sname,Rstarttime from station,relation"+
						" where Sname='"+start+"' and "+
						"station.Sid=relation.Sid and "+
						"relation.Tid in "+
						"(select Tid from relation where Sid in"+
						"(select Sid from station where Sname='"+start+"') "+
						"and Tid in"+
						"(select Tid from relation where Sid in "+
						"(select Sid from station where Sname='"+end+"')))";
		//查找終點站和火車到站時間
		String sql2 = "select Sname,Rarrivetime from station,relation"+
						" where Sname='"+end+"' and "+
						"station.Sid=relation.Sid and "+
						"relation.Tid in "+
						"(select Tid from relation where Sid in"+
						"(select Sid from station where Sname='"+start+"') "+
						"and Tid in"+
						"(select Tid from relation where Sid in "+
						"(select Sid from station where Sname='"+end+"')))";
		//得到有關火車站的信息
		Vector<Vector<String>> temp1 = query(sql1);		
		Vector<Vector<String>> temp2 = query(sql2);			
		//將查詢結果組合到一起	
		temp = combine(temp,temp1,temp2);		
		return temp;		
	}
	
	//組合向量
	public static  Vector<Vector<String>> combine(Vector<Vector<String>> temp,Vector<Vector<String>> temp1,Vector<Vector<String>> temp2)
	{//將這三個Vector組合成一個	
	for(int i=0;i<temp.size();i++)
	{
		Vector<String> v1 = temp.get(i);
		if(i<temp1.size())
		{
			Vector<String> v2 = temp1.get(i);
			//將V2里面的元素加到V1里面
			for(int j=0;j<v2.size();j++)
			{
				v1.add(v2.get(j));
			}				
		}
		else{
		//沒有關係時添加空
			v1.add("");
			v1.add("");
		}	
	}
		for(int i=0;i<temp.size();i++)
		{
			Vector<String> v1 = temp.get(i);
			if(i<temp2.size())
			{
				Vector<String> v2 = temp2.get(i);
				//將V2里面的元素加到V1里面
				for(int j=0;j<v2.size();j++)
				{
					v1.add(v2.get(j));
				}				
			}
			else
			{
				//沒有關係時添加空
				v1.add("");
			}
		
		}
	return temp;
	}

	//某車次的情況，初始站和末尾站還有車型等時間
	public static Vector<Vector<String>> trainSearch(String tname)
	{//車次查詢		
		
		
		String sql =//查找車名,始發站,終點站和車類型
			"select Tname,Tstartstation,Tterminus,Ttype "+
									"from train where Tname='"+tname+"'";
								
		String sql1 = //查找出發站和火車開車的時間	
			"select Tstartstation,Rstarttime from train,relation "+
								"where train.Tid=relation.Tid and "+
								"Tname='"+tname+"' and relation.Sid="+
								"(select Sid from station "+
								"where Sname=train.Tstartstation)";

		
		
		String sql2 = //查找終點站和火車到站時間Rarrivetime
			"select Tterminus,Rarrivetime from train,relation "+
								"where train.Tid=relation.Tid and "+
								"Tname='"+tname+"' and relation.Sid="+
								"(select Sid from station "+
								"where Sname=train.Tterminus)";
		
		Vector<Vector<String>> temp = query(sql);//得到車名,始發站,終點站和車類型的Vector
		
		Vector<Vector<String>> temp1 = query(sql1);//得到出發站和火車開車時間的vector
				
		Vector<Vector<String>> temp2 = query(sql2);//得到終點站和火車到站時間的vector
		temp = combine(temp,temp1,temp2);
		
		return temp;
	}
	
	public static Vector<Vector<String>> stationSearch(String station)////根據車站名字查詢經過車站的所有車
	{//車站查詢	，得到經過每一輛車的到站時間和出戰時間
		
		
		//查詢有關火車的信息
		String sql = "select Tname,Tstartstation,Tterminus,Ttype "+
								"from train where Tid in "+
								"(select Tid from relation where Sid in "+
								"(select Sid from station where "+
								"Sname='"+station+"'))";
//		//查詢出發站及出發時間
		String sql1 = "select '"+station+"',Rstarttime from relation "
								+"where Sid = "+	
								"(select Sid from station where "+
								"Sname='"+station+"')";
		

		
		String sql2 = "select '"+station+"',Rarrivetime from relation "
		+"where Sid = "+	
		"(select Sid from station where "+
		"Sname='"+station+"')";
		
		//得到有關信息的向量
		Vector<Vector<String>> temp = query(sql);
		
		//得到出發站和火車開車時間的vector
		Vector<Vector<String>> temp1 = query(sql1);
		
		//得到終點站和火車到站時間的vector
		Vector<Vector<String>> temp2 = query(sql2);
		
		
		//將三個Vector組合在一起		
		temp = combine(temp,temp1,temp2);
		return temp;
	}
	public  static  Vector<Vector<String>>  Zjzquery(String start,String zjz,String end)//查詢，中轉站查詢的
	{
		Vector<Vector<String>> vector=getSameVector(start,zjz);//分兩步，先查出起點站到中轉站，然後再查出中轉站到終點站車次即可
		Vector<Vector<String>> vector2=getSameVector(zjz,end);
		if(vector.size()==0||vector2.size()==0)//如果某一個無結果，則說明無該中轉站的車次
		{
			
			vector.clear();
			vector2.clear();//清空向量中數據
		}
		
		for(int i=0;i<vector2.size();i++)
		{
			vector.add(vector2.get(i));
		}//否則將第二個向量中的數據添加到第一個向量中
		
		return vector;//返回
	}

	//查詢其插入的表項ID的最大值
	public static int getInsertId(String name,String tid)
	{
		int id = 0;
		String sql = "select Max("+tid+") from "+name;		
			
			SQLiteDatabase sld=createOrOpenDatabase();
			
			
			try{			
				Cursor cur=sld.rawQuery(sql, new String[]{});
			
			//查看結果集			
			if(cur.moveToNext())
			{
				id = cur.getInt(0);
			}
			//關閉結果集,語句及連接
			cur.close();
			sld.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		id++;
		return id;
	}

}

