<%@ page contentType="text/html; charset=big5" %>
<html>
<head><title>Ex14</title></head>
<body> 
<% 
 int bufSize, remSize;

 bufSize= out.getBufferSize();
 out.print("本例預設BufferSize為: " + bufSize + "bytes<br>");

 remSize= out.getRemaining();
 out.print("目前BufferSize尚餘: " + remSize + "bytes<p>");

 out.clear();
 remSize= out.getRemaining();
 out.print("經過clear目前BufferSize尚餘: " + remSize + "bytes<p>");
%>
</body>
</html>