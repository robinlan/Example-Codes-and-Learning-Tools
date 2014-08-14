<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*" %>
<html>
<head><title>Ex78</title></head><body>
<p align="center">
<font size="5"><b>Sub Page of Ex78</b></font>
</p>
<%
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB09";

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

  request.setCharacterEncoding("big5");
  String Number = request.getParameter("number");
  String Name = request.getParameter("name");
  String Pwd = request.getParameter("pwd");
  String Address = request.getParameter("address");

  String sql="INSERT INTO UserList(身分證字號,使用者帳號," +
                "使用者密碼,使用者地址) VALUES ('" +
                Number + "','" + Name + "','" +
                Pwd + "','" + Address + "')" ;
                      
  stmt.executeUpdate(sql);
  stmt.close();
  con.close();
%>
<center>
成功完成註冊輸入
</body>
</html>