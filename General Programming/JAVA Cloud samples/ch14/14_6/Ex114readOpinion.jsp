<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*" %>
<%@ page import= "java.io.*" %>
<html>
<head><title>Ex114</title></head><body>
<p align="left">
<font size="5"><b>Page of Ex114 讀取回應意見</b></font>
</p><HR>
<%
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB14";

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

  request.setCharacterEncoding("big5");
  String indexStr = request.getParameter("respIndex");

  String sql1="SELECT * FROM articleINFO WHERE 編號= " +
                      indexStr + ";" ;

  if (stmt.execute(sql1))   {
     ResultSet rs = stmt.getResultSet();
     while (rs.next()) {
        String timeStr= rs.getString("時間");
        String nameStr= rs.getString("名稱");
        String emailStr= rs.getString("信箱");
        String articalStr= rs.getString("文章");

        out.print("時間：" + timeStr + "<BR>");
        out.print("名稱：" + nameStr + "<BR>");
        out.print("信箱：" + emailStr + "<BR>");
        out.print("文章<BR>" + articalStr + "<BR><HR><HR>");
      }
  }

  String sql2="SELECT * FROM responseINFO WHERE 原文章編號= " +
                      indexStr + ";" ;

  if (stmt.execute(sql2))   {
     ResultSet rs = stmt.getResultSet();
     while (rs.next()) {
        String resptimeStr= rs.getString("回應時間");
        String respnameStr= rs.getString("回應者名稱");
        String respemailStr= rs.getString("回應者信箱");
        String resparticalStr= rs.getString("回應者意見");

        out.print("回應時間：" + resptimeStr + "<BR>");
        out.print("回應者名稱：" + respnameStr + "<BR>");
        out.print("回應者信箱：" + respemailStr + "<BR>");
        out.print("回應者意見<BR>" +  resparticalStr + "<BR>");

        out.print("<HR>");
 
        }
  }
  stmt.close();
  con.close();
%>
</body>
</html>