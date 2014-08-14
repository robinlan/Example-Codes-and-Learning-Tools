<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String tmp = request.getParameter("cars");
    String carname = new String(tmp.getBytes("ISO-8859-1"), "UTF-8");
%>

<html>
<head>
<title><%= carname %></title>
</head>
<body>
<center>
<h2><%= carname %></h2>
感謝您購買本公司的
<%= carname %>
。<br/>
</center>
</body>
</html>