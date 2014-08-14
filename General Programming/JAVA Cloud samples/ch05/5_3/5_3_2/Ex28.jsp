<%@ page contentType="text/html;charset=big5" %>
<html>
<head><title>Ex28</title></head><body>
<%
  request.setCharacterEncoding("big5");
  String val = request.getParameter("data");
  out.print("Data: " + val);
%>
</body>
</html>