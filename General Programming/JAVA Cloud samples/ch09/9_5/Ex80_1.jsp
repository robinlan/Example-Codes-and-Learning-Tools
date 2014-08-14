<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*" %>
<html>
<head><title>Ex80_1</title></head><body>
<p align="center">
<font size="5"><b>Sub Page of Ex80_1</b></font>
</p><p align="left">
<B>修改下列資料(鍵入新資料覆蓋舊資料)</B></p>
<%
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB09";

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

  request.setCharacterEncoding("big5");
  String Name = request.getParameter("name");
  String Pwd = request.getParameter("pwd");

 String sql="SELECT *  FROM UserList WHERE 使用者帳號='" +
                    Name + "'AND 使用者密碼='" + Pwd + "';";

  ResultSet rs= stmt.executeQuery(sql);
  ResultSetMetaData rsmd = rs.getMetaData();
  int colCount = rsmd.getColumnCount();

  rs.next();
  out.print("<FORM ACTION=Ex80_2.jsp " +
                  "METHOD=post>");
  out.print("身分證字號：<INPUT TYPE=text NAME= number_1 " +
                             "VALUE=" + rs.getString("身分證字號") + ">(不宜修改)<BR>");
  out.print("使用者帳號：<INPUT TYPE=text NAME= name_1 " +
                             "VALUE=" + Name + "><BR>");
  out.print("使用者密碼：<INPUT TYPE=text NAME= pwd_1 " +
                             "VALUE=" + Pwd + "><BR>");
  out.print("使用者地址：<INPUT TYPE=text NAME= address_1 " +
                             "VALUE=" + rs.getString("使用者地址") + "> <BR><BR>");
  out.print("<INPUT TYPE=submit VALUE=\"遞送\">");
  out.print("<INPUT TYPE=reset VALUE=\"取消\">");

  stmt.close();
  con.close();
%>
</body>
</html>