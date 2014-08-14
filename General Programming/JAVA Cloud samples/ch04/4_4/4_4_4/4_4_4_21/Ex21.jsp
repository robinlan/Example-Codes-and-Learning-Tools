<%@ page contentType= "text/html;charset=big5" %>
<html>
<head><title>Ex21</title></head><body>
<%
  request.setCharacterEncoding("big5");

  String user = request.getParameter("user");
  String pwd = request.getParameter("pwd");

  if(user.equals("¸ë»T¥Í") && pwd.equals("1234"))
      out.print("¦WºÙ±K½XµL»~");
  else
      response.sendRedirect("http://163.15.40.242:8080/examples/Ex21.html");
%>
</body>
</html>