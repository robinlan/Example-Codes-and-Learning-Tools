<%@ page contentType="text/html;charset=big5"
    import="java.util.*"%>
 <html>
  <head>
   <title>訂單管理</title>
   <link href="css/generalstyle.css" type="text/css" rel="stylesheet">
   <script language="JavaScript">
    function check()
    {
       if(document.searchOrder.oid.value=="")
       {
         alert("請輸入訂單號查詢！！！");
         searchOrder.oid.focus();
         return false;
       }
       document.searchOrder.submit();
    }
   </script>
  </head>
 <body>
   <%@ include file="admintop.jsp" %>	
   <hr width="100%"></hr><br>
   <table align="center" border="0" width="80%">
    <tr>
       
	  <form name="searchOrder" action="OrderServlet" method="post">
	   <td align="right">訂單編號:	 
	    <input type="hidden" name="action" value="query">
	    <input type="text" name="oid">
	    <input type="button" value="查詢" onClick="check()">
	   </td>
	  </tr>
	  </form>
   </table><br>
   <table align="center" width="60%">
   <tr>
   <td align="left"><a href=OrderServlet?action=allOrders&&condition=2>已處理</a></td>
   <td align="center"><a href=OrderServlet?action=allOrders&&condition=1>所有訂單</a></td>
   <td align="right"><a href=OrderServlet?action=allOrders&&condition=3>未處理</a></td>
   </tr>
   </table><br>
	   <%Vector<String[]> list = //得到訂單列表
          (Vector<String[]>)request.getAttribute("list");
       if(list==null||list.size()==0){//列表不為空
	     out.println("<center>");
	     out.println("<font color=red size=5>沒有訂單</font>");
	     out.println("<br><br><a href="+
	       "OrderServlet?action=allOrders&&condition=1>返回</a></center>");
	    }
	    else{%>
   <table align="center" width="70%" cellspacing="1" bgcolor="black">
	 <tr width="60%" height="30" bgcolor="white">
	   <th>編號</th>   <th>下訂人</th>   <th>提交時間</th> 
	   <th>狀態</th>   <th>處理人</th>	 <th>詳情</th>
	   <th>備註</th>   <th>處理訂單</th>
	 </tr>
	   <%int color = 0;
	  	 for(int i=0;i<list.size();i++){ 
		   String[] s = list.get(i);%>
     <tr bgcolor=<%= color%2==0?"eeffee":"ffeeee" %> height="40">
	   <td align="center"><%= s[0] %></td>  <td align="center"><%= s[1] %></td>
	   <td align="center"><%= s[2] %></td>  <td align="center"><%= s[4] %></td>
	   <td align="center"><%= s[3] %></td>
	   <td align="center">
	    <a target="blank" href=OrderServlet?action=ListDetail&&oid=<%= s[0] %>>訂單詳情</a>	   
	   </td>   
	   <form action="OrderServlet" method="post">
	   <td align="center">
	      <input type="text" name="reason" size="10" value=<%= s[5] %>>
	   </td><td align="center">
	     <select name="ostatus">
	     <option selected>預訂成功</option>
	     <option>預訂失敗</option>
	     <option>預訂中</option>
	     </select>
	     <input type="hidden" name="action" value="dealOrder">
	     <input type="hidden" name="oid" value=<%= s[0] %>>
	     <input type="submit" value="提交">
	   </td>
	   </form>	  
	</tr> <%color++;}%>
   </table><%}%>
 </body>
</html>
