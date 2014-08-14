<%@ page contentType= "text/html;charset=big5" %>
<%@ page import = "java.io.*" %>
<html>
<head><title>Ex32</title></head><body>
<%
  request.setCharacterEncoding("big5");
  String val_fil = request.getParameter("fil");
  BufferedReader fin = new BufferedReader(new FileReader(val_fil));
  int msg;

  while ((msg = fin.read()) != -1)
       out.println((char)msg);

  fin.close();
%>
</body>
</html>