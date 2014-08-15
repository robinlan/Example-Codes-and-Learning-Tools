<%@ page contentType="text/html;charset=big5"
    import="java.util.*,wyf.wyy.*"
%>
 <html>
  <head>
   <title>添加資源</title>
   <link href="css/generalstyle.css" type="text/css" rel="stylesheet">
   <script language="JavaScript">
    function check3()
    {
       if(document.addRes.rgid.value=="")
       {
         alert("編號為空！！！");
         addRes.rgid.focus();
         return false;
       }
       if(document.addRes.rlevel.value=="")
       {
         alert("規格為空");
         addRes.rlevel.focus();
         return false;
       }
       if(document.addRes.rmoney.value=="")
       {
         alert("價格為空！！！");
         addRes.rmoney.focus();
         return false;
       }
       if(document.addRes.rdetail.value=="")
       {
         alert("描述為空！！！");
         addRes.rdetail.focus();
         return false;
       }
       document.addRes.submit();
    }
   </script>
  </head>
 <body>
   <%@ include file="adminRestop.jsp" %>
   <center>
    <font color="red" size="4">請正確填寫資源的信息</font>
   </center><br>
   <table align="center" border="0" width="60%">
    <form name="addRes" action="ListServlet" method="post">
     <tr bgcolor="b9fbfa">
      <td align="right" width="20%">編號:</td>
      <td><input type="text" name="rgid"></td>
     </tr>
     <tr>
      <td align="right">分組:</td>
      <td>
       <select name="rgroup">
		<% 
		   for(String s[]:vgroup)
		   {
		 %>        
             <option value=<%= s[3] %>><%= s[0] %></option>
   		<%
   		   }
   		 %>
       </select>          
      </td>
     </tr>
     <tr bgcolor="b9fbfa">
      <td align="right">規格:</td>
      <td><input type="text" name="rlevel"></td>
     </tr>
     <tr>
      <td align="right">價格/時段:</td>
      <td><input type="text" name="rmoney"></td>
     </tr>
     <tr bgcolor="b9fbfa">
       <td align="right">描述:</td>
       <td>
         <textarea rows=4 cols=40 name="rdetail"></textarea>
       </td>
     </tr>
     <tr>
      <td align="right">狀態:</td>
      <td>
       <select name="rstatus">
        <option>空閒</option>
        <option>佔用</option>               
       </select>
      </td>
     </tr>
     <tr bgcolor="b9fbfa"><td></td>
      <td align="left">
        <input type="hidden" name="action" value="addRes">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="button" value="保存" onClick="check3()">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <input type="reset" value="重置">
       </td>
     </tr>
    </form>
   </table>
 </body>
</html>
