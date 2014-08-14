<%@ page contentType= "text/html;charset=big5" %>
<%@ page import = "java.io.*" %>
<html>
<head><title>Ex29</title></head><body>
<%
  request.setCharacterEncoding("big5");
  String val = request.getParameter("dir");
  File f = new File(val);

   if (f.mkdir())
        out.print("成功建立目錄 : " + val +"<br>");
   else
        out.print("建立目錄失敗" + "<br>");
%>
</body>
</html>