<%@ page contentType="text/html;charset=big5" %>
<%
  class Mynumber {
      int i;
  }
%>
<html>
<head><title>Ex10</title></head>
<body> 
<% 
  int j = 5;
  out.print("內建物件之 j 為： " + j + "<br>");

  Mynumber a = new Mynumber();
  a.i = 10;
  out.print("建構物件之 i 為： " + a.i); 
 %>
</body>
</html>