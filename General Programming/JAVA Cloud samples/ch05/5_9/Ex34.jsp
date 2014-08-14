<%@ page contentType= "text/html;charset=big5" %>
<%@ page import = "java.io.*" %>
<html>
<head><title>Ex34</title></head><body>
<%
  request.setCharacterEncoding("big5");
  String val = request.getParameter("dir");
  File f = new File(val);

  if (f.delete())
      out.print("成功刪除目錄 : " + val +"<br>");
  else
      out.print("刪除目錄失敗" + "<br>");
%>
</body>
</html>