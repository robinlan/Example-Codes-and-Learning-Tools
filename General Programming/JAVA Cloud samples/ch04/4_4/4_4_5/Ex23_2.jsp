<%@ page contentType= "text/html;charset=big5" %>
<html>
<head><title>Ex23_2</title></head><body>
<%
  request.setCharacterEncoding("big5");
  session = request.getSession();
  String sessionID = session.getId();
  out.print("sessionID : " + sessionID + "<br>");
%>
</body>
</html>