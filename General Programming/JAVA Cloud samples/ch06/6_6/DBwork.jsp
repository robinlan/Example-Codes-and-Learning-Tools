<%@ page contentType="text/html;charset=big5" %>
<%@ page import= "java.sql.*" %>
<%@ page import= "java.io.*" %>
<html>
<head><title>DBwork</title></head><body>
<p align="left">
<font size="5"><b>Sub Page of DBwork</b></font>
</p>
<%
  request.setCharacterEncoding("big5");
  String DBname = request.getParameter("DBname");
  String SQLcmd = request.getParameter("SQLcmd");

  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:" + DBname;
  StringBuffer sb = new StringBuffer();

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();
 
   if (stmt.execute(SQLcmd))
      {
        ResultSet rs = stmt.getResultSet();
        ResultSetMetaData md = rs.getMetaData();
        int colCount = md.getColumnCount();
        sb.append("<TABLE CELLSPACING=10><TR>");
        for (int i = 1; i <= colCount; i++)
              sb.append("<TH>" + md.getColumnLabel(i));
         while (rs.next())
             {
               sb.append("<TR>");
               for (int i = 1; i <= colCount; i++)
                   {
                     sb.append("<TD>");
                     Object obj = rs.getObject(i);
                     if (obj != null)
                         sb.append(obj.toString());
                     else
                         sb.append("&nbsp;");
                    }
                }
              sb.append("</TABLE>\n");
            }
           else
              sb.append("<B>°õ¦æ¦¨¥\</B>") ;

  String result= sb.toString();
  out.print(result);

  stmt.close();
  con.close();
%>
</body>
</html>