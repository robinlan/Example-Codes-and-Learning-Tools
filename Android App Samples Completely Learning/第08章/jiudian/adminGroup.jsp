<%@ page contentType="text/html;charset=big5"
    import="java.util.*,wyf.wyy.*"%>
 <html>
  <head>
   <title>分組管理</title>
   <link href="css/generalstyle.css" type="text/css" rel="stylesheet">
  </head>
 <body>
    <%@ include file="admintop.jsp" %>
    
    <hr width="100%"></hr><br>
    <table align="center" width="80%">
    <tr>
    <td align="right" >
    <a href=addGroup.jsp>添加分組>></a>
    </td>
    </tr>
    </table>
    <% Vector<String []> vgroup = DB.getGroup();
       int color=0;//改變每行顏色 
     %>
    <table align="center" width="90%" cellspacing="1" bgcolor="black">
     <tr bgcolor="white">
       <th>名稱</th>  <th>圖片URL</th>  <th>描述</th>
       <th>規則</th>  <th>修改/刪除</th>    
     </tr>     
      <%for(String []s:vgroup){%>
      <tr bgcolor=<%= color%2==0?"eeffee":"ffeeee" %>>
        <td align="center"><%= s[0] %></td>
        <td align="center"><%= s[1] %></td>
        <td align="center"><%= s[2] %></td>
        <td align="center"><%= s[4] %></td> 
        <td align="center">
         <a href=ListServlet?action=editGroup&&gId=<%= s[3] %>>修改/刪除</a>
        </td>
       </tr>
     <%color++;}
      %>
    </table><br>
    
 </body>
</html>
