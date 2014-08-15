<%@ page
 contentType="text/html;charset=big5"
 import="java.io.*,javax.servlet.*,wyf.wyy.MyConverter,wyf.wyy.DB,wyf.wyy.Order_DB,java.util.*"
 %>
<%
  System.out.println("setOlist========");
  String param1=request.getParameter("param1").trim();
  String param2=request.getParameter("param2").trim();
  String param3=request.getParameter("param3").trim();
  String param4=request.getParameter("param4").trim();
  String uname=MyConverter.unescape(param1); 
  String roomid=MyConverter.unescape(param2); 
  String starttime=MyConverter.unescape(param3); 
  String endtime=MyConverter.unescape(param4);
  Vector<String[]> v=new Vector<String[]>();
  String[] s=new String[3];
  if(!Order_DB.isOrdered(roomid))
  {
      s[0]=new String(roomid.getBytes("iso8859-1"),"big5");
    if(s[0].length()==2)
    {
           s[1]=new String(starttime.getBytes("iso8859-1"),"big5");
           s[2]=new String(endtime.getBytes("iso8859-1"),"big5");
    }
    else
    {
        s[1]=new String((starttime+" 14:00").getBytes("iso8859-1"),"big5");
        s[2]=new String((endtime+" 12:00").getBytes("iso8859-1"),"big5");
    
    }


    v.add(s);
    int i=0;
    i=Order_DB.addOrder(uname,v);
    if(i==-1)
    {
      out.println(MyConverter.escape("訂單提交失敗！"));
      //System.out.println(MyConverter.escape("訂單提交失敗！"));
    }
    else
    {
      String oid="";
      String sql="select oid from oinfo where rgid="+roomid;
      oid=DB.getInfo(sql);
      Vector<String[]> vv=new Vector<String[]>();
      vv=Order_DB.getOrderDetail(oid);
      StringBuffer msg=new StringBuffer("");
      for(String[] ss:vv)
      {
        msg.append(ss[0]);
        msg.append("   ");
        msg.append(ss[1]);
        msg.append("   ");
        msg.append(ss[2]);
        msg.append("   ");
        msg.append(ss[3]);
        msg.append("   ");
        msg.append(ss[4]);
        msg.append("   |");
      }
      out.println(MyConverter.escape("預訂成功！"));
      //System.out.println(MyConverter.escape("預訂成功！"));
    }
  }
  else
  {
    out.println(MyConverter.escape("訂單還在預訂中！"));
  }
  //}
%>

