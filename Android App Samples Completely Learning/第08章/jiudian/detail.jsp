<%@ page contentType="text/html;charset=big5" 
    import="java.util.*"%>
<html>
 <head>
   <title>訂單明細</title>
    <link href="css/generalstyle.css" type="text/css" rel="stylesheet">
 </head>
 <body>
   <%Vector<String[]> ListDetail =
     (Vector<String[]>)request.getAttribute("ListDetail");
    String oid =(String)request.getAttribute("oid");%>
    <br><center>訂單<%= oid %>明細</center><br>
   <table align="center" width="70%" cellspacing="1" bgcolor="black">
	 <tr width="60%" height="30" bgcolor="white">
	   <th>編號</th>      <th>類別</th> <th>開始時間</th>  
	   <th>結束時間</th>  <th>狀態</th>
	  </tr>
	   <%int color = 0;//控制每行顏色
	  	 for(int i=0;i<ListDetail.size();i++){
		   String[] s = ListDetail.get(i);%>
     <tr bgcolor=<%= color%2==0?"eeffee":"ffeeee" %> height="40">
	   <td align="center"><%= s[0] %></td>
	   <td align="center"><%= s[4] %></td>
	   <td align="center"><%= s[1] %></td>
	   <td align="center"><%= s[2] %></td>
	   <td align="center"><%= s[3] %></td>
	</tr><%color++;}%>
   </table>
 </body>
</html>
