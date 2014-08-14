<%@ page contentType= "text/html;charset=big5" %>
<%@ page import = "java.io.*" %>
<html>
<head><title>Ex46</title></head><body>
<%
  request.setCharacterEncoding("big5");
  String val_fOld = request.getParameter("f_Old");
  String val_fNew = request.getParameter("f_New");

  File fNew = new File(val_fNew);
  if (fNew.createNewFile()) 
      out.print("成功建立新檔案" + val_fNew +"<br>");

  BufferedReader bfOld = new BufferedReader(new FileReader(val_fOld));
  BufferedWriter bfNew = new BufferedWriter(new FileWriter(val_fNew));
  int msg;
  while ((msg = bfOld.read()) != -1) 
        bfNew.write((char)msg);

  out.print("成功移置檔案" + "<br>");
  bfOld.close();
  bfNew.close();

  File fOld = new File(val_fOld);
  if (fOld.delete()) 
      out.print("成功刪除原檔案" + val_fOld + "<br>");
%>
</body>
</html>