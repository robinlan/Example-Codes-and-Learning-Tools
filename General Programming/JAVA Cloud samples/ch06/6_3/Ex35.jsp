<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*" %>
<html>
<head><title>Ex35</title></head><body>
<p align="center">
<font size="5"><b>Sub Page of Ex35</b></font>
</p>
<center>
已成功執行SQL指令
<%
  request.setCharacterEncoding("big5");
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB06";

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();
  String query = request.getParameter("SQLcmd");
   stmt.executeUpdate(query);

   stmt.close();
   con.close();
%>
</body>
</html>