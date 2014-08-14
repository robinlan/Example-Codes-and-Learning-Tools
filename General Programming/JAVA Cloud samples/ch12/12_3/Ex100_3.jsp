<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*, java.util.Date" %>
<html>
<head><title>Ex100_3</title></head><body>
<p align="center">
<font size="5"><b>Sub Page of Ex100_3 訊息寫入資料庫</b></font>
</p>
<%
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB12";

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

  request.setCharacterEncoding("big5");
  String dataStr = request.getParameter("data");

  Date T = new Date();
  String timeStr= T.toLocaleString();

  int year = (T.getYear() + 1900);
  int month = T.getMonth() + 1;
  int date = T.getDate();
  int hours = T.getHours();
  int minutes = T.getMinutes();
  int seconds = T.getSeconds();

  String timeKey= String.format("%02d:%02d:%02d:%02d:%02d:%02d", year, month, date, hours, minutes, seconds);
                         
  String sql="INSERT INTO instantNews(時間索引, 時間, 訊息)" +
                "VALUES ('" + timeKey + "','" + timeStr + "','" + dataStr  + "')" ;

  session = request.getSession();
  if(session.getAttribute("ex100") == "true") {
       out.print("本網頁為合法認證網頁" + "<br>");
       out.print(timeStr);
       stmt.executeUpdate(sql);
       out.print("成功完成訊息輸入");
  }
  else 
       out.print("本網頁為非法認證網頁無法執行" + "<br>");

  stmt.close();
  con.close();
%>
</body>
</html>