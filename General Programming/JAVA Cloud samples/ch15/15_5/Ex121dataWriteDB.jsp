<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*, java.util.Date" %>
<html>
<head><title>dataWriteDB</title></head><body>
<%
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB15";

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

  request.setCharacterEncoding("big5");
  String chatStr = request.getParameter("chatData");

  session= request.getSession();
  String nameStr= session.getAttribute("loginName").toString();

  if(chatStr=="")  {
     out.print("資料填寫未完成");
     %><br>
     <a href= "01chatPage.jsp" target= "_top">按此重新輸入</a>
     <%
  } 
  else  {
     String sql= "INSERT INTO chatINFO(名稱, 訊息)" +
                      "VALUES('" + nameStr + "','" + chatStr + "')";
                      
    stmt.executeUpdate(sql);
  }
   stmt.close();
   con.close();

  out.print("<FORM ACTION=Ex121dataWriteDB.jsp " +
                  "METHOD=post>");
  out.print("<TEXTAREA NAME= chatData  ROWS=3 COLS=45></TEXTAREA>");
  out.print("<INPUT TYPE=submit VALUE=\"輸入訊息\">");
 %>
</body>
</html>