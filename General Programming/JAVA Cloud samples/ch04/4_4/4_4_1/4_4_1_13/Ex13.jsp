<%@ page contentType="text/html; charset=big5" %>
<html>
<head><title>Ex13</title></head>
<body> 
<% 
 int bufSize, remSize;

 bufSize= out.getBufferSize();
 out.print("本例預設BufferSize為: " + bufSize + "bytes<br>");

 remSize= out.getRemaining();
 out.print("目前BufferSize尚餘: " + remSize + "bytes<p>");
 %>
</body>
</html>