<%@ page contentType="text/html;charset=big5"
    import="java.util.*,wyf.wyy.*"
%>
<html>
  <head>
   <title>分組管理</title>
   <script language="JavaScript">
    function check()
    {
      if(document.groInfo.gNameAfter.value=="")
      {
         alert("請填寫分組名！！！");
         groInfo.gNameAfter.focus();
         return false;
      }
      if(document.groInfo.gImg.value=="")
      {
         alert("請填寫圖片URL！！！");
         groInfo.gImg.focus();
         return false;
      }
      if(document.groInfo.gDetail.value=="")
      {
         alert("請填寫描述信息！！！");
         groInfo.gDetail.focus();
         return false;
      }
      if(document.groInfo.gOrderDet.value=="")
      {
         alert("請填寫預訂的一些規則！！！");
         groInfo.gOrderDet.focus();
         return false;
      }
      document.groInfo.action.value="changeGroup";
      document.groInfo.submit();
    }    
   </script>
  </head>
 <body>
   <%@ include file="admintop.jsp" %><br>   
    <%
      Vector<String> ginfo = 
      	 (Vector<String>)request.getAttribute("ginfo");
 	%>
   <center>
    <font color="black" size="5.5"><%= ginfo.get(1) %>信息
   </center>
   <table align="center" border="0" width="60%">
    <form name="groInfo" action="ListServlet" method="post">
     <tr bgcolor="ffeeee">
      <td align="right" width="20%">組名:</td>
      <td><input type="text" name="gNameAfter" value=<%= ginfo.get(1) %>></td>
     </tr>
     
     <tr>
      <td align="right">圖片URL:</td>
      <td><input type="text" name="gImg" value=<%= ginfo.get(3) %>></td>
     </tr>
     
     <tr bgcolor="ffeeee">
      <td align="right">描述:</td>
      <td>
        <textarea rows=6 cols=50 name="gDetail"><%= ginfo.get(4) %></textarea>
      </td>
     </tr>
     <tr>
      <td align="right">規則:</td>
      <td>
        <textarea rows=6 cols=50 name="gOrderDet"><%= ginfo.get(2) %></textarea>
      </td>
     </tr>
     <tr bgcolor="ffeeee"><td></td>
      <td align="left">
       <input type="hidden" name="action" value="deleteGroup">
       <input type="hidden" name="gId" value=<%= ginfo.get(0) %> >
       <input type="hidden" name="gNameBefor" value=<%= ginfo.get(1) %>>       
       <input type="button" value="保存" onClick="check()">
       &nbsp&nbsp&nbsp&nbsp
       <input type="submit" value="刪除">
       <font color="red" size="2">*刪除分組，將會刪除分組下所有的資源
      </td>
     </tr>
    </form>
   </table><br>
 </body>
</html>
