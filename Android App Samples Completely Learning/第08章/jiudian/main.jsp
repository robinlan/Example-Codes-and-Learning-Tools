<%@ page contentType="text/html;charset=big5"
    import="java.util.*,wyf.wyy.MyConverter,wyf.wyy.DB,javax.servlet.*,javax.servlet.http.*"%>
<%
    String param1=request.getParameter("params1");
    String uname=MyConverter.unescape(param1);
    Vector<String[]> v = DB.getGroup();
    String msg="";
    for(String[]s:v)
    {
       msg+=s[0]+"|"+s[2]+"|";
    }
    out.println(MyConverter.escape(msg));
    //System.out.println(MyConverter.escape(msg));
%>