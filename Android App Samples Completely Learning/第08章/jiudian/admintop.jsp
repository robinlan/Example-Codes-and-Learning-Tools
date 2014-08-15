<%@ page contentType="text/html;charset=big5"%>
<link href="css/generalstyle.css" type="text/css" rel="stylesheet"><br>

<table align=center border="0" width="80%" height=20 >
  <tr align="center">
    <td height=15 colspan="5">
	  <font color="#5e82e9" size="6">夏日酒店預訂管理</font>
	</td>
  </tr>
  <tr>
   <td align="right" colspan="5">
  	<%String adname = (String)session.getAttribute("adname");
  	  if(adname!=null){
  	   out.println("管理員"+adname+"您好");
  	   }%>
    </td>
  </tr>
</table>
<table align="center" border="0" width="80%" bgcolor="#92cfeb">
  <tr>
   <td><a href="adindex.jsp">登錄</a></td>
   <td><a href=RegAndLoginServlet?action=adlogout>註銷</a></td>
   <td><a href=adminChangePwd.jsp>修改密碼</a></td>
   <td><a href=ListServlet?action=admanage>管理員管理</a></td>
   <td><a href=ListServlet?action=adminGroup>分組管理</a></td>
   <td><a href=ListServlet?action=adminList&&gId=0>資源管理</a></td>
   <td><a href=OrderServlet?action=allOrders&&condition=1>訂單管理</a></td>
  </tr>
</table>
