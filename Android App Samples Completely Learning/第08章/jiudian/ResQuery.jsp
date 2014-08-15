<%@ page contentType="text/html;charset=big5"
    import="java.util.*,wyf.wyy.*"
%>
 <html>
  <head>
   <title>資源管理</title>
   <link href="css/generalstyle.css" type="text/css" rel="stylesheet">
  </head>
 <body>
   <%@ include file="adminRestop.jsp" %>
    <%
      Vector<String[]> res = 
      	 (Vector<String[]>)request.getAttribute("list");
     if(res==null||res.size()==0)
     {
       out.println("<center>");
	   out.println("<font color=red size=5>沒有此資源</font>");
	   out.println("<br><br><a href="
	     +"ListServlet?action=adminList&&gId=room>返回</a></center>");
     }
     else
     {
    %>
    <table align="center" width="80%" cellspacing="1" bgcolor="black">
     <tr width="60%" height="30" bgcolor="white">
		<th>編號</th>
		<th>組別</th>
		<th>規格</th>
		<th>價格?/時段</th>
		<th>描述</th>
		<th>狀態</th>
		<th>修改/刪除</th>
	 </tr>
	 <% 
	    int color=0;//改變每行顏色
	 	for(String []s:res)
	    {
	  %>
	  <tr bgcolor=<%= color%2==0?"eeffee":"ffeeee" %>>
	   <td align="center"><%= s[0] %></td>
	   <td align="center"><%= s[7] %></td>
	   <td align="center"><%= s[1] %></td>
	   <td align="center"><%= s[2] %>￥</td>
	   <td align="center"><%= s[3] %></td>
	   <td align="center"><%= s[4] %></td>
	   <td><a href=ListServlet?action=edit&&rid=<%= s[6] %>>修改/刪除</a></td>
	  </tr>
	  <%
	       color++;
	     }
	  }
	   %>
 </body>
</html>
