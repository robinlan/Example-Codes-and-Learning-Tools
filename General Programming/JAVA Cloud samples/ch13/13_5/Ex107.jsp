<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*" %>
<%@ page import= "java.io.*" %>
<html>
<head><title>Ex107</title></head><body>
<p align="left">
<font size="5"><b>Sub Page of Ex107 讀取資料庫特定留言</b></font>
</p><HR>
<%
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB13";

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

  request.setCharacterEncoding("big5");
  String MsgName = request.getParameter("msgName");
  String sql="SELECT * FROM boardMessage WHERE 名稱= '" +
                   MsgName + "';" ;

  if (stmt.execute(sql))   {
      ResultSet rs = stmt.getResultSet();
      while (rs.next()) {
          %>
          時間：<%= rs.getString("時間")%><BR>
          名稱：<%= rs.getString("名稱")%><BR>
          信箱：<%= rs.getString("信箱")%><BR>
          留言：<BR>
           <%= rs.getString("留言")%><BR><HR>
           <%
        }
  }
  stmt.close();
  con.close();
%>
</body>
</html>