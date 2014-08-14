<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*" %>
<%@ page import= "java.io.*" %>
<html>
<head><title>Ex91visitCounter</title></head><body>
<p align="left">
<font size="5"><b>Page of Ex91visitCounter</b></font>
</p>
<%
  int numInt= 0;
  String numStr;
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB11";

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

//讀取資料庫曾儲存之拜訪人次--------------------------------------------
  String sql1= "SELECT * FROM visitCounter WHERE workPointer=0";
  if (stmt.execute(sql1))   {
      ResultSet rs = stmt.getResultSet();
      while (rs.next())
          numInt= rs.getInt("count");
  }

//將拜訪人次加1再儲存入資料庫---------------------------------------------
  if (session.isNew()) {
      numInt= numInt + 1;
      String sql2= "UPDATE visitCounter SET " +
                          "workPointer= " + 0 + "," +
                          "count= " + numInt + 
                          " WHERE workPointer=  0";
      stmt.executeUpdate(sql2);
  }

//使用圖形數字印出拜訪人次--------------------------------------
  numStr = String.valueOf(numInt);
  out.print("本網頁拜訪人次： "  +"<br>");
  for(int i = 0; i < numStr.length(); i++)  {
    %>
    <img src = "./images/<%= numStr.charAt(i) %>.gif"></img>
    <%
  }
  stmt.close();
  con.close();
%>
</body>
</html>