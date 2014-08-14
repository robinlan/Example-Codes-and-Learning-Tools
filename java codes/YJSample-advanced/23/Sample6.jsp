<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="cb" class="mybeans.CarBean" scope="request"/>

<html>
<head>
<title>範例</title>
</head>
<body>
<center>
<h2>感謝</h2>
感謝您購買本公司的
<jsp:getProperty name="cb" property="cardata"/>
。<br/>
</center>
</body>
</html>