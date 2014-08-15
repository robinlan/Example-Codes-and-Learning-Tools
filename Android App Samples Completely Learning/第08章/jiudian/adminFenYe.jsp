<%@ page contentType="text/html;charset=big5"%>
<%
 Vector<String[]> v=DB.getPageContent(currPage,span,group);
 int color=0;//改變每行顏色
%>
<center>
  <font color="black" size="5.5"><%= gName %>
</center>
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
  	for(String[] s:v)
	{
   %>
  <tr bgcolor=<%= color%2==0?"f7fbb9":"ffeeee" %>>
	<td align="center">
	   <%=s[0]%>
	</td>
	<td align="center">
	  <%= s[6] %>
	</td>
	<td align="center">
	   <%=s[1]%>
	</td>
	<td align="center">
	  <font color="red" size="3">
	  <%=s[2]%>￥
	  </font>
	</td>
	 <td align="center">
	   <%=s[3]%>
	 </td>
	 <td align="center">
	  <%= s[4] %>
	 </td>
	 <td align="center">
	  <a href=ListServlet?action=editRes&&rid=<%= s[5] %>>修改/刪除</a>
	 </td>
	</tr>
	 <%
	    color++;
	   }
	  %>
</table>

<table width="80%" align="center" border="0">
 <tr>
  <td width="33%">
   <% 
    if(currPage>1)
    {
   %>
   <a href=adminResource.jsp?cp=<%= currPage-1 %>><<上一頁</a>
   <%
    }
   %>&nbsp;
  </td>
 <form action=adminResource.jsp method="post">
  <td align="center"><br>
   <select name="cp" onchange="this.form.submit();">
    <%
     for(int i=1;i<=totalPage;i++)
     {
       String s="";
       if(i==currPage)
     {
       s="selected";
     }
    %>
     <option value="<%= i %>" <%= s %>>第<%= i %>頁</option>
	 <%
	  }
	 %>
   </select>
 </form>
   </td>

   <td align="right" width="33%">
     <% 
     if(currPage<totalPage)
     {
     %>
    <a href=adminResource.jsp?cp=<%= currPage+1 %>>下一頁>></a>
     <%
      }
     %>
  </td>
 </tr>
</table>
