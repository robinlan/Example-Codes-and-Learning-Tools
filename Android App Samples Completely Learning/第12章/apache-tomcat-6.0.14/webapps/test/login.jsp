<%@ page
 contentType="text/html;charset=gbk"
%>
 
<% 
   String uid=request.getParameter("uid");
   String pwd=request.getParameter("pwd");  
      
   if(uid.equals("wyf")&&pwd.equals("1234"))
   {
	   out.print("ok");
   }else
   {
	   out.print("fail");
   }      
%>