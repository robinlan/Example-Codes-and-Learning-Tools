<%@ page contentType= "text/html;charset=big5" %>
<%@ page import = "java.io.*" %>
<html>
<head><title>Ex33</title></head><body>
<%
  request.setCharacterEncoding("big5");
  String val = request.getParameter("fil");
  File f = new File(val);

  if (f.delete())
      out.print("成功刪除檔案 : " + val +"<br>");
  else
      out.print("刪除檔案失敗" + "<br>");
%>
</body>
</html>