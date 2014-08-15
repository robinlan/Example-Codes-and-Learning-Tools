<%@ page
 contentType="text/html;charset=big5"
 import="java.io.*,javax.servlet.*,wyf.wyy.MyConverter,wyf.wyy.DB,wyf.wyy.Order_DB,java.util.*"
 %>
 <% 
    String param1=request.getParameter("param1").trim();
     String uname=MyConverter.unescape(param1);
    System.out.println("哈哈哈 用戶訂單已經傳回客戶端！");
      Vector<String[]> v=new Vector<String[]>();
      String sqla="select oid,otime,ostatus from olist where oname='"+uname+"'";
      v=Order_DB.getOrderListThree(sqla);
      StringBuffer sb=new StringBuffer();
      for(String[] s:v)
      {
        sb.append("       ");
        sb.append(s[0]);
        sb.append("|");
        sb.append("         ");
        sb.append(s[1]);
        sb.append("|");
        sb.append("          ");
        sb.append(s[2]);
        sb.append("|");
      }
      String s=sb.toString();
      out.println(MyConverter.escape(s));
    //System.out.println(MyConverter.escape(s));
  %>
