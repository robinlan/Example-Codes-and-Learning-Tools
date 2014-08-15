<%@ page contentType="text/html;charset=big5"
    import="java.util.*,wyf.wyy.*"
%>
<html>
  <head>
   <title>分組管理</title>
   <script language="JavaScript">
    function check()
    {
      if(document.addGroup.gName.value=="")
      {
         alert("請填寫分組名！！！");
         addGroup.gName.focus();
         return false;
      }
      if(document.addGroup.gImg.value=="")
      {
         alert("請填寫圖片URL！！！");
         addGroup.gImg.focus();
         return false;
      }
      if(document.addGroup.gDetail.value=="")
      {
         alert("請填寫描述信息！！！");
         addGroup.gDetail.focus();
         return false;
      }
      if(document.addGroup.gOrderDet.value=="")
      {
         alert("請填寫預訂的一些規則！！！");
         addGroup.gOrderDet.focus();
         return false;
      }
      document.addGroup.submit();
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
    <font color="BLUE" size="5.5">請認真正確填寫分組信息
   </center>
   <table align="center" border="0" width="60%">
    <form name="addGroup" action="ListServlet" method="post">
     <tr bgcolor="eedbf8">
      <td align="right" width="20%">組名:</td>
      <td><input type="text" name="gName"></td>
     </tr>
     
     <tr>
      <td align="right">圖片URL:</td>
      <td><input type="text" name="gImg"></td>
     </tr>
     
     <tr bgcolor="eedbf8">
      <td align="right">描述:</td>
      <td>
        <textarea rows=6 cols=50 name="gDetail"></textarea>
      </td>
     </tr>
     <tr>
      <td align="right">規則:</td>
      <td>
        <textarea rows=6 cols=50 name="gOrderDet"></textarea>
      </td>
     </tr>
     <tr bgcolor="eedbf8"><td></td>
      <td align="left">
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       <input type="hidden" name="action" value="addGroup"> 
       <input type="button" value="添加" onClick="check()">
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
       <input type="reset" value="重置">
      </td>
     </tr>
    </form>
   </table><br>
 </body>
</html>
