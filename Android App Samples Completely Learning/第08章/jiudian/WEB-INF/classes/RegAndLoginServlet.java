package wyf.wyy;
import java.io.*; 
import javax.servlet.*; 
import javax.servlet.http.*;
import java.util.*;
public class RegAndLoginServlet extends HttpServlet
{
	public void init(ServletConfig conf) throws ServletException 
	{ 
		super.init(conf);
	}
	public void doGet(HttpServletRequest req,HttpServletResponse res)
	throws ServletException, IOException 
	{
		doPost(req,res);
	}
	public void doPost(HttpServletRequest req,HttpServletResponse res)
	throws ServletException, IOException
	{
		req.setCharacterEncoding("big5");
		res.setCharacterEncoding("big5");
		//得到請求的響應action		
		String action = req.getParameter("action");
		//得到session對像
		HttpSession session=req.getSession(true);
		PrintWriter pw=res.getWriter();
		String msg="";
	/*if(action.equals("reg")){			
		//得到註冊用戶填寫的資料
		String uname = req.getParameter("uname").trim();			
		String pwd = req.getParameter("pwd").trim();
		String telNum = req.getParameter("telNum").trim();
		String realName = req.getParameter("realName").trim();
		String gender = req.getParameter("gender");
		String email = req.getParameter("email").trim();
		//拼裝查看用戶是否存在的SQL
		String sqla = "select * from user where uname='"+uname+"'";
		if(DB.isExist(sqla)){
			msg=uname+":用戶名已存在，請試試另一個！！！<br/><a href=reg.jsp>重新註冊</a>";
		}
		else{//插入新用戶的SQL				
			String sql = "insert into user values ('"+uname+"','"+pwd+"'"+
					",'"+telNum+"','"+realName+"','"+gender+"','"+email+"')";							
			DB.update(sql);//執行插入動作
			msg=uname+":註冊成功！！！<br/><a href=login.jsp>現在登陸？？？</a>";
		}
		req.setAttribute("msg",msg);//轉發消息
		req.getRequestDispatcher("info.jsp").forward(req,res);
	}

		else if(action.equals("login")){			
			session.removeAttribute("uname");//從session中移除當前登陸用戶
			String uname = req.getParameter("uname").trim();//得到登陸用戶名
			String pwd =  req.getParameter("pwd").trim();//得到登陸密碼
			//拼裝查看用戶是否存在的SQL
			String sqla = "select * from user where uname='"+uname+"'";
			if(DB.isExist(sqla)){
				String sql = "select pwd from user where uname='"+uname+"'";
				String password=DB.getInfo(sql).trim();//從數據庫得到密碼
				if(pwd.equals(password)){				
					session.setAttribute("uname",uname);//登陸成功
					res.sendRedirect("main.jsp");//跳轉到主頁	
				}
				else{
					msg=uname+"密碼不正確！！！<br><br>"+
					       "<a href=login.jsp>重新登陸</a>";					
					req.setAttribute("msg",msg);//將信息發送到信息顯示頁面
					req.getRequestDispatcher("info.jsp").forward(req,res);				
				}
			}
			else{
				msg=uname+"此用戶名不存在！！！<br><br>"+
				     "<a href=login.jsp>重新登陸</a>";				
				req.setAttribute("msg",msg);//將信息發送到信息顯示頁面
				req.getRequestDispatcher("info.jsp").forward(req,res);
			}	
		}						
		else if(action.equals("logout")){
			session.removeAttribute("uname");//從session中移除登陸用戶
			msg = "歡迎再次光臨！！！";//提示消息
			req.setAttribute("msg",msg);//設置並轉發提示消息
			req.getRequestDispatcher("info.jsp").forward(req,res);
		}		
		else if(action.equals("changeMyInfo")){
			String uname=(String)session.getAttribute("uname");
			//得到修改後的信息
			String telNum = req.getParameter("telNum").trim();
			String realName = req.getParameter("realName").trim();
			String gender = req.getParameter("gender");
			String email = req.getParameter("email").trim();
			//生成更新SQL
			String sql="update user set telNum='"+telNum+"',realName='"+realName+
			   "',gender='"+gender+"',email='"+email+"'where uname='"+uname+"'";
			if(DB.update(sql)==1){
				msg = "修改資料成功！！！<br>";				
				req.setAttribute("msg",msg);//將信息發送到信息顯示頁面
				req.getRequestDispatcher("info.jsp").forward(req,res);
			}
		}
		else if(action.equals("changePwd")){
			//得到提交的信息
			String uname=(String)session.getAttribute("uname");
			String currentPwd = req.getParameter("currentPwd").trim();
			String newPwd = req.getParameter("newPwd").trim();
			//從數據庫拿到當前用戶的密碼
			String sqla = "select pwd from user where uname='"+uname+"'";
			String pwdFromDB =  DB.getInfo(sqla);			
			if(currentPwd.equals(pwdFromDB)){//比較密碼
				String sqlb = "update user set pwd='"+newPwd+"'where uname='"+
								uname+"'";//更新此用戶的密碼
				DB.update(sqlb);//執行更新
				msg = "密碼修改成功！！！下一次請用新密碼登陸。<br>";
			}
			else{
				msg = "您輸入的密碼不正確，修改失敗！！！<br>"+
				       "<a href=changeMyInfo.jsp>返回繼續修改？？？";
			}			
			req.setAttribute("msg",msg);//將信息發送到信息顯示頁面
			req.getRequestDispatcher("info.jsp").forward(req,res);
		}*/

		if(action.equals("adlogin")){
			session.removeAttribute("adname");
			String adname = req.getParameter("adname").trim();//得到登陸名
			String pwd =  req.getParameter("pwd").trim();//得到登陸密碼
			//拼裝從數據庫得到登陸管理員密碼的SQL語句
			String sqla = "select adpwd from adinfo where adname='"+adname+"'";
			String pwdFromDB = DB.getInfo(sqla);//執行查詢得到正確密碼
			if(pwdFromDB!=null&&pwd.equals(pwdFromDB)){//登陸成功			
				session.setAttribute("adname",adname);//將登陸管理員保存進session
				msg = "登陸成功。";//提示登陸成功
			}
			else{//登陸失敗的提示
				msg = "錯誤的用戶名和密碼，請重新登陸<br><br>"+
				 		"<a href=adindex.jsp>重新登陸";				
			}
			//將信息發送到信息顯示頁面
			req.setAttribute("msg",msg);
			req.getRequestDispatcher("adinfo.jsp").forward(req,res);
		}
		else if(action.equals("adlogout")){//註銷
			session.removeAttribute("adname");
			msg = "退出成功。";//提示註銷成功
			req.setAttribute("msg",msg);
			req.getRequestDispatcher("adinfo.jsp").forward(req,res);
		}
	}
}
