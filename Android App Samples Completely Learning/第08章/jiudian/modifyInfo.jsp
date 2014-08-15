<%@ page
 contentType="text/html;charset=big5"
 import="java.io.*,javax.servlet.*,wyf.wyy.MyConverter,wyf.wyy.DB,java.util.*"
 %>
 <%
   String param1=request.getParameter("param1").trim();//姓名
   String param2=request.getParameter("param2").trim();//密碼
   String param3=request.getParameter("param3").trim();//手機號
   String param4=request.getParameter("param4").trim();//真實姓名
   String param5=request.getParameter("param5").trim();//性別
   String param6=request.getParameter("param6").trim();//電子郵箱
   String uname=MyConverter.unescape(param1);
   String mm=MyConverter.unescape(param2);
   String telnum=MyConverter.unescape(param3);
   String realname=MyConverter.unescape(param4);
   String sex=MyConverter.unescape(param5);
   String email=MyConverter.unescape(param6);
   Vector<String> userInfo=new Vector<String>(); 
   String sqla="update user set pwd='"+mm+"',telNum='"+telnum+"',realName='"+
   realname+"',gender='"+sex+"',email='"+email+"' where uname='"+uname+"'";
   if(DB.updatea(sqla)){
	 out.println(MyConverter.escape("更新成功，請重新登錄！"));
	 	//System.out.println("更新成功！！！");
	}
   else
   {
   //System.out.println("更新失敗！！！！");
   }
%>
