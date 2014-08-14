<%@ page contentType="text/html;charset=big5" %>
<html>
<head><title>Ex24</title></head><body>
<p align="left">
<font size="5"><b>Sub Page of Ex24</b></font>
</p>
<%
  request.setCharacterEncoding("big5");
  String val = request.getParameter("data");
  application.setAttribute("application_ex24", val);

  Object info= application.getAttribute("application_ex24");
  String infoStr= (String)info;
%>

  application_ex24資訊內容為： <%= infoStr %>
</body>
</html>