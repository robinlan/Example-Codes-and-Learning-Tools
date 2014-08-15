<%@ page contentType="text/html;charset=big5"
    import="java.util.*,wyf.wyy.*"
%>
 <html>
  <head>
   <title>資源管理</title>
   <link href="css/generalstyle.css" type="text/css" rel="stylesheet">
   <script language="JavaScript">
    function check2()
    {
       if(document.resInfo.rgidAfter.value=="")
       {
         alert("編號為空！！！");
         resInfo.rgidAfter.focus();
         return false;
       }
       if(document.resInfo.rlevel.value=="")
       {
         alert("規格為空");
         resInfo.rlevel.focus();
         return false;
       }
       if(document.resInfo.rmoney.value=="")
       {
         alert("價格為空！！！");
         resInfo.rmoney.focus();
         return false;
       }
       if(document.resInfo.rdetail.value=="")
       {
         alert("描述為空！！！");
         resInfo.rdetail.focus();
         return false;
       }
       document.resInfo.action.value="changeRes";
       document.resInfo.submit();
    }
   </script>
  </head>
 <body>
   <%@ include file="adminRestop.jsp" %>	
    <%
      Vector<String[]> rinfo = 
      	 (Vector<String[]>)request.getAttribute("rinfo");
 	  String []s = rinfo.get(0);
 	%>
   <table align="center" border="0" width="60%">
    <form name="resInfo" action="ListServlet" method="post">
     <tr bgcolor="ffeeee">
      <td align="right" width="20%">編號:</td>
      <td><input type="text" name="rgidAfter" value=<%= s[0] %>></td>
     </tr>
     <tr>
      <td align="right">分組:</td>
      <td>
       <select name="rgroup">
        <%
         for(String ss[]:vgroup)
         {
           if(ss[0].equals(s[7]))
           {
         %>           
             <option selected value=<%= ss[3] %>><%= ss[0] %></option>
         <%
           }
           else
           {
           %>
             <option value=<%= ss[3] %>><%= ss[0] %></option>
           <%
           }
          }
          %>
       </select>          
      </td>
     </tr>
     
     <tr bgcolor="ffeeee">
      <td align="right">規格:</td>
      <td><input type="text" name="rlevel" value=<%= s[1] %>></td>
     </tr>
     <tr>
      <td align="right">價格/時段:</td>
      <td><input type="text" name="rmoney" value=<%= s[2] %>></td>
     </tr>
     <tr bgcolor="ffeeee">
       <td align="right">描述:</td>
       <td>
         <textarea rows=4 cols=40 name="rdetail"><%= s[3] %></textarea>
       </td>
     </tr>
     <tr>
      <td align="right">狀態:</td>
      <td>
       <select name="rstatus">
        <option>空閒</option>
        <%
        if(s[4].equals("佔用"))
        {
        %>
        <option selected>佔用</option>
        <%
        }
        else
        {
        %>
        <option>佔用</option>
        <% 
        }
         %>                
       </select>
      </td>
     </tr>
     <tr bgcolor="ffeeee"><td></td>
      <td align="left">
       <input type="hidden" name="action" value="deleteRes">
       <input type="hidden" name="rid" value=<%= s[6] %>>
       <input type="hidden" name="rgidBefor" value=<%= s[0] %>>       
       <input type="button" value="保存" onClick="check2()">
       &nbsp&nbsp&nbsp&nbsp
       <input type="submit" value="刪除">
       <font color="red" size="2">*刪除資源之前請先確認該資源沒有訂單且處於空閒狀態
      </td>
     </tr>
    </form>
   </table><br>
 </body>
</html>
