<%@ page contentType="text/html;charset=big5" %>
<%
  class Mynumber {
      int number;
  }
%>
<html>
<head><title>Ex08</title></head>
<body> 
<% 
  Mynumber a = new Mynumber();
  a.number = 10;
  out.print("物件 a 儲存之 number 為：" + a.number); 
 %>
</body>
</html>