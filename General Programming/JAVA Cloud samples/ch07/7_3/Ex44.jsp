<%@ page contentType= "text/html;charset=big5" %>
<%@ page import = "java.io.*" %>
<html>
<head><title>Ex43</title></head><body>
<%
  request.setCharacterEncoding("big5");
  String val = request.getParameter("dir");
  File f = new File(val);
  String dirFiles[] = f.list();

  out.print(val + " : " + "<br>");
  int i;
  for(i = 0; i < dirFiles.length; i++) {
        File fd = new File(val + "\\" + dirFiles[i]);

        if (fd.isFile()) out.print("File: ");
        else if (fd.isDirectory()) out.print("Dir : ");
        else out.print("Err : ");

        out.print( dirFiles[i] + "<br>");
   }
%>
</body>
</html>