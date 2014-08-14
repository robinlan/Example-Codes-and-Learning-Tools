<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*" %>
<%@ page import= "java.io.*" %>
<html>
<head><title>Ex103</title></head><body>
<p align="left">
<font size="5"><b>Page of Ex103 跑馬燈雲端訊息</b></font>
</p><HR>
<%
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB12";
  StringBuffer sb = new StringBuffer();

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

  request.setCharacterEncoding("big5");
  String StartTime = request.getParameter("startTime");
  String EndTime = request.getParameter("endTime");

  String sql="SELECT 時間, 訊息  FROM instantNews WHERE 時間索引>='" +
                    StartTime + "' AND 時間索引<='" +  EndTime + "';";

  if (stmt.execute(sql))   {
      ResultSet rs = stmt.getResultSet();
      while (rs.next()) {
          sb.append( rs.getString("時間"));
          sb.append( rs.getString("訊息"));
          sb.append("!!      ");
      }
  }
  String Info= sb.toString();
  %>
  <b><font color="#FF0000"><marquee scrolldelay="120" loop="5"        bgcolor="#00FFFF"><%= Info%>～</marquee></font></b>
  <%
  stmt.close();
  con.close();
%>
</body>
</html>