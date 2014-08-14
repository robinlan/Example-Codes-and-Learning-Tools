<%@ page contentType="text/html; charset=UTF-8" %>
<jsp:useBean id="cb" class="mybeans.CarBean" scope="request"/>

<html>
<head>
<title>範例</title>
</head>
<body>
<center>
<h2>抱歉</h2>
您無法購買
<jsp:getProperty name="cb" property="cardata"/>
。<br/>
</center>
</body>
</html>