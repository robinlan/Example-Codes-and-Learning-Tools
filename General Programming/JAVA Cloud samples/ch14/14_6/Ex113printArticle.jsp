<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*" %>
<%@ page import= "java.io.*" %>
<html>
<head><title>Ex113</title></head><body>
<p align="left">
<font size="5"><b>Page of Ex113 印出雲端全部文章</b></font>
</p><HR>
<%
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB14";

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

  request.setCharacterEncoding("big5");
  String sql="SELECT * FROM articleINFO" ;

  if (stmt.execute(sql))   {
     ResultSet rs = stmt.getResultSet();
     while (rs.next()) {
        String indexStr= rs.getString("編號");
        String timeStr= rs.getString("時間");
        String nameStr= rs.getString("名稱");
        String emailStr= rs.getString("信箱");
        String articalStr= rs.getString("文章");

        out.print("時間：" + timeStr + "<BR>");
        out.print("名稱：" + nameStr + "<BR>");
        out.print("信箱：" + emailStr + "<BR>");
        out.print("文章<BR>" + articalStr + "<BR>");

        out.print("<FORM METHOD=post  ACTION=Ex113inputOpinion.jsp>");
        out.print("<INPUT TYPE=radio  NAME=postIndex " +
                          "VALUE=" + indexStr + ">選擇鈕點取");
        out.print("<INPUT TYPE=submit VALUE=\"寫入回應\">");
        out.print("<HR>");
 
        }
  }
  stmt.close();
  con.close();
%>
</body>
</html>