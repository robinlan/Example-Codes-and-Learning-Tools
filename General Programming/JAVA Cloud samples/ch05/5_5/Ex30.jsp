<%@ page contentType= "text/html;charset=big5" %>
<%@ page import = "java.io.*" %>
<html>
<head><title>Ex30</title></head><body>
<%
  request.setCharacterEncoding("big5");
  String val = request.getParameter("fil");
  File f = new File(val);

  if (f.createNewFile())
        out.print("成功建立新檔案 : " + val +"<br>");
   else
        out.print("建立新檔案失敗" + "<br>");
%>
</body>
</html>