<%@ page contentType="text/html; charset=UTF-8" %>
<%
    String tmp = request.getParameter("cars");
    String carname = new String(tmp.getBytes("ISO-8859-1"), "UTF-8");
%>
<html>
<head>
<title>範例</title>
</head>
<body>
<center>

<%
   if(carname.length() != 0){
%>

<h2><%= carname %></h2>
感謝您購買本公司的
<%= carname %>
。<br/>

<%
   }
   else{
%>

<h2>錯誤</h2>
請輸入商品名稱。<br/>

<%
   }
%>

</center>
</body>
</html>