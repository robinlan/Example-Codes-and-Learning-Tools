<%@ page contentType="text/html;charset=big5"
    import="java.util.*,wyf.wyy.*"
%>
 <html>
  <head>
   <title>管理員修改密碼</title>
   <link href="css/generalstyle.css" type="text/css" rel="stylesheet">
   <script language="javascript">
    function check()
    { 
      if(document.changePwd.adname.value=="")
      {
         alert("請填寫登錄名！！！");
         changePwd.adname.focus();
         return false;
      }
      if(document.changePwd.adpwd.value=="")
      {
         alert("請填寫登陸密碼！！！");
         changePwd.adpwd.focus();
         return false;
      }
      if(document.changePwd.newPwd.value=="")
      {
         alert("請填寫新密碼！！！");
         changePwd.newPwd.focus();
         return false;         
      }
      if(document.changePwd.newPwd.value!=document.changePwd.reNewPwd.value)
       {
         alert("兩次密碼輸入不一樣，請檢查輸入！！！");
         changePwd.newPwd.value="";
         changePwd.reNewPwd.value="";
         changePwd.newPwd.focus();
         return false;
       }
       document.changePwd.submit();
    }
   </script>
  </head>
 <body>
    <%@ include file="admintop.jsp" %>
    <hr width="100%"></hr><br>
    <table align="center" border="0" width="40%">
     <form name="changePwd" action="ListServlet" method="post">
      <tr bgcolor="eeffee">
       <td align="right">登錄名</td>
       <td><input type="text" name="adname" size="20"></td>
      </tr>
      <tr bgcolor="ffeeff">
       <td align="right">登陸密碼</td>
       <td><input type="password" name="adpwd" size="20"></td>
      </tr>
      <tr bgcolor="eeffee">
       <td align="right">新密碼</td>
       <td><input type="password" name="newPwd" size="20"></td>
      </tr>
      <tr bgcolor="ffeeff">
       <td align="right">再次輸入新密碼</td>
       <td><input type="password" name="reNewPwd" size="20"></td>
      </tr>
      <tr bgcolor="eeffee">
       <td></td>
       <td>
        <input type="hidden" name="action" value="changePwd">
        <input type="button" value="修改" onClick="check()">
       </td>
      </tr>
     </form>
    </table>    
 </body>
</html>
