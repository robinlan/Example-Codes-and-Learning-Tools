<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*, java.util.Date" %>
<html>
<head><title>Ex113</title></head><body>
<p align="center">
<font size="5"><b>Page of Ex113 回應意見寫入資料庫</b></font>
</p>
<%
  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB14";

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

  request.setCharacterEncoding("big5");
  String indexStr= request.getParameter("respIndex");
  String nameStr = request.getParameter("respName");
  String emailStr = request.getParameter("respEmail");
  String dataStr = request.getParameter("respData");

  if(indexStr=="" || nameStr=="" || emailStr=="" || dataStr=="")  {
     out.print("資料填寫未完成");
     %><br>
     <a href= "Ex113printArticle.jsp" target= "_top">按此回首頁</a>
     <%
  } 
  else {
     Date respDate= new Date();
     String dateStr= respDate.toLocaleString();

     String sql="INSERT INTO ResponseInfo(原文章編號, 回應時間," +
                       "回應者名稱, 回應者信箱, 回應者意見) VALUES (" +
                        indexStr + ",'" + dateStr + "','" + nameStr + "','" +
                        emailStr + "','" + dataStr + "')" ;
                      
    stmt.executeUpdate(sql);
    out.print("成功完成回應意見輸入資料庫");

     stmt.close();
     con.close();
  }
   %>
</body>
</html>