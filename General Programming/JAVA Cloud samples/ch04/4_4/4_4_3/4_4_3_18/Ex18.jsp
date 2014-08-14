<%@ page contentType="text/html;charset=big5" %>
<%@ page import = "java.util.*" %>
<html>
<head><title>Ex18</title></head><body>
<table border= "1">
<%
  String item= null , value= null;

  Enumeration en= request.getHeaderNames();
  while(en.hasMoreElements())
      {
        item=(String)en.nextElement();
         value = request.getHeader(item);
%>
<tr><td><%=item%></td><td><%=value%></td></tr>
<%
       }
%>
</table>
</body>
</html>