package wyf.wyy;
import java.io.*; 
import java.util.*;
import javax.servlet.*; 
import javax.servlet.http.*;
public class ListServlet extends HttpServlet
{
	public void init(ServletConfig conf) throws ServletException
	{//Servlet的inti初始化方法
		super.init(conf);
	}
	public void doGet(HttpServletRequest req,HttpServletResponse res)
	throws ServletException, IOException{//doGet方法
		doPost(req,res);//調用doPost方法
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res)
	throws ServletException,IOException{
		req.setCharacterEncoding("big5");//設置req編碼為big5
		res.setCharacterEncoding("big5");//設置res編碼為big5		
		String action = req.getParameter("action");//得到請求的響應action		
		HttpSession session=req.getSession(true);//得到session對像
		//得到登陸的管理員ID		
		String adnameSes = (String)session.getAttribute("adname");
		String msg = "";//聲明消息字符串
		/*if(action.equals("list")){//得到分組的預訂規則			
			int gId = Integer.parseInt(req.getParameter("gId"));
			Vector<String> list = DB.getGroupInfo(gId);
			session.setAttribute("list",list);
			res.sendRedirect("groupList.jsp");
		}
		else if(action.equals("status")){
			Vector<String []> v = null;//發送到顯示資源狀態頁面的信息
			String isOrdered = "NO";//是否被預定，初始為"NO"，即沒有被預訂
			String rgid = req.getParameter("rgid");//得到要查看狀態的資源編號
			//資源正在被預定中，未處理之前不再接受訂單
			if(Order_DB.isOrdered(rgid)) {isOrdered = "YES";}
			//可以預訂時，從數據庫得到已經被預訂列表
			else {v = Order_DB.getOrderedDay(rgid);}
			//設置資源狀態的一些信息並轉發到顯示頁面
			req.setAttribute("v",v);
			req.setAttribute("rgid",rgid);
			req.setAttribute("isOrdered",isOrdered);	
			req.getRequestDispatcher("lookRes.jsp").forward(req,res);			
		}*/
//************************************資源***********************************************
		if(action.equals("adminList")){
			Vector<String> list = new Vector<String>();
			if(session.getAttribute("adname")!=null){//判斷管理員是否登陸
			    int gId = Integer.parseInt(req.getParameter("gId"));
			    if(gId==0){//0代表所有分組
			    	list.add("0");
			    	list.add("所有分組");
			    }
			    else{//某一特定分組
			    	list = DB.getGroupInfo(gId);
			    }								
				session.setAttribute("list",list);
				res.sendRedirect("adminResource.jsp");
			}
			else{//管理員沒有登陸
				msg = "請先登陸";
				this.forward(req,res,msg,"adinfo.jsp");		 
			}
		}
		else if(action.equals("queryRes")){//查詢
			String rgid = req.getParameter("rgid");//得到資源ID
			String sql = "select rgid,rlevel,rmoney,rdetail,rstatus,rgroup,rid "+
										"from resource where rgid='"+rgid+"'";			
			Vector<String[]> v = DB.getResInfo(sql);//執行查詢
			req.setAttribute("list",v);
			req.getRequestDispatcher("ResQuery.jsp").forward(req,res);	
		}
		
		else if(action.equals("editRes")){//編輯資源
			int rid = Integer.parseInt(req.getParameter("rid"));
			String sql = "select rgid,rlevel,rmoney,rdetail,rstatus,rgroup,rid from resource where rid='"+rid+"'";
			Vector<String []> rinfo = DB.getResInfo(sql);
			req.setAttribute("rinfo",rinfo);
			req.getRequestDispatcher("ResInfo.jsp").forward(req,res);
		}
		else if(action.equals("changeRes")){			
			String rgidBefor = req.getParameter("rgidBefor");//編輯前的編號
			String rgidAfter = req.getParameter("rgidAfter");//編輯後的編號			
			int rid = Integer.parseInt(req.getParameter("rid"));//得到資源ID主鍵
			//得到修改後的信息
			String rgroup = req.getParameter("rgroup");
			String rlevel = req.getParameter("rlevel").trim();
			double rmoney = Double.parseDouble(req.getParameter("rmoney").trim());
			String rdetail = req.getParameter("rdetail").trim();
			String rstatus = req.getParameter("rstatus");			
			String sql = "update resource set rgid='"+rgidAfter+"',rgroup='"+rgroup+"',rlevel='"+rlevel+
			             "',rmoney="+rmoney+",rdetail='"+rdetail+"',rstatus='"+rstatus+"' where rid="+rid;			
			if(rgidBefor.equals(rgidAfter)){//組內編號沒改變
				if(DB.update(sql)>0){
					msg = "修改保存成功<br><br><a href=ListServlet?action=adminList&&gId=0>返回";
				}
			}
			else{//組內編號改變了
			    String sqla = "select * from resource where rgid='"+rgidAfter+"'";
				if(DB.isExist(sqla)){
					msg = "已經有此編號的資源，請核對輸入。<br><br>"+
				      	  "<a href=ListServlet?action=editRes&&rid="+rid+">返回繼續修改";
				}
			 	else{
			 		if(DB.update(sql)>0){
			 			msg = "修改保存成功<br><br><a href=ListServlet?action=adminList&&gId=0>返回";
			 		}
			 	}
			}
			this.forward(req,res,msg,"adinfo.jsp");		 	
		}
		else if(action.equals("deleteRes")){
			String rid = req.getParameter("rid");//得到要刪除的ID號
			String sql = "delete from resource where rid='"+rid+"'";
			if(DB.update(sql)>0){
				msg = "刪除成功<br><br><a href=ListServlet?action=adminList&&gId=0>返回";						 
			}
			else{
				msg = "未知錯誤，刪除失敗";
			}
			this.forward(req,res,msg,"adinfo.jsp");
		}
		else if(action.equals("addRes")){
			//得到要添加資源的詳細信息
			String rgid = req.getParameter("rgid").trim();
			String rgroup = req.getParameter("rgroup");
			String rlevel = req.getParameter("rlevel").trim();
			Double rmoney = Double.parseDouble(req.getParameter("rmoney").trim());
			String rdetail = req.getParameter("rdetail").trim();
			String rstatus = req.getParameter("rstatus");			
			String sql = "select * from resource where rgid='"+rgid+"'";
			if(DB.isExist(sql))	{
				msg="此編號對應的資源已經存在，請核對編號輸入。<br><a href=addRes.jsp>返回";
			}
			else{
				int rid = DB.getId("resource","rid");//得到資源表中主鍵最大值加1
				sql = "insert into resource(rid,rgroup,rgid,rlevel,rmoney,rdetail,rstatus)"+
				      "values("+rid+",'"+rgroup+"','"+rgid+"','"+rlevel+"',"+rmoney+",'"+
				       rdetail+"','"+rstatus+"')";
				if(DB.update(sql)>0){
					msg = "增加資源成功！！！<br><a href=addRes.jsp>返回";
				}
			}
			this.forward(req,res,msg,"adinfo.jsp");		 
		}
//***************************分組*******************************************
		else if(action.equals("adminGroup")){
			if(session.getAttribute("adname")!=null){//判斷管理員是否登陸			   
				res.sendRedirect("adminGroup.jsp");
			}
			else{//沒有登陸
				msg = "請先登陸";
				this.forward(req,res,msg,"adinfo.jsp");//轉發到消息 頁面	 
			}
		}
		else if(action.equals("editGroup")){//編輯分組
			int gId = Integer.parseInt(req.getParameter("gId"));
			Vector<String> ginfo = DB.getGroupInfo(gId);
			req.setAttribute("ginfo",ginfo);
			req.getRequestDispatcher("GroupInfo.jsp").forward(req,res);
		}
		else if(action.equals("changeGroup")){//修改分組信息後提交		    
		    int gId = Integer.parseInt(req.getParameter("gId"));//得到分組ID
		    String gNameBefor = req.getParameter("gNameBefor");//得到修改前的名字
		    //得到修改後的信息
		    String gNameAfter = req.getParameter("gNameAfter");
		    String gImg = req.getParameter("gImg");
		    String gDetail = req.getParameter("gDetail");
		    String gOrderDet = req.getParameter("gOrderDet");		    
		    String sql = "update rgroup set gName='"+gNameAfter+"',gImg='"+gImg+"',gDetail='"+gDetail+
		    			"',gOrderDet='"+gOrderDet+"' where gId="+gId;//拼裝SQL		    
		    if(gNameAfter.equals(gNameBefor)){//判斷分組名字是否改變
		    	if(DB.update(sql)>0){
					msg = "修改保存成功<br><br><a href=ListServlet?action=adminGroup>返回";
				}
		    }
		    else{//分組名字改變
		    	String sqla = "select * from rgroup where gName='"+gNameAfter+"'";
				if(DB.isExist(sqla)){//名字已經存在
					msg = "已經有此名字的分組，請核對輸入。<br>"+
							"<a href=ListServlet?action=editGroup&&gId="+gId+">返回";
				}
			 	else{//分組名不存在
			 		if(DB.update(sql)>0){
			 			msg = "修改保存成功<br><br><a href=ListServlet?action=adminGroup>返回";
			 		}
			 	}
		    }
		   this.forward(req,res,msg,"adinfo.jsp");		 			
		}
		else if(action.equals("deleteGroup")){
			int gId = Integer.parseInt(req.getParameter("gId"));//得到要刪除的ID
			String sqla = "delete from resource where rgroup="+gId;//刪除分組記錄的SQL
			String sqlb = "delete from rgroup where gId="+gId;//刪除分組下資源的SQL
			if(DB.update(sqla,sqlb)==true){
				msg = "刪除分組成功<br><br><a href=ListServlet?action=adminGroup>返回";
			}
			else{
				msg = "未知錯誤，刪除失敗";
			}
			this.forward(req,res,msg,"adinfo.jsp");		 
		}
		else if(action.equals("addGroup")){
			int gId = DB.getId("rgroup","gId");//得到添加分組的ID
			//得到填寫的信息
			String gName = req.getParameter("gName");
			String gImg = req.getParameter("gImg");
		    String gDetail = req.getParameter("gDetail");
		    String gOrderDet = req.getParameter("gOrderDet"); 
		    String sql = "insert into rgroup(gId,gName,gImg,gDetail,gOrderDet)values('"+gId+"','"+
		    				gName+"','"+gImg+"','"+gDetail+"','"+gOrderDet+"')";//拼裝SQL   
		    if(DB.update(sql)>0){//執行更新
		    	msg = "添加分組成功<br><br><a href=ListServlet?action=adminGroup>返回";
		    }
		    else{
		    	msg = "未知錯誤，添加失敗";
		    }
		    this.forward(req,res,msg,"adinfo.jsp");	
		}
//********************************管理員*****************************************
		else if(action.equals("admanage")){			
			if(adnameSes==null){//判斷管理員是否登陸
			    msg = "請先登陸";
			    this.forward(req,res,msg,"adinfo.jsp");		 				
			}
			else{
				String sql = "select adlevel from adinfo where adname='"+adnameSes+"'";				
				int adlevel = Integer.parseInt(DB.getInfo(sql));//得到管理員級別
				if(adlevel!=1){
					msg = "對不起，權限不夠";
					this.forward(req,res,msg,"adinfo.jsp");		 
				}
				else{
					res.sendRedirect("adminManage.jsp");
				}
			}
		}
		else if(action.equals("addAdmin")){//添加管理員
			String adnameAdd = req.getParameter("adname");
			String adpwd = req.getParameter("adpwd");
			String sql = "insert into adinfo(adname,adpwd,adlevel)values"+
			  			 "('"+adnameAdd+"','"+adpwd+"',"+0+")";
			String sqla = "select * from adinfo where adname='"+adnameAdd+"'";
			if(DB.isExist(sqla)){
				msg = "此管理員ID已經存在請核對輸入";
			}
			else{
				if(DB.update(sql)>0)
				msg = "添加管理員成功";
			}		
			this.forward(req,res,msg,"adinfo.jsp");		 
		}
		else if(action.equals("deleteAdmin")){
			//得到要刪除的管理員ID
			String adnameDel = req.getParameter("adname");
			if(adnameDel.equals(adnameSes)){
				msg = "不能刪除自己";
			}
			else{
				String sql = "delete from adinfo where adname='"+adnameDel+"'";
				DB.update(sql);
				msg = "刪除成功";
			}
			this.forward(req,res,msg,"adinfo.jsp");
		}
		else if(action.equals("resetPwd")){
			String adname = req.getParameter("adname").trim();
			String adpwd = req.getParameter("adpwd").trim();
			String sql = "update adinfo set adpwd='"+adpwd+"' where adname='"+adname+"'";
			if(DB.update(sql)>0){
				msg = "設置密碼成功";
			}
			else{
				msg = "設置失敗，請重新檢查設置";
			}
			this.forward(req,res,msg,"adinfo.jsp");
		}
		else if(action.equals("changePwd")){//管理員修改密碼
			String adname = req.getParameter("adname");
			String adpwd = req.getParameter("adpwd");
			String newPwd = req.getParameter("newPwd");
			String sql = "select adpwd from adinfo where adname='"+adname+"'";
			String pwdFromDB = DB.getInfo(sql);
			if(pwdFromDB==null){
				msg = "該管理員用戶不存在，請重新核對輸入";
			}
			else if(!pwdFromDB.equals(adpwd)){
				msg = "密碼輸入不正確，請重新輸入";
			}
			else{
				sql = "update adinfo set adpwd='"+newPwd+"' where adname='"+adname+"'";				
				if(DB.update(sql)>0){
					msg = "修改成功";
				}
				else{
					msg = "未知錯誤，修改失敗！！！";
				}				
			}
			this.forward(req,res,msg,"adinfo.jsp");			
		}			
}
	public void forward(HttpServletRequest req,HttpServletResponse res,
	String msg,String url)throws ServletException,IOException
	{
		req.setAttribute("msg",msg);//設置消息
		req.getRequestDispatcher("adinfo.jsp").forward(req,res);//轉發到消息頁面
	}
}
