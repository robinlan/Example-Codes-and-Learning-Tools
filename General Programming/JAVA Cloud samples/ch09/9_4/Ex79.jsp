<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*" %>
<html>
<head><title>Ex79</title></head><body>
<p align="center">
<font size="5"><b>Sub Page of Ex79</b></font>
</p><p align="left">
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

  ResultSet rs = stmt.executeQuery(sql);
  boolean flag = false;
  while(rs.next()) flag = true;
  if(flag)
      out.print("帳號密碼正確,成功登入雲端!!");
  else
      out.print("帳號密碼有誤,登入雲端失敗!!");

  stmt.close();
  con.close();
%>
</body>
</html>