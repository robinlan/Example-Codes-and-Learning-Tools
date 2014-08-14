<%@ page contentType="text/html;charset=big5" %>
<html>
<head><title>Ex102</title></head><body>
<p align="center">
<font size="5"><b>Page of Ex102 跑馬燈設計</b></font>
</p>
<%
  String dataStr = "我的第一個跑馬燈 My first Marquee";
 %>
<b><font color="#FF0000"><marquee scrolldelay="120" loop="5" bgcolor="#00FFFF"><%= dataStr%>～</marquee></font></b>

</body>
</html>