<%@ page contentType="text/html;charset=big5"
    import="java.util.*"
%>
 <html>
  <head>
   <title>管理首頁</title>
   <link href="css/generalstyle.css" type="text/css" rel="stylesheet">
   <script language="JavaScript">
    function Check()
    {
       if(document.adlogin.adname.value=="")
       {
         alert("請填寫用戶名！！！");
         adlogin.adname.focus();
         return false;
       }
       if(document.adlogin.pwd.value=="")
       {
         alert("請填寫密碼！！！");
         adlogin.pwd.focus();
         return false;
       }
       document.adlogin.submit();
    }
   </script>
  </head>
 <body>
   <%@ include file="admintop.jsp" %>
	<br><br><br>
	<table align="center" border="0" width="40%" bgcolor="aabbcc">
	 <form  name="adlogin" action="RegAndLoginServlet" method="post">
	     <tr height=20></tr>
		 <tr>
		  <td align="right" width="40%">用戶名:</td>
		  <td colspan="2" width="60%">
		  	 <input type="text" name="adname" size="20">		    
		  </td>
		 </tr>
		  <tr>
		  <td align="right" width="40%">密碼:</td>
		  <td colspan="2" width="60%">
		  	 <input type="password" name="pwd" size="20">		     
		  </td>
		 </tr>
		 <tr>
		  <td align="right">
		   <input type="hidden" name="action" value="adlogin">	
		   <input type="button" value="登錄" onClick="Check()">
		  </td>		 
		  <td align="left">
		   &nbsp;&nbsp;&nbsp;&nbsp;
		   <input type="reset" value="重置">		   
		  </td>
		 </tr>
	  </form>
	 </table>
 </body>
</html>
