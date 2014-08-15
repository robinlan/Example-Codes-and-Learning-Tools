<%@ page contentType="text/html;charset=big5"
    import="java.util.*,wyf.wyy.*"
%>
 <html>
  <head>
   <title>重置管理員密碼</title>
   <link href="css/generalstyle.css" type="text/css" rel="stylesheet">
   <script language="javascript">
    function check()
    { 
      if(document.resetPwd.adname.value=="")
      {
         alert("請填寫登錄名！！！");
         resetPwd.adname.focus();
         return false;
      }
      if(document.resetPwd.adpwd.value=="")
      {
         alert("請填寫登陸密碼！！！");
         resetPwd.adpwd.focus();
         return false;
      }
      if(document.resetPwd.repwd.value=="")
      {
         alert("請重複密碼！！！");
         resetPwd.repwd.focus();
         return false;
      }
      if(document.resetPwd.adpwd.value!=document.resetPwd.repwd.value)
       {
         alert("兩次密碼輸入不一樣，請檢查輸入！！！");
         resetPwd.adpwd.value="";
         resetPwd.repwd.value="";
         resetPwd.adpwd.focus();
         return false;
       }
       document.resetPwd.submit();
    }
   </script>
  </head>
 <body>
    <%@ include file="admintop.jsp" %>
    <hr width="100%"></hr><br>
    <table align="center" border="0" width="40%">
     <form name="resetPwd" action="ListServlet" method="post">
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
        <input type="hidden" name="action" value="resetPwd">
        <input type="button" value="重置" onClick="check()">
       </td>
      </tr>
     </form>
    </table>    
 </body>
</html>
