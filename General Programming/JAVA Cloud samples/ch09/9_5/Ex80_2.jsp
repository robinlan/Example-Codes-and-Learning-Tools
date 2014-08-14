<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*" %>
<html>
<head><title>Ex80_2</title></head><body>
<p align="center">
<font size="5"><b>Sub Page of Ex80_2</b></font>
</p>
<%
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB09";

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

  request.setCharacterEncoding("big5");
  String Number_2 = request.getParameter("number_1");
  String Name_2 = request.getParameter("name_1");
  String Pwd_2 = request.getParameter("pwd_1");
  String Address_2 = request.getParameter("address_1");

  String sql="UPDATE UserList SET " +
                       "身分證字號='" + Number_2 +
                       "', 使用者帳號='" + Name_2 +
                       "', 使用者密碼='" + Pwd_2 +
                       "', 使用者地址='" + Address_2 +
                       "' WHERE 身分證字號='" + Number_2 + "';" ;
                      
  if(stmt.executeUpdate(sql) == 1)
      out.print("成功修改資料!!");
  else
      out.print("修改資料失敗!!");

  stmt.close();
  con.close();
%>
</body>
</html>