<%@ page contentType="text/html; charset=big5" %>
<html>
<head><title>Ex15</title></head>
<body> 
<% 
 int bufSize, remSize;

 bufSize= out.getBufferSize();
 out.print("本例預設BufferSize為: " + bufSize + "bytes<br>");

 remSize= out.getRemaining();
 out.print("目前BufferSize尚餘: " + remSize + "bytes<p>");

 out.flush();
 remSize= out.getRemaining();
 out.print("經過flush目前BufferSize尚餘: " + remSize + "bytes<p>");

 boolean b= out.isAutoFlush();
 out.print("本例Buffer是否可執行flush: " + b);
%>
</body>
</html>