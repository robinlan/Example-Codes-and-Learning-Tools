<%@ page
 contentType="text/html;charset=big5"
 import="java.io.*,javax.servlet.*,wyf.wyy.MyConverter,wyf.wyy.DB,java.util.*,wyf.wyy.Order_DB"
 %>
<%
  String params1=request.getParameter("params1");
  String param1=MyConverter.unescape(params1);
  Vector<String[]> v = DB.getResource1();
  String msg="";
  for(String[]s:v)
  {
      msg+=s[0]+"|"+s[1]+"|"+s[2]+"|"+s[3]+"|";
  }
  out.println(MyConverter.escape(msg));
%>