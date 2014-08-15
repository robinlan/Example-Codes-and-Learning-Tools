<%@ page
 contentType="text/html;charset=big5"
 import="java.io.*,javax.servlet.*,wyf.wyy.MyConverter,wyf.wyy.DB"
 %>
 
<% 
   String param1=request.getParameter("params1").trim();
   String param2=request.getParameter("params2").trim();
   
   String uname=MyConverter.unescape(param1);
   String pwd=MyConverter.unescape(param2);
   
   String sqla="select pwd from user where uname='"+uname+"'";
   if(DB.isExist(sqla)){
				String sql = "select pwd from user where uname='"+uname+"'";
				String password=DB.getInfo(sql).trim();//從數據庫得到密碼
				if(pwd.equals(password)){
				   out.println(MyConverter.escape("登錄成功"));	
				}
				else{
				   out.println(MyConverter.escape("登錄失敗"));	 
				}
	}
   else
   {
       out.println(MyConverter.escape("用戶不存在，請重新輸入"));
   }

%>

 
 
