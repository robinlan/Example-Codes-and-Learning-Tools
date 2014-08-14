<%@ page contentType="text/html;charset=big5" %>
<html>
<head><title>Ex27</title></head><body>
<%
  String val = request.getParameter("data");
  out.print("Data: " + val);
%>
</body>
</html>