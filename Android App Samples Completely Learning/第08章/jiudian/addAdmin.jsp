<%@ page contentType="text/html;charset=big5"
    import="java.util.*,wyf.wyy.*"
%>
 <html>
  <head>
   <title>添加管理員</title>
   <link href="css/generalstyle.css" type="text/css" rel="stylesheet">
   <script language="javascript">
    function check()
    { 
      if(document.addAdmin.adname.value=="")
      {
         alert("請填寫登錄名！！！");
         addAdmin.adname.focus();
         return false;
      }
      if(document.addAdmin.adpwd.value=="")
      {
         alert("請填寫登陸密碼！！！");
         addAdmin.adpwd.focus();
         return false;
      }
      if(document.addAdmin.repwd.value=="")
      {
         alert("請重複密碼！！！");
         addAdmin.repwd.focus();
         return false;
      }
      if(document.addAdmin.adpwd.value!=document.addAdmin.repwd.value)
       {
         alert("兩次密碼輸入不一樣，請檢查輸入！！！");
         addAdmin.adpwd.value="";
         addAdmin.repwd.value="";
         addAdmin.adpwd.focus();
         return false;
       }
       document.addAdmin.submit();
    }
   </script>
  </head>
 <body>
    <%@ include file="admintop.jsp" %>
    <hr width="100%"></hr><br>
    <table align="center" border="0" width="40%">
     <form name="addAdmin" action="ListServlet" method="post">
      <tr bgcolor="eeffee">
       <td align="right">登錄名</td>
       <td><input type="text" name="adname" size="20"></td>
      </tr>
      <tr bgcolor="ffeeff">
       <td align="right">登陸密碼</td>
       <td><input type="password" name="adpwd" size="20"></td>
      </tr>
      <tr bgcolor="eeffee">
       <td align="right">重複密碼</td>
       <td><input type="password" name="repwd" size="20"></td>
      </tr>
      <tr bgcolor="ffeeff">
       <td></td>
       <td>
        <input type="hidden" name="action" value="addAdmin">
        <input type="button" value="添加" onClick="check()">
       </td>
      </tr>
     </form>
    </table>    
 </body>
</html>
