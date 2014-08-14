<%@ page contentType= "text/html;charset=big5" %>
<html>
<head><title>Ex100_2</title></head><body>
<%
  request.setCharacterEncoding("big5");
  out.print("Sub Page of Ex100_2 輸入即時訊息" + "<br>");
  out.print("" + "<br>");

  session = request.getSession();

  if(session.getAttribute("ex100") == "true") {
       out.print("本網頁為合法認證網頁" + "<br>");
       out.print("<FORM ACTION = Ex100_3.jsp " +
                       "METHOD = post>");
       out.print("輸入即時訊息：" + "<br>");
       out.print("<TEXTAREA NAME = data " +
                       "ROWS = 3 COLS= 40>" + "</TEXTAREA>" + "<br>");
       out.print("<INPUT TYPE = submit VALUE = \"遞送\">");
       out.print("<INPUT TYPE = reset VALUE = \"取消\">");
  }
   else
       out.print("本網頁為非法認證網頁無法執行" + "<br>");
%>
</body>
</html>