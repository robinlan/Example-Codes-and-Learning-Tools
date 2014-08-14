<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*, java.util.Date" %>
<html>
<head><title>Ex113</title></head><body>
<p align="center">
<font size="5"><b>Page of Ex113 鍵入回應意見</b></font>
</p>
<%
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB14";

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

  request.setCharacterEncoding("big5");
  String indexStr = request.getParameter("postIndex");

  String sql="SELECT * FROM articleINFO WHERE 編號= " +
                      indexStr + ";" ;

  if (stmt.execute(sql))   {
      ResultSet rs = stmt.getResultSet();
      while (rs.next()) {
        String timeStr= rs.getString("時間");
        String nameStr= rs.getString("名稱");
        String emailStr= rs.getString("信箱");
        String articalStr= rs.getString("文章");

        out.print("編號：" + indexStr + "<BR>");
        out.print("時間：" + timeStr + "<BR>");
        out.print("名稱：" + nameStr + "<BR>");
        out.print("信箱：" + emailStr + "<BR>");
        out.print("文章<BR>" + articalStr + "<BR><HR>");
      }
      stmt.close();
      con.close();
  }
  out.print("<FORM ACTION=Ex113writeDatabase.jsp " +
                  "METHOD=post>");
  out.print("原文章編號：<INPUT TYPE=text NAME=respIndex " +
                  "VALUE=" + indexStr + "><BR>");
  out.print("回應者名稱：<INPUT TYPE=text NAME=respName " +
                  "SIZE=" + 10 + "><BR>");
  out.print("回應者信箱：<INPUT TYPE=text NAME=respEmail " +
                  "SIZE=" + 20 + "></p><p>");
  out.print("回應者意見：(50宇以內)<BR>" + 
             "<TEXTAREA NAME=respData  ROWS=3 COLS=45></TEXTAREA>");
     %>
</p><p>
<INPUT TYPE="submit" VALUE="遞送">
<INPUT TYPE="reset" VALUE="取消"> 
 
</body>
</html>