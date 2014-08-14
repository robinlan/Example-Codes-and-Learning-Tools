<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*, java.util.Date" %>
<html>
<head><title>loginWriteDB</title></head><body>
<%
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB15";

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

  request.setCharacterEncoding("big5");
  String nameStr = request.getParameter("loginName");

  if(nameStr=="")  {
     out.print("資料填寫未完成");
     %><br>
     <a href= "Ex120chatPage.jsp" target= "_top">按此重新輸入</a>
     <%
  } 
  else  {
     Date T= new Date();
     int year = (T.getYear() + 1900);
     int month = T.getMonth() + 1;
     int date = T.getDate();
     int hours = T.getHours();
     int minutes = T.getMinutes();
     int seconds = T.getSeconds();
  String timeKey= String.format("%02d:%02d:%02d:%02d:%02d:%02d", year, month, date, hours, minutes, seconds);
     String timeStr= T.toLocaleString();
     String addrStr= request.getRemoteAddr();

     String sql= "INSERT INTO loginINFO(時間索引, 時間, 名稱, 網址)" +
                      "VALUES('" + timeKey + "','" + timeStr + "','" + nameStr + "','" + addrStr + "')";
    stmt.executeUpdate(sql);
  }
   stmt.close();
   con.close();

  session.setAttribute("loginName", nameStr);
  out.print("<FORM ACTION=Ex120dataWriteDB.jsp " +
                  "METHOD=post>");
  out.print("<TEXTAREA NAME= chatData  ROWS=3 COLS=45></TEXTAREA>");
  out.print("<INPUT TYPE=submit VALUE=\"輸入訊息\">");
 %>
</body>
</html>