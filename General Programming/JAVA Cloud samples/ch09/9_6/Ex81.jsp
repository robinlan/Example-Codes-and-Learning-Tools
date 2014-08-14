<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*" %>
<html>
<head><title>Ex81</title></head><body>
<p align="center">
<font size="5"><b>Sub Page of Ex81</b></font>
</p>
<%
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB09";

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

  request.setCharacterEncoding("big5");
  String Name = request.getParameter("name");
  String Pwd = request.getParameter("pwd");

  String sql="DELETE FROM UserList WHERE 使用者帳號='" +
                    Name + "'AND 使用者密碼='" + Pwd + "';";
                      
  stmt.execute(sql);
  stmt.close();
  con.close();
%>
<center>
成功刪除註冊資料
</body>
</html>