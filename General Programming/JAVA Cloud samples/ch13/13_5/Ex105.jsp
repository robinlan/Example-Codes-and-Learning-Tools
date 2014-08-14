<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*, java.util.Date" %>
<html>
<head><title>Ex105</title></head><body>
<p align="center">
<font size="5"><b>Sub Page of Ex105 寫入資料庫</b></font>
</p>
<%
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB13";

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

  request.setCharacterEncoding("big5");
  String MsgName = request.getParameter("msgName");
  String EMail = request.getParameter("eMail");
  String Data = request.getParameter("data");

  if(MsgName=="" || EMail=="" || Data=="")  {
     out.print("資料填寫未完成");
     %><br>
     <a href= "Ex105.html" target= "_top">按此回首頁</a>
     <%
  } 
  else {
     Date msgDate= new Date();
     String dateStr= msgDate.toLocaleString();

     String sql="INSERT INTO boardMessage(時間, 名稱," +
                       "信箱, 留言) VALUES ('" +
                        dateStr + "','" + MsgName + "','" +
                        EMail + "','" + Data + "')" ;
                      
    stmt.executeUpdate(sql);
    out.print("成功完成留言寫入資料庫");

     stmt.close();
     con.close();
  }
   %>
</body>
</html>