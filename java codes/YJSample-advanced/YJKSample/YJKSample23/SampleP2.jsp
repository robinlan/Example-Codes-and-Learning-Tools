<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>

<%!
   HttpSession hs;
   Integer cn;
   Date dt;
   String str1, str2;
%>
<%
   // 取得session
   hs = request.getSession(true);
   cn = (Integer) hs.getAttribute("count");
   dt = (Date) hs.getAttribute("date");

   // 設定次數
   if(cn == null){
   cn = new Integer(1);
      dt = new Date();
      str1 = "這是您的初次造訪。";
      str2 = "";
   }
   else{
      cn = new Integer(cn.intValue() + 1);
      dt = new Date();
      str1 = "這是您的第" + cn + "次造訪。";
      str2 = "(上次是在：" + dt + ")";
   }

   // 設定Session
   hs.setAttribute("count", cn);
   hs.setAttribute("date", dt);
%>
<html>
<head>
<title>範例</title>
</head>
<body>
<center>

<h2>歡迎</h2>
<%= str1 %><br/>
<%= str2 %><br/>
請選擇一件商品。<br/>
<a href="car1.html">汽車</a><br/>
<a href="car2.html">卡車</a><br/>
<a href="car3.html">戰車</a><br/>
</center>
</body>
</html>