<%@ page contentType= "text/html;charset=big5" %>
<html>
<head><title>Ex23_1</title></head><body>
<%
  request.setCharacterEncoding("big5");
  session = request.getSession();
  String sessionID = session.getId();
  out.print("sessionID : " + sessionID + "<br>");
%>
<FORM METHOD="post" ACTION="Ex23_2.jsp">
<INPUT TYPE="submit" VALUE="go to Sub Page">
</body>
</html>