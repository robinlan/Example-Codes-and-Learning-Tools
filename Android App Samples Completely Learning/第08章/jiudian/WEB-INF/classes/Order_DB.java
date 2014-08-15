package wyf.wyy;
import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.text.*;
public class Order_DB{
	private static Connection con=null;//聲明Connection引用
	private static Statement stat=null;//聲明Statement引用
	private static ResultSet rs=null;//聲明ResultSet引用
	public static boolean isOrdered(String rgid1){//判斷資源是否被預定中
		boolean b = false;//初始為false，即沒有被預訂
		try{
			String ostatus = new String("預定中".getBytes(),"iso8859-1");
			String rstatus = new String("佔用".getBytes(),"iso8859-1");
			String rgid = new String(rgid1.getBytes(),"iso8859-1");
			con = DB.getCon();
			stat = con.createStatement();
			//查看當前rgid對應的資源有沒有正在預訂中
			rs = stat.executeQuery("select rgid from oinfo where ostatus='"+
								ostatus+"' and rgid='"+rgid+"'");
			if(rs.next()) {b = true;}
			//查看當前資源是否處於空閒狀態
			rs = stat.executeQuery("select rgid from resource where rstatus='"+
								rstatus+"' and rgid='"+rgid+"'");
			if(rs.next()) {b = true;}
		}
		catch(Exception e){e.printStackTrace();}
		finally{DB.closeCon();}//關閉數據庫連接
		return b;//返回結果
	}
	public static Vector<String []> getOrderedDay(String rgid1){
		Vector<String []> v = new Vector<String []>();
		try{
			String ostatus = new String("預訂成功".getBytes(),"iso8859-1");
			String rgid = new String(rgid1.getBytes(),"iso8859-1");
			con = DB.getCon();//得到數據庫連接
			stat = con.createStatement();//創建語句對像
			//查看當前rgid對應的資源已經被預訂的列表（未來七天之內）
			rs = stat.executeQuery("select ftime,etime from oinfo where ostatus='"+
								ostatus+"' and rgid='"+rgid+"'");
			while(rs.next()){				
				String []s =new String[2];
				//得到開始和結束時間
				s[0] = new String(rs.getString(1).getBytes("iso8859-1"),"big5");
				s[1] = new String(rs.getString(2).getBytes("iso8859-1"),"big5");
				java.util.Date etime = Order_DB.chageStringToDate(s[1]);
				java.util.Date now = new java.util.Date();
				//如果結束時間在當前時間之後，則未來七天之內有已經被訂的日期
				if(etime.after(now)) {v.add(s);}
			}
		}
		catch(Exception e)	{e.printStackTrace();}
		finally {DB.closeCon();}//關閉數據庫連接
		return v;//返回結果
	}
	public static java.util.Date chageStringToDate(String s){
		java.util.Date da = null;//聲明日期對像引用
		if(s!=null){
			String p = "-|:| "; //用於拆分的正則式
			String[] d = s.split(p);//得到拆分後的字符串數組			
			int[] date = new int[d.length];//將字符穿數組轉換為int型數組
			for(int i=0;i<d.length;i++)
			{date[i]=Integer.parseInt(d[i]);}
			//調用java.util.Date的構造器構造一個日期對像
			da = new java.util.Date(date[0]-1900,date[1]-1,date[2],date[3],date[4]);
		}	
		return da;//返回日期對像
	}
/*	public static int addOrder(String user,Vector<String[]> OrderList)
	{
		int i = 0;
		int orid = DB.getId("oinfo","orid");//得到訂單明細的主鍵ID+1值
		int oid = DB.getId("olist","oid");//得到訂單表的主鍵ID+1值
		try{			
			con = DB.getCon();
			stat = con.createStatement();
			//得到相關信息組成訂單			
			java.util.Date d = new java.util.Date();
			String otime = d.toLocaleString();
			con.setAutoCommit(false);//禁用自動提交，開始一個事務
			String sqla = "insert into olist(oid,oname,otime) values"+
					"("+oid+",'"+user+"','"+otime+"')";
			String sql = new String(sqla.getBytes(),"iso8859-1");			
			stat.executeUpdate(sql);
			//得到訂單明細信息
			Vector<String> sqlb = new Vector<String>();			
			for(String []s:OrderList){												
				String rgid = s[0];//得到所訂資源號
				//得到開始和結束時間
				String ftime = s[2]; String etime = s[3];
				String sqlc = "insert into oinfo(orid,oid,rgid,ftime,etime) values"+
						"("+orid+","+oid+",'"+rgid+"','"+ftime+"','"+etime+"')";
				String sqld = new String(sqlc.getBytes(),"iso8859-1");
				stat.executeUpdate(sqld);//執行更新
				orid++;//主鍵自加，作為下一條記錄的主鍵
			}
			con.commit();//將事務提交
			con.setAutoCommit(true);//恢復自動提交模式
		}
		catch(Exception e){
			e.printStackTrace();
			i = -1;
			try{con.rollback();}//出現錯誤，發起回滾操作
			catch(Exception ea)	{e.printStackTrace();}
		}
		finally	{DB.closeCon();}
		return i;//返回執行結果,-1代表失敗
	}*/
		public static int addOrder(String user,Vector<String[]> OrderList)
	{
		int i = 0;
		int orid = DB.getId("oinfo","orid");//得到訂單明細的主鍵ID+1值
		int oid = DB.getId("olist","oid");//得到訂單表的主鍵ID+1值
		try{			
			con = DB.getCon();
			stat = con.createStatement();
			//得到相關信息組成訂單			
			//java.util.Date d = new java.util.Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String otime = df.format(new java.util.Date());
			//String otime = d.toLocaleString();
			con.setAutoCommit(false);//禁用自動提交，開始一個事務
			String sqla = "insert into olist(oid,oname,otime) values"+  
					"("+oid+",'"+user+"','"+otime+"')";//訂單編號 用戶名 下定時間
			String sql = new String(sqla.getBytes(),"iso8859-1");			
			stat.executeUpdate(sql);
			//得到訂單明細信息
			Vector<String> sqlb = new Vector<String>();			
			for(String []s:OrderList){												
				String rgid = s[0];//得到所訂資源號
				//得到開始和結束時間
				String ftime = s[1]; String etime = s[2];
				String sqlc = "insert into oinfo(orid,oid,rgid,ftime,etime) values"+
						"("+orid+","+oid+",'"+rgid+"','"+ftime+"','"+etime+"')";
				String sqld = new String(sqlc.getBytes(),"iso8859-1");
				stat.executeUpdate(sqld);//執行更新
				orid++;//主鍵自加，作為下一條記錄的主鍵
			}
			con.commit();//將事務提交
			con.setAutoCommit(true);//恢復自動提交模式
		}
		catch(Exception e){
			e.printStackTrace();
			i = -1;
			try{con.rollback();}//出現錯誤，發起回滾操作
			catch(Exception ea)	{e.printStackTrace();}
		}
		finally	{DB.closeCon();}
		return i;//返回執行結果,-1代表失敗
	}
	public static Vector<String []> getOrderList(String sqla){//得到用戶已提交訂單
		Vector<String []> v = new Vector<String[]>();//創建返回向量
		try{
			con = DB.getCon();//得到數據庫連接
			stat = con.createStatement();//創建語句對像		
			String sql = new String(sqla.getBytes(),"iso8859-1");//轉碼
			rs = stat.executeQuery(sql);//執行查詢
			while(rs.next()){//遍歷結果集
				String s[] = new String[6];
				for(int i=0;i<s.length;i++){//對信息進行轉
					s[i] = new String(rs.getString(i+1).getBytes("iso8859-1"),"big5");
				}				
				v.add(s);//將訂單信息添加進返回向量
			}
		}
		catch(Exception e) {e.printStackTrace();}
		finally	{DB.closeCon();}
		return v;
	}
	public static Vector<String []> getOrderDetail(String oid){//得到某一訂單詳情
		Vector<String []> v = new Vector<String[]>();
		try{
			con = DB.getCon();//得到數據庫連接
			stat = con.createStatement();//創建語句對像
			rs = stat.executeQuery("select rgid,ftime,etime,ostatus from oinfo"+
						" where oid='"+oid+"'");//執行查詢得到結果集			
			while(rs.next()){//遍歷結果集
				String s[] = new String[5];
				for(int i=0;i<s.length-1;i++){//轉碼
					s[i] = new String(rs.getString(i+1).getBytes("iso8859-1"),"big5");
				}										
				v.add(s);//將信息添加到返回向量
			}
			for(String[] s:v){//得到分組名
				String rgid = new String(s[0].getBytes(),"iso8859-1");
				rs = stat.executeQuery("select gName from rgroup where gId=("+
							"select rgroup from resource where rgid='"+rgid+"')");
				rs.next();
				s[4] = new String(rs.getString(1).getBytes("iso8859-1"),"big5");
			}
		}
		catch(Exception e) {e.printStackTrace();}
		finally	{DB.closeCon();}
		return v;
	}
		public static Vector<String []> getOrderListThree(String sqla){//得到用戶已提交訂單其中的三項
		Vector<String []> v = new Vector<String[]>();//創建返回向量
		try{
			con = DB.getCon();//得到數據庫連接
			stat = con.createStatement();//創建語句對像		
			String sql = new String(sqla.getBytes(),"iso8859-1");//轉碼
			rs = stat.executeQuery(sql);//執行查詢
			while(rs.next()){//遍歷結果集
				String s[] = new String[3];
				for(int i=0;i<s.length;i++){//對信息進行轉
					s[i] = new String(rs.getString(i+1).getBytes("iso8859-1"),"big5");
				}				
				v.add(s);//將訂單信息添加進返回向量
			}
		}
		catch(Exception e) {e.printStackTrace();}
		finally	{DB.closeCon();}
		return v;
	}
}
}
