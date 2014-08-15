package wyf.wyy;
import javax.naming.*;
import java.sql.*;
import javax.sql.*;
import java.util.*;
public class DB
{
	private static Connection con=null;//聲明Connection引用
	private static Statement stat=null;//聲明Statement引用
	private static ResultSet rs=null;//聲明ResultSet引用
//*****************************數據庫連接和關閉操作*************************
	public static Connection getCon(){//得到數據庫連接的方法	
		try{			
			 Context initial = new InitialContext();//得到上下文引用
			 DataSource ds = //得到DataSource引用
				    (DataSource)initial.lookup("java:comp/env/jdbc/jiudian");
			 con = ds.getConnection();//得到數據庫連接
		}
		catch(Exception e)
		{e.printStackTrace();}
		return con;//返回數據庫連接
		
		/*try{
		Class.forName("com.mysql.jdbc.Driver");
		String url="jdbc:mysql://localhost/test";
		con = DriverManager.getConnection(url,"root","123");
		}catch(ClassNotFoundException e){
			e.printStackTrace();
			}catch(SQLException e1){
				e1.printStackTrace();
				}
	
		return con;*/
	}
	public static void closeCon(){//關閉數據庫連接方法
		try	{
			  if(rs!=null){rs.close();}
			  if(stat!=null){stat.close();}
			  if(con!=null){con.close();}
		}
		catch(Exception e)
		{e.printStackTrace();}
	}
//*******************對分組的操作******************************************
	public static Vector<String[]> getGroup(){
		Vector<String[]> v =new Vector<String[]>();//創建返回向量對像
		try{
			 con = DB.getCon();//得到數據庫連接
			 stat = con.createStatement();//創建語句對像
			 String sql = "select gName,gImg,gDetail,gId,gOrderDet from rgroup";
			 rs = stat.executeQuery(sql);
			 while(rs.next()){//遍歷結果集得到分組信息		    
			    String group[] = new String[5];
			    for(int i=0;i<group.length;i++){
			      group[i] = //將信息添加到數組
			    	new String(rs.getString(i+1).getBytes("iso8859-1"),"big5");
			    }			
				v.add(group);//將信息數組添加到返回的向量裡
			}
		}
		catch(Exception e)
		{e.printStackTrace();}
		finally
		{DB.closeCon();}	
		return v;
	}
	public static Vector<String> getGroupInfo(int gId){
		Vector<String> v =new Vector<String>();//創建返回信息向量	
		try{
			 con = DB.getCon();//得到數據庫連接
			 stat = con.createStatement();//創建語句對像
			 String sql = "select gId,gName,gOrderDet,gImg,gDetail from"+
			  				" rgroup where gId="+gId;
			 rs = stat.executeQuery(sql);//執行SQL查詢
			 if(rs.next()){//將結果集信息添加到返回向量中			  
				v.add(new String(rs.getString(1).getBytes("iso8859-1"),"big5"));
				v.add(new String(rs.getString(2).getBytes("iso8859-1"),"big5"));
				v.add(new String(rs.getString(3).getBytes("iso8859-1"),"big5"));
				v.add(new String(rs.getString(4).getBytes("iso8859-1"),"big5"));
				v.add(new String(rs.getString(5).getBytes("iso8859-1"),"big5"));								
			 }
		}
		catch(Exception e){e.printStackTrace();}
		finally	{DB.closeCon();}//關閉數據庫連接
		return v;//返回分組信息
	}
	public static String getOrderDet(int gId)
	{
		String msg="";
		try
		{
			con=DB.getCon();
			stat=con.createStatement();
			String sql="select gOrderDet from rgroup where gId="+gId;
			rs=stat.executeQuery(sql);
			if(rs.next())
			{
				msg=new String(rs.getString(1).getBytes("iso8859-1"),"big5");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DB.closeCon();
		}
		return msg;
	}
	
//******************分頁操作**************************************************
public static int getTotal(int span,int group){
	int result=0;//初始化返回頁數
	String sql = "";//聲明sql引用
	try{
		con = DB.getCon();
		stat = con.createStatement();
		//得到相關記錄的總條數
		if(group==0)//0代表所有分組
		{sql = "select count(*) from resource";}
		else{
			sql = "select count(*) from resource "+"where rgroup='"+group+"'";
		}
		rs = stat.executeQuery(sql);//執行sql語句			
	    rs.next();
	    int rows=rs.getInt(1);//得到記錄條數
	    result=rows/span+((rows%span==0)?0:1);//計算出總頁數
	}
	catch(Exception e){e.printStackTrace();}
	finally{DB.closeCon();}//關閉數據庫連接		
	return result;//返回結果
}
public static Vector<String[]> getResource(String rlevel)
	{
		Vector<String[]> v=new Vector<String[]>();
		
		String sql;
		try
		{
			con=DB.getCon();
			stat=con.createStatement();
			String rlevell = new String(rlevel.getBytes("big5"),"iso8859-1");
			sql="select rgid,rlevel,rmoney,rstatus from resource where rlevel='"+rlevell+"'";
			rs=stat.executeQuery(sql);
            while(rs.next()){//遍歷結果集得到分組信息		    
			    String group[] = new String[4];
			    for(int i=0;i<group.length;i++){
			      group[i] = //將信息添加到數組
			    	new String(rs.getString(i+1).getBytes("iso8859-1"),"big5");
			    }			
				v.add(group);//將信息數組添加到返回的向量裡
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DB.closeCon();
		}
		return  v;
	}
	public static Vector<String[]> getResource1()
	{
		Vector<String[]> v=new Vector<String[]>();
		try
		{
			con=DB.getCon();
			stat=con.createStatement();
			String sql="select rgroup,rgid,rlevel,rmoney,rstatus from resource";
			rs=stat.executeQuery(sql);
            while(rs.next()){//遍歷結果集得到分組信息		    
			    String group[] = new String[4];
			    for(int i=0;i<group.length;i++){
			      group[i] = //將信息添加到數組
			    	new String(rs.getString(i+2).getBytes("iso8859-1"),"big5");
			    }			
				v.add(group);//將信息數組添加到返回的向量裡
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DB.closeCon();
		}
		return  v;
	}
	
public static Vector<String[]> getPageContent(int page,int span,int group){
	Vector<String[]> v = new Vector<String[]>();//聲明返回向量集合
	String sql = "";//聲明sql語句引用
	int startRow = 	(page-1)*span;//計算出起始記錄行數
	try{
		con = DB.getCon();
		stat = con.createStatement();
		if(group==0){//group參數為零，則是對所有的分組進行分頁顯示
			sql = "select rgid,rlevel,rmoney,rdetail,rstatus,rid,gName from "+
			       "resource,rgroup where resource.rgroup=rgroup.gId order "+
			        "by rgroup, rgid, rid";
		}
		else{//對具體分組進行分頁顯示
			sql = "select rgid,rlevel,rmoney,rdetail,rstatus,rid,gName "+
			 	   "from resource,rgroup where resource.rgroup=rgroup.gId "+
			 	   "and rgroup='"+group+"' order by rgid";
		}
		rs = stat.executeQuery(sql);//執行sql語句，拿到結果集
		if(startRow!=0)//如果其實行不是第零行
		{rs.absolute(startRow);}//結果集滾動到起始行
		int c=0;//控制讀取的記錄條數
		while(c<span&&rs.next()){//從其實行讀每頁顯示的記錄條數
			String s[] = new String[7];
			for(int i=0;i<s.length;i++){
		      s[i] = //遍歷結果集將信息添加到數組
		    	new String(rs.getString(i+1).getBytes("iso8859-1"),"big5");
		    }							
			v.add(s);//將數組添加到返回向量
			c++;
		}
	}
	catch(Exception e){e.printStackTrace();}
	finally{DB.closeCon();}//關閉數據庫連接	
	return v;//返回結果
}
//*******************得到某張表某一列的最大值並加1***************************
	public static int getId(String table,String row){//得到一個表主鍵ID+1值
		int id = 0;
		try	{
			con = DB.getCon();//得到數據庫連接
			stat = con.createStatement();//創建語句對像
			rs = stat.executeQuery("select count(*) from "+table);
			rs.next();
			if(rs.getInt(1)==0)	{ id = 1; }//如果表中沒有記錄，則將id置為1
			else{
				rs = stat.executeQuery("select max("+row+") from "+table);
				rs.next();
				id = Integer.parseInt(rs.getString(1))+1;//將其值加一
			}						
		}
		catch(Exception e){e.printStackTrace();}
		finally	{DB.closeCon();}//關閉數據庫連接
		return id;//返回結果
	}
//********************某條記錄是否存在**************************************
	public static boolean isExist(String sqla){//查看此條記錄是否存在
		boolean flag = false;			
		try{			
			String sql = new String(sqla.getBytes("big5"),"iso8859-1");//轉碼	
			con = DB.getCon();//得到數據庫連接
			stat = con.createStatement();//創建語句對像
			rs = stat.executeQuery(sql);//執行查詢
			if(rs.next()) {flag = true;}
		}
		catch(Exception e)	{e.printStackTrace();}
		finally	{DB.closeCon();}//關閉數據庫連接		
		return flag;//返回結果
	}
//*********************更新數據庫*****************************************
	public static int update(String sqla){
		int changedCount=0;
		try{
			String sql = new String(sqla.getBytes(),"iso8859-1");//轉碼
			con = DB.getCon();//得到數據庫連接
			stat = con.createStatement();//創建語句對像
			changedCount = stat.executeUpdate(sql);//進行更新
		}
		catch(Exception e)	{e.printStackTrace();}
		finally {DB.closeCon();}//關閉數據庫連接 		
		return changedCount;//返回跟新記錄條數
	}
	public static boolean update(String sqla,String sqlb){//事務處理
		boolean b = false;//記錄是否更新成功
		try{
			con = DB.getCon();//得到數據庫連接
			con.setAutoCommit(false);//禁止自動提交，開始一個事務
			stat = con.createStatement();
			String sql = new String(sqla.getBytes(),"iso8859-1");//轉碼
			stat.executeUpdate(sql);//執行更新
			sql = new String(sqlb.getBytes(),"iso8859-1");//轉碼
			stat.executeUpdate(sql);//執行更新			
			con.commit();//將事務提交			
			con.setAutoCommit(true);//恢復自動提交模式
			b = true;//設置更新成功
		}
		catch(Exception e){
			e.printStackTrace();
			try{
				con.rollback();//出現問題，事務回滾
				b = false;
			}
			catch(Exception ea){ea.printStackTrace();}
		}
		finally{DB.closeCon();}//關閉數據庫連接
		return b;//返回更新成功或者失敗標誌
	}
		public static boolean updatea(String sqla)//我添加的
	{
		boolean b=false;
		try{
			String sql = new String(sqla.getBytes(),"iso8859-1");//轉碼
			con = DB.getCon();//得到數據庫連接
			con.setAutoCommit(false);//禁止自動提交，開始一個事務
			stat = con.createStatement();//創建語句對像
			stat.executeUpdate(sql);//進行更新
			con.commit();//將事務提交			
			con.setAutoCommit(true);//恢復自動提交模式
			b = true;//設置更新成功
		}
		catch(Exception e){
			e.printStackTrace();
			try{
				con.rollback();//出現問題，事務回滾
				b = false;
			}
			catch(Exception ea){ea.printStackTrace();}
		}
		finally{DB.closeCon();}//關閉數據庫連接
		return b;//返回更新成功或者失敗標誌
	}
//********************根據一條SQL得到數據庫中信息****************************
	public static String getInfo(String sqla){
		String Info=null;
		try{			
			String sql = new String(sqla.getBytes(),"iso8859-1");//SQL轉碼
			con = DB.getCon();//得到數據庫連接
			stat = con.createStatement();//創建語句對像
			rs = stat.executeQuery(sql);//執行查詢
			if(rs.next())
			{Info=new String(rs.getString(1).getBytes("iso8859-1"),"big5");}
		}
		catch(Exception e)	{e.printStackTrace();}
		finally {DB.closeCon();}		
		return Info;
	}
	public static String getDetail(String rgid)
	{
		String s=null;
		try
		{
			String sql="select rdetail from resource where rgid="+rgid;
			con=DB.getCon();
			stat=con.createStatement();
			rs=stat.executeQuery(sql);
			if(rs.next())
			{
				s=new String(rs.getString(1).getBytes("iso8859-1"),"big5");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			DB.closeCon();
		}
		return s;
	}
	
//***********************得到用戶的詳細信息********************************
	public static Vector<String> getUserInfo(String uname1){
		Vector<String> userInfo=new Vector<String>();
		try{
			String uname = new String(uname1.getBytes("big5"),"iso8859-1");//轉碼
			con = DB.getCon();//得到數據庫連接
			stat = con.createStatement();//創建語句對像
			rs = stat.executeQuery("select pwd,telNum,realName,gender,"+
									"email from user where uname='"+uname+"'");
			if(rs.next()){//將用戶信息添加到向量中
			    userInfo.add(new String(rs.getString(1).getBytes("iso8859-1"),"big5"));
				userInfo.add(new String(rs.getString(2).getBytes("iso8859-1"),"big5"));
				userInfo.add(new String(rs.getString(3).getBytes("iso8859-1"),"big5"));
				userInfo.add(new String(rs.getString(4).getBytes("iso8859-1"),"big5"));
				userInfo.add(new String(rs.getString(5).getBytes("iso8859-1"),"big5"));
			}
		}
		catch(Exception e) {e.printStackTrace();}
		finally	{DB.closeCon();}//關閉數據庫連接		
		return userInfo;//返回用戶信息
	}
//******************得到資源的詳細信息***************************
	public static Vector<String[]> getResInfo(String sqla){
		Vector<String []> v = new Vector<String[]>();
		try{
			con = DB.getCon();//得到數據庫連接
			stat = con.createStatement();//創建語句對像			
			String sql = new String(sqla.getBytes(),"iso8859-1");//轉碼
			rs = stat.executeQuery(sql);//執行查詢
			while(rs.next()){
				String s[] = new String[8];
				for(int i=0;i<s.length-1;i++){//將資源信息添加到數組
					s[i] = new String(rs.getString(i+1).getBytes("iso8859-1"),"big5");
				}				
				v.add(s);//將數組添加到返回向量中
			}
			for(String s[]:v){//根據分組ID得到分組名				
				String sqlb = "select gName from rgroup where gId='"+s[5]+"'";
				rs = stat.executeQuery(sqlb);
				rs.next();//結果集游標向後移一位
				s[7] = new String(rs.getString(1).getBytes("iso8859-1"),"big5");
			}
		}
		catch(Exception e){e.printStackTrace();}
		finally{DB.closeCon();}//關閉數據庫連接
		return v;//返回查詢結果
	}
	
//*****************得到管理員詳細信息********************************
	public static Vector<String[]> getAdminInfo(){
		Vector<String[]> v = new Vector<String[]>();
		try{
			con = DB.getCon();//得到數據庫連接
			stat = con.createStatement();//創建語句對像
			rs = stat.executeQuery("select adname,adlevel from adinfo");//執行查詢
			while(rs.next()){
				String s[] = new String[2];
				s[0] = new String(rs.getString(1).getBytes("iso8859-1"),"big5");
				s[1] = new String(rs.getString(2).getBytes("iso8859-1"),"big5");				
				v.add(s);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{DB.closeCon();}		
		return v;
	}
		public static boolean isDelete(String rid1){
		boolean b = false;
		int count=0;
		try{
			String orid=new String(rid1.getBytes("iso8859-1"),"big5");
			System.out.println(orid);
			String sqla="delete from olist where oid="+orid;
			String sqlb="delete from oinfo where orid="+orid;
		    count+=stat.executeUpdate(sqla);
		    count+=stat.executeUpdate(sqlb);
		    if(count==2){b=true;}
		}
		catch(Exception e){e.printStackTrace();}
		finally{DB.closeCon();}
		return b;
	}
}
