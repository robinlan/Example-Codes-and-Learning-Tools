<%@ page contentType="text/html; charset=big5" %>
<html>
<head><title>Ex22</title></head>
<body> 
<% 
 int bufSize;

 bufSize= response.getBufferSize();
 out.print("本例預設BufferSize為: " + bufSize + "bytes<br>");

 bufSize= 16*1024;
 response.setBufferSize(bufSize);
 bufSize= response.getBufferSize();
 out.print("新設定BufferSize為: " + bufSize + "bytes<p>");

 response.flushBuffer();
 boolean b= response.isCommitted();
 out.print("本例Buffer是否已執行flush: " + b);
%>
</body>
</html>