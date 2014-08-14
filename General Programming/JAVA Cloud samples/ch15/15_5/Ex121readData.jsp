<%@ page contentType= "text/html;charset=big5" %>
<%@ page import= "java.sql.*, java.io.*, java.util.Date" %>
<% Date T= new Date(); %>
<html>
<head><title>readData</title></head><body>
<%
  response.addIntHeader("refresh", 5);

  String JDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
  String connectDB="jdbc:odbc:cloudDB15";
  StringBuffer sb = new StringBuffer();
  String objStr= "";
  int maxInt= 0, workInt= 0;

  Class.forName(JDriver);
  Connection con = DriverManager.getConnection(connectDB);
  Statement stmt = con.createStatement();

  request.setCharacterEncoding("big5");

  String sql1="SELECT MAX(編號) AS max_index FROM chatINFO";

  if (stmt.execute(sql1))
      {
        ResultSet rs = stmt.getResultSet();
        ResultSetMetaData md = rs.getMetaData();
        int colCount = md.getColumnCount();
        while (rs.next())
            {
              for (int i = 1; i <= colCount; i++)
                   {
                     Object obj = rs.getObject(i);
                     objStr= obj.toString();
                    }
              }
         }
  maxInt= Integer.parseInt(objStr);
  workInt= maxInt - 10;
  
  String sql2="SELECT 名稱, 訊息 FROM chatINFO " +
                    "WHERE 編號>= " + workInt + ";";

  if (stmt.execute(sql2))
      {
        ResultSet rs = stmt.getResultSet();
        ResultSetMetaData md = rs.getMetaData();
        int colCount = md.getColumnCount();
        sb.append("<TABLE CELLSPACING=1><TR>");
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
             sb.append("<B>Update Count:</B> " +
                                 stmt.getUpdateCount());

  String result= sb.toString();
  out.print(result);

  stmt.close();
  con.close();
%>
</body>
</html>