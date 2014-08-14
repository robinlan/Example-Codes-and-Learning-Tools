<%@ page contentType="text/html; charset=Big5" %>
<%@ page import="java.util.*" %>
<jsp:useBean id="cb" class="mybeans.CarDBBean" scope="request"/>
<%!
   ArrayList colname;
   ArrayList data;
%>
<%
   colname = cb.getColname();
   data = cb.getData();
%>

<html>
<head>
<title>範例</title>
</head>
<body>
<center>
<h2>歡迎光臨</h2>
<hr/>
請選擇一件商品。<br/>
<br/>
<table border="1">
<tr bgcolor="#E0C76F">
<%
   for(int column=0; column<colname.size(); column++){
%>
<th>
<%= (String) colname.get(column) %>
</th>
<%
   }
%>
</tr>

<%
   for(int row=0; row<data.size(); row++){
%>
<tr>
<%
      ArrayList rowdata = (ArrayList) (data.get(row));
      for(int column=0; column<rowdata.size(); column++){
%>
<td>
<%= rowdata.get(column) %>
</td>
<%
      }
%>
</tr>
<%
   }
%>
</table>
</center>
</body>
</html>