<%@ page contentType= "text/html;charset=big5" %>
<%@ page import= "java.util.*" %>
<% Date T= new Date(); %>
<html>
<head><title>Ex19</title></head><body>

<%
 response.addIntHeader("refresh", 5);

  out.print("This is the page of Ex19" + "<br>");
  out.print("" + "<br>");

  out.print("本網頁每5秒重整一次" + "<br>");
  out.print("" + "<br>");

  out.print("現在時間: " + T);
%>
</body>
</html>