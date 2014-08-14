<%@ page contentType= "text/html;charset=big5" %>
<html>
<head><title>Ex82_2</title></head><body>
<%
  request.setCharacterEncoding("big5");
  out.print("This is the Sub Page of Ex82_2" + "<br>");
  out.print("" + "<br>");

  session = request.getSession();

  if(session.getAttribute("ex82") == "true")
       out.print("本網頁為合法認證網頁" + "<br>");
   else
       out.print("本網頁為非法認證網頁" + "<br>");
%>
</body>
</html>